package com.cob3218.metroroulette.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * container for info related to a given Metro train station
 * 
 * @author Coray Bennett
 */
public class Station {
    
    private String name;
    private String code;
    private List<String> altCodes;
    private List<String> lineCodes;

    public Station(String name, String code, List<String> altCodes, List<String> lineCodes) {
        this.name = name;
        this.code = code;
        this.altCodes = altCodes;
        this.lineCodes = lineCodes;
    }

    public Station(JSONObject json) {
        this.name = json.getString("Name");
        this.code = json.getString("Code");

        this.altCodes = new ArrayList<>();
        try {
            this.altCodes.add(json.getString("StationTogether1"));
            this.altCodes.add(json.getString("StationTogether2"));
        }
        catch(Exception e) {}
        

        this.lineCodes = new ArrayList<>();
        try {
            this.lineCodes.add(json.getString("LineCode1"));
            this.lineCodes.add(json.getString("LineCode2"));
            this.lineCodes.add(json.getString("LineCode3"));
            this.lineCodes.add(json.getString("LineCode4"));
        }
        catch(Exception e) {}

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<String> getAltCodes() {
        return altCodes;
    }

    public List<String> getLineCodes() {
        return lineCodes;
    }

    @Override
    public String toString() {
        String str = "";

        str += "Station={name:" + name;

        str += ", code:" + code;

        str += ", altCodes:[";
        for(String c : altCodes) {str += c + ",";}
        str = str.substring(0, str.length() - 2) + "]";

        str += ", lineCodes:[";
        for(String c : lineCodes) {str += c + ",";}
        str = str.substring(0, str.length() - 1) + "]}";

        return str;

    }

}
