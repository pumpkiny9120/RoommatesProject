package com.oose2013.group7.roommates.server.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This thread takes care of the user downloading.
 * It will send a whole directory to the user.
 * @author lbowen5
 */

public class OutImageThread extends Thread{
	
	private Socket socket = null;
	private OutputStream os;
	private PrintWriter out;

    public OutImageThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        System.out.println("An out Image connection established. Client's socket:"+socket.getPort());
    }
	
    public void run(){
		try {
			os = socket.getOutputStream();
			out=new PrintWriter(os, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	sendImages();
    }
    
    public void sendImages(){
    	try {
			send();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void sendMessage(String message){
		if (out!=null && !out.checkError()) {
			out.println(message);
			out.flush();
		}
	}

    
    public void send() throws IOException{
    	String directory = "E:/Roommates Server/Waiting Room/";
		File[] files = new File(directory).listFiles();

		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeInt(files.length);

		for(File file : files)
		{
		    long length = file.length();
		    dos.writeLong(length);

		    String name = file.getName();
		    dos.writeUTF(name);

		    FileInputStream fis = new FileInputStream(file);
		    BufferedInputStream bis = new BufferedInputStream(fis);

		    int theByte = 0;
		    while((theByte = bis.read()) != -1) bos.write(theByte);

		    bis.close();
		    System.out.println(name+" has been sent.");
		}

		dos.close();

    }
    
    
	public void sendFile(OutputStream os, String fileName) throws Exception {
		
		
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
}
