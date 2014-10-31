package org.example.frontend.gallery.imageutil;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ImageScalerTest {

    @Test
    public void testScale_landscape() throws Exception {
        String resourceName = "pictures/Tolstoy_542x273.png";
        ImageScaler scaler = getImageScaler(resourceName, new Dimension(50, 50), true);
        BufferedImage scaledLandscape = scaler.getTarget();
        assertEquals(new Dimension(99, 50), getActual(scaledLandscape));
    }

    @Test
    public void testScale_portrait() throws Exception {
        String resourceName = "pictures/vanGogh_196x257.jpg";
        ImageScaler scaler = getImageScaler(resourceName, new Dimension(50, 100), true);
        BufferedImage scaledPortrait = scaler.getTarget();
        assertEquals(new Dimension(76, 100), getActual(scaledPortrait));
    }

    @Test
    public void testScale_portrait_upscaleAndupscalingFalse() throws Exception {
        String resourceName = "pictures/vanGogh_196x257.jpg";
        ImageScaler scaler = getImageScaler(resourceName, new Dimension(200, 400), false);
        BufferedImage scaledPortrait = scaler.getTarget();
        assertEquals(new Dimension(196, 257), getActual(scaledPortrait));
    }

    @Test
    public void testScale_portrait_upscaleAndupscalingTrue() throws Exception {
        String resourceName = "pictures/vanGogh_196x257.jpg";
        ImageScaler scaler = getImageScaler(resourceName, new Dimension(200, 400), true);
        BufferedImage scaledPortrait = scaler.getTarget();
        assertEquals(new Dimension(305, 400), getActual(scaledPortrait));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScale_portraitOrginalNull() throws Exception {
        ImageScaler scaler = new ImageScaler(null, new Dimension(50, 50));
        BufferedImage scaledPortrait = scaler.getTarget();
        assertEquals(new Dimension(50, 66), getActual(scaledPortrait));
    }

    private Dimension getActual(final Image target) {
        return new Dimension(target.getWidth(null), target.getHeight(null));
    }

    private ImageScaler getImageScaler(final String resourceName, final Dimension boundingBox, final boolean upscaling) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(resourceName);
        BufferedImage original = ImageIO.read(stream);
        ImageScaler scaler = new ImageScaler(original, boundingBox);
        scaler.setUpscaling(upscaling);
        scaler.scale();
        stream.close();
        return scaler;
    }
}
