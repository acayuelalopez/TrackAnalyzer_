import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.example.DynamicJWizard;

public class LastWizardPanel extends LabelWizardPanel {
		public LastWizardPanel(JWizardComponents wizardComponents) {
			super(wizardComponents, "");
			setPanelTitle("⊚  PLOT-OPTIONS:  Spots,Links or Tracks");
			update();
			this.removeAll();

			JTabbedPane tabbedPane = new JTabbedPane();
		/*
		 * ImageIcon iconSpot = createImageIcon(
		 * "images/5441165-point-of-light-png-104-images-in-collection-page-1-point-of-light-png-320_320_preview.png"
		 * ); Icon iconSpotCell = new
		 * ImageIcon(iconSpot.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
		 * ImageIcon iconLink = createImageIcon("images/link.png"); Icon iconLinkCell =
		 * new ImageIcon(iconLink.getImage().getScaledInstance(16, 16,
		 * Image.SCALE_SMOOTH)); ImageIcon iconTrack =
		 * createImageIcon("images/track.jpg"); Icon iconTrackCell = new
		 * ImageIcon(iconTrack.getImage().getScaledInstance(16, 16,
		 * Image.SCALE_SMOOTH));
		 * 
		 * JComponent panel1 = makeTextPanel(""); JComponent panelX = makeTextPanel("");
		 * JComponent panelY = makeTextPanel(""); panelY.setLayout(new
		 * FlowLayout(FlowLayout.LEFT)); JLabel xSpot = new JLabel();
		 * xSpot.setText("⊳ Spot-Feature for X axis:   "); xSpot.setFont(new
		 * Font("Verdana", Font.BOLD, 12)); JLabel ySpot = new JLabel();
		 * ySpot.setText("⊳ Spot-Feature for Y axis:   "); ySpot.setFont(new
		 * Font("Verdana", Font.BOLD, 12)); JComboBox comboSpotsX = new JComboBox();
		 * JComboBox comboSpotsY = new JComboBox(); Object[] spotItems = { "Quality",
		 * "T", "Frame", "Signal/Noise Ratio", "Standard Deviation", "Contrast",
		 * "Manual Spot Color", "Minimal Intensity", "Maximal Intensity",
		 * "Median Intensity", "Mean Intensity", "Total Intensity",
		 * "Estimated Diameter", "Radius", "X", "Y", "Z" }; for (int i = 0; i <
		 * spotItems.length; i++) { comboSpotsX.addItem(spotItems[i]);
		 * comboSpotsY.addItem(spotItems[i]); }
		 * 
		 * if (comboSpotsX.getSelectedItem().toString().equals("Quality")) xSelectedSpot
		 * = "QUALITY"; if (comboSpotsX.getSelectedItem().toString().equals("T"))
		 * xSelectedSpot = "POSITION_T"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Frame")) xSelectedSpot =
		 * "FRAME"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Signal/Noise Ratio"))
		 * xSelectedSpot = "SNR"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Standard Deviation"))
		 * xSelectedSpot = "STANDARD_DEVIATION"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Contrast")) xSelectedSpot =
		 * "CONTRAST"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Manual Spot Color"))
		 * xSelectedSpot = "MANUAL_COLOR"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Minimal Intensity"))
		 * xSelectedSpot = "MIN_INTENSITY"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Maximal Intensity"))
		 * xSelectedSpot = "MAX_INTENSITY"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Median Intensity"))
		 * xSelectedSpot = "MEDIAN_INTENSITY"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Mean Intensity"))
		 * xSelectedSpot = "MEAN_INTENSITY"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Total Intensity"))
		 * xSelectedSpot = "TOTAL_INTENSITY"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Estimated Diameter"))
		 * xSelectedSpot = "ESTIMATED_DIAMETER"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Radius")) xSelectedSpot =
		 * "RADIUS"; if (comboSpotsX.getSelectedItem().toString().equals("X"))
		 * xSelectedSpot = "POSITION_X"; if
		 * (comboSpotsX.getSelectedItem().toString().equals("Y")) xSelectedSpot =
		 * "POSITION_Y"; if (comboSpotsX.getSelectedItem().toString().equals("Z"))
		 * xSelectedSpot = "POSITION_Z";
		 * 
		 * if (comboSpotsY.getSelectedItem().toString().equals("Quality")) ySelectedSpot
		 * = "QUALITY"; if (comboSpotsY.getSelectedItem().toString().equals("T"))
		 * ySelectedSpot = "POSITION_T"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Frame")) ySelectedSpot =
		 * "FRAME"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Signal/Noise Ratio"))
		 * ySelectedSpot = "SNR"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Standard Deviation"))
		 * ySelectedSpot = "STANDARD_DEVIATION"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Contrast")) ySelectedSpot =
		 * "CONTRAST"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Manual Spot Color"))
		 * ySelectedSpot = "MANUAL_COLOR"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Minimal Intensity"))
		 * ySelectedSpot = "MIN_INTENSITY"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Maximal Intensity"))
		 * ySelectedSpot = "MAX_INTENSITY"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Median Intensity"))
		 * ySelectedSpot = "MEDIAN_INTENSITY"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Mean Intensity"))
		 * ySelectedSpot = "MEAN_INTENSITY"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Total Intensity"))
		 * ySelectedSpot = "TOTAL_INTENSITY"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Estimated Diameter"))
		 * ySelectedSpot = "ESTIMATED_DIAMETER"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Radius")) ySelectedSpot =
		 * "RADIUS"; if (comboSpotsY.getSelectedItem().toString().equals("X"))
		 * ySelectedSpot = "POSITION_X"; if
		 * (comboSpotsY.getSelectedItem().toString().equals("Y")) ySelectedSpot =
		 * "POSITION_Y"; if (comboSpotsY.getSelectedItem().toString().equals("Z"))
		 * ySelectedSpot = "POSITION_Z";
		 * 
		 * panelX.setLayout(new FlowLayout(FlowLayout.LEFT));
		 * panelX.add(Box.createVerticalStrut(10)); panelX.add(xSpot);
		 * panelX.add(Box.createVerticalStrut(5)); panelX.add(comboSpotsX);
		 * panelY.add(ySpot); panelY.add(Box.createVerticalStrut(5));
		 * panelY.add(comboSpotsY); panel1.setLayout(new BoxLayout(panel1,
		 * BoxLayout.Y_AXIS)); panel1.add(panelX); panel1.add(panelY); JPanel panelESP =
		 * new JPanel(new FlowLayout(FlowLayout.CENTER));
		 * panelESP.setBorder(BorderFactory.createLoweredBevelBorder()); ESP = new
		 * JCheckBox("  Export User-Defined Spotss-Plot."); panelESP.add(ESP);
		 * panel1.add(Box.createVerticalStrut(15)); panel1.add(panelESP);
		 * panel1.setBorder(BorderFactory.createTitledBorder(""));
		 * tabbedPane.addTab("Spots", iconSpotCell, panel1,
		 * "Select the X-Y Spot features to plot."); tabbedPane.setMnemonicAt(0,
		 * KeyEvent.VK_1);
		 * 
		 * JComponent panel2 = makeTextPanel(""); JComponent panelLinkX =
		 * makeTextPanel(""); JComponent panelLinkY = makeTextPanel("");
		 * panelLinkX.setLayout(new FlowLayout(FlowLayout.LEFT));
		 * panelLinkY.setLayout(new FlowLayout(FlowLayout.LEFT)); JLabel xLink = new
		 * JLabel(); xLink.setText("⊳ Link-Feature for X axis:   "); xLink.setFont(new
		 * Font("Verdana", Font.BOLD, 12)); JLabel yLink = new JLabel();
		 * yLink.setText("⊳ Link-Feature for Y axis:   "); yLink.setFont(new
		 * Font("Verdana", Font.BOLD, 12)); JComboBox comboLinkX = new JComboBox();
		 * JComboBox comboLinkY = new JComboBox(); Object[] edgeItems = { "Link Cost",
		 * "Velocity", "Time (Mean)", "X Location (Mean)", "Source Spot ID",
		 * "Y Location (Mean)", "Displacement", "Manual Edge Color",
		 * "Z Location (Mean)", "Target Spot ID", }; for (int i = 0; i <
		 * edgeItems.length; i++) { comboLinkX.addItem(edgeItems[i]);
		 * comboLinkY.addItem(edgeItems[i]); }
		 * 
		 * if (comboLinkX.getSelectedItem().toString().equals("Link Cost"))
		 * xSelectedLink = "LINK_COST"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Velocity")) xSelectedLink =
		 * "VELOCITY"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Time (Mean)")) xSelectedLink
		 * = "EDGE_TIME"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Source Spot ID"))
		 * xSelectedLink = "SPOT_SOURCE_ID"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Y Location (Mean)"))
		 * xSelectedLink = "EDGE_Y_LOCATION"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Displacement"))
		 * xSelectedLink = "DISPLACEMENT"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Manual Edge Color"))
		 * xSelectedLink = "MANUAL_COLOR"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Z Location (Mean)"))
		 * xSelectedLink = "EDGE_Z_LOCATION"; if
		 * (comboLinkX.getSelectedItem().toString().equals("Target Spot ID"))
		 * xSelectedLink = "SPOT_TARGET_ID";
		 * 
		 * if (comboLinkY.getSelectedItem().toString().equals("Link Cost"))
		 * ySelectedLink = "LINK_COST"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Velocity")) ySelectedLink =
		 * "VELOCITY"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Time (Mean)")) ySelectedLink
		 * = "EDGE_TIME"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Source Spot ID"))
		 * ySelectedLink = "SPOT_SOURCE_ID"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Y Location (Mean)"))
		 * ySelectedLink = "EDGE_Y_LOCATION"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Displacement"))
		 * ySelectedLink = "DISPLACEMENT"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Manual Edge Color"))
		 * ySelectedLink = "MANUAL_COLOR"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Z Location (Mean)"))
		 * ySelectedLink = "EDGE_Z_LOCATION"; if
		 * (comboLinkY.getSelectedItem().toString().equals("Target Spot ID"))
		 * ySelectedLink = "SPOT_TARGET_ID";
		 * 
		 * panelLinkX.setLayout(new FlowLayout(FlowLayout.LEFT));
		 * panelLinkX.add(Box.createVerticalStrut(10)); panelLinkX.add(xLink);
		 * panelLinkX.add(Box.createVerticalStrut(5)); panelLinkX.add(comboLinkX);
		 * panelLinkY.add(yLink); panelLinkY.add(Box.createVerticalStrut(5));
		 * panelLinkY.add(comboLinkY); panel2.setLayout(new BoxLayout(panel2,
		 * BoxLayout.Y_AXIS)); panel2.add(panelLinkX); panel2.add(panelLinkY); JPanel
		 * panelELP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		 * panelELP.setBorder(BorderFactory.createLoweredBevelBorder()); ELP = new
		 * JCheckBox("  Export User-Defined Links-Plot."); panelELP.add(ELP);
		 * panel2.add(Box.createVerticalStrut(15)); panel2.add(panelELP);
		 * panel2.setBorder(BorderFactory.createTitledBorder(""));
		 * tabbedPane.addTab("Links", iconLinkCell, panel2,
		 * "Select the  X-Y Link features to plot."); tabbedPane.setMnemonicAt(1,
		 * KeyEvent.VK_2);
		 * 
		 * JComponent panel3 = makeTextPanel(""); JComponent panelTrackX =
		 * makeTextPanel(""); JComponent panelTrackY = makeTextPanel("");
		 * panelTrackX.setLayout(new FlowLayout(FlowLayout.LEFT));
		 * panelTrackY.setLayout(new FlowLayout(FlowLayout.LEFT)); JLabel xTrack = new
		 * JLabel(); xTrack.setText("⊳ Track-Feature for X axis:   ");
		 * xTrack.setFont(new Font("Verdana", Font.BOLD, 12)); JLabel yTrack = new
		 * JLabel(); yTrack.setText("⊳ Track-Feature for Y axis:   ");
		 * yTrack.setFont(new Font("Verdana", Font.BOLD, 12)); JComboBox comboTrackX =
		 * new JComboBox(); JComboBox comboTrackY = new JComboBox(); Object[] trackItems
		 * = { "Track Start", "Track Index", "Number of Merge Events",
		 * "Velocity Standard Deviation", "Track ID", "Median Quality",
		 * "Quality Standard Deviation", "Median Velocity", "X Location (Mean)",
		 * "Minimal Speed", "Number of Spots in Track", "Z Location (Mean)",
		 * "Number of Gaps", "Track Stop", "Mean Velocity", "Maximal Velocity",
		 * "Number of Split Events", "Y Location (Mean)", "Track Displacement",
		 * "Complex Points", "Mean Quality", "Duration of Track", "Maximal Quality",
		 * "Longest Gap", "Minimal Quality" }; for (int i = 0; i < trackItems.length;
		 * i++) { comboTrackX.addItem(trackItems[i]);
		 * comboTrackY.addItem(trackItems[i]); }
		 * 
		 * if (comboTrackX.getSelectedItem().toString().equals("Track Start"))
		 * xSelectedTrack = "TRACK_START"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Track Index"))
		 * xSelectedTrack = "TRACK_INDEX"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Number of Merge Events"))
		 * xSelectedTrack = "NUMBER_MERGES"; if
		 * (comboTrackX.getSelectedItem().toString().
		 * equals("Velocity Standard Deviation")) xSelectedTrack = "TRACK_STD_SPEED"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Track ID")) xSelectedTrack
		 * = "TRACK_ID"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Median Quality"))
		 * xSelectedTrack = "TRACK_MEDIAN_QUALITY"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Quality Standard Deviation"
		 * )) xSelectedTrack = "TRACK_STD_QUALITY"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Median Velocity"))
		 * xSelectedTrack = "TRACK_MEDIAN_SPEED"; if
		 * (comboTrackX.getSelectedItem().toString().equals("X Location (Mean)"))
		 * xSelectedTrack = "TRACK_X_LOCATION"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Minimal Speed"))
		 * xSelectedTrack = "TRACK_MIN_SPEED"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Number of Spots in Track"))
		 * xSelectedTrack = "NUMBER_SPOTS"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Z Location (Mean)"))
		 * xSelectedTrack = "TRACK_Z_LOCATION"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Number of Gaps"))
		 * xSelectedTrack = "NUMBER_GAPS"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Track Stop"))
		 * xSelectedTrack = "TRACK_STOP"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Mean Velocity"))
		 * xSelectedTrack = "TRACK_MEAN_SPEED"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Maximal Velocity"))
		 * xSelectedTrack = "TRACK_MAX_SPEED"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Number of Split Events"))
		 * xSelectedTrack = "NUMBER_SPLITS"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Y Location (Mean)"))
		 * xSelectedTrack = "TRACK_Y_LOCATION"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Track Displacement"))
		 * xSelectedTrack = "TRACK_DISPLACEMENT"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Complex Points"))
		 * xSelectedTrack = "NUMBER_COMPLEX"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Mean Quality"))
		 * xSelectedTrack = "TRACK_MEAN_QUALITY"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Duration of Track"))
		 * xSelectedTrack = "TRACK_DURATION"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Maximal Quality"))
		 * xSelectedTrack = "TRACK_MAX_QUALITY"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Longest Gap"))
		 * xSelectedTrack = "LONGEST_GAP"; if
		 * (comboTrackX.getSelectedItem().toString().equals("Minimal Quality"))
		 * xSelectedTrack = "TRACK_MIN_QUALITY";
		 * 
		 * if (comboTrackY.getSelectedItem().toString().equals("Track Start"))
		 * ySelectedTrack = "TRACK_START"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Track Index"))
		 * ySelectedTrack = "TRACK_INDEX"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Number of Merge Events"))
		 * ySelectedTrack = "NUMBER_MERGES"; if
		 * (comboTrackY.getSelectedItem().toString().
		 * equals("Velocity Standard Deviation")) ySelectedTrack = "TRACK_STD_SPEED"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Track ID")) ySelectedTrack
		 * = "TRACK_ID"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Median Quality"))
		 * ySelectedTrack = "TRACK_MEDIAN_QUALITY"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Quality Standard Deviation"
		 * )) ySelectedTrack = "TRACK_STD_QUALITY"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Median Velocity"))
		 * ySelectedTrack = "TRACK_MEDIAN_SPEED"; if
		 * (comboTrackY.getSelectedItem().toString().equals("X Location (Mean)"))
		 * ySelectedTrack = "TRACK_X_LOCATION"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Minimal Speed"))
		 * ySelectedTrack = "TRACK_MIN_SPEED"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Number of Spots in Track"))
		 * ySelectedTrack = "NUMBER_SPOTS"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Z Location (Mean)"))
		 * ySelectedTrack = "TRACK_Z_LOCATION"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Number of Gaps"))
		 * ySelectedTrack = "NUMBER_GAPS"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Track Stop"))
		 * ySelectedTrack = "TRACK_STOP"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Mean Velocity"))
		 * ySelectedTrack = "TRACK_MEAN_SPEED"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Maximal Velocity"))
		 * ySelectedTrack = "TRACK_MAX_SPEED"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Number of Split Events"))
		 * ySelectedTrack = "NUMBER_SPLITS"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Y Location (Mean)"))
		 * ySelectedTrack = "TRACK_Y_LOCATION"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Track Displacement"))
		 * ySelectedTrack = "TRACK_DISPLACEMENT"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Complex Points"))
		 * ySelectedTrack = "NUMBER_COMPLEX"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Mean Quality"))
		 * ySelectedTrack = "TRACK_MEAN_QUALITY"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Duration of Track"))
		 * ySelectedTrack = "TRACK_DURATION"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Maximal Quality"))
		 * ySelectedTrack = "TRACK_MAX_QUALITY"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Longest Gap"))
		 * ySelectedTrack = "LONGEST_GAP"; if
		 * (comboTrackY.getSelectedItem().toString().equals("Minimal Quality"))
		 * ySelectedTrack = "TRACK_MIN_QUALITY";
		 * 
		 * panelTrackX.setLayout(new FlowLayout(FlowLayout.LEFT));
		 * panelTrackX.add(Box.createVerticalStrut(10)); panelTrackX.add(xTrack);
		 * panelTrackX.add(Box.createVerticalStrut(5)); panelTrackX.add(comboTrackX);
		 * panelTrackY.add(yTrack); panelTrackY.add(Box.createVerticalStrut(5));
		 * panelTrackY.add(comboTrackY); panel3.setLayout(new BoxLayout(panel3,
		 * BoxLayout.Y_AXIS)); panel3.add(panelTrackX); panel3.add(panelTrackY); JPanel
		 * panelETP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		 * panelETP.setBorder(BorderFactory.createLoweredBevelBorder()); ETP = new
		 * JCheckBox("  Export User-Defined Tracks-Plot."); panelETP.add(ETP);
		 * panel3.add(Box.createVerticalStrut(15)); panel3.add(panelETP);
		 * panel3.setBorder(BorderFactory.createTitledBorder(""));
		 * 
		 * tabbedPane.addTab("Tracks", iconTrackCell, panel3,
		 * "Select the  X-Y Track features to plot."); tabbedPane.setMnemonicAt(2,
		 * KeyEvent.VK_3); tabbedPane.setPreferredSize(new Dimension(450, 250));
		 * this.add(tabbedPane);
		 * 
		 * // The following line enables to use scrolling tabs.
		 * tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		 */
		}

		public void update() {
			setNextButtonEnabled(false);
			//if (itemCheckPlot == ItemEvent.SELECTED)
			//	setNextButtonEnabled(false);

			setFinishButtonEnabled(true);
			setBackButtonEnabled(true);

		}

		public void next() {

			// if (itemCheckPlot == ItemEvent.SELECTED)
			// switchPanel(DynamicJWizard.PANEL_OPTION_A);

		}

		public void back() {
			switchPanel(DynamicJWizard.PANEL_CHOOSER);

		}

		public void finish() {
			switchPanel(DynamicJWizard.DISPOSE_ON_CLOSE);
		}
	}
