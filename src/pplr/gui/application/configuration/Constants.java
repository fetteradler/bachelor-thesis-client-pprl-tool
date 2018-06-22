package pplr.gui.application.configuration;

/**
 * Contains constants for the data types and format.
 * @author CM
 *
 */
public abstract class Constants {
	
	public static final String TYPE_STRING = "String";
	
	public static final String TYPE_INT = "Integer";
	
	public static final String TYPE_DATE = "Date";
	
	public static final String DC_PROMT_ATTRIBUTE = "Attribute";
	
	public static final String DC_PROMT_TYP = "Typ";
	
	/* ------------------------------ Date formats ------------------------------ */
	
	public static final String DATE_YMD_DASHES = "YYYY-MM-DD";
	public static final String DATE_MDY_SLASHES = "MM/DD/YYYY";
	public static final String DATE_EMDY_COMMAS = "EEE, MMM d, ''yy";
	public static final String DATE_YMD_LOW_DASHES = "yyyy-MM-dd";
	public static final String DATE_YWU_DASHES = "YYYY-'W'ww-u";
	public static final String DATE_DMY_DOTS = "dd.MM.yyyy";
	
	/* ------------------------------ Pattern handling ------------------------------ */
	
	public static final String PATTERN_PLZ_REST = "\\d{5}";
	
	public static final String PATTERN_PLZ = "PLZ";
	
	public static final String PATTERN_SPACE_WORD = "Space";
	
	public static final String PATTERN_SPACE = " ";
	
	public static final String PATTERN_DOT = ".";
	
	public static final String PATTERN_COMMA = ",";
	
	public static final String PATTERN_DASH = "-";
	
	public static final String PATTERN_SLASH = "/";
	
	public static final String PATTERN_WHITESPACES = "\\s+";
	
	public static final String PATTERN_NON_STRING = "";
	
	public static final String PATTER_DOUBBLE_BACKSLASHES_DOT = "\\.";
	
	/* ------------------------------ Encoding ------------------------------ */
	
	public static final String ENC_QGRAM = "q-gram-splitter";
	
	public static final String ENC_N_PATTERN = "n. pattern";
	
	public static final String ENC_FIRST_PATTERN = "1. pattern";
	
}
