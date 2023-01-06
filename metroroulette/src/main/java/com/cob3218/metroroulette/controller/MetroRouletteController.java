package com.cob3218.metroroulette.controller;

import java.net.http.HttpResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cob3218.metroroulette.model.JsonStation;
import com.cob3218.metroroulette.persistence.TransitSystemDAO;

@RestController
@RequestMapping("MetroRoulette")
public class MetroRouletteController {
    private TransitSystemDAO transitDao;

    public MetroRouletteController(TransitSystemDAO transitDao) {
        this.transitDao = transitDao;
    }
    
    @GetMapping("/")
    public HttpResponse<JsonStation[]> getRandomRoute() {


        return null;
    }

}
