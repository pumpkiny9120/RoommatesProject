package com.oose2013.group7.roommates.server.network;

import java.io.IOException;

/**
 * This is the main entrance for the server. It will run three sockets on the same time:
 * 4455: for user to upload images.
 * 4453: for user to download images.
 * 4456: for all other text-based commands.
 * @author lbowen5
 */
public class Application {

	public static void main(String[] args) throws IOException {
		
        int inImageSocket = 4455; // for image in
        int outImageSocket = 4453;
        
        System.out.println("Server started.");

        new ThreadApplication(inImageSocket, "InImage").start();
        new ThreadApplication(outImageSocket, "OutImage").start();
        
		NetworkManager.getNetworkManager().startServer();
		System.out.println("server stopped...");
	}
}