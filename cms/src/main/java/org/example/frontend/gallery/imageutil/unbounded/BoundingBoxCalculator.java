package org.example.frontend.gallery.imageutil.unbounded;

import java.awt.Dimension;


public final class BoundingBoxCalculator {
	
	private BoundingBoxCalculator() {
		
	}
    
	public static Dimension getCalculatedBoundingBoxDimensions(Dimension boundingBox, Dimension original) {
    	int width = boundingBox.width;
        int height = boundingBox.height;
        if (hasUnboundedWidthAndHeight(boundingBox)) {
            width = original.width;
            height = original.height;
        } else if (hasUnboundedWidth(boundingBox)) {
            width = (int) Math.round(aspectRatio(original) * height);
        } else if (hasUnboundedHeight(boundingBox)) {
            height = (int) Math.round( width / aspectRatio(original) );
        }
        return new Dimension(width, height);
    }


    private static double aspectRatio(Dimension dimension) {
        return dimension.getWidth() / dimension.getHeight();
    }

    private static boolean hasUnboundedWidthAndHeight(Dimension dimension) {
        return dimension.height <= 0 && dimension.width <= 0;
    }

    private static boolean hasUnboundedHeight(Dimension dimension) {
        return dimension.height <= 0;
    }

    private static boolean hasUnboundedWidth(Dimension dimension) {
        return dimension.width <= 0;
    }

}
