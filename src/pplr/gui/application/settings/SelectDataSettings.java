package pplr.gui.application.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Wraps the settings for the select data tab to a xml file.
 * 
 * @author CM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SelectDataSettings {

	/** True, if the user want to load data from file system. */
	@XmlAttribute(name = "load")
	private boolean load;
	
	/** The path to the data in file system */
	@XmlAttribute(name = "path")
	private String path;
	
	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */
	
	/**
	 * Set the load from the file system.
	 * @param load True, if the user want so load data from local file system.
	 */
	public void setLoad(boolean load) {
		this.load = load;
	}
	
	/**
	 * Set the path to the file system.
	 * @param path to the file
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Is the load from from file system activated?
	 * @return True, if the user want so load data from local file system.
	 */
	public boolean isLoad() {
		return load;
	}
	
	/**
	 * Get the path to the file system.
	 * @return path to file system
	 */
	public String getPath() {
		return path;
	}
}
