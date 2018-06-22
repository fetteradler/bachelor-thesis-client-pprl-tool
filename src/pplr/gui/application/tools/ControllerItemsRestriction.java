package pplr.gui.application.tools;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

/**
 * Restriction for GUI controls.
 * 
 * @author CM
 */
public class ControllerItemsRestriction {

	/**
	 * Restrict a text field input on only one letter.
	 * 
	 * @param textField
	 *            that should be restricted
	 * @return a restriction on the text field
	 */
	public static Change restrictTextFieldOnOneLetter(TextField textField) {

		textField.setTextFormatter(new TextFormatter<String>((Change change) -> {
			String newText = change.getControlNewText();
			if (newText.length() > 1) {
				return null;
			} else {
				return change;
			}
		}));

		return null;
	}

	/**
	 * Restrict a text field input only on integer.
	 * 
	 * @param textField
	 *            that should be restricted
	 * @return a restriction on the text field
	 */
	public static Change restrictTextFieldOnInteger(TextField textField) {

		textField.setTextFormatter(new TextFormatter<String>((Change change) -> {
			String newText = change.getControlNewText();
			if (isInteger(newText)) {
				return change;
			} else if (newText.isEmpty()) {
				return change;
			} else {
				return null;
			}
		}));

		return null;

	}

	/**
	 * Check if an string is an integer.
	 * 
	 * @param s
	 *            string that should be tested.
	 * @return True, if the string is an integer.
	 */
	public static boolean isInteger(String s) {
		boolean isValidInteger = false;
		try {
			Integer.parseInt(s);
			isValidInteger = true;
		} catch (NumberFormatException ex) {

		}
		return isValidInteger;
	}
}
