package pplr.gui.application.view;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import pplr.gui.application.Main;
import pplr.gui.application.settings.UserSettingsWrapper;
import pplr.gui.application.tools.DynamicTableViewSingleton;
import pplr.gui.application.tools.ImportCSV;

/**
 * Controller of the tab layout.
 * 
 * @author CM
 *
 */
public class TabLayoutController {

	/* ------------------- Stage definitions. ------------------- */

	/** Primary stage. */
	protected Stage primaryStage;
	
	/** The stage. */
	protected Stage stage;

	/* ------------------- References to the fxml elements. ------------------- */

	/** The tab pane that includes all tabs. **/
	@FXML
	private TabPane tabPane;
	
	/** The controller for the select data tab. **/
	@FXML
	private SelectDataTab2Controller selectTabController;
	
	/** The controller for the data cleaning tab. **/
	@FXML
	private DataCleaningController dataCleaningTabController;
	
	/** The controller for the standardization tab. **/
	@FXML
	private StandardizationController standardizationTabController;
	
	/** The controller for the encoding tab. **/
	@FXML
	private CodierungController codierungTabController;
	
	/** The controller for the finish tab. **/
	@FXML
	private FinishTabController finishTabController;

	/* ------------------- ------------------- */

	/** The tabs for each controller. **/
	private Tab selectTab, dataCleaningTab, standardizationTab, codierungTab, finishTab;

	/** Contains the selected data records for the table view. */
	private DynamicTableViewSingleton dynamicTable;

	/** All tabs in the tab pane. */
	private ObservableList<Tab> allTabs;

	/** Importer for csv files. **/
	private ImportCSV importer;

	/** Main class **/
	private Main main;

	/** All user settings. */
	private UserSettingsWrapper wrapper;

	/**
	 * Constructor
	 */
	public TabLayoutController() {

	}

	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	private void initialize() {

		selectTab = tabPane.getTabs().get(0);
		dataCleaningTab = tabPane.getTabs().get(1);
		standardizationTab = tabPane.getTabs().get(2);
		codierungTab = tabPane.getTabs().get(3);
		finishTab = tabPane.getTabs().get(4);

		// Set default tab to selectTabController
		selectTabController.setTabPane(tabPane);

		// Disable follow tabs until the data is selected.
		disableTabs(selectTab);

		tabPane.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {

					if (newValue == selectTab) {
						disableTabs(selectTab);
					} else if (newValue == dataCleaningTab) {
						tabControllsDataCleaning();
					} else if (newValue == standardizationTab) {
						tabStandartization();
					} else if (newValue == codierungTab) {
						tabEncoding();
					} else if (newValue == finishTab) {
						tabFinish();
					} else {

					}
				});
	}

	/**
	 * Contains the controls that should be done if the data cleaning tab is
	 * selected.
	 */
	public void tabControllsDataCleaning() {

		disableTabs(dataCleaningTab);
		dataCleaningTabController.setTabPane(tabPane);
		dataCleaningTabController.setMain(main);
		dataCleaningTabController.loadTableView(selectTabController);
		dataCleaningTabController.refreshAttributeCombo();
		if (selectTabController.isSameData()) {
			if (wrapper != null)
				dataCleaningTabController.setSettings(wrapper.getDataCleaningSettings());

		}
		dataCleaningTabController.setAttributeValues();
		dataCleaningTabController.setTypeValues();
	}

	/**
	 * Contains the controls that should be done if the standartization tab is
	 * selected.
	 */
	public void tabStandartization() {

		// standardizationTabController.setDataCleaningType(dataCleaningTabController.getSettings().getTypes());
		disableTabs(standardizationTab);
		standardizationTabController.createLayout();
		if (wrapper != null)
			standardizationTabController.setSettings(wrapper.getStandardizationSettings());
		standardizationTabController.setDCController(dataCleaningTabController);
		standardizationTabController.setDCSettings(dataCleaningTabController.getSettings());
		standardizationTabController.setTabPane(tabPane);
		standardizationTabController.setMain(main);
		standardizationTabController.refreshTypeLabel();
		standardizationTabController.loadTableView(selectTabController.isHideDataSelected());

		// if(standardizationTabController.getInit()) {
		standardizationTabController.refreshAttributLabel();
		standardizationTabController.setAttributeValuesFromSettings();

		// standardizationTabController.setInit(false);
	}

	/**
	 * Contains the controls that should be done if the encoding tab is selected.
	 */
	public void tabEncoding() {

		disableTabs(codierungTab);
		codierungTabController.setTabPane(tabPane);
		codierungTabController.insertAttributeLabel(standardizationTabController);
		codierungTabController.loadTableView(selectTabController.isHideDataSelected());
		codierungTabController.insertFunctionComboBox();
		// settings
		try {
			codierungTabController.setSettings(wrapper.getCodierungSettings());
			codierungTabController.setBloomLengthToTab();
			codierungTabController.setFunctionsToTab();
			codierungTabController.setHashingToTab();
			codierungTabController.setK();
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Contains the controls that should be done if the finish tab is selected.
	 */
	public void tabFinish() {

		disableTabs(finishTab);
		finishTabController.setMain(main);
		finishTabController.setTabPane(tabPane);
		finishTabController.refreshTableView(dataCleaningTabController, selectTabController.isHideDataSelected());
	}

	/**
	 * Invoke the selected tab and disable all other tabs.
	 * 
	 * @param currentTab
	 *            the selected tab
	 */
	public void disableTabs(Tab currentTab) {
		currentTab.setDisable(false);
		for (Tab tab : tabPane.getTabs()) {
			if (tab != currentTab) {
				tab.setDisable(true);
			}
		}
	}

	/**
	 * Getter for tabPane
	 * 
	 * @return the tab pane for all tabs
	 */
	public TabPane getTabPane() {
		return tabPane;
	}

	/**
	 * Getter for dataCleaningTab.
	 * 
	 * @return the tab for data cleaning.
	 */
	public Tab getDataCleaningTab() {
		return dataCleaningTab;
	}

	/**
	 * Set the main.
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;
	}

	/**
	 * Get the main.
	 * @return
	 */
	public Main getMain() {
		return main;
	}

	/**
	 * Get the select data controller.
	 * @return select data controller
	 */
	public SelectDataTab2Controller getSelectTabController() {
		return selectTabController;
	}

	/**
	 * Get the data cleaning controller.
	 * @return data cleaning controller
	 */
	public DataCleaningController getDataCleaningController() {
		return dataCleaningTabController;
	}

	/**
	 * Get the standardization controller.
	 * @return the standardization controller
	 */
	public StandardizationController getStandardizationController() {
		return standardizationTabController;
	}

	/**
	 * Get the encoding controller.
	 * @return the encoding controller
	 */
	public CodierungController getCodierungController() {
		return codierungTabController;
	}

	/**
	 * Set the user data wrapper.
	 * @param wrapper
	 */
	public void setWrapper(UserSettingsWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
