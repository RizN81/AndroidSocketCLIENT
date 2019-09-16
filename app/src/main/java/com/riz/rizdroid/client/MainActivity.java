package com.riz.rizdroid.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	TcpClient client;
	EditText  txtServerIP;
	EditText  txtServerPort;
	EditText  data;
	Button    btnSend, btnConnect;
	boolean isConnected = false;
	ConnectTask connectTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtServerIP = (EditText) findViewById(R.id.serverIP);
		txtServerPort = (EditText) findViewById(R.id.serverPort);
		data = (EditText) findViewById(R.id.requestText);
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				connect();
			}
		});
		
		
		btnSend = (Button) findViewById(R.id.myButton);
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				client = connectTask.getTcpClient();
				if ( client != null ) {
					client.sendMessage("Hello");
				}
				
			}
		});
	}
	
	private void connect() {
		
		try {
			String serverIP   = txtServerIP.getText().toString();
			int    serverPort = Integer.valueOf(txtServerPort.getText().toString());
			connectTask = new ConnectTask(serverIP, serverPort);
			connectTask.execute("");
			client = connectTask.getTcpClient();
			isConnected = true;
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
