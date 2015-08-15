package com.dmt.train.routing;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Rule;
import org.junit.Test;

public class TrainCommuterApplicationServiceTest {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);

	@TestSubject
	private TrainCommuterApplicationService commuter = new TrainCommuterApplicationService();

	@Mock
	private RouteRepository mockRouteRepository;

	@Mock
	private RouteDistanceCalculator mockDistanceCalculator;

	@Mock
	private TripFinder mockTripFinder;

	@Test
	public void testRegister() {

		// setup: data
		Route fooRoute = new Route("TF", 500);

		// setup: expectations
		expect(mockRouteRepository.addRoute(fooRoute)).andReturn(fooRoute);

		// exercise
		replay(mockRouteRepository);
		commuter.register(fooRoute.getPath(), fooRoute.getDistance());

		// verify
		verify(mockRouteRepository);
	}

	@Test
	public void testDistance() {

		// setup: data
		Route fooRoute = new Route("TF");
		Integer distance = 500;

		// setup: expectations
		expect(mockDistanceCalculator.distance(fooRoute)).andReturn(Optional.of(distance));

		// exercise
		replay(mockDistanceCalculator);
		Optional<Integer> result = commuter.distance(fooRoute.getPath());

		// verify
		verify(mockDistanceCalculator);
		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(distance));
	}

	@Test
	public void testFind_byStops() {

		// setup: data
		String startingPoint = "A";
		String endPoint = "B";
		int minStops = 4;
		int maxStops = 5;

		// setup: expectations
		ArrayList<Route> routes = new ArrayList<Route>();
		String path1 = "ATRSB";
		routes.add(new Route(path1));
		String path2 = "AQUERB";
		routes.add(new Route(path2));
		expect(mockTripFinder.find(startingPoint, endPoint, minStops, maxStops)).andReturn(routes);

		// exercise
		replay(mockTripFinder);
		Collection<String> result = commuter.find(startingPoint, endPoint, minStops, maxStops);

		// verify
		verify(mockTripFinder);
		assertThat(result.size(), is(2));
		assertTrue(result.stream().anyMatch(s -> s.contains(path1)));
		assertTrue(result.stream().anyMatch(s -> s.contains(path2)));
	}

	@Test
	public void testFind_byDistance() {

		// setup: data
		String startingPoint = "A";
		String endPoint = "B";
		int maxDistance = 200;

		// setup: expectations
		ArrayList<Route> routes = new ArrayList<Route>();
		String path1 = "ATRSB";
		routes.add(new Route(path1));
		String path2 = "AQUERB";
		routes.add(new Route(path2));
		expect(mockTripFinder.find(startingPoint, endPoint, maxDistance)).andReturn(routes);

		// exercise
		replay(mockTripFinder);
		Collection<String> result = commuter.find(startingPoint, endPoint, maxDistance);

		// verify
		verify(mockTripFinder);
		assertThat(result.size(), is(2));
		assertTrue(result.stream().anyMatch(s -> s.contains(path1)));
		assertTrue(result.stream().anyMatch(s -> s.contains(path2)));
	}

	@Test
	public void testFind_routesWithShortestDistance() {

		// setup: data
		String startingPoint = "A";
		String endPoint = "B";

		// setup: expectations
		ArrayList<Route> routes = new ArrayList<Route>();
		int distance = 25;
		String path1 = "ATRSB";
		routes.add(new Route(path1, distance));
		String path2 = "AQUERB";
		routes.add(new Route(path2, distance));
		expect(mockTripFinder.find(startingPoint, endPoint)).andReturn(routes);

		// exercise
		replay(mockTripFinder);
		Optional<Integer> shortestDistance = commuter.find(startingPoint, endPoint);

		// verify
		verify(mockTripFinder);
		assertThat(shortestDistance.get(), is(25));
	}
}
