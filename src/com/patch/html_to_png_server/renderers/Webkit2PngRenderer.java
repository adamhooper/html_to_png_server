package com.patch.html_to_png_server.renderers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.patch.html_to_png_server.renderers.helpers.ImageProcessor;

/*
 * Renders using "webkit2png" on OSX.
 * 
 * See http://github.com/paulhammond/webkit2png/tree/master
 */
public class Webkit2PngRenderer extends AbstractRenderer {
	private static File scriptFile;
	
	public Webkit2PngRenderer() throws IOException {
		super();
	}
	
	@Override
	public void generatePngFile() throws IOException {
		File midPngFile =
			File.createTempFile("html_to_png_webkit2png_mid", "-full.png");
		midPngFile.deleteOnExit();
		
		String midPngFilePath = midPngFile.getAbsolutePath();
		String midPngFilePrefix =
			midPngFilePath.substring(0, midPngFilePath.length() - 9);
		
		ProcessBuilder pb = new ProcessBuilder(
				"python",
				getScriptFile().getPath(),
				htmlFile.toURI().toURL().toString(),
				"-o", midPngFilePrefix,
				"-W", "1024", "-H", "768",
				"-F");
		System.err.println("About to start process");
		Process p = pb.start();
		System.err.println("Started process");
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			p.destroy();
			throw new IOException(e);
		}
		System.err.println("Process complete");

		BufferedImage midImage = ImageIO.read(midPngFile);
		BufferedImage finalImage =
			(new ImageProcessor()).autocropImage(midImage);
		ImageIO.write(finalImage, "png", pngFile);
		
		midPngFile.delete();
	}
	
	private static File getScriptFile() throws IOException {
		if (scriptFile != null) return scriptFile;
		
		scriptFile = File.createTempFile("html_to_png_ie-webkit2png", ".py");
		scriptFile.deleteOnExit();
		
		InputStream is = IERenderer.class.getResourceAsStream("webkit2png.py");
		
		FileChannel scriptChannel =
			new FileOutputStream(scriptFile).getChannel();
		scriptChannel.transferFrom(
			Channels.newChannel(is), 0, Integer.MAX_VALUE);
		
		is.close();
		scriptChannel.close();
		
		return scriptFile;
	}
}
