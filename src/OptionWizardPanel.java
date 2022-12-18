import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.frame.RoiManager;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.example.DynamicJWizard;

public class OptionWizardPanel extends LabelWizardPanel {

	public double maxDomainSpot, maxRangeSpot, maxDomainTrack, maxRangeTrack;
	public IntervalMarker markerRangeSpot, markerDomainSpot, markerRangeTrack, markerDomainTrack;
	private JSpinner filterOrderSpot, filterOrderTrack;// filterRangeSpot, filterDomainSpot, filterRangeTrack,
														// filterDomainTrack,
	// public JSlider sliderDomainSpot, sliderRangeSpot, sliderDomainTrack,
	// sliderRangeTrack;
	static int selectedIndexCh2, selectedIndexCh3, numCh2Positive, numCh3Positive, countSenescentNumber, lhCountAll,
			hhCountAll, llCountAll, hlCountAll, lhCountNID, hhCountNID, llCountNID, hlCountNID, lhCountClass,
			hhCountClass, llCountClass, hlCountClass, selectedIndexDomainSpot, selectedIndexRangeSpot,
			selectedIndexDomainTrack, selectedIndexRangeTrack;
	static JLabel scatLabel, sumLabel, labelScoresSpot, labelScoresTrack;
	JComboBox<String> comboFeatureDomainSpot, comboFeatureRangeSpot, comboFeatureDomainTrack, comboFeatureRangeTrack,
			comboClassSpot, comboParamSpot, comboClassTrack, comboParamTrack, comboRegressionSpot, comboRegressionTrack;
	List<String> itemFiltersSpot, itemFiltersTrack;
	STScatterPlot scatterPlot;
	JButton refreshButtonSpot, zoomInSpot, zoomOutSpot, refreshButtonTrack, zoomInTrack, zoomOutTrack;
	STScatterPlot sp2Spot, sp2Track;
	ChartPanel scatterPlotSpot, scatterPlotTrack;
	static JPanel regressionPanelSpot, regressionPanelTrack;
	List<Double> dataToStatisticsSpot, dataToStatisticsTrack;
	DefaultTableModel modelSpot, modelTrack;
	Thread refreshSpotThread, refreshTrackThread, zoomInSpotThread, zoomInTrackThread, zoomOutSpotThread,
			zoomOutTrackThread, comboRegSpotThread, comboRegTrackThread, comboClassSpotThread, comboClassTrackThread;

	public OptionWizardPanel(JWizardComponents wizardComponents, String option) {
		super(wizardComponents, "");
		setPanelTitle("");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel chartPanel2Spot = new JPanel();
		markerRangeSpot = new IntervalMarker(0, 0, new Color(229, 255, 204), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(1.5f), 0.6f);
		markerDomainSpot = new IntervalMarker(0, 0, new Color(229, 255, 204), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(1.5f), 0.5f);
		markerRangeTrack = new IntervalMarker(0, 0, new Color(229, 255, 204), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(1.5f), 0.6f);
		markerDomainTrack = new IntervalMarker(0, 0, new Color(229, 255, 204), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(1.5f), 0.5f);
		sp2Spot = new STScatterPlot("");
		scatterPlotSpot = sp2Spot.createScatterChartPanelInitial("", "", new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0)),
				new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0)), markerRangeSpot, markerDomainSpot,
				new Double[][] { { 0.0 }, { 0.0 } }, new Double[][] { { 0.0 }, { 0.0 } });
		refreshButtonSpot = new JButton("");
		refreshButtonSpot.setIcon(FirstWizardPanel.refreshCell);
		refreshButtonSpot.setToolTipText("Click this button to refresh scatter-plot.");
		zoomInSpot = new JButton("");
		ImageIcon iconZoomIn = FirstWizardPanel.createImageIcon("images/zoomin.png");
		Icon zoomInCell = new ImageIcon(iconZoomIn.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		zoomInSpot.setIcon(zoomInCell);
		zoomInSpot.setToolTipText("Click this button to zoom in Chart");
		zoomOutSpot = new JButton("");
		ImageIcon iconZoomOut = FirstWizardPanel.createImageIcon("images/zoomout.png");
		Icon zoomOutCell = new ImageIcon(iconZoomOut.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		zoomOutSpot.setIcon(zoomOutCell);
		zoomOutSpot.setToolTipText("Click this button to zoom out Chart");
		itemFiltersSpot = new ArrayList<String>();
		itemFiltersTrack = new ArrayList<String>();
		for (int i = 2; i < FirstWizardPanel.columnNamesSpot.length; i++)
			itemFiltersSpot.add(FirstWizardPanel.columnNamesSpot[i].toString());
		for (int i = 0; i < ChooserWizardPanel.columnNamesTrack.length; i++)
			itemFiltersTrack.add(ChooserWizardPanel.columnNamesTrack[i].toString());
		comboFeatureDomainSpot = new JComboBox<String>();
		comboFeatureDomainSpot.setPreferredSize(new Dimension(110, 20));
		for (int i = 0; i < itemFiltersSpot.size(); i++)
			comboFeatureDomainSpot.addItem((String) itemFiltersSpot.get(i));
		comboFeatureDomainSpot.setOpaque(true);
		scatterPlot = new STScatterPlot("holaa");
		chartPanel2Spot.add(scatterPlotSpot);
		filterOrderSpot = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		filterOrderSpot.setPreferredSize(new Dimension(60, 20));
		filterOrderSpot.setEnabled(false);
//		filterRangeSpot = new JSpinner(new SpinnerNumberModel(30, 0, 100000000, 1));
//		filterRangeSpot.setPreferredSize(new Dimension(40, 20));
		// sliderRangeSpot = new JSlider(JSlider.VERTICAL, 0, 100000000, 50);
		JPanel filtersMaxSpot = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		filterDomainSpot = new JSpinner(new SpinnerNumberModel(50, 0, 100000000, 1));
//		filterDomainSpot.setPreferredSize(new Dimension(60, 20));
		// sliderDomainSpot = new JSlider(JSlider.HORIZONTAL, 0, 100000000, 150);
		// filtersMaxSpot.add(sliderDomainSpot);
		filtersMaxSpot.add(Box.createHorizontalStrut(2));
		// filtersMaxSpot.add(filterDomainSpot);

		comboFeatureRangeSpot = new JComboBox<String>();
		comboFeatureRangeSpot.setPreferredSize(new Dimension(110, 20));
		for (int i = 0; i < itemFiltersSpot.size(); i++)
			comboFeatureRangeSpot.addItem((String) itemFiltersSpot.get(i));
		comboFeatureRangeSpot.setOpaque(true);
		filtersMaxSpot.add(new JLabel("X :  "));
		filtersMaxSpot.add(comboFeatureDomainSpot);
		filtersMaxSpot.add(new JLabel("   Y :  "));
		filtersMaxSpot.add(comboFeatureRangeSpot);
		JPanel rangePanelFSpot = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// JPanel rangePanelBoxSpot = new JPanel();
		// rangePanelBoxSpot.setLayout(new BoxLayout(rangePanelBoxSpot,
		// BoxLayout.Y_AXIS));
		// rangePanelBoxSpot.add(sliderRangeSpot);
		// rangePanelBoxSpot.add(Box.createVerticalStrut(2));
		// rangePanelBoxSpot.add(filterRangeSpot);
		// rangePanelBoxSpot.add(comboFeatureRangeSpot);
		JPanel chartDomainPanelBoxSpot = new JPanel();
		chartDomainPanelBoxSpot.setLayout(new BoxLayout(chartDomainPanelBoxSpot, BoxLayout.Y_AXIS));
		chartDomainPanelBoxSpot.add(Box.createVerticalStrut(10));
		chartDomainPanelBoxSpot.add(chartPanel2Spot);
		chartDomainPanelBoxSpot.add(filtersMaxSpot);
		// controlPanel2Spot = sp2Spot.createControlPanel();
		// chartDomainPanelBoxSpot.add(controlPanel2Spot);
		// rangePanelFSpot.add(rangePanelBoxSpot);
		rangePanelFSpot.add(chartDomainPanelBoxSpot);
		JPanel buttonBox = new JPanel();
		buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));
		JPanel refreshButtonPanelSpot = new JPanel(new FlowLayout(FlowLayout.LEFT));
		refreshButtonPanelSpot.add(refreshButtonSpot);
		JPanel zoomOutButtonPanelSpot = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zoomOutButtonPanelSpot.add(zoomOutSpot);
		JPanel zoomInButtonPanelSpot = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zoomInButtonPanelSpot.add(zoomInSpot);
		buttonBox.add(zoomInButtonPanelSpot);
		buttonBox.add(zoomOutButtonPanelSpot);
		buttonBox.add(refreshButtonPanelSpot);
		comboRegressionSpot = new JComboBox<String>();
		comboRegressionSpot.setPreferredSize(new Dimension(90, 20));
		comboRegressionSpot.addItem("Linear");
		comboRegressionSpot.addItem("Polynomial");
		comboRegressionSpot.addItem("Power");
		comboRegressionSpot.addItem("Logarithmic");
		comboRegressionSpot.addItem("Exponential");

		comboRegressionSpot.setSelectedIndex(0);
		comboRegressionSpot.setOpaque(true);
		JPanel regreOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		regreOrderPanel.add(comboRegressionSpot);
		JPanel filterOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterOrderPanel.add(filterOrderSpot);
		buttonBox.add(regreOrderPanel);
		buttonBox.add(filterOrderPanel);
		regressionPanelSpot = new JPanel();
		regressionPanelSpot.setBorder(BorderFactory.createTitledBorder("Reg.Params"));
		regressionPanelSpot.setPreferredSize(new Dimension(comboRegressionSpot.getWidth() + 10, 35));
		rangePanelFSpot.add(buttonBox);
		JPanel spotPanel = new JPanel();
		spotPanel.add(rangePanelFSpot);
		spotPanel.setBorder(BorderFactory.createTitledBorder(""));
		// spotPanel.setPreferredSize(new Dimension(590, 280));
		JPanel spotStatistics = new JPanel();
		spotStatistics.setLayout(new BoxLayout(spotStatistics, BoxLayout.Y_AXIS));
		JPanel spotStatisticsFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel classParamSpot = new JPanel(new FlowLayout(FlowLayout.LEFT));
		comboClassSpot = new JComboBox<String>();
		comboClassSpot.setPreferredSize(new Dimension(120, 20));
		for (int i = 2; i < FirstWizardPanel.columnNamesSpot.length; i++)
			comboClassSpot.addItem((String) FirstWizardPanel.columnNamesSpot[i]);
		comboClassSpot.setOpaque(true);
		comboClassSpot.setToolTipText("Choose selected items for descriptive statistics.");
		comboParamSpot = new JComboBox<String>();
		comboParamSpot.setPreferredSize(new Dimension(120, 20));
		for (int i = 2; i < FirstWizardPanel.columnNamesSpot.length; i++)
			comboParamSpot.addItem((String) FirstWizardPanel.columnNamesSpot[i]);
		comboParamSpot.setOpaque(true);
		comboParamSpot.setToolTipText("Choose a spot parameter for descriptive statistics.");
		classParamSpot.add(comboClassSpot);
		classParamSpot.add(comboParamSpot);
		labelScoresSpot = new JLabel("SCORES");
		labelScoresSpot.setFont(new Font("Verdana", Font.BOLD, 20));
		spotStatisticsFlow.add(labelScoresSpot);
		spotStatistics.add(Box.createVerticalStrut(60));
		spotStatistics.add(spotStatisticsFlow);
		spotStatistics.add(new JSeparator(JSeparator.HORIZONTAL));
		spotStatistics.add(Box.createVerticalStrut(20));
		spotStatistics.add(classParamSpot);
		spotStatistics.add(Box.createVerticalStrut(20));
		spotStatistics.add(new JSeparator(JSeparator.HORIZONTAL));
		String data[][] = { { "", "", "", "", "", "", "", "", "", "", "" } };
		String column[] = { "Mean ", "Std.Error ", "Median ", "Std.Dev ", "Variance ", "Kurtosis ", "Skewness ", "Min ",
				"Max ", "Sum ", "Count " };

		JTable table = new JTable();
		modelSpot = new DefaultTableModel(data, column) {

			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}

				return super.getColumnClass(column);
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(modelSpot);
		table.setRowHeight(60);
		JScrollPane sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(500, 100));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		for (int u = 0; u < table.getColumnCount(); u++) {
			table.getColumnModel().getColumn(u).setMinWidth(100);
			table.getColumnModel().getColumn(u).setMaxWidth(100);
			table.getColumnModel().getColumn(u).setPreferredWidth(100);

		}
		spotStatistics.add(Box.createVerticalStrut(20));
		spotStatistics.add(sp);
		spotStatistics.add(Box.createVerticalStrut(20));
		spotStatistics.add(new JSeparator(JSeparator.HORIZONTAL));
		JButton plotButton = new JButton();
		ImageIcon iconPlot = FirstWizardPanel.createImageIcon("images/plot.jpg");
		Icon plotCell = new ImageIcon(iconPlot.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		plotButton.setIcon(plotCell);
		plotButton.setToolTipText("Click to export scatter plot.");
		JPanel panelPlot = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPlot.add(plotButton);
		JButton csvButton = new JButton();
		ImageIcon iconCsv = FirstWizardPanel.createImageIcon("images/csv.png");
		Icon csvCell = new ImageIcon(iconCsv.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		csvButton.setIcon(csvCell);
		csvButton.setToolTipText("Click to export your spots statistics table.");
		JPanel panelCsv = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCsv.add(csvButton);
		JPanel panelPngCsv = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPngCsv.add(panelPlot);
		panelPngCsv.add(panelCsv);
		spotStatistics.add(panelPngCsv);
		spotPanel.add(spotStatistics);

		/////////////////////////////////////// TRACKS/////////////////////////////////////////////////////////////////

		JPanel chartPanel2Track = new JPanel();
		sp2Track = new STScatterPlot("");
		scatterPlotTrack = sp2Track.createScatterChartPanelInitial("", "",
				new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0)), new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0)),
				markerRangeTrack, markerDomainTrack, new Double[][] { { 0.0 }, { 0.0 } },
				new Double[][] { { 0.0 }, { 0.0 } });
		refreshButtonTrack = new JButton("");
		refreshButtonTrack.setIcon(FirstWizardPanel.refreshCell);
		refreshButtonTrack.setToolTipText("Click this button to refresh scatter-plot.");
		zoomInTrack = new JButton("");
		zoomInTrack.setIcon(zoomInCell);
		zoomInTrack.setToolTipText("Click this button to zoom in Chart");
		zoomOutTrack = new JButton("");
		zoomOutTrack.setIcon(zoomOutCell);
		zoomOutTrack.setToolTipText("Click this button to zoom out Chart");
		for (int i = 0; i < ChooserWizardPanel.columnNamesTrack.length; i++)
			itemFiltersTrack.add(ChooserWizardPanel.columnNamesTrack[i].toString());
		comboFeatureDomainTrack = new JComboBox<String>();
		comboFeatureDomainTrack.setPreferredSize(new Dimension(110, 20));
		for (int i = 3; i < itemFiltersTrack.size(); i++)
			comboFeatureDomainTrack.addItem((String) itemFiltersTrack.get(i));
		comboFeatureDomainTrack.setOpaque(true);
		// scatterPlot = new STScatterPlot("holaa");
		chartPanel2Track.add(scatterPlotTrack);
		filterOrderTrack = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		filterOrderTrack.setPreferredSize(new Dimension(60, 20));
		filterOrderTrack.setEnabled(false);
		JPanel filtersMaxTrack = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filtersMaxTrack.add(Box.createHorizontalStrut(2));

		comboFeatureRangeTrack = new JComboBox<String>();
		comboFeatureRangeTrack.setPreferredSize(new Dimension(110, 20));
		for (int i = 3; i < itemFiltersTrack.size(); i++)
			comboFeatureRangeTrack.addItem((String) itemFiltersTrack.get(i));
		comboFeatureRangeTrack.setOpaque(true);
		filtersMaxTrack.add(new JLabel("X :  "));
		filtersMaxTrack.add(comboFeatureDomainTrack);
		filtersMaxTrack.add(new JLabel("   Y :  "));
		filtersMaxTrack.add(comboFeatureRangeTrack);
		JPanel rangePanelFTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel chartDomainPanelBoxTrack = new JPanel();
		chartDomainPanelBoxTrack.setLayout(new BoxLayout(chartDomainPanelBoxTrack, BoxLayout.Y_AXIS));
		chartDomainPanelBoxTrack.add(Box.createVerticalStrut(10));
		chartDomainPanelBoxTrack.add(chartPanel2Track);
		chartDomainPanelBoxTrack.add(filtersMaxTrack);
		rangePanelFTrack.add(chartDomainPanelBoxTrack);
		JPanel buttonBoxTrack = new JPanel();
		buttonBoxTrack.setLayout(new BoxLayout(buttonBoxTrack, BoxLayout.Y_AXIS));
		JPanel refreshButtonPanelTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		refreshButtonPanelTrack.add(refreshButtonTrack);
		JPanel zoomOutButtonPanelTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zoomOutButtonPanelTrack.add(zoomOutTrack);
		JPanel zoomInButtonPanelTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zoomInButtonPanelTrack.add(zoomInTrack);
		buttonBoxTrack.add(zoomInButtonPanelTrack);
		buttonBoxTrack.add(zoomOutButtonPanelTrack);
		buttonBoxTrack.add(refreshButtonPanelTrack);
		comboRegressionTrack = new JComboBox<String>();
		comboRegressionTrack.setPreferredSize(new Dimension(90, 20));
		comboRegressionTrack.addItem("Linear");
		comboRegressionTrack.addItem("Polynomial");
		comboRegressionTrack.addItem("Power");
		comboRegressionTrack.addItem("Logarithmic");
		comboRegressionTrack.addItem("Exponential");

		comboRegressionTrack.setSelectedIndex(0);
		comboRegressionTrack.setOpaque(true);
		JPanel regreOrderPanelTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		regreOrderPanelTrack.add(comboRegressionTrack);
		JPanel filterOrderPanelTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterOrderPanelTrack.add(filterOrderTrack);
		buttonBoxTrack.add(regreOrderPanelTrack);
		buttonBoxTrack.add(filterOrderPanelTrack);
		regressionPanelTrack = new JPanel();
		regressionPanelTrack.setBorder(BorderFactory.createTitledBorder("Reg.Params"));
		regressionPanelTrack.setPreferredSize(new Dimension(comboRegressionTrack.getWidth() + 10, 35));
		rangePanelFTrack.add(buttonBoxTrack);

		JPanel trackPanel = new JPanel();
		trackPanel.add(rangePanelFTrack);
		trackPanel.setBorder(BorderFactory.createTitledBorder(""));
		// trackPanel.setPreferredSize(new Dimension(590, 280));
		JPanel trackStatistics = new JPanel();
		trackStatistics.setLayout(new BoxLayout(trackStatistics, BoxLayout.Y_AXIS));
		JPanel trackStatisticsFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel classParamTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		comboClassTrack = new JComboBox<String>();
		comboClassTrack.setPreferredSize(new Dimension(120, 20));
		for (int i = 3; i < ChooserWizardPanel.columnNamesTrack.length; i++)
			comboClassTrack.addItem((String) ChooserWizardPanel.columnNamesTrack[i]);
		comboClassTrack.setOpaque(true);
		comboClassTrack.setToolTipText("Choose selected items for descriptive statistics.");
		comboParamTrack = new JComboBox<String>();
		comboParamTrack.setPreferredSize(new Dimension(120, 20));
		for (int i = 3; i < ChooserWizardPanel.columnNamesTrack.length; i++)
			comboParamTrack.addItem((String) ChooserWizardPanel.columnNamesTrack[i]);
		comboParamTrack.setOpaque(true);
		comboParamTrack.setToolTipText("Choose a track parameter for descriptive statistics.");
		classParamTrack.add(comboClassTrack);
		classParamTrack.add(comboParamTrack);
		labelScoresTrack = new JLabel("SCORES");
		labelScoresTrack.setFont(new Font("Verdana", Font.BOLD, 20));
		trackStatisticsFlow.add(labelScoresTrack);
		trackStatistics.add(Box.createVerticalStrut(60));
		trackStatistics.add(trackStatisticsFlow);
		trackStatistics.add(new JSeparator(JSeparator.HORIZONTAL));
		trackStatistics.add(Box.createVerticalStrut(20));
		trackStatistics.add(classParamTrack);
		trackStatistics.add(Box.createVerticalStrut(20));
		trackStatistics.add(new JSeparator(JSeparator.HORIZONTAL));
		String dataTrack[][] = { { "", "", "", "", "", "", "", "", "", "", "" } };
		String columnTrack[] = { "Mean ", "Std.Error ", "Median ", "Std.Dev ", "Variance ", "Kurtosis ", "Skewness ",
				"Min ", "Max ", "Sum ", "Count " };

		JTable tableTrack = new JTable();
		modelTrack = new DefaultTableModel(dataTrack, columnTrack) {

			@Override
			public Class<?> getColumnClass(int columnTrack) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, columnTrack);
					if (value != null) {
						return getValueAt(0, columnTrack).getClass();
					}
				}

				return super.getColumnClass(columnTrack);
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		tableTrack.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableTrack.setModel(modelTrack);
		tableTrack.setRowHeight(60);
		JScrollPane spTrack = new JScrollPane(tableTrack);
		spTrack.setPreferredSize(new Dimension(500, 100));
		spTrack.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spTrack.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		for (int u = 0; u < tableTrack.getColumnCount(); u++) {
			tableTrack.getColumnModel().getColumn(u).setMinWidth(100);
			tableTrack.getColumnModel().getColumn(u).setMaxWidth(100);
			tableTrack.getColumnModel().getColumn(u).setPreferredWidth(100);

		}
		trackStatistics.add(Box.createVerticalStrut(20));
		trackStatistics.add(spTrack);
		trackStatistics.add(Box.createVerticalStrut(20));
		trackStatistics.add(new JSeparator(JSeparator.HORIZONTAL));
		JButton plotButtonTrack = new JButton();
		plotButtonTrack.setIcon(plotCell);
		plotButtonTrack.setToolTipText("Click to export scatter plot.");
		JPanel panelPlotTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPlotTrack.add(plotButtonTrack);
		JButton csvButtonTrack = new JButton();
		csvButtonTrack.setIcon(csvCell);
		csvButtonTrack.setToolTipText("Click to export your tracks statistics table.");
		JPanel panelCsvTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCsvTrack.add(csvButtonTrack);
		JPanel panelPngCsvTrack = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPngCsvTrack.add(panelPlotTrack);
		panelPngCsvTrack.add(panelCsvTrack);
		trackStatistics.add(panelPngCsvTrack);
		trackPanel.add(trackStatistics);

		JTabbedPane maintabbedPane = new JTabbedPane(JTabbedPane.TOP);
		maintabbedPane.addTab("SPOTS ", FirstWizardPanel.iconSpotCell, spotPanel, "Scatter-Plot for spots");
		maintabbedPane.addTab("TRACKS ", ChooserWizardPanel.iconTrackCell, trackPanel, "Scatter-Plot for tracks");

		maintabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.add(maintabbedPane, BorderLayout.CENTER);

		plotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Specify a directory to save plot as .png file");
				int userSelection = fileChooser.showSaveDialog(new JFrame());

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					BufferedImage chartImage = STScatterPlot.plot.getChart().createBufferedImage(1024, 768);
					try {
						ImageIO.write(chartImage, "png", new File(fileToSave.getAbsolutePath() + File.separator
								+ "SpotPlot for " + IJ.getImage().getShortTitle() + ".png"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});
		csvButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> columnSpotHead = new ArrayList<String>();
				for (int j = 0; j < modelSpot.getColumnCount(); j++)
					columnSpotHead.add(modelSpot.getColumnName(j));

				ResultsTable rt = new ResultsTable(modelSpot.getRowCount());
				if (rt != null)
					rt.reset();

				for (int i = 0; i < modelSpot.getRowCount(); i++)
					for (int j = 0; j < modelSpot.getColumnCount(); j++)
						rt.setValue(columnSpotHead.get(j), i, modelSpot.getValueAt(i, j).toString());
				JFrame pngFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Specify a directory to save csv file");
				int userSelection = fileChooser.showSaveDialog(pngFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					try {
						rt.saveAs(fileToSave.getAbsolutePath() + File.separator + "SpotStatistics for-"
								+ IJ.getImage().getShortTitle() + ".csv");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		});

		refreshButtonSpot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshSpotThread = new Thread(new Runnable() {
					public void run() {
						refreshActionSpot();
					}
				});
				refreshSpotThread.start();
			}
		});
		refreshButtonTrack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshTrackThread = new Thread(new Runnable() {
					public void run() {
						refreshActionTrack();
					}
				});
				refreshTrackThread.start();
			}
		});
		zoomInSpot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomInSpotThread = new Thread(new Runnable() {
					public void run() {
						zoomInSpot.setActionCommand(ChartPanel.ZOOM_IN_BOTH_COMMAND);
						zoomInSpot.addActionListener(scatterPlotSpot);
					}
				});
				zoomInSpotThread.start();
			}
		});
		zoomOutSpot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomOutSpotThread = new Thread(new Runnable() {
					public void run() {
						zoomOutSpot.setActionCommand(ChartPanel.ZOOM_OUT_BOTH_COMMAND);
						zoomOutSpot.addActionListener(scatterPlotSpot);
					}
				});
				zoomOutSpotThread.start();
			}
		});
		zoomInTrack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomInTrackThread = new Thread(new Runnable() {
					public void run() {
						zoomInSpot.setActionCommand(ChartPanel.ZOOM_IN_BOTH_COMMAND);
						zoomInSpot.addActionListener(scatterPlotTrack);
					}
				});
				zoomInTrackThread.start();
			}
		});
		zoomOutTrack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomOutTrackThread = new Thread(new Runnable() {
					public void run() {
						zoomOutTrack.setActionCommand(ChartPanel.ZOOM_OUT_BOTH_COMMAND);
						zoomOutTrack.addActionListener(scatterPlotTrack);
					}
				});
				zoomOutTrackThread.start();
			}
		});
		comboRegressionSpot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboRegSpotThread = new Thread(new Runnable() {
					public void run() {
						if (comboRegressionSpot.getSelectedIndex() == 1)
							filterOrderSpot.setEnabled(true);
						if (comboRegressionSpot.getSelectedIndex() != 1)
							filterOrderSpot.setEnabled(false);
					}
				});
				comboRegSpotThread.start();
			}
		});
		comboRegressionTrack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboRegTrackThread = new Thread(new Runnable() {
					public void run() {
						if (comboRegressionTrack.getSelectedIndex() == 1)
							filterOrderTrack.setEnabled(true);
						if (comboRegressionTrack.getSelectedIndex() != 1)
							filterOrderTrack.setEnabled(false);
					}
				});
				comboRegTrackThread.start();
			}
		});
		comboClassSpot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboClassSpotThread = new Thread(new Runnable() {
					public void run() {

						if (comboClassSpot.getSelectedIndex() == 0) {
							dataToStatisticsSpot = new ArrayList<Double>();
							for (int i = 0; i < FirstWizardPanel.modelSpot.getRowCount(); i++)
								if (FirstWizardPanel.modelSpot.getValueAt(i,
										FirstWizardPanel.modelSpot.getColumnCount() - 1) == Boolean.TRUE)
									dataToStatisticsSpot.add(Double.valueOf(FirstWizardPanel.modelSpot
											.getValueAt(i, comboParamSpot.getSelectedIndex() + 3).toString()));
							descriptiveStatisticsActionSpot();
						}
						if (comboClassSpot.getSelectedIndex() == 1) {
							dataToStatisticsSpot = new ArrayList<Double>();
							for (int i = 0; i < FirstWizardPanel.modelSpot.getRowCount(); i++)
								if (FirstWizardPanel.modelSpot.getValueAt(i,
										FirstWizardPanel.modelSpot.getColumnCount() - 1) == Boolean.TRUE)
									if (((JLabel) FirstWizardPanel.modelSpot.getValueAt(i, 0)).getText() == "")
										dataToStatisticsSpot.add(Double.valueOf(FirstWizardPanel.modelSpot
												.getValueAt(i, comboParamSpot.getSelectedIndex() + 3).toString()));
							descriptiveStatisticsActionSpot();

						}
						if (comboClassSpot.getSelectedIndex() == 2) {
							dataToStatisticsSpot = new ArrayList<Double>();
							for (int i = 0; i < FirstWizardPanel.modelSpot.getRowCount(); i++)
								if (FirstWizardPanel.modelSpot.getValueAt(i,
										FirstWizardPanel.modelSpot.getColumnCount() - 1) == Boolean.TRUE)
									if (((JLabel) FirstWizardPanel.modelSpot.getValueAt(i, 0))
											.getText() == comboClassSpot.getSelectedItem().toString())
										dataToStatisticsSpot.add(Double.valueOf(FirstWizardPanel.modelSpot
												.getValueAt(i, comboParamSpot.getSelectedIndex() + 3).toString()));
							descriptiveStatisticsActionSpot();

						}
					}
				});
				comboClassSpotThread.start();

			}
		});
		comboClassTrack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboClassTrackThread = new Thread(new Runnable() {
					public void run() {

						if (comboClassTrack.getSelectedIndex() == 0) {
							dataToStatisticsTrack = new ArrayList<Double>();
							for (int i = 0; i < ChooserWizardPanel.modelTrack.getRowCount(); i++)
								if (ChooserWizardPanel.modelTrack.getValueAt(i,
										ChooserWizardPanel.modelTrack.getColumnCount() - 1) == Boolean.TRUE)
									dataToStatisticsTrack.add(Double.valueOf(ChooserWizardPanel.modelTrack
											.getValueAt(i, comboParamTrack.getSelectedIndex() + 3).toString()));
							descriptiveStatisticsActionTrack();
						}
						if (comboClassTrack.getSelectedIndex() == 1) {
							dataToStatisticsTrack = new ArrayList<Double>();
							for (int i = 0; i < ChooserWizardPanel.modelTrack.getRowCount(); i++)
								if (ChooserWizardPanel.modelTrack.getValueAt(i,
										ChooserWizardPanel.modelTrack.getColumnCount() - 1) == Boolean.TRUE)
									if (((JLabel) ChooserWizardPanel.modelTrack.getValueAt(i, 0)).getText() == "")
										dataToStatisticsTrack.add(Double.valueOf(ChooserWizardPanel.modelTrack
												.getValueAt(i, comboParamTrack.getSelectedIndex() + 3).toString()));
							descriptiveStatisticsActionTrack();

						}
						if (comboClassTrack.getSelectedIndex() == 2) {
							dataToStatisticsTrack = new ArrayList<Double>();
							for (int i = 0; i < ChooserWizardPanel.modelTrack.getRowCount(); i++)
								if (ChooserWizardPanel.modelTrack.getValueAt(i,
										ChooserWizardPanel.modelTrack.getColumnCount() - 1) == Boolean.TRUE)
									if (((JLabel) ChooserWizardPanel.modelTrack.getValueAt(i, 0))
											.getText() == comboClassTrack.getSelectedItem().toString())
										dataToStatisticsTrack.add(Double.valueOf(ChooserWizardPanel.modelTrack
												.getValueAt(i, comboParamTrack.getSelectedIndex() + 3).toString()));
							descriptiveStatisticsActionTrack();

						}

					}
				});
				comboClassTrackThread.start();
			}
		});

	}

	public void descriptiveStatisticsActionSpot() {

		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getMean() * 1000d)
				/ 1000d), 0, 0);
		modelSpot.setValueAt(String.valueOf(Math.round(new DescriptiveStatistics(
				dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getStandardDeviation()
				/ new DescriptiveStatistics(dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray())
						.getN()
				* 1000d) / 1000d), 0, 1);

		modelSpot.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray())
						.getPercentile(50) * 1000d)
				/ 1000d), 0, 2);
		modelSpot.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray())
						.getStandardDeviation() * 1000d)
				/ 1000d), 0, 3);
		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getVariance() * 1000d)
				/ 1000d), 0, 4);
		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getKurtosis() * 1000d)
				/ 1000d), 0, 5);
		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getSkewness() * 1000d)
				/ 1000d), 0, 6);
		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getMin() * 1000d)
				/ 1000d), 0, 7);
		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getMax() * 1000d)
				/ 1000d), 0, 8);
		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getSum() * 1000d)
				/ 1000d), 0, 9);

		modelSpot.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsSpot.stream().mapToDouble(Double::doubleValue).toArray()).getN() * 1000d)
				/ 1000d), 0, 10);

	}

	public void descriptiveStatisticsActionTrack() {

		modelTrack.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray()).getMean() * 1000d)
				/ 1000d), 0, 0);
		modelTrack.setValueAt(String.valueOf(Math.round(new DescriptiveStatistics(
				dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray()).getStandardDeviation()
				/ new DescriptiveStatistics(dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray())
						.getN()
				* 1000d) / 1000d), 0, 1);

		modelTrack.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray())
						.getPercentile(50) * 1000d)
				/ 1000d), 0, 2);
		modelTrack.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray())
						.getStandardDeviation() * 1000d)
				/ 1000d), 0, 3);
		modelTrack.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray())
						.getVariance() * 1000d)
				/ 1000d), 0, 4);
		modelTrack.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray())
						.getKurtosis() * 1000d)
				/ 1000d), 0, 5);
		modelTrack.setValueAt(String.valueOf(Math.round(
				new DescriptiveStatistics(dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray())
						.getSkewness() * 1000d)
				/ 1000d), 0, 6);
		modelTrack.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray()).getMin() * 1000d)
				/ 1000d), 0, 7);
		modelTrack.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray()).getMax() * 1000d)
				/ 1000d), 0, 8);
		modelTrack.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray()).getSum() * 1000d)
				/ 1000d), 0, 9);

		modelTrack.setValueAt(String.valueOf(Math
				.round(new DescriptiveStatistics(
						dataToStatisticsTrack.stream().mapToDouble(Double::doubleValue).toArray()).getN() * 1000d)
				/ 1000d), 0, 10);

	}

	public void refreshActionSpot() {

		comboClassSpot.removeAllItems();
		comboClassSpot.addItem("All items");
		comboClassSpot.addItem("No Identified");

		if (ColorEditorSpot.modelC.getRowCount() > 0) {
			for (int i = 0; i < ColorEditorSpot.modelC.getRowCount(); i++)
				comboClassSpot.addItem(((JLabel) ColorEditorSpot.modelC.getValueAt(i, 0)).getText());

		}
		int rowCount = FirstWizardPanel.tableSpot.getRowCount();
		int columnCount = FirstWizardPanel.tableSpot.getColumnCount();
		selectedIndexDomainSpot = comboFeatureDomainSpot.getSelectedIndex();
		selectedIndexRangeSpot = comboFeatureRangeSpot.getSelectedIndex();
		List<Double> valuesDomainSpot = new ArrayList<Double>();
		List<Double> valuesRangeSpot = new ArrayList<Double>();
		Object[][] dataSpot = new Object[rowCount][columnCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				dataSpot[i][j] = FirstWizardPanel.tableSpot.getValueAt(i, j);

			}
			valuesDomainSpot.add(Double.parseDouble((dataSpot[i][selectedIndexDomainSpot + 4]).toString()));
			valuesRangeSpot.add(Double.parseDouble((dataSpot[i][selectedIndexRangeSpot + 4]).toString()));
		}
		if (valuesDomainSpot.isEmpty() == Boolean.TRUE) {
			IJ.error("You should have your spot analysis done. Please go backwards.");
		} else {

			maxDomainSpot = Collections.max(valuesDomainSpot);
			maxRangeSpot = Collections.max(valuesRangeSpot);
//			sliderDomainSpot.setMinimum(0);
//			sliderDomainSpot.setMaximum((int) maxDomainSpot);
//			sliderRangeSpot.setMinimum(0);
//			sliderRangeSpot.setMaximum((int) maxRangeSpot);

			List<Color> listColorSpot = new ArrayList<Color>();
			for (int i = 0; i < FirstWizardPanel.modelSpot.getRowCount(); i++)
				listColorSpot.add(((JLabel) FirstWizardPanel.modelSpot.getValueAt(i,
						FirstWizardPanel.tableSpot.convertColumnIndexToModel(1))).getBackground());
			Color[] classColorSpot = new Color[listColorSpot.size()];
			listColorSpot.toArray(classColorSpot);
			if (comboRegressionSpot.getSelectedIndex() == 0)
				sp2Spot.addScatterPlotSeriesLinear(comboFeatureDomainSpot.getSelectedItem().toString(),
						comboFeatureRangeSpot.getSelectedItem().toString(), valuesDomainSpot, valuesRangeSpot,
						markerRangeSpot, markerDomainSpot, dataSpot, classColorSpot);
			if (comboRegressionSpot.getSelectedIndex() == 1)
				sp2Spot.addScatterPlotSeriesPolynomial(comboFeatureDomainSpot.getSelectedItem().toString(),
						comboFeatureRangeSpot.getSelectedItem().toString(), valuesDomainSpot, valuesRangeSpot,
						markerRangeSpot, markerDomainSpot, dataSpot, classColorSpot, (int) filterOrderSpot.getValue());
			if (comboRegressionSpot.getSelectedIndex() == 2)
				sp2Spot.addScatterPlotSeriesPower(comboFeatureDomainSpot.getSelectedItem().toString(),
						comboFeatureRangeSpot.getSelectedItem().toString(), valuesDomainSpot, valuesRangeSpot,
						markerRangeSpot, markerDomainSpot, dataSpot, classColorSpot);
			if (comboRegressionSpot.getSelectedIndex() == 3)
				sp2Spot.addScatterPlotSeriesLogarithmic(comboFeatureDomainSpot.getSelectedItem().toString(),
						comboFeatureRangeSpot.getSelectedItem().toString(), valuesDomainSpot, valuesRangeSpot,
						markerRangeSpot, markerDomainSpot, dataSpot, classColorSpot);
			if (comboRegressionSpot.getSelectedIndex() == 4)
				sp2Spot.addScatterPlotSeriesExponential(comboFeatureDomainSpot.getSelectedItem().toString(),
						comboFeatureRangeSpot.getSelectedItem().toString(), valuesDomainSpot, valuesRangeSpot,
						markerRangeSpot, markerDomainSpot, dataSpot, classColorSpot);
		}
	}

	public void refreshActionTrack() {

		comboClassTrack.removeAllItems();
		comboClassTrack.addItem("All items");
		comboClassTrack.addItem("No Identified");

		if (ColorEditorTrack.modelC.getRowCount() > 0) {
			for (int i = 0; i < ColorEditorTrack.modelC.getRowCount(); i++)
				comboClassTrack.addItem(((JLabel) ColorEditorTrack.modelC.getValueAt(i, 0)).getText());

		}
		int rowCount = ChooserWizardPanel.tableTrack.getRowCount();
		int columnCount = ChooserWizardPanel.tableTrack.getColumnCount();
		selectedIndexDomainTrack = comboFeatureDomainTrack.getSelectedIndex();
		selectedIndexRangeTrack = comboFeatureRangeTrack.getSelectedIndex();
		List<Double> valuesDomainTrack = new ArrayList<Double>();
		List<Double> valuesRangeTrack = new ArrayList<Double>();
		Object[][] dataTrack = new Object[rowCount][columnCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				dataTrack[i][j] = ChooserWizardPanel.tableTrack.getValueAt(i, j);

			}
			valuesDomainTrack.add(Double.parseDouble((dataTrack[i][selectedIndexDomainTrack + 4]).toString()));
			valuesRangeTrack.add(Double.parseDouble((dataTrack[i][selectedIndexRangeTrack + 4]).toString()));
		}
		if (valuesDomainTrack.isEmpty() == Boolean.TRUE) {
			IJ.error("You should have your track analysis done. Please go backwards.");
		} else {

			maxDomainTrack = Collections.max(valuesDomainTrack);
			maxRangeTrack = Collections.max(valuesRangeTrack);
//			sliderDomainSpot.setMinimum(0);
//			sliderDomainSpot.setMaximum((int) maxDomainSpot);
//			sliderRangeSpot.setMinimum(0);
//			sliderRangeSpot.setMaximum((int) maxRangeSpot);

			List<Color> listColorTrack = new ArrayList<Color>();
			for (int i = 0; i < ChooserWizardPanel.modelTrack.getRowCount(); i++)
				listColorTrack.add(((JLabel) ChooserWizardPanel.modelTrack.getValueAt(i,
						ChooserWizardPanel.tableTrack.convertColumnIndexToModel(1))).getBackground());
			Color[] classColorTrack = new Color[listColorTrack.size()];
			listColorTrack.toArray(classColorTrack);
			if (comboRegressionTrack.getSelectedIndex() == 0)
				sp2Track.addScatterPlotSeriesLinear(comboFeatureDomainTrack.getSelectedItem().toString(),
						comboFeatureRangeTrack.getSelectedItem().toString(), valuesDomainTrack, valuesRangeTrack,
						markerRangeTrack, markerDomainTrack, dataTrack, classColorTrack);
			if (comboRegressionTrack.getSelectedIndex() == 1)
				sp2Track.addScatterPlotSeriesPolynomial(comboFeatureDomainTrack.getSelectedItem().toString(),
						comboFeatureRangeTrack.getSelectedItem().toString(), valuesDomainTrack, valuesRangeTrack,
						markerRangeTrack, markerDomainTrack, dataTrack, classColorTrack,
						(int) filterOrderTrack.getValue());
			if (comboRegressionTrack.getSelectedIndex() == 2)
				sp2Track.addScatterPlotSeriesPower(comboFeatureDomainTrack.getSelectedItem().toString(),
						comboFeatureRangeTrack.getSelectedItem().toString(), valuesDomainTrack, valuesRangeTrack,
						markerRangeTrack, markerDomainTrack, dataTrack, classColorTrack);
			if (comboRegressionTrack.getSelectedIndex() == 3)
				sp2Track.addScatterPlotSeriesLogarithmic(comboFeatureDomainTrack.getSelectedItem().toString(),
						comboFeatureRangeTrack.getSelectedItem().toString(), valuesDomainTrack, valuesRangeTrack,
						markerRangeTrack, markerDomainTrack, dataTrack, classColorTrack);
			if (comboRegressionTrack.getSelectedIndex() == 4)
				sp2Track.addScatterPlotSeriesExponential(comboFeatureDomainTrack.getSelectedItem().toString(),
						comboFeatureRangeTrack.getSelectedItem().toString(), valuesDomainTrack, valuesRangeTrack,
						markerRangeTrack, markerDomainTrack, dataTrack, classColorTrack);
		}
	}

	public void update1() {
		setNextButtonEnabled(true);
		setFinishButtonEnabled(true);
		setBackButtonEnabled(true);

	}

	public void next() {
		setNextButtonEnabled(false);
		// switchPanel(DynamicJWizard.PANEL_LAST);
	}

	public void back() {
		switchPanel(DynamicJWizard.PANEL_CHOOSER);

	}

	public void finish() {
		switchPanel(DynamicJWizard.DISPOSE_ON_CLOSE);
	}
}