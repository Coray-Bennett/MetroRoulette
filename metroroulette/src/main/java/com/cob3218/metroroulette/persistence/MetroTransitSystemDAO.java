package com.cob3218.metroroulette.persistence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.graph.AsWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Multigraph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.cob3218.metroroulette.model.Station;

@Component
public class MetroTransitSystemDAO implements TransitSystemDAO {

    public List<Station> getLine(String lineCode) {

        List<Station> line = new LinkedList<>();
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("https://api.wmata.com/Rail.svc/json/jStations?LineCode=" + lineCode)).header("api_key", "9960c138fb2e48fcbb0e6bf41ce573db")
         .build();
        String str = client.sendAsync(request, BodyHandlers.ofString())
         .join()
         .body(); 

        //removes leading and end text/brackets to create json array with [] at start/end
        while(str.charAt(0) != '[') {
            str = str.substring(1, str.length());
        }
        str = str.substring(0, str.length() - 1);

        JSONArray jsonArr = new JSONArray(str);

        for(Object json : jsonArr) {
            if(json.getClass().equals(JSONObject.class)) {
                line.add(new Station((JSONObject) json));
            }
        }

        return line;

    }

    public List<List<Station>> getAllLines() {
        final String[] lineCodes = {"RD", "BL", "YL", "OR", "GR", "SV"};

        List<List<Station>> lines = new ArrayList<List<Station>>();

        for(String lineCode : lineCodes) {
            


        }

        return lines;
    }

    @Override
    public Graph<Station, Integer> createGraph(List<List<Station>> lines) {
        
        Graph<Station, DefaultWeightedEdge> metroGraph = new Multigraph<>(DefaultWeightedEdge.class);



        return null;
    }

    @Override
    public List<Station> generateRandomRoute(int maxStops, List<String> selectedLines, int maxLengthMinutes) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
