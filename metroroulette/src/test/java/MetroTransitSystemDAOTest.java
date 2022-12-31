import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

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

}
