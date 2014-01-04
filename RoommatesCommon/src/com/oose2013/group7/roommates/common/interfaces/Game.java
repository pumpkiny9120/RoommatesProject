package com.oose2013.group7.roommates.common.interfaces;

import java.io.IOException;

/***
 * Every game implements this interface. It adds listeners to the game i.e. an
 * inner class in a Session that consists of users playing the game
 ***/
public interface Game<T extends GameEvent> {
	

	public void addListener(EventListener<? super T> listener);

	public void removeListener(EventListener<? super T> listener);

	public void notifyListeners(GameEvent e);
}
