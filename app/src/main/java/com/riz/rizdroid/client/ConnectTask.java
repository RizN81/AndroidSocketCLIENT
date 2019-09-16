package com.riz.rizdroid.client;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectTask extends AsyncTask<String, String, String> {
	String serverIP;
	int    port;
	public TcpClient tcpClient;
	
	public ConnectTask(String serverIP, int port) {
		
		this.serverIP = serverIP;
		this.port = port;
	}
	
	public TcpClient getTcpClient() {
		
		return tcpClient;
	}
	
	public void setTcpClient(TcpClient tcpClient) {
		
		this.tcpClient = tcpClient;
	}
	
	@Override
	protected String doInBackground(String... message) {
		
		//we create a TCPClient object
		tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
			@Override
			//here the messageReceived method is implemented
			public void messageReceived(String message) {
				//this method calls the onProgressUpdate
				publishProgress(message);
			}
		}, serverIP, port);
		
		tcpClient.run();
		return "";
		
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		
		super.onProgressUpdate(values);
		//response received from server
		Log.d("test", "response " + values[0]);
		//process server response here....
		
	}
}