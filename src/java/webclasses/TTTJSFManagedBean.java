package webclasses;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DTO.objecte.DTOKarte;
import DTO.objecte.DTOKarteBestellen;
import DTO.objecte.DTOKarteReservieren;
import DTO.objecte.DTOKategorieInformation;
import DTO.objecte.DTOKategorieKarte;
import DTO.objecte.DTOKategorienAuswaehlen;
import DTO.objecte.DTOKundeNeuSpeichern;
import DTO.objecte.DTOKundenDaten;
import DTO.objecte.DTOVeranstaltung;
import DTO.objecte.DTOVeranstaltungAnzeigen;
import DTO.objecte.DTOVeranstaltungInformation;
import Exceptions.KarteNichtVerfuegbarException;
import Exceptions.SaveFailedException;
import hello.ejb.HelloRemote;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Anastasia
 */
@ManagedBean
@SessionScoped
public class TTTJSFManagedBean implements Serializable {

    @EJB
    private HelloRemote bean;

    private String data = null;
    private String ort = null;
    private String kuenstler = null;
    private List<DTOVeranstaltungInformation> veranstaltungsliste;
    private int veranstaltungsID;
    private List<DTOKategorieInformation> kategorien;
    private int kategorieID;
    private List<DTOKarte> karten;
    private List<Object[]> warenkorb = new LinkedList<Object[]>();
    private String customerFirstName;
    private String customerLastName;
    private String customerStreet;
    private String customerHouseNumber;
    private String customerPostcode;
    private String customerCountry;
    private int customerPayingChoice = 0;
    private String transaktioncode;

    public TTTJSFManagedBean() {
        try {
            sucheVeranstaltungenNachKrieterien();
        } catch (Exception ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setVeranstaltungsID(int veranstaltungsID) {
        try {
            this.veranstaltungsID = veranstaltungsID;
            kategorien = new LinkedList<DTOKategorieInformation>();
            karten = new LinkedList<DTOKarte>();
            System.out.println("VeranstaltungsID gesetzt: " + veranstaltungsID);
            try {
                getKategorieInfoVonVeranstaltung();
                if (!kategorien.isEmpty()) {
                    kategorieID = kategorien.get(0).getId();
                    getAlleFreieKartenNachKategorie();
                } else {
                    karten = new LinkedList<>();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<WarenkorbKarte> getWarenkorb() {
        List<WarenkorbKarte> rv = new LinkedList<WarenkorbKarte>();
        for (Object[] temp : warenkorb) {
            DTOKarte k = (DTOKarte) temp[0];
            rv.add(new WarenkorbKarte(k.getID(), k.getReihe(), k.getPlatz(), (double) temp[1]));
        }
        return rv;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setOrt(String ort) {
        this.ort = ort;
        System.out.println(ort);
    }

    public void setKuenstler(String kuenstler) {
        this.kuenstler = kuenstler;
    }

    public String getOrt() {
        return ort;
    }

    public String getKuenstler() {
        return kuenstler;
    }

    public List<DTOVeranstaltungInformation> getVeranstaltungsliste() {
        return veranstaltungsliste;
    }

    public List<DTOKategorieInformation> getKategorien() {
        return kategorien;
    }

    public void setKategorieID(int kategorieID) {
        try {
            this.kategorieID = kategorieID;
            karten = new LinkedList<DTOKarte>();
            System.out.println("KategorieID gesetzt auf: " + kategorieID);
            try {
                getAlleFreieKartenNachKategorie();
            } catch (RemoteException ex) {
                Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<DTOKarte> getKarten() {
        return karten;
    }

    public DTOKategorieInformation getKategorie() {
        for (DTOKategorieInformation k : kategorien) {
            if (k.getId() == kategorieID) {
                return k;
            }
        }
        return null;

    }

    public int getCustomerPayingChoice() {
        return customerPayingChoice;
    }

    public void setCustomerPayingChoice(int customerPayingChoice) {
        this.customerPayingChoice = customerPayingChoice;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public void setCustomerStreet(String customerStreet) {
        this.customerStreet = customerStreet;
    }

    public void setCustomerHouseNumber(String customerHouseNumber) {
        this.customerHouseNumber = customerHouseNumber;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getCustomerStreet() {
        return customerStreet;
    }

    public String getCustomerHouseNumber() {
        return customerHouseNumber;
    }

    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void getAlleFreieKartenNachKategorie() throws RemoteException {
        DTOKategorieKarte x = bean.getAlleFreieKartenNachKategorie(new DTOKategorienAuswaehlen(kategorieID));
        List<DTOKarte> karteInKategorie = x.getDTOKarten();
        List<DTOKarte> verfuegbareKarten = new LinkedList<DTOKarte>();
        for (DTOKarte k : karteInKategorie) {
            int i = 0;
            boolean isInWarenkorb = false;
            while (i < warenkorb.size()) {
                Object[] temp = warenkorb.get(i);
                DTOKarte w = (DTOKarte) temp[0];
                if (k.getID() == w.getID()) {
                    isInWarenkorb = true;
                    break;
                } else {
                    i++;
                }
            }
            if (isInWarenkorb == false) {
                verfuegbareKarten.add(k);
            }
        }
        karten = verfuegbareKarten;
    }

    public void getKategorieInfoVonVeranstaltung() throws RemoteException {
        DTOVeranstaltungAnzeigen temp = new DTOVeranstaltungAnzeigen(veranstaltungsID);
        kategorien = bean.getKategorieInfoVonVeranstaltung(temp);
    }

    public ArrayList<DTOKundenDaten> getKundenListNachNachname(String nachname) throws RemoteException, Exception {
        ArrayList<DTOKundenDaten> x = bean.getKundenListNachNachname(nachname);
        return x;
    }

    public DTOKundenDaten getKundendatenNachID(int id) throws RemoteException, Exception {
        DTOKundenDaten x = null;
        x = bean.getKundendatenNachID(id);
        return x;
    }

    public void reservierungSpeichern(List<DTOKarteReservieren> karten) throws RemoteException, SaveFailedException, Exception, KarteNichtVerfuegbarException {
        bean.reservierungSpeichern(karten);
    }

    public void sucheVeranstaltungenNachKrieterien() throws Exception {
        Date d;
        if (data == null) {
            data = "";
        }
        if (!data.equals("")) {
            SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy");
            d = sdfToDate.parse(data);
        } else {
            d = new Date();
        }
        veranstaltungsliste = bean.sucheVeranstaltungenNachKrieterien(d, ort, kuenstler);
    }

    public void verkaufSpeichern(List<DTOKarteBestellen> karten) throws RemoteException, SaveFailedException, Exception, KarteNichtVerfuegbarException {
        bean.verkaufSpeichern(karten);
    }

    public DTOKategorieInformation getKategorieInfo(int id) throws RemoteException {
        DTOKategorieInformation x = null;
        x = bean.getKategorieInfo(id);
        return x;

    }

    public DTOVeranstaltung getVeranstaltungById(int veranstaltungID) throws Exception {
        DTOVeranstaltung x = null;
        x = bean.getVeranstaltungById(veranstaltungID);
        return x;
    }

    public void neuenKundeSpeichern(DTOKundeNeuSpeichern kunde) throws RemoteException, SaveFailedException {
        bean.neuenKundenSpeichern(kunde);
    }

    public void karteKaufen(int id) {
        try {
            System.out.println("Karte gekauft " + id);
            for (int i = 0; i < karten.size(); i++) {
                if (karten.get(i).getID() == id) {
                    for (DTOKategorieInformation k : kategorien) {
                        if (k.getId() == kategorieID) {
                            double preis = k.getPreis().doubleValue();
                            Object[] temp = {karten.get(i), preis};
                            warenkorb.add(temp);
                            karten.remove(i);
                        }
                    }
                }
            }
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void KarteEntfernen(int id) {
        try {
            System.out.println("Karten entfernt " + id);
            for (int i = 0; i < warenkorb.size(); i++) {
                Object[] temp = warenkorb.get(i);
                DTOKarte k = (DTOKarte) temp[0];
                if (k.getID() == id) {
                    karten.add(k);
                    warenkorb.remove(i);
                }
            }
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double totalPrice() {
        double preis = 0;
        if (warenkorb.isEmpty()) {
            return preis;
        }

        for (Object[] temp : warenkorb) {
            preis = preis + (double) temp[1];
        }
        return preis;
    }

    private void verkaufSpeichern() {
        try {
            if (!warenkorb.isEmpty()) {
                List<DTOKarteBestellen> bestellung = new LinkedList<DTOKarteBestellen>();
                for (Object[] temp : warenkorb) {
                    DTOKarte k = (DTOKarte) temp[0];
                    bestellung.add(new DTOKarteBestellen(k.getID(), 1, false));
                }
                bean.verkaufSpeichern(bestellung);
                warenkorb = new LinkedList<Object[]>();
            }
        } catch (Exception ex) {
            System.out.println("Verkauf Speichern Error: " + ex.getMessage());
        }

    }

    public void transaktionAbschliessen() {
        if (!warenkorb.isEmpty()) {
            System.out.println("Transaktion wird abgeschlossen!");
            Date d = new Date();
            transaktioncode = "" + customerPostcode + "-" + customerHouseNumber + "-" + d.getDay() + "-" + d.getMonth() + "-" + d.getYear() + "-" + d.getMinutes();
            verkaufSpeichern();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Succesfull", "Ihr Transaktionscode:\n " + transaktioncode));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Information", "Ihr Warenkorb enthält keine Elemente. Transaktion wurde nicht ausgeführt."));
        }

    }

    public String getTransaktioncode() {
        return transaktioncode;
    }

}
