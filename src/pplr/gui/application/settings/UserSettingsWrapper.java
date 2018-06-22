package pplr.gui.application.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap the user settings.
 * Settings will be saved to XML file.
 */
@XmlRootElement(namespace = "pplr.gui.application.settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSettingsWrapper {

	/** Select data setting. */
	@XmlElement(name = "selectData")
	private SelectDataSettings selectSettings;
	
	/** Data cleaning settings. */
	@XmlElement(name = "dataCleaning")
	private DataCleaningSettings dataCleaningSettings;
	
	/** Standardization settings. */
	@XmlElement(name = "standardization")
	private StandardizationSettings standardizationSettings;
	
	/** Encoding settings. */
	@XmlElement(name = "codierung")
	private CodierungSettings codierungSettings;
	
	/*
	 * ------------------------------ Getter & Setter ------------------------------
	 */
	
	/**
	 * Get the select data settings.
	 * @return select data settings
	 */
	public SelectDataSettings getSelectSettings() {
		return selectSettings;
	}
	
	/**
	 * Set the select data settings.
	 * @param selectSettings select data settings.
	 */
	public void setSelectSettings(SelectDataSettings selectSettings) {
		this.selectSettings = selectSettings;
	}
	
	/**
	 * Get the data cleaning settings.
	 * @return data cleaning settings.
	 */
	public DataCleaningSettings getDataCleaningSettings() {
		return dataCleaningSettings;
	}
	
	/**
	 * Set the data cleaning settings.
	 * @param dataCleaningSettings data cleaning settings
	 */
	public void setDataCleaningSettings(DataCleaningSettings dataCleaningSettings) {
		this.dataCleaningSettings = dataCleaningSettings;
	}
	
	/**
	 * Get the standardization settings.
	 * @return the standardization settings
	 */
	public StandardizationSettings getStandardizationSettings() {
		return standardizationSettings;
	}
	
	/**
	 * Set the standardization settings.
	 * @param standardizationSettings 
	 */
	public void setStandardizationSettings(StandardizationSettings standardizationSettings) {
		this.standardizationSettings = standardizationSettings;
	}
	
	/**
	 * Get the encoding settings.
	 * @return encoding settings
	 */
	public CodierungSettings getCodierungSettings() {
		return codierungSettings;
	}
	
	/**
	 * Set the encoding settings.
	 * @param codierungSettings
	 */
	public void setCodierungSettings(CodierungSettings codierungSettings) {
		this.codierungSettings = codierungSettings;
	}
}
