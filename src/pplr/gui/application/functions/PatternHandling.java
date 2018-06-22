package pplr.gui.application.functions;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.ObservableList;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.configuration.FilePathConstants;
import pplr.gui.application.tools.DynamicTableViewSingleton;
import pplr.gui.application.tools.FileConfiguration;

/**
 * Defines the dealing of pattern manipulation.
 * @author CM
 *
 */
public class PatternHandling {

	/**
	 * Create an data set in with defined pattern are removed.
	 * 
	 * @param dynamicTableView
	 *            table to remove these pattern
	 * @return data with removed pattern
	 */
	public static ObservableList<ObservableList<String>> removePattern(DynamicTableViewSingleton dynamicTableView) {

		File f = new File(FilePathConstants.REMOVE_PATTERN);
		ArrayList<String> pattern = FileConfiguration.readFile(f);

		ObservableList<ObservableList<String>> data = dynamicTableView.getData();

		for (int i = 0; i < data.size(); i++) {
			ObservableList<String> list = data.get(i);
			for (int j = 0; j < data.get(i).size(); j++) {
				String temp = data.get(i).get(j);
				if (temp != null) {
					for (String str : pattern) {
						if (temp.contains(str)) {
							list.set(j, temp.replace(str, ""));
							break;
						}
					}
				}
			}
			data.set(i, list);
		}
		return data;
	}

	/**
	 * Count the containing of a PLZ pattern.
	 * 
	 * @param value
	 *            to count pattern
	 * @return total of containing pattern
	 */
	public static int countPLZ(String value) {

		Pattern PLZ = Pattern.compile(Constants.PATTERN_PLZ_REST);
		Matcher m = PLZ.matcher(value);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count;
	}

	/**
	 * Split a value on a PLZ pattern in three parts.
	 * 
	 * @param value
	 *            to split
	 * @return an array of the three parts
	 */
	public static String[] splitOnPLZ(String value) {

		Pattern PLZ = Pattern.compile(Constants.PATTERN_PLZ_REST);
		Matcher m = PLZ.matcher(value);
		int start = 0;
		while (m.find()) {
			start = m.start();
		}

		String[] split = { value.substring(0, start - 1), value.substring(start, start + 5),
				value.substring(start + 6) };

		return split;
	}

	/**
	 * Count how often a pattern is contained in a string. This function helps to
	 * get the maximum of new attributes by splitting.
	 * 
	 * @param tableView
	 *            table with splitting data
	 * @param pattern
	 *            the pattern for splitting
	 * @param index
	 *            column with attributes for splitting
	 * @return maximum of pattern in a string from dataset
	 */
	public static int countPattern(DynamicTableViewSingleton tableView, String pattern, int index) {

		ObservableList<ObservableList<String>> data = tableView.getData();
		int count = 0;
		if (pattern.equals(Constants.PATTERN_PLZ)) {
			count = 2;
		} else {
			if (pattern.equals(Constants.PATTERN_SPACE_WORD)) {
				pattern = " ";
			}
			for (int i = 0; i < data.size(); i++) {
				String value = data.get(i).get(index);
				int temp = StringUtils.countMatches(value, pattern);
				if (count < temp) {
					count = temp;
				}
			}
		}
		return count;
	}

	/**
	 * For several columns the data will be checked if in this columns the format
	 * for data has leading zeros. If not, the data will be modified with leading
	 * zeros.
	 * 
	 * @param tableView
	 *            Tableview with the data
	 * @param columnIndex
	 *            Columns to check if the data form is valid.
	 * @return Upsated dataset with leading zeros.
	 */
	public static ObservableList<ObservableList<String>> addLeadingZero(DynamicTableViewSingleton tableView,
			ArrayList<Integer> columnIndex) {
		File f = new File(FilePathConstants.DATE_PATTERN);
		ArrayList<String> pattern = FileConfiguration.readFile(f);

		ObservableList<ObservableList<String>> data = tableView.getData();

		for (int i = 0; i < data.size(); i++) {
			ObservableList<String> list = data.get(i);
			for (int j = 0; j < data.get(i).size(); j++) {
				if (columnIndex.contains(j)) {
					String temp = data.get(i).get(j);
					if (temp != null) {
						for (String str : pattern) {
							if (temp.contains(str)) {
								int countPattern = StringUtils.countMatches(temp, str);
								String dateFormat = "";
								String[] parts;
								if (str.equals(Constants.PATTERN_DOT)) {
									parts = temp.split(Pattern.quote(Constants.PATTERN_DOT));
								} else {
									parts = temp.split(str);
								}
								int k = 0;
								while (k <= countPattern) {
									String substring = parts[k];
									if (substring.length() == 1) {
										substring = '0' + substring;
									}
									dateFormat = dateFormat + substring;
									if (k < countPattern) {
										dateFormat = dateFormat + str;
									}
									k++;
								}
								list.set(j, dateFormat);
								break;
							}
						}
					}
				}
			}
			data.set(i, list);
		}
		return data;
	}
}
