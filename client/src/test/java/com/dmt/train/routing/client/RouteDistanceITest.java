package com.dmt.train.routing.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dmt.train.routing.RouteDistanceApplicationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
public class RouteDistanceITest extends AbstractITest {

	@Inject
	private RouteDistanceApplicationService distanceService;

	@Before
	public void setUp() throws IOException {

		loadInitialRoutes("sample-fixture.txt");
	}

	@Test
	public void testDistance_twoDirectlyConnectedCities() {

		// setup
		String path = "AB";

		// exercise
		Optional<Integer> calculatedDistance = distanceService.distance(path);

		// verify
		Integer expectedDistance = Integer.valueOf(5);
		assertThat(calculatedDistance.get(), is(expectedDistance));
	}

	@Test
	public void testDistance_twoUnconnectedCities() {

		// setup
		String path = "BA";

		// exercise
		Optional<Integer> calculatedDistance = distanceService.distance(path);

		// verify
		assertThat(calculatedDistance.isPresent(), CoreMatchers.is(false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDistance_emptyRoutePath() {

		// setup
		String path = "";

		// exercise
		distanceService.distance(path);

		// verify
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDistance_invalidPath() {

		// setup
		String path = "foo bar";

		// exercise
		distanceService.distance(path);

		// verify
	}

	@Test
	public void testDistance_routeABCDE() {

		// setup
		String path = "ABCDE";

		// exercise
		Optional<Integer> calculatedDistance = distanceService.distance(path);

		// verify
		Integer expectedDistance = Integer.valueOf(23);
		assertThat(calculatedDistance.get(), is(expectedDistance));
	}

	@Test
	public void testDistance_routeEDCBA() {

		// setup
		String path = "EDCBA";

		// exercise
		Optional<Integer> calculatedDistance = distanceService.distance(path);

		// verify
		assertThat(calculatedDistance.isPresent(), CoreMatchers.is(false));
	}

}
