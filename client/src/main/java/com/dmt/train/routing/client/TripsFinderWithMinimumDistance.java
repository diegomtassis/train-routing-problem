package com.dmt.train.routing.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dmt.train.routing.TripFinderApplicationService;
import com.dmt.train.routing.utils.Assert;

/**
 * Distance calculator invoker.
 * 
 * @author diegomtassis
 *
 */
public class TripsFinderWithMinimumDistance {

	public static void main(String[] args) {

		if (args == null || args.length != 3) {
			System.out.println("Usage: command routes-file-path startPoint endPoint");
			System.out.println("Example: command ~/routes.txt A C");
			return;
		}

		String path = args[0];
		String startPoint = args[1];
		String endPoint = args[2];

		// Get services from the DI context
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		Assert.state(applicationContext != null);
		TripFinderApplicationService tripFinderService = applicationContext.getBean(TripFinderApplicationService.class);
		Assert.state(tripFinderService != null);

		// Register the graph
		RegisterRoutesHelper.registerRoutes(applicationContext, path);

		// Calculate the routes
		tripFinderService.find(startPoint, endPoint);
	}
}
