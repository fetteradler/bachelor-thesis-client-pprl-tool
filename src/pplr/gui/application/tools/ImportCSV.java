package pplr.gui.application.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that provides the import of csv data from the file system in the
 * application.
 * 
 * @author CM
 *
 */
public class ImportCSV {

	/** Reader for csv files. */
	private static ICsvMapReader reader;

	/**
	 * Representation of the csv data in table view format. The outer observable
	 * list represents each row in the table view, the inner is for each entry in a
	 * row.
	 */
	private static ObservableList<ObservableList<String>> csvData = FXCollections.observableArrayList();

	/** Counter of the rows. */
	private static int rowCounter = 0;

	/** 
	 * Initialize an reader for the csv file.
	 * @param path to the file system
	 * @return reader with the csv file
	 */
	public static ICsvMapReader readCSVFile(String path) {

		try {
			ICsvMapReader reader = new CsvMapReader(new FileReader(path), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			return reader;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read the data from a csv file.
	 * 
	 * @param path in file system
	 * @return data of the csv file
	 */
	public static ObservableList<ObservableList<String>> importCSVData(String path) {

		try {
			reader = readCSVFile(path);

			final String[] header = reader.getHeader(true);
			importCSVDataHeader(header);

			Map<String, String> map;
			while ((map = reader.read(header)) != null) {
				rowCounter++;
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < header.length; i++) {

					String value = map.get(header[i]);
					if (value != null) {
						list.add(value.trim());
					} else {
						list.add(value);
					}

				}
				ObservableList<String> dataRow = FXCollections.observableList(list);
				csvData.add(dataRow);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvData;
	}

	/**
	 * Import the header of the csv file. 
	 * @param header elements of the csv file
	 */
	public static void importCSVDataHeader(String[] header) {

		List<StringProperty> headerList = new ArrayList<StringProperty>();

		for (int i = 0; i < header.length; i++) {
			StringProperty valueProberty = new SimpleStringProperty();
			valueProberty.setValue(header[i]);
			headerList.add(valueProberty);
		}

		ObservableList<StringProperty> headerRow = FXCollections.observableList(headerList);
	}

	/**
	 * Import the header of the csv file. 
	 * @param path in file system
	 * @return list of all header elements
	 */
	public static List<String> importCSVDataHeader3(String path) {

		List<String> headerList = new ArrayList<String>();
		reader = readCSVFile(path);

		String[] header;
		try {
			header = reader.getHeader(true);
			for (int i = 0; i < header.length; i++) {
				String value = header[i].trim();
				headerList.add(value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headerList;
	}

	/**
	 * Get the number of rows.
	 * @return number of rows
	 */
	public static int getRowCounter() {
		return rowCounter;
	}
}
