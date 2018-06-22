package pplr.gui.application.interfaces;

import java.util.Set;

/**
 * Definition of the map function to store the settings of the encoding.
 * @author CM
 *
 */
public class MappingFunctions {

	/** The values of the encoding. */
	private Set<String> values;
	
	/** The selected encoding function */
	private String function;
	
	/** Limitation for Q-Gram-Splitter or index of the n. character */
	private int limitIndex;
	
	/** Should filler characters be used? */
	private boolean filling;
	
	/** 
	 * Constructor.
	 * @param values of encoding
	 * @param function encoding function
	 * @param limitIndex Q-Gram length or n. character index
	 * @param filling filler characters?
	 */
	public MappingFunctions(Set<String> values, String function, int limitIndex, boolean filling) {
		this.values = values;
		this.function = function;
		this.limitIndex = limitIndex;
		this.filling = filling;
	}
}
