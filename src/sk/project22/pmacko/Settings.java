package sk.project22.pmacko;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;

import sk.project22.pmacko.translate.XMLHelper;

public class Settings
{
	private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
	private static final String settingsUri = String.format("settings%ssettings.xml",File.separator);
	
	public String language;
	public static Settings actual;
	
	static
	{
		actual = new Settings();
	}
	
	private Settings()
	{
		Document settDoc;
		try 
		{
			settDoc = XMLHelper.openFile(settingsUri);
			
			language = XMLHelper.getValueById(settDoc, "language");
		}
		catch (FileNotFoundException e){
			logger.warning("settings.xml not found. Using default settings.");
		}
		catch (Exception e)
		{
			logger.warning(e.getMessage());
		}
	}
	public void editSettings(String ID, String val) throws ParserConfigurationException, IOException, TransformerException
	{
		Document settDoc;
		try 
		{
			settDoc = XMLHelper.openFile(settingsUri);
		}
		catch (FileNotFoundException e)
		{
			settDoc = XMLHelper.createSettingsDocumnet();
		}
		catch (Exception e) {
			settDoc = XMLHelper.createSettingsDocumnet();
		}
		
		XMLHelper.setValueById(settDoc, ID, val);
		XMLHelper.saveToFile(settDoc,settingsUri);
	}
}
