package com.dmt.train.routing;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository for direct routes between cities.
 * 
 * @author diegomtassis
 */
public interface RouteRepository {

	/**
	 * Adds a route.
	 * 
	 * @param route
	 * @return the stored route
	 */
	Route addRoute(Route route);

	/**
	 * Find a {@link Route} given its path
	 * 
	 * @param routePath
	 * @return route for a given path
	 */
	Optional<Route> findByPath(String routePath);

	/**
	 * @return {@link Collection} of registered routes.
	 */
	Collection<Route> list();

	/**
	 * @param startingPoint
	 * @return {@link Collection} of registered routes starting with a given
	 *         starting point.
	 */
	Collection<Route> listByStartingPoint(String startingPoint);
}
