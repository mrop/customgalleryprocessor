package org.example.frontend.gallery.imageutil;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.apache.commons.io.IOUtils;
import org.example.frontend.gallery.imageutil.unbounded.BoundingBoxCalculator;
import org.hippoecm.frontend.plugins.gallery.imageutil.AbstractImageOperation;
import org.hippoecm.frontend.plugins.gallery.imageutil.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScaleCropImageOperation extends AbstractImageOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScaleCropImageOperation.class);
    private static final Object SCALING_LOCK = new Object();
    private InputStream scaledData;
    private int scaledWidth;
    private ImageUtils.ScalingStrategy scaleStrategy;
    private boolean upScaling;

    public int getScaledHeight() {
        return scaledHeight;
    }

    public int getScaledWidth() {
        return scaledWidth;
    }

    private int scaledHeight;

    public InputStream getScaledData() {
        return scaledData;
    }

    private Dimension boundingBoxDimension;

    public Dimension getBoundingBoxDimension() {
        return boundingBoxDimension;
    }

    public void setBoundingBoxDimension(final Dimension boundingBoxDimension) {
        this.boundingBoxDimension = boundingBoxDimension;
    }

    public ScaleCropImageOperation() {
    }

    public void execute(final InputStream data, final ImageReader reader, final ImageWriter writer) throws IOException {
        // save the image data in a temporary file so we can reuse the original data as-is if needed without
        // putting all the data into memory
        final File tmpFile = writeToTmpFile(data);
        boolean deleteTmpFile = true;
        LOGGER.debug("Stored uploaded image in temporary file {}", tmpFile);

        InputStream dataInputStream = null;
        ImageInputStream imageInputStream = null;
        try {

            dataInputStream = new FileInputStream(tmpFile);
            imageInputStream = new MemoryCacheImageInputStream(dataInputStream);
            reader.setInput(imageInputStream);

            BufferedImage scaledCroppedImage;

            synchronized (SCALING_LOCK) {
                BufferedImage originalImage = reader.read(0);
                Dimension calculatedBoundingBoxDimensions = 
                	BoundingBoxCalculator.getCalculatedBoundingBoxDimensions(getBoundingBoxDimension(), new Dimension(originalImage.getWidth(), originalImage.getHeight()));
                ImageScaler scaler = new ImageScaler(originalImage, calculatedBoundingBoxDimensions);
                scaler.setStrategy(getScaleStrategy());
                scaler.setUpscaling(getUpScaling());
                scaler.scale();
                BufferedImage scaledImage = scaler.getTarget();
                ImageCropper cropper = new ImageCropper(scaledImage, calculatedBoundingBoxDimensions);
                cropper.crop();
                scaledCroppedImage = cropper.getTarget();
                scaledWidth = scaledCroppedImage.getWidth();
                scaledHeight = scaledCroppedImage.getHeight();
            }

            ByteArrayOutputStream outputStream = ImageUtils.writeImage(writer, scaledCroppedImage, 1f);
            scaledData = new ByteArrayInputStream(outputStream.toByteArray());
        } finally {
            if (imageInputStream != null) {
                imageInputStream.close();
            }
            IOUtils.closeQuietly(dataInputStream);
            if (deleteTmpFile) {
                LOGGER.debug("Deleting temporary file {}", tmpFile);
                tmpFile.delete();
            }
        }

    }

    private File writeToTmpFile(InputStream data) throws IOException {
        File tmpFile = File.createTempFile("hippo-image", ".tmp");
        OutputStream tmpStream = null;
        try {
            tmpStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
            IOUtils.copy(data, tmpStream);
        } finally {
            IOUtils.closeQuietly(tmpStream);
        }
        return tmpFile;
    }

    public void setScaleStrategy(final ImageUtils.ScalingStrategy scaleStrategy) {
        this.scaleStrategy = scaleStrategy;
    }

    public ImageUtils.ScalingStrategy getScaleStrategy() {
        return scaleStrategy;
    }

    public boolean getUpScaling() {
        return upScaling;
    }

    public boolean isUpScaling() {
        return upScaling;
    }

    public void setUpScaling(final boolean upScaling) {
        this.upScaling = upScaling;
    }

    private static class AutoDeletingTmpFileInputStream extends FileInputStream {

        private final File tmpFile;

        AutoDeletingTmpFileInputStream(File tmpFile) throws FileNotFoundException {
            super(tmpFile);
            this.tmpFile = tmpFile;
        }

        @Override
        public void close() throws IOException {
            super.close();
            LOGGER.debug("Deleting temporary file {}", tmpFile);
            tmpFile.delete();
        }

    }

}
