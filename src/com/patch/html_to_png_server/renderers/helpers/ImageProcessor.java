package com.patch.html_to_png_server.renderers.helpers;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ImageProcessor {
	public BufferedImage autocropImage(BufferedImage image) {
		int top = -1;
		int left = -1;
		int right = -1;
		int bottom = -1;
		
		WritableRaster raster = image.getRaster();
		
		int[] edgeColor = new int[4];
		int[] curPixelColor = new int[edgeColor.length];
		
		raster.getPixel(0, 0, edgeColor);
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				raster.getPixel(j, i, curPixelColor);
				
				for (int k = 0; k < edgeColor.length; k++) {
					if (edgeColor[k] != curPixelColor[k]) {
						top = i;
						break;
					}
				}
			}
			if (top != -1) break;
		}
		
		for (int i = raster.getHeight() - 1; i >= top; i--) {
			for (int j = 0; j < image.getWidth(); j++) {
				raster.getPixel(j, i, curPixelColor);
				
				for (int k = 0; k < edgeColor.length; k++) {
					if (edgeColor[k] != curPixelColor[k]) {
						bottom = i;
						break;
					}
				}
			}
			if (bottom != -1) break;
		}
		
		for (int j = 0; j < raster.getWidth(); j++) {
			for (int i = top; i <= bottom; i++) {
				raster.getPixel(j, i, curPixelColor);
				
				for (int k = 0; k < edgeColor.length; k++) {
					if (edgeColor[k] != curPixelColor[k]) {
						left = j;
						break;
					}
				}
			}
			if (left != -1) break;
		}
		
		for (int j = raster.getWidth() - 1; j >= left; j--) {
			for (int i = top; i <= bottom; i++) {
				raster.getPixel(j, i, curPixelColor);
				
				for (int k = 0; k < edgeColor.length; k++) {
					if (edgeColor[k] != curPixelColor[k]) {
						right = j;
						break;
					}
				}
			}
			if (right != -1) break;
		}
		
		return image.getSubimage(left, top, right - left, bottom - top);
	}
}
