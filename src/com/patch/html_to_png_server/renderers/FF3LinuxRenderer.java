package com.patch.html_to_png_server.renderers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.patch.html_to_png_server.renderers.helpers.ImageProcessor;

/*
 * Renders using the Firefox 3 engine through "gnome-web-photo".
 * 
 * For this renderer to function properly, "/usr/bin/gnome-web-photo" must
 * be present. It should accept two arguments (an HTML input filename and a
 * PNG output filename).
 * 
 * The PNG output will be cropped using ImageProcessor.autocropImage().
 */
public class FF3LinuxRenderer extends AbstractRenderer {
	public FF3LinuxRenderer() throws IOException {
		super();
	}

	@Override
	public void generatePngFile() throws IOException {
		File midPngFile = File.createTempFile("html_to_png_ff3_mid", ".png");
		midPngFile.deleteOnExit();
		
		ProcessBuilder pb = new ProcessBuilder(
				"/usr/bin/gnome-web-photo",
				htmlFile.getAbsolutePath(),
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
}
