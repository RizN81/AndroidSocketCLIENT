package com.riz.rizdroid.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public class TcpClient {
	
	public String SERVER_IP   = "192.168.1.8"; //server IP address
	public int    SERVER_PORT = 1234;
	// message to send to the server
	private String mServerMessage;
	// sends message received notifications
	private OnMessageReceived mMessageListener = null;
	// while this is true, the server will continue running
	private boolean           mRun             = false;
	// used to send messages
	private PrintWriter    mBufferOut;
	// used to read messages from the server
	private BufferedReader mBufferIn;
	
	/**
	 * Constructor of the class. OnMessagedReceived listens for the messages received from server
	 */
	public TcpClient(OnMessageReceived listener, String serverIP, int serverPort) {
		
		mMessageListener = listener;
		SERVER_IP = serverIP;
		SERVER_PORT = serverPort;
	}
	
	/**
	 * Sends the message entered by client to the server
	 *
	 * @param message text entered by client
	 */
	public void sendMessage(String message) {
		
		if ( mBufferOut != null && !mBufferOut.checkError() ) {
			mBufferOut.println(message);
			mBufferOut.flush();
		}
	}
	
	/**
	 * Close the connection and release the members
	 */
	public void stopClient() {
		mRun = false;
		if ( mBufferOut != null ) {
			mBufferOut.flush();
			mBufferOut.close();
		}
		mMessageListener = null;
		mBufferIn = null;
		mBufferOut = null;
		mServerMessage = null;
	}
	
	public void run() {
		
		mRun = true;
		
		try {
			//here you must put your computer's IP address.
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
			Log.e("TCP Client", "C: Connecting...");
			//create a socket to make the connection with the server
			Socket socket = new Socket(serverAddr, SERVER_PORT);
			
			try {
				
				//sends the message to the server
				mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				
				//receives the message which the server sends back
				mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//in this while the client listens for the messages sent by the server
				while (mRun) {
					mServerMessage = mBufferIn.readLine();
					if ( mServerMessage != null && mMessageListener != null ) {
						//call the method messageReceived from MyActivity class
						mMessageListener.messageReceived(mServerMessage);
					}
				}
				Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
			}
			catch (Exception e) {
				Log.e("TCP", "S: Error", e);
			}
			finally {
				//the socket must be closed. It is not possible to reconnect to this socket
				// after it is closed, which means a new socket instance has to be created.
				socket.close();
			}
		}
		catch (Exception e) {
			Log.e("TCP", "C: Error", e);
		}
	}
	
	//Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
	//class at on asynckTask doInBackground
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
	
}