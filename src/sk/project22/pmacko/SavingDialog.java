package sk.project22.pmacko;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class SavingDialog extends JWindow 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(SavingDialog.class.getName());
	private JLabel comment;
	private JFrame frame;
	private JButton kill;
	private JProgressBar pb;
	
	public static void main(String[] args) throws IOException
	{
		SavingDialog sd = new SavingDialog(null);
		sd.closeDialog();	   
	}
	
	/**
	 * This costructor call initialiyation of window set siy and show window
	 * as a parameter its give a parentalFrame witch is blocket after show this frame.
	 * 
	 * @param parentalFrame
	 */
	public SavingDialog(JFrame parentalFrame)
	{
		super();
		
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run()
			{
				initGUI();
				setSize(240, 80);
				setVisible(true);
				setIconImage(new ImageIcon("src\\favicon.jpg").getImage());
				setAlwaysOnTop(true);
			}	
		});
		GUIMetods.setLocationRelativeTo(this, parentalFrame);
		parentalFrame.setEnabled(false);
		this.frame = parentalFrame;
	}
	/**
	 * initialization of all elements in window
	 */
	private void initGUI()
	{
		JPanel cont = new JPanel();
		cont.setBorder(new LineBorder(Color.BLACK));
		getContentPane().add(cont);
		cont.setLayout(null);
		 
		comment = new JLabel("Ukladám dokument");
		comment.setBounds(20, 4, 200, 20);
		cont.add(comment);
		
		pb = new JProgressBar();
		pb.setBounds(20, 30, 200, 20);
		pb.setIndeterminate(true);
		cont.add(pb);
		
		kill = new JButton();
		kill.setBounds(160,55,60,20);
		kill.setMargin(new Insets(2, 2, 2, 2));
		cont.add(kill);
		kill.setText("Cancel");
	}

	JComponent[] inputs;
	
	/**
	 * when everithing done ist time to close dialog this metod change
	 * name of cancel button to OK and ad listner to close window and allow
	 * you to continue with parental window
	 */
	public void closeDialog()
	{
		SwingUtilities.invokeLater(new Runnable()
		{	
			@Override
			public void run()
			{
				comment.setText("Dokument uloženy");
				kill.setText("OK");
				pb.setIndeterminate(false);
				pb.setMaximum(100);
				pb.setValue(100);
				pb.setStringPainted(true);
				
				kill.addActionListener(new ActionListener()
				{	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
						frame.setEnabled(true);
					}
				});
			}
		});
	}
}
