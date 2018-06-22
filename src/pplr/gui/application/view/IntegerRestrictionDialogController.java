package pplr.gui.application.view;

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
import javafx.stage.Stage;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.model.IntegerPropertySettings;
import pplr.gui.application.model.PropertySettingsSingleton;
import pplr.gui.application.tools.ControllerItemsRestriction;
import pplr.gui.application.tools.DynamicTableViewSingleton;

/**
 * Controller of the integer restriction dialog.
 * 
 * @author CM
 *
 */
public class IntegerRestrictionDialogController {

	/** Defines the text fields of the tab. */
	@FXML
	private TextField minimum, maximum;

	/** Defines the button of the tab. */
	@FXML
	private Button confirmButton, cancelButton;

	/** Defines the check box of the tab. */
	@FXML
	private CheckBox negativInteger;

	/** Stage of the tab. */
	private Stage intDialogStage;

	/** Is every entry valid? */
	private boolean confirmClick = false;

	/** Name of the attribute */
	private String attributName;

	/** Property settings that contains all settings. */
	private PropertySettingsSingleton settings;

	/** Minimum/maximum value of an integer. */
	private Integer min, max;

	/** Alow negative integer? */
	private boolean negativ;

	/** The settings for the attribute. */
	private IntegerPropertySettings integerSettings;

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
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
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

		ControllerItemsRestriction.restrictTextFieldOnInteger(minimum);
		ControllerItemsRestriction.restrictTextFieldOnInteger(maximum);
	}

	/**
	 * Set stage of the dialog.
	 * 
	 * @param stringDialogStage
	 */
	public void setDialogStage(Stage intDialogStage) {
		this.intDialogStage = intDialogStage;
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
			intDialogStage.close();
		}
	}

	/** 
	 * Check if entries are valid before saving data.
	 * @return True, if the entries are valid
	 */
	private boolean isInputValid() {

		String errorMessage = "";
		try {
			for (HashMap<String, IntegerPropertySettings> map : settings) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().equals(attributName)) {
						pair.setValue(saveSettings());
					}
				}
			}
		} catch (NumberFormatException e) {
			errorMessage = AlertConstants.NOT_ALLOWED_ENTRY;
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(intDialogStage);
			alert.setTitle(AlertConstants.INVALID_ENTRY);
			alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	/**
	 * Handling for clicking the cancel button.
	 */
	@FXML
	public void handleCancel() {
		intDialogStage.close();
	}

	/**
	 * Set the name of the attribute.
	 * @param attributName
	 */
	public void setAttributName(String attributName) {
		this.attributName = attributName;
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

					temp = refreshMaxInt(temp);
					temp = refreshMinInt(temp);
					temp = refreshNegativInt(temp);

					list.set(columnIndex, temp);
				}
				data.set(j, list);
			}
		}

		dynamicTableView.update(tableView, data);
	}

	/**
	 * Check every entry for the integer is not bigger than the set maximum. If the
	 * value is bigger it will be deleted.
	 * 
	 * @param value
	 *            that should be checked for the maximum
	 * @return the value or an empty string if the int value was bigger than the
	 *         maximum
	 */
	public String refreshMaxInt(String value) {

		if (max != null) {
			try {
				int maxValue = Integer.parseInt(value);
				if (!(maxValue <= this.max))
					value = "";
			} catch (NumberFormatException e) {
				numberException = true;
			}
		}
		return value;
	}

	/**
	 * Check every entry for the integer is not smaller than the set minimum. If the
	 * value is smaller it will be deleted.
	 * 
	 * @param value
	 *            that should be checked for the minimum
	 * @return the value or an empty string if the int value was smaller than the
	 *         minimum
	 */
	public String refreshMinInt(String value) {

		if (min != null) {
			try {
				int minValue = Integer.parseInt(value);
				if (minValue < this.min)
					value = "";
			} catch (NumberFormatException e) {
				numberException = true;
			}
		}
		return value;
	}

	/**
	 * If negative integer wasn't allowed, this will remove all negatives from the
	 * entries.
	 * 
	 * @param value
	 *            that should be checked for negatives
	 * @return the value or an empty string if negatives not allowed and the value
	 *         was negative.
	 */
	public String refreshNegativInt(String value) {

		if (!negativ) {
			try {
				int intValue = Integer.parseInt(value);
				if (intValue < 0)
					value = "";
			} catch (NumberFormatException e) {
				numberException = true;
			}
		}
		return value;
	}

	/**
	 * Thrown if the entered value was not an integer.
	 */
	public void showNumberFormatException() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(intDialogStage);
		alert.setTitle(AlertConstants.NUMBER_FORMAT_EXCEPTION);
		alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
		alert.setContentText(AlertConstants.NUMBER_FORMAT_EXCEPTION_DESCRIPTION);
		alert.showAndWait();
	}

	public void loadSettingsToGui(String attributName) {
		settings = PropertySettingsSingleton.getInstance();

		IntegerPropertySettings intSettings = null;
		settings = PropertySettingsSingleton.getInstance();
		boolean check = false;
		for (HashMap<String, IntegerPropertySettings> hm : settings) {
			for (String s : hm.keySet()) {
				if (s.equals(attributName)) {
					intSettings = (IntegerPropertySettings) hm.get(s);
					check = true;
					break;
				}
			}
			if (check) {
				break;
			}
		}
		if (!check) {
			intSettings = new IntegerPropertySettings();
		}

		if (intSettings.getMinimum() != null) {
			minimum.appendText(intSettings.getMinimum().toString());
		}
		if (intSettings.getMaximum() != null) {
			maximum.appendText(intSettings.getMaximum().toString());
		}

		negativInteger.setSelected(intSettings.isNegativInt());
	}

	/**
	 * Save the settings for the attribute.
	 * @return the setting object
	 */
	public IntegerPropertySettings saveSettings() {

		if (!minimum.getText().equals("")) {
			min = Integer.valueOf(minimum.getText().trim());
		} else {
			min = null;
		}
		if (!maximum.getText().equals("")) {
			max = Integer.valueOf(maximum.getText().trim());
		} else {
			max = null;
		}
		negativ = negativInteger.isSelected();

		integerSettings = new IntegerPropertySettings(min, max, negativ);
		return integerSettings;
	}

	/**
	 * Set the table view.
	 * @param tableView
	 */
	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}
}
