package com.patch.html_to_png_server.renderers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

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
		ProcessBuilder pb = new ProcessBuilder(
				getExeFile().getPath(),
				htmlFile.toURI().toURL().toString(),
				pngFile.getAbsolutePath());
		Process p = pb.start();
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			p.destroy();
			throw new IOException(e);
		}
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
