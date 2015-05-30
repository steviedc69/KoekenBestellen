package com.example.steven.koekenbestellen.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steven on 5/02/15.
 */
public class Bestelling implements Parcelable {
    private String naam;
    private String voornaam;
    private String afleveringAdres;
    private int aantalVanille;
    private int aantalChoco;
    private int aantalFranchi;
    private boolean betaald;
    private String gsm;
    private int id;

    public Bestelling(Parcel in)
    {
        id = in.readInt();
        naam = in.readString();
        voornaam = in.readString();
        afleveringAdres = in.readString();
        gsm = in.readString();
        aantalChoco = in.readInt();
        aantalVanille = in.readInt();
        aantalFranchi = in.readInt();
        int b = in.readInt();
        if(b == 1)
        {
            setBetaald(true);
        }
        else
        {
            setBetaald(false);
        }
    }

    public Bestelling()
    {
        betaald = false;
    }

    public Bestelling(String naam, String voornaam, String afleveringAdres, int aantalVanille, int aantalChoco, int aantalFranchi,boolean betaald) {
        this.naam = naam;
        this.voornaam = voornaam;
        this.afleveringAdres = afleveringAdres;
        this.aantalVanille = aantalVanille;
        this.aantalChoco = aantalChoco;
        this.aantalFranchi = aantalFranchi;
        this.betaald = betaald;
    }

    public String getNaam() {
        return naam;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAfleveringAdres() {
        return afleveringAdres;
    }

    public void setAfleveringAdres(String afleveringAdres) {
        this.afleveringAdres = afleveringAdres;
    }

    public int getAantalVanille() {
        return aantalVanille;
    }

    public void setAantalVanille(int aantalVanille) {
        this.aantalVanille = aantalVanille;
    }

    public int getAantalChoco() {
        return aantalChoco;
    }

    public void setAantalChoco(int aantalChoco) {
        this.aantalChoco = aantalChoco;
    }

    public int getAantalFranchi() {
        return aantalFranchi;
    }

    public void setAantalFranchi(int aantalFranchi) {
        this.aantalFranchi = aantalFranchi;
    }

    public void setBetaald(boolean betaald) {
        this.betaald = betaald;
    }

    public boolean isBetaald()
    {
        return this.betaald;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(naam);
        dest.writeString(voornaam);
        dest.writeString(afleveringAdres);
        dest.writeString(gsm);
        dest.writeInt(aantalChoco);
        dest.writeInt(aantalVanille);
        dest.writeInt(aantalFranchi);
        int b = 0;
        if(isBetaald())
        {
            b = 1;
        }
        dest.writeInt(b);
    }
    public static final Creator<Bestelling> CREATOR = new Creator<Bestelling>() {
        @Override
        public Bestelling createFromParcel(Parcel source) {
            return new Bestelling(source);
        }

        @Override
        public Bestelling[] newArray(int size) {
            return new Bestelling[size];
        }
    };
}



