package pplr.gui.application.settings;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Wraps the settings for the encoding tab to a xml file.
 * 
 * @author CM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CodierungSettings {

	/** Length of the bloom filter. */
	@XmlAttribute(name = "hashLength")
	private Integer bloomLength;

	/** Type of the hash function. */
	@XmlAttribute(name = "type")
	private boolean[] type;

	/** Hash function */
	@XmlElement(name = "function")
	private ArrayList<String> function;

	/** Number of hash functions. */
	@XmlElement(name = "k")
	private ArrayList<Integer> k;

	/** Length of Q-Gram or index of the n. pattern. */
	@XmlElement(name = "nq")
	private ArrayList<Integer> nq;

	/** Fill pattern. (Only for Q-gram-splitter) */
	@XmlElement(name = "fillPattern")
	private LinkedHashMap<Boolean, Boolean> fillPattern;

	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */

	/**
	 * Get the length of the bloom filter
	 * 
	 * @return length of bloom filter
	 */
	public Integer getBloomLength() {
		return bloomLength;
	}

	/**
	 * Set the length of the bloom filter
	 * 
	 * @param bloomLength
	 *            length of bloom filter
	 */
	public void setBloomLength(Integer bloomLength) {
		this.bloomLength = bloomLength;
	}

	/**
	 * Get the type of the hash function.
	 * 
	 * @return type of hash function
	 */
	public boolean[] getType() {
		return type;
	}

	/**
	 * Set the type of the hash function.
	 * 
	 * @param type
	 *            of the hash function
	 */
	public void setType(boolean[] type) {
		this.type = type;
	}

	/**
	 * Get the hash functions.
	 * 
	 * @return Hash functions
	 */
	public ArrayList<String> getFunction() {
		return function;
	}

	/**
	 * Set the hash functions.
	 * 
	 * @param function
	 *            list of the hash functions.
	 */
	public void setFunction(ArrayList<String> function) {
		this.function = function;
	}

	/**
	 * Get the number of hash functions.
	 * 
	 * @return number of hash functions
	 */
	public ArrayList<Integer> getK() {
		return k;
	}

	/**
	 * Set the number of hash functions.
	 * 
	 * @param k
	 *            number of hash functions
	 */
	public void setK(ArrayList<Integer> k) {
		this.k = k;
	}

	/**
	 * Get the length of Q-Gram/index of the n. pattern
	 * 
	 * @return length of q-gram or index
	 */
	public ArrayList<Integer> getNQ() {
		return nq;
	}

	/**
	 * Set the length of q-gram / index of the n. pattern
	 * 
	 * @param nq
	 *            length of q-gram or index of n- pattern
	 */
	public void setNQ(ArrayList<Integer> nq) {
		this.nq = nq;
	}

	/**
	 * Get the fill pattern.
	 * 
	 * @return fill pattern
	 */
	public LinkedHashMap<Boolean, Boolean> getFillPattern() {
		return fillPattern;
	}

	/**
	 * Set the fill pattern
	 * 
	 * @param fillPattern
	 */
	public void setFillPattern(LinkedHashMap<Boolean, Boolean> fillPattern) {
		this.fillPattern = fillPattern;
	}
}
