package com.dmt.train.routing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmt.train.routing.utils.Assert;

/**
 * Very simple implementation of {@link RoutesParser} using java built in
 * mechanisms.</p>
 * 
 * It loads the whole file in memory before parsing so it is not recommended for
 * huge files.
 * 
 * @author diegomtassis
 *
 */
@Named
public class SimpleRoutesParser implements RoutesParser {

	private static final String LINE_BREAK = "\n";
	private static final String EMPTY_STRING = "";
	private static final String WHITE_SPACES_REGEX = "\\s+";
	private static final String ROUTE_REGEX = "([A-Z][A-Z])(\\d+)";
	private static final String COMMA = ",";

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRoutesParser.class);

	@Override
	public Collection<Route> parse(InputStream inputStream) {

		String content = inputStreamToString(inputStream);

		content = content.replaceAll(LINE_BREAK, EMPTY_STRING);
		content = content.replaceAll(WHITE_SPACES_REGEX, EMPTY_STRING);

		LOGGER.debug("Trimmed routes file content: {}", content);

		return Arrays.asList(content.split(COMMA)).stream().map(r -> parseRoute(r)).collect(Collectors.toList());
	}

	/**
	 * Parses a single route.
	 * 
	 * @param routeRepresentation
	 * @return the {@link Route}
	 */
	private Route parseRoute(String routeRepresentation) {

		Matcher matcher = Pattern.compile(ROUTE_REGEX).matcher(routeRepresentation);

		Assert.isTrue(matcher.matches(), "Invalid route " + routeRepresentation + ". Routes must match " + ROUTE_REGEX);

		String path = matcher.group(1);
		Integer distance = Integer.valueOf(matcher.group(2));

		return new Route(path, distance);
	}

	/**
	 * Loads an input stream into a String. Apache IOUtils would be very useful
	 * here.
	 * 
	 * @param inputStream
	 * @return the string representing the content of the input stream.
	 */
	private String inputStreamToString(InputStream inputStream) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			throw new IllegalArgumentException("Problem dealing with input file", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw new IllegalArgumentException("Problem dealing with input file", e);
				}
			}
		}

		return sb.toString();
	}
}
