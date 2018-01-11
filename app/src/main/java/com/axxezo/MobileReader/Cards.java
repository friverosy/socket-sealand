package com.axxezo.MobileReader;

/**
 * Created by jmora on 22/11/2016.
 */

public class Cards {
    private String document;
    private String name;
    private String nationality;
    private String destination;
    private int isInside;
    private boolean manual_sell;

    public boolean isManual_sell() {
        return manual_sell;
    }

    public void setManual_sell(boolean manual_sell) {
        this.manual_sell = manual_sell;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    private String origin;

    public Cards(String document, String name, int isInside, String origin,String destination) {
        this.document = document;
        this.name = name;
        this.isInside = isInside;
        this.destination = destination;
        this.origin=origin;
    }
    public Cards(){

    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getIsInside() {
        return isInside;
    }

    public void setIsInside(int isInside) {
        this.isInside = isInside;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "document='" + document + '\'' +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", isInside=" + isInside +
                '}';
    }
}
