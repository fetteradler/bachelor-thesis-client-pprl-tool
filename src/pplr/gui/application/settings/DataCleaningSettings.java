package pplr.gui.application.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Wraps the settings for the data cleaning tab to a xml file.
 * @author CM
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataCleaningSettings {

	/** Name of the attributes */
	@XmlAttribute(name = "attributes")
	private String[] attributes;
	
	/** Data types of the attribute. */
	@XmlAttribute(name = "types")
	private String[] types;
	
	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Get the attributes.
	 * @return attributes
	 */
	public String[] getAttributes() {
		return attributes;
	}
	
	/**
	 * Set the attributes.
	 * @param attributes for workflow
	 */
	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Get the types of the attributes.
	 * @return types
	 */
	public String[] getTypes() {
		return types;
	}
	
	/**
	 * Set the types of the attributes.
	 * @param types of attributes
	 */
	public void setTypes(String[] types) {
		this.types = types;
	}
}
