package com.dmt.train.routing;

import java.util.Collection;

/**
 * Finds all the possible trips between 2 cities.
 * 
 * @author diegomtassis
 */
public interface TripFinder extends DomainService {

	/**
	 * Calculates the different routes between 2 cities, specifying a condition
	 * on the number of stops of the routes.
	 * 
	 * @param startingPoint
	 * @param endPoint
	 * @param minStops
	 * @param maxStops
	 * @return collection of routes fulfilling the request
	 */
	Collection<Route> find(String startingPoint, String endPoint, Integer minStops, Integer maxStops);

	/**
	 * Calculates the different routes between 2 cities, specifying a condition
	 * on the distance of the routes.
	 * 
	 * @param startingPoint
	 * @param endPoint
	 * @param maxDistance
	 * @return collection of routes fulfilling the request
	 */
	Collection<Route> find(String startingPoint, String endPoint, Integer maxDistance);

	/**
	 * Finds the shortest route (in terms of distance) between 2 cities.
	 * 
	 * @param startingPoint
	 * @param endPoint
	 * @return collections containing the shortest routes between 2 cities (when
	 *         more than one route is part of the result it means all of them
	 *         have are the same distance)
	 */
	Collection<Route> find(String startingPoint, String endPoint);
}
