package com.cob3218.metroroulette.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonStation {
    
    @JsonProperty("name") String name;
    @JsonProperty("codes") String codes;
    @JsonProperty("lineCodes") String lineCodes;

    public JsonStation(Station station) {
        this.name = station.getName();

        this.codes = "";
        for(String code : station.getCodes()) {
            this.codes += code + ",";
        }
        this.codes = this.codes.substring(0, this.codes.length() - 1);

        this.lineCodes = "";
        for(String lineCode : station.getLineCodes()) {
            this.lineCodes += lineCode + ",";
        }
        this.lineCodes = this.lineCodes.substring(0, this.lineCodes.length() - 1);
    }   

}
