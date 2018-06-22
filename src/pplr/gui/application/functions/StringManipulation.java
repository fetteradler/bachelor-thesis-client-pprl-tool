package pplr.gui.application.functions;

import java.util.ArrayList;

import pplr.gui.application.configuration.Constants;

/**
 * Defines the string manipulations for split and connect of strings.
 * @author CM
 *
 */
public abstract class StringManipulation {

	/**
	 * Split an attribute at a specific pattern in several attributes
	 * @param attribut that should be split
	 * @param splitPattern the pattern to split
	 * @return list of split attributes.
	 */
	public static ArrayList<String> splitString(String attribut, int splitPattern) {

		/*
		 * 1 " " 2 "-" 3 "/" 4 "."
		 */
		ArrayList<String> list = new ArrayList<String>();
		switch (splitPattern) {
		case 1:
			String[] parts1 = attribut.split(Constants.PATTERN_SPACE);
			for (int i = 0; i < parts1.length; i++) {
				list.add(parts1[i]);
			}
			break;
		case 2:
			String[] parts2 = attribut.split(Constants.PATTERN_DASH);
			for (int i = 0; i < parts2.length; i++) {
				list.add(parts2[i]);
			}
			break;
		case 3:
			if (attribut != null) {
				String[] parts3 = attribut.split(Constants.PATTERN_SLASH);
				for (int i = 0; i < parts3.length; i++) {
					list.add(parts3[i]);
				}
			} else {
				list.add("");
			}
			break;

		case 4:
			String[] parts4 = attribut.split(Constants.PATTER_DOUBBLE_BACKSLASHES_DOT);
			for (int i = 0; i < parts4.length; i++) {
				list.add(parts4[i]);
			}
			break;
		case 5:
			String[] parts5 = PatternHandling.splitOnPLZ(attribut);
			for (int i = 0; i < parts5.length; i++) {
				list.add(parts5[i]);
			}
			break;
		default:
			break;
		}
		return list;
	}

	/**
	 * Connect several attribute to one. The attribute will delimit by a selected pattern.
	 * @param attributes the attribute that should be connect
	 * @param connectPattern the pattern for connect
	 * @return a connected string
	 */
	public static String connectString(ArrayList<String> attributes, int connectPattern) {
		/*
		 * 1 " " 2 "-" 3 "/" 4 "."
		 */
		String result = "";
		int count = 1;
		switch (connectPattern) {
		case 1:
			for (String s : attributes) {
				result = result + s;
				if (attributes.size() > count) {
					result = result + Constants.PATTERN_SPACE;
				}
				count++;
			}
			break;
		case 2:
			for (String s : attributes) {
				result = result + s;
				if (attributes.size() > count) {
					result = result + Constants.PATTERN_DASH;
				}
				count++;
			}
			break;
		case 3:
			for (String s : attributes) {
				result = result + s;
				if (attributes.size() > count) {
					result = result + Constants.PATTERN_SLASH;
				}
				count++;
			}
			break;
		case 4:
			for (String s : attributes) {
				result = result + s;
				if (attributes.size() > count) {
					result = result + Constants.PATTER_DOUBBLE_BACKSLASHES_DOT;
				}
				count++;
			}
			break;
		default:
			break;
		}
		return result;
	}
}
