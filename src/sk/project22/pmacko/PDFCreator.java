package sk.project22.pmacko;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class allow you to work with pdf files
 * Its alow you to loking file and setting pasworld
 * ther is too some functions for manipulating with metadates
 * 
 * @author Peter Macko
 *
 */
public class PDFCreator 
{
	private static final Logger logger = Logger.getLogger(PDFCreator.class.getName());
	private PdfReader reader;
	private HashMap<String, String> meta;
	private PdfStamper stp;
	private FileOutputStream fos;
	private String password=null;
	private String fileName;

	/**
	 * The Construcktor is inicializating PDFReader and load pdf given as agrument
	 * then get metadates from loaded pdf and set name of creator. Constructor chack 
	 * if file is prtected by passworld.
	 * 
	 * @param fileName - name of file witch will be loaded form disk
	 * @throws IOException when file path is wrong or you dont have permission to read file
	 */
	public PDFCreator(File fileName) throws IOException
	{
		reader = new PdfReader(fileName.getCanonicalPath());
		logger.info("file was loaded");
		meta = reader.getInfo();
		logger.info("meta was getted from file");
		meta.put("Creator", "PDFProtector");
		this.fileName = fileName.getCanonicalPath();
	}

	/**
	 * The Construcktor is inicializating PDFReader and load pdf given as agrument
	 * then get metadates from loaded pdf and set name of creator.
	 * This constructor allow you to give passworld and open protected PDF file.
	 * It check if you put private or public pass. If you get public pass Trow exception
	 * 
	 * @see BadPasswordException
	 * 
	 * @param fileName - name of file witch will be loaded form disk
	 * @param password - password for pdf file
	 * @throws IOException when file path is wrong or you dont have permission to read file
	 */
	public PDFCreator(File fileName, String password) throws IOException 
	{
		reader = new PdfReader(fileName.getCanonicalPath(), password.getBytes());
		
		if(!reader.isOpenedWithFullPermissions())
		{
			logger.severe("Public password was getten. Cant continue with it");
			throw new BadPasswordException(password);
		}
		
		logger.info("file was loaded with password");
		meta = reader.getInfo();
		logger.info("meta was getted from file");
		meta.put("Creator", "PDFProtector");
		this.password = password;
		this.fileName = fileName.getCanonicalPath();
	}
	
	/**
	 * method reload file after file was saved. 
	 * This is inportant because you cannot work with file witch was saved with iText Library
	 * 
	 * @throws IOException - when the path is corrupted or you dont have permission for file
	 */
	public void reloadFile() throws IOException
	{
		reader.close();
		logger.info("reader was closed");
		
		if(password == null)
			reader = new PdfReader(fileName);
		else
			reader = new PdfReader(fileName,password.getBytes());
		logger.info("reader was reloaded");
	}

	/**
	 * metod create Stamer for manipulating with PDF file
	 *
	 * @param fileName - filename of new file
	 * @throws FileNotFoundException -  if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason 
	 * @throws DocumentException - when occure error in iText library
	 * @throws IOException - when there are problems with saving file
	 */
	public void createStamper(String fileName) throws FileNotFoundException, DocumentException, IOException 
	{
		fos = new FileOutputStream(fileName);
		stp = new PdfStamper(reader, fos);
		stp.setMoreInfo(meta);
	}
	
	/**
	 * this method allow you to lock pdf file, as a arguments its
	 * take public and privat key. There you can set permission
	 * 
	 * @param publicPass - public key of file allow parly manipulation with file
	 * @param privatePass - private key of file allow yo to full acces to file
	 * @param permissions - perrmisions for public key
	 * @throws DocumentException - when occure error in iText library
	 */
	@SuppressWarnings("deprecation")
	public void setStamperEncryption(byte[] publicPass, byte[] privatePass,
			int permissions) throws DocumentException
	{
		if(stp == null) throw new NullPointerException("Stamper is not create)d");
		stp.setEncryption(
				publicPass, privatePass,
				permissions,
				PdfWriter.STRENGTH128BITS);
	}
	
	/**
	 * after you protect file this method close stamer an output stream
	 * inportant for flash buffer
	 * 
	 * @throws DocumentException - problem with closing stamer
	 * @throws IOException - problem with closing output
	 */
	public void closeStamper() throws DocumentException, IOException
	{
		stp.close();
		fos.close();
	}
	
	/**
	 * actualizate meta date if stamer is createt its update meta dates of stamper
	 * 
	 * @param name - name of meta date value
	 * @param value - meta date value
	 */
	public void updateMeta(String name, String value)
	{
		meta.put(name,value);
		if(stp != null)
		{
			stp.setMoreInfo(meta);
		}
	}
	
	/**
	 * getting meta date object for showing infos to user
	 * 
	 * @return meta date
	 */
	public HashMap<String, String> getMeta()
	{
		return meta;
	}
}
