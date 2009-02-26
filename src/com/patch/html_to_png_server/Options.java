package com.patch.html_to_png_server;

public class Options {
	private int port = 0x504e; // "PN", as in "PNG". Amounts to 20558

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
