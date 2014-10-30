package com.koolyun.mis.line;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;

public class LineFactory {

	public static Line getCurrentLine() {
		String server = "211.147.75.226";
		boolean retval = pingServer(server, 5000);
		if (true == retval)
			return new Online();
		else {
			System.out.println("FAILURE - ping " + server);
			return new Offline();
		}
	}

	public static Line getOffLine() {
		return new Offline();
	}

	public static int isAddressAvailable(String ip) {
		try {
			InetAddress address = InetAddress.getByName(ip);// ping this IP
			if (address.isReachable(300000)) {
				System.out.println("SUCCESS - ping " + ip + " with no interface specified");
				return 0;
			} else {
				System.out.println("FAILURE - ping " + ip + " with no interface specified");
				return -1;
			}
		} catch (Exception e) {
			System.out.println("error occurs.");
			return -2;
		}
	}

	/*
	 * 能否ping通IP地址
	 * 
	 * @param server IP地址
	 * 
	 * @param timeout 超时时长(毫秒)
	 * 
	 * @return true能ping通
	 */
	public static boolean pingServer(String server, int timeout) {
		BufferedReader in = null;
		Runtime r = Runtime.getRuntime();
		String pingCommand = "ping " + " -c 1 -s 1 -w " + timeout + " " + server;

		try {
			Process p = r.exec(pingCommand);
			if (p == null) {
				return false;
			}
			InputStream inputstream = p.getInputStream();
			Reader reader = new InputStreamReader(inputstream);
			in = new BufferedReader(reader);
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.contains("1 received")) {
					System.out.println("SUCCESS - ping " + server);
					return true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
