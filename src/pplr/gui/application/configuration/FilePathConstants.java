package pplr.gui.application.configuration;

import java.io.File;

/**
 * Constants for file handling.
 * @author CM
 *
 */
public abstract class FilePathConstants {

	public static final String DATA_FILE_PATH = "lib";
	
	public static final String REMOVE_PATTERN = "config" + File.separator + "strings-to-delete.txt";
	
	public static final String DATE_PATTERN = "config" + File.separator + "datepattern.txt";
	
	public static final String SETTINGS_FILE = "config" + File.separator + "user-settings.xml";
	
	public static final String EXTENSION_FILLER_FIRST = "TXT files (*.txt)";
	
	public static final String EXTENSION_FILLER_SECOND = "*.txt";
}
