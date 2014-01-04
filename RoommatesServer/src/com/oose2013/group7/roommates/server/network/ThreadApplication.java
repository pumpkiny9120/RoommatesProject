package com.oose2013.group7.roommates.server.network;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class manages all three sockets. Given a socket number and type, it will open corresponding socket.
 * @author lbowen5
 */

public class ThreadApplication extends Thread {
	
	private int portNumber;
	private String portType;
	boolean listening = true;
	
	public ThreadApplication(int portNumber, String portType) {
		this.portNumber=portNumber;
		this.portType=portType;
	}
	
	public void run() {
		if (portType=="InImage") InImage();
		if (portType=="OutImage") OutImage();
		if (portType=="InText") InText();
    }
	
	private void InImage(){
		System.out.println("In image service established, on socket: "+portNumber);
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
	            new InImageThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
		System.out.println("In image service ends.");
	}
	
	private void OutImage(){
		System.out.println("Out image service established, on socket: "+portNumber);
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
	            new OutImageThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
		System.out.println("Out image service ends.");
	}
	
	private void InText(){
		System.out.println("In text service established, on socket: "+portNumber);
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
	            new InTextThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
		System.out.println("In text service ends.");
	}
}