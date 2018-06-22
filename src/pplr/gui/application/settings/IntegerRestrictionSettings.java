package pplr.gui.application.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Wraps the settings for the integer restriction tab to a xml file.
 * 
 * @author CM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerRestrictionSettings {

	/** The minimum of the integer value. */
	@XmlAttribute(name = "minimum")
	private Integer minimum;
	
	/** The maximum of the integer value. */
	@XmlAttribute(name = "maximum")
	private Integer maximum;
	
	/** Allow negative integer? */
	@XmlAttribute(name = "negative")
	private boolean allowNegative;
	
	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */
	
	/**
	 * Get the minimum integer value.
	 * @return minimum
	 */
	public Integer getMinimum() {
		return minimum;
	}
	
	/**
	 * Set the minimum integer value.
	 * @param minimum integer value
	 */
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	
	/**
	 * Get the maximum integer value.
	 * @return maximum
	 */
	public Integer getMaximum() {
		return maximum;
	}
	
	/**
	 * Set the maximum integer value.
	 * @param maximum integer value
	 */
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	
	/**
	 * Is the allow negative true?
	 * @return True, if negatives are allowed
	 */
	public boolean isAllowNegative() {
		return allowNegative;
	}
	
	/**
	 * Set the allow negative value for the integer
	 * @param allowNegative True, if negatives are allowed.
	 */
	public void setAllowNegative(boolean allowNegative) {
		this.allowNegative = allowNegative;
	}
}
