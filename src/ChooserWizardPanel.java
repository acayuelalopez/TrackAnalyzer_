import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.features.track.TrackIndexAnalyzer;
import fiji.plugin.trackmate.visualization.PerTrackFeatureColorGenerator;
import fiji.plugin.trackmate.visualization.SpotColorGenerator;
import fiji.plugin.trackmate.visualization.TrackMateModelView;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.process.ColorProcessor;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;
import jwizardcomponent.example.DynamicJWizard;

public class ChooserWizardPanel extends JWizardPanel {
	JCheckBox checkRPicker;
	static JTable tableTrack, tableImages;
	static DefaultTableModel modelTrack;
	JScrollPane jScrollPaneTrack, jScrollPaneImages;
	JSpinner filterMin, filterMax;
	HistogramFilterVersion hs2 = new HistogramFilterVersion();
	ChartPanel histogram;
	JComboBox<String> comboFilters;
	static DefaultListModel<String> modelListClass, modelListFeature;
	static JList<String> classList, featureList;
	static JLabel labelReset;
	static String trackEnable = "", command;
	List<Integer> indexesToReset, indexesToReset1, tracksID, tracksID1, indexesTI;
	static Icon iconTrackCell;
	static Object[] columnNamesTrack;
	Thread refreshThread, csvThread, pngThread, paintThread, tInsideThread, tOutsideThread, enableThread, disableThread,
			slMinThread, filterMinThread, slMaxThread, filterMaxThread, filtersThread, pickerThread, classThread,
			remClassThread, addThread, remThread;

	public ChooserWizardPanel(JWizardComponents wizardComponents) {
		super(wizardComponents, "");

		tableTrack = new JTable();
		tableImages = new JTable();
		tableImages.setModel(FirstWizardPanel.modelImages);
		tableImages.getColumnModel().getColumn(0).setPreferredWidth(90);
		tableImages.getColumnModel().getColumn(1).setPreferredWidth(460);
		tableImages.getColumnModel().getColumn(2).setPreferredWidth(80);
		modelTrack = new DefaultTableModel();
		columnNamesTrack = new Object[] { "Label", "TRACK_ID", "TRACK_INDEX", "NUMBER_SPOTS", "NUMBER_GAPS",
				"NUMBER_SPLITS", "NUMBER_MERGES", "NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION", "TRACK_START",
				"TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION", "TRACK_Y_LOCATION", "TRACK_Z_LOCATION",
				"TRACK_MEAN_SPEED", "TRACK_MAX_SPEED", "TRACK_MIN_SPEED", "TRACK_MEDIAN_SPEED", "TRACK_STD_SPEED",
				"TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED", "MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO",
				"MEAN_STRAIGHT_LINE_SPEED", "LINEARITY_OF_FORWARD_PROGRESSION", "MEAN_DIRECTIONAL_CHANGE_RATE",
				"TOTAL_ABSOLUTE_ANGLE_XY", "TOTAL_ABSOLUTE_ANGLE_YZ", "TOTAL_ABSOLUTE_ANGLE_ZX" };

		tableTrack.setModel(modelTrack);
		tableTrack.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jScrollPaneTrack = new JScrollPane(tableTrack);
		jScrollPaneTrack.setPreferredSize(new Dimension(590, 240));
		jScrollPaneTrack.setBorder(BorderFactory.createTitledBorder(""));
		jScrollPaneImages = new JScrollPane(tableImages);
		jScrollPaneImages.setPreferredSize(new Dimension(590, 240));
		jScrollPaneImages.setBorder(BorderFactory.createTitledBorder(""));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JTabbedPane tabbedPaneTrack = new JTabbedPane(JTabbedPane.TOP);
		ImageIcon iconTrack = FirstWizardPanel.createImageIcon("images/track.jpg");
		iconTrackCell = new ImageIcon(iconTrack.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		JButton pngButton = new JButton();
		ImageIcon iconPng = FirstWizardPanel.createImageIcon("images/save.png");
		Icon pngCell = new ImageIcon(iconPng.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		pngButton.setIcon(pngCell);
		pngButton.setToolTipText("Click to capture spots overlay.");
		JPanel panelPng = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPng.add(pngButton);
		JButton csvButton = new JButton();
		ImageIcon iconCsv = FirstWizardPanel.createImageIcon("images/csv.png");
		Icon csvCell = new ImageIcon(iconCsv.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		csvButton.setIcon(csvCell);
		csvButton.setToolTipText("Click to export your spots table selection.");
		JPanel panelCsv = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCsv.add(csvButton);
		JPanel panelPngCsv = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelPngCsv.add(panelPng);
		panelPngCsv.add(panelCsv);
		tabbedPaneTrack.addTab("TRACKS ", iconTrackCell, mainPanel, "Display Track Analysis");
		tabbedPaneTrack.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JButton refreshButton = new JButton();
		ImageIcon iconRefresh = FirstWizardPanel.createImageIcon("images/refresh.png");
		Icon refreshCell = new ImageIcon(iconRefresh.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		refreshButton.setIcon(refreshCell);
		refreshButton.setToolTipText("Click this button to get Track analysis");
		JToggleButton paintButton = new JToggleButton();
		ImageIcon iconPaint = FirstWizardPanel.createImageIcon("images/paint.png");
		Icon paintCell = new ImageIcon(iconPaint.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		paintButton.setIcon(paintCell);
		paintButton.setToolTipText("Click this button to display labeled-Tracks");
		JToggleButton tInsideButton = new JToggleButton();
		ImageIcon iconTI = FirstWizardPanel.createImageIcon("images/tinside.png");
		Icon TICell = new ImageIcon(iconTI.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		tInsideButton.setIcon(TICell);
		tInsideButton.setToolTipText("Click this button to toggle inside Tracks.");
		JToggleButton tOutsideButton = new JToggleButton();
		ImageIcon iconTO = FirstWizardPanel.createImageIcon("images/toutside.png");
		Icon TOCell = new ImageIcon(iconTO.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		tOutsideButton.setIcon(TOCell);
		tOutsideButton.setToolTipText("Click this button to toggle outside Tracks.");
		JButton enableButton = new JButton();
		ImageIcon iconEnable = FirstWizardPanel.createImageIcon("images/enable.png");
		Icon enableCell = new ImageIcon(iconEnable.getImage().getScaledInstance(18, 20, Image.SCALE_SMOOTH));
		enableButton.setIcon(enableCell);
		enableButton.setToolTipText("Click this button to enable your selection");
		JButton disableButton = new JButton();
		ImageIcon iconDisable = FirstWizardPanel.createImageIcon("images/disable.png");
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
		checkRPicker = new JCheckBox(" Track Picker");
		JLabel filterLabel = new JLabel("  ➠ Track Analysis : ");
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
		mainPanel.add(jScrollPaneTrack);
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
		JLabel featureTrack = new JLabel(" » Track-Features :  ");
		featureTrack.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
		comboFilters = new JComboBox<String>();
		for (int i = 1; i < columnNamesTrack.length; i++)
			comboFilters.addItem((String) columnNamesTrack[i]);
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
		ColorEditorTrack colorEditor = new ColorEditorTrack(featureList);
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
		ImageIcon iconAdd = FirstWizardPanel.createImageIcon("images/add.png");
		Icon addCell = new ImageIcon(iconAdd.getImage().getScaledInstance(14, 16, Image.SCALE_SMOOTH));
		addButton.setIcon(addCell);
		addButton.setToolTipText("Click this button to add features");
		JButton remButton = new JButton();
		ImageIcon iconRem = FirstWizardPanel.createImageIcon("images/remove.png");
		Icon remCell = new ImageIcon(iconRem.getImage().getScaledInstance(14, 16, Image.SCALE_SMOOTH));
		remButton.setIcon(remCell);
		remButton.setToolTipText("Click this button to remove features");
		JButton classButton = new JButton();
		ImageIcon iconClass = FirstWizardPanel.createImageIcon("images/classes.png");
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
		this.add(tabbedPaneTrack);
		createMovieTable();

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
		csvButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				csvThread = new Thread(new Runnable() {
					public void run() {
						List<String> columnTrackHead = new ArrayList<String>();
						for (int j = 0; j < modelTrack.getColumnCount(); j++)
							columnTrackHead.add(modelTrack.getColumnName(j));

						ResultsTable rt = new ResultsTable(modelTrack.getRowCount());
						if (rt != null)
							rt.reset();

						for (int i = 0; i < modelTrack.getRowCount(); i++)
							for (int j = 0; j < modelTrack.getColumnCount(); j++)
								if (modelTrack.getValueAt(i, modelTrack.getColumnCount() - 1) == Boolean.TRUE) {

									if (columnTrackHead.get(j).equals(columnTrackHead.get(0)) == Boolean.TRUE) {

										rt.setValue(columnTrackHead.get(j), i,
												((JLabel) modelTrack.getValueAt(i, j)).getText());
									} else {

										rt.setValue(columnTrackHead.get(j), i, modelTrack.getValueAt(i, j).toString());
									}
								}
						rt.show("Resutls tracks");
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
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshThread = new Thread(new Runnable() {
					public void run() {

						trackEnable = "trackEnable";
						ProcessTrackMateXml.tracksVisible = true;
						ProcessTrackMateXml.spotsVisible = false;
						ProcessTrackMateXml ptx = new ProcessTrackMateXml();
						ptx.processTrackMateXml();
					}
				});
				refreshThread.start();

			}
		});
		enableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableThread = new Thread(new Runnable() {
					public void run() {
						enableTracks();
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
						disableTracks();
					}
				});
				disableThread.start();

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
		comboFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtersThread = new Thread(new Runnable() {
					public void run() {
						String selectedName = (String) comboFilters.getSelectedItem();
						int selectedIndex = (int) comboFilters.getSelectedIndex();
						double values[] = null;
						double max;

						values = new double[tableTrack.getRowCount()];
						for (int r = 0; r < tableTrack.getRowCount(); r++)
							for (int c = 0; c < tableTrack.getColumnCount(); c++)
								values[r] = Double.parseDouble((String) tableTrack.getValueAt(r, selectedIndex + 2));
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
		classButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classThread = new Thread(new Runnable() {
					public void run() {
						ColorEditorTrack.myFrame.setVisible(true);
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
						for (int i = 0; i < modelTrack.getRowCount(); i++)
							if (((JLabel) modelTrack.getValueAt(i, tableTrack.convertColumnIndexToModel(1))).getText()
									.equals(classSelectedValue) == true) {
								modelTrack.setValueAt(labelReset, i, tableTrack.convertColumnIndexToModel(1));
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
		tOutsideButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				tOutsideThread = new Thread(new Runnable() {
					public void run() {

						if (ev.getStateChange() == ItemEvent.SELECTED) {
							toggleOutsideAction();
						} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
							resetToggleInsideAction();
						}
					}

				});
				tOutsideThread.start();
			}
		});
	}

	public void toggleOutsideAction() {
		Roi mainRoi = null;
		if (IJ.getImage().getRoi().getType() == Roi.RECTANGLE)
			mainRoi = IJ.getImage().getRoi();
		indexesTI = new ArrayList<Integer>();
		// List<Integer> trackIDss = new ArrayList<Integer>();
		for (int i = 0; i < modelTrack.getRowCount(); i++) {

			if (mainRoi
					.contains(
							(int) IJ.getImage().getCalibration()
									.getRawX(Double.parseDouble(modelTrack
											.getValueAt(i, tableTrack.convertColumnIndexToModel(13)).toString())),
							(int) IJ.getImage().getCalibration()
									.getRawY(Double.parseDouble(
											modelTrack.getValueAt(i, tableTrack.convertColumnIndexToModel(14))
													.toString()))) == Boolean.FALSE) {
				indexesTI.add(i);
				modelTrack.setValueAt(false, i, tableTrack.convertColumnIndexToModel(0));
				// trackIDss.add(Integer.parseInt((String) tableTrack.getValueAt(i, 2)));
				final int trackID = Integer.parseInt((String) tableTrack.getValueAt(i, 2));
				ProcessTrackMateXml.model.beginUpdate();
				try {
					ProcessTrackMateXml.model.setTrackVisibility(trackID, false);
				} finally {
					ProcessTrackMateXml.model.endUpdate();
				}

				ProcessTrackMateXml.displayer.refresh();

			}
		}
	}

	public void toggleInsideAction() {

		Roi mainRoi = null;
		if (IJ.getImage().getRoi().getType() == Roi.RECTANGLE)
			mainRoi = IJ.getImage().getRoi();
		indexesTI = new ArrayList<Integer>();
		// List<Integer> trackIDss = new ArrayList<Integer>();
		for (int i = 0; i < modelTrack.getRowCount(); i++) {

			if (mainRoi
					.contains(
							(int) IJ.getImage().getCalibration()
									.getRawX(Double.parseDouble(modelTrack
											.getValueAt(i, tableTrack.convertColumnIndexToModel(13)).toString())),
							(int) IJ.getImage().getCalibration()
									.getRawY(Double.parseDouble(
											modelTrack.getValueAt(i, tableTrack.convertColumnIndexToModel(14))
													.toString()))) == Boolean.TRUE) {
				indexesTI.add(i);
				modelTrack.setValueAt(false, i, tableTrack.convertColumnIndexToModel(0));
				// trackIDss.add(Integer.parseInt((String) tableTrack.getValueAt(i, 2)));
				final int trackID = Integer.parseInt((String) tableTrack.getValueAt(i, 2));
				ProcessTrackMateXml.model.beginUpdate();
				try {
					ProcessTrackMateXml.model.setTrackVisibility(trackID, false);
				} finally {
					ProcessTrackMateXml.model.endUpdate();
				}

				ProcessTrackMateXml.displayer.refresh();

			}
		}

	}

	public void resetToggleInsideAction() {

		for (int row = 0; row < modelTrack.getRowCount(); row++) {
			modelTrack.setValueAt(true, tableTrack.convertRowIndexToModel(row),
					tableTrack.convertColumnIndexToModel(0));
			final int trackID = Integer.parseInt((String) tableTrack.getValueAt(row, 2));
			ProcessTrackMateXml.model.beginUpdate();
			try {
				ProcessTrackMateXml.model.setTrackVisibility(trackID, true);
			} finally {
				ProcessTrackMateXml.model.endUpdate();
			}

			ProcessTrackMateXml.displayer.refresh();
		}

	}

	public static void createTrackTable() {

		modelTrack = new DefaultTableModel(ProcessTrackMateXml.dataTrack, ProcessTrackMateXml.columnHeadersTrack) {

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
		modelTrack.addColumn("Enable");
		tableTrack.setModel(modelTrack);
		tableTrack.moveColumn(tableTrack.getColumnCount() - 1, 0);
		tableTrack.setSelectionBackground(new Color(229, 255, 204));
		tableTrack.setSelectionForeground(new Color(0, 102, 0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableTrack.setDefaultRenderer(String.class, centerRenderer);
		tableTrack.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableTrack.setRowHeight(45);
		tableTrack.setAutoCreateRowSorter(true);
		tableTrack.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		for (int u = 0; u < tableTrack.getColumnCount(); u++)
			tableTrack.getColumnModel().getColumn(u).setPreferredWidth(90);
		for (int u = 3; u < tableTrack.getColumnCount(); u++)
			tableTrack.getColumnModel().getColumn(u).setPreferredWidth(130);

		for (int i = 0; i < tableTrack.getRowCount(); i++)
			tableTrack.setValueAt(true, i, 0);
		tableTrack.getColumnModel().getColumn(1).setCellRenderer(new Renderer());
		labelReset = new JLabel();
		labelReset.setText("");
		labelReset.setOpaque(true);
		labelReset.setBackground(new Color(214, 217, 223));
		for (int i = 0; i < modelTrack.getRowCount(); i++)
			modelTrack.setValueAt(labelReset, i, tableTrack.convertColumnIndexToModel(1));

	}

	public void enableTracks() {
		indexesToReset1 = new ArrayList<Integer>();
		tracksID1 = new ArrayList<Integer>();
		int[] selectedRows = tableTrack.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			indexesToReset1.add(selectedRows[i]);
			modelTrack.setValueAt(true, selectedRows[i], tableTrack.convertColumnIndexToModel(0));
			tracksID1.add(Integer.parseInt((String) tableTrack.getValueAt(selectedRows[i], 2)));
		}

		for (int row = 0; row < tracksID1.size(); row++) {

			ProcessTrackMateXml.model.beginUpdate();
			try {
				ProcessTrackMateXml.model.setTrackVisibility(tracksID1.get(row), true);
			} finally {
				ProcessTrackMateXml.model.endUpdate();
			}

			// ProcessTrackMateXml.model.endUpdate();
			ProcessTrackMateXml.displayer.refresh();

		}

	}

	public void disableTracks() {
		indexesToReset1 = new ArrayList<Integer>();
		tracksID1 = new ArrayList<Integer>();
		int[] selectedRows = tableTrack.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			indexesToReset1.add(selectedRows[i]);
			modelTrack.setValueAt(false, selectedRows[i], tableTrack.convertColumnIndexToModel(0));
			tracksID1.add(Integer.parseInt((String) tableTrack.getValueAt(selectedRows[i], 2)));
		}

		for (int row = 0; row < tracksID1.size(); row++) {

			ProcessTrackMateXml.model.beginUpdate();
			try {
				ProcessTrackMateXml.model.setTrackVisibility(tracksID1.get(row), false);
			} finally {
				ProcessTrackMateXml.model.endUpdate();
			}

			// ProcessTrackMateXml.model.endUpdate();
			ProcessTrackMateXml.displayer.refresh();

		}

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
		tableImages.setModel(FirstWizardPanel.modelImages);

	}

	public void paintAndDisableAction() {
		indexesToReset = new ArrayList<Integer>();
		tracksID = new ArrayList<Integer>();

		for (int i = 0; i < modelTrack.getRowCount(); i++)
			if (((JLabel) modelTrack.getValueAt(i, tableTrack.convertColumnIndexToModel(1))).getBackground()
					.equals(new Color(214, 217, 223)) == Boolean.TRUE) {
				indexesToReset.add(i);
				modelTrack.setValueAt(false, i, tableTrack.convertColumnIndexToModel(0));
				tracksID.add(Integer.parseInt((String) tableTrack.getValueAt(i, 2)));
			}
		for (int row = 0; row < tracksID.size(); row++) {

			ProcessTrackMateXml.model.beginUpdate();
			try {
				ProcessTrackMateXml.model.setTrackVisibility(tracksID.get(row), false);
			} finally {
				ProcessTrackMateXml.model.endUpdate();
			}

			// ProcessTrackMateXml.model.endUpdate();
			ProcessTrackMateXml.displayer.refresh();

		}
	}

	public void resetAndEnableAction() {
		for (int i = 0; i < indexesToReset.size(); i++)
			modelTrack.setValueAt(true, tableTrack.convertRowIndexToModel(indexesToReset.get(i)),
					tableTrack.convertColumnIndexToModel(0));
		for (int row = 0; row < tracksID.size(); row++) {
			ProcessTrackMateXml.model.beginUpdate();
			try {
				ProcessTrackMateXml.model.setTrackVisibility(tracksID.get(row), true);
			} finally {
				ProcessTrackMateXml.model.endUpdate();
			}
		}
		ProcessTrackMateXml.displayer.refresh();
	}

	public void update() {
		setNextButtonEnabled(true);
		setFinishButtonEnabled(true);
		setBackButtonEnabled(true); // there is no way back
	}

	public void next() {
		switchPanel(DynamicJWizard.PANEL_OPTION_A);

	}

	public void back() {

		switchPanel(DynamicJWizard.PANEL_FIRST);

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