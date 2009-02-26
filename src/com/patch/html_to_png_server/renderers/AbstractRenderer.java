package com.patch.html_to_png_server.renderers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class AbstractRenderer {
	protected File htmlFile;
	protected File pngFile;
	
	public AbstractRenderer() throws IOException {
		htmlFile = File.createTempFile("html_to_png", ".html");
		htmlFile.deleteOnExit();

		pngFile = File.createTempFile("html_to_png", ".png");
		pngFile.deleteOnExit();
	}
	
	public void setHtml(InputStream is) throws IOException {
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		FileWriter fw = new FileWriter(htmlFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		char[] buf = new char[1024];
		int len;
		while ((len = br.read(buf, 0, 1024)) > 0) {
			bw.write(buf, 0, len);
		}
		
		bw.close();
	}
	
	public File getPngFile() {
		return this.pngFile;
	}
	
	public void cleanUp() throws IOException {
		htmlFile.delete();
		pngFile.delete();
	}
	
	public abstract void generatePngFile() throws IOException;
}
