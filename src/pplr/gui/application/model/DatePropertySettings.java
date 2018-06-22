package pplr.gui.application.model;

import java.util.Date;

/**
 * Contains the settings for a date value.
 * @author CM
 *
 */
public class DatePropertySettings {

	/** Earliest date */
	private Date minimum;

	/** Latest date. */
	private Date maximum;

	/** Allow future date? */
	private boolean allowFuture;

	/**
	 * Create a new DatePropertySettings object.
	 */
	public DatePropertySettings() {
		this(null, null, false);
	}

	/**
	 * Create a new DatePropertySettings object with user data.
	 */
	public DatePropertySettings(Date minimum, Date maximum, boolean allowFuture) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.allowFuture = allowFuture;
	}

	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Set the earliest date.
	 * @param minimum earliest date
	 */
	public void setMinimum(Date minimum) {
		this.minimum = minimum;
	}

	/**
	 * Get the eariest date.
	 * @return the earliest date.
	 */
	public Date getMinimum() {
		return minimum;
	}

	/**
	 * Set the latest date.
	 * @param maximum latest date
	 */
	public void setMaximum(Date maximum) {
		this.maximum = maximum;
	}

	/**
	 * Get the latest date.
	 * @return the latest date
	 */
	public Date getMaximum() {
		return maximum;
	}

	/**
	 * Set the allow future date
	 * @param allowFuture allow future date?
	 */
	public void setAllowFuture(boolean allowFuture) {
		this.allowFuture = allowFuture;
	}

	/**
	 * Get the allow future date.
	 * @return the allow future date
	 */
	public boolean isAllowFuture() {
		return allowFuture;
	}
}
