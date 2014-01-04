package com.oose2013.group7.roommates.server.network;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.awt.event.*;

public class ServerThread extends Thread {
    
	private Socket socket = null;
	private ArrayList listenerList=new ArrayList();

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        System.out.println("Connection established. Client's socket:"+socket.getPort());
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
        	
        	String inputLine;
        	out.println("Connection established. Your socket is "+socket.getPort());

            while ((inputLine=in.readLine()) != null) {
            	ProcessInput(inputLine, out);
            }
            System.out.println("Socket "+socket.getPort()+" closes.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void ProcessInput(String inputLine, PrintWriter out){
    	System.out.println("I got this line from client: "+inputLine);
    	out.println(inputLine);
    }
    
    private class ListenerNotifier implements Runnable {
    	
    	private String message;
    	
    	public ListenerNotifier(String msg) {
    		message=msg;
    	}
    	
		@Override
		public void run() {
			ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, message);
			for (Iterator iter=listenerList.iterator(); iter.hasNext();){
				ActionListener element=(ActionListener)iter.next();
				element.actionPerformed(ae);
			}
		}
    	
    }
}