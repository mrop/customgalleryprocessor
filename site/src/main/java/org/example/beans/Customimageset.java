package org.example.beans;

import org.hippoecm.hst.content.beans.standard.HippoGalleryImageBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

@HippoEssentialsGenerated(internalName = "customgalleryprocessor:customimageset")
@Node(jcrType = "customgalleryprocessor:customimageset")
public class Customimageset extends HippoGalleryImageSet {

    /**
     * Get the large version of the image.
     *
     * @return the large version of the image
     */
    @HippoEssentialsGenerated(internalName = "customgalleryprocessor:square")
    public HippoGalleryImageBean getSquare() {
        return getBean("customgalleryprocessor:square",  HippoGalleryImageBean.class);
    }
}
