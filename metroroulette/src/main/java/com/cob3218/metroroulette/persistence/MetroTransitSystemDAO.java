package com.cob3218.metroroulette.persistence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cob3218.metroroulette.model.Station;

/**
 * Implementation of TransitSystemDAO that represents the Washington DC Metro
 * 
 * @author Coray Bennett
 */
@Component
public class MetroTransitSystemDAO implements TransitSystemDAO {

    private Map<String, String[]> lineEndpoints;
    private Map<String, Station> stationMap;
    private Graph<Station, DefaultWeightedEdge> metroGraph;

    private String stationsJSON;
    private String linesJSON;
    
    @Value("${api_key}")
    private String api_key;

    public final String[] LINE_CODES = {"RD", "BL", "YL", "OR", "GR", "SV"};

    public MetroTransitSystemDAO(@Value("${api_key}") String api_key) throws InterruptedException {
        this.api_key = api_key;
        stationsJSON = HttpGetRequest("https://api.wmata.com/Rail.svc/json/jStations");
        linesJSON = HttpGetRequest("https://api.wmata.com/Rail.svc/json/jLines");
        lineEndpoints = lineEndpoints();
        stationMap = stationMap();
        metroGraph = createGraph();
    }

    /* helper functions */
    
    //make http requests
    private String HttpGetRequest(String uri) throws InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create(uri)).header("api_key", api_key)
         .build();
        
        String str = client.sendAsync(request, BodyHandlers.ofString())
         .join()
         .body();
        
        JSONObject json = new JSONObject(str);
        try {
            Object statusCode = json.get("statusCode");
            if(statusCode.equals(429)) {
                Thread.sleep(1000);
                return HttpGetRequest(uri);
            }
        }
        catch(JSONException e) {}
        return str;
    }

    //removes leading and end text/brackets to create json array with [] at start/end
    private JSONArray parseJSONArray(String str) {

        while(str.charAt(0) != '[') {
            str = str.substring(1, str.length());
        }
        str = str.substring(0, str.length() - 1);

        JSONArray jsonArr = new JSONArray(str);
        return jsonArr;
    }

    /* implementation logic */

    /**
     * creates a map relating station codes to their respective Stations
     * @return Map<String, Station>
     */
    private Map<String, Station> stationMap() {
        Map<String, Station> stationMap = new HashMap<>();

        String str = stationsJSON;
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

    /*
     * functionally dependent methods, in order: 
     * 
     * 1. lineEndpoints()
     * 2. getLine() (depends on 1)
     * 3. getAllLines() (depends on 2) 
     * 4. createGraph() (depends on 3)
     * 5. generateRandomRoute(...) (depends on 4)
     * 
     * methods 1 and 4 are called upon object construction, 
     * and ensures that the methods are called in the proper order
    */


    /**
     * maps each line code to the station codes that begin and end each line
     * @return Map<String, String[]>
     */
    private Map<String, String[]> lineEndpoints() {
       Map<String, String[]> endpoints = new HashMap<>();
        
       String str = linesJSON;
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

    /**
     * gets a line in order of physical location from start to end
     * @param lineCode
     * @return JSONArray from WPA API
     * @throws InterruptedException
     */
    public JSONArray getLine(String lineCode) throws InterruptedException {

        String[] endpoints = lineEndpoints.get(lineCode);

        String str = 
            HttpGetRequest("https://api.wmata.com/Rail.svc/json/jPath?FromStationCode=" 
                + endpoints[0] + "&ToStationCode=" + endpoints[1]);

        JSONArray jsonArr = parseJSONArray(str);

        return jsonArr;

    }

    /**
     * performs getLine calls on all pre-defined line codes
     * @return List<JSONArray>: a list of all JSONArray objects from WPA API
     * @throws InterruptedException
     */
    public List<JSONArray> getAllLines() throws InterruptedException {

        List<JSONArray> lines = new ArrayList<JSONArray>();

        for(String lineCode : LINE_CODES) {
            JSONArray line = getLine(lineCode);
            lines.add(line);
        }

        return lines;
    }

    @Override
    public Graph<Station, DefaultWeightedEdge> createGraph() throws InterruptedException {
        
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
    public List<Station> generateRandomRoute(Station start, int maxStops, String[] selectedLines, int maxLengthMinutes) {
        final double AVG_SPEED_MPH = 33;
        final double FT_IN_MILE = 5280;

        if(!metroGraph.containsVertex(start)) {
            return null;
        }

        if(selectedLines == null) {
            selectedLines = LINE_CODES;
        }   

        Object[] stations = stationMap.values().toArray();
        Random random = new Random();
        Set<Station> validStations = new HashSet<>();

        for(Object obj : stations) {
            Station station = (Station) obj;

            for(String line : selectedLines) {
                if(station.getLineCodes().contains(line)) {
                    validStations.add(station);
                    break;
                }
            }
        }

        validStations.remove(start);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(metroGraph);
        GraphPath<Station, DefaultWeightedEdge> path = null;

        int pathLengthMinutes = Integer.MAX_VALUE;
        int waitTime = 0;
        
        while(!validStations.isEmpty() 
        && (path == null || path.getLength() > maxStops || pathLengthMinutes > maxLengthMinutes) )  {
            
            Station destination = (Station) validStations.toArray()[random.nextInt(validStations.size())];
            if(!metroGraph.containsVertex(destination)) {
                validStations.remove(destination);
                continue;
            }
            path = dijk.getPath(start, destination);

            //a change in the current lines you are on implies you will be waiting for another train car
            //we must add wait time to accurately approximate total time to reach destination
            waitTime = 0;
            for(DefaultWeightedEdge edge : path.getEdgeList()) {
                Station stationA = metroGraph.getEdgeSource(edge);
                Station stationB = metroGraph.getEdgeTarget(edge);

                Set<String> intersection = new HashSet<>(stationA.getLineCodes());
                if(intersection.retainAll(stationB.getLineCodes())) {
                    waitTime += 5;
                }
            }

            waitTime += path.getVertexList().size(); //approx. 1min wait per stop at station
            pathLengthMinutes = waitTime + (int) Math.ceil(1 / ( (AVG_SPEED_MPH * FT_IN_MILE / 60) / path.getWeight() ));

            validStations.remove(destination);
        }

        if(path == null || (validStations.isEmpty() && (path.getLength() > maxStops || pathLengthMinutes > maxLengthMinutes)) ) {
            return null;
        }

        return path.getVertexList();
    }

    /* getters */

    public Map<String, Station> getStationMap() {
        return stationMap;
    }
    
    public Map<String, String[]> getLineEndpoints() {
        return lineEndpoints;
    }

    
}
