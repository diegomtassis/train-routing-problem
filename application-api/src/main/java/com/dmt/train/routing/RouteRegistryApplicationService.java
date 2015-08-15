package com.dmt.train.routing;

/**
 * Application service to enable clients to register new direct routes in the
 * system.
 * 
 * @author diegomtassis
 *
 */
public interface RouteRegistryApplicationService extends ApplicationService {

	/**
	 * Registers a route in the registry. Allows to register only direct routes.
	 * 
	 * @param path
	 *            route's path
	 * @param distance
	 */
	void register(String path, Integer distance);

}
