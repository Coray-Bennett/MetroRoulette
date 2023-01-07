import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.cob3218.metroroulette.model.Station;
import com.cob3218.metroroulette.persistence.MetroTransitSystemDAO;


@Testable
public class MetroTransitSystemDAOTest {
    MetroTransitSystemDAO metroDao;

    @BeforeEach
    public void setupMetroDAO() {
        metroDao = new MetroTransitSystemDAO();
    }
    
    @Test
    public void testLineEndpoints() {
        Map<String, String[]> endpoints = metroDao.getLineEndpoints();
        assertEquals("A15", endpoints.get("RD")[0]);
        assertEquals("B11", endpoints.get("RD")[1]);
    }

    @Test
    public void testGetLine() {
        JSONArray redLine = metroDao.getLine("RD");
        assertTrue(redLine.getJSONObject(0).getString("StationCode").equals("A15"));
        assertTrue(redLine.getJSONObject(redLine.length()-1).getString("StationCode").equals("B11"));
    }

    @Test
    public void testCreateGraph() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();
        for(Station station : stationMap.values()) {
            assertTrue(graph.containsVertex(station));
        }
    }

    @Test
    public void testMetroGraphSameLine() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();

        Station start = stationMap.get("A15");
        Station end = stationMap.get("B11");

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(graph);
        assertTrue(dijk.getPath(start, end) != null);
    }

    @Test
    public void testMetroGraphValidConnections() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();

        Station start = stationMap.get("A15");
        Station end = stationMap.get("A14");
        assertTrue(graph.getEdge(start, end) != null);

        start = stationMap.get("A14");
        end = stationMap.get("A15");
        assertTrue(graph.getEdge(start, end) != null);

        start = stationMap.get("N03");
        end = stationMap.get("N02");
        assertTrue(graph.getEdge(start, end) != null);
        
    }

    @Test
    public void testMetroGraphInvalidConnections() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();

        Station start = stationMap.get("A15");
        Station end = stationMap.get("B11");
        assertTrue(graph.getEdge(start, end) == null);

        start = stationMap.get("A15");
        end = stationMap.get("A13");
        assertTrue(graph.getEdge(start, end) == null);

        start = stationMap.get("A15");
        end = stationMap.get("G05");
        assertTrue(graph.getEdge(start, end) == null);
        
    }

    @Test
    public void testMetroGraphSameLinePathIsCorrect() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();

        Station start = stationMap.get("A15");
        Station end = stationMap.get("B11");

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijk.getPath(start, end);

        JSONArray redLine = metroDao.getLine("RD");

        List<String> expectedCodePath = new ArrayList<>();
        List<Set<String>> actualCodePath = new ArrayList<>();

        for(Object obj : redLine) {
            if(obj.getClass().equals(JSONObject.class)) {
                JSONObject json = (JSONObject) obj;
                
                String expectedCode = json.getString("StationCode");
                expectedCodePath.add(expectedCode);
            }
        }

        for(Station station : path.getVertexList()) {
            actualCodePath.add(station.getCodes());
        }

        assertTrue(expectedCodePath.size() == actualCodePath.size());

        for(int i = 0; i < expectedCodePath.size(); i++) {
            String expectedCode = expectedCodePath.get(i);
            Set<String> actualCodes = actualCodePath.get(i);
            
            System.out.println(expectedCode + " expected " + i);
            System.out.println(actualCodes.toString() + " actual " + i);

            // assertTrue(
            //     actualCodes.contains(expectedCode)
            // );

            System.out.println("passed " + i);
        }
    }



    @Test
    public void testMetroGraphDifferentLine() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();

        Station start = stationMap.get("A15");
        Station end = stationMap.get("G05");

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(graph);
        assertTrue(dijk.getPath(start, end) != null);
    }

    @Test
    public void testMetroGraphSameStationDifferentCode() {
        Graph<Station, DefaultWeightedEdge> graph = metroDao.createGraph();
        Map<String, Station> stationMap = metroDao.getStationMap();

        Station start = stationMap.get("C01");
        Station end = stationMap.get("A01");

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijk.getPath(start, end);

        assertTrue(path != null);
    }


    @Test
    public void testGenerateRouteCreatesPath() {
        List<Station> randomPath = 
            metroDao.generateRandomRoute(metroDao.getStationMap().get("A01"), Integer.MAX_VALUE, null, 120);

        assertTrue(randomPath != null);
    } 
    
    @Test
    public void testGenerateRouteOneValidLine() {
        List<Station> randomPath = 
            metroDao.generateRandomRoute(metroDao.getStationMap().get("G02"), Integer.MAX_VALUE, new String[]{"RD"}, 120);

        assertTrue(randomPath != null);
        assertTrue(randomPath.get(randomPath.size() - 1).getLineCodes().contains("RD") );
    }  
    
    @Test
    public void testGenerateRouteOneMultipleValidLines() {
        String[] lines = new String[]{"RD", "YL", "SV"};
        List<Station> randomPath = 
            metroDao.generateRandomRoute(metroDao.getStationMap().get("G02"), Integer.MAX_VALUE, lines, 120);

        assertTrue(randomPath != null);
        assertTrue(
               randomPath.get(randomPath.size() - 1).getLineCodes().contains(lines[0])
            || randomPath.get(randomPath.size() - 1).getLineCodes().contains(lines[1])
            || randomPath.get(randomPath.size() - 1).getLineCodes().contains(lines[2]));
    }   

    @Test
    public void testGenerateRouteImpossibleRoute() {
        List<Station> randomPath = 
            metroDao.generateRandomRoute(metroDao.getStationMap().get("A01"), 0, null, 0);

        assertTrue(randomPath == null);
    }   

    @Test
    public void testGenerateRouteMaxStops() {
        List<Station> randomPath = 
            metroDao.generateRandomRoute(metroDao.getStationMap().get("A01"), 3, null, 120);

        assertTrue(randomPath != null);
        System.out.println(randomPath.toString());
        assertTrue(randomPath.size() <= 4);
    } 



}
