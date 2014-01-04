/**
 * 
 */
package com.oose2013.group7.roommate.test.games.describe;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oose2013.group7.roommates.server.DatabaseObject;
import com.oose2013.group7.roommates.server.database.User;
import com.oose2013.group7.roommates.server.games.describe.DescribeGame;

/**
 * @author rujuta
 * This class tests the Describe Game Functionality
 */

public class DescribeGameTest {

	User user1, user2, user3, user4;
	DescribeGame dg;

	/**Initialization of the User Objects**/
	public void initialize() {
		DatabaseObject registration = new DatabaseObject();
		user1 = registration.login("rujuta", "password");
		user2 = registration.login("Peter", "password");
		user3 = registration.login("Jane", "password");
		user4 = registration.login("libowen", "password");
		dg = new DescribeGame();
	}

	/***Tests if the set description method sets the descriptions correctly**/
	@Test
	public void setDescriptionTest() {

		initialize();
		dg.setCurrentWord("FAT");
		dg.setDescription("yellow", user1.getUserName());
		dg.setDescription("yellow tasteless", user2.getUserName());
		dg.setDescription("tasteless", user3.getUserName());
		dg.setDescription("green", user4.getUserName());

		Map<String, String> words = dg.getUserWordDescription().get(
				dg.getCurrentWord());

		// Test for words in the WordList i.e. they should match
		// apple,banana,pear
		assertTrue(dg.getUserWordDescription().keySet().contains("FAT"));
		//assertTrue(dg.getUserWordDescription().keySet().contains("apple"));
		//assertTrue(dg.getUserWordDescription().keySet().contains("banana"));

		// Test if the current word is pear and if pear is initialized with
		// these user descriptions
		assertTrue(words.get(user1.getUserName()) == "yellow");
		assertTrue(words.get(user2.getUserName()) == "yellow tasteless");
		assertTrue(words.get(user3.getUserName()) == "tasteless");
		assertTrue(words.get(user4.getUserName()) == "green");

	}

	/**Tests if the method populateScores() correctly calculates the scores. 
	 * The score is the count of number of common words between two descriptions (i.e  the intersection)**/
	@Test
	public void getScoreBoardTest() {

		setDescriptionTest();
		// Populate Scores Test
		dg.populateScores();

		// Matching words between user 1 and all other users
		assertTrue(dg.getUserScoreboard().get("FAT").get(user1.getUserName()).get(user2.getUserName()) == 1);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user1.getUserName()).get(user3.getUserName()) == 0);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user1.getUserName()).get(user4.getUserName()) == 0);

		// Matching words between user 2 and all other users
		assertTrue(dg.getUserScoreboard().get("FAT").get(user2.getUserName()).get(user1.getUserName()) == 1);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user2.getUserName()).get(user3.getUserName()) == 1);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user2.getUserName()).get(user4.getUserName()) == 0);

		// Matching words between user 3 and all other users
		assertTrue(dg.getUserScoreboard().get("FAT").get(user3.getUserName()).get(user1.getUserName()) == 0);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user3.getUserName()).get(user2.getUserName()) == 1);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user3.getUserName()).get(user4.getUserName()) == 0);

		// Matching words between user 4 and all other users
		assertTrue(dg.getUserScoreboard().get("FAT").get(user4.getUserName()).get(user1.getUserName()) == 0);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user4.getUserName()).get(user2.getUserName()) == 0);
		assertTrue(dg.getUserScoreboard().get("FAT").get(user4.getUserName()).get(user3.getUserName()) == 0);
	}

}
