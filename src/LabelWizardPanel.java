import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

public class LabelWizardPanel extends JWizardPanel {
		public LabelWizardPanel(JWizardComponents wizardComponents, String label) {
			super(wizardComponents);
			JButton backButton = wizardComponents.getBackButton();
			backButton.setText("");
		/*
		 * ImageIcon iconBack = createImageIcon("images/img_71224.png"); Icon
		 * iconBackCell = new ImageIcon(iconBack.getImage().getScaledInstance(12, 12,
		 * Image.SCALE_SMOOTH)); backButton.setIcon(iconBackCell);
		 * backButton.setToolTipText("Click this button to back the previous wizard.");
		 * 
		 * nextButton = wizardComponents.getNextButton(); nextButton.setText("");
		 * ImageIcon iconNext = createImageIcon("images/img_174455.png"); Icon
		 * iconNextCell = new ImageIcon(iconNext.getImage().getScaledInstance(12, 12,
		 * Image.SCALE_SMOOTH)); nextButton.setIcon(iconNextCell);
		 * nextButton.setToolTipText("Click this button to go to the next wizard.");
		 * 
		 * finishButton = wizardComponents.getFinishButton(); finishButton.setText("");
		 * ImageIcon iconFinish = createImageIcon("images/ojala.png"); Icon
		 * iconFinishCell = new ImageIcon(iconFinish.getImage().getScaledInstance(12,
		 * 12, Image.SCALE_SMOOTH)); finishButton.setIcon(iconFinishCell); finishButton.
		 * setToolTipText("Click this button to finish your settings selection.");
		 * 
		 * JButton cancelButton = wizardComponents.getCancelButton();
		 * cancelButton.setText(""); ImageIcon iconCancel =
		 * createImageIcon("images/delete.png"); Icon iconCancelCell = new
		 * ImageIcon(iconCancel.getImage().getScaledInstance(12, 12,
		 * Image.SCALE_SMOOTH)); cancelButton.setIcon(iconCancelCell);
		 * cancelButton.setToolTipText("Click this button to cancel this process.");
		 * 
		 * this.setLayout(new GridBagLayout()); this.add(new JLabel(label), new
		 * GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		 * GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0)); TextField textXML =
		 * (TextField) new TextField(20);
		 * 
		 * textXML.setText(pref1.get(TRACKMATE_TRANSF_PATH, "")); GridBagLayout
		 * layoutXML = (GridBagLayout) getLayout(); GridBagConstraints constraintsXML =
		 * layoutXML.getConstraints(textXML); JButton buttonXML = new JButton("");
		 * ImageIcon iconXML = createImageIcon("images/browse.png"); Icon iconXMLCell =
		 * new ImageIcon(iconXML.getImage().getScaledInstance(15, 15,
		 * Image.SCALE_SMOOTH)); buttonXML.setIcon(iconXMLCell); ImageIcon iconBrowse =
		 * createImageIcon("images/browse.png"); Icon iconBrowseCell = new
		 * ImageIcon(iconBrowse.getImage().getScaledInstance(15, 15,
		 * Image.SCALE_SMOOTH)); buttonXML.setIcon(iconBrowseCell); DirectoryListener
		 * listenerXML = new DirectoryListener("Browse for " + label, textXML,
		 * JFileChooser.FILES_AND_DIRECTORIES);
		 * buttonXML.addActionListener(listenerXML); Panel panelXML = new Panel();
		 * panelXML.setLayout(new FlowLayout(FlowLayout.LEFT)); JLabel loadLabel = new
		 * JLabel("⊳  Load TrackMate .XML file: "); loadLabel.setFont(new
		 * Font("Verdana", Font.BOLD, 12)); panelXML.add(loadLabel);
		 * panelXML.add(textXML); panelXML.add(buttonXML);
		 * layoutXML.setConstraints(panelXML, constraintsXML); TextField textImg =
		 * (TextField) new TextField(20);
		 * 
		 * textImg.setText(pref1.get(TRACKMATE_IMAGES_PATH, "")); GridBagLayout
		 * layoutImg = (GridBagLayout) getLayout(); GridBagConstraints constraintsImg =
		 * layoutImg.getConstraints(textImg); JButton buttonImg = new JButton("");
		 * ImageIcon iconIM = createImageIcon("images/browse.png"); Icon iconIMCell =
		 * new ImageIcon(iconIM.getImage().getScaledInstance(15, 15,
		 * Image.SCALE_SMOOTH)); buttonImg.setIcon(iconIMCell); ImageIcon iconB =
		 * createImageIcon("images/browse.png"); Icon iconBCell = new
		 * ImageIcon(iconB.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		 * buttonImg.setIcon(iconBCell); DirectoryListener listenerImg = new
		 * DirectoryListener("Browse for " + label, textImg,
		 * JFileChooser.FILES_AND_DIRECTORIES);
		 * buttonImg.addActionListener(listenerImg); Panel panelImg = new Panel();
		 * panelImg.setLayout(new FlowLayout(FlowLayout.LEFT)); JLabel directLabel = new
		 * JLabel("⊳  Select the directory where movie files are:   ");
		 * directLabel.setFont(new Font("Verdana", Font.BOLD, 12));
		 * panelImg.add(directLabel); panelImg.add(textImg); panelImg.add(buttonImg);
		 * layoutImg.setConstraints(panelImg, constraintsImg); JPanel mainPanel = new
		 * JPanel(); mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		 * mainPanel.add(Box.createVerticalStrut(15)); mainPanel.add(panelXML);
		 * mainPanel.add(Box.createVerticalStrut(20)); mainPanel.add(panelImg);
		 * mainPanel.add(Box.createVerticalStrut(15));
		 * mainPanel.setBorder(BorderFactory.createTitledBorder(""));
		 * this.add(mainPanel); this.setPanelTitle("⊚  USER-INPUT REQUIREMENTS :  ");
		 * nextButton.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent event) { pref1.put(TRACKMATE_TRANSF_PATH,
		 * textXML.getText()); pref1.put(TRACKMATE_IMAGES_PATH, textImg.getText());
		 * TransfPath = textXML.getText(); imagesPath = textImg.getText(); } });
		 */

		}
	}