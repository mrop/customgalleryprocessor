package org.example.frontend.gallery.imageutil;

import java.awt.Dimension;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;
import org.hippoecm.frontend.editor.plugins.resource.ResourceHelper;
import org.hippoecm.frontend.plugins.gallery.model.GalleryException;
import org.hippoecm.frontend.plugins.gallery.processor.ScalingGalleryProcessor;
import org.hippoecm.frontend.plugins.gallery.processor.ScalingParameters;
import org.hippoecm.repository.gallery.HippoGalleryNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScalingCroppingGalleryProcessor extends ScalingGalleryProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScalingCroppingGalleryProcessor.class);
    
    /**
     * Initializes a hippo:resource node of an the main gallery node. Such initialization happens at two times: when a new image is uploaded to the
     * gallery, and when an image in an existing imageset is replaced by another image.
     *
     * @param node         the hippo:resource node
     * @param data         the uploaded data
     * @param mimeType     the MIME type of the uploaded data
     * @param fileName     the file name of the uploaded data
     * @param lastModified
     * @throws javax.jcr.RepositoryException when repository access failed.
     */
    @Override
    public void initGalleryResource(final Node node, final InputStream data, final String mimeType, final String fileName, final Calendar lastModified) throws RepositoryException {
        node.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
        node.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified);

        InputStream stored = data;
        int width = 0;
        int height = 0;

        if (isImageMimeType(mimeType)) {
            final String nodeName = node.getName();
            final ScalingParameters p = scalingParametersMap.get(nodeName);
            if (p != null) {
                try {
                    final ScaleCropImageOperation scaleCropOperation = new ScaleCropImageOperation();
                    scaleCropOperation.setBoundingBoxDimension(new Dimension(p.getWidth(),p.getHeight()));
                    scaleCropOperation.setScaleStrategy(p.getStrategy());
                    scaleCropOperation.setUpScaling(p.getUpscaling());
                    scaleCropOperation.execute(data, mimeType);
                    stored = scaleCropOperation.getScaledData();
                    width = scaleCropOperation.getScaledWidth();
                    height = scaleCropOperation.getScaledHeight();
                } catch (GalleryException e) {
                    LOGGER.warn("Scaling and cropping  failed, using original image instead", e);
                }
            } else {
                LOGGER.debug("No scaling parameters specified for {}, using original image", nodeName);
            }
        } else {
            LOGGER.debug("Unknown image MIME type: {}, using raw data", mimeType);
        }

        node.setProperty(JcrConstants.JCR_DATA, ResourceHelper.getValueFactory(node).createBinary(stored));
        node.setProperty(HippoGalleryNodeType.IMAGE_WIDTH, width);
        node.setProperty(HippoGalleryNodeType.IMAGE_HEIGHT, height);
    }




}
