package pplr.gui.application.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for the configuration object for the PPRL-Workflow. The necessary
 * attribute has to be configured manually be the admin.
 * 
 * @author CM
 *
 */
public class ConfigurationObjectSingleton {

	/**
	 * Necessary attributes the admin defines before the workflow will be started.
	 */
	private static final StringProperty ATTRIBUT_NAME_1 = new SimpleStringProperty("Vorname");
	private static final StringProperty ATTRIBUT_NAME_2 = new SimpleStringProperty("Nachname");
	private static final StringProperty ATTRIBUT_NAME_3 = new SimpleStringProperty("Geburtsjahr");
	private static final StringProperty ATTRIBUT_NAME_4 = new SimpleStringProperty("Geburtsmonat");
	private static final StringProperty ATTRIBUT_NAME_5 = new SimpleStringProperty("Geburtstag");
	private static final StringProperty ATTRIBUT_NAME_6 = new SimpleStringProperty("Sozialversicherungsnummer");
	private static final StringProperty ATTRIBUT_NAME_7 = new SimpleStringProperty("Geschlecht");

	/** Instance of the configuration singleton object. */
	private static ConfigurationObjectSingleton instance;

	/**
	 * Configure names for attribute that should be matched Names of the attributes
	 * that should be created for record linkage
	 */
	private static ArrayList<StringProperty> attributeNames;

	/**
	 * Constructor. Create a new configuration singleton object. This will only
	 * create once.
	 */
	private ConfigurationObjectSingleton() {

		this.attributeNames = createConfigurationLabel();
	}

	/**
	 * Instance of the configuration singleton object.
	 * 
	 * @return the configuration object
	 */
	public static ConfigurationObjectSingleton getInstance() {
		if (instance == null) {
			instance = new ConfigurationObjectSingleton();
		}
		return instance;
	}

	/**
	 * Create a list of all necessary attributes.
	 * 
	 * @return list of all attribute names.
	 */
	public static ArrayList<StringProperty> createConfigurationLabel() {

		ArrayList<StringProperty> attributeNames = new ArrayList<StringProperty>();

		attributeNames.add(ATTRIBUT_NAME_1);
		attributeNames.add(ATTRIBUT_NAME_2);
		attributeNames.add(ATTRIBUT_NAME_3);
		attributeNames.add(ATTRIBUT_NAME_4);
		attributeNames.add(ATTRIBUT_NAME_5);
		attributeNames.add(ATTRIBUT_NAME_6);
		attributeNames.add(ATTRIBUT_NAME_7);

		return attributeNames;
	}

	/**
	 * Add a new attribute to the configuration object.
	 * 
	 * @param attribute
	 *            to add.
	 */
	public void addProperty(StringProperty attribute) {
		attributeNames.add(attribute);
	}

	/**
	 * Deletes an attribute from the configuration object.
	 * 
	 * @param attribute
	 *            to delete.
	 */
	public void deleteProperty(StringProperty attribute) {
		attributeNames.remove(attribute);
	}

	/**
	 * Get all configuration attribute names.
	 * 
	 * @return list of all attribute names
	 */
	public ArrayList<StringProperty> getAttributeNames() {
		return attributeNames;
	}

	/**
	 * Set all configuration attribute names.
	 * 
	 * @param attributeNames
	 *            list of all attribute names
	 */
	public void setAttributeNames(ArrayList<StringProperty> attributeNames) {
		this.attributeNames = attributeNames;
	}
}
