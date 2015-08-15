package com.dmt.train.routing;

import java.util.regex.Pattern;

import com.dmt.train.routing.utils.Assert;

/**
 * Route entity.
 * 
 * @author diegomtassis
 */
public class Route {

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
	public Route(String path, Integer distance) {
		this(path);
		Assert.notNull(distance);
		this.distance = distance;
	}

	/**
	 * Builds a route.
	 * 
	 * @param path
	 */
	public Route(String path) {
		Assert.notNull(path);
		Assert.isTrue(ROUTE_PATH_PATTERN.matcher(path).matches(), "Invalid path " + path + ". It must match "
				+ ROUTE_PATH_REGEXP);
		this.path = path;
	}

	/**
	 * @return the path a route describes
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return route's start point
	 */
	public String getStartPoint() {
		return path.substring(0, 1);
	}

	/**
	 * @return route's end point
	 */
	public String getEndPoint() {
		return path.substring(path.length() - 1);
	}

	/**
	 * @return distance between the start and the end point
	 */
	public Integer getDistance() {
		return distance;
	}

	/**
	 * Sets the distance of the route
	 * 
	 * @param distance
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	/**
	 * @return number of stops of the route
	 */
	public Integer calculateStops() {
		return Integer.valueOf(this.path.length() - 1);
	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Route (path:").append(this.getPath());
		buffer.append(", distance:").append(this.getDistance());
		buffer.append(", stops:").append(this.calculateStops());
		buffer.append(")");

		return buffer.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Route && ((Route) obj).getPath().equals(this.getPath())
				&& (((Route) obj).getDistance() == null && this.getDistance() == null)
				|| (this.getDistance() != null && getDistance().equals(((Route) obj).getDistance()));
	}
}
