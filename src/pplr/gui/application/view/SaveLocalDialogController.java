package pplr.gui.application.view;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import pplr.gui.application.configuration.FilePathConstants;

/**
 * Controller of the file chooser dialog for saving data.
 * 
 * @author CM
 *
 */
public class SaveLocalDialogController {

	/**
	 * Initializes controller class. Is automatically called after the FXML file has
	 * been loaded.
	 */
	@FXML
	private void initialize() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				FilePathConstants.EXTENSION_FILLER_FIRST, FilePathConstants.EXTENSION_FILLER_SECOND);
		fileChooser.getExtensionFilters().add(extFilter);
	}
}
