package com.dmt.train.routing;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import com.dmt.train.routing.utils.Assert;

/**
 * Single point implementing the different train commuter related application
 * services.
 * 
 * @author diegomtassis
 */
@Named
public class TrainCommuterApplicationService implements RouteDistanceApplicationService,
		RouteRegistryApplicationService, TripFinderApplicationService {

	private static final String NO_ROUTES_FOUND = "No routes found";

	private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";

	@Inject
	private RouteRepository routeRepository;

	@Inject
	private RouteDistanceCalculator distanceCalculator;

	@Inject
	private TripFinder tripFinder;

	@Override
	public void register(String path, Integer distance) {

		Assert.isTrue(path.length() == 2, "Only direct routes can be registered");
		routeRepository.addRoute(new Route(path, distance));
	}

	@Override
	public Optional<Integer> distance(String routePath) {

		Optional<Integer> distance = distanceCalculator.distance(new Route(routePath));

		// Printing a summary message
		System.out.println("The distance of the route " + routePath + " is: "
				+ (distance.isPresent() ? distance.get() : NO_SUCH_ROUTE));

		return distance;
	}

	@Override
	public Collection<String> find(String startingPoint, String endPoint, Integer minStops, Integer maxStops) {

		validateCity(startingPoint);
		validateCity(endPoint);
		Assert.isTrue(minStops != null && minStops.intValue() > 0);
		Assert.isTrue(maxStops != null && maxStops.intValue() > 0);

		List<String> foundRoutes = this.tripFinder.find(startingPoint, endPoint, minStops, maxStops).stream()
				.map(Object::toString).collect(Collectors.toList());

		// Printing a summary message
		StringBuffer buffer = new StringBuffer();
		buffer.append("Trips connecting ")//
				.append(startingPoint)//
				.append(" and ")//
				.append(endPoint)//
				.append(" with a minimun of ")//
				.append(minStops)//
				.append(" and a maximum of ")//
				.append(maxStops)//
				.append(" stops:\n");
		if (foundRoutes.isEmpty()) {
			buffer.append(foundRoutes.isEmpty() ? NO_ROUTES_FOUND : foundRoutes);
		} else {
			foundRoutes.stream().forEach(s -> buffer.append(" ").append(s).append("\n"));
		}
		System.out.println(buffer.toString());

		return foundRoutes;
	}

	@Override
	public Collection<String> find(String startingPoint, String endPoint, Integer maxDistance) {

		validateCity(startingPoint);
		validateCity(endPoint);
		Assert.isTrue(maxDistance != null && maxDistance.intValue() > 0);

		List<String> foundRoutes = this.tripFinder.find(startingPoint, endPoint, maxDistance).stream()
				.map(Object::toString).collect(Collectors.toList());

		// Printing a summary message
		StringBuffer buffer = new StringBuffer();
		buffer.append("Trips connecting ")//
				.append(startingPoint)//
				.append(" and ")//
				.append(endPoint)//
				.append(" with a distance lower than ")//
				.append(maxDistance)//
				.append(":\n");
		if (foundRoutes.isEmpty()) {
			buffer.append(foundRoutes.isEmpty() ? NO_ROUTES_FOUND : foundRoutes);
		} else {
			foundRoutes.stream().forEach(s -> buffer.append(" ").append(s).append("\n"));
		}
		System.out.println(buffer.toString());

		return foundRoutes;
	}

	@Override
	public Optional<Integer> find(String startingPoint, String endPoint) {

		validateCity(startingPoint);
		validateCity(endPoint);

		Collection<Route> shortestRoutes = tripFinder.find(startingPoint, endPoint);

		String foundRoutes = shortestRoutes.stream().map(r -> r.toString()).reduce((r1, r2) -> r1 + "\n " + r2)
				.orElse("");

		// Printing a summary message
		StringBuffer buffer = new StringBuffer();
		buffer.append("Trips connecting ")//
				.append(startingPoint)//
				.append(" and ")//
				.append(endPoint)//
				.append(" with minimum distance:\n")//
				.append(" ");
		buffer.append(foundRoutes.isEmpty() ? NO_ROUTES_FOUND : foundRoutes);
		System.out.println(buffer.toString());

		if (shortestRoutes.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(shortestRoutes.iterator().next().getDistance());
	}

	/**
	 * Validates a given city
	 * 
	 * @param city
	 */
	private void validateCity(String city) {
		Assert.isTrue(city != null && !city.isEmpty());
	}
}
