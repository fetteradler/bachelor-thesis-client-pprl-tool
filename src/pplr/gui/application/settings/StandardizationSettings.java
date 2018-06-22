package pplr.gui.application.settings;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Wraps the settings for the standardization tab to a xml file.
 * 
 * @author CM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StandardizationSettings {

	/** Format of the attributes. */
	@XmlElement(name = "format")
	private LinkedHashMap<String, String> format;
	
	/** Restrictions for all string attributes. */
	@XmlElement(name = "stringRestrictions")
	private HashMap<String, StringRestrictionSettings> stringSettings;
	
	/** Restrictions for all integer attributes. */
	@XmlElement(name = "integerRestrictions")
	private HashMap<String, IntegerRestrictionSettings> integerSettings;
	
	/** Restrictions for all date attributes. */
	@XmlElement(name = "dateRestrictions")
	private HashMap<String, DateRestrictionSettings> dateSettings;
	
	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */
	
	/**
	 * Get the attribute formats.
	 * @return formats of all attributes
	 */
	public LinkedHashMap<String, String> getFormat() {
		return format;
	}
	
	/**
	 * Set the format of all attributes.
	 * @param format of all attributes
	 */
	public void setFormat(LinkedHashMap<String, String> format) {
		this.format = format;
	}
	
	/**
	 * Get the restrictions for all string attributes.
	 * @return string restriction settings
	 */
	public HashMap<String, StringRestrictionSettings> getStringSettings() {
		return stringSettings;
	}
	
	/**
	 * Set the restrictions for all string attributes.
	 * @param stringSettings attribute name mapped to restrictions
	 */
	public void setStringSettings(HashMap<String, StringRestrictionSettings> stringSettings) {
		this.stringSettings = stringSettings;
	}
	
	/**
	 * Get the restrictions for all integer attributes.
	 * @return integer restriction settings
	 */
	public HashMap<String, IntegerRestrictionSettings> getIntegerSettings() {
		return integerSettings;
	}
	
	/**
	 * Set the restrictions for all integer attributes.
	 * @param integerSettings attribute name mapped to restrictions
	 */
	public void setIntegerSettings(HashMap<String, IntegerRestrictionSettings> integerSettings) {
		this.integerSettings = integerSettings;
	}
	
	/**
	 * Get the restrictions for all date attributes.
	 * @return date restriction settings
	 */
	public HashMap<String, DateRestrictionSettings> getDateSettings() {
		return dateSettings;
	}
	
	/**
	 * Set the restrictions for all date attributes.
	 * @param dateSettings attribute name mapped to restrictions
	 */
	public void setDateSettings(HashMap<String, DateRestrictionSettings> dateSettings) {
		this.dateSettings = dateSettings;
	}
}
