package com.patch.html_to_png_server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.patch.html_to_png_server.Options;
import com.patch.html_to_png_server.renderers.AbstractRenderer;
import com.patch.html_to_png_server.renderers.FF3LinuxRenderer;
import com.patch.html_to_png_server.renderers.IERenderer;
import com.sun.net.httpserver.HttpServer;

public class HtmlToPngHttpServer {
	private Options options;
	
	private static Map<String, Class<? extends AbstractRenderer>> strategies
		= new HashMap<String, Class<? extends AbstractRenderer>>();
	static {
		strategies.put("/ff3-linux", FF3LinuxRenderer.class);
		strategies.put("/ie", IERenderer.class);
	}
	
	public HtmlToPngHttpServer(Options options) {
		this.options = options;
	}
	
	public void run() throws IOException {
		HttpServer server = HttpServer.create(
				new InetSocketAddress(options.getPort()), 0);
		
		for (String key : strategies.keySet()) {
			Class<? extends AbstractRenderer> clazz = strategies.get(key);
			server.createContext(key, new HtmlToPngHttpHandler(clazz));
		}
		
		server.setExecutor(null);
		server.start();
	}
}
