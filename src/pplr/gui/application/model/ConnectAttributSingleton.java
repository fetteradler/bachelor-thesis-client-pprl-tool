package pplr.gui.application.model;

import java.util.ArrayList;

/**
 * Singleton object that defines the functions to connect attributes.
 * 
 * @author CM
 *
 */
public class ConnectAttributSingleton {

	/** Instance of the connect attribute singleton object. */
	private static ConnectAttributSingleton instance;

	/** Values that should be connected. */
	private ArrayList<String> oldAttributes;

	/** Name of the new attribute. */
	private String newAttribut;

	/** Index of the delimiter pattern for the connect. */
	private int patternIndex;

	/**
	 * Constructor. Create a new connect attribute singleton object. This will only
	 * create once.
	 */
	private ConnectAttributSingleton() {
		oldAttributes = new ArrayList<String>();
		newAttribut = "";
		patternIndex = 0;
	}

	/**
	 * Instance of the connect attribute singleton object.
	 * 
	 * @return the connect attribute object
	 */
	public static ConnectAttributSingleton getInstance() {
		if (instance == null) {
			instance = new ConnectAttributSingleton();
		}
		return instance;
	}

	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Set the attributes for the connect.
	 * @param oldAttributes attributes for connection
	 */
	public void setOldAttributes(ArrayList<String> oldAttributes) {
		this.oldAttributes = oldAttributes;
	}

	/**
	 * Set the name of the new attribute.
	 * @param newAttribut name of the new attribute
	 */
	public void setNewAttribut(String newAttribut) {
		this.newAttribut = newAttribut;
	}

	/**
	 * Set the index of the delimiter pattern.
	 * @param patternIndex of the delimiter pattern.
	 */
	public void setPatternIndex(int patternIndex) {
		this.patternIndex = patternIndex;
	}

	/**
	 * Get the attributes for the connect
	 * @return list of attributes
	 */
	public ArrayList<String> getOldAttributes() {
		return oldAttributes;
	}

	/**
	 * Get the name of the new attribute.
	 * @return new attribute
	 */
	public String getNewAttribut() {
		return newAttribut;
	}

	/**
	 * Get the delimiter pattern index.
	 * @return pattern index.
	 */
	public int getPatternIndex() {
		return patternIndex;
	}
}
