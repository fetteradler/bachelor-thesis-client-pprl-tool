package pplr.gui.application.model;

import java.util.ArrayList;

/**
 * Singleton object that defines the functions to split an attribute.
 * 
 * @author CM
 *
 */
public class SplitAttributSingleton {

	/** Instance of the split attribute singleton object. */
	private static SplitAttributSingleton instance;

	/** New values after split. */
	private ArrayList<String> newAttributes;

	/** Value that should be split. */
	private String oldAttribut;

	/** Index of the delimiter pattern for the split. */
	private int patternIndex;

	/**
	 * Constructor. Create a new split attribute singleton object. This will only
	 * create once.
	 */
	private SplitAttributSingleton() {
		newAttributes = new ArrayList<String>();
		oldAttribut = "";
		patternIndex = 0;
	}

	/**
	 * Instance of the split attribute singleton object.
	 * 
	 * @return the split attribute object
	 */
	public static SplitAttributSingleton getInstance() {
		if (instance == null) {
			instance = new SplitAttributSingleton();
		}
		return instance;
	}

	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */

	/**
	 * Set the resulting attributes for the split.
	 * @param newAttributes resulting attributes
	 */
	public void setNewAttributes(ArrayList<String> newAttributes) {
		this.newAttributes = newAttributes;
	}

	/**
	 * Set the attribute that should be split.
	 * @param oldAttribut that should be split
	 */
	public void setOldAttribut(String oldAttribut) {
		this.oldAttribut = oldAttribut;
	}

	/**
	 * Set the index of the delimiter pattern.
	 * @param patternIndex of the delimiter pattern.
	 */
	public void setPatternIndex(int patternIndex) {
		this.patternIndex = patternIndex;
	}

	/**
	 * Get the resulting attributes for split.
	 * @return resulting attributes
	 */
	public ArrayList<String> getNewAttributes() {
		return newAttributes;
	}

	/**
	 * Get the attribute that should be split.
	 * @return the attribute that should be split
	 */
	public String getOldAttribut() {
		return oldAttribut;
	}

	/**
	 * Get the delimiter pattern index.
	 * @return pattern index.
	 */
	public int getPatternIndex() {
		return patternIndex;
	}
}
