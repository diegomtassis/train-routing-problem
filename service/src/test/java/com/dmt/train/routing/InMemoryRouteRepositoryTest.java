package com.dmt.train.routing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class InMemoryRouteRepositoryTest {

	private static final Route ROUTE_STUTTGART_BRUSSELS = new Route("SB", 250);

	/** SUT */
	private InMemoryRouteRepository repository;

	@Before
	public void setUp() {
		this.repository = new InMemoryRouteRepository();

		// fixture
		repository.addRoute(ROUTE_STUTTGART_BRUSSELS);
	}

	@Test
	public void testAddRoute() {

		// setup: data
		Route route = new Route("FB", 100);

		// exercise
		Route savedRoute = repository.addRoute(route);

		// verify
		assertThat(savedRoute.getPath(), is(route.getPath()));
		assertThat(savedRoute.getDistance(), is(route.getDistance()));
	}

	@Test
	public void testFindByPath() {

		// setup: data

		// exercise
		Optional<Route> optRetrievedRoute = repository.findByPath(ROUTE_STUTTGART_BRUSSELS.getPath());

		// verify
		Route retrievedRoute = optRetrievedRoute.get();
		assertThat(retrievedRoute.getPath(), is(ROUTE_STUTTGART_BRUSSELS.getPath()));
		assertThat(retrievedRoute.getDistance(), is(ROUTE_STUTTGART_BRUSSELS.getDistance()));
	}

	@Test
	public void testList() {

		// setup

		// exercise
		Collection<Route> result = repository.list();

		// verify
		assertThat(result.size(), is(1));
		assertThat(result.iterator().next(), is(ROUTE_STUTTGART_BRUSSELS));
	}
}
