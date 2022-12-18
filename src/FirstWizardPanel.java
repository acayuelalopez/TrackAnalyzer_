import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;

import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.io.FileInfo;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.process.ColorProcessor;
import jwizardcomponent.JWizardComponents;

public class FirstWizardPanel extends LabelWizardPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JTable tableImages, tableSpot;
	static DefaultTableModel modelImages, modelSpot;
	static ImagePlus imps[], impsPZ[];
	ImageIcon[] icons;
	Thread mainProcess;
	ImagePlus impAnal;
	static String command = "", spotEnable = "";
	List<Spot> removedSpots, spots;
	JSpinner filterMin, filterMax;
	ChartPanel histogram;
	HistogramFilterVersion hs2 = new HistogramFilterVersion();
	IntervalMarker intervalMarker;
	JCheckBox checkRPicker;
	static JList<String> classList, featureList;
	static DefaultListModel<String> modelListClass, modelListFeature;
	static JComboBox<String> comboFilters;
	static JLabel labelReset;
	List<Integer> indexesToReset, spotID, spotIDTI, spotIDTO, indexesTI, indexesTO;
	static JScrollPane jScrollPaneImages, jScrollPaneSpot;
	static Icon iconSpotCell, refreshCell;
	static Object[] columnNamesSpot;
	Thread refreshThread, csvThread, pngThread, paintThread, tInsideThread, tOutsideThread, enableThread, disableThread,
			slMinThread, filterMinThread, slMaxThread, filterMaxThread, filtersThread, pickerThread, classThread,
			remClassThread, addThread, remThread;

	public FirstWizardPanel(JWizardComponents wizardComponents) {
		super(wizardComponents, "");

		// setPanelTitle("Set a directory for export/import images and .xml file");
		File imageFolder = new File(TrackAnalyzer_.textImages.getText());
		File[] listOfFiles = imageFolder.listFiles();
		String[] imageTitles = new String[listOfFiles.length];
		File filesXML[] = new File[listOfFiles.length];

		for (int u = 0; u < filesXML.length; u++)
			filesXML[u] = new File(TrackAnalyzer_.textXml.getText());

		impsPZ = new ImagePlus[imageTitles.length];
		imps = new ImagePlus[imageTitles.length];
		icons = new ImageIcon[imps.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())
				imageTitles[i] = listOfFiles[i].getName();
			imps[i] = IJ.openImage(TrackAnalyzer_.textImages.getText() + "/" + imageTitles[i]);
			impsPZ[i] = extractTFrame(imps[i], 1);
			// impsPZ[i] = ZProjector.run(imps[i], "sum", 1, 1);
			icons[i] = new ImageIcon(getScaledImage(impsPZ[i].getImage(), 90, 95));

		}
		tableImages = new JTable();

		tableSpot = new JTable();
		modelImages = new DefaultTableModel();
		modelSpot = new DefaultTableModel();
		Object[] columnNames = new Object[] { "Movie", "Title", "Extension" };
		columnNamesSpot = new Object[] { "ID", "TRACK_ID", "QUALITY", "POSITION_X", "POSITION_Y", "POSITION_Z",
				"POSITION_T", "FRAME", "RADIUS", "VISIBILITY", "MEAN_INTENSITY", "MEDIAN_INTENSITY", "MIN_INTENSITY",
				"MAX_INTENSITY", "TOTAL_INTENSITY", "STANDARD_DEVIATION", "CONTRAST", "SNR", "ESTIMATED_DIAMETER",
				"MORPHOLOGY", "ELLIPSOIDFIT_SEMIAXISLENGTH_C", "ELLIPSOIDFIT_SEMIAXISLENGTH_B",
				"ELLIPSOIDFIT_SEMIAXISLENGTH_A", "ELLIPSOIDFIT_AXISPHI_C", "ELLIPSOIDFIT_AXISPHI_B",
				"ELLIPSOIDFIT_AXISPHI_A", "ELLIPSOIDFIT_AXISTHETA_C", "ELLIPSOIDFIT_AXISTHETA_B",
				"ELLIPSOIDFIT_AXISTHETA_A", "MANUAL_COLOR" };
		Object[][] data = new Object[imps.length][columnNames.length];
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				data[i][j] = "";

		modelSpot = new DefaultTableModel();
		modelImages = new DefaultTableModel(data, columnNames) {

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

		};
		tableImages.setModel(modelImages);
		tableSpot.setModel(modelSpot);
		tableSpot.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tableImages.getColumnModel().getColumn(0).setPreferredWidth(90);
		tableImages.getColumnModel().getColumn(1).setPreferredWidth(460);
		tableImages.getColumnModel().getColumn(2).setPreferredWidth(80);
		jScrollPaneImages = new JScrollPane(tableImages);
		jScrollPaneSpot = new JScrollPane(tableSpot);
		jScrollPaneImages.setPreferredSize(new Dimension(590, 240));
		jScrollPaneSpot.setPreferredSize(new Dimension(590, 240));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		jScrollPaneImages.setBorder(BorderFactory.createTitledBorder(""));
		jScrollPaneSpot.setBorder(BorderFactory.createTitledBorder(""));
		JTabbedPane tabbedPaneSpot = new JTabbedPane(JTabbedPane.TOP);
		ImageIcon iconSpot = createImageIcon("images/spot.png");
		iconSpotCell = new ImageIcon(iconSpot.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		JButton pngButton = new JButton();
		ImageIcon iconPng = createImageIcon("images/save.png");
		Icon pngCell = new ImageIcon(iconPng.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		pngButton.setIcon(pngCell);
		pngButton.setToolTipText("Click to capture spots overlay.");
		JPanel panelPng = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPng.add(pngButton);
		JButton csvButton = new JButton();
		ImageIcon iconCsv = createImageIcon("images/csv.png");
		Icon csvCell = new ImageIcon(iconCsv.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		csvButton.setIcon(csvCell);
		csvButton.setToolTipText("Click to export your spots table selection.");
		JPanel panelCsv = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCsv.add(csvButton);
		JPanel panelPngCsv = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPngCsv.add(panelPng);
		panelPngCsv.add(panelCsv);
		tabbedPaneSpot.addTab("SPOTS ", iconSpotCell, mainPanel, "Display Spot Analysis");
		tabbedPaneSpot.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JButton refreshButton = new JButton();
		ImageIcon iconRefresh = createImageIcon("images/refresh.png");
		refreshCell = new ImageIcon(iconRefresh.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		refreshButton.setIcon(refreshCell);
		refreshButton.setToolTipText("Click this button to get spot analysis");
		JToggleButton paintButton = new JToggleButton();
		ImageIcon iconPaint = createImageIcon("images/paint.png");
		Icon paintCell = new ImageIcon(iconPaint.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		paintButton.setIcon(paintCell);
		paintButton.setToolTipText("Click this button to display labeled-spots");
		JToggleButton tInsideButton = new JToggleButton();
		ImageIcon iconTI = createImageIcon("images/tinside.png");
		Icon TICell = new ImageIcon(iconTI.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		tInsideButton.setIcon(TICell);
		tInsideButton.setToolTipText("Click this button to toggle inside spots.");
		JToggleButton tOutsideButton = new JToggleButton();
		ImageIcon iconTO = createImageIcon("images/toutside.png");
		Icon TOCell = new ImageIcon(iconTO.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		tOutsideButton.setIcon(TOCell);
		tOutsideButton.setToolTipText("Click this button to toggle outside spots.");
		JButton enableButton = new JButton();
		ImageIcon iconEnable = createImageIcon("images/enable.png");
		Icon enableCell = new ImageIcon(iconEnable.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		enableButton.setIcon(enableCell);
		enableButton.setToolTipText("Click this button to enable your selection");
		JButton disableButton = new JButton();
		ImageIcon iconDisable = createImageIcon("images/disable.png");
		Icon disableCell = new ImageIcon(iconDisable.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		disableButton.setIcon(disableCell);
		disableButton.setToolTipText("Click this button to disable your selection");
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
		Dimension dime = separator1.getPreferredSize();
		dime.height = refreshButton.getPreferredSize().height;
		separator1.setPreferredSize(dime);
		separator2.setPreferredSize(dime);
		checkRPicker = new JCheckBox(" Spot Picker");
		JLabel filterLabel = new JLabel("  ➠ Spot Analysis : ");
		filterLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
		filterLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.add(filterLabel);
		filterPanel.add(checkRPicker);
		filterPanel.add(Box.createHorizontalStrut(20));
		JPanel filterMain = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterMain.add(filterPanel);
		buttonPanel.add(refreshButton);
		buttonPanel.add(paintButton);
		buttonPanel.add(separator1);
		buttonPanel.add(enableButton);
		buttonPanel.add(disableButton);
		buttonPanel.add(separator2);
		buttonPanel.add(tInsideButton);
		buttonPanel.add(tOutsideButton);
		filterMain.add(buttonPanel);
		mainPanel.add(jScrollPaneImages);
		mainPanel.add(Box.createVerticalStrut(5));
		mainPanel.add(filterMain);
		mainPanel.add(jScrollPaneSpot);
		JLabel settingsLabel = new JLabel("  ➠ Settings for Filters/Classes : ");
		settingsLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
		settingsLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		settingsPanel.add(settingsLabel);
		mainPanel.add(settingsPanel);
		JPanel filtersMin = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterMin = new JSpinner(new SpinnerNumberModel(30, 0, 5000, 1));
		filterMin.setPreferredSize(new Dimension(60, 20));
		JSlider sliderMin = new JSlider(0, 300, 50);
		sliderMin.setPreferredSize(new Dimension(150, 15));
		JLabel filterMinLabel = new JLabel("              Min :  ");
		filtersMin.add(filterMinLabel);
		filtersMin.add(sliderMin);
		filtersMin.add(Box.createHorizontalStrut(2));
		filtersMin.add(filterMin);
		JPanel filtersMax = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterMax = new JSpinner(new SpinnerNumberModel(200, 0, 5000, 1));
		filterMax.setPreferredSize(new Dimension(60, 20));
		JSlider sliderMax = new JSlider(0, 300, 150);
		sliderMax.setPreferredSize(new Dimension(150, 15));
		JLabel filterMaxLabel = new JLabel("              Max :  ");
		filtersMax.add(filterMaxLabel);
		filtersMax.add(sliderMax);
		filtersMax.add(Box.createHorizontalStrut(2));
		filtersMax.add(filterMax);
		JPanel boxPanel2 = new JPanel();
		boxPanel2.setLayout(new BoxLayout(boxPanel2, BoxLayout.Y_AXIS));
		IntervalMarker intervalMarker = new IntervalMarker(0, 0, new Color(229, 255, 204), new BasicStroke(),
				new Color(0, 102, 0), new BasicStroke(1.5f), 0.5f);
		histogram = hs2.createChartPanel("", new double[] { 0.0, 0.0, 0.0 }, 100, intervalMarker);

		JPanel chartPanel2 = new JPanel(new BorderLayout());
		chartPanel2.setPreferredSize(new Dimension(390, 180));
		chartPanel2.add(histogram);
		boxPanel2.add(chartPanel2);
		JPanel controlPanel2 = hs2.createControlPanel();
		boxPanel2.add(controlPanel2);
		JPanel filtersMain2 = new JPanel();
		filtersMain2.setLayout(new BoxLayout(filtersMain2, BoxLayout.Y_AXIS));
		filtersMain2.add(boxPanel2);
		filtersMain2.add(filtersMin);
		filtersMain2.add(filtersMax);
		JLabel featureSpot = new JLabel(" » Spot-Features :  ");
		featureSpot.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
		comboFilters = new JComboBox<String>();
		for (int i = 0; i < columnNamesSpot.length; i++)
			comboFilters.addItem((String) columnNamesSpot[i]);
		comboFilters.setPreferredSize(new Dimension(130, 25));
		comboFilters.setSelectedIndex(0);
		comboFilters.setOpaque(true);
		JPanel panelFilters = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JSeparator separator3 = new JSeparator(SwingConstants.VERTICAL);
		Dimension dime2 = separator3.getPreferredSize();
		dime2.height = filtersMain2.getPreferredSize().height;
		separator3.setPreferredSize(dime2);
		panelFilters.add(filtersMain2);
		panelFilters.add(separator3);
		modelListClass = new DefaultListModel<String>();
		classList = new JList<String>(modelListClass);
		modelListFeature = new DefaultListModel<String>();
		featureList = new JList<String>(modelListFeature);
		ColorEditorSpot colorEditor = new ColorEditorSpot(featureList);
		JScrollPane scrollListFilter = new JScrollPane(featureList);
		JScrollPane scrollListClass = new JScrollPane(classList);
		Dimension d = featureList.getPreferredSize();
		d.width = 150;
		d.height = 90;
		scrollListFilter.setPreferredSize(d);
		scrollListClass.setPreferredSize(d);
		JPanel filterPanelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel classPanelButtons = new JPanel();
		classPanelButtons.setLayout(new BoxLayout(classPanelButtons, BoxLayout.Y_AXIS));
		filterPanelButtons.add(scrollListFilter);
		JPanel fButtonsPanel = new JPanel();
		fButtonsPanel.setLayout(new BoxLayout(fButtonsPanel, BoxLayout.Y_AXIS));
		JButton addButton = new JButton();
		ImageIcon iconAdd = createImageIcon("images/add.png");
		Icon addCell = new ImageIcon(iconAdd.getImage().getScaledInstance(14, 16, Image.SCALE_SMOOTH));
		addButton.setIcon(addCell);
		addButton.setToolTipText("Click this button to add features");
		JButton remButton = new JButton();
		ImageIcon iconRem = createImageIcon("images/remove.png");
		Icon remCell = new ImageIcon(iconRem.getImage().getScaledInstance(14, 16, Image.SCALE_SMOOTH));
		remButton.setIcon(remCell);
		remButton.setToolTipText("Click this button to remove features");
		JButton classButton = new JButton();
		ImageIcon iconClass = createImageIcon("images/classes.png");
		Icon classCell = new ImageIcon(iconClass.getImage().getScaledInstance(14, 16, Image.SCALE_SMOOTH));
		classButton.setIcon(classCell);
		classButton.setToolTipText("Click this button to create a class.");
		JButton remClassButton = new JButton();
		remClassButton.setIcon(remCell);
		remClassButton.setToolTipText("Click this button to remove a class.");
		fButtonsPanel.add(addButton);
		fButtonsPanel.add(remButton);
		filterPanelButtons.add(fButtonsPanel);
		classPanelButtons.add(classButton);
		classPanelButtons.add(remClassButton);
		JPanel classPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		classPanel.add(scrollListClass);
		classPanel.add(classPanelButtons);
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		boxPanel.add(comboFilters);
		boxPanel.add(Box.createHorizontalStrut(5));
		boxPanel.add(filterPanelButtons);
		boxPanel.add(Box.createHorizontalStrut(5));
		boxPanel.add(classPanel);
		boxPanel.add(panelPngCsv);
		panelFilters.add(boxPanel);
		mainPanel.add(panelFilters);
		this.add(tabbedPaneSpot);
		createMovieTable();

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshThread = new Thread(new Runnable() {
					public void run() {
						spotEnable = "spotEnable";
						ProcessTrackMateXml.tracksVisible = false;
						ProcessTrackMateXml.spotsVisible = true;
						ProcessTrackMateXml ptx = new ProcessTrackMateXml();
						ptx.processTrackMateXml();
					}
				});
				refreshThread.start();
			}
		});
		csvButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				csvThread = new Thread(new Runnable() {
					public void run() {
						List<String> columnSpotHead = new ArrayList<String>();
						for (int j = 0; j < modelSpot.getColumnCount(); j++)
							columnSpotHead.add(modelSpot.getColumnName(j));

						ResultsTable rt = new ResultsTable(modelSpot.getRowCount());
						if (rt != null)
							rt.reset();

						for (int i = 0; i < modelSpot.getRowCount(); i++)
							for (int j = 0; j < modelSpot.getColumnCount(); j++)
								if (modelSpot.getValueAt(i, modelSpot.getColumnCount() - 1) == Boolean.TRUE) {

									if (columnSpotHead.get(j).equals(columnSpotHead.get(0)) == Boolean.TRUE) {

										rt.setValue(columnSpotHead.get(j), i,
												((JLabel) modelSpot.getValueAt(i, j)).getText());
									} else {

										rt.setValue(columnSpotHead.get(j), i, modelSpot.getValueAt(i, j).toString());
									}
								}
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
				csvThread.start();
			}
		});
		pngButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pngThread = new Thread(new Runnable() {
					public void run() {
						if (IJ.getImage() == null)
							IJ.error("You must have an image window active.");
						if (IJ.getImage() != null) {
							JFrame pngFrame = new JFrame();
							JFileChooser fileChooser = new JFileChooser();
							fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							fileChooser.setDialogTitle("Specify a directory to save");
							int userSelection = fileChooser.showSaveDialog(pngFrame);

							if (userSelection == JFileChooser.APPROVE_OPTION) {
								File fileToSave = fileChooser.getSelectedFile();
								// System.out.println("Save as file: " + fileToSave.getAbsolutePath());
								int firstFrame = 0, lastFrame = 0;
								if (ProcessTrackMateXml.displayer.getImp().getNFrames() > 1) {
									firstFrame = Math.max(1, Math.min(IJ.getImage().getNFrames(), 1));
									lastFrame = Math.min(IJ.getImage().getNFrames(),
											Math.max(IJ.getImage().getNFrames(), 1));
								}
								if (ProcessTrackMateXml.displayer.getImp().getNSlices() > 1) {
									firstFrame = Math.max(1, Math.min(IJ.getImage().getNSlices(), 1));
									lastFrame = Math.min(IJ.getImage().getNSlices(),
											Math.max(IJ.getImage().getNSlices(), 1));
								}

								Rectangle bounds = ProcessTrackMateXml.displayer.getImp().getCanvas().getBounds();
								int width = bounds.width;
								int height = bounds.height;
								int nCaptures = lastFrame - firstFrame + 1;
								ImageStack stack = new ImageStack(width, height);
								int channel = ProcessTrackMateXml.displayer.getImp().getChannel();
								int slice = ProcessTrackMateXml.displayer.getImp().getSlice();
								ProcessTrackMateXml.displayer.getImp().getCanvas().hideZoomIndicator(true);
								for (int frame = firstFrame; frame <= lastFrame; frame++) {
									// taskOutput.append(String.valueOf((frame - firstFrame) / nCaptures) + "\n");
									ProcessTrackMateXml.displayer.getImp().setPositionWithoutUpdate(channel, slice,
											frame);
									BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
									ProcessTrackMateXml.displayer.getImp().getCanvas().paint(bi.getGraphics());
									ColorProcessor cp = new ColorProcessor(bi);
									int index = ProcessTrackMateXml.displayer.getImp().getStackIndex(channel, slice,
											frame);
									stack.addSlice(
											ProcessTrackMateXml.displayer.getImp().getImageStack().getSliceLabel(index),
											cp);
								}
								ProcessTrackMateXml.displayer.getImp().getCanvas().hideZoomIndicator(false);
								ImagePlus capture = new ImagePlus("TrackMate capture of "
										+ ProcessTrackMateXml.displayer.getImp().getShortTitle(), stack);
								transferCalibration(ProcessTrackMateXml.displayer.getImp(), capture);
								IJ.saveAs(capture, "Tiff", fileToSave.getAbsolutePath() + File.separator
										+ "Capture Overlay for " + IJ.getImage().getShortTitle());
							}
						}
					}
				});
				pngThread.start();
			}
		});

		paintButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				paintThread = new Thread(new Runnable() {
					public void run() {
						if (ev.getStateChange() == ItemEvent.SELECTED) {
							paintAndDisableAction();
						} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
							resetAndEnableAction();
						}
					}
				});
				paintThread.start();
			}
		});
		tInsideButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				tInsideThread = new Thread(new Runnable() {
					public void run() {
						if (ev.getStateChange() == ItemEvent.SELECTED) {
							toggleInsideAction();
						} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
							resetToggleInsideAction();
						}
					}
				});
				tInsideThread.start();
			}
		});
		tOutsideButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				tOutsideThread = new Thread(new Runnable() {
					public void run() {
						if (ev.getStateChange() == ItemEvent.SELECTED) {
							toggleOutsideAction();
						} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
							resetToggleOutsideAction();
						}
					}
				});
				tOutsideThread.start();
			}
		});
		enableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableThread = new Thread(new Runnable() {
					public void run() {
						enableSpots();
					}
				});
				enableThread.start();
			}
		});
		disableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disableThread = new Thread(new Runnable() {
					public void run() {
						disableSpots();
					}
				});
				disableThread.start();

			}
		});
		sliderMin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				slMinThread = new Thread(new Runnable() {
					public void run() {

						filterMin.setValue(sliderMin.getValue());
						intervalMarker.setStartValue(sliderMin.getValue());
					}
				});
				slMinThread.start();
			}
		});

		filterMin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterMinThread = new Thread(new Runnable() {
					public void run() {
						sliderMin.setValue((int) filterMin.getValue());
						intervalMarker.setStartValue((int) filterMin.getValue());
					}
				});
				filterMinThread.start();
			}
		});

		sliderMax.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				slMaxThread = new Thread(new Runnable() {
					public void run() {
						filterMax.setValue(sliderMax.getValue());
						intervalMarker.setEndValue(sliderMax.getValue());
					}
				});
				slMaxThread.start();
			}
		});

		filterMax.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterMaxThread = new Thread(new Runnable() {
					public void run() {
						sliderMax.setValue((int) filterMax.getValue());
						intervalMarker.setEndValue((int) filterMax.getValue());
					}
				});
				filterMaxThread.start();
			}
		});
		comboFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtersThread = new Thread(new Runnable() {
					public void run() {
						String selectedName = (String) comboFilters.getSelectedItem();
						int selectedIndex = (int) comboFilters.getSelectedIndex();
						double values[] = null;
						double max;

						values = new double[tableSpot.getRowCount()];
						for (int r = 0; r < tableSpot.getRowCount(); r++)
							for (int c = 0; c < tableSpot.getColumnCount(); c++)
								values[r] = Double.parseDouble((String) tableSpot.getValueAt(r, selectedIndex + 2));
						int i;
						max = values[0];
						for (i = 1; i < values.length; i++)
							if (values[i] > max)
								max = values[i];

						sliderMin.setMinimum(0);
						sliderMin.setMaximum((int) max);
						sliderMax.setMinimum(0);
						sliderMax.setMaximum((int) max);

						hs2.addHistogramSeries(selectedName, values, (int) max, intervalMarker);
					}
				});
				filtersThread.start();
			}

		});
		checkRPicker.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				pickerThread = new Thread(new Runnable() {
					public void run() {
						if (e.getStateChange() == ItemEvent.SELECTED)
							command = "enable";
						if (e.getStateChange() == ItemEvent.DESELECTED) {
							command = null;
							ProcessTrackMateXml.selectionModel.clearSpotSelection();
							ProcessTrackMateXml.selectionModel.clearSelection();
							return;
						}
					}
				});
				pickerThread.start();

			}
		});
		classButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classThread = new Thread(new Runnable() {
					public void run() {
						ColorEditorSpot.myFrame.setVisible(true);
						colorEditor.setClassAction();
					}
				});
				classThread.start();

			}
		});
		remClassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remClassThread = new Thread(new Runnable() {
					public void run() {
						String classSelectedValue = classList.getSelectedValue();
						int[] classSelectedIndex = classList.getSelectedIndices();
						for (int i = 0; i < modelSpot.getRowCount(); i++)
							if (((JLabel) modelSpot.getValueAt(i, tableSpot.convertColumnIndexToModel(1))).getText()
									.equals(classSelectedValue) == true) {
								modelSpot.setValueAt(labelReset, i, tableSpot.convertColumnIndexToModel(1));
							}
						for (int i = 0; i < classSelectedIndex.length; i++)
							modelListClass.removeElementAt(classSelectedIndex[i]);
					}
				});
				remClassThread.start();
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addThread = new Thread(new Runnable() {
					public void run() {

						List<String> listFilters = new ArrayList<String>();

						if (featureList.getModel().getSize() < 1)
							modelListFeature.addElement((String) comboFilters.getSelectedItem() + ":  ["
									+ filterMin.getValue() + "," + filterMax.getValue() + "]");

						if (featureList.getModel().getSize() >= 1) {
							for (int i = 0; i < featureList.getModel().getSize(); i++)
								listFilters.add(String.valueOf(featureList.getModel().getElementAt(i).substring(0,
										featureList.getModel().getElementAt(i).lastIndexOf(":"))));

							if (listFilters.contains(comboFilters.getSelectedItem().toString()) == false)
								modelListFeature.addElement((String) comboFilters.getSelectedItem() + ":  ["
										+ filterMin.getValue() + "," + filterMax.getValue() + "]");

							if (listFilters.contains(comboFilters.getSelectedItem().toString()) == true)
								return;

						}
					}
				});
				addThread.start();

			}
		});

		remButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remThread = new Thread(new Runnable() {
					public void run() {

						try {
							int[] indexes = featureList.getSelectedIndices();
							for (int i = 0; i < indexes.length; i++)
								modelListFeature.remove(indexes[i]);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

					}
				});
				remThread.start();
			}
		});

	}

	public void toggleOutsideAction() {
		Roi mainRoi = null;
		if (IJ.getImage().getRoi().getType() == Roi.RECTANGLE)
			mainRoi = IJ.getImage().getRoi();
		indexesTO = new ArrayList<Integer>();
		for (int i = 0; i < modelSpot.getRowCount(); i++) {
			if (mainRoi
					.contains(
							(int) IJ.getImage().getCalibration()
									.getRawX(
											Double.parseDouble(
													modelSpot.getValueAt(i, tableSpot.convertColumnIndexToModel(5))
															.toString())),
							(int) IJ.getImage().getCalibration().getRawY(
									Double.parseDouble(modelSpot.getValueAt(i, tableSpot.convertColumnIndexToModel(6))
											.toString()))) == Boolean.FALSE) {
				indexesTO.add(i);
				modelSpot.setValueAt(false, i, tableSpot.convertColumnIndexToModel(0));
				final int spotID = Integer.parseInt((String) tableSpot.getValueAt(i, 2));
				Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);

				if (null != spot) {
					spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ZERO);
					ProcessTrackMateXml.model.endUpdate();
					ProcessTrackMateXml.displayer.refresh();

				}
			}
		}
	}

	public void resetToggleOutsideAction() {

		for (int row = 0; row < modelSpot.getRowCount(); row++) {
			modelSpot.setValueAt(true, tableSpot.convertRowIndexToModel(row), tableSpot.convertColumnIndexToModel(0));
			final int spotID = Integer.parseInt((String) tableSpot.getValueAt(row, 2));
			Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);
			if (null != spot) {
				spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ONE);
				ProcessTrackMateXml.model.endUpdate();
				ProcessTrackMateXml.displayer.refresh();
			}

		}

	}

	public void toggleInsideAction() {

		Roi mainRoi = null;
		if (IJ.getImage().getRoi().getType() == Roi.RECTANGLE)
			mainRoi = IJ.getImage().getRoi();
		indexesTI = new ArrayList<Integer>();
		for (int i = 0; i < modelSpot.getRowCount(); i++) {
			if (mainRoi
					.contains(
							(int) IJ.getImage().getCalibration()
									.getRawX(
											Double.parseDouble(
													modelSpot.getValueAt(i, tableSpot.convertColumnIndexToModel(5))
															.toString())),
							(int) IJ.getImage().getCalibration().getRawY(
									Double.parseDouble(modelSpot.getValueAt(i, tableSpot.convertColumnIndexToModel(6))
											.toString()))) == Boolean.TRUE) {
				indexesTI.add(i);
				modelSpot.setValueAt(false, i, tableSpot.convertColumnIndexToModel(0));
				final int spotID = Integer.parseInt((String) tableSpot.getValueAt(i, 2));
				Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);

				if (null != spot) {
					spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ZERO);
					ProcessTrackMateXml.model.endUpdate();
					ProcessTrackMateXml.displayer.refresh();

				}
			}
		}

	}

	public void resetToggleInsideAction() {

		for (int row = 0; row < modelSpot.getRowCount(); row++) {
			modelSpot.setValueAt(true, tableSpot.convertRowIndexToModel(row), tableSpot.convertColumnIndexToModel(0));
			final int spotID = Integer.parseInt((String) tableSpot.getValueAt(row, 2));
			Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);
			if (null != spot) {
				spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ONE);
				ProcessTrackMateXml.model.endUpdate();
				ProcessTrackMateXml.displayer.refresh();

			}

		}
	}

	public void paintAndDisableAction() {
		indexesToReset = new ArrayList<Integer>();
		spotID = new ArrayList<Integer>();
		spots = new ArrayList<Spot>();
		for (int i = 0; i < modelSpot.getRowCount(); i++)
			if (((JLabel) modelSpot.getValueAt(i, tableSpot.convertColumnIndexToModel(1))).getBackground()
					.equals(new Color(214, 217, 223)) == Boolean.TRUE) {
				indexesToReset.add(i);
				modelSpot.setValueAt(false, i, tableSpot.convertColumnIndexToModel(0));
				spotID.add(Integer.parseInt((String) tableSpot.getValueAt(i, 2)));
			}
		for (int row = 0; row < indexesToReset.size(); row++) {
			final int spotID = Integer.parseInt((String) tableSpot.getValueAt(indexesToReset.get(row), 2));
			Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);
			if (null != spot) {
				spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ZERO);
				ProcessTrackMateXml.model.endUpdate();
				ProcessTrackMateXml.displayer.refresh();

			}

		}
	}

	public void resetAndEnableAction() {
		for (int i = 0; i < indexesToReset.size(); i++)
			modelSpot.setValueAt(true, tableSpot.convertRowIndexToModel(indexesToReset.get(i)),
					tableSpot.convertColumnIndexToModel(0));
		for (int row = 0; row < indexesToReset.size(); row++) {
			final int spotID = Integer.parseInt((String) tableSpot.getValueAt(indexesToReset.get(row), 2));
			Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);
			if (null != spot) {
				spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ONE);
				ProcessTrackMateXml.model.endUpdate();
				ProcessTrackMateXml.displayer.refresh();

			}

		}
	}

	public void enableSpots() {

		ListSelectionModel lsm = tableSpot.getSelectionModel();
		int[] selectedIndices = tableSpot.getSelectedRows();
		for (int i = 0; i < selectedIndices.length; i++)
			tableSpot.setValueAt(true, selectedIndices[i], 0);
		final int selStart = lsm.getMinSelectionIndex();
		final int selEnd = lsm.getMaxSelectionIndex();
		if (selStart < 0 || selEnd < 0)
			return;

		final int minLine = Math.min(selStart, selEnd);
		final int maxLine = Math.max(selStart, selEnd);
		for (int row = minLine; row <= maxLine; row++) {
			final int spotIDEnable = Integer.parseInt((String) tableSpot.getValueAt(row, 2));
			Spot spotEnable = ProcessTrackMateXml.model.getSpots().search(spotIDEnable);
			if (spotEnable != null) {
				spotEnable.putFeature(SpotCollection.VISIBILITY, SpotCollection.ONE);
				ProcessTrackMateXml.model.endUpdate();
				ProcessTrackMateXml.displayer.refresh();
			}

		}
	}

	public void disableSpots() {

		ListSelectionModel lsm = tableSpot.getSelectionModel();
		final int selStart = lsm.getMinSelectionIndex();
		final int selEnd = lsm.getMaxSelectionIndex();
		if (selStart < 0 || selEnd < 0)
			return;

		final int minLine = Math.min(selStart, selEnd);
		final int maxLine = Math.max(selStart, selEnd);
		for (int row = minLine; row <= maxLine; row++) {
			final int spotID = Integer.parseInt((String) tableSpot.getValueAt(row, 2));
			Spot spot = ProcessTrackMateXml.model.getSpots().search(spotID);
			if (null != spot) {
				spot.putFeature(SpotCollection.VISIBILITY, SpotCollection.ZERO);
				ProcessTrackMateXml.model.endUpdate();
				ProcessTrackMateXml.displayer.refresh();

			}

		}
		int[] selectedIndices = tableSpot.getSelectedRows();
		for (int i = 0; i < selectedIndices.length; i++)
			tableSpot.setValueAt(false, selectedIndices[i], 0);
	}

	public static void createSpotTable() {
		// SLTResultsTableVersion sltRT = new
		// SLTResultsTableVersion(ProcessTrackMateXml.selectionModel);
		// ResultsTable spotTable = sltRT.executeSpot(trackmate)

		modelSpot = new DefaultTableModel(ProcessTrackMateXml.dataSpot, ProcessTrackMateXml.columnHeadersSpot) {

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

		};
		modelSpot.addColumn("Enable");
		tableSpot.setModel(modelSpot);
		tableSpot.moveColumn(tableSpot.getColumnCount() - 1, 0);
		tableSpot.setSelectionBackground(new Color(229, 255, 204));
		tableSpot.setSelectionForeground(new Color(0, 102, 0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableSpot.setDefaultRenderer(String.class, centerRenderer);
		tableSpot.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSpot.setRowHeight(45);
		tableSpot.setAutoCreateRowSorter(true);
		tableSpot.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		for (int u = 0; u < tableSpot.getColumnCount(); u++)
			tableSpot.getColumnModel().getColumn(u).setPreferredWidth(90);
		for (int u = 16; u < tableSpot.getColumnCount(); u++)
			tableSpot.getColumnModel().getColumn(u).setPreferredWidth(150);

		for (int i = 0; i < tableSpot.getRowCount(); i++)
			tableSpot.setValueAt(true, i, 0);
		tableSpot.getColumnModel().getColumn(1).setCellRenderer(new Renderer());
		labelReset = new JLabel();
		labelReset.setText("");
		labelReset.setOpaque(true);
		labelReset.setBackground(new Color(214, 217, 223));
		for (int i = 0; i < modelSpot.getRowCount(); i++)
			modelSpot.setValueAt(labelReset, i, tableSpot.convertColumnIndexToModel(1));

	}

	public void createMovieTable() {

		tableImages.setSelectionBackground(new Color(229, 255, 204));
		tableImages.setSelectionForeground(new Color(0, 102, 0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableImages.setDefaultRenderer(String.class, centerRenderer);
		tableImages.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableImages.setRowHeight(95);
		tableImages.setAutoCreateRowSorter(true);
		tableImages.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());

		for (int i = 0; i < modelImages.getRowCount(); i++) {
			modelImages.setValueAt(icons[i], i, tableImages.convertColumnIndexToModel(0));
			modelImages.setValueAt(imps[i].getShortTitle(), i, tableImages.convertColumnIndexToModel(1));
			modelImages.setValueAt(imps[i].getTitle().substring(imps[i].getTitle().lastIndexOf(".")), i,
					tableImages.convertColumnIndexToModel(2));
		}

	}

	public static Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FirstWizardPanel.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public ImagePlus extractTFrame(ImagePlus imp, int frame) {
		int width = imp.getWidth();
		int height = imp.getHeight();
		int channels = imp.getNChannels();
		int zslices = imp.getNSlices();
		FileInfo fileInfo = imp.getOriginalFileInfo();
		ImageStack stack2 = new ImageStack(width, height);
		ImagePlus imp2 = new ImagePlus();
		imp2.setTitle("T" + frame + "-" + imp.getTitle());

		for (int z = 1; z <= zslices; z++)
			for (int c = 1; c <= channels; c++) {
				int sliceSix = imp.getStackIndex(c, z, frame);
				stack2.addSlice("", imp.getStack().getProcessor(sliceSix));
			}
		imp2.setStack(stack2);
		imp2.setDimensions(channels, zslices, 1);
		if (channels * zslices > 1)
			imp2.setOpenAsHyperStack(true);
		imp2.setFileInfo(fileInfo);
		return imp2;
	}

	private static final void transferCalibration(final ImagePlus from, final ImagePlus to) {
		final Calibration fc = from.getCalibration();
		final Calibration tc = to.getCalibration();

		tc.setUnit(fc.getUnit());
		tc.setTimeUnit(fc.getTimeUnit());
		tc.frameInterval = fc.frameInterval;

		final double mag = from.getCanvas().getMagnification();
		tc.pixelWidth = fc.pixelWidth / mag;
		tc.pixelHeight = fc.pixelHeight / mag;
		tc.pixelDepth = fc.pixelDepth;
	}

}