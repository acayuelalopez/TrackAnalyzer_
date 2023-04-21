import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class traJParametersWindow {
	static JButton okButton;
	static String minLengthTextS, windowTextS, minSegTextS, keyWord;
	static JFrame frame;
	static JTextField minLengthText, windowText, minSegText;

	public traJParametersWindow() {

	}

	public void run(String args) {
		frame = new JFrame("TraJ Classifier Parameters");
		frame.setSize(200, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);

		frame.setVisible(true);

	}

	private static void placeComponents(JPanel panel) {

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel minLengthLabel = new JLabel("Min. Tracklength: ");
		panel.add(minLengthLabel);

		minLengthText = new JTextField("10", 5);
		minLengthText.setText(SPTBatch_.pref1.get(SPTBatch_.TRACKMATE_MIN_TRACK, ""));
		panel.add(minLengthText);

		JLabel windowLabel = new JLabel("Windowsize (SPOT positions): ");
		panel.add(windowLabel);

		windowText = new JTextField("5", 5);
		windowText.setText(SPTBatch_.pref1.get(SPTBatch_.TRACKMATE_WINDOW, ""));
		panel.add(windowText);

		JLabel minSegLabel = new JLabel("Min.Segment Length: ");
		panel.add(minSegLabel);

		minSegText = new JTextField("5", 5);
		minSegText.setText(SPTBatch_.pref1.get(SPTBatch_.TRACKMATE_MIN_SEGMENT, ""));
		panel.add(minSegText);

		okButton = new JButton("OK");
		okButton.setBounds(10, 80, 80, 25);
		panel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				keyWord = "keyword";
				minLengthTextS = minLengthText.getText();
				windowTextS = windowText.getText();
				minSegTextS = minSegText.getText();
				frame.setVisible(false);

			}
		});

	}

}
