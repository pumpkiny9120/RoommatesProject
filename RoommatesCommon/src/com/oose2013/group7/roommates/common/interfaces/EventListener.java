package com.oose2013.group7.roommates.common.interfaces;

import java.io.IOException;

/***
 * An event listener interface. The class that implements the method basically
 * pushes data to the clients
 ***/
public interface EventListener<T extends Event> {
	public void eventReceived(T event) throws IOException;

}
