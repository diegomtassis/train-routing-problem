package com.dmt.train.routing;

import java.util.regex.Pattern;

import com.dmt.train.routing.utils.Assert;

/**
 * Route entity.
 * 
 * @author diegomtassis
 */
public class InputRoute {

	private static final String ROUTE_PATH_REGEXP = "[A-Z]{2,}+";

	private static final Pattern ROUTE_PATH_PATTERN = Pattern.compile(ROUTE_PATH_REGEXP);

	private final String path;

	private Integer distance;

	/**
	 * Builds a route.
	 * 
	 * @param path
	 * @param distance
	 */
	public InputRoute(String path, Integer distance) {
		Assert.notNull(distance);
		Assert.notNull(path);
		Assert.isTrue(ROUTE_PATH_PATTERN.matcher(path).matches(), "Invalid path " + path + ". It must match "
				+ ROUTE_PATH_REGEXP);
		this.path = path;
		this.distance = distance;
	}

	/**
	 * @return the path a route describes
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return distance between the start and the end point
	 */
	public Integer getDistance() {
		return distance;
	}
}
