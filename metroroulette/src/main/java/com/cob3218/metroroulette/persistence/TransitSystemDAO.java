package com.cob3218.metroroulette.persistence;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.cob3218.metroroulette.model.Station;

/**
 * defines the methods necessary for a given Transit System to function in the application
 * 
 * @author Coray Bennett
 */
public interface TransitSystemDAO {

    /**
     * 
     * @return a map relating each station code to it's corresponding station
     */
    Map<String, Station> getStationMap();

    /**
     * Creates a weighted graph using JGraphT that represents all connections between
     * different stations and the distances between them
     * @return Graph<Station, DefaultWeightedEdge> representing a transit system
     * @throws InterruptedException
     */
    Graph<Station, DefaultWeightedEdge> createGraph() throws InterruptedException;

    /**
     * Using the weighted graph of a Transit System, generates a random valid route
     * based on the following parameters. If no valid route exists, returns null.
     * @param start the Station that you are currently at (start of path)
     * @param maxStops the maximum number of stops away your destination can be (length of path)
     * @param selectedLines the lines that your destination can end on (optional, can be null)
     * @param maxLengthMinutes the maximum (approximate) minutes you would like the route to take (travel time)
     * @return List<Station> representing the randomly generated path from start to finish
     */
    List<Station> generateRandomRoute(Station start, int maxStops, String[] selectedLines, 
        int maxLengthMinutes);

}
