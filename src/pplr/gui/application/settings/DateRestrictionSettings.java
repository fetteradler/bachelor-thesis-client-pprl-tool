package pplr.gui.application.settings;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Wraps the settings for the date restriction tab to a xml file.
 * 
 * @author CM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DateRestrictionSettings {

	/** Earliest date. */
	@XmlAttribute(name = "minimum")
	private Date minimum;
	
	/** Latest date. */
	@XmlAttribute(name = "maximum")
	private Date maximum;
	
	/** Allow future date? */
	@XmlAttribute(name = "future")
	private boolean allowFuture;
	
	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */
	
	/**
	 * Get the earliest date.
	 * @return earliest date
	 */
	public Date getMinimum() {
		return minimum;
	}
	
	/**
	 * Set the earliest date.
	 * @param minimum earliest date
	 */
	public void setMinimum(Date minimum) {
		this.minimum = minimum;
	}
	
	/**
	 * Get the latest date.
	 * @return latest date
	 */
	public Date getMaximum() {
		return maximum;
	}
	
	/**
	 * Set the latest date.
	 * @param maximum latest date
	 */
	public void setMaximum(Date maximum) {
		this.maximum = maximum;
	}
	
	/**
	 * Get the boolean to allow future date.
	 * @return True, if future date are allowed.
	 */
	public boolean isAllowNegative() {
		return allowFuture;
	}
	
	/**
	 * Set allow future date.
	 * @param allowFuture True, if future date are allowed.
	 */
	public void setAllowNegative(boolean allowFuture) {
		this.allowFuture = allowFuture;
	}
}
