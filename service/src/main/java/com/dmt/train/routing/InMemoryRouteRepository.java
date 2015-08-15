package com.dmt.train.routing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmt.train.routing.utils.Assert;

/**
 * Route repository that stores the routes in memory.
 * 
 * @author diegomtassis
 *
 */
@Named
public class InMemoryRouteRepository implements RouteRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryRouteRepository.class);

	/** Routes indexed by path */
	private Map<String, Route> routes = new HashMap<String, Route>();

	public Route addRoute(Route route) {

		Assert.notNull(route);
		Assert.notNull(route.getPath());
		Assert.notNull(route.getDistance(), "Not allowed to store routes without distance");

		routes.put(route.getPath(), route);

		LOGGER.debug("Route {} with distance {} added to the repository", route.getPath(), route.getDistance());

		return route;
	}

	@Override
	public Optional<Route> findByPath(String routePath) {

		return Optional.ofNullable(this.routes.get(routePath));
	}

	@Override
	public Collection<Route> list() {
		return routes.values();
	}

	@Override
	public Collection<Route> listByStartingPoint(String startingPoint) {
		return list().stream().filter(r -> r.getStartPoint().equals(startingPoint)).collect(Collectors.toList());
	}
}
