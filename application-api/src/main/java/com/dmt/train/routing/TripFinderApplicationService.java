package com.dmt.train.routing;

import java.util.Collection;
import java.util.Optional;

/**
 * Application service to find all the possible trips between 2 cities.
 * 
 * @author diegomtassis
 */
public interface TripFinderApplicationService extends ApplicationService {

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
	Collection<String> find(String startingPoint, String endPoint, Integer minStops, Integer maxStops);

	/**
	 * Calculates the different routes between 2 cities, specifying a condition
	 * on the distance of the routes.
	 * 
	 * @param startingPoint
	 * @param endPoint
	 * @param maxDistance
	 * @return collection of routes fulfilling the request
	 */
	Collection<String> find(String startingPoint, String endPoint, Integer maxDistance);

	/**
	 * Finds the shortest route (in terms of distance) between 2 cities.
	 * 
	 * @param startingPoint
	 * @param endPoint
	 * @return length of the shortest route between 2 cities. If there is not
	 *         route then an empty optional is returned.
	 */
	Optional<Integer> find(String startingPoint, String endPoint);
}
