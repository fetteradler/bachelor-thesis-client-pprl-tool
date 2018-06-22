package pplr.gui.application.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.io.dozer.ICsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Export the data from tableView to file system.
 * 
 * @author CM
 *
 */
public abstract class ExportCSV {

	/** Contains the selected data records for the table view. **/
	private static DynamicTableViewSingleton tableView = DynamicTableViewSingleton.getInstance();

	/**
	 * 
	 * @param path
	 *            Location in the file system to export.
	 * @return True if the export was successful.
	 */
	public static boolean exportStandartisierung2(String path, String[] columnNames) throws IOException {

		tableView = DynamicTableViewSingleton.getInstance();

		boolean successes = false;

		ICsvDozerBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvDozerBeanWriter(new FileWriter(path), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

			ArrayList<Optional> list = new ArrayList<>();
			for (int i = 0; i < columnNames.length; i++) {
				beanWriter.writeHeader(columnNames[i]);
				list.add(new Optional());
			}
			for (int i = 0; i < tableView.getData().size(); i++) {
				for (int j = 0; j < columnNames.length; j++) {
				}
				beanWriter.write(tableView.getData().get(i), createProcessor(list));

			}
			successes = true;
		} finally {
			if (beanWriter != null) {
				beanWriter.close();
			}
		}
		return successes;
	}

	/**
	 * Write the data from the table view as csv file in the selected file system.
	 * 
	 * @param path
	 *            to export data
	 * @return true if the writing was successful
	 * @throws IOException
	 *             thrown if the writer could not be open
	 */
	public static boolean exportData(String path) throws IOException {
		boolean success = false;
		CSVWriter writer = null;
		String[] columnNames = new String[tableView.getColumnNames().size()];
		List<String> cn = tableView.getColumnNames();
		columnNames = cn.toArray(columnNames);

		try {
			if (path != null) {
				File file = new File(path);
				writer = new CSVWriter(file, null);
				writer.writeHeader(columnNames);
				for (int i = 0; i < tableView.getData().size(); i++) {
					String[] data = new String[tableView.getData().get(i).size()];
					data = tableView.getData().get(i).toArray(data);
					writer.writeData(data);
				}
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return success;
	}

	/**
	 * Create the cell processor for the beanWriter to write in csv data.
	 * 
	 * @param elements
	 *            for the cell processor
	 * @return CellProcessor[]
	 */
	public static CellProcessor[] createProcessor(ArrayList<Optional> elements) {

		CellProcessor[] processor = new CellProcessor[tableView.getData().get(0).size()];
		processor = elements.toArray(processor);
		return processor;
	}
}
