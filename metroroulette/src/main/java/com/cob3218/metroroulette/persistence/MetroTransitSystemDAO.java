package com.cob3218.metroroulette.persistence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
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
                //finds any identical station (shared codes) and makes sure they list all line codes
                for(Station s : stationMap.values()) {
                    if(station.equals(s)) {
                        station.addLineCodeSet(s.getLineCodes());
                        s.addLineCodeSet(station.getLineCodes());
                        break;
                    }
                }

                stationMap.put(code, station);
            }
        }

        return stationMap;
    }

    public Map<String, Station> getStationMap() {
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

        List<JSONArray> lines = new ArrayList<JSONArray>();

        for(String lineCode : lineCodes) {
            lines.add(getLine(lineCode));
        }

        return lines;
    }

    @Override
    public Graph<Station, DefaultWeightedEdge> createGraph() {
        
        Graph<Station, DefaultWeightedEdge> metroGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<JSONArray> lines = getAllLines();

        for(JSONArray line : lines) {
            JSONObject last = null;
            for(Object obj : line) {
                if(obj.getClass().equals(JSONObject.class)) {
                    JSONObject json = (JSONObject) obj;
                    String code = json.getString("StationCode");

                    if(last == null) {
                        last = json;
                        continue;
                    }

                    String lastCode = last.getString("StationCode");
                    last = json;

                    Station station = stationMap.get(code);
                    Station lastStation = stationMap.get(lastCode);

                    int weight = json.getInt("DistanceToPrev");
                    
                    if(!metroGraph.containsVertex(station)) {
                        metroGraph.addVertex(station);
                    }

                    if(!metroGraph.containsVertex(lastStation)) {
                        metroGraph.addVertex(lastStation);
                    }

                    DefaultWeightedEdge edge = metroGraph.addEdge(station, lastStation);
                    metroGraph.setEdgeWeight(edge, weight);

                }
            }
        }

        return metroGraph;
    }

    @Override
    public List<Station> generateRandomRoute(int maxStops, List<String> selectedLines, int maxLengthMinutes) {
        // TODO Auto-generated method stub
        return null;
    }

    // public static void main(String[] args) {
    //     MetroTransitSystemDAO metroDao = new MetroTransitSystemDAO();
    // }
    
}
