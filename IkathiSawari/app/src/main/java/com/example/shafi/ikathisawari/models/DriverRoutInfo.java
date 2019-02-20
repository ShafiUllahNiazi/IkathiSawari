package com.example.shafi.ikathisawari.models;

import java.util.HashMap;
import java.util.List;

public class DriverRoutInfo {
    private List<List<HashMap<String, String>>> routes;

    public DriverRoutInfo(){

    }

    public DriverRoutInfo(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "DriverRoutInfo{" +
                "routes=" + routes +
                '}';
    }
}
