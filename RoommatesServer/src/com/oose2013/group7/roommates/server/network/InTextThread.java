package com.oose2013.group7.roommates.server.network;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.awt.event.*;

public class InTextThread extends Thread {
    
	private Socket socket = null;
	private ArrayList listenerList=new ArrayList();
	
	private InputStream inputstream;

    public InTextThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        System.out.println("Connection established. Client's socket:"+socket.getPort());
    }
    
    public void run() {
    	try {
			inputstream=socket.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
    	if (inputLine.compareTo("Upload Image!")==0) {
    		System.out.println("I get an image here.");
    		try {
				receiveFile(inputstream);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return;
    	}
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
    
    
    public void receiveFile(InputStream is) throws Exception
    {
        String fileName = "myFile.png";
        String imageInSD = "E:/Roommates Server/" + fileName;

        // read first 4 bytes containing the file size
        byte[] bSize = new byte[4];
        is.read(bSize, 0, 4);

        int filesize;
        filesize = (int) (bSize[0] & 0xff) << 24 | 
                   (int) (bSize[1] & 0xff) << 16 | 
                   (int) (bSize[2] & 0xff) << 8 | 
                   (int) (bSize[3] & 0xff);

        int bytesRead;
        // You may but don't have to read the whole file in memory
        // 8k buffer is good enough
        byte[] data = new byte[8 * 1024];
        int bToRead;
        FileOutputStream fos = new FileOutputStream(imageInSD);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        while (filesize > 0)
        {
            // EDIT: just in case there is more data in the stream. 
            if (filesize > data.length) bToRead=data.length;
            else bToRead=filesize;
            bytesRead = is.read(data, 0, bToRead);
            if (bytesRead > 0)
            {
                bos.write(data, 0, bytesRead);
                filesize -= bytesRead;
            }
        }
        bos.close();
    }
}