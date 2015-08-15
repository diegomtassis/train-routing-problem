package com.dmt.train.routing;

import java.io.InputStream;
import java.util.Collection;

/**
 * Parser for routes.
 * 
 * @author diegomtassis
 *
 */
public interface RoutesParserApplicationService extends ApplicationService {

	/**
	 * Parses a file containing a graph of routes.
	 * 
	 * @param inputStream
	 * @return collections of routes
	 */
	Collection<InputRoute> parse(InputStream inputStream);
}
