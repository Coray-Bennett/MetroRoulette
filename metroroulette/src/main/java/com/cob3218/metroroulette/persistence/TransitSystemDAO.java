package com.cob3218.metroroulette.persistence;

import java.util.List;

import org.jgrapht.Graph;
import org.json.JSONObject;

import com.cob3218.metroroulette.model.Station;

public interface TransitSystemDAO {

    Graph<Station, Integer> createGraph(List<List<Station>> lines);

    List<Station> generateRandomRoute(int maxStops, List<String> selectedLines, 
        int maxLengthMinutes);

}
