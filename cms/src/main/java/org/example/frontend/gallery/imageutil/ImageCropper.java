package org.example.frontend.gallery.imageutil;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ImageCropper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCropper.class);

    private int boundingBoxHeight;
    private int boundingBoxWidth;
    private int originalWidth;
    private int originalHeight;
    private BufferedImage original;
    private BufferedImage target;

    /**
     * @param original Original Image
     * @param boundingBox Bounding Box of target Image
     */
    public ImageCropper(final BufferedImage original, final Dimension boundingBox) {
        if (boundingBox == null) {
            LOGGER.warn("Bounding Box should not be null, cropping will set original as target ");
            throw new IllegalArgumentException("boundingBox should not be null");
        }
        if (original == null) {
            LOGGER.warn("original should not be null, cropping will set nulll as target ");
            throw new IllegalArgumentException("original should not be null");
        }
        this.original = original;
        originalWidth = original.getWidth(null);
        originalHeight = original.getHeight(null);
        boundingBoxHeight = (int) boundingBox.getHeight();
        boundingBoxWidth = (int) boundingBox.getWidth();
    }

    /**
     * If Bounding Box or orginal is <code>null</code> or if the orginal does not touch exactly on of the sides of the bounding box no cropping will
     * be done.
     *
     * Otherwise the image will be centered horizontally or vertically
     */
    public void crop() {
        if (shouldCrop()) {
            int xOffSet = getXOffset();
            int yOffSet = getYOffset();
            int widthToCropTo = getWidthToCropTo();
            int heightToCropTo = getHeightToCropTo();
            LOGGER.info("Cropping original, width:{},height:{} to boundingbox, width:{},height:{}", originalWidth, originalHeight, boundingBoxWidth, boundingBoxHeight);
            LOGGER.info("Cropping image xOffSet:{};yOffset:{};widthToCropTo:{};heightToCropTo:{}", xOffSet, yOffSet, widthToCropTo, heightToCropTo);
            target = original.getSubimage(xOffSet, yOffSet, widthToCropTo, heightToCropTo);
        } else {
            target = original;
        }
    }

    private int getHeightToCropTo() {
        return isInsideBoundingBox(originalHeight, boundingBoxHeight) ? originalHeight : boundingBoxHeight;
    }

    private int getWidthToCropTo() {
        return isInsideBoundingBox(originalWidth, boundingBoxWidth) ? originalWidth : boundingBoxWidth;
    }

    private boolean isInsideBoundingBox(int original, int boundingBox) {
        return boundingBox > original;
    }

    /**
     * returns the cropped image
     */
    public BufferedImage getTarget() {
        return target;
    }

    private boolean shouldCrop() {
        return (originalWidth > boundingBoxWidth || originalHeight > boundingBoxHeight)
            && boundingBoxWidth > 0 && boundingBoxHeight > 0;
    }

    private int getXOffset() {
        return getOffsetToCenteredBoundingBox(originalWidth, boundingBoxWidth);
    }

    private int getYOffset() {
        return getOffsetToCenteredBoundingBox(originalHeight, boundingBoxHeight);
    }

    private int getOffsetToCenteredBoundingBox(final int original, final int boundingBox) {
        return isInsideBoundingBox(original, boundingBox) ? 0 : (original - boundingBox) / 2;
    }

}
