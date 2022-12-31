import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.cob3218.metroroulette.model.Station;
import com.cob3218.metroroulette.persistence.MetroTransitSystemDAO;

@Testable
public class MetroTransitSystemDAOTest {
    

    @Test
    public void testGetLine() {
        MetroTransitSystemDAO metroDao = new MetroTransitSystemDAO();
        List<Station> redLine = metroDao.getLine("RD");
        assertTrue(redLine.get(0).getCode().equals("B11"));
        assertTrue(redLine.get(redLine.size() - 1).getCode().equals("A11"));
    }

}
