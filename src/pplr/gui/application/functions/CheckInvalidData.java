package pplr.gui.application.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pplr.gui.application.configuration.Constants;

/**
 * Class to check every entry of the selected data if it is valid.
 * 
 * @author CM
 *
 */
public abstract class CheckInvalidData {

	/** Representation of the invalid data for the table view. */
	private static ObservableList<ObservableList<String>> invalidData;

	/** List of all invalid rows of the table view. */
	static Set<Integer> invalidRows = new HashSet<Integer>();

	/**
	 * Initialize the invalid data list.
	 */
	public static void createInvalidDataSet() {
		invalidData = FXCollections.observableArrayList();
	}

	/**
	 * Check every attribute according to its format.
	 * 
	 * @param indeces
	 *            of the invalid rows in the table view.
	 */
	public static void pickTypeFromMap(LinkedHashMap<LinkedHashMap<String, Integer>, ArrayList<String>> indeces) {

		for (Entry<LinkedHashMap<String, Integer>, ArrayList<String>> entry : indeces.entrySet()) {
			for (Entry<String, Integer> index : entry.getKey().entrySet()) {
				int type = index.getValue();
				switch (type) {
				case 1:
					checkInvalidString(entry.getValue());
					break;
				case 2:
					checkInvalidInteger(entry.getValue());
					break;
				case 3:
					checkInvalidDate(entry.getValue());
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Check every string attribute if it is a valid entry.
	 * 
	 * @param attributes
	 *            that should be checked.
	 */
	public static void checkInvalidString(ArrayList<String> attributes) {

		int index = 0;
		for (String attribut : attributes) {
			int check = 0;

			if (attribut == null) {
				check = 3;
			} else if (checkIfNotNUll(attribut)) {
				check = 1;
			} else if (!attribut.getClass().equals(String.class)) {
				check = 2;
			}
			if (check != 0) {
				if (!invalidRows.contains(index)) {
					invalidRows.add(index);
				}
			}
			index++;
		}
	}

	/**
	 * Check every integer attribute if it is a valid entry.
	 * 
	 * @param attributes
	 *            that should be checked.
	 */
	public static void checkInvalidInteger(ArrayList<String> attributes) {
		int index = 0;
		int convertedInteger = 0;

		for (String attribut : attributes) {
			boolean successParse = false;
			if (attribut != null) {
				try {
					convertedInteger = Integer.parseInt(attribut.trim());
					successParse = true;
				} catch (NumberFormatException e) {
				}
				if (!successParse) {
					if (!invalidRows.contains(index)) {
						invalidRows.add(index);
					}
				}
				index++;
			}
		}
	}

	/**
	 * Check every date attribute if it is a valid entry.
	 * 
	 * @param attributes
	 *            that should be checked.
	 */
	public static void checkInvalidDate(ArrayList<String> attributes) {

		int index = 0;
		for (String attribut : attributes) {
			Date date = null;
			String[] format = { Constants.DATE_DMY_DOTS, Constants.DATE_EMDY_COMMAS, Constants.DATE_MDY_SLASHES,
					Constants.DATE_YMD_DASHES, Constants.DATE_YMD_LOW_DASHES, Constants.DATE_YWU_DASHES };
			boolean successParse = false;

			if (attribut != null) {
				for (int i = 0; i < format.length; i++) {

					SimpleDateFormat sdf = new SimpleDateFormat(format[i]);
					try {
						date = sdf.parse(attribut);
						successParse = true;
						break;
					} catch (ParseException ex) {
					}
				}
			}
			if (!successParse) {
				if (!invalidRows.contains(index)) {
					invalidRows.add(index);
				}
			}
			index++;
		}
	}

	/**
	 * Update the table view with the invalid data.
	 * @param inData new data of the table view
	 * @return updated content of the table view.
	 */
	public static ObservableList<ObservableList<String>> updateInvalidDataSets(
			ObservableList<ObservableList<String>> inData) {

		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
		for (int l = 0; l < inData.size(); l++) {
			if (invalidRows.contains(inData.get(l))) {

				ObservableList<String> dataRow = FXCollections.observableList(inData.get(l));
				data.add(dataRow);
			}
		}
		return data;
	}

	/**
	 * Check it the attribute is not empty.
	 * @param attribute that should be checked.
	 * @return True, if the attribute is not empty.
	 */
	public static boolean checkIfNotNUll(String attribute) {
		return attribute.trim().isEmpty();
	}

	/**
	 * Get the invalid rows of the data.
	 * @return invalid rows.
	 */
	public static Set<Integer> getInvalidRows() {
		return invalidRows;
	}

}
