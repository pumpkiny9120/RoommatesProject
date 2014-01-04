package com.oose2013.group7.roommates.common.commands;

import com.oose2013.group7.roommates.common.interfaces.DescribeGameAbstract;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameProxy;

/***A Context interface to be passed to the Command object**/
public interface GameContext{

	public DescribeGameProxy getGame();
	
}
