package org.example.frontend.gallery.imageutil;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.hippoecm.frontend.plugins.gallery.imageutil.ImageUtils;


class ImageScaler {

    private BufferedImage original;
    private Dimension boundingBox;
    private BufferedImage target;
    private double aspectRatioOriginal;
    private double aspectRatioBoundingBox;
    private ImageUtils.ScalingStrategy strategy;
    private boolean upscaling;

    public ImageScaler(final BufferedImage original, final Dimension boundingBox) {
    	strategy = ImageUtils.ScalingStrategy.AUTO;
        if (original == null || boundingBox == null) {
            throw new IllegalArgumentException("Orginal or boundingBox should not be null");
        }
        this.original = original;
        aspectRatioOriginal = (double) original.getWidth(null) / (double) original.getHeight(null);
        this.boundingBox = boundingBox;
        aspectRatioBoundingBox = boundingBox.getWidth() / boundingBox.getHeight();
    }

    public BufferedImage getTarget() {
        return target;
    }

    public void setStrategy(final ImageUtils.ScalingStrategy strategy) {
        this.strategy = strategy;
    }
	
    public void setUpscaling(final boolean upscaling) {
        this.upscaling = upscaling;
    }

    public void scale() {
        if (shouldScale()) {
            target = ImageUtils.scaleImage(original, getWidthToScaleTo(), getHeightToScaleTo(), strategy);
        } else {
            target = original;
        }
    }

    private int getWidthToScaleTo() {
        return shouldScaleToHeight() ? (int) Math.round(aspectRatioOriginal * boundingBox.getHeight()) : boundingBox.width;
    }

    private int getHeightToScaleTo() {
        return shouldScaleToHeight() ? boundingBox.height : (int) Math.round(boundingBox.getWidth() / aspectRatioOriginal);
    }

    private boolean shouldScale() {
    	return shouldDownscale() || shouldUpscale();
	}
    
	private boolean shouldDownscale() {
        return (boundingBox.width < original.getWidth() || boundingBox.height < original.getHeight())
        		&& boundingBox.width != original.getWidth() && boundingBox.height != original.getHeight()
        		&& boundingBox.width > 0 && boundingBox.height > 0;
    }
	
    private boolean shouldUpscale() {
        return boundingBox.width > original.getWidth() && boundingBox.height > original.getHeight() 
        		&& upscaling
        		&& boundingBox.width > 0 && boundingBox.height > 0;
    }

    private boolean shouldScaleToHeight() {
        return aspectRatioOriginal >= aspectRatioBoundingBox;
    }

}
