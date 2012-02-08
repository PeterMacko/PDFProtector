package sk.project22.pmacko;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIMetods 
{
	/**
	 * This method show filechooser for user and than return selected file as File object
	 * 
	 * @param mainWindow - parental window
	 * @return - selected File
	 * @throws NoFileLoadedException - if no file was loaded or some error occure
	 */
	public static File getFileFromChooser(JFrame mainWindow) throws NoFileLoadedException
	{
		JFileChooser c = getPdfFileChooser();
    	
    	if (c.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION)
    	{
    		try
    		{
				return c.getSelectedFile().getCanonicalFile();
			}
    		catch (IOException e) 
    		{
				throw new NoFileLoadedException();
			}
    	}
    	throw new NoFileLoadedException();
	}
	
	/**
	 * This method return passworld from user. If some passworld is inserted to method
	 * then user give message of wrong passworld and user can try again
	 * 
	 * @param password - old passworld or null
	 * @return passworld for pdf file
	 * @throws NoPassLoadedException - if no passworld was inserted from user
	 */
	public static String getPdfPass(String password) throws NoPassLoadedException
	{
		if(password == null)
		{
			if((password = passInputDialog())== null)
			{
				throw new NoPassLoadedException();
			}
		}
		else
		{
			if((password = badPassInputDialog())== null)
			{
				throw new NoPassLoadedException();
			}
		}
		return password;
	}
	
	/**
	 * This method show input dialog for passworld and after user submit return value
	 * 
	 * @return - entered passworld by user
	 */
	private static String passInputDialog()
	{
		return JOptionPane.showInputDialog(
			null,
			"Dokument je chr·nen˝ heslom. Pre Ôalöie pokraËoanie je toto heslo nutnÈ zadaù: ",
			"Zadajte Heslo",
			JOptionPane.INFORMATION_MESSAGE
			);
	}
	
	/**
	 * This method has same function as passInputDialog, 
	 * but message in input dialog show message with warning
	 * 
	 * @see #passInputDialog()
	 * @return - entered passworld by user
	 */
	private static String badPassInputDialog()
	{
		return JOptionPane.showInputDialog(
			null,
			"Zadali ste neplatnÈ heslo. Sk˙ste znova:",
			"Zadajte Heslo",
			JOptionPane.WARNING_MESSAGE
		);	
	}
	
	/**
	 * method return file chooser with setten filter for pdf files
	 * 
	 * @return JFileChooser for pdffiles
	 */
	public static JFileChooser getPdfFileChooser()
	{
		JFileChooser chooser = new JFileChooser();
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF file", "pdf");
    	chooser.setFileFilter(filter);
    	
    	return chooser;
	}
	
	
	public static String getText(JTextField textField)
	{
		if(textField instanceof JPasswordField)
			return new String(((JPasswordField) textField).getPassword());
		else
			return textField.getText();
	}
	
	/**
	 * method check if file getten form user is PDF file or no
	 * 
	 * @param f - file getten from user
	 * @return if it is pdf true else false
	 */
	public static boolean checkPDFFile(File f)
	{
		return "Adobe Acrobat Document".equals(new JFileChooser().getTypeDescription(f));
	}
	
	/**
	 * this method allow you to set window to center of another window. when zou put null as a second argument
	 * method get screen as a relative to window and align window to center of screen. When zou put as a second argument
	 * another window method give the first window to center of the second one.
	 * 
	 * @param window - changing its position
	 * @param relativeTo - relative object for changing this position
	 */
	public static void setLocationRelativeTo(Window window, JFrame relativeTo)
	{
		int windowHeight = window.getHeight();
		int windowWidth = window.getWidth();
		int relativeToHeight = 0,relativeToWidth = 0,relativeToLeft = 0,relativeToTop = 0;
		
		if(relativeTo == null)
		{
			relativeToLeft = relativeToTop = 0;
			

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] gs = ge.getScreenDevices();
			
			DisplayMode dm = gs[0].getDisplayMode();
		    relativeToHeight = dm.getHeight();
		    relativeToWidth = dm.getWidth();
		}
		else
		{
			relativeToHeight = relativeTo.getHeight();
			relativeToWidth  = relativeTo.getWidth();
			
			Point location = relativeTo.getLocation();
			relativeToLeft = (int) location.getX();
			relativeToTop  = (int) location.getY();
		}
		
		window.setLocation( 
				(relativeToWidth-windowWidth)/2 + relativeToLeft,
				(relativeToHeight-windowHeight)/2 + relativeToTop
		);
	}
}
