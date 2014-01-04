package com.oose2013.group7.roommates.common.interfaces;

/**
 * The common interface for the DescribeGame that both server and client
 * implement
 ***/
public interface DescribeGameProxy{

	/**
	 * Client sends his description for the current word along with his userName
	 **/
	public void setDescription(String text, String userName);
}
