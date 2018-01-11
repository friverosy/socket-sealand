package com.axxezo.MobileReader;

/**
 * Created by axxezo on 17/11/2016.
 */

public class Ports {
    private String id_mongo;
    private int id_api;
    private String name;

    public Ports(String id_mongo, int id_api, String name) {
        this.id_mongo = id_mongo;
        this.id_api = id_api;
        this.name = name;
    }

    public String getId_mongo() {
        return id_mongo;
    }

    public void setId_mongo(String id_mongo) {
        this.id_mongo = id_mongo;
    }

    public int getId_api() {
        return id_api;
    }

    public void setId_api(int id_api) {
        this.id_api = id_api;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ports{" +
                "id_mongo='" + id_mongo + '\'' +
                ", id_api=" + id_api +
                ", name='" + name + '\'' +
                '}';
    }
}
