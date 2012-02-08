package sk.project22.pmacko;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

import sk.project22.pmacko.translate.Language;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;

/**
 * methods for helping to Main window
 * This class create brdge between GUI and logic of program
 * 
 * @author Peter Macko
 *
 */
public class GUIHelper
{
	private static final Logger logger = Logger.getLogger(GUIHelper.class.getName());
	private MainWindow mainWindow;
	private PDFCreator creator;
	
	/**
	 * Creating of GUIhelper nedd to get GUI Window class
	 * 
	 * @param mainWindow - GUI window of program
	 */
	public GUIHelper(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

	/**
	 * metod for setting look and feel
	 * metod set look and feel in depend on platform
	 */
	public static void setMyLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			logger.info("Look and Feel was set : " + UIManager.getSystemLookAndFeelClassName());
		}	
		catch (UnsupportedLookAndFeelException ex)
		{
			logger.severe("lookAndFeel not supported");
		}
		catch (ClassNotFoundException ex)
		{
			logger.severe("lookAndFeel not found");
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			logger.severe("problem with setting look and feel");
		}
	}
	
	/**
	 * show the confirm if replace file or not
	 * 
	 * @param fileName - name of file in confirm
	 * @return - selected statement of user
	 */
	public static int fileExistsConfirm(String fileName)
	{
		Object[] options = {Language.actual.yes, Language.actual.no, Language.actual.cancelOperation};
		
		return JOptionPane.showOptionDialog(
			null,
		    String.format(Language.actual.rewriteFileMessage, fileName),
		    Language.actual.rewriteFileTitle,
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[0]);
	}
	
	public void openFile(File srcPath)
	{
		try 
		{
			if(!srcPath.exists() || !srcPath.isFile() || !srcPath.canRead() || !"Adobe Acrobat Document".equals(new JFileChooser().getTypeDescription(srcPath))){
				throw new IOException("Wrong file type");
			}
			boolean opened = false;
			String password = null;
			
			while(!opened)
			{
				try 
	    		{
					if(password == null)
						creator = new PDFCreator(srcPath);
					else
						creator = new PDFCreator(srcPath, password);
					
					opened = true;

					mainWindow.fillFile(srcPath.getCanonicalPath());
				}
	    		catch(BadPasswordException ex)
	    		{
    				password = GUIMetods.getPdfPass(password);
				}
			}
		}
		catch (IOException ex) 
		{
			logger.log(Level.SEVERE, "Error while reading file", ex);
			JOptionPane.showMessageDialog(
				mainWindow,
				Language.actual.incorrectFile,
				Language.actual.error,
				JOptionPane.ERROR_MESSAGE
			);
		}
		catch (NoPassLoadedException ex)
		{
			logger.log(Level.WARNING, "Error while reading file. No passwoed given.");
			JOptionPane.showMessageDialog(
				mainWindow,
				Language.actual.noPasswordGiven,
				Language.actual.error,
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
	/**
	 * This function open the file witch name was getten from chooser
	 * by function getFileFromChooser. Then try to open file. If the 
	 * file is protected by pass function get the pass from getPdfPass()
	 * function
	 * 
	 * @see getPdfPass
	 * @see getFileFromChooser
	 * @return file name of open file
	 * @throws NoFileLoadedException - if user cancel selection
	 */
	public void openFile()
	{
		File srcFile;
		try {
			srcFile = GUIMetods.getFileFromChooser(mainWindow);
			
			openFile(srcFile);
		}
		catch (NoFileLoadedException e) 
		{
			logger.log(Level.WARNING, "No file selected");
		}	
	}
	
	public void setDestinationFile()
	{
		
		try {
			File srcFile;
			srcFile = GUIMetods.getFileFromChooser(mainWindow);
			mainWindow.setDestinationFile(srcFile.getCanonicalPath());
		}
		catch (NoFileLoadedException e) 
		{
			logger.log(Level.SEVERE, "No file was selected", e);
		} catch (IOException e)
		{
			logger.log(Level.SEVERE, "error while reading file", e);
		}	
	}
	
	public void saveAndSecuredFile(String fileName, String publicPass, String privatePass, int permissions) throws IOException
	{
		try 
    	{	
			creator.createStamper(fileName);
    		
			if(permissions != 0 && !"".equals(privatePass))
			{
				creator.setStamperEncryption(publicPass.getBytes(), privatePass.getBytes(), permissions);
			}
			
			creator.closeStamper();
			
			logger.info("file saving is complet");
		}
    	catch (FileNotFoundException ex) 
		{
    		logger.log(Level.SEVERE,"File not found", ex);
			JOptionPane.showMessageDialog(null,
				    Language.actual.errorFileNotFound,
				    Language.actual.error,
				    JOptionPane.ERROR_MESSAGE);
		}
    	catch (DocumentException ex)
    	{
    		logger.log(Level.SEVERE, "Error of inserted document, see exception stack Trace", ex);
			JOptionPane.showMessageDialog(null,
				    "Dokument nie je moûnÈ spracovaù.",
				    "Chyba spracovania",
				    JOptionPane.ERROR_MESSAGE);
		}
    	catch (IOException ex)
    	{
    		logger.log(Level.SEVERE, "Error while writing to file", ex);
			JOptionPane.showMessageDialog(null,
					"Chyba z·pisu do s˙boru",
					"Chyba z·pisu",
					JOptionPane.ERROR_MESSAGE);
		}
    	finally
    	{
			creator.reloadFile();
			logger.info("file was refreshed");	
    	}
	}
	
	public void editFileMeta()
	{
		HashMap<String, String> meta = creator.getMeta();
    	
    	JTextField titleTF = new JTextField();
    	if (meta.containsKey(new String("Title")))
    		titleTF.setText(meta.get(new String("Title")).toString());
    	
    	titleTF.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
    	
    	JTextField subjectTF = new JTextField();
    	if (meta.containsKey(new String("Subject")))
    		subjectTF.setText(meta.get(new String("Subject")).toString());
    	
    	subjectTF.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
    	
    	
    	JTextField authorTF = new JTextField();
    	if (meta.containsKey(new String("Author")))
    		authorTF.setText(meta.get(new String("Author")).toString());
    	
    	authorTF.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
    	
    	JTextArea keywordsTF = new JTextArea(6,5);
    	keywordsTF.setFont(new Font("Verdana", Font.PLAIN, 10));
    	JScrollPane keywordsJSP = new JScrollPane(keywordsTF);
    	if (meta.containsKey(new String("Keywords")))
    		keywordsTF.setText(meta.get(new String("Keywords")).toString());
    	
    	keywordsJSP.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
    	
    	final JComponent[] inputs = new JComponent[] {
    	                new JLabel(Language.actual.title),
    	                titleTF,
    	                new JLabel(Language.actual.subject),
    	                subjectTF,
    	                new JLabel(Language.actual.autor),
    	                authorTF,
    	                new JLabel(Language.actual.keywords),
    	                keywordsJSP
    	};
    	Object[] options = {Language.actual.save, Language.actual.cancel};
    	int response = JOptionPane.showOptionDialog(null,
    			inputs,
    			Language.actual.metaDatesEditTitle,
    		    JOptionPane.YES_NO_OPTION,
    		    JOptionPane.QUESTION_MESSAGE,
    		    null,
    		    options,
    		    options[0]);
    	
    	if(response == 0)
    	{
    		creator.updateMeta(new String("Title"),titleTF.getText());
    		creator.updateMeta(new String("Subject"),subjectTF.getText());
    		creator.updateMeta(new String("Author"),authorTF.getText());
    		creator.updateMeta(new String("Keywords"),keywordsTF.getText());
    	}
	}
}
