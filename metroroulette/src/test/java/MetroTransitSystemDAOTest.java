import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.json.JSONArray;
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

}
