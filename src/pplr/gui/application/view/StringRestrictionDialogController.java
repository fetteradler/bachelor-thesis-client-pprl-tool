package pplr.gui.application.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.configuration.LayoutConstants;
import pplr.gui.application.model.PropertySettingsSingleton;
import pplr.gui.application.model.StringPropertySettings;
import pplr.gui.application.tools.ControllerItemsRestriction;
import pplr.gui.application.tools.DynamicTableViewSingleton;

/**
 * Controller of the integer restriction dialog.
 * 
 * @author CM
 *
 */
public class StringRestrictionDialogController {

	/** Defines the text fields of the tab. */
	@FXML
	private TextField maxLength, minLength, allowedChar, illegalChar, alternativChar;

	/** Defines the check boxes of the tab. */
	@FXML
	private CheckBox removeSpaces, removeWhitespaces;

	/** Defines the buttons of the tab. */
	@FXML
	private Button addAllowedChar, addIllegalChar, removeAllowedChar, removeIllegalChar, confirmButton, cancelButton;

	/** Defines the anchor pane of the tab. */
	@FXML
	private AnchorPane charPane;

	/** Text fields for allowed/illegal/alternative strings. */
	private List<TextField> addAllowField, addIllegalField, addAlternativField;

	/** Stage of the tab. */
	private Stage stringDialogStage;

	/** Is every entry valid? */
	private boolean confirmClick = false;

	/** Counter for allowed and illegal strings. */
	private int countAllowField, countIllegalField;

	/** Layout configurations. */
	private double countAllowFieldLayoutY, countIllegalFieldLayoutY;

	/** String property settings. */
	private StringPropertySettings stringSettings;

	/** Maximum/minimum of the string length. */
	private String maxStringLength, minStringLength;

	/** Allow any whitespace or invisible chars? */
	private boolean spaces, whitespaces;

	/** List of all allowed/illegal/alternative strings. */
	private ArrayList<String> allowedChars, illegalChars, alternativChars;

	/** Property settings that contains all settings. */
	private PropertySettingsSingleton settings;

	/** Name of the attribute. */
	private String attributName;

	/** Contains the selected data records for the table view. */
	private DynamicTableViewSingleton dynamicTableView;

	/** Table view with user data. */
	private TableView tableView;

	/** Did errors occur? */
	private boolean numberException;
	
	/*
	 * ------------------------------ Initialize controller
	 * ------------------------------
	 */

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		dynamicTableView = DynamicTableViewSingleton.getInstance();
		numberException = false;

		confirmButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleConfirm();
			}
		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleCancel();
			}
		});

		ControllerItemsRestriction.restrictTextFieldOnInteger(maxLength);
		ControllerItemsRestriction.restrictTextFieldOnInteger(minLength);
		ControllerItemsRestriction.restrictTextFieldOnOneLetter(illegalChar);

		addAllowedChar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleAddAllowedChar(countAllowField);
			}
		});

		addIllegalChar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleAddIllegalChar(countIllegalField);
			}
		});

		removeAllowedChar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleRemoveAllowedChar(countAllowField);
			}
		});

		removeIllegalChar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleRemoveIllegalChar(countIllegalField);
			}
		});
	}

	/**
	 * Set stage of the dialog.
	 * 
	 * @param stringDialogStage
	 */
	public void setDialogStage(Stage stringDialogStage) {
		this.stringDialogStage = stringDialogStage;
	}

	/**
	 * Confirm was successful.
	 * 
	 * @return true if confirm
	 */
	public boolean isConfirmClick() {
		return confirmClick;
	}

	/** 
	 * Handling for clicking the confirm button.
	 */
	@FXML
	public void handleConfirm() {
		if (isInputValid()) {

			confirmClick = true;
			refreshTableView();
			if (numberException)
				showNumberFormatException();
			stringDialogStage.close();
		}
	}

	/**
	 * Handling for clicking the cancel button.
	 */
	@FXML
	public void handleCancel() {
		stringDialogStage.close();
	}

	/**
	 * Check if the input in the dialog is valid.
	 * 
	 * @return true if input is valid
	 */
	private boolean isInputValid() {

		String errorMessage = "";
		try {
			for (HashMap<String, StringPropertySettings> map : settings) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().equals(attributName)) {
						pair.setValue(saveSettings());
						break;
					}
				}
			}
		} catch (NumberFormatException e) {
			errorMessage = AlertConstants.NUMBER_FORMAT_EXCEPTION;
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(stringDialogStage);
			alert.setTitle(AlertConstants.INVALID_ENTRY);
			alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	/**
	 * Add textfields for allowed chars.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleAddAllowedChar(int count) {

		addAllowField.add(new TextField());
		charPane.getChildren().add(addAllowField.get(count));
		addAllowField.get(count).setLayoutX(LayoutConstants.SR_ADD_ALLOW_LAYOUT_X);
		addAllowField.get(count).setLayoutY(countAllowFieldLayoutY);
		addAllowField.get(count).setPrefWidth(LayoutConstants.SR_ADD_ALLOW_PREF_WIDTH);

		if (count > 3) {
			charPane.setPrefHeight(charPane.getPrefHeight() + LayoutConstants.SR_PREF_HEIGHT_MODIFIER);
		}

		countAllowFieldLayoutY = countAllowFieldLayoutY + LayoutConstants.SR_PREF_HEIGHT_MODIFIER;

		countAllowField++;
	}

	/**
	 * Add textfields for illegal chars.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleAddIllegalChar(int count) {

		addIllegalField.add(new TextField());
		charPane.getChildren().add(addIllegalField.get(count));
		addIllegalField.get(count).setLayoutX(455.0);
		addIllegalField.get(count).setLayoutY(countIllegalFieldLayoutY);
		addIllegalField.get(count).setPrefWidth(56.0);

		addAlternativField.add(new TextField());
		charPane.getChildren().add(addAlternativField.get(count));
		addAlternativField.get(count).setLayoutX(632.0);
		addAlternativField.get(count).setLayoutY(countIllegalFieldLayoutY);
		addAlternativField.get(count).setPrefWidth(56.0);

		if (count > 3) {
			charPane.setPrefHeight(charPane.getPrefHeight() + LayoutConstants.SR_PREF_HEIGHT_MODIFIER);
		}

		countIllegalFieldLayoutY = countIllegalFieldLayoutY + LayoutConstants.SR_PREF_HEIGHT_MODIFIER;

		ControllerItemsRestriction.restrictTextFieldOnOneLetter(addIllegalField.get(count));
		ControllerItemsRestriction.restrictTextFieldOnOneLetter(addAlternativField.get(count));

		countIllegalField++;
	}

	/**
	 * Delete textfields for allowed chars.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleRemoveAllowedChar(int count) {

		if (countAllowField > 0) {
			count--;
			charPane.getChildren().remove(addAllowField.get(count));

			if (count > 3) {
				charPane.setPrefHeight(charPane.getPrefHeight() - LayoutConstants.SR_PREF_HEIGHT_MODIFIER);
			}

			countAllowFieldLayoutY = countAllowFieldLayoutY - LayoutConstants.SR_PREF_HEIGHT_MODIFIER;

			countAllowField--;
		}
	}

	/**
	 * Delete textfields for illegal chars
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleRemoveIllegalChar(int count) {

		if (countIllegalField > 0) {
			count--;
			charPane.getChildren().remove(addIllegalField.get(count));
			charPane.getChildren().remove(addAlternativField.get(count));

			if (count > 3) {
				charPane.setPrefHeight(charPane.getPrefHeight() - LayoutConstants.SR_PREF_HEIGHT_MODIFIER);
			}

			countIllegalFieldLayoutY = countIllegalFieldLayoutY - LayoutConstants.SR_PREF_HEIGHT_MODIFIER;

			ControllerItemsRestriction.restrictTextFieldOnOneLetter(addIllegalField.get(count));
			ControllerItemsRestriction.restrictTextFieldOnOneLetter(addAlternativField.get(count));

			countIllegalField--;
		}
	}

	/**
	 * Save the settings in the dialog to a string attribute.
	 * 
	 * @return settings to attribute
	 */
	public StringPropertySettings saveSettings() {

		maxStringLength = maxLength.getText();
		minStringLength = minLength.getText();

		spaces = removeSpaces.isSelected();
		whitespaces = removeWhitespaces.isSelected();

		allowedChars = new ArrayList<String>();
		illegalChars = new ArrayList<String>();
		alternativChars = new ArrayList<String>();

		allowedChars.add(allowedChar.getText());
		illegalChars.add(illegalChar.getText());
		alternativChars.add(alternativChar.getText());

		boolean allreadyIn = false;
		for (TextField field : addAllowField) {
			for (String c : allowedChars) {
				if (c.equals(field.getText())) {
					allreadyIn = true;
				}
			}
			if (!allreadyIn) {
				allowedChars.add(field.getText());
				allreadyIn = false;
			}
		}
		for (TextField field : addIllegalField) {
			for (String c : illegalChars) {
				if (c.equals(field.getText())) {
					allreadyIn = true;
				}
			}
			if (!allreadyIn) {
				illegalChars.add(field.getText());
				allreadyIn = false;
			}
		}
		for (TextField field : addAlternativField) {
			for (String c : alternativChars) {
				if (c.equals(field.getText())) {
					allreadyIn = true;
				}
			}
			if (!allreadyIn) {
				alternativChars.add(field.getText());
				allreadyIn = false;
			}
		}

		stringSettings = new StringPropertySettings(maxStringLength, minStringLength, spaces, whitespaces, allowedChars,
				illegalChars, alternativChars);

		return stringSettings;
	}

	/**
	 * Refresh the table view in standardization tab with the set restrictions.
	 */
	public void refreshTableView() {

		List<String> columnNames = dynamicTableView.getColumnNames();
		ObservableList<ObservableList<String>> data = dynamicTableView.getData();

		int columnIndex = -1;
		for (int i = 0; i < columnNames.size(); i++) {
			if (columnNames.get(i).equals(attributName)) {
				columnIndex = i;
				break;
			}
		}

		// refresh table view without illegal char
		if (columnIndex > -1) {
			for (int j = 0; j < data.size(); j++) {
				ObservableList<String> list = data.get(j); // all rows
				String temp = data.get(j).get(columnIndex); // attribute per row
				if (temp != null) {

					temp = refreshMaxLength(temp);
					temp = refreshMinLength(temp);
					temp = refreshSpaces(temp);
					temp = refreshWhiteSpaces(temp);
					temp = refreshAllowedString(temp);
					temp = refreshIllegalChars(temp);

					list.set(columnIndex, temp);
				}
				data.set(j, list);
			}
		}

		dynamicTableView.update(tableView, data);
	}

	/**
	 * Check every entry for the maximal length. If the value is longer it will be
	 * deleted.
	 * 
	 * @param value
	 *            that should be check for maximum length
	 * @return the value or an empty string if the value was bigger than the maximum
	 *         length
	 */
	public String refreshMaxLength(String value) {

		if (maxStringLength.length() > 0) {
			try {
				int max = Integer.parseInt(maxStringLength);
				if (value.length() > max) {
					value = "";
				}
			} catch (NumberFormatException e) {
				numberException = true;
			}
		}
		return value;
	}

	/**
	 * Check every entry for the minimal length. If the value is smaller it will be
	 * deleted.
	 * 
	 * @param value
	 *            that should be checked for minimum length
	 * @return the value or an empty string if the value was smaller than the
	 *         minimum length
	 */
	public String refreshMinLength(String value) {

		if (minStringLength.length() > 0) {
			try {
				int min = Integer.parseInt(minStringLength);
				if (value.length() < min) {
					value = "";
				}
			} catch (NumberFormatException e) {
				numberException = true;
			}
		}
		return value;
	}

	/**
	 * Remove all whitespace (" ") from an entry.
	 * 
	 * @param value
	 *            that should be edit
	 * @return the resulting value
	 */
	public String refreshSpaces(String value) {

		if (spaces) {
			value = value.replace(" ", "");
		}
		return value;
	}

	/**
	 * Remove all whitespace and non-visible characters (like \n) from an entry.
	 * 
	 * @param value
	 *            that should be edit
	 * @return the resulting value
	 */
	public String refreshWhiteSpaces(String value) {

		if (whitespaces) {
			value = value.replaceAll("\\s+", "");
		}
		return value;
	}

	/**
	 * Check if an entry is definie as an allowed string. If it is not, the entry
	 * will be removed.
	 * 
	 * @param value
	 *            that should be checked
	 * @return the allowed value or an empty string
	 */
	public String refreshAllowedString(String value) {

		if (allowedChars != null && allowedChars.get(0).length() > 0) {
			if (!allowedChars.contains(value)) {
				value = "";
			}
		}
		return value;
	}

	/**
	 * Replace the illegal chars for every entry. The char can either removed or
	 * replaced by an alternative char the user entered.
	 * 
	 * @param value
	 *            that should be checked for illegal chars
	 * @return the value with replaced chars
	 */
	public String refreshIllegalChars(String value) {

		for (int k = 0; k < illegalChars.size(); k++) {
			if (value.contains(illegalChars.get(k))) {
				if (k < alternativChars.size() && alternativChars.get(k) != null) {
					value = value.replace(illegalChars.get(k), alternativChars.get(k));
				} else {
					value = value.replace(illegalChars.get(k), "");
				}
			}
		}
		return value;
	}

	/**
	 * Thrown if the entered value was not an integer.
	 */
	public void showNumberFormatException() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stringDialogStage);
		alert.setTitle(AlertConstants.NUMBER_FORMAT_EXCEPTION);
		alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
		alert.setContentText(AlertConstants.NUMBER_FORMAT_EXCEPTION_DESCRIPTION);
		alert.showAndWait();
	}

	/**
	 * Load the settings for a string attribute to the dialog.
	 */
	public void loadSettingsToGui(String attributName) {
		settings = PropertySettingsSingleton.getInstance();

		StringPropertySettings stringSettings = null;
		boolean check = false;
		for (HashMap<String, StringPropertySettings> hm : settings) {
			for (String s : hm.keySet()) {
				if (s.equals(attributName)) {
					stringSettings = (StringPropertySettings) hm.get(s);
					check = true;
					break;
				}
			}
			if (check) {
				break;
			}
		}
		if (!check) {
			stringSettings = new StringPropertySettings();
		}

		// initial config for add textfields
		if (stringSettings.getAllowedChars() != null && stringSettings.getAllowedChars().size() > 0) {
			countAllowField = stringSettings.getAllowedChars().size();
		} else {
			countAllowField = 0;
		}
		if (stringSettings.getIllegalChars() != null && stringSettings.getIllegalChars().size() > 0) {
			countIllegalField = stringSettings.getIllegalChars().size();
		} else {
			countIllegalField = 0;
		}
		countAllowFieldLayoutY = LayoutConstants.SR_ALLOW_FIELD_LAYOUT_Y;
		countIllegalFieldLayoutY = LayoutConstants.SR_ILLEGAL_FIELD_LAYOUT_Y;
		addAllowField = new ArrayList<>();
		addIllegalField = new ArrayList<>();
		addAlternativField = new ArrayList<>();

		if (stringSettings.getMaxLength() != null) {
			maxLength.appendText(stringSettings.getMaxLength());
		}
		if (stringSettings.getMinLength() != null) {
			minLength.appendText(stringSettings.getMinLength());
		}
		if (stringSettings.getAllowedChars() != null) {
			if (stringSettings.getAllowedChars().size() > 0) {
				allowedChar.appendText(stringSettings.getAllowedChars().get(0));
				for (int i = 1; i < stringSettings.getAllowedChars().size(); i++) {
					handleAddAllowedChar(i - 1);
					addAllowField.get(i - 1).appendText(stringSettings.getAllowedChars().get(i));
				}
			}
		}
		if (stringSettings.getIllegalChars() != null) {
			if (stringSettings.getAllowedChars().size() > 0) {
				illegalChar.appendText(stringSettings.getIllegalChars().get(0));
				for (int i = 1; i < stringSettings.getIllegalChars().size(); i++) {
					handleAddIllegalChar(i - 1);
					addIllegalField.get(i - 1).appendText(stringSettings.getIllegalChars().get(i));
				}
			}
		}
		if (stringSettings.getAlternativChars() != null) {
			if (stringSettings.getAllowedChars().size() > 0) {
				alternativChar.appendText(stringSettings.getAlternativChars().get(0));
				for (int i = 1; i < stringSettings.getAlternativChars().size(); i++) {
					handleAddIllegalChar(i - 1);
					addAlternativField.get(i - 1).appendText(stringSettings.getAlternativChars().get(i));
				}
			}
		}
		removeSpaces.setSelected(stringSettings.isRemoveSpaces());
		removeWhitespaces.setSelected(stringSettings.isRemoveWhitespaces());
	}

	/**
	 * Set the name of the attribute from the property in the dialog.
	 * 
	 * @param attributName
	 *            name to the attribute
	 */
	public void setAttributName(String attributName) {
		this.attributName = attributName;
	}

	/**
	 * Get the string property settings.
	 * @return the settings
	 */
	public StringPropertySettings getStringSettings() {
		return stringSettings;
	}

	/**
	 * Setter for tableView
	 * 
	 * @param tableView
	 *            the table view of the standardization tab.
	 */
	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}
}
