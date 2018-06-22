package pplr.gui.application.view;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import pplr.gui.application.configuration.FilePathConstants;
import pplr.gui.application.settings.SelectDataSettings;
import pplr.gui.application.tools.DynamicTableViewSingleton;
import pplr.gui.application.tools.FileTreeView;

/**
 * Controller of the select data tab.
 * 
 * @author CM
 *
 */
public class SelectDataTab2Controller {

	/** The table view for the selected data. **/
	@FXML
	private TableView<ObservableList<StringProperty>> dataTable;

	/** To select load data from file system or generate test data. **/
	@FXML
	private RadioButton generateRButton, loadRButton;

	/** Toggle group for load and generate data radio buttons. **/
	@FXML
	private ToggleGroup groupPreprocessing;

	/** The tree view to select data from file system. **/
	@FXML
	private TreeView<File> locationTreeView;

	/** Continue workflow. **/
	@FXML
	private Button nextButton;

	/** Enable to hide selected data. **/
	@FXML
	private CheckBox hideDataCheckBox;

	/** Contains the selected data records. **/
	private DynamicTableViewSingleton dynamicTableView;

	/** Represented the file system with the containing data records. **/
	private FileTreeView treeView;

	/** Path of the file system. */
	private String path;

	/** Tab pane this tab is included. */
	private TabPane tabPane;

	/** Should the data hide? */
	private boolean hideDataSelected;

	/** Contains the settings the user set for the tab */
	private SelectDataSettings dataSettings;

	/**
	 * True if the user continue the workflow with the same data as the last
	 * session.
	 */
	private boolean sameData;

	/*
	 * ------------------------------ Initialize Controller
	 * ------------------------------
	 */

	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	private void initialize() throws IOException {

		sameData = false;
		dynamicTableView = DynamicTableViewSingleton.getInstance();
		treeView = new FileTreeView();

		// File directory to csv files.
		File currentDir = new File(FilePathConstants.DATA_FILE_PATH);

		// initialize tree view
		locationTreeView.setRoot(treeView.createTreeView(currentDir, null));
		treeView.setCellFactory(locationTreeView);
		locationTreeView.setDisable(true);

		nextButton.setDisable(true);

		loadSavedSettings();

		/* Event handling */

		loadRButton.setOnAction(this::handleLoadButtonAction);
		generateRButton.setOnAction(this::handleGenerateButtonAction);
		hideDataCheckBox.setOnAction(this::handleHideData);

		/*
		 * For double clicking file in path, the containing data will be load and show
		 * in table view. The 'next' button will be enable as well.
		 */
		locationTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2) {

					TreeItem<File> file = locationTreeView.getSelectionModel().getSelectedItem();
					StringBuilder pathBuilder = new StringBuilder();
					pathBuilder.insert(0, file.getValue());
					path = pathBuilder.toString();
					dynamicTableView.setPath(path);
					if (!hideDataCheckBox.isSelected()) {
						dynamicTableView.createAndInsertCSVData(dataTable);
					} else {
						dynamicTableView.hideTableDate(dataTable);
					}
					nextButton.setDisable(false);
				}
			}
		});
	}

	/*
	 * ------------------------------ Event handling ------------------------------
	 */

	/**
	 * Handling for load data from file system.
	 * 
	 * @param event
	 *            Action event
	 */
	@FXML
	private void handleLoadButtonAction(ActionEvent event) {
		locationTreeView.setDisable(false);
	}

	/**
	 * Handling for generate data. NOTE: Function not implemented yet!
	 * 
	 * @param event
	 *            Action event
	 */
	@FXML
	private void handleGenerateButtonAction(ActionEvent event) {
		locationTreeView.setDisable(true);
	}

	/** hide the data if the check box is selected. */
	@FXML
	private void handleHideData(ActionEvent event) {

		// boolean selected
		hideDataSelected = hideDataCheckBox.isSelected();
		if (path != null) {
			if (!hideDataSelected) {
				dynamicTableView.insertCSVData(dataTable);
			} else {
				dynamicTableView.hideTableDate(dataTable);
			}
		}
	}

	/**
	 * When the "Next" button is clicked, the tab pane switch to the data cleaning
	 * tab. The selected settings will be saved.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleButtonClick(ActionEvent event) {
		if (dataSettings == null) {
			dataSettings = new SelectDataSettings();
		}
		if (dataSettings.getPath() != null) {
			if (path == dataSettings.getPath()) {
				sameData = true;
			}
		}
		dataSettings.setPath(path);
		dataSettings.setLoad(loadRButton.isSelected());
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(1);
	}

	/* ------------------------------ Settings ------------------------------ */

	/**
	 * Load settings from previous session if they are exist.
	 */
	public void loadSavedSettings() {

		if (dataSettings != null) {
			loadRButton.setSelected(dataSettings.isLoad());
			dynamicTableView.setPath(dataSettings.getPath());
		}
	}

	/**
	 * If the selected file is deviant from the actually saved -> update the
	 * settings.
	 */
	public void updateSettings() {
		if (dataSettings != null) {
			path = dataSettings.getPath();
			loadRButton.setSelected(dataSettings.isLoad());
			dynamicTableView.setPath(dataSettings.getPath());
			dynamicTableView.createAndInsertCSVData(dataTable);
			locationTreeView.setDisable(false);
			nextButton.setDisable(false);
		}
	}

	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */

	/**
	 * Set the tab pane.
	 * @param tabPane
	 */
	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	/**
	 * Is hide data selected?
	 * @return True, if hide data is selected.
	 */
	public boolean isHideDataSelected() {
		return hideDataSelected;
	}

	/**
	 * Get the path of the file system.
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the path of the file system.
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Set the select data settings.
	 * @param settings
	 */
	public void setSettings(SelectDataSettings settings) {
		this.dataSettings = settings;
	}

	/**
	 * Get the settings for select data.
	 * @return the settings
	 */
	public SelectDataSettings getSettings() {
		return dataSettings;
	}

	/**
	 * Is the data the same like in the previous workflow?
	 * @return True, if the data is the same.
	 */
	public boolean isSameData() {
		return sameData;
	}
}
