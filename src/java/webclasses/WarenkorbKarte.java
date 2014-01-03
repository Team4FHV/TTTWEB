package webclasses;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefan Dietrich
 */
public class WarenkorbKarte {

    private int ID;
    private String reihe;
    private int platz;
    private double preis;

    public WarenkorbKarte(int ID, String reihe, int platz, double preis) {
        this.ID = ID;
        this.reihe = reihe;
        this.platz = platz;
        this.preis = preis;
    }

    public int getID() {
        return ID;
    }

    public String getReihe() {
        return reihe;
    }

    public int getPlatz() {
        return platz;
    }

    public double getPreis() {
        return preis;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setReihe(String reihe) {
        this.reihe = reihe;
    }

    public void setPlatz(int platz) {
        this.platz = platz;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
}
