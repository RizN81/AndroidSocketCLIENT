
package com.riz.rizdroid.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Client extends Activity {
	
	private Socket socket;
	
	private static       int      SERVERPORT   = 6001;
	private static       String   SERVER_IP    = "10.0.2.2";
	private static final String   TAG          = "RizDroid Client";
	private              TextView ip, response = null;
	private EditText serverIP, serverPort;
	boolean isConnected = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ip = (TextView) findViewById(R.id.ip);
		response = (TextView) findViewById(R.id.response);
		serverIP = (EditText) findViewById(R.id.serverIP);
		serverPort = (EditText) findViewById(R.id.serverPort);
		//connect();
	}
	
	private void connect() {
		
		if ( !TextUtils.isEmpty(serverIP.getText()) ) {
			SERVER_IP = serverIP.getText().toString();
			String port = serverPort.getText().toString();
			new Thread(new ClientThread()).start();
			isConnected = true;
		}
		else {
			Toast.makeText(this, "Enter Server IP first", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	public void onClick(View view) {
		String result = "";
		String port   = serverPort.getText().toString();
		String ip = serverIP.getText().toString();
		try {
			EditText et   = (EditText) findViewById(R.id.requestText);
			String   data = et.getText().toString();
			result = sendResponse(ip, port, data);
			response.setText(result);
		}
		
		catch (Exception e) {
			Log.e(TAG, "Error:" + e.getMessage());
		}
	}
	
	/**
	 * Send response data to server.
	 *
	 * @param hostIP
	 * @param port
	 * @param Data
	 *
	 * @return response result
	 */
	public String sendResponse(String hostIP, String port, String Data) {
		
		String result = "";
		try {
			SocketTask task = new SocketTask();
			task.execute("SendData", hostIP, port, Data);
			result = task.get();
		}
		catch (InterruptedException e) {
			Log.e(TAG, "Error :- " + e.getMessage());
		}
		catch (ExecutionException e) {
			Log.e(TAG, "Error :- " + e.getMessage());
		}
		return result;
	}
	
	/**
	 * @author Riz Async task handler class
	 */
	public class SocketTask extends AsyncTask<String, Void, String> {
		
		String result = "NO_RESULT";
		
		@Override
		protected String doInBackground(String... params) {
			
			SocketDataHandler openSocketTask = new SocketDataHandler();
			
			if ( params[0].equals("SendData") ) {
				result = openSocketTask.send(params[1], Integer.valueOf(params[2]), params[3]);
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
		}
	}
	
	/**
	 * @author Riz Common background socket data handler
	 */
	private class SocketDataHandler {
		
		public String send(String hostIp, int hostPort, String data) {
			
			Socket           socket           = null;
			DataOutputStream dataOutputStream = null;
			DataInputStream  dataInputStream  = null;
			String           response         = "";
			
			try {
				socket = new Socket(hostIp, hostPort);
				socket.setSoTimeout(10000);
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataInputStream = new DataInputStream(socket.getInputStream());
				
				if ( data != null ) {
					dataOutputStream.writeUTF(data);
				}
				
				response = dataInputStream.readUTF();
				Log.i(TAG, "Response From Java APP " + response);
				
			}
			catch (UnknownHostException e) {
				
				response = "UnknownHostException: " + e.toString();
				Log.e(TAG, e.getMessage());
			}
			catch (IOException e) {
				
				response = "IOException: " + e.toString();
				Log.e(TAG, e.getMessage());
			}
			finally {
				if ( socket != null ) {
					try {
						socket.close();
					}
					catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
				
				if ( dataOutputStream != null ) {
					try {
						dataOutputStream.close();
					}
					catch (IOException e) {
						Log.e(TAG, e.getMessage());
						
					}
				}
				
				if ( dataInputStream != null ) {
					try {
						dataInputStream.close();
					}
					catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
			return response;
			
		}
		
	}
	
	class ClientThread implements Runnable {
		
		@Override
		public void run() {
			
			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
				socket = new Socket(serverAddr, SERVERPORT);
				
			}
			catch (UnknownHostException e) {
				Log.e(TAG, "Error:" + e.getMessage());
			}
			catch (IOException e) {
				Log.e(TAG, "Error:" + e.getMessage());
			}
			
		}
		
	}
}