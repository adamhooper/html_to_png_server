package com.patch.html_to_png_server;

import java.io.IOException;

import com.patch.html_to_png_server.http.HtmlToPngHttpServer;

public class Main {
	public static void main(String[] args) {
		Options options = new Options();
		HtmlToPngHttpServer server = new HtmlToPngHttpServer(options);
		try {
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
