package com.dmt.train.routing;

import java.io.InputStream;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Simple adapter from {@link RoutesParser} to
 * {@link RoutesParserApplicationService}
 * 
 * @author diegomtassis
 */
@Named
public class SimpleRoutesParserApplicationService implements RoutesParserApplicationService {

	@Inject
	private RoutesParser parser;

	@Override
	public Collection<InputRoute> parse(InputStream inputStream) {
		return parser.parse(inputStream).stream().map(r -> new InputRoute(r.getPath(), r.getDistance()))
				.collect(Collectors.toList());
	}
}
