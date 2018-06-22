package pplr.gui.application.view;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import pplr.gui.application.Main;
import pplr.gui.application.configuration.FilePathConstants;
import pplr.gui.application.settings.UserSettingsWrapper;
import pplr.gui.application.tools.DynamicTableViewSingleton;
import pplr.gui.application.tools.ExportCSV;

/**
 * Controller of the finish tab.
 * 
 * @author CM
 *
 */
public class FinishTabController {

	/** Defines the table view of the tab. */
	@FXML
	private TableView dataTableFinish;
	
	/** Defines the buttons of the tab. */
	@FXML
	private Button saveSettings, saveData, commitData, backButton;
	
	/** Defines the radio buttons of the tab. */
	@FXML
	private RadioButton local, cluster;
	
	/** Defines the toggle group to the radio buttons. */
	@FXML
	private ToggleGroup groupExeclute;
	
	/** Contains the selected data records for the table view. */
	private DynamicTableViewSingleton dynamicTableView;
	
	/** Main of the application */
	private Main main;
	
	/** Tab pane the tab is contained. */
	private TabPane tabPane;
	
	/** All user settings */
	private UserSettingsWrapper settingsWrapper;
	
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
		saveData.setOnAction(this::handleSaveDataButton);
		saveSettings.setOnAction(this::handleSaveSettings);
		backButton.setOnAction(this::handleBackButton);
	}
	
	/**
	 * Insert user data from the selected file into the tableView.
	 * 
	 * @param selectDataTab
	 *            Tab in wich the user select the file.
	 */
	public void refreshTableView(DataCleaningController dataCleaning, boolean hideTableView) {

		if (dynamicTableView.getPath() != null) {
			if(!hideTableView) {
				dynamicTableView.update(dataTableFinish, dynamicTableView.getData());
			}
		}
	}
	
	/**
	 * Handling for clicking the save button of the tab.
	 * @param event Action event
	 */
	@FXML
	private void handleSaveDataButton(ActionEvent event) {
		String path = main.showSaveDataDialog();
		
		try {
			ExportCSV.exportData(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * By clicking on the save button the settings will be saved in a xml file.
	 * @param event Action event
	 */
	@FXML
	private void handleSaveSettings(ActionEvent event) {
		File settingsFile = main.getFilePath();
        if (settingsFile != null) {
            main.saveDataToFile(settingsFile);
        } else {
            File f = new File(FilePathConstants.SETTINGS_FILE);
            f.getParentFile().mkdir();
            try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	main.setFilePath(f);
        	main.saveDataToFile(f);
        }
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
		selectionModel.select(3);
	}
	
	/**
	 * Set the main of the application.
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;
	}

	/**
	 * Set the tab pane of the tab.
	 * @param tabPane
	 */
	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}
	
	/**
	 * Set the settings.
	 * @param wrapper
	 */
	public void setSettingsWrapper(UserSettingsWrapper wrapper) {
		this.settingsWrapper = wrapper;
	}
}
