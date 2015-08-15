package com.dmt.train.routing;

import java.util.Optional;

/**
 * Application service to calculates distances of routes.
 * 
 * @author diegomtassis
 */
public interface RouteDistanceApplicationService extends ApplicationService {

	/**
	 * Calculates the distance of a route.
	 * 
	 * @param routePath
	 *            string representing a route. Each character represents a point
	 *            in the route.
	 * @return distance of the route.
	 */
	Optional<Integer> distance(String routePath);
}
