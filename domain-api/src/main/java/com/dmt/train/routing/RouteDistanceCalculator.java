package com.dmt.train.routing;

import java.util.Optional;

/**
 * Calculates distances of routes.
 * 
 * @author diegomtassis
 */
public interface RouteDistanceCalculator extends DomainService {

	/**
	 * Calculates the distance of a route.
	 * 
	 * @param route
	 *            the route to calculate its distance.
	 * @return distance of the route
	 */
	Optional<Integer> distance(Route route);
}
