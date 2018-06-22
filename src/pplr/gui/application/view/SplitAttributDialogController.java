package pplr.gui.application.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.configuration.LayoutConstants;
import pplr.gui.application.functions.PatternHandling;
import pplr.gui.application.model.SplitAttributSingleton;
import pplr.gui.application.tools.DynamicTableViewSingleton;

/**
 * Controller of the split attribute dialog.
 * 
 * @author CM
 *
 */
public class SplitAttributDialogController {

	/** Buttons to confirm or cancel splitting. **/
	@FXML
	private Button confirmButton, cancelButton;
	
	/** To select attribute and pattern for splitting. **/
	@FXML
	private ComboBox<String> attributComboBox, patternComboBox;
	
	/** The anchor pane. **/
	@FXML
	private AnchorPane newAttributePane;
	
	/** Stage of the dialog **/
	private Stage splitDialogStage;
	
	/** True if the connecting was successfull. **/
	private boolean confirmClick = false;

	/** Contains all textfields to insert the new attribut names. **/
	private List<TextField> addAttributField;

	/** Contains all labels for the textfields. **/
	private List<Label> addAttributLabel;

	/** Increment for every combobox more than two. **/
	private int countAttributField;

	/** Layout settings **/
	private double countAttributLayoutY, countAttributLabelLayoutY;
	
	/** True if two or more of the entered new attribute names are equals. **/
	private boolean checkDuplicates = false;
	
	/** Functions for splitting. **/
	private SplitAttributSingleton splitSingleton;
	
	/** Contains the selected data records for the table view. **/
	private DynamicTableViewSingleton tableView;

	/* ------------------------------ Initialize dialog ------------------------------ */
	
	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	private void initialize() {

		tableView = DynamicTableViewSingleton.getInstance();
		splitSingleton = SplitAttributSingleton.getInstance();

		ObservableList<String> listPattern = FXCollections.observableArrayList(
				Constants.PATTERN_SPACE_WORD, Constants.PATTERN_DASH, Constants.PATTERN_SLASH,
				Constants.PATTERN_DOT, Constants.PATTERN_PLZ);
		patternComboBox.setItems(listPattern);

		attributComboBox.setOnAction(this::handleAttributeCombo);
		patternComboBox.setOnAction(this::handlePatternCombo);

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

		if (patternComboBox.getValue() != null && attributComboBox.getValue() != null) {
			refreshInputFields();
		}

		countAttributField = 0;
		countAttributLayoutY = LayoutConstants.SPLIT_COUNT_LAYOUT_Y;
		addAttributField = new ArrayList<>();
		addAttributLabel = new ArrayList<>();
		countAttributLabelLayoutY = LayoutConstants.SPLIT_COUNT_LABEL_LAYOUT_Y;
	}
	
	/* ------------------------------ Functions ------------------------------ */
	
	/**
	 * Confirm was successful.
	 * 
	 * @return true if confirm
	 */
	public boolean isConfirmClick() {
		return confirmClick;
	}

	/**
	 * Check if the entries are valid. If it is valid -> execute splitting. Else throw alert.
	 * @return True if the input is valid.
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		try {
			saveNewAttributes();
			int patternIndex = setPatternIndex(patternComboBox.getValue());
			if (patternIndex == 0) {
				errorMessage = AlertConstants.ILLEGAL_DELIMITER;
			}
			splitSingleton.setPatternIndex(patternIndex);

			splitSingleton.setOldAttribut(attributComboBox.getValue());
			if (checkDuplicates) {
				errorMessage = AlertConstants.DUPLICATED_DENOTATION;
			}
			checkDuplicates = false;
			for (TextField field : addAttributField) {
				if (field.getText().length() == 0) {
					errorMessage = AlertConstants.EMPTY_DENOTATION;
					break;
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
			alert.initOwner(splitDialogStage);
			alert.setTitle(AlertConstants.INVALID_ENTRY);
			alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	/**
	 * Save the new columnNames for splited attributes.
	 * 
	 * @return list with new columnNames
	 */
	public ArrayList<String> saveNewAttributes() {

		ArrayList<String> list = new ArrayList<String>();

		for (TextField field : addAttributField) {
			for (String c : list) {
				if (c.equals(field.getText())) {
					checkDuplicates = true;
				}
			}
			list.add(field.getText());
		}
		splitSingleton.setNewAttributes(list);
		return list;
	}

	/**
	 * Set automatically the input fields of the new attribute names by
	 * the maximum occurrence of the selected delimiter in the selected attribute.
	 */
	public void refreshInputFields() {
		String value = attributComboBox.getValue();
		String pattern = patternComboBox.getValue();
		int fields = PatternHandling.countPattern(tableView, pattern, tableView.getColumnNames().indexOf(value)) + 1;

		for(int i = 0; i < addAttributLabel.size(); i++) {
			newAttributePane.getChildren().remove(addAttributField.get(i));
			newAttributePane.getChildren().remove(addAttributLabel.get(i));
		}
		addAttributLabel.clear();
		addAttributField.clear();
		countAttributLayoutY = LayoutConstants.SPLIT_COUNT_LAYOUT_Y;
		countAttributLabelLayoutY = LayoutConstants.SPLIT_COUNT_LABEL_LAYOUT_Y;
				
		newAttributePane.setPrefHeight(LayoutConstants.SPLIT_PANE_PREF_HEIGHT);
		for (int i = 0; i < fields; i++) {
			addAttributLabel.add(new Label());
			addAttributLabel.get(i).setText("New attribute " + (i + 1));
			newAttributePane.getChildren().add(addAttributLabel.get(i));
			addAttributLabel.get(i).setLayoutX(LayoutConstants.SPLIT_CONNECT_LABEL_X);
			addAttributLabel.get(i).setLayoutY(countAttributLabelLayoutY);

			addAttributField.add(new TextField());
			newAttributePane.getChildren().add(addAttributField.get(i));
			addAttributField.get(i).setLayoutX(LayoutConstants.SPLIT_FIELD_X);
			addAttributField.get(i).setLayoutY(countAttributLayoutY);

			if (i > 3) {
				newAttributePane.setPrefHeight(newAttributePane.getPrefHeight() + LayoutConstants.SPLIT_MODIFIER);
			}
			countAttributLayoutY = countAttributLayoutY + LayoutConstants.SPLIT_MODIFIER;
			countAttributLabelLayoutY = countAttributLabelLayoutY + LayoutConstants.SPLIT_MODIFIER;
		}
	}
	
	/* ------------------------------ Event handling ------------------------------ */
	
	/**
	 * Add delimiter to combobox.
	 * @param event action event
	 */
	@FXML
	private void handlePatternCombo(ActionEvent event) {
		if(attributComboBox.getValue() != null) {
			refreshInputFields();
		}
	}
	
	/**
	 * Add attributes to combobox.
	 * @param event action event
	 */
	@FXML
	private void handleAttributeCombo(ActionEvent event) {
		if(patternComboBox.getValue() != null) {
			refreshInputFields();
		}
	}

	/**
	 * If the input is valid close the dialog by confirming.
	 */
	@FXML
	public void handleConfirm() {
		if (isInputValid()) {

			confirmClick = true;
			splitDialogStage.close();
		}
	}

	/**
	 * Close the dialog by canceling.
	 */
	@FXML
	public void handleCancel() {
		splitDialogStage.close();
	}

	/**
	 * Add textfields for the new attribute descriptions.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleAddAttribut(int count) {

		String labelDescription = "New attribute denotation: ";
		addAttributLabel.add(new Label());
		Integer labelCount = count + 3;
		addAttributLabel.get(count).setText(labelDescription + labelCount);
		newAttributePane.getChildren().add(addAttributLabel.get(count));
		addAttributLabel.get(count).setLayoutX(LayoutConstants.SPLIT_CONNECT_LABEL_X);
		addAttributLabel.get(count).setLayoutY(countAttributLabelLayoutY);

		addAttributField.add(new TextField());
		newAttributePane.getChildren().add(addAttributField.get(count));
		addAttributField.get(count).setLayoutX(LayoutConstants.SPLIT_FIELD_X);
		addAttributField.get(count).setLayoutY(countAttributLayoutY);

		if (count > 1) {
			newAttributePane.setPrefHeight(newAttributePane.getPrefHeight() + LayoutConstants.SPLIT_MODIFIER);
		}

		countAttributLayoutY = countAttributLayoutY + LayoutConstants.SPLIT_MODIFIER;
		countAttributLabelLayoutY = countAttributLabelLayoutY + LayoutConstants.SPLIT_MODIFIER;

		countAttributField++;
	}

	/**
	 * Delete textfields for a attribute description.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleDeleteAttribut(int count) {

		if (countAttributField > 0) {
			count--;
			newAttributePane.getChildren().remove(addAttributField.get(count));
			newAttributePane.getChildren().remove(addAttributLabel.get(count));

			if (count > 1) {
				newAttributePane.setPrefHeight(newAttributePane.getPrefHeight() - LayoutConstants.SPLIT_MODIFIER);
			}

			countAttributLayoutY = countAttributLayoutY - LayoutConstants.SPLIT_MODIFIER;
			countAttributLabelLayoutY = countAttributLabelLayoutY - LayoutConstants.SPLIT_MODIFIER;

			countAttributField--;
		}
	}
	
	/* ------------------------------ Getter & Setter ------------------------------ */

	/**
	 * Set the index for the selected split pattern
	 * 
	 * @param pattern delimiter pattern
	 * @return index of the delimiter pattern
	 */
	public int setPatternIndex(String pattern) {

		int index = 0;
		if (pattern != null) {
			if (pattern.equals(Constants.PATTERN_SPACE_WORD)) {
				index = 1;
			} else if (pattern.equals(Constants.PATTERN_DASH)) {
				index = 2;
			} else if (pattern.equals(Constants.PATTERN_SLASH)) {
				index = 3;
			} else if (pattern.equals(Constants.PATTERN_DOT)) {
				index = 4;
			} else if (pattern.equals(Constants.PATTERN_PLZ)) {
				index = 5;
			}
		}
		return index;
	}

	/**
	 * Set the names of the column from csv data.
	 * 
	 * @param values
	 *            list with all column names
	 */
	public void setChooseAttributeValues(ArrayList<String> values) {

		if (values != null) {
			ObservableList<String> listAttribut = FXCollections.observableArrayList(values);
			attributComboBox.setItems(listAttribut);
		}
	}

	/**
	 * Set the stage to the dialog.
	 * @param splitDialogStage Stage to the dialog.
	 */
	public void setDialogStage(Stage splitDialogStage) {
		this.splitDialogStage = splitDialogStage;
	}
}
