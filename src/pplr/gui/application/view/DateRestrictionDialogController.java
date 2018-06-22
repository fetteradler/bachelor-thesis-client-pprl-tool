package pplr.gui.application.view;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.model.DatePropertySettings;
import pplr.gui.application.model.PropertySettingsSingleton;
import pplr.gui.application.tools.DynamicTableViewSingleton;

/**
 * Controller of the date restriction dialog.
 * 
 * @author CM
 *
 */
public class DateRestrictionDialogController {

	/** Defines the check box for the future date. */
	@FXML
	private CheckBox dateInFuture;

	/** Defines the data picker for the earliest and latest date. */
	@FXML
	private DatePicker earliestDate, latestDate;

	/** Defines the buttons for confirm and cancel. */
	@FXML
	private Button confirmButton, cancelButton;

	/** Stage of the dialog */
	private Stage dateDialogStage;

	/** Is every entry valid? */
	private boolean confirmClick = false;

	/** Name of the attribute/format */
	private String attributName, formatType;

	/** Property settings that contains all settings. */
	private PropertySettingsSingleton settings;

	/** Earliest/latest date. */
	private Date earliest, latest;

	/** Allow future date? */
	private boolean future;

	/** Date property settings. */
	private DatePropertySettings datePropertySettings;

	/** Contains the selected data records for the table view. */
	private DynamicTableViewSingleton dynamicTableView;

	/** Table view that contains the user data. */
	private TableView tableView;

	/** Did errors occur? */
	private boolean dateParseException, illegalArgumentException;

	/*
	 * ------------------------------ Initialize controller
	 * ------------------------------
	 */

	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	public void initialize() {

		dynamicTableView = DynamicTableViewSingleton.getInstance();
		dateParseException = false;
		illegalArgumentException = false;

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
	}

	/**
	 * Set stage of the dialog.
	 * 
	 * @param stringDialogStage
	 */
	public void setDialogStage(Stage dateDialogStage) {
		this.dateDialogStage = dateDialogStage;
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
			if (dateParseException) {
				showParseException();
			}
			if (illegalArgumentException) {
				showIllegalArgumentException();
			}
			dateDialogStage.close();
		}
	}

	/** 
	 * Check if entries are valid before saving data.
	 * @return True, if the entries are valid
	 */
	private boolean isInputValid() {

		String errorMessage = "";
		try {
			for (HashMap<String, DatePropertySettings> map : settings) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().equals(attributName)) {
						pair.setValue(saveSettings());
					}
				}
			}
		} catch (NumberFormatException e) {
			errorMessage = AlertConstants.INVALID_ENTRY;
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dateDialogStage);
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
		dateDialogStage.close();
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

					temp = refreshEarliestDate(temp);
					temp = refreshLatestDate(temp);
					temp = refreshFutureDate(temp);

					list.set(columnIndex, temp);
				}
				data.set(j, list);
			}
		}

		dynamicTableView.update(tableView, data);
	}

	/**
	 * Refresh the earliest date.
	 * @param value new earliest date.
	 * @return the new earliest date
	 */
	public String refreshEarliestDate(String value) {

		if (earliest != null) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatType, Locale.ENGLISH);
				LocalDate localDate = LocalDate.parse(value, formatter);
				Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				if (date.before(earliest)) {
					value = "";
				}
			} catch (IllegalArgumentException e) {
				illegalArgumentException = true;
			} catch (DateTimeParseException e) {
				// the value was not in the selected date format
				dateParseException = true;
			}
		}
		return value;
	}

	/**
	 * Refresh the latest date.
	 * @param value new latest date.
	 * @return the new latest date
	 */
	public String refreshLatestDate(String value) {

		if (latest != null) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatType, Locale.ENGLISH);
				LocalDate localDate = LocalDate.parse(value, formatter);
				Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				if (date.after(latest)) {
					value = "";
				}
			} catch (IllegalArgumentException e) {
				illegalArgumentException = true;
			} catch (DateTimeParseException e) {
				// the value was not in the selected date format
				dateParseException = true;
			}
		}
		return value;
	}

	/**
	 * Refresh the future date.
	 * @param value new future date.
	 * @return the new future date
	 */
	public String refreshFutureDate(String value) {

		if (!future) {
			try {
				Date current = new Date();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatType, Locale.ENGLISH);
				LocalDate localDate = LocalDate.parse(value, formatter);
				Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				if (date.after(current)) {
					value = "";
				}
			} catch (IllegalArgumentException e) {
				illegalArgumentException = true;
			} catch (DateTimeParseException e) {
				// the value was not in the selected date format
				dateParseException = true;
			}
		}
		return value;
	}

	/**
	 * Thrown if the entered value was not an valid date.
	 */
	public void showIllegalArgumentException() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(dateDialogStage);
		alert.setTitle(AlertConstants.NUMBER_FORMAT_EXCEPTION);
		alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
		alert.setContentText(AlertConstants.NUMBER_FORMAT_EXCEPTION_DESCRIPTION);
		alert.showAndWait();
	}

	/**
	 * Thrown if the value from data is not in the selected format.
	 */
	public void showParseException() {

		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(dateDialogStage);
		alert.setTitle(AlertConstants.DATE_PARSE_EXCEPTION);
		alert.setHeaderText(AlertConstants.DATE_PARSE_EXCEPTION_HEADER);
		alert.setContentText(AlertConstants.DATE_PARSE_EXCEPTION_MESSAGE);
		alert.showAndWait();
	}

	/**
	 * Load the settings into the GUI.
	 * @param attributName of the attribute the settings should be loaded.
	 */
	public void loadSettingsToGui(String attributName) {
		settings = PropertySettingsSingleton.getInstance();

		DatePropertySettings dateSettings = null;
		settings = PropertySettingsSingleton.getInstance();
		boolean check = false;
		for (HashMap<String, DatePropertySettings> hm : settings) {
			for (String s : hm.keySet()) {
				if (s.equals(attributName)) {
					dateSettings = (DatePropertySettings) hm.get(s);
					check = true;
					break;
				}
			}
			if (check) {
				break;
			}
		}
		if (!check) {
			dateSettings = new DatePropertySettings();
		}

		if (dateSettings.getMinimum() != null) {
			Date date = dateSettings.getMinimum();
			Instant instant = Instant.ofEpochMilli(date.getTime());
			earliestDate.setValue(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
		}
		if (dateSettings.getMaximum() != null) {
			Date date = dateSettings.getMaximum();
			Instant instant = Instant.ofEpochMilli(date.getTime());
			latestDate.setValue(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
			// latestDate.setValue(dateSettings.getMaximum());
		}

		dateInFuture.setSelected(dateSettings.isAllowFuture());
	}

	/**
	 * Save the settings for the attribute.
	 * @return settings of the attribue
	 */
	public DatePropertySettings saveSettings() {

		if (earliestDate.getValue() != null) {
			earliest = Date.from(earliestDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		if (latestDate.getValue() != null) {
			latest = Date.from(latestDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		future = dateInFuture.isSelected();

		datePropertySettings = new DatePropertySettings(earliest, latest, future);
		return datePropertySettings;
	}

	/**
	 * Set the table view.
	 * @param tableView
	 */
	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	/**
	 * Set the format typ.
	 * @param formatTyp
	 */
	public void setFormatType(String formatTyp) {
		this.formatType = formatTyp;
	}
}
