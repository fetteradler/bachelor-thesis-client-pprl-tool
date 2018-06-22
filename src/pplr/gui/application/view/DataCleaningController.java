package pplr.gui.application.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import pplr.gui.application.Main;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.configuration.LayoutConstants;
import pplr.gui.application.functions.PatternHandling;
import pplr.gui.application.model.ConfigurationObjectSingleton;
import pplr.gui.application.model.ConnectAttributSingleton;
import pplr.gui.application.model.SplitAttributSingleton;
import pplr.gui.application.settings.DataCleaningSettings;
import pplr.gui.application.tools.DynamicTableViewSingleton;
import pplr.gui.application.tools.ImportCSV;

/**
 * Controller of the data cleaning tab.
 * 
 * @author CM
 *
 */
public class DataCleaningController {

	/** Defines all Buttons in the pane. **/
	@FXML
	private Button splitButton, mergeButton, checkButton, continueButton, removePatternButton, leadingZeroButton,
			backButton;

	/** Defines the table views. **/
	@FXML
	private TableView<ObservableList<StringProperty>> dataTableCleaning, invalidTableView;

	/** The split pane. **/
	@FXML
	private SplitPane dataCleaningSplit;

	/** The anchor pane of the tab. **/
	@FXML
	AnchorPane dataCleaningAnchor;

	/** Controller to the split attribute dialog. **/
	@FXML
	private SplitAttributDialogController splitController;

	/** Controller to the connect attribute dialog. **/
	@FXML
	private ConnectAttributDialogController connectController;

	/** Contains the selected data records for the table view. */
	private DynamicTableViewSingleton dynamicTableView;

	/** Defines the attributes that are necessary for the record linkage. */
	private ConfigurationObjectSingleton configObject;

	/** Functions for the split button. */
	private SplitAttributSingleton splitSilngleton;

	/** Functions for the connect button. */
	private ConnectAttributSingleton connectSingleton;

	/** The tab pane of the application. */
	private TabPane tabPane;

	/**
	 * Contains for every necessary attribute comboboxes to assign attribute from
	 * data set and type.
	 */
	private ComboBox<String>[] attributesComboBoxesArray, typesComboBoxesArray;

	/** Maps the index/the selected value to the combobox. */
	private LinkedHashMap<Integer, String> typesComboMapper, attributsComboMapper;

	/** Main of the application. */
	private Main main;

	/** All column names of the table view. */
	private ArrayList<String> columnNames;

	/** Contains the settings the user set for the tab */
	private DataCleaningSettings settings;

	/*
	 * ------------------------------ Initialize controller
	 * ------------------------------
	 */

	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	private void initialize() {

		dynamicTableView = DynamicTableViewSingleton.getInstance();

		splitSilngleton = SplitAttributSingleton.getInstance();
		connectSingleton = ConnectAttributSingleton.getInstance();
		configObject = ConfigurationObjectSingleton.getInstance();

		// Initialize the controls with user date.
		createAttributeLabels();
		createAttributeComboBoxes();
		createTypeComboBoxes();

		// Event handling
		continueButton.setOnAction(this::handleContinueButton);
		removePatternButton.setOnAction(this::handleRemovePatternButton);
		leadingZeroButton.setOnAction(this::handlLeadingZero);
		checkButton.setOnAction(this::handleCheckButton);
		splitButton.setOnAction(this::handleSplitButton);
		mergeButton.setOnAction(this::handleMergeButton);
		backButton.setOnAction(this::handleBackButton);
	}

	/*
	 * ------------------------------ Creation controls
	 * ------------------------------
	 */

	/**
	 * Create labels for all necessary attributes for the record linkage.
	 */
	public void createAttributeLabels() {
		Label[] allLabels = new Label[configObject.getAttributeNames().size()];
		double labelLayoutY = LayoutConstants.DC_LABEL_LAYOUT_Y;
		for (int i = 0; i < allLabels.length; i++) {
			allLabels[i] = new Label();
			allLabels[i].setText(configObject.getAttributeNames().get(i).getValue());
			dataCleaningAnchor.getChildren().add(allLabels[i]);
			allLabels[i].setLayoutX(LayoutConstants.DC_LABEL_LAYOUT_X);
			allLabels[i].setLayoutY(labelLayoutY);

			labelLayoutY = labelLayoutY + LayoutConstants.DC_MODIFIER;
		}
		dataCleaningAnchor.setPrefHeight(labelLayoutY + LayoutConstants.DC_MODIFIER);
	}

	/**
	 * Create comboboxes for all necessary attributes for the record linkage.
	 */
	public void createAttributeComboBoxes() {
		attributesComboBoxesArray = new ComboBox[configObject.getAttributeNames().size()];
		attributsComboMapper = new LinkedHashMap<Integer, String>();
		double userLayoutY = LayoutConstants.DC_MODIFIER;
		for (int i = 0; i < attributesComboBoxesArray.length; i++) {
			attributesComboBoxesArray[i] = new ComboBox<String>();
			attributesComboBoxesArray[i].setPromptText(Constants.DC_PROMT_ATTRIBUTE);
			attributesComboBoxesArray[i].setPrefWidth(LayoutConstants.DC_PREF_WIDTH);
			dataCleaningAnchor.getChildren().add(attributesComboBoxesArray[i]);
			attributesComboBoxesArray[i].setLayoutX(LayoutConstants.DC_USER_LAYOUT_X);
			attributesComboBoxesArray[i].setLayoutY(userLayoutY);

			final int index = i;
			attributsComboMapper.put(index, attributesComboBoxesArray[index].valueProperty().getValue());

			attributesComboBoxesArray[i].valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

					createAssignAttributeMap(index, attributesComboBoxesArray[index].valueProperty().getValue());
				}

			});

			userLayoutY = userLayoutY + LayoutConstants.DC_MODIFIER;
		}
	}

	/**
	 * Creates for every cofiguration attribute a combobox to select the data type
	 * of the attribute.
	 */
	public void createTypeComboBoxes() {
		typesComboBoxesArray = new ComboBox[configObject.getAttributeNames().size()];
		// to save selected type to attribute
		typesComboMapper = new LinkedHashMap<Integer, String>();
		double typeLayoutY = LayoutConstants.DC_MODIFIER;
		for (int i = 0; i < typesComboBoxesArray.length; i++) {
			typesComboBoxesArray[i] = new ComboBox<String>();
			typesComboBoxesArray[i].setPromptText(Constants.DC_PROMT_TYP);
			typesComboBoxesArray[i].setPrefWidth(LayoutConstants.DC_PREF_WIDTH);
			typesComboBoxesArray[i].getItems().setAll(Constants.TYPE_STRING, Constants.TYPE_INT, Constants.TYPE_DATE);
			dataCleaningAnchor.getChildren().add(typesComboBoxesArray[i]);
			typesComboBoxesArray[i].setLayoutX(LayoutConstants.DC_TYPE_LAYOUT_X);
			typesComboBoxesArray[i].setLayoutY(typeLayoutY);

			typeLayoutY = typeLayoutY + LayoutConstants.DC_MODIFIER;

			final int index = i;
			typesComboMapper.put(index, typesComboBoxesArray[index].valueProperty().getValue());

			typesComboBoxesArray[i].valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
					createTypeMap(index, typesComboBoxesArray[index].valueProperty().getValue());
				}

			});
		}
	}

	/* ------------------------------ ------------------------------ */

	/**
	 * Insert user data from the selected file into the tableView.
	 * 
	 * @param selectDataTab
	 *            Tab in which the user select the file.
	 */
	public void loadTableView(SelectDataTab2Controller selectDataTab) {

		if (dynamicTableView.getPath() != null) {
			if (!selectDataTab.isHideDataSelected()) {
				dynamicTableView.insertCSVData(dataTableCleaning);
			}

			// Get all column names of the csv data for the dialog
			columnNames = (ArrayList<String>) ImportCSV.importCSVDataHeader3(dynamicTableView.getPath());
		}
	}

	/**
	 * Insert selected type from an attribute into a LinkedHashMap.
	 */
	public void createTypeMap(int comboIndex, String typeValue) {
		typesComboMapper.replace(comboIndex, typeValue);
	}

	/**
	 * Insert selected attribute for each combobox into a LinkedHashMap.
	 */
	public void createAssignAttributeMap(int comboIndex, String typeValue) {
		attributsComboMapper.replace(comboIndex, typeValue);
	}

	// -------------------------------------------------------Settings-------------------------------------------------------

	/**
	 * Set the attributes in the settings to the comboboxes.
	 */
	public void setAttributeValues() {
		for (int i = 0; i < attributesComboBoxesArray.length; i++) {
			if (settings != null && settings.getAttributes() != null) {
				if (i < settings.getAttributes().length) {
					if (settings.getAttributes()[i] != null) {
						attributesComboBoxesArray[i].setValue(settings.getAttributes()[i]);
					}
				}
			}
		}
	}

	/**
	 * Set the datatype in the settings to the comboboxes.
	 */
	public void setTypeValues() {
		for (int i = 0; i < typesComboBoxesArray.length; i++) {
			if (settings != null && settings.getTypes() != null) {
				if (i < settings.getTypes().length) {
					if (settings.getTypes()[i] != null) {
						typesComboBoxesArray[i].setValue(settings.getTypes()[i]);
					}
				}
			}
		}
	}

	/* ------------------------------ Settings ------------------------------ */

	/**
	 * Save the datatype the user selected as settings.
	 */
	public void saveTypeSettings() {

		if (settings == null) {
			settings = new DataCleaningSettings();
		}

		String[] attributes = new String[typesComboBoxesArray.length];
		for (int i = 0; i < typesComboBoxesArray.length; i++) {
			attributes[i] = typesComboBoxesArray[i].getValue();
		}
		settings.setTypes(attributes);
	}

	/**
	 * Save the attributes the user selected as settings.
	 */
	public void saveAttributeSettings() {

		if (settings == null) {
			settings = new DataCleaningSettings();
		}

		String[] attributes = new String[attributesComboBoxesArray.length];
		for (int i = 0; i < attributesComboBoxesArray.length; i++) {
			attributes[i] = attributesComboBoxesArray[i].getValue();
		}
		settings.setAttributes(attributes);
	}

	/*
	 * ------------------------------ Event handling ------------------------------
	 */

	/**
	 * When the "Next" button is clicked, the tab pane switch to the standardization
	 * tab. The selected settings will be saved.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleContinueButton(ActionEvent event) {
		saveAttributeSettings();
		saveTypeSettings();
		tabPane.getSelectionModel().select(2);
	}

	/**
	 * When the "Remove useless patterns" button is clicked, user-defined pattern
	 * and strings will be removed from the data set. These pattern and strings are
	 * defined in /config/strings-to-delete.txt. The table view will be updated.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleRemovePatternButton(ActionEvent event) {
		ObservableList<ObservableList<String>> data = PatternHandling.removePattern(dynamicTableView);
		dynamicTableView.update(dataTableCleaning, data);
	}

	/**
	 * For all attributes with the selected type "date", the "Leading zeros date"
	 * button will check if these entries have leading zeroes or not. If not they
	 * will be added and the table view will be updated. E.g. 1.1.18 will be
	 * 01.01.2018
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handlLeadingZero(ActionEvent event) {
		ArrayList<Integer> dateColumns = new ArrayList<Integer>();
		List<String> columnNames = dynamicTableView.getColumnNames();
		for (int i = 0; i < attributesComboBoxesArray.length; i++) {
			if (columnNames.contains(attributesComboBoxesArray[i].valueProperty().getValue())) {
				if (typesComboBoxesArray[i].valueProperty().getValue() != null) {
					if (typesComboBoxesArray[i].valueProperty().getValue().equals(Constants.TYPE_DATE)) {
						dateColumns.add(columnNames.indexOf(attributesComboBoxesArray[i].valueProperty().getValue()));
					}
				}
			}
		}
		ObservableList<ObservableList<String>> data = PatternHandling.addLeadingZero(dynamicTableView, dateColumns);
		dynamicTableView.update(dataTableCleaning, data);
	}

	/**
	 * Check selected values of the data for the selected type and show invalid rows
	 * in another table view.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleCheckButton(ActionEvent event) {
		dynamicTableView.createInvalidTableView(invalidTableView, typesComboMapper, attributsComboMapper);
	}

	/**
	 * Will be called by clicking on the back button. Go the the previous tab.
	 * 
	 * @param event
	 *            Action event
	 */
	@FXML
	private void handleBackButton(ActionEvent event) {
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(0);
	}

	/**
	 * Set the event handing for the split button.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleSplitButton(ActionEvent event) {
		splitAttribut();
		refreshAttributeCombo();
	}

	/**
	 * Set the event handling for the connect button.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleMergeButton(ActionEvent event) {
		connectAttribut();
		refreshAttributeCombo();
	}

	/**
	 * Opens the dialog to splitting an attribute.
	 */
	private void splitAttribut() {
		boolean isClicked;
		isClicked = main.showSplitAttributDialog(columnNames, dataTableCleaning);
		if (isClicked) {
			dynamicTableView.updateSplit(dataTableCleaning, splitSilngleton.getOldAttribut(),
					splitSilngleton.getNewAttributes(), splitSilngleton.getPatternIndex());
			columnNames = (ArrayList<String>) dynamicTableView.getColumnNames();
		}
	}

	/**
	 * Opens the dialog to connect attributes.
	 */
	private void connectAttribut() {
		boolean isClicked;
		isClicked = main.showConnectAttributDialog(columnNames, dataTableCleaning);
		if (isClicked) {
			dynamicTableView.updateConnect(dataTableCleaning, connectSingleton.getNewAttribut(),
					connectSingleton.getOldAttributes(), connectSingleton.getPatternIndex());
			columnNames = (ArrayList<String>) dynamicTableView.getColumnNames();
		}
	}

	/**
	 * Insert user attribute names from the selected file into the combobox.
	 */
	public void refreshAttributeCombo() {

		for (int i = 0; i < attributesComboBoxesArray.length; i++) {
			attributesComboBoxesArray[i].getItems().clear();
			attributesComboBoxesArray[i].getItems().addAll(columnNames);
		}
	}

	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */

	/**
	 * Sets the tab pane to the controller.
	 * 
	 * @param tabPane
	 *            The general tab pane.
	 */
	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	/**
	 * Setter for the main application.
	 * 
	 * @param main
	 *            Main method
	 */
	public void setMain(Main main) {
		this.main = main;
	}

	/**
	 * Getter for assignAttributes.
	 * 
	 * @return
	 */
	public LinkedHashMap<Integer, String> getAssignAttributes() {
		return attributsComboMapper;
	}

	/**
	 * Getter for assignAttributes.
	 * 
	 * @return
	 */
	public LinkedHashMap<Integer, String> getDataTypes() {
		return typesComboMapper;
	}

	/**
	 * Set the data cleaning settings.
	 * @param settings
	 */
	public void setSettings(DataCleaningSettings settings) {
		this.settings = settings;
	}

	/**
	 * Get the data cleaning settings.
	 * @return the settings
	 */
	public DataCleaningSettings getSettings() {
		return settings;
	}
}
