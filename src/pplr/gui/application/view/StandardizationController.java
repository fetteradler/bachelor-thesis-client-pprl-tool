package pplr.gui.application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import pplr.gui.application.Main;
import pplr.gui.application.configuration.Constants;
import pplr.gui.application.configuration.LabelConstants;
import pplr.gui.application.configuration.LayoutConstants;
import pplr.gui.application.model.ConfigurationObjectSingleton;
import pplr.gui.application.model.DatePropertySettings;
import pplr.gui.application.model.IntegerPropertySettings;
import pplr.gui.application.model.PropertySettingsSingleton;
import pplr.gui.application.model.StringPropertySettings;
import pplr.gui.application.settings.DataCleaningSettings;
import pplr.gui.application.settings.DateRestrictionSettings;
import pplr.gui.application.settings.IntegerRestrictionSettings;
import pplr.gui.application.settings.StandardizationSettings;
import pplr.gui.application.settings.StringRestrictionSettings;
import pplr.gui.application.tools.DynamicTableViewSingleton;
import pplr.gui.application.tools.ExportCSV;

/**
 * Controller of the standardization tab.
 * 
 * @author CM
 *
 */
public class StandardizationController {

	/** Defines the table views of the tab. */
	@FXML
	private TableView dataTableStandartisierung, invalidData;

	/** Defines the anchor panes of the tab. */
	@FXML
	private AnchorPane definitionAnchorStandart, addonAnchorStandart;

	/** Defines the scroll pane of the tab. */
	@FXML
	private ScrollPane scrollPane;

	/** Defines the labels of the tab. */
	@FXML
	private Label formatStandart, typStandart, attributStandart;

	/** Defines the tab pane of the tab. */
	@FXML
	private TabPane tabPaneStandart;

	/** Defines the buttons of the tab. */
	@FXML
	private Button continueButton, checkButton, saveData, backButton;

	/** Defines the string restriction dialog controller. */
	@FXML
	private StringRestrictionDialogController controller;

	/** Defines the integer restriction dialog controller. */
	@FXML
	private IntegerRestrictionDialogController intController;

	/** Defines the date restriction dialog of the controller. */
	@FXML
	private DateRestrictionDialogController dateController;

	/**
	 * Dynamic content for the table view of the controller.
	 */
	private DynamicTableViewSingleton dynamicTableView;

	/**
	 * Defines the attributes that are necessary for the record linkage.
	 */
	private ConfigurationObjectSingleton configObject;

	/**
	 * Includes the specific settings for all attributes.
	 */
	private PropertySettingsSingleton allAttributeSettings;

	/**
	 * Main of the application.
	 */
	private Main main;

	/**
	 * The tab pane of the application.
	 */
	private TabPane tabPane;

	/**
	 * Tabs in the tab pane.
	 */
	private Tab definitionTab, addonTab;

	/**
	 * The the position in the layout.
	 */
	private double attributLayoutX, typLayoutX, formatLayoutX;

	/**
	 * A combobox to select the data type per attribute
	 */
	private ComboBox<String>[] attributeTypes;

	/**
	 * Names of the attributes for RL from the user data.
	 */
	private Label[] userAttributes, types;

	/** Contains the settings the user set for the tab */
	private StandardizationSettings standardSettings;

	/** Data cleaning controller */
	private DataCleaningController dCController;

	/** Data cleaning settings. */
	private DataCleaningSettings dCSettings;

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
		configObject = ConfigurationObjectSingleton.getInstance();
		allAttributeSettings = PropertySettingsSingleton.getInstance();

		continueButton.setOnAction(this::handleContinueButton);
		saveData.setOnAction(this::handleSaveDataButton);
		backButton.setOnAction(this::handleBackButton);
	}

	/**
	 * --------------------------------------------------Layout--------------------------------------------------
	 */

	/**
	 * Create all nodes for the tab and set them in the layout.
	 */
	public void createLayout() {

		clearScrollPane();
		createAttributeLabels();
		createTypeComboboxes();
		tabPaneBehavior();
	}

	/**
	 * Create all labels for the necessary attributes for the record linkage. These
	 * are the labels from the configuration object.
	 */
	private void createAttributeLabels() {

		int labelLength = 0;
		Label[] allLabels = new Label[configObject.getAttributeNames().size()];
		double labelLayoutY = LayoutConstants.S_LABEL_Y;
		for (int i = 0; i < allLabels.length; i++) {
			allLabels[i] = new Label();
			allLabels[i].setText(configObject.getAttributeNames().get(i).getValue());
			if (allLabels[i].getText().length() < labelLength) {
				labelLength = allLabels[i].getText().length();
			}
			definitionAnchorStandart.getChildren().add(allLabels[i]);
			allLabels[i].setLayoutX(LayoutConstants.S_LABEL_X);
			allLabels[i].setLayoutY(labelLayoutY);

			labelLayoutY = labelLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
		}
		definitionAnchorStandart.setPrefHeight(labelLayoutY + LayoutConstants.S_DEFUALT_MODIFIER);

		attributLayoutX = LayoutConstants.S_ATTRIBUTE_INI
				+ (LayoutConstants.S_LABEL_X * LayoutConstants.S_LABEL_MULTIPLIER);
		attributStandart.setLayoutX(attributLayoutX);
		typLayoutX = attributLayoutX + LayoutConstants.S_TYPE_FORMAT_MODIFIER;
		typStandart.setLayoutX(typLayoutX);
		formatLayoutX = typLayoutX + LayoutConstants.S_TYPE_FORMAT_MODIFIER;
		formatStandart.setLayoutX(formatLayoutX);
	}

	/**
	 * Create to every necessary attribute for the record linkage a combobox to
	 * select the specific type format.
	 */
	private void createTypeComboboxes() {

		attributeTypes = new ComboBox[configObject.getAttributeNames().size()];
		double formatLayoutY = LayoutConstants.S_FORMAT_LAYOUT_Y;
		for (int i = 0; i < attributeTypes.length; i++) {
			attributeTypes[i] = new ComboBox<String>();
			attributeTypes[i].setPromptText("Select type...");
			attributeTypes[i].setPrefWidth(LayoutConstants.S_TYPE_PREF_WIDTH);
			definitionAnchorStandart.getChildren().add(attributeTypes[i]);
			attributeTypes[i].setLayoutX(formatLayoutX);
			attributeTypes[i].setLayoutY(formatLayoutY);

			formatLayoutY = formatLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
		}
	}

	/**
	 * Controls the behavior of the tab pane.
	 */
	private void tabPaneBehavior() {

		// Define TabPane
		ObservableList<Tab> tabs = tabPaneStandart.getTabs();
		definitionTab = tabs.get(0);
		addonTab = tabs.get(1);

		tabPaneStandart.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
					if (newValue == addonTab) {
						addonAnchorStandart.getChildren().clear();
						addonAttributes();
						addonTypes();
						addonRestrictions();
					} else if (newValue == definitionTab) {
						 refreshAttributLabel();
					}
				});
		checkButton.setOnAction(this::handleCheckButton);

	}

	/**
	 * Create all labels for the necessary attributes for the record linkage. These
	 * are the labels from the user data.
	 */
	private void addonAttributes() {
		if (userAttributes != null) {
			double addonAttributeLayoutX = LayoutConstants.S_ADDON_ATTRIBUTE_LAYOUT_X;
			double addonAttributeLayoutY = LayoutConstants.S_ADDON_ATTRIBUTE_LAYOUT_Y;

			for (int i = 0; i < userAttributes.length; i++) {
				if (userAttributes[i].getText() != null) {
					if (userAttributes[i].getText().length() > addonAttributeLayoutX) {
						addonAttributeLayoutX = userAttributes[i].getText().length();
					}
				}
				Label[] userAttributesAddon = userAttributes.clone();
				addonAnchorStandart.getChildren().add(userAttributesAddon[i]);
				userAttributesAddon[i].setLayoutX(addonAttributeLayoutX);
				userAttributesAddon[i].setLayoutY(addonAttributeLayoutY);

				addonAttributeLayoutY = addonAttributeLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
			}
			addonAnchorStandart.setPrefHeight(addonAttributeLayoutY + LayoutConstants.S_DEFUALT_MODIFIER);
		}
	}

	/**
	 * Create a label of the type to all necessary attributes of the RL.
	 */
	private void addonTypes() {
		if (types != null) {
			double addonTypesLayoutX = LayoutConstants.S_ADDON_TYPE_LAYOUT_X;
			double addonTypesLayoutY = LayoutConstants.S_ADDON_ATTRIBUTE_LAYOUT_Y;

			for (int i = 0; i < types.length; i++) {

				if (types[i].getText() != null) {
					if (types[i].getText().length() > addonTypesLayoutX) {
						addonTypesLayoutX = types[i].getText().length();
					}
				}
				Label[] typesAddon = new Label[types.length];
				for (int j = 0; j < attributeTypes.length; j++) {
					typesAddon[j] = new Label();
					if (attributeTypes[j].getValue() != null) {
						typesAddon[j]
								.setText(types[j].getText() + Constants.PATTERN_SLASH + attributeTypes[j].getValue());
					}
				}
				addonAnchorStandart.getChildren().add(typesAddon[i]);

				typesAddon[i].setLayoutX(addonTypesLayoutX);
				typesAddon[i].setLayoutY(addonTypesLayoutY);

				addonTypesLayoutY = addonTypesLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
			}
			addonAnchorStandart.setPrefHeight(addonTypesLayoutY + LayoutConstants.S_DEFUALT_MODIFIER);
		}
	}

	/**
	 * Create a button to set restrictions to every necessary attribute of the RL.
	 */
	private void addonRestrictions() {
		if (types != null) {
			double addonTypesLayoutY = LayoutConstants.S_ADDON_ATTRIBUTE_LAYOUT_Y;
			double addonButtonLayoutX = LayoutConstants.S_ADDON_BUTTON_LAYOUT_X;

			Button[] addonButtons = new Button[types.length];
			for (int i = 0; i < types.length; i++) {

				addonButtons[i] = new Button();
				if (types[i].getText() != null) {
					if (types[i].getText().length() > addonButtonLayoutX) {
						addonButtonLayoutX = types[i].getText().length();
					}
					if (types[i].getText().equals(Constants.TYPE_STRING)) {
						addonButtons[i].setText(LabelConstants.RESTRICT_STRING);

						// create StringPropertySettings to every string object.
						HashMap<String, StringPropertySettings> settingsToAttribute = new HashMap<String, StringPropertySettings>();

						settingsToAttribute.put(userAttributes[i].getText(), new StringPropertySettings());

						if (!allAttributeSettings.contains(settingsToAttribute)) {
							allAttributeSettings.add(settingsToAttribute);
						}

						// UserSettings
						if (standardSettings != null && standardSettings.getStringSettings() != null) {
							if (standardSettings.getStringSettings().containsKey(userAttributes[i].getText())) {
								StringRestrictionSettings stringSettings = standardSettings.getStringSettings()
										.get(userAttributes[i].getText());
								for (int j = 0; j < allAttributeSettings.size(); j++) {
									for (Object o : allAttributeSettings.get(j).values()) {
										if (o.getClass() == StringPropertySettings.class) {
											int index = 0;
											HashMap<String, StringPropertySettings> map = new HashMap<>();
											map.put(userAttributes[i].getText(), new StringPropertySettings(
													String.valueOf(stringSettings.getMaximum()),
													String.valueOf(stringSettings.getMinimum()),
													stringSettings.isRemoveSpace(),
													stringSettings.isRemoveWhitespaces(),
													stringSettings.getOnlyAllowedStrings(),
													stringSettings.getExchange(), stringSettings.getExchangeWith()));
											if (allAttributeSettings.get(j).containsKey(userAttributes[i].getText())) {
												allAttributeSettings.set(index, map);
											} else {
												allAttributeSettings.setStringProperties(map);
											}

											index++;
										}
									}
								}
							}
						}

						handleRestriction(addonButtons[i], 1, userAttributes[i].getText(), types[i].getText());
					} else if (types[i].getText().equals(Constants.TYPE_INT)) {
						addonButtons[i].setText(LabelConstants.RESTRICT_INT);

						HashMap<String, IntegerPropertySettings> settingsToIntAttribut = new HashMap<String, IntegerPropertySettings>();
						settingsToIntAttribut.put(userAttributes[i].getText(), new IntegerPropertySettings());

						if (!allAttributeSettings.contains(settingsToIntAttribut)) {
							allAttributeSettings.add(settingsToIntAttribut);
						}

						// UserSettings
						if (standardSettings != null && standardSettings.getIntegerSettings() != null) {
							if (standardSettings.getIntegerSettings().containsKey(userAttributes[i].getText())) {
								IntegerRestrictionSettings intSettings = standardSettings.getIntegerSettings()
										.get(userAttributes[i].getText());

								for (int j = 0; j < allAttributeSettings.size(); j++) {
									for (Object o : allAttributeSettings.get(j).values()) {
										if (o.getClass() == IntegerPropertySettings.class) {
											int index = 0;
											HashMap<String, IntegerPropertySettings> map = new HashMap<>();
											map.put(userAttributes[i].getText(),
													new IntegerPropertySettings(intSettings.getMaximum(),
															intSettings.getMinimum(), intSettings.isAllowNegative()));

											if (allAttributeSettings.get(j).containsKey(userAttributes[i].getText())) {
												allAttributeSettings.set(index, map);
											} else {
												allAttributeSettings.setIntegerProperties(map);
											}
											index++;
										}
									}
								}
							}
						}

						handleRestriction(addonButtons[i], 2, userAttributes[i].getText(), types[i].getText());
					} else if (types[i].getText().equals(Constants.TYPE_DATE)) {
						addonButtons[i].setText(LabelConstants.RESTRICT_DATE);

						HashMap<String, DatePropertySettings> settingsToDateAttribut = new HashMap<String, DatePropertySettings>();
						settingsToDateAttribut.put(userAttributes[i].getText(), new DatePropertySettings());

						if (!allAttributeSettings.contains(settingsToDateAttribut)) {
							allAttributeSettings.add(settingsToDateAttribut);
						}
						// UserSettings
						if (standardSettings != null && standardSettings.getDateSettings() != null) {
							if (standardSettings.getDateSettings().containsKey(userAttributes[i].getText())) {
								DateRestrictionSettings dateSettings = standardSettings.getDateSettings()
										.get(userAttributes[i].getText());
								for (int j = 0; j < allAttributeSettings.size(); j++) {
									for (Object o : allAttributeSettings.get(j).values()) {
										if (o.getClass() == DatePropertySettings.class) {
											int index = 0;
											HashMap<String, DatePropertySettings> map = new HashMap<>();
											map.put(userAttributes[i].getText(),
													new DatePropertySettings(dateSettings.getMaximum(),
															dateSettings.getMinimum(),
															dateSettings.isAllowNegative()));
											if (allAttributeSettings.get(j).containsKey(userAttributes[i].getText())) {
												allAttributeSettings.set(index, map);
											} else {
												allAttributeSettings.setDateProperties(map);
											}
											index++;
										}
									}
								}
							}
						}
						handleRestriction(addonButtons[i], 3, userAttributes[i].getText(), attributeTypes[i].getValue());
					}
				} else {
					addonButtons[i].setText(LabelConstants.EMPTY);
					addonButtons[i].setDisable(true);
				}
				addonAnchorStandart.getChildren().add(addonButtons[i]);
				addonButtons[i].setLayoutX(addonButtonLayoutX + LayoutConstants.S_ADDON_MODIFIER);
				addonButtons[i].setLayoutY(addonTypesLayoutY);

				addonTypesLayoutY = addonTypesLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
			}
			addonAnchorStandart.setPrefHeight(addonTypesLayoutY + LayoutConstants.S_DEFUALT_MODIFIER);
		}
	}

	/**
	 * Insert the label of the selected user attribute to the cofig attribute
	 * 
	 * @param cleaningController
	 *            definition between user and config attributes
	 */
	public void refreshAttributLabel() {
		double attributeLayoutY = LayoutConstants.S_ADDON_ATTRIBUTE_LAYOUT_Y;
		double attributLabelLayoutX = LayoutConstants.S_ATTRIBUTE_LABEL_LAYOUT_X;
		userAttributes = new Label[configObject.getAttributeNames().size()];

		for (int i = 0; i < userAttributes.length; i++) {
			userAttributes[i] = new Label();
			userAttributes[i].setText(dCSettings.getAttributes()[i]);
			if (userAttributes[i].getText() != null) {
				if (userAttributes[i].getText().length() > attributLabelLayoutX) {
					attributLabelLayoutX = userAttributes[i].getText().length();
				}
			}
			definitionAnchorStandart.getChildren().add(userAttributes[i]);

			userAttributes[i].setLayoutX(attributLayoutX);
			userAttributes[i].setLayoutY(attributeLayoutY);

			attributeLayoutY = attributeLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
		}
		definitionAnchorStandart.setPrefHeight(attributeLayoutY + LayoutConstants.S_DEFUALT_MODIFIER);
	}

	/**
	 * Clear the scroll pane.
	 */
	public void clearScrollPane() {
		definitionAnchorStandart.getChildren().clear();
	}

	/**
	 * Insert the labels for the type of the attributes.
	 * 
	 * @param cleaningController
	 *            holds the definition between attribute and type
	 */
	public void refreshTypeLabel() {
		double typLayoutY = LayoutConstants.S_ADDON_ATTRIBUTE_LAYOUT_Y;
		double labelLength = LayoutConstants.S_ATTRIBUTE_LABEL_LAYOUT_X;
		types = new Label[configObject.getAttributeNames().size()];

		for (int i = 0; i < types.length; i++) {
			types[i] = new Label();
			types[i].setText(dCSettings.getTypes()[i]);
			if (types[i].getText() != null) {
				if (types[i].getText().length() > labelLength) {
					labelLength = types[i].getText().length();
				}
			}
			definitionAnchorStandart.getChildren().add(types[i]);
			types[i].setLayoutX(typLayoutX);
			types[i].setLayoutY(typLayoutY);

			typLayoutY = typLayoutY + LayoutConstants.S_DEFUALT_MODIFIER;
			refreshTypeCombo(types[i], i);
		}
		definitionAnchorStandart.setPrefHeight(typLayoutY + LayoutConstants.S_DEFUALT_MODIFIER);
	}

	/**
	 * Insert the format options for the selected type.
	 * 
	 * @param cleaningController
	 *            definition of the attributes type
	 */
	public void refreshTypeCombo(Label type, int index) {
		if (type.getText() != null) {
			if (type.getText().equals(Constants.TYPE_STRING)) {
				attributeTypes[index].setValue(Constants.TYPE_STRING);
			} else if (type.getText().equals(Constants.TYPE_INT)) {
				attributeTypes[index].setValue(Constants.TYPE_INT);
			} else if (type.getText().equals(Constants.TYPE_DATE)) {
				attributeTypes[index].getItems().addAll(Constants.DATE_YMD_DASHES, Constants.DATE_MDY_SLASHES,
						Constants.DATE_EMDY_COMMAS, Constants.DATE_YMD_LOW_DASHES, Constants.DATE_YWU_DASHES,
						Constants.DATE_DMY_DOTS);
			}
		}
	}

	/**
	 * --------------------------------------------------Functions--------------------------------------------------
	 */

	/**
	 * Load the user data in the tableview.
	 * 
	 * @param hideTableView
	 *            True, if the user want to hide the data.
	 */
	public void loadTableView(boolean hideTableView) {
		if (dynamicTableView.getPath() != null) {
			if (!hideTableView) {
				dynamicTableView.update(dataTableStandartisierung, dynamicTableView.getData());
			}
		}
	}

	/**
	 * Map the name of the attribute to the type of it. The function will prepare
	 * the creation of an invalid tableview.
	 * 
	 * @return The attribute mapped to the type.
	 */
	public LinkedHashMap<String, String> mapAttributeToType() {
		LinkedHashMap<String, String> attributeToType = new LinkedHashMap<String, String>();
		for (int i = 0; i < types.length; i++) {
			if (userAttributes[i].getText() != null) {
				attributeToType.put(userAttributes[i].getText(), types[i].getText());

				// if the user has not selected a special date format
				if (types[i].getText().equals(Constants.TYPE_DATE)) {
					if (attributeTypes[i].getValue() == null) {
						handleNonSelectedDateFormat();
						return null;
					}
				}
			}
		}
		return attributeToType;
	}

	/**
	 * Get a special format of an attribute if selected.
	 * 
	 * @return List with a special format to all attributes or null if no special
	 *         format exists.
	 */
	public ArrayList<String> specialFormat() {
		ArrayList<String> format = new ArrayList<String>();
		for (int i = 0; i < attributeTypes.length; i++) {
			if (attributeTypes[i].getValue() == null) {
				format.add(null);
			} else if (!attributeTypes[i].getValue().equals("")) {
				format.add(attributeTypes[i].getValue());
			} else {
				format.add(null);
			}
		}
		return format;
	}

	// -------------------------------------------------------Settings-------------------------------------------------------

	/**
	 * Set the attributes in the settings to the comboboxes.
	 */
	public void setAttributeValuesFromSettings() {
		for (int i = 0; i < attributeTypes.length; i++) {
			if (standardSettings != null) {
				LinkedHashMap formatSettings = standardSettings.getFormat();
				if (formatSettings != null) {
					if (i < formatSettings.size()) {
						List<String> keys = new ArrayList<String>(formatSettings.keySet());
						List<String> values = new ArrayList<String>(formatSettings.values());
						if (keys.get(i) != null) {
							if (keys.get(i).equals(dCSettings.getTypes()[i])) {
								attributeTypes[i].setValue(values.get(i));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Save the datatype the user selected as settings.
	 */
	public void saveTypeSettings() {
		if (standardSettings == null) {
			standardSettings = new StandardizationSettings();
		}
		LinkedHashMap<String, String> typeFormat = new LinkedHashMap<>();
		for (int i = 0; i < types.length; i++) {
			if (attributeTypes[i].getValue() != null) {
				typeFormat.put(types[i].getText(), attributeTypes[i].getValue());
			}
		}
		standardSettings.setFormat(typeFormat);
	}

	/**
	 * Save the restrictions for every attribute.
	 */
	public void saveRestrictions() {
		HashMap<String, StringRestrictionSettings> stringSettings = new HashMap<>();
		HashMap<String, IntegerRestrictionSettings> intSettings = new HashMap<>();
		HashMap<String, DateRestrictionSettings> dateSettings = new HashMap<>();
		for (int i = 0; i < allAttributeSettings.size(); i++) {
			for (Object o : allAttributeSettings.get(i).values()) {
				if (o.getClass() == StringPropertySettings.class) {
					StringPropertySettings property = (StringPropertySettings) o;
					Integer max = parseIntegerFromString(property.getMaxLength());
					Integer min = parseIntegerFromString(property.getMinLength());
					checkListNotNull((ArrayList<String>) property.getAllowedChars());
					ArrayList<String> allowed = checkListNotNull((ArrayList<String>) (property.getAllowedChars()));
					ArrayList<String> change = checkListNotNull((ArrayList<String>) property.getIllegalChars());
					ArrayList<String> changeWith = checkListNotNull((ArrayList<String>) property.getAlternativChars());

					StringRestrictionSettings restrict = new StringRestrictionSettings();
					restrict.setMaximum(max);
					restrict.setMinimum(min);
					restrict.setRemoveSpace(property.isRemoveSpaces());
					restrict.setRemoveWhitespaces(property.isRemoveWhitespaces());
					restrict.setOnlyAllowedStrings(allowed);
					restrict.setExchange(change);
					restrict.setExchangeWith(changeWith);
					stringSettings.put((String) allAttributeSettings.get(i).keySet().toArray()[0], restrict);
				} else if (o.getClass() == IntegerPropertySettings.class) {
					IntegerPropertySettings property = (IntegerPropertySettings) o;
					IntegerRestrictionSettings restrict = new IntegerRestrictionSettings();
					restrict.setMaximum(checkIntegerNotNull(property.getMaximum()));
					restrict.setMinimum(checkIntegerNotNull(property.getMinimum()));
					restrict.setAllowNegative(property.isNegativInt());
					intSettings.put((String) allAttributeSettings.get(i).keySet().toArray()[0], restrict);
				} else if (o.getClass() == DatePropertySettings.class) {
					DatePropertySettings property = (DatePropertySettings) o;
					DateRestrictionSettings restrict = new DateRestrictionSettings();
					restrict.setMaximum(checkDateNotNull(property.getMaximum()));
					restrict.setMinimum(checkDateNotNull(property.getMinimum()));
					restrict.setAllowNegative(property.isAllowFuture());
					dateSettings.put((String) allAttributeSettings.get(i).keySet().toArray()[0], restrict);
				}
			}
		}
		standardSettings.setStringSettings(stringSettings);
		standardSettings.setIntegerSettings(intSettings);
		standardSettings.setDateSettings(dateSettings);
	}

	/**
	 * Try to parse the value of an String to an Integer. Returns null if the parse
	 * is not possible.
	 * 
	 * @param value
	 *            that should be parsed.
	 * @return The Integer value of a string or null if the parse fails.
	 */
	public Integer parseIntegerFromString(String value) {
		Integer parse = null;
		try {
			parse = Integer.valueOf(value);
		} catch (Exception e) {
			parse = null;
		}
		return parse;
	}

	/**
	 * Check if an list has no entries.
	 * 
	 * @param list
	 *            that is checked
	 * @return Content of the list if it is not null.
	 */
	public ArrayList<String> checkListNotNull(ArrayList<String> list) {
		if (list == null) {
			return null;
		} else if (list.size() == 0) {
			return null;
		} else {
			return list;
		}
	}

	/**
	 * Check if an integer is not null.
	 * @param value that should be checked
	 * @return the integer or null 
	 */
	public Integer checkIntegerNotNull(Integer value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	/**
	 * Check if a date is null.
	 * @param date that should be checked
	 * @return the date or null
	 */
	public Date checkDateNotNull(Date date) {
		if (date == null) {
			return null;
		} else {
			return date;
		}
	}

	// --------------------------------------------------EventHandling--------------------------------------------------

	/**
	 * When the "Next" button is clicked, the tab pane switch to the encoding
	 * tab. The selected settings will be saved.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleContinueButton(ActionEvent event) {
		saveTypeSettings();
		saveRestrictions();
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(3);
	}

	/**
	 * By clicking on the save button the data will be saved in the file system
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
	 * Check selected values of the data for the selected type and show invalid rows
	 * in another table view.
	 * 
	 * @param event
	 *            Action event of the button.
	 */
	@FXML
	private void handleCheckButton(ActionEvent event) {
		LinkedHashMap<String, String> attributToType = mapAttributeToType();
		if (attributToType != null) {
			dynamicTableView.createInvalidTableView(invalidData, mapAttributeToType(), specialFormat());
			dynamicTableView.updateRestrictions(dataTableStandartisierung);
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
		selectionModel.select(1);
	}

	/**
	 * Set an action event if the restriction button is clicked.
	 * 
	 * @param button
	 *            the button for the event
	 * @param mode
	 *            To select the right restriction handling.
	 * @param mode
	 *            To select format type for date.
	 */
	private void handleRestriction(Button button, int mode, String attributeName, String type) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (mode == 1) {
					handleStringRestriction(attributeName);
				} else if (mode == 2) {
					handleIntegerRestriction(attributeName);
				} else if (mode == 3) {
					handleDateRestriction(attributeName, type);
				}
			}
		});
	}

	/**
	 * Opens the dialog for string restriction.
	 * 
	 * @param attributeName
	 *            The attribute to set restrictions
	 */
	private void handleStringRestriction(String attributeName) {
		boolean isClicked;
		isClicked = main.showStringRestrictionDialog(attributeName, dataTableStandartisierung);
		allAttributeSettings.getInstance();
	}

	/**
	 * Opens the dialog for integer restriction.
	 * 
	 * @param attributeName
	 *            The attribute to set restrictions
	 */
	private void handleIntegerRestriction(String attributeName) {
		boolean isClicked;
		isClicked = main.showIntegerRestrictionDialog(attributeName, dataTableStandartisierung);
		allAttributeSettings.getInstance();
	}

	/**
	 * Opens the dialog for date restriction.
	 * 
	 * @param attributeName
	 *            The attribute to set restrictions
	 */
	private void handleDateRestriction(String attributeName, String formatType) {
		boolean isClicked;
		isClicked = main.showDateRestrictionDialog(attributeName, formatType, dataTableStandartisierung);
		allAttributeSettings.getInstance();
	}

	/**
	 * If a necessary special format for a data type is not selected a error dialog
	 * will be open.
	 */
	private void handleNonSelectedDateFormat() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Missing selection for date format");
		alert.setContentText("Please select a special format for date values.");

		alert.showAndWait();
	}

	/**
	 * --------------------------------------------------Getter&Setter--------------------------------------------------
	 */

	/**
	 * Get the attributes with the user attribute names.
	 * @return all attributes
	 */
	public Label[] getUserAttributes() {
		return userAttributes;
	}

	/**
	 * Set the main.
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;
	}

	/**
	 * Set the tab pane.
	 * @param tabPane
	 */
	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	/**
	 * Set the settings.
	 * @param settings
	 */
	public void setSettings(StandardizationSettings settings) {
		this.standardSettings = settings;
	}

	/**
	 * Get the standardization settings.
	 * @return the settings
	 */
	public StandardizationSettings getSettings() {
		return standardSettings;
	}

	/**
	 * Set the data cleaning controller.
	 * @param dCController
	 */
	public void setDCController(DataCleaningController dCController) {
		this.dCController = dCController;
	}

	/**
	 * Set the data cleaning settings.
	 * @param dCSettings
	 */
	public void setDCSettings(DataCleaningSettings dCSettings) {
		this.dCSettings = dCSettings;
	}
}