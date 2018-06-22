package pplr.gui.application.settings;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Wraps the settings for the string restriction tab to a xml file.
 * 
 * @author CM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StringRestrictionSettings {

	/** Minimal length of the string. */
	@XmlAttribute(name = "minimum")
	private Integer minimum;
	
	/** Maximal length of the string. */
	@XmlAttribute(name = "maximum")
	private Integer maximum;
	
	/** Remove spaces? */
	@XmlAttribute(name = "removeSpace")
	private boolean removeSpace;
	
	/** Remove whitespaces an invisible chars? */
	@XmlAttribute(name = "removeWhitespaces")
	private boolean removeWhitespaces;
	
	/** All allowed values. */
	@XmlAttribute(name = "onlyAllowedStrings")
	private ArrayList<String> onlyAllowedStrings;
	
	/** All illegal values. */
	@XmlAttribute(name = "exchange")
	private ArrayList<String> exchange; 
	
	/** All alternative values. */
	@XmlAttribute(name = "exchangeWith")
	private ArrayList<String> exchangeWith; 
	
	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */
	
	/**
	 * Get the minimum length of the string.
	 * @return minimum
	 */
	public Integer getMinimum() {
		return minimum;
	}
	
	/**
	 * Set the minimum length of the string.
	 * @param minimum length
	 */
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	
	/**
	 * Get the maximum length of the string.
	 * @return maximum
	 */
	public Integer getMaximum() {
		return maximum;
	}
	
	/**
	 * Set the maximum length of the string.
	 * @param maximum length
	 */
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	
	/**
	 * Is remove spaces active?
	 * @return True, if spaces should be removed.
	 */
	public boolean isRemoveSpace() {
		return removeSpace;
	}
	
	/**
	 * Set remove spaces
	 * @param removeSpace from string value
	 */
	public void setRemoveSpace(boolean removeSpace) {
		this.removeSpace = removeSpace;
	}
	
	/**
	 * Is remove whitespaces active?
	 * @return True, if whitespaces and invisible chars should be removed.
	 */
	public boolean isRemoveWhitespaces() {
		return removeWhitespaces;
	}
	
	/**
	 * Set remove whitespaces.
	 * @param removeWhitespaces True, if whitespaces and invisible chars should be removed.
	 */
	public void setRemoveWhitespaces(boolean removeWhitespaces) {
		this.removeWhitespaces = removeWhitespaces;
	}
	
	/**
	 * Get the allowed strings.
	 * @return all alloewd strings
	 */
	public ArrayList<String> getOnlyAllowedStrings() {
		return onlyAllowedStrings;
	}
	
	/**
	 * Set the allowed strings
	 * @param onlyAllowedStrings
	 */
	public void setOnlyAllowedStrings(ArrayList<String> onlyAllowedStrings) {
		this.onlyAllowedStrings = onlyAllowedStrings;
	}
	
	/**
	 * Get the illegal chars.
	 * @return all illegal chars
	 */
	public ArrayList<String> getExchange() {
		return exchange;
	}
	
	/**
	 * Set the illegal chars.
	 * @param exchange illegal chars
	 */
	public void setExchange(ArrayList<String> exchange) {
		this.exchange = exchange;
	}
	
	/**
	 * Get the alternative chars.
	 * @return all alternative chars
	 */
	public ArrayList<String> getExchangeWith() {
		return exchangeWith;
	}
	
	/**
	 * Set the alternative chars.
	 * @param exchangeWith all alternative chars
	 */
	public void setExchangeWith(ArrayList<String> exchangeWith) {
		this.exchangeWith = exchangeWith;
	}
}
