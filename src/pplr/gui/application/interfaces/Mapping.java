package pplr.gui.application.interfaces;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide the mapping functions between the attributes and the settings for the encoding.
 * @author CM
 *
 */
public abstract class Mapping {

	/** Map attribute name to number of Hash functions */
	private static Map<String, Integer> attributeToHashNumber = new HashMap<>();
	
	/** Map attribute to Hash function. */
	private static Map<String, MappingFunctions> attributeToFunction = new HashMap<>();
	
	/** Map attribute to hash function type. */
	private static Map<String, String> attributeToType = new HashMap<>();
	
	/** 
	 * Add a mapping of an attribute and a number of hash function to the map.
	 * @param attribute name of the attribute
	 * @param k number of hash functions
	 */
	public static void addAttributeHashNumber(String attribute, int k) {
		attributeToHashNumber.put(attribute, (Integer)k);
	}
	
	/** 
	 * Add a mapping of an attribute and a hash function to the map.
	 * @param attribute name of the attribute
	 * @param function index of the hash function
	 */
	public static void addAttributeToFunction(String attribute, MappingFunctions function) {
		attributeToFunction.put(attribute, function);
	}
	
	/**
	 * Add a mapping of an attribute and a type to the map.
	 * @param attribute name of the attribute
	 * @param type type of the hash function
	 */
	public static void addAttributeToType(String attribute, String type) {
		attributeToType.put(attribute, type);
	}
	
	/* ------------------------------ Getter ------------------------------ */
	
	/**
	 * Get the map for attribute and the number of hash functions.
	 * @return the map
	 */
	public static Map<String, Integer> getAttributeToHashNumber() {
		return attributeToHashNumber;
	}
	
	/**
	 * Get the map for attribute and hash function
	 * @return the map
	 */
	public static Map<String, MappingFunctions> getAttributeToFunction() {
		return attributeToFunction;
	}
	
	/**
	 * Get the map for attribute an hash function type
	 * @return the map
	 */
	public static Map<String, String> getAttributeToType() {
		return attributeToType;
	}
}
