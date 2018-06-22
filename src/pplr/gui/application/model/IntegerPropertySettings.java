package pplr.gui.application.model;

/**
 * Contains the settings for a integer value.
 * @author CM
 *
 */
public class IntegerPropertySettings {

	/** Smallest integer */
	private Integer minimum;
	
	/** Biggest integer */
	private Integer maximum;
	
	/** Allow negative integer? */
	private boolean negativInt;
	
	/**
	 * Create a new IntegerPropertySettings object.
	 */
	public IntegerPropertySettings() {
		this(null, null, false);
	}
	
	/**
	 * Create a new IntegerPropertySettings object with user data.
	 */
	public IntegerPropertySettings(Integer minimum, Integer maximum, boolean negativInt) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.negativInt = negativInt;
	}
	
	/* ------------------------------ Getter & Setter ------------------------------ */
	
	/**
	 * Set the minimum of an integer
	 * @param minimum of an integer
	 */
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	
	/**
	 * Get the minimum of an integer.
	 * @return the minimum
	 */
	public Integer getMinimum() {
		return minimum;
	}
	
	/**
	 * Set the maximum of an integer
	 * @param maximum of an integer.
	 */
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	
	/**
	 * Get the maximum of an integer
	 * @return maximum
	 */
	public Integer getMaximum() {
		return maximum;
	}
	
	/**
	 * Set the allow negative integer
	 * @param negativInt allow negatives
	 */
	public void setNegativInt(boolean negativInt) {
		this.negativInt = negativInt;
	}
	
	/**
	 * Get the allow negative integer
	 * @return allow negative?
	 */
	public boolean isNegativInt() {
		return negativInt;
	}
}
