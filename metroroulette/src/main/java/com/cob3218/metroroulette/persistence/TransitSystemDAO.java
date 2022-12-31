package com.cob3218.metroroulette.persistence;

import java.util.List;

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
     * Creates a weighted graph using JGraphT that represents all connections between
     * different stations and the distances between them
     * @param lines
     * @return
     */
    Graph<Station, DefaultWeightedEdge> createGraph();

    List<Station> generateRandomRoute(int maxStops, List<String> selectedLines, 
        int maxLengthMinutes);

}
