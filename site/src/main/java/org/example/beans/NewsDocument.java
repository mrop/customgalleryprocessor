package org.example.beans;

import java.util.Calendar;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.example.beans.Customimageset;

@HippoEssentialsGenerated(internalName = "customgalleryprocessor:newsdocument")
@Node(jcrType = "customgalleryprocessor:newsdocument")
public class NewsDocument extends HippoDocument {
	/** 
	 * The document type of the news document.
	 */
	public final static String DOCUMENT_TYPE = "customgalleryprocessor:newsdocument";
	private final static String TITLE = "customgalleryprocessor:title";
	private final static String DATE = "customgalleryprocessor:date";
	private final static String INTRODUCTION = "customgalleryprocessor:introduction";
	private final static String IMAGE = "customgalleryprocessor:image";
	private final static String CONTENT = "customgalleryprocessor:content";
	private final static String LOCATION = "customgalleryprocessor:location";
	private final static String AUTHOR = "customgalleryprocessor:author";
	private final static String SOURCE = "customgalleryprocessor:source";

	/** 
	 * Get the title of the document.
	 * @return the title
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:title")
	public String getTitle() {
		return getProperty(TITLE);
	}

	/** 
	 * Get the date of the document.
	 * @return the date
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:date")
	public Calendar getDate() {
		return getProperty(DATE);
	}

	/** 
	 * Get the introduction of the document.
	 * @return the introduction
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:introduction")
	public String getIntroduction() {
		return getProperty(INTRODUCTION);
	}

	/** 
	 * Get the main content of the document.
	 * @return the content
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:content")
	public HippoHtml getContent() {
		return getHippoHtml(CONTENT);
	}

	/** 
	 * Get the location of the document.
	 * @return the location
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:location")
	public String getLocation() {
		return getProperty(LOCATION);
	}

	/** 
	 * Get the author of the document.
	 * @return the author
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:author")
	public String getAuthor() {
		return getProperty(AUTHOR);
	}

	/** 
	 * Get the source of the document.
	 * @return the source
	 */
	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:source")
	public String getSource() {
		return getProperty(SOURCE);
	}

	@HippoEssentialsGenerated(internalName = "customgalleryprocessor:image")
	public Customimageset getImage() {
		return getLinkedBean("customgalleryprocessor:image",
				Customimageset.class);
	}
}
