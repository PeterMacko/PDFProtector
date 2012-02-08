package sk.project22.pmacko;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.transform.TransformerException;

import sk.project22.pmacko.translate.Language;

import com.itextpdf.text.pdf.PdfWriter;
/**
 * Class for show the main window
 * 
 * @author Peter Macko
 */
public class MainWindow extends JFrame implements DropTargetListener
{
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
	private static final long serialVersionUID = 1L;
	private JButton loadFile, saveFile, startSecure, editMetaBT;
	private JPanel Allows;
	private JTextField loadedFile, savedFile, privatePass, publicPass;
	private JLabel privatePassLb, publicPassLb;
	private JCheckBox allowCopyChBx, allowPrintChBx, allowPrintDegChBx, 
					  allowModifyChBx, allowFillInChBx, allowAnnotationChBx,
					  allowScrReadersChBx,allowAssemblyChBx, editAllows;
	private GUIHelper guiHelper;
	private JCheckBox hidePasses;
	private JFrame thisFrame = this;
	
	@SuppressWarnings("unused")
	private DropTarget dt;

	/**
	 * metod invoke swing window 
	 * 
	 * @param args - parameters of program
	 * @throws TransformerException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws TransformerException
	{
		//TODO toto co tu robi ... urcite inam
		GUIHelper.setMyLookAndFeel();
		
		Language.loadActualLanguage();
		
		MainWindow mw = new MainWindow();
		
		if(args.length == 1)
		{
			File f = new File(args[0]);
			
			if(f.exists() && f.isFile() && GUIMetods.checkPDFFile(f))
			{
				try 
				{
					mw.openFile(f);
				}
				catch (NoFileLoadedException e) 
				{
					logger.log(Level.SEVERE, "error while reading file", e);
					JOptionPane.showMessageDialog(
						null,
						Language.actual.incorrectFile,
						Language.actual.error,
						JOptionPane.ERROR_MESSAGE
					);
				}
			}
		}
	}
	
	/**
	 * constructor of main window witch set lookand feel 
	 * and initializate window
	 */
	public MainWindow() 
	{
		super();
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run()
			{
				initGUI();
				setSize(530, 258);
				GUIMetods.setLocationRelativeTo(thisFrame, null);
				setVisible(true);
				setResizable(false);
				setIconImage(new ImageIcon(this.getClass().getResource("/favicon.jpg")).getImage());
				setTitle("PDFProtector - Project22");
			}
		});
	}
	
	/**
	 * initialization of window metod create and add every component of window
	 * 
	 */
	private void initGUI()
	{
		{
			guiHelper = new GUIHelper(this);
			getContentPane().setLayout(null);
			{
				loadFile = new JButton();
				loadFile.addActionListener(new ActionListener() 
					{
						public void actionPerformed(ActionEvent ae)
					    {
							guiHelper.openFile();
					    }
					});
				
				getContentPane().add(loadFile);
				loadFile.setText(Language.actual.sourceFile);
				loadFile.setBounds(370, 20, 120, 20);
			}
			{
				ImageIcon img = new ImageIcon("images/settings.png");
				JButton button = new JButton(img);
				button.setBounds(500,5,21,21);
				button.setFocusable(false);
				button.setBackground(new Color(0, 1, 1, 1));
				button.setBorder(new LineBorder(Color.BLACK,0));
				getContentPane().add(button);
				button.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						//TODO dokoncit nastavenia
					}
				});
			}
			{
				loadedFile = new JTextField();
				loadedFile.setBounds(10, 20, 350, 20);
				loadedFile.setEditable(false);
				getContentPane().add(loadedFile);
				loadedFile.setBackground(new Color(255,255,255));
				loadedFile.setBorder(new LineBorder(new Color(0,0,0), 1, false));
				loadedFile.setForeground(new Color(0,0,0));
				loadedFile.setOpaque(true);
			}
			dt = new DropTarget(loadedFile, this);
			
			{
				savedFile = new JTextField();
				savedFile.setBounds(10, 50, 350, 20);
				getContentPane().add(savedFile);
				savedFile.setBackground(new Color(255,255,255));
				savedFile.setBorder(new LineBorder(new Color(0,0,0), 1, false));
				savedFile.setForeground(new Color(0,0,0));
				savedFile.setOpaque(true);
			}
			
			{
				saveFile = new JButton();
				getContentPane().add(saveFile);
				saveFile.setText(Language.actual.destFile);
				saveFile.setBounds(370, 50, 120, 20);
				saveFile.addActionListener(new ActionListener()
				{
				    public void actionPerformed(ActionEvent e) 
				    {
				    	final JFileChooser c = GUIMetods.getPdfFileChooser();

				    	if (c.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) 
				    	{
			    			SwingUtilities.invokeLater(new Runnable()
			    			{
								@Override
								public void run() {
									try 
						    		{
										savedFile.setText(c.getSelectedFile().getCanonicalPath());
						    		}
						    		catch (IOException ex) 
						    		{
										JOptionPane.showMessageDialog(null,
												Language.actual.missingFile,
												Language.actual.error,
												JOptionPane.ERROR_MESSAGE);
									}
								}
			    			});
			    		}
			    	}
				});
			}
			{
				Allows = new JPanel();
				Allows.setOpaque(false);
				getContentPane().add(Allows);
				Allows.setBounds(10, 90, 500, 100);
				Allows.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				Allows.setLayout(null);
			}
			
			{
				privatePassLb = new JLabel(Language.actual.privateKey);
				privatePassLb.setBounds(8, 7, 110, 20);
				Allows.add(privatePassLb);
			}
			
			{
				privatePass = new JPasswordField();
				privatePass.setBounds(8, 27, 110, 20);
				Allows.add(privatePass);
				privatePass.setBackground(new Color(255,255,255));
				privatePass.setBorder(new LineBorder(new Color(0,0,0), 1, false));
				privatePass.setForeground(new Color(0,0,0));
				privatePass.setOpaque(true);
			}
			
			{
				publicPassLb = new JLabel(Language.actual.publicKey);
				publicPassLb.setBounds(8, 47, 110, 20);
				Allows.add(publicPassLb);
			}
			
			{
				publicPass = new JPasswordField();
				publicPass.setBounds(8, 67, 110, 20);
				Allows.add(publicPass);
				publicPass.setBackground(new Color(255,255,255));
				publicPass.setBorder(new LineBorder(new Color(0,0,0), 1, false));
				publicPass.setForeground(new Color(0,0,0));
				publicPass.setOpaque(true);
			}
			
			{
				allowPrintChBx = new JCheckBox(Language.actual.printing);
				allowPrintChBx.setBounds(130, 7, 180, 20);
				allowPrintChBx.setSelected(true);
				Allows.add(allowPrintChBx);
				allowPrintChBx.setOpaque(false);
			}
			{
				allowCopyChBx = new JCheckBox(Language.actual.contentCopy);
				allowCopyChBx.setBounds(310, 7, 180, 20);
				Allows.add(allowCopyChBx);
				allowCopyChBx.setOpaque(false);
			}
			{
				allowPrintDegChBx = new JCheckBox(Language.actual.lessQualityPrint);
				allowPrintDegChBx.setSelected(true);
				allowPrintDegChBx.setBounds(130,27,180,20);
				Allows.add(allowPrintDegChBx);
				allowPrintDegChBx.setOpaque(false);
			}
			{
				allowModifyChBx = new JCheckBox(Language.actual.documentModificating);
				allowModifyChBx.setBounds(310,27,180,20);
				Allows.add(allowModifyChBx);
				allowModifyChBx.setOpaque(false);
			}
			{
				allowAssemblyChBx = new JCheckBox(Language.actual.pageBookmarkRotation);
				allowAssemblyChBx.setSelected(true);
				allowAssemblyChBx.setBounds(130,47,180,20);
				Allows.add(allowAssemblyChBx);
				allowAssemblyChBx.setOpaque(false);
			}
			{
				allowScrReadersChBx = new JCheckBox(Language.actual.mobileDevices);
				allowScrReadersChBx.setSelected(true);
				allowScrReadersChBx.setBounds(310,47,180,20);
				Allows.add(allowScrReadersChBx);
				allowScrReadersChBx.setOpaque(false);
			}
			{
				allowAnnotationChBx = new JCheckBox(Language.actual.modifyAnnotations);
				allowAnnotationChBx.setBounds(130,67,180,20);
				Allows.add(allowAnnotationChBx);
				allowAnnotationChBx.setOpaque(false);
			}
			{
				allowFillInChBx = new JCheckBox(Language.actual.fillIn);
				allowFillInChBx.setBounds(310,67,180,20);
				Allows.add(allowFillInChBx);
				allowFillInChBx.setOpaque(false);
			}
			{
				editAllows = new JCheckBox(Language.actual.allowEditing);
				editAllows.setBounds(6,72,110,20);
				getContentPane().add(editAllows);
				editAllows.setOpaque(false);
				editAllows.setEnabled(false);
				editAllows.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if(isFileLoaded())
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									setEnabledAll(editAllows.isSelected());
									
								}
							});
						else
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									editAllows.setSelected(false);	
								}
							});
					}
				});
			}
			
			{
				hidePasses = new JCheckBox(Language.actual.hidePasses);
				hidePasses.setBounds(110,72,110,20);
				getContentPane().add(hidePasses);
				hidePasses.setOpaque(false);
				hidePasses.setSelected(true);
				hidePasses.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run()
							{
								Allows.remove(privatePass);
								Allows.remove(publicPass);
								Allows.repaint();
								
								String tmpPrivatePass = GUIMetods.getText(privatePass);
								String tmpPublicPass = GUIMetods.getText(publicPass);
								
								if(hidePasses.isSelected())
								{
									privatePass = new JPasswordField(tmpPrivatePass);
									publicPass = new JPasswordField(tmpPublicPass);
								}
								else
								{
									privatePass = new JTextField(tmpPrivatePass);
									publicPass = new JTextField(tmpPublicPass);
								}
								
								privatePass.setBounds(8, 27, 110, 20);
								Allows.add(privatePass);
								privatePass.setBackground(new Color(255,255,255));
								privatePass.setBorder(new LineBorder(new Color(0,0,0), 1, false));
								privatePass.setForeground(new Color(0,0,0));
								privatePass.setOpaque(true);
								
								publicPass.setBounds(8, 67, 110, 20);
								Allows.add(publicPass);
								publicPass.setBackground(new Color(255,255,255));
								publicPass.setBorder(new LineBorder(new Color(0,0,0), 1, false));
								publicPass.setForeground(new Color(0,0,0));
								publicPass.setOpaque(true);
							}
						});
					}
				});
			}

			{
				startSecure = new JButton();
				startSecure.setEnabled(false);
				startSecure.setBounds(350, 200, 160, 20);
				getContentPane().add(startSecure);
				startSecure.setText(Language.actual.secureDocument);	
				startSecure.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
				    {
			    		if(savedFile.getText().equals(""))
			    		{
							saveFile.doClick();
						}
						boolean out = false;
						
						while (new File(savedFile.getText()).exists())
						{
							switch (GUIHelper.fileExistsConfirm(savedFile.getText())){
								case 0 : out = true; break;
								case 1 : saveFile.doClick(); break;
								case -1 :
								case 2 : return;
							}
							if(out) break;
						}
						
						int permissions = 0;
						
						if(editAllows.isSelected())
						{
							if(allowCopyChBx.isSelected()) permissions |= PdfWriter.ALLOW_COPY;
							if(allowPrintChBx.isSelected()) permissions |= PdfWriter.ALLOW_PRINTING;
							if(allowPrintDegChBx.isSelected()) permissions |= PdfWriter.ALLOW_DEGRADED_PRINTING;
							if(allowModifyChBx.isSelected()) permissions |= PdfWriter.ALLOW_MODIFY_CONTENTS;
							if(allowFillInChBx.isSelected()) permissions |= PdfWriter.ALLOW_FILL_IN;
							if(allowAnnotationChBx.isSelected()) permissions |= PdfWriter.ALLOW_MODIFY_ANNOTATIONS;
							if(allowScrReadersChBx.isSelected()) permissions |= PdfWriter.ALLOW_SCREENREADERS;
							if(allowAssemblyChBx.isSelected()) permissions |= PdfWriter.ALLOW_ASSEMBLY;	
						}
							
						new Thread(new SavingRunnable(permissions)).start();
				    }
				});
			}
			
			{
				editMetaBT = new JButton();
				editMetaBT.setEnabled(false);
				editMetaBT.addActionListener(new ActionListener() 
				{
				    public void actionPerformed(ActionEvent e)
				    {
				    	guiHelper.editFileMeta();
				    }
				});
				editMetaBT.setBounds(170, 200, 160, 20);
				getContentPane().add(editMetaBT);
				editMetaBT.setText(Language.actual.metaDatesEdit);
				
				setEnabledAll(false);
			}
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	
	/**
	 * method check if file is loaded or no
	 * 
	 * @return if file is loadet true else false
	 */
	protected boolean isFileLoaded() 
	{
		return !"".equals(loadedFile.getText());
	}

	/**
	 * loking and unloking editing of opitions
	 * 
	 * @param editable - lock or unlock
	 */
	private void setEnabledAll(final boolean editable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{	
			@Override
			public void run() 
			{
				allowCopyChBx.setEnabled(editable);
				allowPrintChBx.setEnabled(editable);
				allowPrintDegChBx.setEnabled(editable);
				allowModifyChBx.setEnabled(editable);
				allowFillInChBx.setEnabled(editable);
				allowAnnotationChBx.setEnabled(editable);
				allowScrReadersChBx.setEnabled(editable);
				allowAssemblyChBx.setEnabled(editable);
				privatePassLb.setEnabled(editable);
				publicPassLb.setEnabled(editable);
				privatePass.setEnabled(editable);
				publicPass.setEnabled(editable);
			}
		});
	}
	
	class SavingRunnable implements Runnable
	{
		int permissions;
		
		public SavingRunnable(int permissions)
		{
			this.permissions = permissions;
		}
		@Override
		public void run() 
		{
			SavingDialog sd = new SavingDialog(thisFrame);
			try
			{
				guiHelper.saveAndSecuredFile(savedFile.getText(), GUIMetods.getText(publicPass), GUIMetods.getText(privatePass), permissions);
			}
	    	catch (IOException e1)
			{
				logger.info("Error while reading file");
				JOptionPane.showMessageDialog(null,
						Language.actual.errorReadingFile,
						Language.actual.error,
						JOptionPane.ERROR_MESSAGE);
				
				SwingUtilities.invokeLater(new Runnable()
				{	
					@Override
					public void run()
					{
						savedFile.setText("");
						loadedFile.setText("");
						setEnabledAll(false);
					}
				});
				
				loadFile.doClick();
			}
	    	sd.closeDialog();
		}
	}
	
	public void fillFile(final String path)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() 
			{
				loadedFile.setText(path);
				savedFile.setText(path.substring(0, path.lastIndexOf('.')) + "-secured.pdf");
				startSecure.setEnabled(true);
				editMetaBT.setEnabled(true);
				editAllows.setEnabled(true);
				
				setEnabledAll(false);
				editAllows.setSelected(false);
				privatePass.setText("");
				publicPass.setText("");
			}
		});
	}
	
	public void openFile(File file) throws NoFileLoadedException
	{
		guiHelper.openFile(file);
	}
	
	// DragTargetListener
	public void dragEnter(DropTargetDragEvent dtde) {}
	public void dragExit(DropTargetEvent dte) {}
	public void dragOver(DropTargetDragEvent dtde) {}
	public void dropActionChanged(DropTargetDragEvent dtde) {}

	@SuppressWarnings("unchecked")
	/**
	 * Method run after drag file to surceFile textField
	 * after that it open file and user can continue
	 */
	public void drop(DropTargetDropEvent dtde) 
	{
		try
		{
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();

			for (int i = 0; i < flavors.length; i++)
			{
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

				List<File> list = (List<File>)tr.getTransferData(flavors[i]);
				
				if(list.size() > 0)
					guiHelper.openFile(list.get(0));
				
				dtde.dropComplete(true);
			}		
		}
		catch (IOException | UnsupportedFlavorException e)
		{
			logger.severe(e.getMessage());
		}
	}

	public void setDestinationFile(final String destFile) {
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
					savedFile.setText(destFile);
			}
		});
		
	}
}
