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
 * Renders using the platform's built-in Internet Explorer 6 or 7.
 * 
 * The necessary program, "ie-thumbnail-generator.exe", is packaged here. Its
 * source code is provided in the "ie_thumbnail_generator" folder.
 */
public class IERenderer extends AbstractRenderer {
	private static File exeFile;
	
	public IERenderer() throws IOException {
		super();
	}

	@Override
	public void generatePngFile() throws IOException {
		File midPngFile = File.createTempFile("html_to_png_ff3_mid", ".png");
		midPngFile.deleteOnExit();
		
		ProcessBuilder pb = new ProcessBuilder(
				getExeFile().getPath(),
				htmlFile.toURI().toURL().toString(),
				midPngFile.getAbsolutePath());
		Process p = pb.start();
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			p.destroy();
			throw new IOException(e);
		}

		BufferedImage midImage = ImageIO.read(midPngFile);
		BufferedImage finalImage =
			(new ImageProcessor()).autocropImage(midImage);
		ImageIO.write(finalImage, "png", pngFile);
		
		midPngFile.delete();
	}

	private static File getExeFile() throws IOException {
		if (exeFile != null) return exeFile;
		
		exeFile = File.createTempFile(
				"html_to_png_ie-thumbnail-generator", ".exe");
		exeFile.deleteOnExit();
		
		InputStream is = IERenderer.class.getResourceAsStream(
				"ie-thumbnail-generator.exe");
		
		FileChannel exeChannel = new FileOutputStream(exeFile).getChannel();
		exeChannel.transferFrom(Channels.newChannel(is), 0, Integer.MAX_VALUE);
		
		is.close();
		exeChannel.close();
		
		return exeFile;
	}
}
