import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import ij.IJ;
import ij.measure.Measurements;
import ij.plugin.PlugIn;
import jwizardcomponent.JWizardPanel;
import jwizardcomponent.Utilities;
import jwizardcomponent.frame.JWizardFrame;

public class SPTAnalyzer_ implements PlugIn, Measurements {

	static String SLTDISPLAYER_XML_PATH, SLTDISPLAYER_IMAGES_PATH, xmlPath, imagesPath;;
	Preferences prefXml, prefImages;
	JPanel panelInitial;
	JFrame frame;
	JWizardFrame wizard;
	JWizardPanel panel;
	public static final int PANEL_FIRST = 0;
	public static final int PANEL_CHOOSER = 1;
	public static final int PANEL_OPTION_A = 2;
	public static final int PANEL_OPTION_B = 3;
	public static final int PANEL_LAST = 4;
	public static TextField textXml, textImages;
	Thread sptViewer, sptBatch;

	@Override
	public void run(String arg0) {

		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		prefXml = Preferences.userRoot();
		prefImages = Preferences.userRoot();
		SLTDISPLAYER_XML_PATH = "xml_path";
		SLTDISPLAYER_IMAGES_PATH = "images_path";

		JButton buttonBrowse1 = new JButton("");
		JButton buttonBrowse2 = new JButton("");
		ImageIcon iconBrowse = createImageIcon("images/browse.png");
		Icon iconBrowseCell = new ImageIcon(iconBrowse.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		buttonBrowse1.setIcon(iconBrowseCell);
		panelInitial = new JPanel();
		panelInitial.setLayout(new BoxLayout(panelInitial, BoxLayout.Y_AXIS));

		textXml = (TextField) new TextField(20);
		textXml.setText(prefXml.get(SLTDISPLAYER_XML_PATH, ""));
		textImages = (TextField) new TextField(20);
		textImages.setText(prefImages.get(SLTDISPLAYER_IMAGES_PATH, ""));
		JLabel labelXml = new JLabel("⊳  Load TrackMate .XML file: ");
		labelXml.setFont(new Font("Verdana", Font.BOLD, 12));
		JLabel labelImages = new JLabel("⊳  Load movies to be analyzed:   ");
		labelImages.setFont(new Font("Verdana", Font.BOLD, 12));
		DirectoryListener listenerXml = new DirectoryListener("Browse for TrackMate XML file...  ", textXml,
				JFileChooser.FILES_AND_DIRECTORIES);
		DirectoryListener listenerImages = new DirectoryListener("Browse for movies...  ", textImages,
				JFileChooser.FILES_AND_DIRECTORIES);
		buttonBrowse2.setIcon(iconBrowseCell);
		buttonBrowse1.addActionListener(listenerXml);
		buttonBrowse2.addActionListener(listenerImages);

		Panel panelXml = new Panel();
		panelXml.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelXml.add(labelXml);
		panelXml.add(textXml);
		panelXml.add(buttonBrowse1);
		Panel panelImages = new Panel();
		panelImages.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelImages.add(labelImages);
		panelImages.add(textImages);
		panelImages.add(buttonBrowse2);
		JButton okButton = new JButton("SPT-Viewer");
		ImageIcon iconOk = createImageIcon("images/viewer.png");
		Icon iconOkCell = new ImageIcon(iconOk.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		okButton.setIcon(iconOkCell);
		JButton cancelButton = new JButton("SPT-Batch");
		ImageIcon iconCancel = createImageIcon("images/batch.png");
		Icon iconCancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		cancelButton.setIcon(iconCancelCell);
		Panel panelOkCancel = new Panel();
		panelOkCancel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelOkCancel.add(okButton);
		panelOkCancel.add(cancelButton);
		panelInitial.add(panelXml);
		panelInitial.add(panelImages);
		panelInitial.add(panelOkCancel);
		createFrame();
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				sptViewer = new Thread(new Runnable() {
					public void run() {
						xmlPath = textXml.getText();
						imagesPath = textImages.getText();
						prefXml.put(SLTDISPLAYER_XML_PATH, textXml.getText());
						prefImages.put(SLTDISPLAYER_IMAGES_PATH, textImages.getText());
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						wizard = new JWizardFrame();
						wizard.setTitle("SLT Viewer: ANALYSIS");

						panel = new FirstWizardPanel(wizard.getWizardComponents());
						wizard.getWizardComponents().addWizardPanel(PANEL_FIRST, panel);

						panel = new ChooserWizardPanel(wizard.getWizardComponents());
						wizard.getWizardComponents().addWizardPanel(PANEL_CHOOSER, panel);

						panel = new OptionWizardPanel(wizard.getWizardComponents(), "A");
						wizard.getWizardComponents().addWizardPanel(PANEL_OPTION_A, panel);

						panel = new OptionWizardPanel(wizard.getWizardComponents(), "B");
						wizard.getWizardComponents().addWizardPanel(PANEL_OPTION_B, panel);

						panel = new LastWizardPanel(wizard.getWizardComponents());
						wizard.getWizardComponents().addWizardPanel(PANEL_LAST, panel);
						Component[] components = null;
						try {
							components = wizard.getWizardComponents().getFinishButton().getParent().getComponents();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						wizard.getWizardComponents().getFinishButton().getParent().remove(components[2]);
						JButton backButton = wizard.getWizardComponents().getBackButton();
						backButton.setText("");
						ImageIcon iconBack = createImageIcon("images/next.png");
						Icon backCell = new ImageIcon(
								iconBack.getImage().getScaledInstance(20, 22, Image.SCALE_SMOOTH));
						backButton.setIcon(backCell);
						backButton.setToolTipText("Click this button to back on wizard.");
						JButton nextButton = wizard.getWizardComponents().getNextButton();
						nextButton.setText("");
						ImageIcon iconNext = createImageIcon("images/back.png");
						Icon nextCell = new ImageIcon(
								iconNext.getImage().getScaledInstance(20, 22, Image.SCALE_SMOOTH));
						nextButton.setIcon(nextCell);
						nextButton.setToolTipText("Click this button to switch wizard.");
						JButton cancelButton = wizard.getWizardComponents().getCancelButton();
						cancelButton.setText("");
						ImageIcon iconCancel = createImageIcon("images/cancel.png");
						Icon cancelCell = new ImageIcon(
								iconCancel.getImage().getScaledInstance(20, 22, Image.SCALE_SMOOTH));
						cancelButton.setIcon(cancelCell);
						cancelButton.setToolTipText("Click this button to cancel the process.");
						wizard.setSize(630, 1000);
						Utilities.centerComponentOnScreen(wizard);

						wizard.setVisible(true);
					}
				});
				sptViewer.start();
			}

		});
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				sptBatch = new Thread(new Runnable() {
					public void run() {
						xmlPath = textXml.getText();
						imagesPath = textImages.getText();
						prefXml.put(SLTDISPLAYER_XML_PATH, textXml.getText());
						prefImages.put(SLTDISPLAYER_IMAGES_PATH, textImages.getText());
						SPTBatch_ sptBatch = new SPTBatch_(xmlPath, imagesPath);
						sptBatch.run("");
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					}
				});
				sptBatch.start();
			}
		});

	}

	public void createFrame() {

		frame = new JFrame("Single Particle Tracking Analysis- TrackMate based Toolbox");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 500);
		frame.setResizable(false);
		frame.getContentPane().add(panelInitial);
		frame.pack();
		frame.setVisible(true);

	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL img = SPTAnalyzer_.class.getResource(path);
		if (img != null) {
			return new ImageIcon(img);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
