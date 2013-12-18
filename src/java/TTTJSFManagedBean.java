/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import DTO.objecte.DTOKarteBestellen;
import DTO.objecte.DTOKarteReservieren;
import DTO.objecte.DTOKategorieInformation;
import DTO.objecte.DTOKategorieKarte;
import DTO.objecte.DTOKategorienAuswaehlen;
import DTO.objecte.DTOKundeNeuSpeichern;
import DTO.objecte.DTOKundenDaten;
import DTO.objecte.DTOLoginDaten;
import DTO.objecte.DTOMessage;
import DTO.objecte.DTORollenList;
import DTO.objecte.DTOTopicData;
import DTO.objecte.DTOVeranstaltung;
import DTO.objecte.DTOVeranstaltungAnzeigen;
import DTO.objecte.DTOVeranstaltungInformation;
import Exceptions.BenutzerNichtInDBException;
import Exceptions.FalschesPasswordExeption;
import Exceptions.KarteNichtVerfuegbarException;
import Exceptions.SaveFailedException;
import hello.ejb.HelloRemote;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;

/**
 *
 * @author Anastasia
 */
@ManagedBean
@SessionScoped
public class TTTJSFManagedBean implements Serializable{
    
@EJB
    private HelloRemote bean;  

    public TTTJSFManagedBean() {
    }
    private String data;
    private String ort;
    private String kuenstler;
    DTORollenList _userRollen;
    String username;
    List<DTOMessage> messages = new LinkedList();

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
    
    
       public DTOKategorieKarte getAlleFreieKartenNachKategorie(DTOKategorienAuswaehlen kat) throws RemoteException {
        DTOKategorieKarte x = null;
        x = bean.getAlleFreieKartenNachKategorie(kat);
        return x;
    }

    public ArrayList<DTOKategorieInformation> getKategorieInfoVonVeranstaltung(DTOVeranstaltungAnzeigen v) throws RemoteException {
        ArrayList<DTOKategorieInformation> x = null;
        x = bean.getKategorieInfoVonVeranstaltung(v);
        return x;
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

    public ArrayList<DTOVeranstaltungInformation> sucheVeranstaltungenNachKrieterien() throws Exception {
        ArrayList<DTOVeranstaltungInformation> x = null;
        
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//	
//	Date da = sdf.parse(data);
        
        x = bean.sucheVeranstaltungenNachKrieterien(new Date(), ort, kuenstler);
        return x;
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

    public DTORollenList login(DTOLoginDaten l) throws RemoteException,
            BenutzerNichtInDBException, FalschesPasswordExeption {
        _userRollen = bean.login(l);
        username = l.getUsername(); 
        return _userRollen;
    }

    public DTORollenList getUserRollen() {
        return _userRollen;
    }

    public void clearRoles() {
        _userRollen = null;
    }

    public DTOMessage getFirstMessage() {
        if (messages.size() > 0) {
            return messages.get(0);
        }
        return null;
    }

    public void addMessageToClient(DTOMessage m) {
        messages.add(m);
//        if (MainGuiCtrl.getVeranstaltungSuchenView() != null) {
//            MainGuiCtrl.getVeranstaltungSuchenView().checkMessages();
//        }
    }

    public void removeFirstMessage() {
        if (messages.size() > 0) {
            messages.remove(0);
        }
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
    
    public void startListenToMessages() throws RemoteException, NamingException{
         ArrayList<DTOTopicData> topics = bean.getTopicsVonBenutzer(username);
         for (DTOTopicData topic :topics){
            System.out.println("topic  " + topic.getName());
//            Subscriber s = new Subscriber(topic.getName(), this);
//              s.subscribe();
         }
    }
}

    
    
    

