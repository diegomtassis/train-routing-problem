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
public class TripsFinderByStops {

	public static void main(String[] args) {

		if (args == null || args.length != 5) {
			System.out.println("Usage: command routes-file-path startPoint endPoint minStops maxStops");
			System.out.println("Example: command ~/routes.txt A C 2 5");
			return;
		}

		String path = args[0];
		String startPoint = args[1];
		String endPoint = args[2];
		String minStops = args[3];
		String maxStops = args[4];

		// Get services from the DI context
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		Assert.state(applicationContext != null);
		TripFinderApplicationService tripFinderService = applicationContext.getBean(TripFinderApplicationService.class);
		Assert.state(tripFinderService != null);

		// Register the graph
		RegisterRoutesHelper.registerRoutes(applicationContext, path);

		// Calculate the routes
		tripFinderService.find(startPoint, endPoint, Integer.valueOf(minStops), Integer.valueOf(maxStops));
	}
}
