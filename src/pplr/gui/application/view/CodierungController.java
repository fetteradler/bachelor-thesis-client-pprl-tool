package pplr.gui.application.view;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import pplr.gui.application.configuration.AlertConstants;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.configuration.LabelConstants;
import pplr.gui.application.configuration.LayoutConstants;
import pplr.gui.application.settings.CodierungSettings;
import pplr.gui.application.tools.DynamicTableViewSingleton;

/**
 * Controller of the Encoding tab.
 * @author CM
 *
 */
public class CodierungController {

	/** Defines the table views. */
	@FXML
	private TableView dataTableCodierung;

	/** Defines the scroll pane of the tab. */
	@FXML
	private ScrollPane codierungScroll;

	/** Defines the buttons for go back and continue workflow. */
	@FXML
	private Button enterButtonCodierung, backButton;

	/** Defines the labels of the tab. */
	@FXML
	private Label attributLabelCodierung, functionLabelCodierung, parameterLabelCodierung;

	/** Defines the anchor panes of the tab. */
	@FXML
	private AnchorPane codierungAnchor, functionPane;

	/** Radio buttons to select the hash function. */
	@FXML
	private RadioButton randomHashButton, doubleHashButton;

	/** Defines the toggle group for the radio buttons. */
	@FXML
	private ToggleGroup hashFunction;

	/** Defines the text field for the length of the bloom filter. */
	@FXML
	TextField bloomLength;

	/** Contains the selected data records for the table view. */
	private DynamicTableViewSingleton dynamicTableView;

	/** Labels for all attribute names. */
	private Label[] allAssignAttributesLabels;

	/** Combo boxes to select the hash functions for double hashing. */
	private ComboBox<String> doubleHashFunction1, doubleHashFunction2;

	/** To select the function. */
	private ComboBox<String>[] functionCombo;

	/** The tab pane the tab is contained. */
	private TabPane tabPane;

	/** The labels for the q-gramm-splitter function. */
	private ArrayList<Label> labelQ, labelFilling, labelN;

	/** Radio buttons for the filler pattern for q-gram-splitter. */
	private ArrayList<RadioButton> radioYes, radioNo;

	/** Spinner for q-gram-splitter to select length of q. */
	private ArrayList<Spinner> spinner;

	/** Text field to enter the index of the n. pattern function. */
	private ArrayList<TextField> positionN;

	/** Text field to enter the length of the bloom filter. */
	private ArrayList<TextField> kField;

	/** Names of the attributes the user select in the data cleaning tab. */
	private String[] dataCleaningAttributes;

	/** Contains the settings the user set for the tab */
	private CodierungSettings settings;

	/**  */
	private ArrayList<ArrayList<Integer>> inFunctionValues;
	
	/** True if the entry was invalid **/
	private boolean invalidEntry;

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
		invalidEntry = false;
		enterButtonCodierung.setOnAction(this::handleContinueButton);
		backButton.setOnAction(this::handleBackButton);
	}
	
	/* ------------------------------ Creation controls ------------------------------ */

	/**
	 * Insert the labels of the attributes.
	 * 
	 * @param standartisierungTabController
	 *            holds the definition of the attributes
	 */
	public void insertAttributeLabel(StandardizationController standardizationTabController) {
		allAssignAttributesLabels = standardizationTabController.getUserAttributes();

		if (allAssignAttributesLabels != null) {
			double addonAttributeLayoutX = LayoutConstants.ENC_ADDON_ATTRIBUT_LAYOUT_X;
			double addonAttributeLayoutY = LayoutConstants.ENC_ADDON_ATTRIBUT_LAYOUT_Y;

			for (int i = 0; i < allAssignAttributesLabels.length; i++) {
				if (allAssignAttributesLabels[i].getText() != null) {
					if (allAssignAttributesLabels[i].getText().length() > addonAttributeLayoutX) {
						addonAttributeLayoutX = allAssignAttributesLabels[i].getText().length();
					}
				}
				codierungAnchor.getChildren().add(allAssignAttributesLabels[i]);
				allAssignAttributesLabels[i].setLayoutX(addonAttributeLayoutX);
				allAssignAttributesLabels[i].setLayoutY(addonAttributeLayoutY);

				addonAttributeLayoutY = addonAttributeLayoutY + LayoutConstants.ENC_COMBO_FUNC_MODIFIER;
			}
			codierungAnchor.setPrefHeight(addonAttributeLayoutY + LayoutConstants.ENC_COMBO_FUNC_OUTER_MODIFIER);
		}

		// insertHashSelection();
		hashFunction.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> value, Toggle oldToggle, Toggle newToggle) {
				insertDoubleHashMapFunctions();
			}
		});
		insertParameter();

		double helpButtonSet = codierungAnchor.getPrefHeight();
		enterButtonCodierung.setLayoutY(helpButtonSet - LayoutConstants.ENC_REDUCER);
		backButton.setLayoutY(helpButtonSet - LayoutConstants.ENC_REDUCER);

		// Set the size of the arrayLists for functions to length of all assign
		// attributes
		labelQ = new ArrayList<>(allAssignAttributesLabels.length);
		labelFilling = new ArrayList<>(allAssignAttributesLabels.length);
		spinner = new ArrayList<>(allAssignAttributesLabels.length);
		radioYes = new ArrayList<>(allAssignAttributesLabels.length);
		radioNo = new ArrayList<>(allAssignAttributesLabels.length);
		positionN = new ArrayList<>(allAssignAttributesLabels.length);
		labelN = new ArrayList<>(allAssignAttributesLabels.length);

		for (Label l : allAssignAttributesLabels) {
			labelQ.add(null);
			labelFilling.add(null);
			spinner.add(null);
			radioYes.add(null);
			radioNo.add(null);
			positionN.add(null);
			labelN.add(null);
		}
	}

	/**
	 * If double-hashing is selected, two comboboxes to select the algorithms will
	 * insert.
	 * 
	 * @param index
	 *            to identify the current attribute
	 */
	public void insertDoubleHashMapFunctions() { // int index) {

		if (doubleHashButton.isSelected()) {
			double comboFunction1LayoutX = LayoutConstants.ENC_COMBO_DOUBLE_LAYOUT_X1;
			double comboFunction2LayoutX = LayoutConstants.ENC_COMBO_DOUBLE_LAYOUT_X2;
			double comboFunctionsLayoutY = LayoutConstants.ENC_COMBO_DOUBLE_LAYOUT_Y;

			doubleHashFunction1 = new ComboBox<>();
			doubleHashFunction2 = new ComboBox<>();
			try {
				doubleHashFunction1.getItems().addAll(MessageDigest.getInstance(LabelConstants.SHA).getAlgorithm());
				doubleHashFunction2.getItems().addAll(MessageDigest.getInstance(LabelConstants.SHA).getAlgorithm());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			doubleHashFunction1.setLayoutX(comboFunction1LayoutX);
			doubleHashFunction1.setLayoutY(comboFunctionsLayoutY);
			doubleHashFunction2.setLayoutX(comboFunction2LayoutX);
			doubleHashFunction2.setLayoutY(comboFunctionsLayoutY);

			functionPane.getChildren().addAll(doubleHashFunction1, doubleHashFunction2);
		} else if (randomHashButton.isSelected()) {
			if (doubleHashFunction1 != null && doubleHashFunction2 != null) {
				((AnchorPane) doubleHashFunction1.getParent()).getChildren().remove(doubleHashFunction1);
				((AnchorPane) doubleHashFunction2.getParent()).getChildren().remove(doubleHashFunction2);
			}
		}
	}

	/**
	 * Insert for all attributes a combobox to select encoding function
	 */
	public void insertFunctionComboBox() {
		if (allAssignAttributesLabels != null) {
			double comboFunctionLayoutX = LayoutConstants.ENC_COMBO_FUNC_LAYOUT_X;
			double comboFunctionLayoutY = LayoutConstants.ENC_COMBO_FUNC_LAYOUT_Y;

			inFunctionValues = new ArrayList(allAssignAttributesLabels.length);

			functionCombo = new ComboBox[allAssignAttributesLabels.length];

			for (int i = 0; i < allAssignAttributesLabels.length; i++) {
				if (allAssignAttributesLabels[i].getText() != null) {
					if (allAssignAttributesLabels[i].getText().length() > comboFunctionLayoutX) {
						comboFunctionLayoutX = allAssignAttributesLabels[i].getText().length();
					}
				}
				functionCombo[i] = new ComboBox<String>();
				functionCombo[i].setPromptText(LabelConstants.FUNCTION);
				functionCombo[i].getItems().addAll(Constants.ENC_QGRAM, Constants.ENC_FIRST_PATTERN, Constants.ENC_N_PATTERN);

				codierungAnchor.getChildren().add(functionCombo[i]);
				functionCombo[i].setLayoutX(comboFunctionLayoutX);
				functionCombo[i].setLayoutY(comboFunctionLayoutY);
				int index = i;

				functionCombo[i].valueProperty().addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
						insertFunction(index, functionCombo[index].valueProperty().toString());
					}
				});

				comboFunctionLayoutY = comboFunctionLayoutY + LayoutConstants.ENC_COMBO_FUNC_MODIFIER;
			}
			codierungAnchor.setPrefHeight(comboFunctionLayoutY + LayoutConstants.ENC_COMBO_FUNC_OUTER_MODIFIER);
		}
	}

	/**
	 * Insert the controlls for the encoding function
	 */
	public void insertFunction(int index, String value) {
		double qFunctionLayoutX = LayoutConstants.ENC_Q_FUNC_LAYOUT_X;
		double fillAreaFunctionLayoutX = LayoutConstants.ENC_FILL_AREA_FUNC_LAYOUT_X;
		double yesFunctionLayoutX = LayoutConstants.ENC_YES_FUNC_LAYOUT_X;
		double noFunctionLayoutX = LayoutConstants.ENC_NO_FUNC_LAYOUT_X;
		double functionLayoutY = LayoutConstants.ENC_FUNC_LAYYOUT_Y;

		double spinnerFunctionLayoutX = LayoutConstants.ENC_SPINNER_FUNC_LAYOUT_X;
		double spinnerFunctionLayoutY = LayoutConstants.ENC_SPINNER_FUNC_LAYOUT_Y;
		if (functionCombo[index].getValue() != null) {

			functionLayoutY = functionLayoutY + (LayoutConstants.CONNECT_MODIFIER * index);
			spinnerFunctionLayoutY = spinnerFunctionLayoutY * (1 + index);

			removeAnchorPaneObjects(index);

			if (functionCombo[index].valueProperty().getValue().equals(Constants.ENC_QGRAM)) {

				Label q = new Label(LabelConstants.Q);
				labelQ.set(index, q);
				Spinner<Integer> valueQ = new Spinner<Integer>(1, 4, 3);
				spinner.set(index, valueQ);
				spinner.get(index).setPrefWidth(57.00);
				Label fillArea = new Label(LabelConstants.FILL_PATTERN);
				labelFilling.set(index, fillArea);
				ToggleGroup fillGroup = new ToggleGroup();
				RadioButton yes = new RadioButton(LabelConstants.YES);
				radioYes.set(index, yes);
				radioYes.get(index).setToggleGroup(fillGroup);
				RadioButton no = new RadioButton(LabelConstants.NO);
				radioNo.set(index, no);
				radioNo.get(index).setToggleGroup(fillGroup);

				labelQ.get(index).setLayoutX(qFunctionLayoutX);
				labelQ.get(index).setLayoutY(functionLayoutY);
				spinner.get(index).setLayoutX(spinnerFunctionLayoutX);
				spinner.get(index).setLayoutY(spinnerFunctionLayoutY);
				labelFilling.get(index).setLayoutX(fillAreaFunctionLayoutX);
				labelFilling.get(index).setLayoutY(functionLayoutY);
				radioYes.get(index).setLayoutX(yesFunctionLayoutX);
				radioYes.get(index).setLayoutY(functionLayoutY);
				radioNo.get(index).setLayoutX(noFunctionLayoutX);
				radioNo.get(index).setLayoutY(functionLayoutY);

				codierungAnchor.getChildren().addAll(labelQ.get(index), spinner.get(index), labelFilling.get(index),
						radioYes.get(index), radioNo.get(index));

				// ----Settings----
				try {
					spinner.get(index).getValueFactory().setValue(settings.getNQ().get(index));
					List<Boolean> keyYes = new ArrayList<Boolean>(settings.getFillPattern().keySet());
					List<Boolean> valueNo = new ArrayList<Boolean>(settings.getFillPattern().values());
					radioYes.get(index).setSelected(keyYes.get(index));
					radioNo.get(index).setSelected(valueNo.get(index));
				} catch (Exception e) {

				}
			} else if (functionCombo[index].valueProperty().getValue().equals(Constants.ENC_FIRST_PATTERN)) {

			} else if (functionCombo[index].valueProperty().getValue().equals(Constants.ENC_N_PATTERN)) {
				Label n = new Label("n");
				labelN.set(index, n);
				TextField valueN = new TextField();
				positionN.set(index, valueN);

				labelN.get(index).setLayoutX(qFunctionLayoutX);
				labelN.get(index).setLayoutY(functionLayoutY);
				positionN.get(index).setLayoutX(spinnerFunctionLayoutX);
				positionN.get(index).setLayoutY(spinnerFunctionLayoutY);

				codierungAnchor.getChildren().addAll(labelN.get(index), positionN.get(index));

				// ----Settings----
				try {
					valueN.setText(settings.getNQ().get(index).toString());
				} catch (Exception e) {

				}
			}
		}
	}

	/**
	 * Insert the controlls for the parameter selection
	 */
	public void insertParameter() {
		if (allAssignAttributesLabels != null) {
			double kParamLayoutX = LayoutConstants.ENC_K_PARAM_LAYOUT_X;
			double paramLayoutY = LayoutConstants.ENC_K_PARAM_LAYOUT_Y;

			double fieldLayoutX = LayoutConstants.ENC_FIELD_LAYOUT_X;
			double fieldLayoutY = LayoutConstants.ENC_FIELD_LAYOUT_Y;

			kField = new ArrayList<>(allAssignAttributesLabels.length);

			for (int i = 0; i < allAssignAttributesLabels.length; i++) {
				Label k = new Label(LabelConstants.K);

				kField.add(new TextField());

				k.setLayoutX(kParamLayoutX);
				k.setLayoutY(paramLayoutY);
				kField.get(i).setLayoutX(fieldLayoutX);
				kField.get(i).setLayoutY(fieldLayoutY);

				paramLayoutY = paramLayoutY + LayoutConstants.ENC_COMBO_FUNC_MODIFIER;
				fieldLayoutY = fieldLayoutY + LayoutConstants.ENC_COMBO_FUNC_MODIFIER;

				codierungAnchor.getChildren().addAll(k, kField.get(i));
			}
		}
	}

	// -------------------------------------------------------Remove-------------------------------------------------------

	/**
	 * To remove the children of an anchor pane.
	 * @param index of the children that should be removed
	 */
	public void removeAnchorPaneObjects(int index) {
		if (labelQ.get(index) != null) {
			codierungAnchor.getChildren().remove(labelQ.get(index));
			labelQ.set(index, null);
		}
		if (labelFilling.get(index) != null) {
			codierungAnchor.getChildren().remove(labelFilling.get(index));
			labelFilling.set(index, null);
		}
		if (radioYes.get(index) != null) {
			codierungAnchor.getChildren().remove(radioYes.get(index));
			radioYes.set(index, null);
		}
		if (radioNo.get(index) != null) {
			codierungAnchor.getChildren().remove(radioNo.get(index));
			radioNo.set(index, null);
		}
		if (spinner.get(index) != null) {
			codierungAnchor.getChildren().remove(spinner.get(index));
			spinner.set(index, null);
		}
		if (labelN.get(index) != null) {
			codierungAnchor.getChildren().remove(labelN.get(index));
			labelN.set(index, null);
		}
		if (positionN.get(index) != null) {
			codierungAnchor.getChildren().remove(positionN.get(index));
			positionN.set(index, null);
		}
	}

	// -------------------------------------------------------Settings-------------------------------------------------------

	/**
	 * Set the length of the bloom filter in the settings.
	 */
	public void setBloomLengthToTab() {
		if (settings.getBloomLength() != null) {
			bloomLength.setText(settings.getBloomLength().toString());
		}
	}

	/**
	 * Set the selected hashing.
	 */
	public void setHashingToTab() {
		if (settings.getType() != null) {
			doubleHashButton.setSelected(settings.getType()[0]);
			randomHashButton.setSelected(settings.getType()[1]);
		}
	}

	/**
	 * Set the saved value for k from the settings to the textfields.
	 */
	public void setK() {
		if (settings.getK() != null) {
			int maxIndex = 0;
			if (settings.getK().size() <= kField.size()) {
				maxIndex = settings.getK().size();
			} else {
				maxIndex = kField.size();
			}
			for (int i = 0; i < maxIndex; i++) {
				kField.get(i).setText(settings.getK().get(i).toString());
			}
		}
	}

	/**
	 * Set the functions from the settings.
	 */
	public void setFunctionsToTab() {
		if (settings.getFunction() != null && settings.getNQ() != null && settings.getFillPattern() != null) {
			int maxIndex = 0;
			if (settings.getFunction().size() <= functionCombo.length) {
				maxIndex = settings.getFunction().size();
			} else {
				maxIndex = functionCombo.length;
			}
			for (int i = 0; i < maxIndex; i++) {
				functionCombo[i].setValue(settings.getFunction().get(i));
			}
		}
	}

	/**
	 * Save the length of the bloom filter the user selected as settings.
	 */
	public void saveBloomLengthSettings() {
		if (settings == null) {
			settings = new CodierungSettings();
		}
		settings.setBloomLength(checkInteger(bloomLength.getText()));
	}

	/**
	 * Save the length of the bloom filter the user selected as settings.
	 */
	public void saveHashingSettings() {
		boolean[] attributes = new boolean[2];
		attributes[0] = doubleHashButton.isSelected();
		attributes[1] = randomHashButton.isSelected();
		settings.setType(attributes);
	}
	
	/**
	 * Insert user data from the selected file into the tableView.
	 * 
	 * @param hideTableView
	 *            True, if the user want to hide the data.
	 */
	public void loadTableView(boolean hideTableView) {
		if (dynamicTableView.getPath() != null) {
			if (!hideTableView) {
				dynamicTableView.update(dataTableCodierung, dynamicTableView.getData());
			}
		}
	}

	/**
	 * Save the values of k as settings.
	 */
	public void saveKSettings() {
		ArrayList<Integer> kList = new ArrayList<>();
		for (int i = 0; i < kField.size(); i++) {
			kList.add(checkInteger(kField.get(i).getText()));
		}
		settings.setK(kList);
	}

	/**
	 * Save the function the user selected to the settings.
	 */
	public void saveFunctionSettings() {
		ArrayList<String> func = new ArrayList<>();
		for (int i = 0; i < functionCombo.length; i++) {
			func.add(functionCombo[i].getValue());
		}
		settings.setFunction(func);
	}

	/**
	 * Helper function to save the user settings for each infunction settings.
	 * 
	 * @param index
	 *            Defines the function the settings will take.
	 * @param NQ
	 *            The value of the 'Q' in 'Q-Gramm-Splitter' or 'n' in 'n. Pattern'.
	 * @param fillYes
	 *            For function 'Q-Gramm-Splitter', true if user want fill pattern.
	 * @param fillNo
	 *            For function 'Q-Gramm-Splitter', true if user not want fill
	 *            pattern.
	 */
	public void saveInFunctionSettings() {

		ArrayList<Integer> newNQ = new ArrayList<>();
		LinkedHashMap<Boolean, Boolean> newFillPattern = new LinkedHashMap<>();

		for (int i = 0; i < allAssignAttributesLabels.length; i++) {
			if (spinner.get(i) != null) {
				newNQ.add((Integer) spinner.get(i).getValue());
			} else {
				if (positionN.get(i) != null) {
					newNQ.add(checkInteger(positionN.get(i).getText()));
				}
			}
			if (radioYes.get(i) != null && radioNo.get(i) != null) {
				newFillPattern.put(radioYes.get(i).isSelected(), radioNo.get(i).isSelected());
			}
		}

		settings.setNQ(newNQ);
		settings.setFillPattern(newFillPattern);
	}

	/**
	 * Check if an String contains an Integer. This is used for textfield to check
	 * if the input is an Integer.
	 * 
	 * @param check
	 *            The String to check.
	 * @return The integer value if it is success.
	 */
	public Integer checkInteger(String check) {
		Integer castInt = null;
		try {
			castInt = Integer.valueOf(check);
		} catch (Exception e) {
			invalidEntry = true;
		}
		return castInt;
	}
	
	/**
	 * Open a warning dialog to inform that the entry was invalid. An entry is invalid if it is not an integer.
	 */
	public static void showErrorInvalidEntry() {
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(AlertConstants.WARNING);
		alert.setHeaderText(AlertConstants.INVALID_ENTRY);
		alert.setContentText(AlertConstants.MUST_BE_INTEGER);
		alert.showAndWait();
	}
	
	// ------------------------------ Event handling ---------------------------------------

	/**
	 * Will be called by clicking on the continue button. Save settings and go to next tab.
	 * @param event Action event
	 */
	@FXML
	private void handleContinueButton(ActionEvent event) {
		saveBloomLengthSettings();
		saveFunctionSettings();
		saveHashingSettings();
		saveInFunctionSettings();
		saveKSettings();
		if(invalidEntry)
			showErrorInvalidEntry();
		for (int i = 0; i < allAssignAttributesLabels.length; i++) {
			codierungAnchor.getChildren().remove(allAssignAttributesLabels[i]);
			removeAnchorPaneObjects(i);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(4);
	}

	/**
	 * Will be called by clicking on the back button. Go the the previous tab.
	 * 
	 * @param event
	 *            Action event
	 */
	@FXML
	private void handleBackButton(ActionEvent event) {
		for (int i = 0; i < allAssignAttributesLabels.length; i++) {
			codierungAnchor.getChildren().remove(allAssignAttributesLabels[i]);
			removeAnchorPaneObjects(i);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(2);
	}

	// -------------------------------------------------------------------------------------------------

	/**
	 * Adapt the controlls to the layout
	 */
	public void updateLayout() {

	}

	/**
	 * Set the tab pane.
	 * @param tabPane
	 */
	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	/**
	 * Set the data cleaning attributes.
	 * @param dataCleaningAttributes
	 */
	public void setDataCleaningAttributes(String[] dataCleaningAttributes) {
		this.dataCleaningAttributes = dataCleaningAttributes;
	}

	/**
	 * Set the settings.
	 * @param settings
	 */
	public void setSettings(CodierungSettings settings) {
		this.settings = settings;
	}

	/**
	 * Set the encoding settings.
	 * @return the settings
	 */
	public CodierungSettings getSettings() {
		return settings;
	}
}
