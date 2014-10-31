package org.example.frontend.gallery.imageutil;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageCropperTest {

    private static final boolean CREATE_OUTPUT = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCropperTest.class);

    @Test
    public void testScale_landscape() throws Exception {
        String extension = "png";
        String picture = "Tolstoy_99x50";
        String resourceName = "pictures/" + picture + "." + extension;
        ImageCropper cropper = getImageCropper(resourceName, new Dimension(50, 50));
        final Image target = cropper.getTarget();
        Dimension expected = new Dimension(50, 50);
        assertEquals(expected, getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test
    public void testCrop_0_boundingBox() throws Exception {
        String extension = "png";
        String picture = "Tolstoy_99x50";
        String resourceName = "pictures/" + picture + "." + extension;
        Dimension boundingBox = new Dimension(0, 0);
        ImageCropper cropper = getImageCropper(resourceName, boundingBox);
        final Image target = cropper.getTarget();
        Dimension expected = new Dimension(99, 50);
        assertEquals(expected, getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrop_boundingBoxNull() throws Exception {
        String extension = "png";
        String picture = "Tolstoy_99x50";
        String resourceName = "pictures/" + picture + "." + extension;
        ImageCropper cropper = getImageCropper(resourceName, null);
        final Image target = cropper.getTarget();
        Dimension expected = new Dimension(99, 50);
        assertEquals(expected, getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrop_originalNull() throws Exception {
        String extension = "png";
        String picture = "Tolstoy_99x50";
        Dimension boundingBox = new Dimension(0, 0);
        ImageCropper cropper = new ImageCropper(null, boundingBox);
        cropper.crop();
        final Image target = cropper.getTarget();
        Dimension expected = new Dimension(99, 50);
        assertEquals(expected, getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test
    public void testCrop_landscape_same() throws Exception {
        String extension = "png";
        String picture = "Tolstoy_99x50";
        String resourceName = "pictures/" + picture + "." + extension;
        Dimension boundingBox = new Dimension(99, 50);
        ImageCropper cropper = getImageCropper(resourceName, boundingBox);
        final Image target = cropper.getTarget();
        Dimension expected = new Dimension(99, 50);
        assertEquals(expected, getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test
    public void testCrop_cropOriginalWithWidthAndHeightGreaterThanBoundingBox() throws Exception {
        String extension = "png";
        String picture = "Tolstoy_99x50";
        String resourceName = "pictures/" + picture + "." + extension;
        Dimension boundingBox = new Dimension(50, 40);
        ImageCropper cropper = getImageCropper(resourceName, boundingBox);
        final Image target = cropper.getTarget();
        Dimension expected = new Dimension(50, 40);
        assertEquals(expected, getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test
    public void testCrop_portrait() throws Exception {
        String picture = "Tolstoy_50x71";
        String extension = "jpg";
        String resourceName = "pictures/"
            + picture
            + "."
            + extension;
        Dimension boundingBox = new Dimension(50, 50);
        ImageCropper cropper = getImageCropper(resourceName, boundingBox);
        final Image target = cropper.getTarget();
        assertEquals(new Dimension(50, 50), getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test
    public void testCrop_toolow() throws Exception {
        String picture = "artis-thema-940x350";
        String extension = "png";
        String resourceName = "pictures/"
            + picture
            + "."
            + extension;
        Dimension boundingBox = new Dimension(896, 417);
        ImageCropper cropper = getImageCropper(resourceName, boundingBox);
        final Image target = cropper.getTarget();
        assertEquals(new Dimension(896, 350), getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    @Test
    public void testCrop_toosmall() throws Exception {
        String picture = "download_1000x669";
        String extension = "jpg";
        String resourceName = "pictures/"
            + picture
            + "."
            + extension;
        Dimension boundingBox = new Dimension(1400, 652);
        ImageCropper cropper = getImageCropper(resourceName, boundingBox);
        final Image target = cropper.getTarget();
        assertEquals(new Dimension(1000, 652), getActual(target));
        if (CREATE_OUTPUT) {
            createOutput(picture, extension, (RenderedImage) target);
        }
    }

    private void createOutput(final String picture, final String extension, final RenderedImage target) throws IOException {
        File outputfile = File.createTempFile(picture + "-", "_cropped." + extension);
        LOGGER.info("Outputting to file: {}", outputfile.getAbsolutePath());
        ImageIO.write(target, extension, outputfile);
    }

    private Dimension getActual(final Image target) {
        return new Dimension(target.getWidth(null), target.getHeight(null));
    }

    private ImageCropper getImageCropper(final String resourceName, Dimension boundingBox) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(resourceName);
        BufferedImage original = ImageIO.read(stream);
        ImageCropper cropper = new ImageCropper(original, boundingBox);
        cropper.crop();
        stream.close();
        return cropper;
    }
}
