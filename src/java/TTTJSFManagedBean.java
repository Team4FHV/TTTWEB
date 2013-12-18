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
import DTO.objecte.DTOMessage;
import DTO.objecte.DTOTopicData;
import DTO.objecte.DTOVeranstaltung;
import DTO.objecte.DTOVeranstaltungAnzeigen;
import DTO.objecte.DTOVeranstaltungInformation;
import Exceptions.KarteNichtVerfuegbarException;
import Exceptions.SaveFailedException;
import hello.ejb.HelloRemote;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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

    public TTTJSFManagedBean() {
        try {
            sucheVeranstaltungenNachKrieterien();
        } catch (Exception ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setVeranstaltungsID(int veranstaltungsID) {
        this.veranstaltungsID = veranstaltungsID;
        System.out.println("VeranstaltungsID gesetzt: " + veranstaltungsID);
        try {
            getKategorieInfoVonVeranstaltung();
        } catch (RemoteException ex) {
            Logger.getLogger(TTTJSFManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        this.kategorieID = kategorieID;
    }
    
    
    
    
    
    
    
    

    public void getAlleFreieKartenNachKategorie() throws RemoteException {
        DTOKategorieKarte x = null;
        x = bean.getAlleFreieKartenNachKategorie(new DTOKategorienAuswaehlen(kategorieID));
        karten = x.getDTOKarten();
    }

    public void getKategorieInfoVonVeranstaltung() throws RemoteException {
        DTOVeranstaltungAnzeigen temp = new DTOVeranstaltungAnzeigen(veranstaltungsID);
        kategorien = bean.getKategorieInfoVonVeranstaltung(temp);
    }

    public ArrayList<DTOKundenDaten> getKundenListNachNachname(String nachname) throws RemoteException, Exception {
        ArrayList<DTOKundenDaten> x = null;
        x = bean.getKundenListNachNachname(nachname);
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

    public /*ArrayList<DTOVeranstaltungInformation>*/ void sucheVeranstaltungenNachKrieterien() throws Exception {
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

    public ArrayList<DTOTopicData> getTopics() throws RemoteException {
        return bean.getTopics();
    }

    public void publishMessage(DTOMessage message) throws RemoteException {
        bean.publishMessage(message);
    }

    public List<DTOMessage> loadUnpublishedMessages() {
        try {
            return bean.loadUnpublishedMessages();

        } catch (Exception ex) {
            //   Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
