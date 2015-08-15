package com.dmt.train.routing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.Collection;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class SimpleRoutesParserTest {

	private SimpleRoutesParser parser = new SimpleRoutesParser();

	@Test
	public void testParse_fileWithASingleRoute() {

		// setup
		InputStream inputStream = this.getClass().getResourceAsStream("/routes/single-route-fixture.txt");

		// exercise
		Collection<Route> routes = parser.parse(inputStream);

		// verify
		assertThat(routes, CoreMatchers.notNullValue());
		assertThat(routes.size(), is(1));
		assertThat(routes.iterator().next().getPath(), is("AB"));
	}

	@Test
	public void testParse_fileWithBreakLinesAndSpaces() {

		// setup
		InputStream inputStream = this.getClass().getResourceAsStream("/routes/different-lines-fixture.txt");

		// exercise
		Collection<Route> routes = parser.parse(inputStream);

		// verify
		assertThat(routes, CoreMatchers.notNullValue());
		assertThat(routes.size(), is(9));
	}
}
