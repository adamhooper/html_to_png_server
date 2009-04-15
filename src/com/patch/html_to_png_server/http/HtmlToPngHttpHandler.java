package com.patch.html_to_png_server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import com.patch.html_to_png_server.renderers.AbstractRenderer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HtmlToPngHttpHandler implements HttpHandler {
	Class<? extends AbstractRenderer> renderClass;
	
	public HtmlToPngHttpHandler(Class<? extends AbstractRenderer> renderClass) {
		this.renderClass = renderClass;
	}

	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			doHandle(httpExchange);
		} catch (IOException e) {
			e.printStackTrace();
			throw(e);
		}
	}
	
	private void doHandle(HttpExchange httpExchange) throws IOException {
		AbstractRenderer renderer;
		try {
			renderer = renderClass.newInstance();
		} catch (InstantiationException e) {
			throw new IOException(e);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		
		InputStream is = httpExchange.getRequestBody();
		
		renderer.setHtml(is);
		renderer.generatePngFile();
		
		File pngFile = renderer.getPngFile();
		
		FileChannel pngChannel = new FileInputStream(pngFile).getChannel();
		
		httpExchange.getResponseHeaders().set("Content-Type", "image/png");
		httpExchange.sendResponseHeaders(200, pngChannel.size());
		
		WritableByteChannel bodyChannel
			= Channels.newChannel(httpExchange.getResponseBody());
		
		pngChannel.transferTo(0, pngChannel.size(), bodyChannel);

		pngChannel.close();
		renderer.cleanUp();
		httpExchange.close();
	}
}
