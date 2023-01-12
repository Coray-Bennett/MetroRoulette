package com.cob3218.metroroulette.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiKey {
    
    @JsonProperty("name") private String name;
    @JsonProperty("value") private String value;

    public ApiKey(@JsonProperty("name") String name, @JsonProperty("value") String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
