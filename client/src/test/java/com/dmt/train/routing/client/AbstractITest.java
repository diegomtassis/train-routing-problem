package com.dmt.train.routing.client;

import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;

import com.dmt.train.routing.InputRoute;
import com.dmt.train.routing.RouteRegistryApplicationService;
import com.dmt.train.routing.RoutesParserApplicationService;

public abstract class AbstractITest {

	@Inject
	private RouteRegistryApplicationService registryService;

	@Inject
	private RoutesParserApplicationService parser;

	/**
	 * Imports to the registry service the direct routes present in a file.
	 * 
	 * @param location
	 * @throws IOException
	 */
	protected void loadInitialRoutes(String location) throws IOException {
		Collection<InputRoute> routes = parser.parse(new ClassPathResource(location).getInputStream());
		routes.stream().forEach(r -> registryService.register(r.getPath(), r.getDistance()));
	}

	protected RouteRegistryApplicationService getRegistryService() {
		return registryService;
	}
}
