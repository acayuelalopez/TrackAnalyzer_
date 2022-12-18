import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jgrapht.graph.DefaultWeightedEdge;

import checkable.CheckableItem;
import checkable.CheckedComboBox;
import fiji.plugin.trackmate.visualization.table.TablePanel;
import ij.IJ;

public class summaryColsWindow {
	static JButton okButton;
	static JFrame frame;
	public static CheckedComboBox comboSpots, comboLinks, comboTracks;
	static String[] columnNamesSpot = null, columnNamesLinks = null, columnNamesTracks = null;
	public static CheckableItem[] itemsSpots, itemsLinks, itemsTracks;
	static int indexSLT;
	static JComboBox combo;

	public summaryColsWindow() {

	}

	public void run(String args) {
		frame = new JFrame("Configure Summary Outputs");
		frame.setSize(200, 150);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);

		frame.setVisible(true);

	}

	private static void placeComponents(JPanel panel) {

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel paramLabel = new JLabel("Parameters: ");
		panel.add(paramLabel);

		combo = new JComboBox();
		combo.addItem("Spots");
		combo.addItem("Links");
		combo.addItem("Tracks");
		combo.setSelectedIndex(2);
		combo.setBounds(50, 50, 90, 20);
		if (SPTBatch_.checkboxSubBg.isSelected() == true) {

			columnNamesSpot = new String[] { "LABEL", "ID", "TRACK_ID", "QUALITY", "POSITION_X", "POSITION_Y",
					"POSITION_Z", "POSITION_T", "FRAME", "RADIUS", "VISIBILITY", "MANUAL_SPOT_COLOR",
					"MEAN_INTENSITY_CH1", "MEDIAN_INTENSITY_CH1", "MIN_INTENSITY_CH1", "MAX_INTENSITY_CH1",
					"TOTAL_INTENSITY_CH1", "STD_INTENSITY_CH1", "CONTRAST_CH1", "SNR_CH1", "Intensity-Bg Subtract" };
		}
		if (SPTBatch_.checkboxSubBg.isSelected() == false) {

			columnNamesSpot = new String[] { "LABEL", "ID", "TRACK_ID", "QUALITY", "POSITION_X", "POSITION_Y",
					"POSITION_Z", "POSITION_T", "FRAME", "RADIUS", "VISIBILITY", "MANUAL_SPOT_COLOR",
					"MEAN_INTENSITY_CH1", "MEDIAN_INTENSITY_CH1", "MIN_INTENSITY_CH1", "MAX_INTENSITY_CH1",
					"TOTAL_INTENSITY_CH1", "STD_INTENSITY_CH1", "CONTRAST_CH1", "SNR_CH1" };
		}
		itemsSpots = new CheckableItem[columnNamesSpot.length];
		columnNamesLinks = new String[] { "TRACK_ID", "SPOT_SOURCE_ID", "SPOT_TARGET_ID", "LINK_COST",
				"DIRECTIONAL_CHANGE_RATE", "SPEED", "DISPLACEMENT", "EDGE_TIME", "EDGE_X_LOCATION", "EDGE_Y_LOCATION",
				"EDGE_Z_LOCATION", "MANUAL_EGE_COLOR" };
		itemsLinks = new CheckableItem[columnNamesLinks.length];
		if (SPTBatch_.checkboxSubBg.isSelected() == true) {

			if (SPTBatch_.checkTracks.isSelected() == true)
				columnNamesTracks = new String[] { "LABEL", "TRACK_INDEX", "TRACK_ID", "NUMBER_SPOTS", "NUMBER_GAPS",
						"NUMBER_SPLITS", "NUMBER_MERGES", "NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION",
						"TRACK_START", "TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION", "TRACK_Y_LOCATION",
						"TRACK_Z_LOCATION", "TRACK_MEAN_SPEED", "TRACK_MAX_SPEED", "TRACK_MIN_SPEED",
						"TRACK_MEDIAN_SPEED", "TRACK_STD_SPEED", "TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED",
						"MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO", "MEAN_STRAIGHT_LINE_SPEED",
						"LINEARITY_OF_FORWARD_PROGRESSION", "MEAN_DIRECTIONAL_CHANGE_RATE", "MSD timelag=1",
						"MSD timelag=2", "MSD timelag=3", "MSD", "Intensity-Bg Subtract",
						"Intensity-Bg Subtract" + " (" + SPTBatch_.minTracksJTF + "-" + SPTBatch_.maxTracksJTF + ")",
						"Diffusion Coef.", "D1-4", "Track Length", "Motility", "Alpha", "Movement", "sMSS",
						"sMSS Movement" };

			if (SPTBatch_.checkTracks.isSelected() == false)
				columnNamesTracks = new String[] { "LABEL", "TRACK_INDEX", "TRACK_ID", "NUMBER_SPOTS", "NUMBER_GAPS",
						"NUMBER_SPLITS", "NUMBER_MERGES", "NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION",
						"TRACK_START", "TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION", "TRACK_Y_LOCATION",
						"TRACK_Z_LOCATION", "TRACK_MEAN_SPEED", "TRACK_MAX_SPEED", "TRACK_MIN_SPEED",
						"TRACK_MEDIAN_SPEED", "TRACK_STD_SPEED", "TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED",
						"MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO", "MEAN_STRAIGHT_LINE_SPEED",
						"LINEARITY_OF_FORWARD_PROGRESSION", "MEAN_DIRECTIONAL_CHANGE_RATE", "MSD timelag=1",
						"MSD timelag=2", "MSD timelag=3", "MSD", "Intensity-Bg Subtract",
						"Diffusion Coef.", "D1-4", "Track Length", "Motility", "Alpha", "Movement", "sMSS",
						"sMSS Movement" };
		}
		if (SPTBatch_.checkboxSubBg.isSelected() == false) {
			columnNamesTracks = new String[] { "LABEL", "TRACK_INDEX", "TRACK_ID", "NUMBER_SPOTS", "NUMBER_GAPS",
					"NUMBER_SPLITS", "NUMBER_MERGES", "NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION", "TRACK_START",
					"TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION", "TRACK_Y_LOCATION", "TRACK_Z_LOCATION",
					"TRACK_MEAN_SPEED", "TRACK_MAX_SPEED", "TRACK_MIN_SPEED", "TRACK_MEDIAN_SPEED", "TRACK_STD_SPEED",
					"TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED", "MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO",
					"MEAN_STRAIGHT_LINE_SPEED", "LINEARITY_OF_FORWARD_PROGRESSION", "MEAN_DIRECTIONAL_CHANGE_RATE",
					"MSD timelag=1", "MSD timelag=2", "MSD timelag=3", "MSD", "Diffusion Coef.", "D1-4",
					"Track Length", "Motility", "Alpha", "Movement", "sMSS", "sMSS Movement" };
		}
		itemsTracks = new CheckableItem[columnNamesTracks.length];
		for (int i = 0; i < columnNamesSpot.length; i++)
			itemsSpots[i] = new CheckableItem(columnNamesSpot[i], false);

		for (int i = 0; i < columnNamesLinks.length; i++)
			itemsLinks[i] = new CheckableItem(columnNamesLinks[i], false);

		for (int i = 0; i < columnNamesTracks.length; i++)
			itemsTracks[i] = new CheckableItem(columnNamesTracks[i], false);

		comboSpots = new CheckedComboBox<>(new DefaultComboBoxModel<>(itemsSpots));
		comboSpots.setOpaque(true);
		comboSpots.setToolTipText("Select parameter to build the summary for links.");
		comboSpots.setSelectedItem(itemsSpots[0]);

		comboLinks = new CheckedComboBox<>(new DefaultComboBoxModel<>(itemsLinks));
		comboLinks.setOpaque(true);
		comboLinks.setToolTipText("Select parameter to build the summary for links.");
		comboLinks.setSelectedItem(itemsLinks[0]);

		comboTracks = new CheckedComboBox<>(new DefaultComboBoxModel<>(itemsTracks));
		comboTracks.setOpaque(true);
		comboTracks.setToolTipText("Select parameter to build the summary for tracks.");
		comboTracks.setSelectedItem(itemsTracks[0]);

		panel.add(combo);

		JLabel columnParamLabel = new JLabel("Column Parameter: ");
		panel.add(columnParamLabel);

		okButton = new JButton("OK");
		okButton.setBounds(10, 80, 80, 25);
		panel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				indexSLT = combo.getSelectedIndex();
				frame.setVisible(false);

			}
		});
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (panel.getComponents().length == 5)
					panel.remove(3);
				if (combo.getSelectedIndex() == 0) {
					panel.add(comboSpots, 3);
				}
				if (combo.getSelectedIndex() == 1) {
					panel.add(comboLinks, 3);
				}
				if (combo.getSelectedIndex() == 2) {
					panel.add(comboTracks, 3);

				}
			}
		});

	}

}