package com.cob3218.metroroulette.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

/**
 * container for info related to a given Metro train station
 * 
 * @author Coray Bennett
 */
public class Station {
    
    private String name;
    private Set<String> codes;
    private Set<String> lineCodes;

    public Station(String name, Set<String> codes, Set<String> lineCodes) {
        this.name = name;
        this.codes = codes;
        this.lineCodes = lineCodes;
    }

    public Station(JSONObject json) {
        this.codes = new HashSet<>();

        this.name = json.getString("Name");
        this.codes.add(json.getString("Code"));
        try {
            this.codes.add(json.getString("StationTogether1"));
            this.codes.add(json.getString("StationTogether2"));
        }
        catch(Exception e) {}

        if(this.codes.contains("")) {
            this.codes.remove("");
        }

        this.lineCodes = new HashSet<>();
        try {
            this.lineCodes.add(json.getString("LineCode1"));
            this.lineCodes.add(json.getString("LineCode2"));
            this.lineCodes.add(json.getString("LineCode3"));
            this.lineCodes.add(json.getString("LineCode4"));
        }
        catch(Exception e) {}

    }

    public String getName() {
        return name;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public Set<String> getLineCodes() {
        return lineCodes;
    }

    public void addLineCodeSet(Set<String> lineCodes) {
        this.lineCodes.addAll(lineCodes);
    }

    @Override
    public boolean equals(Object o) {
        if(! this.getClass().equals(o.getClass())) {
            return false;
        }

        return this.getName().equals(((Station) o).getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public String toString() {
        String str = "";

        str += "Station={name:" + name;

        str += ", codes:[";
        for(String c : codes) {str += c + ",";}
        str = str.substring(0, str.length() - 1) + "]";

        str += ", lineCodes:[";
        for(String c : lineCodes) {str += c + ",";}
        str = str.substring(0, str.length() - 1) + "]}";

        return str;

    }

}
