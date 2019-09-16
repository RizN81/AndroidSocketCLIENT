
package com.riz.rizdroid.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

public class Utility {
	private static final String	TAG	= "RizDroid Utility";

	public Utility() {
	}

	public String getIpAddress() {

		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += inetAddress.getHostAddress();
					}

				}

			}

		}
		catch (SocketException e) {
			Log.e(TAG, "Error Occurred While getting ip: " + e.getMessage());
		}

		return ip;
	}

}
