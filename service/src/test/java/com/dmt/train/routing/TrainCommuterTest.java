package com.dmt.train.routing;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Rule;
import org.junit.Test;

public class TrainCommuterTest {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);

	@TestSubject
	private TrainCommuter commuter = new TrainCommuter();

	@Mock
	private RouteRepository mockRouteRepository;

	@Test
	public void testDistance_oneStopRoute() {

		// setup: data
		Route route = new Route("AB", 200);

		// setup: expectations
		expect(mockRouteRepository.findByPath(route.getPath())).andReturn(Optional.of(route));

		// exercise
		replay(mockRouteRepository);
		Optional<Integer> distance = commuter.distance(route);

		// verify
		verify(mockRouteRepository);
		assertThat(distance.get(), is(route.getDistance()));
	}

	@Test
	public void testDistance_multipleStopsRoute() {

		// setup: data
		Route requestedRoute = new Route("ABC", 200);
		Route firstSubRoute = new Route("AB", 150);
		Route secondSubRoute = new Route("BC", 50);

		// setup: expectations
		expect(mockRouteRepository.findByPath(requestedRoute.getPath())).andReturn(Optional.empty());
		expect(mockRouteRepository.findByPath(firstSubRoute.getPath())).andReturn(Optional.of(firstSubRoute));
		expect(mockRouteRepository.findByPath(secondSubRoute.getPath())).andReturn(Optional.of(secondSubRoute));
		expect(mockRouteRepository.addRoute(requestedRoute)).andReturn(requestedRoute);

		// exercise
		replay(mockRouteRepository);
		Optional<Integer> distance = commuter.distance(requestedRoute);

		// verify
		verify(mockRouteRepository);
		assertThat(distance.get(), is(requestedRoute.getDistance()));
	}

	@Test
	public void testDistance_routeNotRegistered() {

		// setup: data
		Route route = new Route("AB");

		// setup: expectations
		expect(mockRouteRepository.findByPath(route.getPath())).andReturn(Optional.empty());

		// exercise
		replay(mockRouteRepository);
		Optional<Integer> distance = commuter.distance(route);

		// verify
		verify(mockRouteRepository);
		distance.ifPresent(r -> fail());
	}

	@Test
	public void testFindFilteringByStops() {

		// setup: data
		String startingPoint = "A";
		String endPoint = "C";
		List<Route> startingWithA = new ArrayList<Route>();
		startingWithA.add(new Route("AB", 5));
		startingWithA.add(new Route("AC", 5));
		List<Route> startingWithB = new ArrayList<Route>();
		startingWithB.add(new Route("BC", 5));
		List<Route> startingWithC = new ArrayList<Route>();
		startingWithC.add(new Route("CE", 5));

		// setup: expectations
		expect(mockRouteRepository.listByStartingPoint("A")).andReturn(startingWithA);
		expect(mockRouteRepository.listByStartingPoint("B")).andReturn(startingWithB);
		expect(mockRouteRepository.listByStartingPoint("C")).andReturn(startingWithC).times(2);
		expect(mockRouteRepository.listByStartingPoint("E")).andReturn(new ArrayList<Route>());

		// exercise
		replay(mockRouteRepository);
		Collection<Route> routes = commuter.find(startingPoint, endPoint, 1, 2);

		// verify
		verify(mockRouteRepository);
		assertThat(routes.size(), is(2));
		assertEquals(1, routes.stream().filter(r -> r.getPath().equals("ABC")).collect(Collectors.toList()).size());
		assertEquals(1, routes.stream().filter(r -> r.getPath().equals("AC")).collect(Collectors.toList()).size());
	}

	@Test
	public void testFindFilteringByDistance() {

		// setup: data
		String startingPoint = "A";
		String endPoint = "C";
		List<Route> startingWithA = new ArrayList<Route>();
		startingWithA.add(new Route("AB", 10));
		startingWithA.add(new Route("AC", 12));
		List<Route> startingWithB = new ArrayList<Route>();
		startingWithB.add(new Route("BC", 13));
		List<Route> startingWithC = new ArrayList<Route>();
		startingWithC.add(new Route("CE", 14));

		// setup: expectations
		expect(mockRouteRepository.listByStartingPoint("A")).andReturn(startingWithA);
		expect(mockRouteRepository.listByStartingPoint("B")).andReturn(startingWithB);
		expect(mockRouteRepository.listByStartingPoint("C")).andReturn(startingWithC).times(2);

		// exercise
		replay(mockRouteRepository);
		Collection<Route> routes = commuter.find(startingPoint, endPoint, 25);

		// verify
		verify(mockRouteRepository);
		assertThat(routes.size(), is(2));
		assertEquals(1, routes.stream().filter(r -> r.getPath().equals("ABC")).collect(Collectors.toList()).size());
		assertEquals(1, routes.stream().filter(r -> r.getPath().equals("AC")).collect(Collectors.toList()).size());
	}

	@Test
	public void testFindShortestDistance() {

		// setup: data
		String startingPoint = "A";
		String endPoint = "C";
		List<Route> startingWithA = new ArrayList<Route>();
		startingWithA.add(new Route("AB", 10));
		startingWithA.add(new Route("AC", 15));
		List<Route> startingWithB = new ArrayList<Route>();
		startingWithB.add(new Route("BC", 5));
		List<Route> startingWithC = new ArrayList<Route>();
		startingWithC.add(new Route("CE", 14));

		// setup: expectations
		expect(mockRouteRepository.listByStartingPoint("A")).andReturn(startingWithA);
		expect(mockRouteRepository.listByStartingPoint("B")).andReturn(startingWithB);
		expect(mockRouteRepository.listByStartingPoint("C")).andReturn(startingWithC).times(2);

		// exercise
		replay(mockRouteRepository);
		Collection<Route> routes = commuter.find(startingPoint, endPoint);

		// verify
		verify(mockRouteRepository);
		assertThat(routes.size(), is(2));
		assertEquals(1, routes.stream().filter(r -> r.getPath().equals("ABC")).collect(Collectors.toList()).size());
		assertEquals(1, routes.stream().filter(r -> r.getPath().equals("AC")).collect(Collectors.toList()).size());
	}
}
