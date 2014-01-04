package com.oose2013.group7.roommates.server.network;

import java.net.*;
import java.io.*;

/**
 * This thread takes care of the user uploading.
 * It will receive only one image at one time.
 * @author lbowen5
 */

public class InImageThread extends Thread {
    
	private Socket socket = null;

    public InImageThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        System.out.println("In image connection established. Client's socket:"+socket.getPort());
    }
    
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
        	String inputLine;
            while ((inputLine=in.readLine()) != null) {
            	String temp="";
            	for (int i=0; i<13; i++)
            		temp+=inputLine.charAt(i);
            	if (temp.compareTo("Upload Image:")==0)
            		try {
        				receiveFile(inputLine.substring(14));
	        			} catch (Exception e) {
	       				e.printStackTrace();
	       			}
            }
            
            System.out.println("Image socket:"+socket.getPort()+" is closed.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void receiveFile(String fileName) throws Exception {
    	
    	fileName = fileName+".png";
    	
    	InputStream is=socket.getInputStream();
    	
    	System.out.println("I get an image here. Name: " + fileName);
    	
        File file=new File("E:/Roommates Server/Waiting Room");
   		if (!file.exists()) file.mkdirs();
        String imageInSD = "E:/Roommates Server/Waiting Room/" + fileName;

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