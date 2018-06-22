package pplr.gui.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.configuration.LabelConstants;
import pplr.gui.application.model.ConfigurationObjectSingleton;
import pplr.gui.application.settings.UserSettingsWrapper;
import pplr.gui.application.view.ConnectAttributDialogController;
import pplr.gui.application.view.DateRestrictionDialogController;
import pplr.gui.application.view.IntegerRestrictionDialogController;
import pplr.gui.application.view.SplitAttributDialogController;
import pplr.gui.application.view.StringRestrictionDialogController;
import pplr.gui.application.view.TabLayoutController;

/**
 * Main class of an application for the pre-processing of a PPRL-Workflow.
 * Initialize the GUI elements and handle file loading and saving.
 * @author CM
 *
 */
public class Main extends Application {

	/**
	 * Contains the data set of the table view. The outer observable list contains
	 * each row and the inner observable list contains every column for each row.
	 */
	private ObservableList<ObservableList<StringProperty>> tableView = FXCollections.observableArrayList();

	/** Defines the primary stage of the application. */
	private Stage primaryStage;

	/** The root layout of the application. */
	private BorderPane rootLayout;

	/** The main layout that contains all tabs. */
	private TabLayoutController controller;

	/**
	 * List of all necessary attribute names the admin defines before starting the
	 * workflow.
	 */
	private ObservableList<ConfigurationObjectSingleton> configData = FXCollections.observableArrayList();

	/**
	 * Constructor
	 */
	public Main() {

	}
	
	/**
	 * Start the application. Build the layout and initialize it.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(LabelConstants.PPRL_WORKFLOW_HEADER);

		initRootLayout();

		showTabOverview();
	}
	
	/* ------------------------------ Building Layout ------------------------------ */

	/**
	 * Build the layout that contains all tabs with functions for the application.
	 */
	private void showTabOverview() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/TabLayout.fxml"));
		try {
			AnchorPane tabPane = (AnchorPane) loader.load();
			rootLayout.setCenter(tabPane);

			// Give controller access to main TabLayoutController
			controller = loader.getController();
			controller.setMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = getFilePath();
		if (file != null) {
			loadDataFromFile(file);
		}
	}

	/**
	 * Initialize the main layout that contains every container of the application.
	 * Build a border pane and insert the menu bar.
	 */
	private void initRootLayout() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
		try {
			rootLayout = (BorderPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Build the string restriction dialog.
	 * 
	 * @param attributName
	 *            name of the attribute to be restricted.
	 * @param tableView
	 *            that contains data.
	 * @return True, if the restrict was successful.
	 */
	public boolean showStringRestrictionDialog(String attributName, TableView tableView) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/StringRestrictionDialog.fxml"));
		try {
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(LabelConstants.RESTRICTION);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			StringRestrictionDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			controller.setAttributName(attributName);
			controller.loadSettingsToGui(attributName);
			controller.setTableView(tableView);

			// Show dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isConfirmClick();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Build the integer restriction dialog.
	 * 
	 * @param attributName
	 *            name of the attribute to be restricted.
	 * @param tableView
	 *            that contains data.
	 * @return True, if the restrict was successful.
	 */
	public boolean showIntegerRestrictionDialog(String attributName, TableView tableView) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/IntegerRestrictionDIalog.fxml"));
		try {
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(LabelConstants.RESTRICTION);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			IntegerRestrictionDialogController intController = loader.getController();
			intController.setDialogStage(dialogStage);

			intController.setAttributName(attributName);
			intController.loadSettingsToGui(attributName);
			intController.setTableView(tableView);

			// Show dialog and wait until the user closes it
			dialogStage.showAndWait();

			return intController.isConfirmClick();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Build the date restriction dialog.
	 * 
	 * @param attributName
	 *            name of the attribute to be restricted.
	 * @param formatTyp
	 *            the format ot the date value
	 * @param tableView
	 *            that contains data.
	 * @return True, if the restrict was successful.
	 */
	public boolean showDateRestrictionDialog(String attributName, String formatTyp, TableView tableView) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/DateRestrictionDialog.fxml"));
		try {
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(LabelConstants.RESTRICTION);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			DateRestrictionDialogController dateController = loader.getController();
			dateController.setDialogStage(dialogStage);

			dateController.setAttributName(attributName);
			dateController.setFormatType(formatTyp);
			dateController.loadSettingsToGui(attributName);
			dateController.setTableView(tableView);

			// Show dialog and wait until the user closes it
			dialogStage.showAndWait();

			return dateController.isConfirmClick();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Build the dialog for splitting an attribute.
	 * 
	 * @param attributNames
	 *            Name of the attribute that should be split.
	 * @param tableView
	 *            that contains the data.
	 * @return True, if the splitting was successful.
	 */
	public boolean showSplitAttributDialog(ArrayList<String> attributNames, TableView tableView) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/SplitAttributDialog2.fxml"));
		try {
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(LabelConstants.SPLIT_ATTRIBUTES);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			SplitAttributDialogController splitController = loader.getController();
			// splitController.setTableView(dynamicTableView);
			splitController.setDialogStage(dialogStage);

			splitController.setChooseAttributeValues(attributNames);
			// Show dialog and wait until the user closes it
			dialogStage.showAndWait();

			return splitController.isConfirmClick();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Build the dialog to connect attributes.
	 * 
	 * @param attributNames
	 *            List that contains all names of the attribute that should be
	 *            connect.
	 * @param tableView
	 *            that conatins all data.
	 * @return True, if the connecting was successful.
	 */
	public boolean showConnectAttributDialog(ArrayList<String> attributNames, TableView tableView) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ConnectAttributDialog.fxml"));
		try {
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(LabelConstants.CONNECT_ATTRIBUTES);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			ConnectAttributDialogController connectController = loader.getController();
			connectController.setDialogStage(dialogStage);

			connectController.setChooseAttributeValues(attributNames);
			// Show dialog and wait until the user closes it
			dialogStage.showAndWait();

			return connectController.isConfirmClick();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Opens a file chooser dialog to select the path to save the data.
	 * 
	 * @return The path in the local file system
	 */
	public String showSaveDataDialog() {
		Stage dialogStage = new Stage();
		dialogStage.setTitle(LabelConstants.SAVE_DATA);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(LabelConstants.SAVE_DATA);
		File file = fileChooser.showSaveDialog(dialogStage);
		if (file != null) {
			if (file.getAbsolutePath().endsWith(LabelConstants.CSV_FILE_ENDING)) {
				return file.getAbsolutePath();
			} else {
				return file.getAbsolutePath() + LabelConstants.CSV_FILE_ENDING;
			}
		}
		return null;
	}
	
	/* ------------------------------ Save & Load ------------------------------ */

	/**
	 * Loads user selected data from the specified file.
	 * 
	 * @param file
	 *            Contains the data.
	 */
	public void loadDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(UserSettingsWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			UserSettingsWrapper wrapper = (UserSettingsWrapper) um.unmarshal(file);
			controller.setWrapper(wrapper);
			controller.getSelectTabController().setSettings(wrapper.getSelectSettings());
			controller.getSelectTabController().updateSettings();
			// Save the file path to the registry.
			setFilePath(file);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(AlertConstants.ERROR);
			alert.setHeaderText(AlertConstants.LOAD_EXCEPTION_HEADER);
			alert.setContentText(AlertConstants.LOAD_EXCEPTION + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current user selected data to the specified file.
	 * 
	 * @param file
	 *            Contains the data.
	 */
	public void saveDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(UserSettingsWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			UserSettingsWrapper wrapper = new UserSettingsWrapper();
			wrapper.setSelectSettings(controller.getSelectTabController().getSettings());
			wrapper.setDataCleaningSettings(controller.getDataCleaningController().getSettings());
			wrapper.setStandardizationSettings(controller.getStandardizationController().getSettings());
			wrapper.setCodierungSettings(controller.getCodierungController().getSettings());

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);

			// Save the file path to the registry.
			setFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(AlertConstants.ERROR);
			alert.setHeaderText(AlertConstants.SAVE_EXCEPTION_HEDER);
			alert.setContentText(AlertConstants.SAVE_EXCEPTION + file.getPath());
			alert.showAndWait();
		}
	}
	
	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Get the file path of a file.
	 * 
	 * @return the file path
	 */
	public File getFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get(LabelConstants.FILEPATH, null);
		if (filePath != null) {
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		} else {
			return null;
		}
	}

	/**
	 * Set the file path to a file.
	 * 
	 * @param file
	 *            to set the path
	 */
	public void setFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put(LabelConstants.FILEPATH, file.getPath());
		} else {
			prefs.remove(LabelConstants.FILEPATH);
		}
	}

	/**
	 * Returns the data as an observable list
	 * 
	 * @return an observable list containing the content of the table view.
	 */
	public ObservableList<ObservableList<StringProperty>> getTableView() {
		return tableView;
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return the stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/* ------------------------------ Main ------------------------------ */

	/**
	 * Main 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
