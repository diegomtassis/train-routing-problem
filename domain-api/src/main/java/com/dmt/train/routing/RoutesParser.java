package com.dmt.train.routing;

import java.io.InputStream;
import java.util.Collection;

/**
 * Parser for routes.
 * 
 * @author diegomtassis
 *
 */
public interface RoutesParser extends DomainService {

	/**
	 * Parses a file containing a graph of routes.
	 * 
	 * @param inputStream
	 * @return collections of routes
	 */
	Collection<Route> parse(InputStream inputStream);
}
