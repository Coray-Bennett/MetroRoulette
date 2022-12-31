package com.cob3218.metroroulette.persistence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Multigraph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.cob3218.metroroulette.model.Station;

@Component
public class MetroTransitSystemDAO implements TransitSystemDAO {

    private Map<String, String[]> lineEndpoints;
    private Map<String, Station> stationMap;

    public MetroTransitSystemDAO() {
        lineEndpoints = lineEndpoints();
        stationMap = stationMap();
    }
    
    //helper function to make http requests
    public String HttpGetRequest(String uri) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create(uri)).header("api_key", "9960c138fb2e48fcbb0e6bf41ce573db")
         .build();
        
        String str = client.sendAsync(request, BodyHandlers.ofString())
         .join()
         .body(); 

        return str;
    }

    public JSONArray parseJSONArray(String str) {
        //removes leading and end text/brackets to create json array with [] at start/end
        while(str.charAt(0) != '[') {
            str = str.substring(1, str.length());
        }
        str = str.substring(0, str.length() - 1);

        JSONArray jsonArr = new JSONArray(str);
        return jsonArr;
    }

    private Map<String, String[]> lineEndpoints() {
       Map<String, String[]> endpoints = new HashMap<>();
        
       String str = HttpGetRequest("https://api.wmata.com/Rail.svc/json/jLines");
       JSONArray jsonArr = parseJSONArray(str);

       for(Object obj : jsonArr) {
        if(obj.getClass().equals(JSONObject.class)) {
            JSONObject json = (JSONObject) obj;
            String key = json.getString("LineCode");
            String[] value = {
                json.getString("StartStationCode"),
                json.getString("EndStationCode")
            };
            endpoints.put(key, value);
        }
       }

       return endpoints;
    }

    public Map<String, String[]> getLineEndpoints() {
        return lineEndpoints;
    }

    
    private Map<String, Station> stationMap() {
        Map<String, Station> stationMap = new HashMap<>();

        String str = HttpGetRequest("https://api.wmata.com/Rail.svc/json/jStations");
        JSONArray jsonArr = parseJSONArray(str);

        for(Object obj : jsonArr) {
            if(obj.getClass().equals(JSONObject.class)) {
                JSONObject json = (JSONObject) obj;
                String code = json.getString("Code");
                Station station = new Station(json);
            }
        }

        return stationMap;
    }

    public JSONArray getLine(String lineCode) {

        String[] endpoints = lineEndpoints.get(lineCode);

        String str = 
            HttpGetRequest("https://api.wmata.com/Rail.svc/json/jPath?FromStationCode=" 
                + endpoints[0] + "&ToStationCode=" + endpoints[1]);

        JSONArray jsonArr = parseJSONArray(str);

        return jsonArr;

    }

    public List<JSONArray> getAllLines() {
        final String[] lineCodes = {"RD", "BL", "YL", "OR", "GR", "SV"};

        List<List<Station>> lines = new ArrayList<List<Station>>();

        for(String lineCode : lineCodes) {
            


        }

        return null;
    }

    @Override
    public Graph<Station, DefaultWeightedEdge> createGraph(List<List<Station>> lines) {
        
        Graph<Station, DefaultWeightedEdge> metroGraph = new Multigraph<>(DefaultWeightedEdge.class);



        return null;
    }

    @Override
    public List<Station> generateRandomRoute(int maxStops, List<String> selectedLines, int maxLengthMinutes) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
