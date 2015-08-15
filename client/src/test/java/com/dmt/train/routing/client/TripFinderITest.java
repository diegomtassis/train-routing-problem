package com.dmt.train.routing.client;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dmt.train.routing.TripFinderApplicationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
public class TripFinderITest extends AbstractITest {

	@Inject
	private TripFinderApplicationService tripFinderService;

	@Before
	public void setUp() throws IOException {

		loadInitialRoutes("sample-fixture.txt");
	}

	@Test
	public void tesFind_routesBetween2DirectlyConnectedCities() {

		// setup
		String startingCity = "A";
		String endCity = "B";

		// exercise
		Collection<String> routes = tripFinderService.find(startingCity, endCity, 1, 1);

		// verify
		assertThat(routes.size(), is(1));
		assertThat(routes.iterator().next(), containsString("AB"));
	}

	@Test
	public void tesFind_routesBetween2CitiesUnconnected() {

		// setup
		String startingCity = "F";
		String endCity = "J";

		// exercise
		Collection<String> routes = tripFinderService.find(startingCity, endCity, 1, 1);

		// verify
		assertThat(routes.size(), is(0));
	}

	@Test
	public void tesFind_routesBetween2CitiesSeparatedExactlyBy4Stops() {

		// setup
		String startingCity = "A";
		String endCity = "C";

		// exercise
		Collection<String> routes = tripFinderService.find(startingCity, endCity, 4, 4);

		// verify
		assertThat(routes.size(), is(3));
		assertThat(routes, hasItem(containsString("ABCDC")));
		assertThat(routes, hasItem(containsString("ADCDC")));
		assertThat(routes, hasItem(containsString("ADEBC")));
	}

	/**
	 * Number of different routes from C to C with a distance of less than 30.
	 * CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.
	 */
	@Test
	public void tesFind_routesBetween2CitiesWithDistanceLowerThan30() {

		// setup
		String startingCity = "C";
		String endCity = "C";

		// exercise
		Collection<String> routes = tripFinderService.find(startingCity, endCity, 30);

		// verify
		assertThat(routes.size(), is(7));
		assertThat(routes, hasItem(containsString("CDC")));
		assertThat(routes, hasItem(containsString("CEBC")));
		assertThat(routes, hasItem(containsString("CEBCDC")));
		assertThat(routes, hasItem(containsString("CDCEBC")));
		assertThat(routes, hasItem(containsString("CDEBC")));
		assertThat(routes, hasItem(containsString("CEBCEBC")));
		assertThat(routes, hasItem(containsString("CEBCEBCEBC")));
	}

	@Test
	public void tesFind_routesBetween2CitiesWithDistanceLowerThan5_doesNotExist() {

		// setup
		String startingCity = "C";
		String endCity = "C";

		// exercise
		Collection<String> routes = tripFinderService.find(startingCity, endCity, 5);

		// verify
		assertThat(routes.size(), is(0));
	}

	@Test
	public void tesFind_shortestRoute_theDirectRouteIsTheShortest() {

		// setup
		String startingCity = "A";
		String endCity = "B";

		// exercise
		Optional<Integer> distance = tripFinderService.find(startingCity, endCity);

		// verify
		assertThat(distance.get(), is(5));
	}

	@Test
	public void tesFind_shortestRoute_theDirectRouteIsNotTheShortest() {

		// setup
		String startingCity = "F";
		String endCity = "H";

		// exercise
		Optional<Integer> distance = tripFinderService.find(startingCity, endCity);

		// verify
		assertThat(distance.get(), is(7));
	}

	@Test
	public void tesFind_shortestRoute_noRouteAtAll() {

		// setup
		String startingCity = "F";
		String endCity = "Z";

		// exercise
		Optional<Integer> distance = tripFinderService.find(startingCity, endCity);

		// verify
		assertFalse(distance.isPresent());
	}

	@Test
	public void tesFind_shortestRoute_2routesWithTheSameDistance() {

		// setup
		String startingCity = "X";
		String endCity = "Z";

		// exercise
		Optional<Integer> distance = tripFinderService.find(startingCity, endCity);

		// verify
		assertThat(distance.get(), is(50));
	}
}
