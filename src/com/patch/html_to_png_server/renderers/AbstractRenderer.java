package com.patch.html_to_png_server.renderers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Renders HTML text to PNG.
 * 
 * Subclasses must implement "generatePngFile()", which should read from the
 * protected File "htmlFile" and write to the protected file "pngFile".
 */
public abstract class AbstractRenderer {
	protected File htmlFile;
	protected File pngFile;
	
	public AbstractRenderer() throws IOException {
		htmlFile = File.createTempFile("html_to_png", ".html");
		htmlFile.deleteOnExit();

		pngFile = File.createTempFile("html_to_png", ".png");
		pngFile.deleteOnExit();
	}
	
	/*
	 * Provides the HTML to this renderer, via an InputStream.
	 */
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
	
	/*
	 * Returns the output PNG File. This should only be called after
	 * generatePngFile().
	 */
	public File getPngFile() {
		return this.pngFile;
	}
	
	/*
	 * Deletes temporary files used during the lifetime of this Renderer.
	 */
	public void cleanUp() throws IOException {
		htmlFile.delete();
		pngFile.delete();
	}
	
	/*
	 * Reads from the protected File "htmlFile" and writes to the protected
	 * File "pngFile".
	 */
	public abstract void generatePngFile() throws IOException;
}
