package com.dmt.train.routing.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dmt.train.routing.RouteDistanceApplicationService;
import com.dmt.train.routing.utils.Assert;

/**
 * Distance calculator invoker.
 * 
 * @author diegomtassis
 *
 */
public class DistanceCalculator {

	public static void main(String[] args) {

		if (args == null || args.length != 2) {
			System.out.println("Usage: command routes-file-path route");
			System.out.println("Example: command ~/routes.txt ABCDE");
			return;
		}

		String path = args[0];
		String route = args[1];

		// Get services from the DI context
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		Assert.state(applicationContext != null);
		RouteDistanceApplicationService distanceService = applicationContext
				.getBean(RouteDistanceApplicationService.class);
		Assert.state(distanceService != null);

		// Register the graph
		RegisterRoutesHelper.registerRoutes(applicationContext, path);

		// Calculate the distance
		distanceService.distance(route);
	}
}
