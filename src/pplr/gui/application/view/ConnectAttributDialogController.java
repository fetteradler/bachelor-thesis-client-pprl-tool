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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.configuration.LayoutConstants;
import pplr.gui.application.model.ConnectAttributSingleton;

/**
 * Controller of the connect attribute dialog.
 * 
 * @author CM
 *
 */
public class ConnectAttributDialogController {

	/** The buttons of the dialog. **/
	@FXML
	private Button confirmButton, cancelButton, addAttribute, deleteAttribute;

	/** Comboboxes to chose the attributes to connect and the delimiter. **/
	@FXML
	private ComboBox<String> chooseAttribut1, chooseAttribut2, choosePattern;

	/** Field to enter the name of the connected attribute. **/
	@FXML
	private TextField newAttributName;

	/** Pane oft the dialog. **/
	@FXML
	private AnchorPane attributPane;

	/** True if the connecting was successfull. **/
	private boolean confirmClick = false;

	/** Stage oft the dialog. **/
	private Stage connectDialogStage;

	/**
	 * Contains all comboboxes to select the attribute for more than two attribute
	 * to connect.
	 **/
	private List<ComboBox> addAttributField;

	/** Contains all labels for the comboboxes. **/
	private List<Label> addAttributLabel;

	/** Increment for every combobox more than two. **/
	private int countAttributField;

	/** Layout settings **/
	private double countAttributLayoutY, countAttributLabelLayoutY;

	/** Functions for connect. **/
	private ConnectAttributSingleton connectSingleton;

	/** All attribute names of the data records. **/
	private ObservableList<String> listAttributNames;

	/** Sum of the selected attributes. **/
	private int selectMergeItems;

	/** True if the selected attributes are equals. **/
	private boolean checkDuplicates = false;

	/*
	 * ------------------------------ Initialize dialog
	 * ------------------------------
	 */

	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	private void initialize() {

		connectSingleton = ConnectAttributSingleton.getInstance();
		listAttributNames = FXCollections.observableArrayList();
		ObservableList<String> listPattern = FXCollections.observableArrayList(Constants.PATTERN_SPACE_WORD,
				Constants.PATTERN_DASH, Constants.PATTERN_SLASH, Constants.PATTERN_DOT);
		choosePattern.setItems(listPattern);

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

		countAttributField = 0;
		countAttributLayoutY = LayoutConstants.CONNECT_COUNT_LAYOUT_Y;
		addAttributField = new ArrayList<>();
		addAttributLabel = new ArrayList<>();
		countAttributLabelLayoutY = LayoutConstants.CONNECT_COUNT_LABEL_LAYOUT_Y;

		addAttribute.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleAddAttribut(countAttributField);
			}
		});

		deleteAttribute.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleDeleteAttribut(countAttributField);
			}
		});
	}

	/* ------------------------------ Functions ------------------------------ */

	/**
	 * Save a list of the columnNames of the attributes that should be connected
	 * 
	 * @return list of column names
	 */
	public ArrayList<String> saveNewAttributes() {

		// check if a minimum of two items is selected
		selectMergeItems = 0;

		ArrayList<String> list = new ArrayList<String>();
		if (chooseAttribut1.getValue().toString().equals("") || chooseAttribut2.getValue().toString().equals("")) {
			selectMergeItems = 0;
		} else if (chooseAttribut1.getValue().toString().equals(chooseAttribut2.getValue().toString())) {
			selectMergeItems = 0; // the first two attributes are empty
		} else {
			list.add(chooseAttribut1.getValue().toString());
			list.add(chooseAttribut2.getValue().toString());
			selectMergeItems = 1; // the first two attributes are empty and not equals
		}

		for (ComboBox<String> box : addAttributField) {
			if (box.getValue() == null) {
				selectMergeItems = 2; // at least one attribute is empty but not the first two
			} else {
				for (String c : list) {
					if (c.equals(box.getValue().toString())) {
						checkDuplicates = true;
					}
				}
				list.add(box.getValue().toString());
			}
		}
		connectSingleton.setOldAttributes(list);
		return list;
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
	 * Check if the entries are valid. If it is valid -> execute connecting. Else
	 * throw alert.
	 * 
	 * @return True if the input is valid.
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		String warningMessage = "";
		try {
			saveNewAttributes();
			int patternIndex = setPatternIndex(choosePattern.getValue());
			if (patternIndex == 0) {
				errorMessage = AlertConstants.ILLEGAL_DELIMITER;
			}
			connectSingleton.setPatternIndex(patternIndex);

			if (newAttributName.getText().trim().length() == 0) {
				errorMessage = AlertConstants.ILLEGAL_DENOTATION;
			} else {
				connectSingleton.setNewAttribut(newAttributName.getText());
			}
			if (checkDuplicates) {
				errorMessage = AlertConstants.DUPLICATED_DENOTATION;
			}
			checkDuplicates = false;
			if (selectMergeItems == 0) {
				errorMessage = AlertConstants.LESS_ATTRIBUTES;
			} else if (selectMergeItems == 2) {
				warningMessage = AlertConstants.NOT_SELECTED_ATTRIBUTE;
			}

		} catch (NumberFormatException e) {
			errorMessage = AlertConstants.INVALID_ENTRY;
		}
		if (errorMessage.length() == 0) {
			if (warningMessage.length() != 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION, warningMessage, ButtonType.YES, ButtonType.NO,
						ButtonType.CANCEL);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(connectDialogStage);
			alert.setTitle(AlertConstants.INVALID_ENTRY);
			alert.setHeaderText(AlertConstants.CORRECT_ENTRY);
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	/*
	 * ------------------------------ Event handling ------------------------------
	 */

	/**
	 * If the input is valid close the dialog by confirming.
	 */
	@FXML
	public void handleConfirm() {
		if (isInputValid()) {

			confirmClick = true;
			connectDialogStage.close();
		}
	}

	/**
	 * Close the dialog by canceling.
	 */
	@FXML
	public void handleCancel() {
		connectDialogStage.close();
	}

	/**
	 * Add textfields for illegal chars.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleAddAttribut(int count) {

		String labelDescription = "Select attribute ";
		addAttributLabel.add(new Label());
		Integer labelCount = count + 3;
		addAttributLabel.get(count).setText(labelDescription + labelCount);
		attributPane.getChildren().add(addAttributLabel.get(count));
		addAttributLabel.get(count).setLayoutX(LayoutConstants.SPLIT_CONNECT_LABEL_X);
		addAttributLabel.get(count).setLayoutY(countAttributLabelLayoutY);

		addAttributField.add(new ComboBox<String>());
		attributPane.getChildren().add(addAttributField.get(count));
		addAttributField.get(count).setLayoutX(LayoutConstants.CONNECT_FIELD_X);
		addAttributField.get(count).setLayoutY(countAttributLayoutY);
		addAttributField.get(count).setPrefWidth(LayoutConstants.CONNECT_FIELD_PREF_WIDTH);

		addAttributField.get(count).setItems(listAttributNames);

		if (count > 0) {
			attributPane.setPrefHeight(attributPane.getPrefHeight() + LayoutConstants.CONNECT_MODIFIER);
		}

		countAttributLayoutY = countAttributLayoutY + LayoutConstants.CONNECT_MODIFIER;
		countAttributLabelLayoutY = countAttributLabelLayoutY + LayoutConstants.CONNECT_MODIFIER;

		countAttributField++;
	}

	/**
	 * Delete textfields for allowed chars.
	 * 
	 * @param count
	 *            number of textfields
	 */
	public void handleDeleteAttribut(int count) {

		if (countAttributField > 0) {
			count--;
			attributPane.getChildren().remove(addAttributField.get(count));
			attributPane.getChildren().remove(addAttributLabel.get(count));

			if (count > 0) {
				attributPane.setPrefHeight(attributPane.getPrefHeight() - LayoutConstants.CONNECT_MODIFIER);
			}

			countAttributLayoutY = countAttributLayoutY - LayoutConstants.CONNECT_MODIFIER;
			countAttributLabelLayoutY = countAttributLabelLayoutY - LayoutConstants.CONNECT_MODIFIER;

			countAttributField--;
		}
	}

	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */

	/**
	 * Set the names of the column from csv data.
	 * 
	 * @param values
	 *            list with all column names
	 */
	public void setChooseAttributeValues(ArrayList<String> values) {

		if (values != null) {
			ObservableList<String> listAttribut = FXCollections.observableArrayList(values);
			chooseAttribut1.setItems(listAttribut);
			chooseAttribut2.setItems(listAttribut);

			listAttributNames = listAttribut;
		}
	}

	/**
	 * Set the stage of the dialog.
	 * 
	 * @param connectDialogStage
	 *            Stage of the dialog.
	 */
	public void setDialogStage(Stage connectDialogStage) {
		this.connectDialogStage = connectDialogStage;
	}

	/**
	 * Set the index for the selected split pattern
	 * 
	 * @param pattern
	 * @return
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
			}
		}
		return index;
	}

}
