package pplr.gui.application.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.functions.CheckInvalidData;
import pplr.gui.application.functions.CheckInvalidForm;
import pplr.gui.application.functions.StringManipulation;

/**
 * Singleton to handle the act of the table view.
 * 
 * @author CM
 */
public class DynamicTableViewSingleton {

	private static DynamicTableViewSingleton instance;

	/** The data set for the table view. */
	private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

	/** The names of the columns for table view. */
	private List<String> columnNames;

	/** The path to the table view. */
	private String path;

	/**
	 * Restrict the shown data for the table view on 100 records.
	 */
	private ObservableList<ObservableList<String>> showData;

	/**
	 * Constructor. Create a new dynamic table view singleton object. This will only
	 * create once.
	 */
	private DynamicTableViewSingleton() {

	}

	/**
	 * Instance of the dynamic table view singleton object.
	 * 
	 * @return the dynamic table view object
	 */
	public static DynamicTableViewSingleton getInstance() {
		if (instance == null) {
			instance = new DynamicTableViewSingleton();
		}
		return instance;
	}

	/* --------------------------------CSV Handling-------------------------------- */

	/**
	 * Inserts the csv data from selected file into the table view.
	 * 
	 * @param dataTable
	 *            table view to insert selected data.
	 */
	public void createAndInsertCSVData(TableView dataTable) {
		importCSV();
		insertCSVData(dataTable);
	}

	/**
	 * Import the CSV data from file.
	 */
	public void importCSV() {
		if (data == null) {
			data = ImportCSV.importCSVData(path);
		} else {
			data.clear();
			data = ImportCSV.importCSVData(path);
		}
		if (columnNames == null) {
			columnNames = ImportCSV.importCSVDataHeader3(path);
		} else {
			columnNames.clear();
			columnNames = ImportCSV.importCSVDataHeader3(path);
		}
		limitRows();
	}

	/**
	 * Inserts the csv data from selected file into the table view.
	 * 
	 * @param dataTable
	 *            table view to insert selected data.
	 */
	public void insertCSVData(TableView dataTable) {

		if (dataTable.getColumns().size() != 0) {
			if (dataTable.getItems().size() != 0) {
				dataTable.getItems().clear();
			}
			dataTable.getColumns().clear();
		}
		createDataTableColumns(dataTable);
		dataTable.setItems(showData);
	}

	/* --------------------------------Create tableview-------------------------------- */

	/**
	 * Create the columns for a table view to the user selected data.
	 * 
	 * @param dataTable
	 *            table view to insert selected data.
	 */
	public void createDataTableColumns(TableView dataTable) {

		dataTable.getColumns().add(insertIndexColumn());
		for (int i = 0; i < columnNames.size(); i++) {
			final int finalIdx = i;
			TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
			dataTable.getColumns().add(column);
		}
	}

	/**
	 * Create columns for a table view to the user selected data.
	 * 
	 * @param dataTable
	 *            table view to insert selected data.
	 * @param columns
	 *            The columns that should be added to the table view.
	 */
	public void createDataTableColumns(TableView dataTable, ArrayList<String> columns) {

		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i) != null) {
				final int finalIdx = i;
				TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(i));
				column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
				dataTable.getColumns().add(column);
			}
		}
	}

	/**
	 * Insert an index column for every row.
	 */
	public TableColumn<String, Integer> insertIndexColumn() {
		TableColumn<String, Integer> indexColumn = new TableColumn<>();

		indexColumn.setCellFactory(col -> {
			TableCell<String, Integer> cell = new TableCell<>();
			cell.textProperty()
					.bind(Bindings.when(cell.emptyProperty()).then("").otherwise(cell.indexProperty().asString()));
			return cell;
		});
		return indexColumn;
	}

	/**
	 * Create the content of a table view that shows the invalid data. (Used in
	 * DataCleaningController) 
	 * 
	 * @param dataTable
	 *            TableView to insert data.
	 * @param dataTypes
	 *            Maps a configuration attribute to the type.
	 * @param assignAttributes
	 *            Maps a configuration attribute to the user attribute.
	 */
	public void createInvalidTableView(TableView dataTable, LinkedHashMap<Integer, String> dataTypes,
			LinkedHashMap<Integer, String> assignAttributes) {

		dataTable.getColumns().clear();
		ArrayList<String> columns = new ArrayList<String>();

		columns.add(""); // Indexcolumn to identify easier the invalid dataset in userdata.
		for (Map.Entry<Integer, String> entries : assignAttributes.entrySet()) {
			columns.add(entries.getValue());
		}
		updateColumns(dataTable, columns);

		LinkedHashMap<String, Integer> attributeType = columnTypeMapper(dataTypes, assignAttributes); // Map data type
																										// to attribute
		ArrayList<String> containingAttributes; // contains for one row the data for the columns
		LinkedHashMap<LinkedHashMap<String, Integer>, ArrayList<String>> dataPerRow =
				new LinkedHashMap<LinkedHashMap<String, Integer>, ArrayList<String>>(); // Map row
		for (Entry<String, Integer> column : attributeType.entrySet()) {
			containingAttributes = dataPicker(column.getKey());
			dataPerRow.put(attributeType, containingAttributes);
		}
		CheckInvalidData.pickTypeFromMap(dataPerRow);

		Set<Integer> invalidDataRows = CheckInvalidData.getInvalidRows();
		for(Integer i : invalidDataRows) {
		}
		dataTable.getItems().clear();
		insertTableDataByRows(dataTable, columns, invalidDataRows);
	}

	/**
	 * Create the content of a table view that shows the invalid data. (Used in
	 * StandartisierungController)
	 * 
	 * @param dataTable
	 * @param attributeToType
	 * @param specialFormat
	 */
	public void createInvalidTableView(TableView dataTable, LinkedHashMap<String, String> attributeToType,
			ArrayList<String> specialFormat) {

		dataTable.getColumns().clear();
		ArrayList<String> columns = new ArrayList<String>();

		columns.add(""); // Indexcolumn to identify easier the invalid dataset in userdata.
		for (Map.Entry<String, String> entries : attributeToType.entrySet()) {
			columns.add(entries.getKey());
		}
		updateColumns(dataTable, columns);

		//
		LinkedHashMap<String, ArrayList<String>> attributeString = new LinkedHashMap<String, ArrayList<String>>();
		LinkedHashMap<String, ArrayList<String>> attributeDate = new LinkedHashMap<String, ArrayList<String>>();
		LinkedHashMap<String, ArrayList<String>> attributeInteger = new LinkedHashMap<String, ArrayList<String>>();

		ArrayList<String> dateFormat = new ArrayList<String>(); // Only for "date" data type to set the selected format
		int index = 0;
		for (Entry<String, String> entries : attributeToType.entrySet()) {
			if (entries.getValue().equals(Constants.TYPE_STRING)) {
				attributeString.put(entries.getKey(), dataPicker(entries.getKey()));
			} else if (entries.getValue().equals(Constants.TYPE_DATE)) {
				dateFormat.add(specialFormat.get(index));
				attributeDate.put(entries.getKey(), dataPicker(entries.getKey()));
			} else if (entries.getValue().equals(Constants.TYPE_INT)) {
				attributeInteger.put(entries.getKey(), dataPicker(entries.getKey()));
			}
			index++;
		}

		CheckInvalidForm.setStringToPropertySettings(attributeString);
		CheckInvalidForm.setDateToPropertySettings(attributeDate, dateFormat);
		CheckInvalidForm.setIntegerToPropertySettings(attributeInteger);
		Set<Integer> invalidDataRows = CheckInvalidForm.getInvalidRows();
		insertTableDataByRows(dataTable, columns, invalidDataRows);
	}

	/**
	 * Maps the name of the user attribute to the data type.
	 * 
	 * @param dataTypes
	 *            Attribute types.
	 * @param assignAttributes
	 *            Attribute names.
	 * @return Map of attribute name and type
	 */
	public LinkedHashMap<String, Integer> columnTypeMapper(LinkedHashMap<Integer, String> dataTypes,
			LinkedHashMap<Integer, String> assignAttributes) {

		LinkedHashMap<String, Integer> attributeType = new LinkedHashMap<String, Integer>(); // Set column name to type
		// Iterate through the user attribute names and map these to the data type
		for (Map.Entry<Integer, String> entries : assignAttributes.entrySet()) {
			for (Map.Entry<Integer, String> types : dataTypes.entrySet()) {
				if (types.getKey() == entries.getKey()) {
					int type = 0;
					if (types.getValue() != null) {
						if (types.getValue().trim().equals(Constants.TYPE_STRING)) {
							type = 1;
						} else if (types.getValue().trim().equals(Constants.TYPE_INT)) {
							type = 2;
						} else if (types.getValue().trim().equals(Constants.TYPE_DATE)) {
							type = 3;
						}
						attributeType.put(entries.getValue(), type);
					}
				}
			}
		}
		return attributeType;
	}

	/**
	 * For several columns this method will pick the including data for the
	 * table view.
	 * 
	 * @param column
	 *            The column that contains the data.
	 * @return The data in the columns.
	 */
	public ArrayList<String> dataPicker(String column) {

		ArrayList<String> attributes = new ArrayList<String>();// Set attributes
		for (int l = 0; l < data.size(); l++) {
			for (int k = 0; k < data.get(l).size(); k++) {
				if (column.equals(columnNames.get(k))) {
					attributes.add(data.get(l).get(k));
				}
			}
		}
		return attributes;
	}

	/**
	 * Insert content to a table view per row. Only a certain set of rows should be
	 * insert. This method is used for the creation of a table view that shows the
	 * invalid data.
	 * 
	 * @param dataTable
	 *            table view to insert data.
	 * @param columns
	 *            columns for the table view
	 * @param dataRows
	 *            Index of the rows that should be insert in table view.
	 */
	public void insertTableDataByRows(TableView dataTable, ArrayList<String> columns, Set<Integer> dataRows) {

		ObservableList<ObservableList<String>> newData = FXCollections.observableArrayList();
		for (int l = 0; l < data.size(); l++) {
			if (dataRows.contains(l)) {
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(String.valueOf(l));
				for (String str : columns) {
					for (int k = 0; k < data.get(l).size(); k++) {
						if (columnNames.get(k).equals(str)) {
							if (data.get(l).get(k) == null) {
								newList.add("");
							} else {
								newList.add(data.get(l).get(k));
							}
						}
					}
				}
				for(String s : newList) {
				}
				ObservableList<String> dataRow = FXCollections.observableList(newList);
				newData.add(dataRow);
			}
		}
		ObservableList<ObservableList<String>> newShowData = returnLimitRows(newData);
		dataTable.setItems(newShowData);
	}

	/**
	 * Hide the csv data from the selected file into the table view
	 */
	public void hideTableDate(TableView dataTable) {
		createDataTableColumns(dataTable);
		dataTable.getItems().clear();
	}

	/* --------------------------------Update-------------------------------- */

	/**
	 * Update the TableView with new datasets. For this the new dataset is already
	 * in the form of the table view items and it contains all records, so that it
	 * only has to be insert.
	 * 
	 * @param dataTable
	 *            Table view that should be updated.
	 * @param newData
	 *            New data sets for the table view.
	 */
	public void update(TableView dataTable, ObservableList<ObservableList<String>> newData) {

		updateColumns(dataTable);
		limitRows(newData);
		dataTable.setItems(showData);
	}

	/**
	 * Update the data of a table view with new values. The values that should be
	 * updated are given as an ArrayList.
	 * 
	 * @param dataTable
	 *            Table view that should be updated.
	 * @param values
	 *            New values for the table view.
	 * @return updated items of the table view.
	 */
	public ObservableList<ObservableList<String>> updateData(TableView dataTable, ArrayList<ArrayList<String>> values) {

		ObservableList<ObservableList<String>> updateData = FXCollections.observableArrayList();
		for (int l = 0; l < data.size(); l++) {
			ArrayList<String> newList = new ArrayList<String>();
			for (int k = 0; k < data.get(l).size(); k++) {
				if (values.get(k) != null) {
					for (String s : values.get(k)) {
					}
					newList.add(values.get(k).get(l));
				} else {
					newList.add(data.get(l).get(k));
				}
			}
			ObservableList<String> dataRow = FXCollections.observableList(newList);
			updateData.add(dataRow);
		}
		limitRows(updateData);
		dataTable.setItems(showData);

		return updateData;
	}

	/**
	 * Updates the columns of a table view.
	 * 
	 * @param dataTable
	 *            The table view.
	 */
	public void updateColumns(TableView dataTable) {
		dataTable.getColumns().clear();
		createDataTableColumns(dataTable);
	}

	/**
	 * Updates the columns of a table view.
	 * 
	 * @param dataTable
	 *            The table view.
	 * @param columns Name of the columns
	 */
	public void updateColumns(TableView dataTable, ArrayList<String> columns) {
		dataTable.getColumns().clear();
		createDataTableColumns(dataTable, columns);
	}

	/**
	 * Updates the table view with property restrictions
	 * 
	 * @param dataTable
	 *            the table view to update
	 */
	public void updateRestrictions(TableView dataTable) {

		LinkedHashMap<String, ArrayList<String>> update = CheckInvalidForm.getUpdatedAttributes();

		dataTable.getItems().clear();
		ArrayList<ArrayList<String>> attributesToUpdate = new ArrayList<ArrayList<String>>();
		for (String column : columnNames) {
			boolean exist = false;
			for (Entry<String, ArrayList<String>> entries : update.entrySet()) {
				if (entries.getKey().equals(column)) {
					attributesToUpdate.add(entries.getValue());
					exist = true;
				}
			}
			if (!exist) {
				attributesToUpdate.add(null);
			}
		}
		updateData(dataTable, attributesToUpdate);
	}

	/**
	 * Update the table view for the connect action.
	 * 
	 * @param dataTable
	 *            Table view that should be updated.
	 * @param newAttribut
	 *            The name of the new connected attribute.
	 * @param oldAttributes
	 *            The attributes that should be connect.
	 * @param pattern
	 *            The pattern for the connection.
	 */
	public void updateConnect(TableView dataTable, String newAttribut, ArrayList<String> oldAttributes, int pattern) {
		// create new column names
		ArrayList<String> newColumnNames = new ArrayList<>();
		int index = 0;
		int j = 0;
		boolean alreadyIn = false;
		ArrayList<Integer> allIndex = new ArrayList<Integer>();
		for (String s : columnNames) {
			boolean contains = false;
			for (String str : oldAttributes) {
				if (s.trim().equals(str.trim())) {
					allIndex.add(j);
					if (!alreadyIn) {
						newColumnNames.add(newAttribut);
						alreadyIn = true;
						index = j;
					}
					contains = true;
					break;
				}
			}
			if (!contains) {
				newColumnNames.add(s);
			}
			j++;
		}
		columnNames = (ArrayList<String>) newColumnNames.clone();
		updateColumns(dataTable);

		// add new data to columns
		ObservableList<ObservableList<String>> newData = FXCollections.observableArrayList();

		// observable list with observable lists
		for (int l = 0; l < data.size(); l++) {
			ArrayList<String> newList = new ArrayList<String>();
			for (int k = 0; k < data.get(l).size(); k++) {
				if (k < index) {
					newList.add(data.get(l).get(k));
				} else if (k == index) {

					ArrayList<String> connectList;
					if(data.get(l).get(k) == null) {
						connectList = new ArrayList<>();
						connectList.add("");
					} else {
						connectList = new ArrayList<String>();
					}
					for (Integer i : allIndex) {
						connectList.add(data.get(l).get(i));
					}
					String connectedString = StringManipulation.connectString(connectList, pattern);
					newList.add(connectedString);

				} else if (k > index) {

					boolean selectedAttribute = false;
					for (Integer i : allIndex) {
						if (k == i) {
							selectedAttribute = true;
						}
					}
					if (!selectedAttribute) {
						newList.add(data.get(l).get(k));
					}
				}
			}
			ObservableList<String> dataRow = FXCollections.observableList(newList);
			newData.add(dataRow);
		}
		limitRows(newData);
		dataTable.setItems(showData);
	}

	/**
	 * Update the table view for the split action.
	 * 
	 * @param dataTable
	 *            Table view that should be updated.
	 * @param oldAttribut
	 *            Attribute that should be split
	 * @param newAttributes
	 *            New names for resulting attributes
	 * @param pattern
	 *            The pattern on which the attribute should be split.
	 */
	public void updateSplit(TableView dataTable, String oldAttribut, ArrayList<String> newAttributes, int pattern) {
		// create new column names
		ArrayList<String> newColumnNames = new ArrayList<>();
		int index = 0;
		int j = 0;
		for (String s : columnNames) {
			if (!s.trim().equals(oldAttribut.trim())) {
				newColumnNames.add(s);
			} else {
				for (String str : newAttributes) {
					newColumnNames.add(str);
					index = j;
				}
			}
			j++;
		}
		columnNames = (ArrayList<String>) newColumnNames.clone();
		updateColumns(dataTable);

		// add content to column
		ObservableList<ObservableList<String>> newData = FXCollections.observableArrayList();

		for (int l = 0; l < data.size(); l++) {
			ArrayList<String> newList = new ArrayList<String>();
			for (int k = 0; k < data.get(l).size(); k++) {
				if (k != index) {
					newList.add(data.get(l).get(k));
				} else if (k == index) {
					ArrayList<String> splitList;
					if(data.get(l).get(k) == null) {
						splitList = new ArrayList<>();
						splitList.add("");
					} else {
					splitList = StringManipulation.splitString(data.get(l).get(k), pattern);
					}
					for (int i = 0; i < newAttributes.size(); i++) {
						if (splitList.size() <= i) {
							newList.add("");
						} else {
							newList.add(splitList.get(i));
						}
					}
				}
			}
			ObservableList<String> dataRow = FXCollections.observableList(newList);
			newData.add(dataRow);
		}
		limitRows(newData);
		dataTable.setItems(showData);
	}

	/* --------------------------------Limit-------------------------------- */

	/**
	 * Restricted the row to show in table.
	 */
	public void limitRows() {

		if (showData != null) {
			showData.clear();
		}
		if (data.size() < 100) {
			showData = FXCollections.observableArrayList(data);
		} else {
			for (int i = 0; i < 100; i++) {
				showData = FXCollections.observableArrayList();
				showData.add(data.get(i));
			}
		}
	}

	/**
	 * Restricted the row to show in table.
	 */
	public void limitRows(ObservableList<ObservableList<String>> newData) {

		showData.clear();
		if (newData.size() < 100) {
			showData = FXCollections.observableArrayList(newData);
		} else {
			for (int i = 0; i < 100; i++) {
				showData = FXCollections.observableArrayList();
				showData.add(newData.get(i));
			}
		}

		data = FXCollections.observableArrayList(newData);
	}
	
	/**
	 * Restricted the row to show in table.
	 */
	public ObservableList<ObservableList<String>> returnLimitRows(ObservableList<ObservableList<String>> newData) {

		ObservableList<ObservableList<String>> newShowData = FXCollections.observableArrayList();
		if (newData.size() < 100) {
			newShowData = FXCollections.observableArrayList(newData);
		} else {
			for (int i = 0; i < 100; i++) {
				newShowData = FXCollections.observableArrayList();
				newShowData.add(newData.get(i));
			}
		}
		return newShowData;
	}

	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Set the path of the table view
	 * 
	 * @param path
	 *            of the table view
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the path of the table view.
	 * 
	 * @return path of the table view
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Getter for columnNames.
	 * @return list of all column names
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * Getter for data.
	 * @return the containing data in table view
	 */
	public ObservableList<ObservableList<String>> getData() {
		return data;
	}
}
