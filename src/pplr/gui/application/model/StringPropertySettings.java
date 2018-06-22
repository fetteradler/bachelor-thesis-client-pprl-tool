package pplr.gui.application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

/**
 * Contains the settings for a string value.
 * 
 * @author CM
 *
 */
public class StringPropertySettings {

	/** Maximal length of the string. */
	String maxLength;

	/** Minimal length of the string. */
	String minLength;

	/** Should whitespace be removed? */
	boolean removeSpaces;

	/** Should whitespace an invisible pattern be removed? */
	boolean removeWhitespaces;

	/** All allowed strings. */
	private List<String> allowedChars;

	/** All illegal chars. */
	private List<String> illegalChars;

	/** All alternative chars. */
	private List<String> alternativChars;

	/** Name of the string value. */
	StringProperty propertyName;

	/**
	 * Create a new StringPropertySettings object.
	 */
	public StringPropertySettings() {

		this(null, null, false, false, null, null, null);
	}
	
	/**
	 * Create a new StringPropertySettings object with user data.
	 */
	public StringPropertySettings(String maxLength, String minLength, boolean removeSpaces, boolean removeWhitespaces,
			ArrayList<String> allowedChars, ArrayList<String> illegalChars, ArrayList<String> alternativChars) {

		this.maxLength = maxLength;
		this.minLength = minLength;
		this.removeSpaces = removeSpaces;
		this.removeWhitespaces = removeWhitespaces;
		this.allowedChars = allowedChars;
		this.illegalChars = illegalChars;
		this.alternativChars = alternativChars;
	}

	/**
	 * Add an allowed character to the settings.
	 * @param character allowed char
	 */
	public void addAllowedChar(String character) {

		boolean alreadyContain = false;

		if (allowedChars.size() == 0) {
			allowedChars.add(character);
		} else {
			for (String c : allowedChars) {
				if (character.equals(c)) {
					alreadyContain = true;
				}
			}

			if (alreadyContain) {
				allowedChars.add(character);
			}
		}
	}

	/**
	 * Update the string property settings for the string.
	 * @param newSettings new StringPropertySetings.
	 * @return the new settings
	 */
	public StringPropertySettings updateSettings(StringPropertySettings newSettings) {

		this.maxLength = newSettings.maxLength;
		this.minLength = newSettings.minLength;
		this.removeSpaces = newSettings.removeSpaces;
		this.removeWhitespaces = newSettings.removeWhitespaces;
		this.allowedChars = newSettings.allowedChars;
		this.illegalChars = newSettings.illegalChars;
		this.alternativChars = newSettings.alternativChars;
		return newSettings;
	}

	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Get the maximum of the sting length.
	 * @return maximum
	 */
	public String getMaxLength() {
		return maxLength;
	}

	/**
	 * Get the minimum of the string length.
	 * @return minimum
	 */
	public String getMinLength() {
		return minLength;
	}

	/**
	 * Get the boolean of the remove spaces setting.
	 * @return True, if spaces should be removed.
	 */
	public boolean isRemoveSpaces() {
		return removeSpaces;
	}

	/**
	 * Get the boolean of the remove whitespaces and invislible pattern setting.
	 * @return True, if whitespaces and invisible pattern should be removed.
	 */
	public boolean isRemoveWhitespaces() {
		return removeWhitespaces;
	}

	/**
	 * Get all allowed chars.
	 * @return list of all allowed chars
	 */
	public List<String> getAllowedChars() {
		return allowedChars;
	}

	/**
	 * Get all illegal chars.
	 * @return list of all illegal chars
	 */
	public List<String> getIllegalChars() {
		return illegalChars;
	}

	/**
	 * Get all alternative chars.
	 * @return list of all alternative chars
	 */
	public List<String> getAlternativChars() {
		return alternativChars;
	}

	/**
	 * Set maximum of string length.
	 * @param maxLength of string length
	 */
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Set minimum of string length.
	 * @param minLength of string length.
	 */
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	/**
	 * Set the boolean for remove spaces.
	 * @param removeSpaces True, if spaces should be removed.
	 */
	public void setRemoveSpaces(boolean removeSpaces) {
		this.removeSpaces = removeSpaces;
	}

	/**
	 * Set the boolean for remove whitespaces and invisible chars. 
	 * @param removeWhitespaces True, if it should be removed.
	 */
	public void setRemoveWhitespaces(boolean removeWhitespaces) {
		this.removeWhitespaces = removeWhitespaces;
	}

	/**
	 * Set the allowed chars.
	 * @param allowedChars list of allowed chars
	 */
	public void setAllowedChars(List<String> allowedChars) {
		this.allowedChars = allowedChars;
	}

	/**
	 * Set the illegal chars.
	 * @param illegalChars list of illegal chars
	 */
	public void setIllegalChars(List<String> illegalChars) {
		this.illegalChars = illegalChars;
	}

	/**
	 * Set the alternative chars.
	 * @param alternativChars list of alternative chars
	 */
	public void setAlternativChars(List<String> alternativChars) {
		this.alternativChars = alternativChars;
	}
}
