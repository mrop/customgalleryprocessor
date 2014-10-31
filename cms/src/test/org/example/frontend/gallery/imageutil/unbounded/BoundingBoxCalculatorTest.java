package org.example.frontend.gallery.imageutil.unbounded;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;

import org.junit.Test;


public class BoundingBoxCalculatorTest {
    @Test
    public void testCalculateUnboundedWidth() throws Exception {
        Dimension actual = BoundingBoxCalculator.getCalculatedBoundingBoxDimensions(new Dimension(0,50),new Dimension(100,200));
        Dimension expected = new Dimension(25,50);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateUnboundedHeight() throws Exception {
        Dimension actual = BoundingBoxCalculator.getCalculatedBoundingBoxDimensions(new Dimension(25,0),new Dimension(100,200));
        Dimension expected = new Dimension(25,50);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateUnboundedWidthExtreme() throws Exception {
        Dimension actual = BoundingBoxCalculator.getCalculatedBoundingBoxDimensions(new Dimension(10,0),new Dimension(1000,10));
        Dimension expected = new Dimension(10,0);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateUnboundedHeightExtreme() throws Exception {
        Dimension actual = BoundingBoxCalculator.getCalculatedBoundingBoxDimensions(new Dimension(0,10),new Dimension(10,1000));
        Dimension expected = new Dimension(0,10);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateBothUnbounded() throws Exception {
        Dimension actual = BoundingBoxCalculator.getCalculatedBoundingBoxDimensions(new Dimension(0,0),new Dimension(100,200));
        Dimension expected = new Dimension(100,200);
        assertEquals(expected, actual);
    }

}
