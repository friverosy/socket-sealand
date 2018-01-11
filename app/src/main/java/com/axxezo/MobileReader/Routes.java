package com.axxezo.MobileReader;

/**
 * Created by axxezo on 15/11/2016.
 */

public class Routes {

    private int ID;
    private String name;
    private String id_mongo;
    private String sailing_date;

    public Routes(int ID, String name, String sailing_date, String id_mongo) {
        this.ID = ID;
        this.name = name;
        this.sailing_date = sailing_date;
        this.id_mongo=id_mongo;
    }

    public Routes(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }
    public Routes(){

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSailing_date() {
        return sailing_date;
    }

    public String getId_mongo() {
        return id_mongo;
    }

    public void setId_mongo(String id_mongo) {
        this.id_mongo = id_mongo;
    }

    public void setSailing_date(String sailing_date) {
        this.sailing_date = sailing_date;
    }

    @Override
    public String toString() {
        return  name;
    }
}
