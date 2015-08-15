package com.dmt.train.routing.client;

import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dmt.train.routing.Route;
import com.dmt.train.routing.RouteRegistryApplicationService;
import com.dmt.train.routing.RouteRepository;
import com.dmt.train.routing.RoutesParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
public class RouteRegistryITest {

	@Inject
	private RouteRegistryApplicationService registryService;

	@Inject
	private RouteRepository routeRepository;

	@Inject
	private RoutesParser parser;

	@Test
	public void testRegister_simpleRoute() {

		// setup

		// exercise
		registryService.register("AB", 250);

		// verify
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegister_multiStopRoute_exceptionIsThrown() {

		// setup

		// exercise
		registryService.register("ABC", 250);

		// verify
		logExistingRoutes();
	}

	@Test
	public void testRegister_routesFromSampleFile() throws IOException {

		// setup
		Resource inputFile = new ClassPathResource("sample-fixture.txt");

		// exercise
		Collection<Route> routes = parser.parse(inputFile.getInputStream());
		routes.stream().forEach(r -> registryService.register(r.getPath(), r.getDistance()));

		// verify
		logExistingRoutes();
	}

	private void logExistingRoutes() {

		routeRepository.list().stream().forEach(r -> System.out.println(r));
	}
}
