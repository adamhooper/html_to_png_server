package com.patch.html_to_png_server.renderers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import javax.imageio.ImageIO;

import com.patch.html_to_png_server.renderers.helpers.ImageProcessor;

/*
 * Renders by proxying to an existing Firefox instance running MozRepl
 * with its "screenshot" interactor.
 * 
 * For MozRepl installation instructions, see
 * http://hyperstruct.net/2009/2/5/turning-firefox-into-a-screenshot-server-with-mozrepl
 * Use the JavaScript file supplied in "contrib/moz-repl-screenshot-server.js".
 * 
 * MozRepl must be running on localhost, port 4242.
 * 
 * The final image will be autocropped.
 */
public class MozReplRenderer extends AbstractRenderer {
	public MozReplRenderer() throws IOException {
		super();
	}

	@Override
	public void generatePngFile() throws IOException {
		File midPngFile = File.createTempFile("html_to_png_ff3_mid", ".png");
		midPngFile.deleteOnExit();
		
		URL htmlUrl = htmlFile.toURI().toURL();
		URL screenshotUrl =
			new URL("http://localhost:4242/screenshot/" + htmlUrl);
		InputStream is = screenshotUrl.openStream();
		ReadableByteChannel ic = Channels.newChannel(is);
		FileOutputStream os = new FileOutputStream(midPngFile);
		FileChannel oc = os.getChannel();
		oc.transferFrom(ic, 0, Integer.MAX_VALUE);
		
		BufferedImage midImage = ImageIO.read(midPngFile);
		BufferedImage finalImage =
			(new ImageProcessor()).autocropImage(midImage);
		ImageIO.write(finalImage, "png", pngFile);
		
		midPngFile.delete();
	}
}
