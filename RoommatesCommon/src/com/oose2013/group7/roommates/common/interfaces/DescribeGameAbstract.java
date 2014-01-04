package com.oose2013.group7.roommates.common.interfaces;

import java.util.Map;

import com.oose2013.group7.roommates.common.enums.DescribeState;

/**
 * This is data class that is extended by a concrete class in client and server
 * The object of the class extending this abstract class is passed two and fro
 * between client and server
 **/
public abstract class DescribeGameAbstract {

	DescribeState state; // The state of the DescribeGame
	private Integer currentRound; // There are 5 rounds in total, this indicates
									// current round
	private String currentWord; // The current query being described
	private Map<String, Map<String, String>> userWordDescription; // A map
																	// hashed by
																	// the words
																	// to be
																	// described
	private Map<String, Map<String, Map<String, Integer>>> userScoreboard; // A
																			// map
																			// that
																			// gives
																			// information
																			// about
																			// how
																			// similar
																			// one
																			// user's
																			// description
																			// is
																			// to
																			// another

}
