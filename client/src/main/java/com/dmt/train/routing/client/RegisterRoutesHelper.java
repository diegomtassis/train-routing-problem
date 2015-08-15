package com.dmt.train.routing.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.springframework.context.ApplicationContext;

import com.dmt.train.routing.InputRoute;
import com.dmt.train.routing.RouteRegistryApplicationService;
import com.dmt.train.routing.RoutesParserApplicationService;
import com.dmt.train.routing.utils.Assert;

/**
 * Helps the invoker to register routes in a registry.
 * 
 * @author diegomtassis
 *
 */
public class RegisterRoutesHelper {

	/**
	 * Registers the routes in a graph file into a routes registry.
	 * 
	 * @param context
	 *            a parser and a registry are expected
	 * @param path
	 *            the graph file
	 */
	public static void registerRoutes(ApplicationContext context, String path) {

		RoutesParserApplicationService parser = context.getBean(RoutesParserApplicationService.class);
		Assert.state(parser != null);
		RouteRegistryApplicationService registryService = context.getBean(RouteRegistryApplicationService.class);
		Assert.state(registryService != null);

		Collection<InputRoute> routes;
		Path graphFile = Paths.get(path);
		try (InputStream in = Files.newInputStream(graphFile)) {
			routes = parser.parse(in);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		routes.stream().forEach(r -> registryService.register(r.getPath(), r.getDistance()));
	}
}
