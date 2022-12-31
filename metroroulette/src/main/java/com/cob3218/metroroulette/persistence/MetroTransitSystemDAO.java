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

import com.cob3218.metroroulette.model.Station;

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

        JSONArray jsonArr = new JSONArray(str);

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
