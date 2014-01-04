package com.oose2013.group7.roommates.common.commands;

import com.oose2013.group7.roommates.common.interfaces.DescribeGameProxy;
import com.oose2013.group7.roommates.common.interfaces.Game;


/*** Context interface*/
public interface Context {

	public Game<?> getGame();
}
