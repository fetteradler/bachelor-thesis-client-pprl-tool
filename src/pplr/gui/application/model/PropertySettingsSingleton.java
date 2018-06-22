package pplr.gui.application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Singleton object that defines the functions of all property settings. Create
 * a List that contains all property setting objects.
 * 
 * @author CM
 *
 */
public class PropertySettingsSingleton extends ArrayList<HashMap> {

	/** Instance of the property settings singleton object. */
	private static PropertySettingsSingleton instance;

	/** List of all properties */
	private ArrayList<HashMap> propertyList;

	/**
	 * Constructor. Create a new property settings singleton object. This will only
	 * create once.
	 */
	private PropertySettingsSingleton() {

		propertyList = new ArrayList<HashMap>();
	}

	/**
	 * Instance of the property settings singleton object.
	 * 
	 * @return the property settings object
	 */
	public static PropertySettingsSingleton getInstance() {
		if (instance == null) {
			instance = new PropertySettingsSingleton();
		}
		return instance;
	}

	/**
	 * Update the settings for a string property.
	 * 
	 * @param attributName
	 *            of the string property
	 * @param newSettings
	 *            for the string property
	 */
	public void updateStringProperties(String attributName, StringPropertySettings newSettings) {

		int i = 0;
		for (HashMap<String, StringPropertySettings> hm : this.propertyList) {
			for (String s : hm.keySet()) {
				if (s.equals(attributName)) {
					this.propertyList.get(i).replace(attributName, newSettings);
				}
			}
			i++;
		}

		for (HashMap<String, StringPropertySettings> map : this.propertyList) {
			Iterator it = map.entrySet().iterator();
			int j = 0;
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				if (pair.getKey().equals(attributName)) {
					this.propertyList.get(j).replace(attributName, newSettings);
				}
				it.remove();
			}
			i++;
		}
	}

	/**
	 * Update the settings for a integer property.
	 * 
	 * @param attributName
	 *            of the integer property
	 * @param newSettings
	 *            for the integer property
	 */
	public void updateIntegerProperties(String attributName, IntegerPropertySettings newSettings) {

		for (HashMap<String, IntegerPropertySettings> map : this.propertyList) {
			Iterator it = map.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				if (pair.getKey().equals(attributName)) {

				}
				it.remove();
			}
			i++;
		}
	}

	/**
	 * Update the settings for a date property.
	 * 
	 * @param attributName
	 *            of the date property
	 * @param newSettings
	 *            for the date property
	 */
	public void updateDateProperties(String attributName, DatePropertySettings newSettings) {

		for (HashMap<String, DatePropertySettings> map : this.propertyList) {
			Iterator it = map.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				if (pair.getKey().equals(attributName)) {

				}
				it.remove();
			}
			i++;
		}
	}
	
	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Get the property list.
	 * @return list of all properties
	 */
	public ArrayList<HashMap> getPropertyList() {
		return propertyList;
	}

	/**
	 * Set the property list.
	 * @param propertyList list of all properties
	 */
	public void setPropertyList(ArrayList<HashMap> propertyList) {
		this.propertyList = propertyList;
	}

	/**
	 * Give the index of the Hashmap for a specific attribute.
	 * 
	 * @return the index of the hashmap in the arraylist
	 */
	public int getIndexForAttribute(String attributName) {
		return 0;
	}

	/**
	 * Set the string properties
	 * @param stringProperties map of string name and its properties
	 */
	public void setStringProperties(HashMap<String, StringPropertySettings> stringProperties) {
		this.propertyList.add(stringProperties);
	}

	/**
	 * Set the integer properties.
	 * @param integerProperties map of integer name and its properties
	 */
	public void setIntegerProperties(HashMap<String, IntegerPropertySettings> integerProperties) {
		this.propertyList.add(integerProperties);
	}

	/**
	 * Set the date properties.
	 * @param dateProperties map of the date name and its properties
	 */
	public void setDateProperties(HashMap<String, DatePropertySettings> dateProperties) {
		this.propertyList.add(dateProperties);
	}
}
