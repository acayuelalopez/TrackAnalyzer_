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
