package com.dmt.train.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmt.train.routing.utils.Assert;

/**
 * Train commuter.
 * 
 * @author diegomtassis
 */
@Named
public class TrainCommuter implements RouteDistanceCalculator, TripFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainCommuter.class);

	@Inject
	private RouteRepository repository;

	@Override
	public Optional<Integer> distance(Route route) {

		Optional<Route> found = repository.findByPath(route.getPath());

		if (found.isPresent()) {
			return Optional.of(found.get().getDistance());
		}

		if (route.getPath().length() == 2) {
			// It's a direct route and it doesn't exist
			LOGGER.debug("No direct route {} in the current network", route.getPath());
			return Optional.empty();
		}

		/*
		 * Divide and conquer. Split the route into 2: A direct route between
		 * the first 2 cities and a multi-stop route connecting the rest.
		 */
		Optional<Integer> firstTwoCitiesDistance = this.distance(new Route(route.getPath().substring(0, 2)));
		Optional<Integer> restCitiesRouteDistance = this.distance(new Route(route.getPath().substring(1)));

		if (firstTwoCitiesDistance.isPresent() && restCitiesRouteDistance.isPresent()) {

			// Both sub routes exist! Store the result for the future and return
			route.setDistance(firstTwoCitiesDistance.get().intValue() + restCitiesRouteDistance.get().intValue());
			repository.addRoute(route);
			return Optional.of(route.getDistance());
		}

		// At least one of the 2 sub routes can't be done
		LOGGER.debug("Route {} can not be traversed in the current network", route.getPath());
		return Optional.empty();
	}

	@Override
	public Collection<Route> find(String startingPoint, String endPoint, Integer minStops, Integer maxStops) {

		List<Route> candidates = retrieveDirectRoutes(startingPoint).stream().collect(Collectors.toList());

		List<Route> solutions = new ArrayList<Route>();

		Predicate<Route> isWorthToBeFurtherExpanded = r -> r.calculateStops() <= maxStops;
		Predicate<Route> isAValidSolution = r -> endPoint.equals(r.getEndPoint()) && r.calculateStops() >= minStops
				&& r.calculateStops() <= maxStops;
		Consumer<Route> updateSolution = r -> solutions.add(r);

		expandAndSolve(candidates, isWorthToBeFurtherExpanded, isAValidSolution, updateSolution);

		return solutions;
	}

	@Override
	public Collection<Route> find(String startingPoint, String endPoint, Integer maxDistance) {

		List<Route> candidates = retrieveDirectRoutes(startingPoint).stream().collect(Collectors.toList());

		List<Route> solutions = new ArrayList<Route>();

		Predicate<Route> isWorthToBeFurtherExpanded = r -> r.getDistance() < maxDistance;
		Predicate<Route> isAValidSolution = r -> endPoint.equals(r.getEndPoint()) && r.getDistance() < maxDistance;
		Consumer<Route> updateSolution = r -> solutions.add(r);

		expandAndSolve(candidates, isWorthToBeFurtherExpanded, isAValidSolution, updateSolution);

		return solutions;
	}

	@Override
	public Collection<Route> find(String startingPoint, String endPoint) {

		List<Route> candidates = retrieveDirectRoutes(startingPoint).stream().collect(Collectors.toList());

		List<Route> solutions = new ArrayList<Route>();
		AtomicInteger shortestDistance = new AtomicInteger(Integer.MAX_VALUE);

		Predicate<Route> isWorthToBeFurtherExpanded = r -> r.getDistance() < shortestDistance.get();
		Predicate<Route> isAValidSolution = r -> endPoint.equals(r.getEndPoint())
				&& r.getDistance() <= shortestDistance.get();
		Consumer<Route> updateSolution = r -> {
			/*
			 * Compare with the shortest, and if this one is shorter then
			 * replace the distance and remove the previous stored solutions
			 */
			if (r.getDistance() < shortestDistance.get()) {
				solutions.clear();
				shortestDistance.set(r.getDistance());
			}
			solutions.add(r);
		};

		expandAndSolve(candidates, isWorthToBeFurtherExpanded, isAValidSolution, updateSolution);

		return solutions;
	}

	/**
	 * Given a list of candidates:<br/>
	 * <ol>
	 * <li>Evaluates whether a candidate is a valid solution, and then performs
	 * something with it.</li>
	 * <li>Expands a candidate when it is worth, this is adds to the candidates
	 * list all the routes resulting of combining the given route with all the
	 * 1-stop route beginning at the end of the current one.</li>
	 * </ol>
	 * 
	 * @param candidates
	 *            list of candidates
	 * @param isWorthToBeFurtherExpanded
	 *            evaluates whether a route might lead (by being expanded) to a
	 *            solution of the problem
	 * @param isAValidSolution
	 *            evaluates whether a route is a solution of the problem
	 * @param updateSolution
	 *            what to do with the solution?
	 */
	private void expandAndSolve(List<Route> candidates, Predicate<Route> isWorthToBeFurtherExpanded,
			Predicate<Route> isAValidSolution, Consumer<Route> updateSolution) {

		int index = 0;
		while (index < candidates.size()) {

			// Is the current route a solution?
			Route route = candidates.get(index++);
			if (isAValidSolution.test(route)) {
				updateSolution.accept(route);
			}

			// Are routes resulted of expanding the current one worth to
			// be further evaluated? Then add them to the candidates
			retrieveDirectRoutes(route.getEndPoint()).stream().map(r -> combine(route, r))
					.filter(isWorthToBeFurtherExpanded).forEach(r -> candidates.add(r));
		}
	}

	/**
	 * Combines 2 routes to create a new one
	 * 
	 * @param firstRoute
	 * @param secondRoute
	 * @return combined route
	 */
	private Route combine(Route firstRoute, Route secondRoute) {

		Assert.notNull(firstRoute);
		Assert.notNull(secondRoute);
		Assert.isTrue(firstRoute.getEndPoint().equals(secondRoute.getStartPoint()),
				"Can't combine routes whose paths do not end and begin in the same city");
		Assert.notNull(firstRoute.getDistance(), "Can't combine routes wihout the distance set");
		Assert.notNull(secondRoute.getDistance(), "Can't combine routes wihout the distance set");

		Route combinedRoute = new Route(firstRoute.getPath() + secondRoute.getPath().substring(1),
				firstRoute.getDistance() + secondRoute.getDistance());

		LOGGER.debug("Obtained {} after combining {} and {}", combinedRoute, firstRoute, secondRoute);

		return combinedRoute;
	}

	/**
	 * Retrieves all the direct routes starting from a given city
	 * 
	 * @param city
	 * @return direct routes
	 */
	private Collection<Route> retrieveDirectRoutes(String city) {

		return repository.listByStartingPoint(city).stream().filter(r -> r.calculateStops().equals(1))
				.collect(Collectors.toList());
	}
}
