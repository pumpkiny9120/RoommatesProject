package com.oose2013.group7.roommates.waitingroom;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Environment;

/**
 * A TCP/IP connection thread called when waitingroom loads.
 * It will download a whole image directory from the server in a specific socket 4453.
 * The thread closes the socket and stops itself after the transpotation is done.
 * @author lbowen5
 */

public class InImageService {
	
	private Socket socket;
	private static final int SERVERPORT=4453;
	private static final String SERVER_IP="10.164.22.29";
	
	public InImageService(){
		new Thread(new ClientThread()).start();
	}
	
    public void receiveFile() throws Exception {
    	
    	BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
    	DataInputStream dis = new DataInputStream(bis);

    	int filesCount = dis.readInt();
    	File[] files = new File[filesCount];
    	
    	File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Roommates/WaitingRoom/");
    	if (file.exists()) file.delete();
   		file.mkdirs();

    	for(int i = 0; i < filesCount; i++)
    	{
    	    long fileLength = dis.readLong();
    	    String fileName = dis.readUTF();

    	    files[i] = new File(Environment.getExternalStorageDirectory().getPath()+"/Roommates/WaitingRoom/" + fileName);

    	    FileOutputStream fos = new FileOutputStream(files[i]);
    	    BufferedOutputStream bos = new BufferedOutputStream(fos);

    	    for(int j = 0; j < fileLength; j++) bos.write(bis.read());

    	    bos.close();
    	}

    	dis.close();
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
			try {
				receiveFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}