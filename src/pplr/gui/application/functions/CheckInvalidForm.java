package pplr.gui.application.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import pplr.gui.application.configuration.Constants;
import pplr.gui.application.model.DatePropertySettings;
import pplr.gui.application.model.IntegerPropertySettings;
import pplr.gui.application.model.PropertySettingsSingleton;
import pplr.gui.application.model.StringPropertySettings;

/**
 * Class to check every entry of the selected data is in valid format.
 * 
 * @author CM
 *
 */
public abstract class CheckInvalidForm {

	/** List of all invalid rows of the table view. */
	static Set<Integer> invalidRows = new HashSet<Integer>();

	/** Contains the setting for each attribute. */
	static PropertySettingsSingleton propertySettings = PropertySettingsSingleton.getInstance();

	/** Map the attribute to the settings */
	static LinkedHashMap<String, ArrayList<String>> updatedAttributes = new LinkedHashMap<String, ArrayList<String>>();

	/**
	 * Set the properties to every string attributes.
	 * 
	 * @param attributes
	 *            mapping of attribute name and settings
	 */
	public static void setStringToPropertySettings(LinkedHashMap<String, ArrayList<String>> attributes) {

		for (Map.Entry<String, ArrayList<String>> entries : attributes.entrySet()) {
			if (propertySettings.contains(entries.getKey())) {
			}
			if (propertySettings.size() != 0) {
				for (HashMap<String, StringPropertySettings> map : propertySettings) {
					Iterator it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						if (pair.getKey().equals(entries.getKey())) {
							ArrayList<String> update = checkExtendedStringSettings(map.get(entries.getKey()),
									entries.getValue());
							updatedAttributes.put(entries.getKey(), update);
							break;
						} else {
							updatedAttributes.put(entries.getKey(), entries.getValue());
						}
					}
				}
			} else {
				StringPropertySettings stringSettings = new StringPropertySettings();
				ArrayList<String> update = checkExtendedStringSettings(stringSettings, entries.getValue());
				updatedAttributes.put(entries.getKey(), update);
			}
		}
	}

	/**
	 * Set the properties to every date attributes.
	 * 
	 * @param attributes
	 *            mapping of attribute name and settings
	 */
	public static void setDateToPropertySettings(LinkedHashMap<String, ArrayList<String>> attributes,
			ArrayList<String> format) {

		int index = 0;
		System.out.println("1");
		for (Map.Entry<String, ArrayList<String>> entries : attributes.entrySet()) {
			if (propertySettings.contains(entries.getKey())) {
			}
			if (propertySettings.size() != 0) {
				for (HashMap<String, DatePropertySettings> map : propertySettings) {
					Iterator it = map.entrySet().iterator();

					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						if (pair.getKey().equals(entries.getKey())) {
							ArrayList<String> update = checkExtendedDateSettings(map.get(entries.getKey()),
									entries.getValue(), format.get(index));
							updatedAttributes.put(entries.getKey(), update);
							break;
						}
						it.remove();
					}
				}
			} else {
				DatePropertySettings dateSettings = new DatePropertySettings(null, null, false);
				ArrayList<String> update = checkExtendedDateSettings(dateSettings, entries.getValue(),
						format.get(index));
				updatedAttributes.put(entries.getKey(), update);
			}
			index++;
		}
	}

	/**
	 * Set the properties to every integer attributes.
	 * 
	 * @param attributes
	 *            mapping of attribute name and settings
	 */
	public static void setIntegerToPropertySettings(LinkedHashMap<String, ArrayList<String>> attributes) {

		for (Map.Entry<String, ArrayList<String>> entries : attributes.entrySet()) {
			if (propertySettings.contains(entries.getKey())) {
			}

			if (propertySettings.size() != 0) {
				for (HashMap<String, IntegerPropertySettings> map : propertySettings) {
					Iterator it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						if (pair.getKey().equals(entries.getKey())) {
							ArrayList<String> update = checkExtendedIntegerSettings(map.get(entries.getKey()),
									entries.getValue());
							updatedAttributes.put(entries.getKey(), update);
							break;
						}
						it.remove();
					}
				}
			} else {
				IntegerPropertySettings integerSettings = new IntegerPropertySettings(null, null, false);
				ArrayList<String> update = checkExtendedIntegerSettings(integerSettings, entries.getValue());
				updatedAttributes.put(entries.getKey(), update);
			}
		}
	}

	/**
	 * Check if date values are in valid format.
	 * @param format valid date format
	 * @param attributes all date values.
	 */
	public static void checkInvalidDateFormat(String format, ArrayList<String> attributes) {

		int index = 0;
		for (String attribut : attributes) {
			Date date = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				date = sdf.parse(attribut);
				if (!attribut.equals(sdf.format(date))) {
					invalidRows.add(index);
				}
			} catch (ParseException e) {
				invalidRows.add(index);
			}
		}
	}

	/**
	 * Check if each string value meets the restrictions.
	 * @param settings String settings
	 * @param attributes all attributes
	 * @return updated list of the attributes.
	 */
	public static ArrayList<String> checkExtendedStringSettings(StringPropertySettings settings,
			ArrayList<String> attributes) {

		int index = 0;
		boolean valid = true;
		ArrayList<String> newAttributes = new ArrayList<String>();
		for (String attribute : attributes) {
			String newAttribute = attribute;
			if (attribute != null) {
				if (settings.getMinLength() == null || settings.getMinLength().trim().equals("")) {

				} else if (attribute.length() < Integer.parseInt(settings.getMinLength().trim())) {
					StringBuilder str = new StringBuilder(attribute);
					str.setLength(Integer.parseInt(settings.getMinLength().trim()));
					newAttribute = str.toString();
					valid = false;
				}
				if (settings.getMaxLength() == null || settings.getMaxLength().trim().equals("")) {

				} else if (attribute.length() > Integer.parseInt(settings.getMaxLength())) {
					StringBuilder str = new StringBuilder(attribute);
					str.setLength(Integer.parseInt(settings.getMaxLength().trim()));
					newAttribute = str.toString();
					valid = false;
				}
				if (settings.isRemoveSpaces()) {
					newAttribute = newAttribute.replaceAll(Constants.PATTERN_SPACE, Constants.PATTERN_NON_STRING);
				}
				if (settings.isRemoveWhitespaces()) {
					newAttribute = newAttribute.replaceAll(Constants.PATTERN_WHITESPACES, Constants.PATTERN_NON_STRING);
				}
				if (settings.getAllowedChars() != null) {
					if (!settings.getAllowedChars().get(0).equals(Constants.PATTERN_NON_STRING)
							&& !settings.getAllowedChars().contains(attribute)) {
						valid = false;
					}
				}
				if (settings.getIllegalChars() != null) {
					for (int i = 0; i < settings.getIllegalChars().size(); i++) {
						if (settings.getIllegalChars() != null) {
							if (attribute.contains(settings.getIllegalChars().get(i))) {
								if (settings.getAlternativChars().get(i) != null) {
									newAttribute = newAttribute.replaceAll(settings.getIllegalChars().get(i),
											settings.getAlternativChars().get(i));
								} else {
									newAttribute = newAttribute.replaceAll(settings.getIllegalChars().get(i), Constants.PATTERN_NON_STRING);
								}
							}
						}
					}
				}
			} else {
				valid = false;
			}
			if (!valid) {
				if (!invalidRows.contains(index)) {
					invalidRows.add(index);
				}
			}
			valid = true;
			index++;
			newAttributes.add(newAttribute);
		}

		return newAttributes;
	}

	/**
	 * Check if each date value meets the restrictions.
	 * @param settings Date settings
	 * @param attributes all attributes
	 * @return updated list of the attributes.
	 */
	public static ArrayList<String> checkExtendedDateSettings(DatePropertySettings settings,
			ArrayList<String> attributes, String dateFormat) {

		ArrayList<String> newAttributes = new ArrayList<String>();
		int index = 0;
		boolean valid = true;
		for (String attribute : attributes) {
			String newAttribute = attribute;
			if (attribute != null) {
				Date date = null;
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				try {
					date = sdf.parse(attribute);
				} catch (ParseException ex) {
					valid = false;
				}
				if (valid) {
					if (!attribute.equals(sdf.format(date))) {
						valid = false;
					}
					if (settings.getMaximum() == null) {

					} else if (!date.before(settings.getMaximum())) {
						valid = false;
					}
					if (settings.getMinimum() == null) {
					} else if (!date.after(settings.getMinimum())) {
						valid = false;
					}
					if (!settings.isAllowFuture()) {
						if (date.after(
								Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))) {
							valid = false;
						}
					}
				}
			} else {
				valid = false;
			}
			if (!valid) {
				if (!invalidRows.contains(index)) {
					invalidRows.add(index);
					newAttribute = Constants.PATTERN_NON_STRING;
				}
			}
			valid = true;
			index++;
			newAttributes.add(newAttribute);
		}
		return newAttributes;
	}

	/**
	 * Check if each integer value meets the restrictions.
	 * @param settings integer settings
	 * @param attributes all attributes
	 * @return updated list of the attributes.
	 */
	public static ArrayList<String> checkExtendedIntegerSettings(IntegerPropertySettings settings,
			ArrayList<String> attributes) {

		ArrayList<String> newAttributes = new ArrayList<String>();
		int index = 0;
		boolean valid = true;
		for (String attribute : attributes) {
			String newAttribute = attribute;
			if (attribute != null) {
				try {
					Integer num = Integer.parseInt(attribute.trim());
					if (settings.getMaximum() != null) {
						if (num > settings.getMaximum()) {
							valid = false;
						}
					}
					if (settings.getMinimum() != null) {
						if (num < settings.getMinimum()) {
							valid = false;
						}
					}
					if (!settings.isNegativInt()) {
						if (num < 0) {
							valid = false;
						}
					}
				} catch (NumberFormatException nfe) {
					valid = false;
				}
			} else {
				valid = false;
			}
			if (!valid) {
				if (!invalidRows.contains(index)) {
					invalidRows.add(index);
					newAttribute = Constants.PATTERN_NON_STRING;
				}
			}
			valid = true;
			index++;
			newAttributes.add(newAttribute);
		}
		return newAttributes;
	}

	/**
	 * Set the invalid rows.
	 * @return invalid rows
	 */
	public static Set<Integer> getInvalidRows() {
		return invalidRows;
	}

	/**
	 * Get a map of all updated attributes.
	 * @return updated attributes
	 */
	public static LinkedHashMap<String, ArrayList<String>> getUpdatedAttributes() {
		return updatedAttributes;
	}
}
