import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.cob3218.metroroulette.model.Station;

@Testable
public class StationTest {
    
    @Test
    public void testStationEquals() {

        Set<String> codesA = new HashSet<>();
        Set<String> codesB = new HashSet<>();
        Set<String> lineCodesA = new HashSet<>();
        Set<String> lineCodesB = new HashSet<>();

        codesA.add("A01");
        codesB.add("C01");

        lineCodesA.add("RD");
        lineCodesB.add("OR");

        Station a = new Station("Metro Center", codesA, lineCodesA);
        Station b = new Station("Metro Center", codesB, lineCodesB);

        assertTrue(a.equals(b));

        a.addLineCodeSet(lineCodesB);
        b.addLineCodeSet(lineCodesA);

        assertArrayEquals(a.getLineCodes().toArray(), b.getLineCodes().toArray());

    }

}
