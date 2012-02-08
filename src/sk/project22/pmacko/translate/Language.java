package sk.project22.pmacko.translate;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import sk.project22.pmacko.Settings;

public class Language
{
	private static final Logger logger = Logger.getLogger(Language.class.getName());
	public static final String pathToLanguage = String.format("settings%slanguages%s.xml",File.separator,File.separator+"%s");
	public static Language actual;
	public String name;
	public String shortcut;
	public String cancel;
	public String sourceFile;
	public String destFile;
	public String privateKey;
	public String publicKey;
	public String printing;
	public String contentCopy;
	public String lessQualityPrint;
	public String documentModificating;
	public String pageBookmarkRotation;
	public String mobileDevices;
	public String modifyAnnotations;
	public String fillIn;
	public String allowEditing;
	public String hidePasses;
	public String secureDocument;
	public String metaDatesEdit;
	public String incorrectFile;
	public String error;
	public String missingFile;
	public String errorReadingFile;
	public String yes;
	public String no;
	public String cancelOperation;
	public String rewriteFileMessage;
	public String rewriteFileTitle;
	public String errorFileNotFound;
	public String title;
	public String subject;
	public String autor;
	public String keywords;
	public String metaDatesEditTitle;
	public String save;
	public String noPasswordGiven;
	
	private Language(String shortcut, String name)
	{
		this.shortcut = shortcut;
		this.name = name;
	}
	
	public static Language loadLanguage(String file) throws FileException
	{
		try 
		{
			Document langXML = XMLHelper.openFile(file);
			Language lang = new Language(
				langXML.getDocumentElement().getAttribute("sc"),
				langXML.getDocumentElement().getAttribute("name")
			);
			lang.cancel 					= XMLHelper.getValueById(langXML, "cancel");
			lang.sourceFile 				= XMLHelper.getValueById(langXML, "sourceFile");
			lang.destFile 					= XMLHelper.getValueById(langXML, "destFile");
			lang.privateKey 				= XMLHelper.getValueById(langXML, "privateKey");
			lang.publicKey					= XMLHelper.getValueById(langXML, "publicKey");
			lang.printing 					= XMLHelper.getValueById(langXML, "printing");
			lang.contentCopy 				= XMLHelper.getValueById(langXML, "contentCopy");
			lang.lessQualityPrint 			= XMLHelper.getValueById(langXML, "lessQualityPrint");
			lang.documentModificating 		= XMLHelper.getValueById(langXML, "documentModificating");
			lang.pageBookmarkRotation 		= XMLHelper.getValueById(langXML, "pageBookmarkRotation");
			lang.mobileDevices 				= XMLHelper.getValueById(langXML, "mobileDevices");
			lang.modifyAnnotations 			= XMLHelper.getValueById(langXML, "modifyAnnotations");
			lang.fillIn 					= XMLHelper.getValueById(langXML, "fillIn");
			lang.allowEditing 				= XMLHelper.getValueById(langXML, "allowEditing");
			lang.hidePasses 				= XMLHelper.getValueById(langXML, "hidePasses");
			lang.secureDocument 			= XMLHelper.getValueById(langXML, "secureDocumet");
			lang.metaDatesEdit 				= XMLHelper.getValueById(langXML, "metaDatesEdit");
			lang.metaDatesEditTitle 		= XMLHelper.getValueById(langXML, "metaDatesEditTitle");
			lang.incorrectFile 				= XMLHelper.getValueById(langXML, "incorrectFile");
			lang.error 						= XMLHelper.getValueById(langXML, "error");
			lang.missingFile 				= XMLHelper.getValueById(langXML, "missingFile");
			lang.errorReadingFile 			= XMLHelper.getValueById(langXML, "errorReadingFile");
			lang.yes 						= XMLHelper.getValueById(langXML, "yes");
			lang.no 						= XMLHelper.getValueById(langXML, "no");
			lang.cancelOperation 			= XMLHelper.getValueById(langXML, "cancelOperation");
			lang.rewriteFileMessage 		= XMLHelper.getValueById(langXML, "rewriteFileMessage");
			lang.rewriteFileTitle 			= XMLHelper.getValueById(langXML, "rewriteFileTitle");
			lang.errorFileNotFound 			= XMLHelper.getValueById(langXML, "errorFileNotFound");
			lang.title 						= XMLHelper.getValueById(langXML, "title");
			lang.subject 					= XMLHelper.getValueById(langXML, "subject");
			lang.autor 						= XMLHelper.getValueById(langXML, "autor");
			lang.keywords 					= XMLHelper.getValueById(langXML, "keywords");
			lang.save 						= XMLHelper.getValueById(langXML, "save");
			lang.noPasswordGiven 			= XMLHelper.getValueById(langXML, "noPasswordGiven");
			
			return lang;
		} 
		catch (Exception e)
		{
			throw new FileException(e.getMessage());
		}
	}
	
	public static Language getDefaultLanguage()
	{
		Language lang = new Language("en", "English");
		
		lang.cancel = "Cancel";
		lang.sourceFile = "Source file";
		lang.destFile = "Destination file";
		lang.privateKey = "private key";
		lang.publicKey = "public key";
		lang.printing = "printing";
		lang.contentCopy = "content copy";
		lang.lessQualityPrint = "less quality printing";
		lang.documentModificating = "document modificating";
		lang.pageBookmarkRotation = "page and bookmark rotation";
		lang.mobileDevices = "mobile devices";
		lang.modifyAnnotations = "annotations modifying";
		lang.fillIn = "fill in forms";
		lang.allowEditing = "allow editing";
		lang.hidePasses = "hide passes";
		lang.secureDocument = "secure documet";
		lang.metaDatesEdit = "edit metadates";
		lang.metaDatesEditTitle = "Metadates Editor";
		lang.incorrectFile = "Incorrect or not existing file.";
		lang.error = "Error";
		lang.missingFile = "Missing file.";
		lang.errorReadingFile = "Error reading file.";
		lang.yes = "yes";
		lang.no = "no";
		lang.cancelOperation = "Cancel operation.";
		lang.rewriteFileMessage = "File %s already exists. Would you like to rewrite it?";
		lang.rewriteFileTitle = "File already exists";
		lang.errorFileNotFound = "File not found.";
		lang.subject = "Title";
		lang.subject = "Subject";
		lang.autor = "Autor";
		lang.keywords = "Keywords";
		lang.save = "Save";
		lang.noPasswordGiven = "No password given for secured file.";
		return lang;
	}

	public static void loadActualLanguage()
	{
		if(Settings.actual.language == null || Settings.actual.language == "")
		{
			Settings.actual.language = System.getProperty("user.language").toLowerCase();
			try
			{
				Settings.actual.editSettings("language", Settings.actual.language);
			}
			catch (ParserConfigurationException | IOException | TransformerException e)
			{
				logger.warning(e.getMessage());
			}
		}
		try
		{
			actual = Language.loadLanguage(String.format(Language.pathToLanguage, Settings.actual.language));
		}
		catch (FileException e)
		{
			logger.warning("No language file found using english dictationary");
			actual = Language.getDefaultLanguage();
		}
		finally
		{
			logger.info("Language was set: " + actual.name);
		}
	}
}
