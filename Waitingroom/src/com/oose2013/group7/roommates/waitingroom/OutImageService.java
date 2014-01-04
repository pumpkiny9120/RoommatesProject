package com.oose2013.group7.roommates.waitingroom;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A TCP/IP connection thread called when the user uploads a photo.
 * The photo's name follows this rule: uploader's username + '~' + photoname.
 * e.g. If user lbwdruid uploads a new photo named "240X360.png", the new name is : lbwdruid~240X360.png.
 * Just photo's path is needed.
 * @author lbowen5
 */

public class OutImageService {
	
	private Socket socket;
	private static final int SERVERPORT=4455;
	private static final String SERVER_IP="10.164.22.29";
	
	public OutImageService(){
		new Thread(new ClientThread()).start();
	}
	
	public void sendMessage(String message){
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
			out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendImage(String path, String username){
		
		int index, index2;
		for (index=path.length()-1; index>=0; index--)
			if (path.charAt(index)=='/') break;
		
		for (index2=path.length()-1; index2>=0; index2--)
			if (path.charAt(index2)=='.') break;
		String name=path.substring(index+1, index2);
		
		sendMessage("Upload Image: "+username+"~"+name);
		OutputStream os;
		try {
			os = socket.getOutputStream();
			sendFile(os, path);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void sendFile(OutputStream os, String fileName) throws Exception
    {
        // File to send
        File myFile = new File(fileName);
        int fSize = (int) myFile.length();
        
        // Send the file's size
        byte[] bSize = new byte[4];
        bSize[0] = (byte) ((fSize & 0xff000000) >> 24);
        bSize[1] = (byte) ((fSize & 0x00ff0000) >> 16);
        bSize[2] = (byte) ((fSize & 0x0000ff00) >> 8);
        bSize[3] = (byte) (fSize & 0x000000ff);
        // 4 bytes containing the file size
        os.write(bSize, 0, 4);

        // In case of memory limitations set this to false
        boolean noMemoryLimitation = true;

        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try
        {
            if (noMemoryLimitation)
            {
                // Use to send the whole file in one chunk
                byte[] outBuffer = new byte[fSize];
                int bRead = bis.read(outBuffer, 0, outBuffer.length);
                os.write(outBuffer, 0, bRead);
            }
            else
            {
                // Use to send in a small buffer, several chunks
                int bRead = 0;
                byte[] outBuffer = new byte[8 * 1024];
                while ((bRead = bis.read(outBuffer, 0, outBuffer.length)) > 0)
                {
                    os.write(outBuffer, 0, bRead);
                }
            }
            os.flush();
        }
        finally
        {
            bis.close();
        }
    }
    
	class ClientThread implements Runnable {
		@Override
		public void run(){
			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
				socket = new Socket(serverAddr, SERVERPORT);
			} catch (UnknownHostException e){
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}