package com.dmt.train.routing.utils;

/**
 * Validates arguments and states.
 * 
 * @author diegomtassis
 *
 */
public class Assert {

	/**
	 * Validates that a given argument is not <code>null</code>, throwing an
	 * {@link IllegalArgumentException} when not.
	 * 
	 * @param argument
	 */
	public static void notNull(Object argument) {

		if (argument == null) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Validates that a given argument is not <code>null</code>, throwing an
	 * {@link IllegalArgumentException} when not.
	 * 
	 * @param argument
	 * @param message
	 */
	public static void notNull(Object argument, String message) {

		if (argument == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Validates that a given expression is <code>true</code>, throwing an
	 * {@link IllegalArgumentException} when not.
	 * 
	 * @param argument
	 */
	public static void isTrue(boolean expression) {

		if (!expression) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Validates that a given expression is <code>true</code>, throwing an
	 * {@link IllegalArgumentException} when not.
	 * 
	 * @param argument
	 * @param message
	 */
	public static void isTrue(boolean expression, String message) {

		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Validates that a given state is fulfilled, throwing an
	 * {@link IllegalStateException} when not.
	 * 
	 * @param argument
	 */
	public static void state(boolean state) {

		if (!state) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Validates that a given state is fulfilled, throwing an
	 * {@link IllegalStateException} when not.
	 * 
	 * @param argument
	 * @param message
	 * 
	 */
	public static void state(boolean state, String message) {

		if (!state) {
			throw new IllegalStateException(message);
		}
	}
}
