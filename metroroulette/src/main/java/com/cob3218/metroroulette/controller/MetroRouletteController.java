package com.cob3218.metroroulette.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cob3218.metroroulette.model.JsonStation;
import com.cob3218.metroroulette.model.Station;
import com.cob3218.metroroulette.persistence.TransitSystemDAO;

@RestController
@RequestMapping("MetroRoulette")
public class MetroRouletteController {
    private static final Logger LOG = Logger.getLogger(MetroRouletteController.class.getName());
    private TransitSystemDAO transitDao;
    private Map<String, Station> stationMap;

    public MetroRouletteController(TransitSystemDAO transitDao) {
        this.transitDao = transitDao;
        this.stationMap = transitDao.getStationMap();
    }
    
    /**
     * Responds to the GET request with a random Station path that fits the specified parameters
     * 
     * @param startCode the code used to find the {@link Station station} at the start of the path
     * @param maxStops the maximum number of stops after the start (max length is maxStops + 1)
     * @param selectedLines CSV (comma separated value) String of the lines that the destination can be on
     * @param maxLength the maximum approx. travel time in minutes the route can take
     * @return returns a {@link ResponseEntity ResponseEntity} object containing an array of {@link JsonStation JsonStation},
     *      json compatible Station objects
     */
    @GetMapping("/")
    public ResponseEntity<JsonStation[]> getRandomRoute(@RequestParam String startCode, 
        @RequestParam int maxStops, @RequestParam(required = false) List<String> selectedLines, @RequestParam int maxLength) {
            LOG.info("http://localhost:8080/MetroRoulette/?startCode=" + startCode
                        + "&maxStops=" + maxStops + "&selectedLines=" + selectedLines + "&maxLength=" + maxLength);
        try {
            Station start = stationMap.get(startCode);
            String[] selectedArray = null;
            if(selectedLines != null) {
                selectedArray = selectedLines.toArray(new String[selectedLines.size()]);
            }

            List<Station> path = 
                transitDao.generateRandomRoute(start, maxStops, selectedArray, maxLength);

            if(path == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<JsonStation> jsonStationList = new ArrayList<>();
            for(Station station : path) {
                jsonStationList.add(new JsonStation(station));
            }

            return new ResponseEntity<JsonStation[]>(
                jsonStationList.toArray(new JsonStation[jsonStationList.size()]), HttpStatus.OK);
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
