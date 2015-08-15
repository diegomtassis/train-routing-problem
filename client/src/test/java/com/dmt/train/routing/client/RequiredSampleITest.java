package com.dmt.train.routing.client;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dmt.train.routing.RouteDistanceApplicationService;
import com.dmt.train.routing.TripFinderApplicationService;

/**
 * Sample test scenarios.<br />
 * 
 * @author diegomtassis
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
public class RequiredSampleITest extends AbstractITest {

	@Inject
	private RouteDistanceApplicationService distanceService;

	@Inject
	private TripFinderApplicationService tripFinderService;

	@Before
	public void setUp() throws IOException {

		assertNotNull(distanceService);
		assertNotNull(tripFinderService);

		loadInitialRoutes("sample-fixture.txt");
	}

	/**
	 * The distance of the route A-B-C.
	 * <P/>
	 * Output #1: 9
	 */
	@Test
	public void testScenario1() {

		// setup
		String routePath = "ABC";

		// exercise
		Optional<Integer> distance = distanceService.distance(routePath);

		// verify
		assertThat(distance.get(), is(9));
	}

	/**
	 * The distance of the route A-D.
	 * <P/>
	 * Output #2: 5
	 */
	@Test
	public void testScenario2() {

		// setup
		String routePath = "AD";

		// exercise
		Optional<Integer> distance = distanceService.distance(routePath);

		// verify
		assertThat(distance.get(), is(5));
	}

	/**
	 * The distance of the route A-D-C.
	 * <P/>
	 * Output #3: 13
	 */
	@Test
	public void testScenario3() {

		// setup
		String routePath = "ADC";

		// exercise
		Optional<Integer> distance = distanceService.distance(routePath);

		// verify
		assertThat(distance.get(), is(13));
	}

	/**
	 * The distance of the route A-E-B-C-D.
	 * <P/>
	 * Output #4: 22
	 */
	@Test
	public void testScenario4() {

		// setup
		String routePath = "AEBCD";

		// exercise
		Optional<Integer> distance = distanceService.distance(routePath);

		// verify
		assertThat(distance.get(), is(22));
	}

	/**
	 * The distance of the route A-E-D.
	 * <P/>
	 * Output #5: NO SUCH ROUTE
	 */
	@Test
	public void testScenario5() {

		// setup
		String routePath = "AED";

		// exercise
		Optional<Integer> distance = distanceService.distance(routePath);

		// verify
		assertFalse(distance.isPresent());
	}

	/**
	 * The number of trips starting at C and ending at C with a maximum of 3
	 * stops. In the sample data below, there are two such trips: C-D-C (2
	 * stops). and C-E-B-C (3 stops).
	 * <P/>
	 * Output #6: 2
	 */
	@Test
	public void testScenario6() {

		// setup
		String startPoint = "C";
		String endPoint = "C";
		Integer minStops = 1;
		Integer maxStops = 3;

		// exercise
		Collection<String> trips = tripFinderService.find(startPoint, endPoint, minStops, maxStops);

		// verify
		assertThat(trips.size(), is(2));
		assertTrue(trips.stream().anyMatch(s -> s.contains("path:CDC,")));
		assertTrue(trips.stream().anyMatch(s -> s.contains("path:CEBC,")));
	}

	/**
	 * The number of trips starting at A and ending at C with exactly 4 stops.
	 * In the sample data below, there are three such trips: A to C (via B,C,D);
	 * A to C (via D,C,D); and A to C (via D,E,B).
	 * <P/>
	 * Output #7: 3
	 */
	@Test
	public void testScenario7() {

		// setup
		String startPoint = "A";
		String endPoint = "C";
		Integer minStops = 4;
		Integer maxStops = 4;

		// exercise
		Collection<String> trips = tripFinderService.find(startPoint, endPoint, minStops, maxStops);

		// verify
		assertThat(trips.size(), is(3));
		assertTrue(trips.stream().anyMatch(s -> s.contains("path:ABCDC,")));
		assertTrue(trips.stream().anyMatch(s -> s.contains("path:ADCDC,")));
		assertTrue(trips.stream().anyMatch(s -> s.contains("path:ADEBC,")));
	}

	/**
	 * The length of the shortest route (in terms of distance to travel) from A
	 * to C.
	 * <P/>
	 * Output #8: 9
	 */
	@Test
	public void testScenario8() {

		// setup
		String startPoint = "A";
		String endPoint = "C";

		// exercise
		Optional<Integer> distance = tripFinderService.find(startPoint, endPoint);

		// verify
		assertThat(distance.get(), is(9));
	}

	/**
	 * The length of the shortest route (in terms of distance to travel) from B
	 * to B.
	 * <P/>
	 * Output #9: 9
	 */
	@Test
	public void testScenario9() {

		// setup
		String startPoint = "B";
		String endPoint = "B";

		// exercise
		Optional<Integer> distance = tripFinderService.find(startPoint, endPoint);

		// verify
		assertThat(distance.get(), is(9));
	}

	/**
	 * The number of different routes from C to C with a distance of less than
	 * 30. In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC,
	 * CEBCEBC, CEBCEBCEBC.
	 * <P/>
	 * Output #10: 7
	 */
	@Test
	public void testScenario10() {

		// setup
		String startPoint = "C";
		String endPoint = "C";
		Integer maxDistance = 30;

		// exercise
		Collection<String> routes = tripFinderService.find(startPoint, endPoint, maxDistance);

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
}
