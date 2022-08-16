import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

import net.chicoronny.trackmate.action.ExportTracksToSQL;
import traJ.TrajectoryModified;

import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.accessibility.Accessible;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVWriter;

import de.biomedical_imaging.ij.trajectory_classifier.TraJClassifier_;
import fiji.plugin.trackmate.FeatureModel;
import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.SelectionModel;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.SpotRoi;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.TrackModel;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.ShapeRoi;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.ChannelSplitter;
import ij.plugin.FolderOpener;
import ij.plugin.PlugIn;
import ij.plugin.RGBStackMerge;
import ij.plugin.ZProjector;
import ij.plugin.frame.RoiManager;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.text.TextPanel;
import ij.text.TextWindow;
import inra.ijpb.morphology.Morphology;
import inra.ijpb.morphology.Strel;
import inra.ijpb.morphology.strel.SquareStrel;
//import ij3d.Image3DUniverse;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;
import jwizardcomponent.Utilities;
import jwizardcomponent.example.DynamicJWizard;
import jwizardcomponent.frame.JWizardFrame;
import jwizardcomponent.frame.SimpleJWizardFrame;
import loci.plugins.in.DisplayHandler;
import loci.plugins.in.ImportProcess;
import loci.plugins.in.ImporterOptions;
import fiji.plugin.trackmate.detection.DetectionUtils;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.LogDetectorFactory;
import fiji.plugin.trackmate.detection.ManualDetectorFactory;
import fiji.plugin.trackmate.detection.SpotDetectorFactory;
import fiji.plugin.trackmate.extra.action.RoiExporter;
import fiji.plugin.trackmate.features.FeatureFilter;
import fiji.plugin.trackmate.features.FeatureUtils;
import fiji.plugin.trackmate.features.ModelDataset;
import fiji.plugin.trackmate.features.spot.SpotContrastAndSNRAnalyzerFactory;
import fiji.plugin.trackmate.features.spot.SpotMorphologyAnalyzerFactory;
import fiji.plugin.trackmate.features.track.TrackAnalyzer;
import fiji.plugin.trackmate.features.track.TrackBranchingAnalyzer;
import fiji.plugin.trackmate.features.track.TrackDurationAnalyzer;
import fiji.plugin.trackmate.features.track.TrackIndexAnalyzer;
import fiji.plugin.trackmate.features.track.TrackLocationAnalyzer;
import fiji.plugin.trackmate.features.track.TrackSpeedStatisticsAnalyzer;
import fiji.plugin.trackmate.features.track.TrackSpotQualityFeatureAnalyzer;
import fiji.plugin.trackmate.graph.ConvexBranchesDecomposition;
import fiji.plugin.trackmate.graph.ConvexBranchesDecomposition.TrackBranchDecomposition;
import fiji.plugin.trackmate.graph.TimeDirectedNeighborIndex;
import fiji.plugin.trackmate.gui.Fonts;
import fiji.plugin.trackmate.gui.displaysettings.Colormap;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings.TrackDisplayMode;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings.TrackMateObject;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettingsIO;
import fiji.plugin.trackmate.io.TmXmlReader;
import fiji.plugin.trackmate.providers.DetectorProvider;
import fiji.plugin.trackmate.providers.EdgeAnalyzerProvider;
import fiji.plugin.trackmate.providers.SpotAnalyzerProvider;
import fiji.plugin.trackmate.providers.TrackAnalyzerProvider;
import fiji.plugin.trackmate.providers.TrackerProvider;
import fiji.plugin.trackmate.tracking.LAPUtils;
import fiji.plugin.trackmate.tracking.ManualTrackerFactory;
import fiji.plugin.trackmate.tracking.SpotTrackerFactory;
import fiji.plugin.trackmate.tracking.TrackerKeys;
import fiji.plugin.trackmate.tracking.kalman.KalmanTrackerFactory;
import fiji.plugin.trackmate.tracking.kdtree.NearestNeighborTrackerFactory;
import fiji.plugin.trackmate.tracking.sparselap.SimpleSparseLAPTrackerFactory;
import fiji.plugin.trackmate.tracking.sparselap.SparseLAPTrackerFactory;
import fiji.plugin.trackmate.visualization.FeatureColorGenerator;
import fiji.plugin.trackmate.visualization.PerTrackFeatureColorGenerator;
import fiji.plugin.trackmate.visualization.SpotColorGenerator;
import fiji.plugin.trackmate.visualization.TrackColorGenerator;
import fiji.plugin.trackmate.visualization.TrackMateModelView;
import fiji.plugin.trackmate.visualization.hyperstack.HyperStackDisplayer;
import fiji.plugin.trackmate.visualization.hyperstack.HyperStackDisplayerFactory;
import fiji.plugin.trackmate.visualization.hyperstack.SpotEditTool;
import fiji.plugin.trackmate.visualization.hyperstack.SpotOverlay;
import fiji.plugin.trackmate.visualization.hyperstack.TrackOverlay;
import fiji.plugin.trackmate.visualization.table.TablePanel;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.SelectionModel;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.DogDetectorFactory;
import fiji.plugin.trackmate.detection.LogDetector;
import fiji.plugin.trackmate.tracking.sparselap.SparseLAPTrackerFactory;
import fiji.plugin.trackmate.tracking.LAPUtils;
import fiji.plugin.trackmate.visualization.hyperstack.HyperStackDisplayer;
import fiji.plugin.trackmate.features.FeatureFilter;
import fiji.plugin.trackmate.features.AbstractFeatureGrapher;
import fiji.plugin.trackmate.features.EdgeCollectionDataset;
import fiji.plugin.trackmate.features.EdgeFeatureGrapher;
import fiji.plugin.trackmate.features.FeatureAnalyzer;
import fiji.plugin.trackmate.features.spot.SpotContrastAndSNRAnalyzerFactory;
import fiji.plugin.trackmate.action.CaptureOverlayAction;
import fiji.plugin.trackmate.action.ExportAllSpotsStatsAction;
import fiji.plugin.trackmate.io.TmXmlReader;
import fiji.plugin.trackmate.action.ExportTracksToXML;
import fiji.plugin.trackmate.action.ISBIChallengeExporter;
import fiji.plugin.trackmate.action.PlotNSpotsVsTimeAction;
import fiji.plugin.trackmate.action.TrackBranchAnalysis;
import fiji.plugin.trackmate.io.TmXmlWriter;
import fiji.plugin.trackmate.features.ModelFeatureUpdater;
import fiji.plugin.trackmate.features.SpotCollectionDataset;
import fiji.plugin.trackmate.features.SpotFeatureCalculator;
import fiji.plugin.trackmate.features.SpotFeatureGrapher;
import fiji.plugin.trackmate.features.TrackCollectionDataset;
import fiji.plugin.trackmate.features.TrackFeatureGrapher;
import fiji.plugin.trackmate.features.edges.EdgeAnalyzer;
import fiji.plugin.trackmate.features.edges.EdgeTargetAnalyzer;
import fiji.plugin.trackmate.features.edges.EdgeTimeLocationAnalyzer;
import fiji.plugin.trackmate.features.manual.ManualEdgeColorAnalyzer;
import fiji.plugin.trackmate.features.manual.ManualSpotColorAnalyzerFactory;
import fiji.plugin.trackmate.features.spot.SpotAnalyzerFactory;
import fiji.plugin.trackmate.features.spot.SpotContrastAndSNRAnalyzer;
import fiji.plugin.trackmate.features.track.TrackSpeedStatisticsAnalyzer;
import fiji.plugin.trackmate.util.ExportableChartPanel;
import fiji.plugin.trackmate.util.ModelTools;
import fiji.plugin.trackmate.util.TMUtils;
import checkable.CheckableItem;
import checkable.CheckedComboBox;

public class SPTBatch_ {

	static String csvPath, imgTitle;
	static String RADIUS;
	private String imagesPath, zstart, zend, ystart, yend, xstart, xend, tstart, tend, THRESHOLD, TARGET_CHANNEL,
			DO_SUBPIXEL_LOCALIZATION, DO_MEDIAN_FILTERING, DETECTOR_NAME, NSPLIT, DOWNSAMPLE_FACTOR, initialSpotFilter,
			TRACKER_NAME, CUTOFF_PERCENTILE, ALTERNATIVE_LINKING_COST_FACTOR, LINKING_MAX_DISTANCE, MAX_FRAME_GAP,
			MAX_DISTANCE, ALLOW_GAP_CLOSING, SPLITTING_MAX_DISTANCE, ALLOW_TRACK_SPLITTING, MERGING_MAX_DISTANCE,
			ALLOW_TRACK_MERGING, BLOCKING_VALUE, TransfPath, enableImages, enableLog, enableXML, enableRois, xmlPath,
			checkEnable, enableCsv, TRACKMATE_TRANSF_PATH, TRACKMATE_IMAGES_PATH, TRACKMATE_CSV_PATH,
			TRACKMATE_XML_PATH, TRACKMATE_TXT_PATH, TRACKMATE_OUT_PATH, xSelectedSpot, ySelectedSpot, xSelectedLink,
			ySelectedLink, xSelectedTrack, ySelectedTrack, enablePlotF, enableSql, enableST, enableSpotTable,
			enableLinkTable, enableTrackTable, enableBranchTable, enablePlot, titleExportLink, titleExportTrack,
			linkingNames, linkingValues, initialFilterFeature, initialFilterValue, initialFilterAbove,
			selectedOption = "N", enableCovariance, enableRegression, enableKalman, trackFilterFeature,
			trackFilterValue, trackFilterAbove, TRACKMATE_MIN_SPOT, TRACKMATE_MAX_SPOT, TRACKMATE_LENGTH_TH,
			TRACKMATE_DIFF_TH;
	public static String TRACKMATE_MIN_TRACK, TRACKMATE_WINDOW, TRACKMATE_MIN_SEGMENT, TRACKMATE_COLUMN_PARAM;
	String[] columnsMovements;
	private int[] dims;
	private JFreeChart chart;
	private JFrame frameChartNS;
	private Map<String, Double> hm;
	public static Thread mainProcess;
	public static final int PANEL_FIRST = 0, PANEL_CHOOSER = 1, PANEL_OPTION_A = 2, PANEL_OPTION_B = 3, PANEL_LAST = 4;
	private ImagePlus imp, capture;
	private JWizardPanel panel;
	private JButton nextButton, backButton, cancelButton, finishButton, buttonCsv, trajButton, summaryButton;
	private CheckedComboBox comboP;
	public static JCheckBox checkbox2, checkbox3, checkbox4, checkboxRoi, checkboxPlot, checkPlot, checkboxAnalysis,
			checkboxST, ESP, ELP, ETP, checkbox1, checkboxDiff, checkboxSP, checkboxMSD, checkDispSpots,
			checkDispTracks, checkSummary, checkPBS;
	public static JCheckBox checkTracks, checkboxSubBg;
	private int itemCheckPlot;
	private Button buttonXMLL, buttonImg;
	public static Preferences pref1;
	private Settings settings;
	public static int a, minTracksJTF, maxTracksJTF, thLengthJTF;
	private double thD14JTF;
	private JFrame f;
	private JWizardFrame wizard;
	private TrackMate trackmate;
	private Logger loggers;
	private ResultsTable spotTable, linkTable, trackTable, rtSpot, rtLink, rtTrack;
	private final String SPOT_TABLE_NAME = "Spots in tracks statistics", EDGE_TABLE_NAME = "Links in tracks statistics",
			TRACK_TABLE_NAME = "Track statistics", ID_COLUMN = "ID";
	private Spot source[];
	public static Model model;
	public static SelectionModel selectionModel;
	public static ImagePlus imps, impsSubBg;
	public ImagePlus impsNano;
	private HyperStackDisplayer displayer;
	private String[] imageTitles;
	private Set<Spot> track;
	private static final String TRACK_ID_COLUMN = "TRACK_ID";
	private PlotNSpotsVsTimeAction plot;
	// private SpotFeatureGrapherVersion sfg;
	private JFreeChart chartSpot, chartLink, chartTrack;
	private int itemPlot2, totalTracksDef, longTracksDef, longConfinedDef, longAnomDef, longFreeDef, longDirectDef,
			immobDef, shortTracksDef, shortConfinedDef, shortAnomDef, shortFreeDef, shortDirectDef;
	public static File directImages, directChemo, directDiff, directMSS;
	private JProgressBar progressBar;
	public static File directSummary, fileXmlInitial, directPBS;
	static JTextArea taskOutput;
	private JComboBox comboSpotsX, comboSpotsY, comboLinkX, comboLinkY, comboTrackX, comboTrackY, comboSubBg,
			comboDispTracks;
	PerTrackFeatureColorGenerator tcg;
	Calibration calibration;
	static JTable trackJTable, linkJTable;
	public ImagePlus[] lifs;
	ArrayList<Float> xPosition, yPosition;
	ArrayList<Integer> tracksID, framePosition;
	static JTextField chemoScaling, minTracks, maxTracks, thLength, thD14; // chemoThreshold;
	public static RoiManager roiManager;
	public static ImagePlus impMaxProject;
	public static ImagePlus[] slices;
	public static double[] slicesIntensityBg, slicesIntensitySpot;
	static JTable tableSpot = null, tableTrack = null;
	static String[] columnNamesTrack, columnNamesSpot;
	List<List<List<String>>> dataAllItemsDef = new ArrayList<List<List<String>>>();
	ArrayList<Integer> indexes;
	static List<Integer> nOfTracks;
	List<String> selectedItems = null;
	static double fps;

	// public Chemotaxis_ToolVersion chemotool = new Chemotaxis_ToolVersion();
	public SPTBatch_(String xmlPath, String imagesPath) {
		this.xmlPath = xmlPath;
		this.imagesPath = imagesPath;

	}

	public void run(String arg) {

		TRACKMATE_TRANSF_PATH = "transf_path";
		TRACKMATE_IMAGES_PATH = "images_path";
		TRACKMATE_CSV_PATH = "csv_path";
		TRACKMATE_XML_PATH = "xml_path";
		TRACKMATE_TXT_PATH = "txt_path";
		TRACKMATE_OUT_PATH = "out_path";
		TRACKMATE_MIN_SPOT = "min_spot";
		TRACKMATE_MAX_SPOT = "max_spot";
		TRACKMATE_LENGTH_TH = "length_th";
		TRACKMATE_DIFF_TH = "diff_th";
		TRACKMATE_MIN_TRACK = "min_track";
		TRACKMATE_WINDOW = "window";
		TRACKMATE_MIN_SEGMENT = "min_segment";
		TRACKMATE_COLUMN_PARAM = "column_param";
		pref1 = Preferences.userRoot();
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
		wizard = new JWizardFrame();
		wizard.setTitle("Single Particle Tracking-Batch");

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

		wizard.getWizardComponents().removeWizardPanel(0);

		wizard.setSize(550, 650);
		Utilities.centerComponentOnScreen(wizard);
		wizard.setResizable(false);
		wizard.setVisible(true);

		mainProcess = new Thread(new Runnable() {
			public void run() {

				// Spots
				xSelectedSpot = comboSpotsX.getSelectedItem().toString();
				ySelectedSpot = comboSpotsY.getSelectedItem().toString();

				// Links
				xSelectedLink = comboLinkX.getSelectedItem().toString();
				ySelectedLink = comboLinkY.getSelectedItem().toString();

				// Tracks
				xSelectedTrack = comboTrackX.getSelectedItem().toString();
				ySelectedTrack = comboTrackY.getSelectedItem().toString();

				fileXmlInitial = new File(xmlPath);
				File imageFolder = new File(imagesPath);
				File[] listOfFiles = imageFolder.listFiles();
				imageTitles = new String[listOfFiles.length];
				File filesXML[] = new File[listOfFiles.length];
				for (int u = 0; u < filesXML.length; u++)
					filesXML[u] = new File(xmlPath);
				Object[] columHeadersFinalSpot = new Object[] { "IMAGE_TITLE", "QUALITY", "POSITION_X", "POSITION_Y",
						"POSITION_Z", "POSITION_T", "FRAME", "RADIUS", "VISIBILITY", "MEAN_INTENSITY",
						"MEDIAN_INTENSITY", "MIN_INTENSITY", "MAX_INTENSITY", "TOTAL_INTENSITY", "STANDARD_DEVIATION",
						"CONTRAST", "SNR", "ESTIMATED_DIAMETER", "MORPHOLOGY", "ELLIPSOIDFIT_SEMIAXISLENGTH_C",
						"ELLIPSOIDFIT_SEMIAXISLENGTH_B", "ELLIPSOIDFIT_SEMIAXISLENGTH_A", "ELLIPSOIDFIT_AXISPHI_C",
						"ELLIPSOIDFIT_AXISPHI_B", "ELLIPSOIDFIT_AXISPHI_A", "ELLIPSOIDFIT_AXISTHETA_C",
						"ELLIPSOIDFIT_AXISTHETA_B", "ELLIPSOIDFIT_AXISTHETA_A" };
				Object[] columHeadersFinalLink = new Object[] { "IMAGE_TITLE", "LINK_COST", "EDGE_TIME",
						"EDGE_X_LOCATION", "EDGE_Y_LOCATION", "EDGE_Z_LOCATION", "VELOCITY", "DISPLACEMENT" };
				Object[] columHeadersFinalTrack = new Object[] { "IMAGE_TITLE", "TRACK_DURATION", "TRACK_START",
						"TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_MEAN_SPEED", "TRACK_MAX_SPEED", "TRACK_MIN_SPEED",
						"TRACK_MEDIAN_SPEED", "TRACK_STD_SPEED", "TRACK_INDEX", "TRACK_X_LOCATION", "TRACK_Y_LOCATION",
						"TRACK_Z_LOCATION", "NUMBER_SPOTS", "NUMBER_GAPS", "LONGEST_GAP", "NUMBER_SPLITS",
						"NUMBER_MERGES", "NUMBER_COMPLEX", "TRACK_MEAN_QUALITY", "TRACK_MAX_QUALITY",
						"TRACK_MIN_QUALITY", "TRACK_MEDIAN_QUALITY", "TRACK_STD_QUALITY", "TOTAL_DISTANCE_TRAVELED",
						"MAX_DISTANCE_TRAVELED", "MEAN_STRAIGHT_LINE_SPEED", "LINEARITY_OF_FORWARD_PROGRESSION",
						"TOTAL_ABSOLUTE_ANGLE_XY", "TOTAL_ABSOLUTE_ANGLE_YZ", "TOTAL_ABSOLUTE_ANGLE_ZX",
						"CONFINMENT_RATIO" };
				rtSpot = new ResultsTable(imageTitles.length);
				rtLink = new ResultsTable(imageTitles.length);
				rtTrack = new ResultsTable(imageTitles.length);
				for (int y = 0; y < columHeadersFinalSpot.length; y++)
					rtSpot.setHeading(y, (String) columHeadersFinalSpot[y]);
				for (int y = 0; y < columHeadersFinalLink.length; y++)
					rtLink.setHeading(y, (String) columHeadersFinalLink[y]);
				for (int y = 0; y < columHeadersFinalTrack.length; y++)
					rtTrack.setHeading(y, (String) columHeadersFinalTrack[y]);

				int MAX = listOfFiles.length;
				JFrame frameAnalyzer = new JFrame("Analyzing...");
				final JProgressBar pb = new JProgressBar();
				pb.setMinimum(0);
				pb.setMaximum(MAX);
				pb.setStringPainted(true);
				taskOutput = new JTextArea(5, 20);
				taskOutput.setMargin(new Insets(5, 5, 5, 5));
				taskOutput.setEditable(false);
				DefaultCaret caret = (DefaultCaret) taskOutput.getCaret();
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, 1));
				panel.add(pb);
				panel.add(Box.createVerticalStrut(5));
				panel.add(new JScrollPane(taskOutput), "Center");
				panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
				frameAnalyzer.getContentPane().add(panel);
				frameAnalyzer.setDefaultCloseOperation(2);
				frameAnalyzer.setSize(550, 500);
				frameAnalyzer.setVisible(true);

				for (int i = 0; i < listOfFiles.length; i++) {
					if (imps != null)
						imps.hide();

					if (listOfFiles[i].isFile()) {
						imageTitles[i] = listOfFiles[i].getName();
						imgTitle = imageTitles[i];
					}
					final int currentValue = i + 1;

					try {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								pb.setValue(currentValue);
								taskOutput.append(String.format("Completed %f%% of task.\n",
										(double) currentValue * 100.0D / (double) listOfFiles.length));
							}
						});
						Thread.sleep(100L);
					} catch (InterruptedException var69) {
						JOptionPane.showMessageDialog(frameAnalyzer, var69.getMessage());
					}
					if (listOfFiles[i].getName().contains(".lif") == true) {
						lifs = openBF((imagesPath + File.separator + imageTitles[i]), false, false, false, false, false,
								true);
						for (int x = 0; x < lifs.length; x++)
							imps = new ImagePlus(imagesPath + File.separator + imageTitles[i]);

					}
					if (listOfFiles[i].getName().contains(".lif") == false)
						imps = new ImagePlus(imagesPath + File.separator + imageTitles[i]);
					IJ.resetMinAndMax(imps);
					dims = imps.getDimensions();
					calibration = imps.getCalibration();

					fps = imps.getFileInfo().frameInterval;
					if (dims[4] == 1 && dims[3] > 1) {

						imps.setDimensions(dims[2], dims[4], dims[3]);
						calibration.frameInterval = 1;
						loggers = Logger.IJ_LOGGER;

					}
					impsSubBg = imps.duplicate();
					impsSubBg.setCalibration(calibration);
					directImages = new File(csvPath + File.separator + imageTitles[i].replaceAll("\\.tif+$", ""));

					if (!directImages.exists()) {
						taskOutput.append("creating directory: " + directImages.getName());
						boolean result = false;

						try {
							directImages.mkdir();
							result = true;
						} catch (SecurityException se) {
							// handle it
						}
						if (result) {
							taskOutput.append("DIR created");
						}
					}

					directSummary = new File(csvPath + File.separator + "Summary_Analysis");

					if (!directSummary.exists()) {
						boolean result = false;

						try {
							directSummary.mkdir();
							result = true;
						} catch (SecurityException se) {
							// handle it
						}
						if (result) {
							taskOutput.append("DIR created-Summary_Analysis");
						}
					}
					directPBS = new File(csvPath + File.separator + imageTitles[i].replaceAll("\\.tif+$", "")
							+ File.separator + "Photobleaching_Analysis");
					if (checkPBS.isSelected() == true) {
					
						if (!directPBS.exists()) {
							boolean result = false;

							try {
								directPBS.mkdir();
								result = true;
							} catch (SecurityException se) {
								// handle it
							}
							if (result) {
								taskOutput.append("DIR created-Photobleching_Analysis");
							}
						}

					}
					if (checkboxMSD.isSelected() == Boolean.TRUE) {
						directMSS = new File(directImages.getAbsolutePath() + File.separator + "MSS_Analysis");

						if (!directMSS.exists()) {
							taskOutput.append("creating directory: " + directMSS.getName());
							boolean result = false;

							try {
								directMSS.mkdir();
								result = true;
							} catch (SecurityException se) {
								// handle it
							}
							if (result) {
								taskOutput.append(directMSS.getName() + "  DIR created");
							}
						}
					}
					TmXmlReader reader = new TmXmlReader(fileXmlInitial);
					DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = null;
					try {
						builder = factory.newDocumentBuilder();
					} catch (ParserConfigurationException e) {

						e.printStackTrace();
					}

					Document doc = null;
					try {

						doc = builder.parse(fileXmlInitial);
					} catch (SAXException e) {

						e.printStackTrace();
					} catch (IOException e) {

						e.printStackTrace();
					}
					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath xpath = xPathfactory.newXPath();

					XPathExpression exprBasicSettings = null;
					XPathExpression exprDetectorSettings = null;
					XPathExpression exprInitialSpotFilter = null;
					XPathExpression exprFilter = null;
					XPathExpression exprTrackerSettings = null;
					XPathExpression exprLinking = null;
					XPathExpression exprGapClosing = null;
					XPathExpression exprSplitting = null;
					XPathExpression exprMerging = null;
					XPathExpression exprTrackFilter = null;
					XPathExpression exprLinkingP = null;

					try {

						exprBasicSettings = (XPathExpression) xpath.compile("//Settings/BasicSettings[@zstart]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprLinkingP = (XPathExpression) xpath.compile("//Linking/FeaturePenalties[@MEAN_INTENSITY]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}

					try {
						exprDetectorSettings = (XPathExpression) xpath.compile("//Settings/DetectorSettings[@RADIUS]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprInitialSpotFilter = (XPathExpression) xpath
								.compile("//Settings/InitialSpotFilter[@feature]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprFilter = (XPathExpression) xpath.compile("//SpotFilterCollection/Filter[@feature]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}

					try {

						exprTrackerSettings = (XPathExpression) xpath
								.compile("//Settings/TrackerSettings[@TRACKER_NAME]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprLinking = (XPathExpression) xpath
								.compile("//TrackerSettings/Linking[@LINKING_MAX_DISTANCE]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprGapClosing = (XPathExpression) xpath
								.compile("//TrackerSettings/GapClosing[@MAX_FRAME_GAP]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprSplitting = (XPathExpression) xpath
								.compile("//TrackerSettings/TrackSplitting[@SPLITTING_MAX_DISTANCE]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprMerging = (XPathExpression) xpath
								.compile("//TrackerSettings/TrackMerging[@MERGING_MAX_DISTANCE]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}
					try {

						exprTrackFilter = (XPathExpression) xpath.compile("//TrackFilterCollection/Filter[@feature]");
					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}

					NodeList nlBasicSettings = null;
					NodeList nlDetectorSettings = null;
					NodeList nlInitialSpotFilter = null;
					NodeList nlFilter = null;
					NodeList nlTrackerSettings = null;
					NodeList nlLinking = null;
					NodeList nlGapClosing = null;
					NodeList nlSplitting = null;
					NodeList nlMerging = null;
					NodeList nlTrackFilter = null;
					NodeList nlLinkingP = null;

					try {

						nlBasicSettings = (NodeList) exprBasicSettings.evaluate(doc, XPathConstants.NODESET);
						nlDetectorSettings = (NodeList) exprDetectorSettings.evaluate(doc, XPathConstants.NODESET);
						nlInitialSpotFilter = (NodeList) exprInitialSpotFilter.evaluate(doc, XPathConstants.NODESET);
						nlFilter = (NodeList) exprFilter.evaluate(doc, XPathConstants.NODESET);
						nlTrackerSettings = (NodeList) exprTrackerSettings.evaluate(doc, XPathConstants.NODESET);
						nlLinking = (NodeList) exprLinking.evaluate(doc, XPathConstants.NODESET);
						nlGapClosing = (NodeList) exprGapClosing.evaluate(doc, XPathConstants.NODESET);
						nlSplitting = (NodeList) exprSplitting.evaluate(doc, XPathConstants.NODESET);
						nlMerging = (NodeList) exprMerging.evaluate(doc, XPathConstants.NODESET);
						nlTrackFilter = (NodeList) exprTrackFilter.evaluate(doc, XPathConstants.NODESET);
						nlLinkingP = (NodeList) exprLinkingP.evaluate(doc, XPathConstants.NODESET);

					} catch (XPathExpressionException e) {

						e.printStackTrace();
					}

					for (int j = 0; j < nlBasicSettings.getLength(); j++) {
						Node currentItem = nlBasicSettings.item(j);
						zstart = currentItem.getAttributes().getNamedItem("zstart").getNodeValue();
						zend = currentItem.getAttributes().getNamedItem("zend").getNodeValue();
						ystart = currentItem.getAttributes().getNamedItem("ystart").getNodeValue();
						yend = currentItem.getAttributes().getNamedItem("yend").getNodeValue();
						xstart = currentItem.getAttributes().getNamedItem("xstart").getNodeValue();
						xend = currentItem.getAttributes().getNamedItem("xend").getNodeValue();
						tstart = currentItem.getAttributes().getNamedItem("tstart").getNodeValue();
						tend = currentItem.getAttributes().getNamedItem("tend").getNodeValue();
					}
					for (int j = 0; j < nlDetectorSettings.getLength(); j++) {
						Node currentItem = nlDetectorSettings.item(j);
						RADIUS = currentItem.getAttributes().getNamedItem("RADIUS").getNodeValue();
						THRESHOLD = currentItem.getAttributes().getNamedItem("THRESHOLD").getNodeValue();
						TARGET_CHANNEL = currentItem.getAttributes().getNamedItem("TARGET_CHANNEL").getNodeValue();
						DO_SUBPIXEL_LOCALIZATION = currentItem.getAttributes().getNamedItem("DO_SUBPIXEL_LOCALIZATION")
								.getNodeValue();
						DO_MEDIAN_FILTERING = currentItem.getAttributes().getNamedItem("DO_MEDIAN_FILTERING")
								.getNodeValue();
						DETECTOR_NAME = currentItem.getAttributes().getNamedItem("DETECTOR_NAME").getNodeValue();
						if (DETECTOR_NAME.equals("BLOCK_LOG_DETECTOR"))
							NSPLIT = currentItem.getAttributes().getNamedItem("NSPLIT").getNodeValue();
						if (DETECTOR_NAME.equals("DOWNSAMLE_LOG_DETECTOR"))
							DOWNSAMPLE_FACTOR = currentItem.getAttributes().getNamedItem("DOWNSAMPLE_FACTOR")
									.getNodeValue();

					}

					for (int j = 0; j < nlLinkingP.getLength(); j++) {
						linkingNames = nlLinkingP.item(j).getAttributes().item(j).getNodeName();
						linkingValues = nlLinkingP.item(j).getAttributes().item(j).getNodeValue();// IJ.log(linkingValues+"------------");
					}
					for (int j = 0; j < nlInitialSpotFilter.getLength(); j++) {
						Node currentItem = nlInitialSpotFilter.item(j);
						initialSpotFilter = currentItem.getAttributes().getNamedItem("value").getNodeValue();

					}
					List<String> spotFilterFeature = new ArrayList<String>();
					List<String> spotFilterValue = new ArrayList<String>();
					List<String> spotFilterAbove = new ArrayList<String>();
					for (int j = 0; j < nlFilter.getLength(); j++) {
						Node currentItem = nlFilter.item(j);
						spotFilterFeature.add(currentItem.getAttributes().getNamedItem("feature").getNodeValue());
						spotFilterValue.add(currentItem.getAttributes().getNamedItem("value").getNodeValue());
						spotFilterAbove.add(currentItem.getAttributes().getNamedItem("isabove").getNodeValue());

					}

					List<String> trackFilterFeature = new ArrayList<String>();
					List<String> trackFilterValue = new ArrayList<String>();
					List<String> trackFilterAbove = new ArrayList<String>();
					for (int j = 0; j < nlTrackFilter.getLength(); j++) {
						Node currentItem = nlTrackFilter.item(j);
						trackFilterFeature.add(currentItem.getAttributes().getNamedItem("feature").getNodeValue());
						trackFilterValue.add(currentItem.getAttributes().getNamedItem("value").getNodeValue());
						trackFilterAbove.add(currentItem.getAttributes().getNamedItem("isabove").getNodeValue());
					}
					for (int j = 0; j < nlTrackerSettings.getLength(); j++) {
						Node currentItem = nlTrackerSettings.item(j);
						TRACKER_NAME = currentItem.getAttributes().getNamedItem("TRACKER_NAME").getNodeValue();
						CUTOFF_PERCENTILE = currentItem.getAttributes().getNamedItem("CUTOFF_PERCENTILE")
								.getNodeValue();
						BLOCKING_VALUE = currentItem.getAttributes().getNamedItem("BLOCKING_VALUE").getNodeValue();
						ALTERNATIVE_LINKING_COST_FACTOR = currentItem.getAttributes()
								.getNamedItem("ALTERNATIVE_LINKING_COST_FACTOR").getNodeValue();
					}
					for (int j = 0; j < nlLinking.getLength(); j++) {
						Node currentItem = nlLinking.item(j);
						LINKING_MAX_DISTANCE = currentItem.getAttributes().getNamedItem("LINKING_MAX_DISTANCE")
								.getNodeValue();
					}
					for (int j = 0; j < nlGapClosing.getLength(); j++) {
						Node currentItem = nlGapClosing.item(j);
						MAX_FRAME_GAP = currentItem.getAttributes().getNamedItem("MAX_FRAME_GAP").getNodeValue();
						MAX_DISTANCE = currentItem.getAttributes().getNamedItem("GAP_CLOSING_MAX_DISTANCE")
								.getNodeValue();
						ALLOW_GAP_CLOSING = currentItem.getAttributes().getNamedItem("ALLOW_GAP_CLOSING")
								.getNodeValue();

					}
					for (int j = 0; j < nlSplitting.getLength(); j++) {
						Node currentItem = nlSplitting.item(j);
						SPLITTING_MAX_DISTANCE = currentItem.getAttributes().getNamedItem("SPLITTING_MAX_DISTANCE")
								.getNodeValue();
						ALLOW_TRACK_SPLITTING = currentItem.getAttributes().getNamedItem("ALLOW_TRACK_SPLITTING")
								.getNodeValue();
					}
					for (int j = 0; j < nlMerging.getLength(); j++) {
						Node currentItem = nlMerging.item(j);
						MERGING_MAX_DISTANCE = currentItem.getAttributes().getNamedItem("MERGING_MAX_DISTANCE")
								.getNodeValue();
						ALLOW_TRACK_MERGING = currentItem.getAttributes().getNamedItem("ALLOW_TRACK_MERGING")
								.getNodeValue();
					}

					settings = new Settings(imps);
					settings.dt = 0.05;
					taskOutput.append(settings.toStringImageInfo());
//
//					if (DETECTOR_NAME.equals("BLOCK_LOG_DETECTOR") == true) {
//						settings.detectorFactory = new BlockLogDetectorFactory();
//						settings.detectorSettings.put(BlockLogDetectorFactory.KEY_NSPLIT, Integer.parseInt(NSPLIT));
//						settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
//						settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL,
//								Integer.parseInt(TARGET_CHANNEL));
//						settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.parseDouble(THRESHOLD));
//						settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION,
//								Boolean.parseBoolean(DO_SUBPIXEL_LOCALIZATION));
//						settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING,
//								Boolean.parseBoolean(DO_MEDIAN_FILTERING));
//						if (initialSpotFilter != null)
//							settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);
//
//					}

					if (DETECTOR_NAME.equals("LOG_DETECTOR") == true) {
						settings.detectorFactory = new LogDetectorFactory();
						settings.detectorSettings = settings.detectorFactory.getDefaultSettings();
						settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION,
								Boolean.parseBoolean(DO_SUBPIXEL_LOCALIZATION));
						settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
						settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL,
								Integer.parseInt(TARGET_CHANNEL));
						settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.parseDouble(THRESHOLD));
						settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING,
								Boolean.parseBoolean(DO_MEDIAN_FILTERING));
						if (initialSpotFilter != null)
							settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);

					}
//					if (DETECTOR_NAME.equals("DOWNSAMPLE_LOG_DETECTOR") == true) {
//
//						settings.detectorFactory = new DownsampleLogDetectorFactory();
//						settings.detectorSettings.put(DetectorKeys.KEY_DOWNSAMPLE_FACTOR, DOWNSAMPLE_FACTOR);
//						settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
//						settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL,
//								Integer.parseInt(TARGET_CHANNEL));
//						settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.parseDouble(THRESHOLD));
//						if (initialSpotFilter != null)
//							settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);
//
//					}

//					if (DETECTOR_NAME.equals("MANUAL_DETECTOR") == true) {
//
//						settings.detectorFactory = new ManualDetectorFactory();
//						settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
//						if (initialSpotFilter != null)
//							settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);
//
//					}

					if (DETECTOR_NAME.equals("DOG_DETECTOR") == true) {

						settings.detectorFactory = new DogDetectorFactory();
						settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION,
								Boolean.parseBoolean(DO_SUBPIXEL_LOCALIZATION));
						settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
						settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL,
								Integer.parseInt(TARGET_CHANNEL));
						settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.parseDouble(THRESHOLD));
						settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING,
								Double.parseDouble(DO_MEDIAN_FILTERING));
						if (initialSpotFilter != null)
							settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);

					}
					List<FeatureFilter> spotFilters = new ArrayList<FeatureFilter>();
					for (int j = 0; j < spotFilterFeature.size(); j++)
						spotFilters.add(new FeatureFilter(spotFilterFeature.get(j),
								Double.valueOf(spotFilterValue.get(j)).doubleValue(),
								Boolean.valueOf(spotFilterAbove.get(j))));
					for (int j = 0; j < spotFilters.size(); j++)
						settings.addSpotFilter(spotFilters.get(j));

					if (TRACKER_NAME.equals("MANUAL_TRACKER") == true) {

						settings.trackerFactory = new ManualTrackerFactory();
						settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();

					}
					if (TRACKER_NAME.equals("MANUAL_TRACKER") == true) {

						settings.trackerFactory = new ManualTrackerFactory();
						settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();

					}

					if (TRACKER_NAME.equals("KALMAN_TRACKER") == true) {

						settings.trackerFactory = new KalmanTrackerFactory();
						settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();
						settings.trackerSettings.put(KalmanTrackerFactory.KEY_KALMAN_SEARCH_RADIUS,
								Double.parseDouble(RADIUS));

					}

					if (TRACKER_NAME.equals("SIMPLE_SPARSE_LAP_TRACKER") == true) {

						settings.trackerFactory = new SimpleSparseLAPTrackerFactory();
						settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();
						settings.trackerSettings.put(TrackerKeys.KEY_LINKING_MAX_DISTANCE,
								Double.parseDouble(LINKING_MAX_DISTANCE));
						settings.trackerSettings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_DISTANCE,
								Double.parseDouble(MAX_DISTANCE));
						settings.trackerSettings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_FRAME_GAP,
								Double.parseDouble(MAX_FRAME_GAP));

					}

					if (TRACKER_NAME.equals("SPARSE_LAP_TRACKER") == true) {
						settings.trackerFactory = new SparseLAPTrackerFactory();
						settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();
						settings.trackerSettings.put(TrackerKeys.KEY_LINKING_MAX_DISTANCE,
								Double.parseDouble(LINKING_MAX_DISTANCE));
						Map<String, Double> linkingPenalty = Stream
								.of(new Object[][] { { "MEAN_INTENSITY", 1.0 }, { "QUALITY", 1.0 },

								}).collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));
						settings.trackerSettings.put(TrackerKeys.KEY_ALLOW_GAP_CLOSING,
								Boolean.parseBoolean(ALLOW_GAP_CLOSING));
						if (Boolean.parseBoolean(ALLOW_GAP_CLOSING) == true) {
							settings.trackerSettings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_FRAME_GAP,
									Integer.parseInt(MAX_FRAME_GAP));
							settings.trackerSettings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_DISTANCE,
									Double.parseDouble(MAX_DISTANCE));

							Map<String, Double> gapPenalty = Stream
									.of(new Object[][] { { "MEAN_INTENSITY", 1.0 }, { "QUALITY", 1.0 }, })
									.collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));

						}

						settings.trackerSettings.put(TrackerKeys.KEY_ALLOW_TRACK_SPLITTING,
								Boolean.parseBoolean(ALLOW_TRACK_SPLITTING));
						if (Boolean.parseBoolean(ALLOW_TRACK_SPLITTING) == true) {
							settings.trackerSettings.put(TrackerKeys.KEY_SPLITTING_MAX_DISTANCE,
									Double.parseDouble(SPLITTING_MAX_DISTANCE));
							Map<String, Double> splitPenalty = Stream
									.of(new Object[][] { { "MEAN_INTENSITY", 1.0 }, { "QUALITY", 1.0 }, })
									.collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));

						}

						settings.trackerSettings.put(TrackerKeys.KEY_ALLOW_TRACK_MERGING,
								Boolean.parseBoolean(ALLOW_TRACK_MERGING));
						if (Boolean.parseBoolean(ALLOW_TRACK_MERGING) == true) {
							settings.trackerSettings.put(TrackerKeys.KEY_MERGING_MAX_DISTANCE,
									Double.parseDouble(MERGING_MAX_DISTANCE));
							Map<String, Double> mergePenalty = Stream
									.of(new Object[][] { { "MEAN_INTENSITY", 1.0 }, { "QUALITY", 1.0 }, })
									.collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));

						}

					}
					List<FeatureFilter> trackFilters = new ArrayList<FeatureFilter>();
					for (int j = 0; j < trackFilterFeature.size(); j++)
						trackFilters.add(new FeatureFilter(trackFilterFeature.get(j),
								Double.valueOf(trackFilterValue.get(j)).doubleValue(),
								Boolean.valueOf(trackFilterAbove.get(j))));
					for (int j = 0; j < trackFilters.size(); j++)
						settings.addTrackFilter(trackFilters.get(j));

					if (checkboxSubBg.isSelected() == true) {
						slices = stack2images(impsSubBg);
						slicesIntensityBg = new double[slices.length];
						slicesIntensitySpot = new double[slices.length];
						if (comboSubBg.getSelectedIndex() == 0) {
							impMaxProject = ZProjector.run(impsSubBg.duplicate(), "max");
							impMaxProject.show();
							roiManager = RoiManager.getInstance();
							if (null == roiManager) {
								roiManager = new RoiManager();
							}
							roiManager.reset();
							impMaxProject.getCanvas().addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent e) {
									if (e.getClickCount() == 2) {

										Roi roi = new Roi(impMaxProject.getCanvas().offScreenX(e.getX()),
												impMaxProject.getCanvas().offScreenY(e.getY()), (int) (5), (int) (5));
										impMaxProject.setRoi(roi);
										roiManager.runCommand(impMaxProject, "Show All with labels");
										roiManager.addRoi(roi);
									}

								}

							});
							Dialog4BgSub0 userDialog = new Dialog4BgSub0("Action Required",
									"Please select manually areas to measure background.");
							userDialog.show();
						}

						if (comboSubBg.getSelectedIndex() == 2)

						{
							for (int j = 0; j < slices.length; j++) {
								ImagePlus slicesDup = slices[j].duplicate();
								IJ.run(slicesDup, "Auto Threshold", "method=Otsu ignore_black white");
								slicesDup = new ImagePlus(slicesDup.getTitle(),
										Morphology.dilation(slicesDup.getProcessor(), SquareStrel.fromRadius(3)));
								IJ.run(slicesDup, "Create Selection", "");
								Roi roiToMeasure = slicesDup.getRoi();
//								Roi[] roisSpots = new ShapeRoi(roiSpots).getRois();
//								List<Double> areaRois = new ArrayList<Double>();
//								for (int s = 0; s < roisSpots.length; s++)
//									areaRois.add(roisSpots[s].getStatistics().area);
//								Double maxRoi = Collections.max(areaRois);
//								int indexMaxRoi = areaRois.indexOf(maxRoi);
//								Roi roiToMeasure = roisSpots[indexMaxRoi];
								slices[j].setRoi(roiToMeasure);
								slicesIntensitySpot[j] = slices[j].getStatistics().mean;

							}
						}
						if (comboSubBg.getSelectedIndex() == 3) {
							for (int j = 0; j < slices.length; j++) {
								ImagePlus slicesDup = slices[j].duplicate();
								IJ.run(slicesDup, "Subtract Background...", "rolling=2000 light");
								IJ.run(slicesDup, "Auto Threshold", "method=Otsu ignore_black white");
								// IJ.run(slicesDup, "Fill Holes", "");
								slicesDup = new ImagePlus(slicesDup.getTitle(), Morphology
										.closing(slicesDup.getProcessor(), Strel.Shape.DISK.fromRadius((int) 0.5)));
								IJ.run(slicesDup, "Create Selection", "");
								Roi roiSpots = slicesDup.getRoi();
								// IJ.run(slicesDup, "Make Inverse", "");
								// Roi roiBg = slices[j].getRoi();
								slices[j].setRoi(roiSpots);
								slicesIntensitySpot[j] = slices[j].getStatistics().mean;
								// slices[j].setRoi(roiBg);
								// slicesIntensityBg[j] = slices[j].getStatistics().mean;
							}
						}

					}

					settings.addAllAnalyzers();
					model = new Model();
					trackmate = new TrackMate(model, settings);
					Boolean ok = trackmate.checkInput();
					if (ok != Boolean.TRUE)
						taskOutput.append(trackmate.getErrorMessage());

					ok = trackmate.process();
					// model = trackmate.getModel();

					// double qMean = qSum.stream().mapToDouble(d -> d).average().orElse(0.0);
					// IJ.log((2 * qMean) + "---");
//					settings.addTrackAnalyzer(new TrackDurationAnalyzer());
//					settings.addSpotAnalyzerFactory(new SpotIntensityAnalyzerFactory());
//					settings.addSpotAnalyzerFactory(new SpotContrastAndSNRAnalyzerFactory());
//					settings.addTrackAnalyzer(new TrackSpeedStatisticsAnalyzer());

//					model = new Model();
//					trackmate = new TrackMate(model, settings);
//					ok = trackmate.checkInput();
//					if (ok != Boolean.TRUE)
//						taskOutput.append(trackmate.getErrorMessage());
//
//					settings.initialSpotFilterValue = (2 * qMean);
//
//					ok = trackmate.process();
					selectionModel = new SelectionModel(model);
//					model.getSpots().getNSpots(true);
//					model.getTrackModel().nTracks(true);
//					settings.addSpotAnalyzerFactory(new SpotIntensityAnalyzerFactory());
//					settings.addSpotAnalyzerFactory(new SpotContrastAndSNRAnalyzerFactory());
//					settings.addSpotAnalyzerFactory(new SpotRadiusEstimatorFactory());
//					settings.addSpotAnalyzerFactory(new SpotMorphologyAnalyzerFactory());
//					settings.addSpotAnalyzerFactory(new SpotContrastAnalyzerFactory());
//					settings.addSpotAnalyzerFactory(new ManualSpotColorAnalyzerFactory());
//					settings.addEdgeAnalyzer(new EdgeTargetAnalyzer());
//					settings.addEdgeAnalyzer(new EdgeTimeLocationAnalyzer());
//					settings.addEdgeAnalyzer(new EdgeVelocityAnalyzer());
//					settings.addEdgeAnalyzer(new ManualEdgeColorAnalyzer());
//					settings.addTrackAnalyzer(new TrackSpeedStatisticsAnalyzer());
//					settings.addTrackAnalyzer(new TrackDurationAnalyzer());
//					settings.addTrackAnalyzer(new TrackIndexAnalyzer());
//					settings.addTrackAnalyzer(new TrackLocationAnalyzer());
//					settings.addTrackAnalyzer(new TrackBranchingAnalyzer());
//					settings.addTrackAnalyzer(new TrackSpotQualityFeatureAnalyzer());
//					trackmate.computeSpotFeatures(true);
//					trackmate.computeTrackFeatures(true);
//					trackmate.computeEdgeFeatures(true);

					model.setLogger(Logger.IJ_LOGGER);
					SpotCollection spots = model.getSpots();
					taskOutput.append(spots.toString());
					FeatureModel fm = model.getFeatureModel();
//					Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
//					List<List<Double>> xSpotInTrackN = new ArrayList<List<Double>>();
//					List<List<Double>> ySpotInTrackN = new ArrayList<List<Double>>();
//					List<Color> trackColor = new ArrayList<Color>();
					tcg = new PerTrackFeatureColorGenerator(model, TrackIndexAnalyzer.TRACK_INDEX, null, null,
							Colormap.Turbo, 0, 1);

//					for (Integer id : trackIDs) {
//
//						trackColor.add(tcg.colorOf(id));
//						taskOutput.append(id + "-" + model.getTrackModel().trackEdges(id).toString());
//						Double v = fm.getTrackFeature(id, "TRACK_MEAN_SPEED");
//						taskOutput.append("");
//						taskOutput.append("Track " + id + ": mean velocity = " + v + "  " + model.getSpaceUnits()
//								+ File.separator + model.getTimeUnits());
//						track = model.getTrackModel().trackSpots(id);
					// List<Double> xTrack = new ArrayList<Double>();
					// List<Double> yTrack = new ArrayList<Double>();
					// List<Double> xTrackN = new ArrayList<Double>();
					// List<Double> yTrackN = new ArrayList<Double>();
					// for (Spot spot : track) {

					// xTrack.add(spot.getFeature("POSITION_X"));
					// yTrack.add(spot.getFeature("POSITION_Y"));

					// }
					// for (int u = 0; u < xTrack.size(); u++) {
					// xTrackN.add(xTrack.get(u) - xTrack.get(0));
					// yTrackN.add(yTrack.get(u) - yTrack.get(0));
					// }
					// xSpotInTrackN.add(xTrackN);
					// ySpotInTrackN.add(yTrackN);

					// }

					// TrackPlot_ demo = new TrackPlot_(xSpotInTrackN, ySpotInTrackN, trackColor,
					// dims[0] * calibration.pixelWidth, dims[1] * calibration.pixelHeight,
					// directImages + File.separator + imps.getShortTitle() + "figura_.png");

					// final XYSplineRendererDemo demo1 = new XYSplineRendererDemo(xSpotInTrackN,
					// ySpotInTrackN, trackColor,
					// dims[0] * calibration.pixelWidth, dims[1] * calibration.pixelHeight,
					// directImages + File.separator + imps.getShortTitle() + "figura_.png");
					// demo1.pack();
					// RefineryUtilities.centerFrameOnScreen(demo1);
					// demo1.setVisible(true);

					// demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					// demo.pack();
					// demo.setLocationRelativeTo(null);
					// demo.setVisible(true);

					taskOutput.append("\n\nSETTINGS:");
					taskOutput.append(settings.toString());

					taskOutput.append(model.toString());
					taskOutput.append("Found" + model.getTrackModel().nTracks(true) + " tracks.");
					taskOutput.append(settings.toStringFeatureAnalyzersInfo());
					Rectangle bounds;
					Integer firstFrame = null;
					Integer lastFrame = null;
					Integer width;
					Integer height;
					Integer nCaptures;
					ImageStack stack;
					Integer channel;
					Integer slice;
					BufferedImage bi;
					ColorProcessor cp;
					Integer index;

//					if (optionBRadioButton.isSelected() == Boolean.TRUE) {
//						SpotDisplayer3DFactory factory3D = new SpotDisplayer3DFactory();
//						TrackMateModelView factory3Disp = factory3D.create(model, settings, selectionModel);
//						factory3Disp.render();
//						factory3Disp.refresh();
//						taskOutput.append(model.toString());
//
//					}
//					if (optionNRadioButton.isSelected() == Boolean.TRUE) {
//
//						DisplaySettings ds = DisplaySettingsIO.readUserDefault();
//						ds.setSpotShowName(true);
//						ds.setSpotVisible(true);
//						ds.setSpotColorBy(TrackMateObject.TRACKS, "TRACK_INDEX");
//						ds.setTrackVisible(true);
//						ds.setTrackColorBy(TrackMateObject.TRACKS, "TRACK_INDEX");
//						ds.setTrackDisplayMode(TrackDisplayMode.FULL);
//						displayer = new HyperStackDisplayer(model, selectionModel, imps, ds);
//						ImagePlus impDisplayer = renderND(displayer, ds);
//						if (imps.getNFrames() > 1) {
//							firstFrame = Math.max(1, Math.min(imps.getNFrames(), 1));
//							lastFrame = Math.min(imps.getNFrames(), Math.max(imps.getNFrames(), 1));
//						}
//						if (imps.getNSlices() > 1) {
//							firstFrame = Math.max(1, Math.min(imps.getNSlices(), 1));
//							lastFrame = Math.min(imps.getNSlices(), Math.max(imps.getNSlices(), 1));
//						}
//
//						taskOutput.append(
//								"Capturing TrackMate overlay from frame " + firstFrame + " to " + lastFrame + ".\n");
//						ImageCanvas canvas = new ImageCanvas(impDisplayer);
//
//						bounds = canvas.getBounds();
//						width = bounds.width;
//						height = bounds.height;
//						nCaptures = lastFrame - firstFrame + 1;
//						stack = new ImageStack(width, height);
//						channel = impDisplayer.getChannel();
//						slice = impDisplayer.getSlice();
//						canvas.hideZoomIndicator(true);
//						for (int frame = firstFrame; frame <= lastFrame; frame++) {
//							// taskOutput.append(String.valueOf((frame - firstFrame) / nCaptures) + "\n");
//							canvas.getImage().setPositionWithoutUpdate(channel, slice, frame);
//							bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							canvas.paint(bi.getGraphics());
//							cp = new ColorProcessor(bi);
//							index = canvas.getImage().getStackIndex(channel, slice, frame);
//							stack.addSlice(canvas.getImage().getImageStack().getSliceLabel(index), cp);
//						}
//						canvas.hideZoomIndicator(false);
//						canvas.getImage().show();
//						capture = new ImagePlus("TrackMate capture of " + impDisplayer.getShortTitle(), stack);
//						transferCalibration(impDisplayer, capture);
//						taskOutput.append(" done.\n");
//
//					}

					// if (optionARadioButton.isSelected() == Boolean.TRUE) {

					DisplaySettings ds = DisplaySettingsIO.readUserDefault();
					ds.setSpotShowName(checkDispSpots.isSelected());
					ds.setSpotVisible(checkDispSpots.isSelected());
					ds.setSpotColorBy(TrackMateObject.TRACKS, "TRACK_INDEX");
					ds.setTrackVisible(checkDispTracks.isSelected());
					ds.setTrackColorBy(TrackMateObject.TRACKS, "TRACK_INDEX");
					if (comboDispTracks.getSelectedIndex() == 0)
						ds.setTrackDisplayMode(TrackDisplayMode.FULL);
					if (comboDispTracks.getSelectedIndex() == 1)
						ds.setTrackDisplayMode(TrackDisplayMode.LOCAL);
					if (comboDispTracks.getSelectedIndex() == 2)
						ds.setTrackDisplayMode(TrackDisplayMode.LOCAL_BACKWARD);
					if (comboDispTracks.getSelectedIndex() == 3)
						ds.setTrackDisplayMode(TrackDisplayMode.LOCAL_FORWARD);

					displayer = new HyperStackDisplayer(model, selectionModel, imps, ds);
					displayer.render();
					displayer.refresh();

					if (imps.getNFrames() > 1) {
						firstFrame = Math.max(1, Math.min(imps.getNFrames(), 1));
						lastFrame = Math.min(imps.getNFrames(), Math.max(imps.getNFrames(), 1));
					}
					if (imps.getNSlices() > 1) {
						firstFrame = Math.max(1, Math.min(imps.getNSlices(), 1));
						lastFrame = Math.min(imps.getNSlices(), Math.max(imps.getNSlices(), 1));
					}

					taskOutput.append(
							"Capturing TrackMate overlay from frame " + firstFrame + " to " + lastFrame + ".\n");
					bounds = displayer.getImp().getCanvas().getBounds();
					width = bounds.width;
					height = bounds.height;
					nCaptures = lastFrame - firstFrame + 1;
					stack = new ImageStack(width, height);
					channel = displayer.getImp().getChannel();
					slice = displayer.getImp().getSlice();
					displayer.getImp().getCanvas().hideZoomIndicator(true);
					for (int frame = firstFrame; frame <= lastFrame; frame++) {
						// taskOutput.append(String.valueOf((frame - firstFrame) / nCaptures) + "\n");
						displayer.getImp().setPositionWithoutUpdate(channel, slice, frame);
						bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
						displayer.getImp().getCanvas().paint(bi.getGraphics());
						cp = new ColorProcessor(bi);
						index = displayer.getImp().getStackIndex(channel, slice, frame);
						stack.addSlice(displayer.getImp().getImageStack().getSliceLabel(index), cp);
					}
					displayer.getImp().getCanvas().hideZoomIndicator(false);
					capture = new ImagePlus("TrackMate capture of " + displayer.getImp().getShortTitle(), stack);

					transferCalibration(displayer.getImp(), capture);
					taskOutput.append(" done.\n");

					// }
					if (checkboxRoi.isSelected() == Boolean.TRUE) {

						final double dx = imps.getCalibration().pixelWidth;
						final double dy = imps.getCalibration().pixelHeight;
						final double dz = imps.getCalibration().pixelDepth;
						RoiManager roiManager = RoiManager.getInstance();
						if (null == roiManager) {
							roiManager = new RoiManager();
						}
						roiManager.reset();
						List<Spot> spotsRoi = new ArrayList<>(trackmate.getModel().getSpots().getNSpots(true));
						for (Integer trackID : trackmate.getModel().getTrackModel().trackIDs(true))
							spotsRoi.addAll(trackmate.getModel().getTrackModel().trackSpots(trackID));
						for (int s = 0; s < spotsRoi.size(); s++) {
							final SpotRoi sroi = spotsRoi.get(s).getRoi();
							final Roi roi;
							if (sroi != null) {
								final double[] xs = sroi.toPolygonX(dx, 0., spotsRoi.get(s).getDoublePosition(0), 1.);
								final double[] ys = sroi.toPolygonY(dy, 0., spotsRoi.get(s).getDoublePosition(1), 1.);
								final float[] xp = toFloat(xs);
								final float[] yp = toFloat(ys);
								roi = new PolygonRoi(xp, yp, PolygonRoi.POLYGON);
							} else {
								final double diameter = 2. * spotsRoi.get(s).getFeature(Spot.RADIUS).doubleValue() / dx;
								final double xs = spotsRoi.get(s).getDoublePosition(0) / dx - diameter / 2. + 0.5;
								final double ys = spotsRoi.get(s).getDoublePosition(1) / dy - diameter / 2. + 0.5;
								roi = new OvalRoi(xs, ys, diameter, diameter);
							}

							final int z = 1 + (int) Math.round(spotsRoi.get(s).getDoublePosition(2) / dz);
							final int frame = 1 + spotsRoi.get(s).getFeature(Spot.FRAME).intValue();
							roi.setPosition(0, z, frame);
							roi.setName(spotsRoi.get(s).getName());
							roiManager.addRoi(roi);

						}
						roiManager.runCommand("Save",
								directImages + File.separator + imps.getShortTitle() + "_" + "RoiSet.zip");
						roiManager.close();
//						final NavigableSet<Integer> frames = trackmate.getModel().getSpots().keySet();
//						for (final int frame : frames) {
//							final int points = trackmate.getModel().getSpots().getNSpots(frame, true);
//							final float[] ox = new float[points];
//							final float[] oy = new float[points];
//							final Iterable<Spot> iterable = trackmate.getModel().getSpots().iterable(frame, true);
//							int indexx = 0;
//							for (final Spot spot : iterable) {
//								final double x = spot.getDoublePosition(0) / dx;
//								final double y = spot.getDoublePosition(1) / dy;
//
//								ox[indexx] = (float) x;
//								oy[indexx] = (float) y;
//
//								indexx++;
//							}
//
//							final PointRoi roi = new PointRoi(ox, oy, points);
//							roiManager.addRoi(roi);
//						}

					}
					if (checkbox2.isSelected() == Boolean.TRUE) {
						// settings.imp = imps;
						taskOutput.append(model.toString());
						ISBIChallengeExporterModified.exportToFile(model, settings,
								new File(directImages.getAbsolutePath() + File.separator + "TrackMate_"
										+ imps.getShortTitle() + ".xml"));
						// ISBIChallengeExporterModified.exportToFile(settings, trackmate,
						// new File(directImages + File.separator + "TrackMate_" + imps.getShortTitle()
						// + ".xml"));
						taskOutput.append("\nDone.");

					}
					if (enableST == "ST") {
						Model model = trackmate.getModel();
						Settings settings = trackmate.getSettings();
						SpotCollection spots1 = model.getSpots();
						int nFrames = spots1.keySet().size();
						double[][] data = new double[2][nFrames];
						int indexx = 0;
						for (final int frame : spots1.keySet()) {
							data[1][indexx] = spots1.getNSpots(frame, true);
							if (data[1][indexx] > 0) {
								data[0][indexx] = spots1.iterable(frame, false).iterator().next()
										.getFeature(Spot.POSITION_T);
							} else {
								data[0][indexx] = frame * settings.dt;
							}
							indexx++;
						}

						String xAxisLabel = "Time (" + trackmate.getModel().getTimeUnits() + ")";
						String yAxisLabel = "N spots";
						String title = "Nspots vs Time for " + trackmate.getSettings().imp.getShortTitle();
						DefaultXYDataset dataset = new DefaultXYDataset();
						dataset.addSeries("Nspots", data);

						chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset,
								PlotOrientation.VERTICAL, true, true, false);
						chart.getTitle().setFont(Fonts.FONT);
						chart.getLegend().setItemFont(Fonts.SMALL_FONT);
						ExportableChartPanel chartPanel = new ExportableChartPanel(chart);

					}

					if (checkbox1.isSelected() == Boolean.TRUE) {

						Double spotSum;
						Double spotAverag;
						// SLTResultsTableVersion sltTable = new SLTResultsTableVersion(selectionModel);
						// sltTable.executeSpot(trackmate);

						if (enableSpotTable.equals("spotTable") == true) {

							// DisplaySettings ds = DisplaySettingsIO.readUserDefault();

							TablePanel<Spot> spotTable = createSpotTable(model, ds);
							JTable spotJTable = spotTable.getTable();
							TablePanel<Integer> trackTable = createTrackTable(model, ds);
							trackJTable = trackTable.getTable();
							nOfTracks = new ArrayList<Integer>();
							for (int t = 0; t < trackJTable.getModel().getRowCount(); t++)
								nOfTracks.add(Integer.valueOf(trackJTable.getModel().getValueAt(t, 2).toString()));
							indexes = new ArrayList<Integer>();
							Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
							Set<Spot> track = null;
							int counter = 0;

							for (int n = 0; n < nOfTracks.size(); n++) {
								ArrayList<Float> framesByTrack = new ArrayList<Float>();
								ArrayList<Float> framesByTrackSort = new ArrayList<Float>();

								for (int r = 0; r < spotJTable.getRowCount(); r++) {

									if (spotJTable.getModel().getValueAt(r, 2).toString()
											.equals(String.valueOf(nOfTracks.get(n).intValue())) == true) {
										framesByTrack
												.add(Float.valueOf(spotJTable.getModel().getValueAt(r, 8).toString()));
										framesByTrackSort
												.add(Float.valueOf(spotJTable.getModel().getValueAt(r, 8).toString()));

									}

								}

								Collections.sort(framesByTrackSort);

								for (int z = 0; z < framesByTrackSort.size(); z++) {
									counter++;
									if (n == 0)
										indexes.add(framesByTrack.indexOf(framesByTrackSort.get(z)));
									if (n != 0)
										indexes.add(
												(counter - 1) + framesByTrack.indexOf(framesByTrackSort.get(z)) - z);

								}

							}

							if (checkboxSubBg.isSelected() == true) {
								// spotJTable.setAutoCreateColumnsFromModel(false);
								columnNamesSpot = new String[] { "LABEL", "ID", "TRACK_ID", "QUALITY", "POSITION_X",
										"POSITION_Y", "POSITION_Z", "POSITION_T", "FRAME", "RADIUS", "VISIBILITY",
										"MANUAL_SPOT_COLOR", "MEAN_INTENSITY_CH1", "MEDIAN_INTENSITY_CH1",
										"MIN_INTENSITY_CH1", "MAX_INTENSITY_CH1", "TOTAL_INTENSITY_CH1",
										"STD_INTENSITY_CH1", "CONTRAST_CH1", "SNR_CH1", "Intensity-Bg Subtract" };

								String[][] rowDataSpot = new String[indexes.size()][columnNamesSpot.length];

								for (int r = 0; r < indexes.size(); r++) {
									rowDataSpot[r][columnNamesSpot.length - 1] = "";
									for (int c = 0; c < spotJTable.getModel().getColumnCount(); c++) {
										rowDataSpot[r][c] = String.valueOf(
												spotJTable.getModel().getValueAt(indexes.get(r).intValue(), c));
									}
								}

								if (comboSubBg.getSelectedIndex() == 1) {
									for (int r = 0; r < rowDataSpot.length; r++)
										rowDataSpot[r][columnNamesSpot.length - 1] = String
												.valueOf((Double.valueOf(rowDataSpot[r][12].toString())
														- (Double.valueOf(rowDataSpot[r][19].toString())
																* Double.valueOf(rowDataSpot[r][17].toString()))));

									DefaultTableModel tableModel = new DefaultTableModel(rowDataSpot, columnNamesSpot);
									tableSpot = new JTable(tableModel);

								}
								if (comboSubBg.getSelectedIndex() == 0 || comboSubBg.getSelectedIndex() == 2
										|| comboSubBg.getSelectedIndex() == 3) {

									for (int r = 0; r < spotJTable.getModel().getRowCount(); r++)
										for (int j = 0; j < slicesIntensitySpot.length; j++)
											if (Integer.valueOf(rowDataSpot[r][8].toString())
													.equals(j) == Boolean.TRUE) {
												rowDataSpot[r][columnNamesSpot.length - 1] = String
														.valueOf((Double.valueOf(rowDataSpot[r][12].toString())
																- Double.valueOf(slicesIntensitySpot[j])));

											}
									tableSpot = new JTable(rowDataSpot, columnNamesSpot);

								}

								exportToCSV(tableSpot, new File(directImages.getAbsolutePath() + File.separator
										+ imps.getShortTitle() + "_" + "Spots in tracks statistics" + ".csv"));
							}
							if (checkboxSubBg.isSelected() == false) {

								columnNamesSpot = new String[] { "LABEL", "ID", "TRACK_ID", "QUALITY", "POSITION_X",
										"POSITION_Y", "POSITION_Z", "POSITION_T", "FRAME", "RADIUS", "VISIBILITY",
										"MANUAL_SPOT_COLOR", "MEAN_INTENSITY_CH1", "MEDIAN_INTENSITY_CH1",
										"MIN_INTENSITY_CH1", "MAX_INTENSITY_CH1", "TOTAL_INTENSITY_CH1",
										"STD_INTENSITY_CH1", "CONTRAST_CH1", "SNR_CH1" };

								String[][] rowDataSpot = new String[indexes.size()][columnNamesSpot.length];

								for (int r = 0; r < indexes.size(); r++)
									for (int c = 0; c < spotJTable.getModel().getColumnCount(); c++)
										rowDataSpot[r][c] = String.valueOf(
												spotJTable.getModel().getValueAt(indexes.get(r).intValue(), c));
								tableSpot = new JTable(rowDataSpot, columnNamesSpot);
								exportToCSV(tableSpot, new File(directImages.getAbsolutePath() + File.separator
										+ imps.getShortTitle() + "_" + "Spots in tracks statistics" + ".csv"));
							}
						}
						///////

						if (enableLinkTable.equals("linkTable") == true) {
							// DisplaySettings ds = DisplaySettingsIO.readUserDefault();
							TablePanel<DefaultWeightedEdge> edgeTable = createEdgeTable(model, ds);
							linkJTable = edgeTable.getTable();

							try {
								edgeTable.exportToCsv(new File(directImages.getAbsolutePath() + File.separator
										+ imps.getShortTitle() + "_" + "Links in tracks statistics" + ".csv"));
							} catch (IOException e) {

								e.printStackTrace();
							}

						}
						if (enableTrackTable.equals("trackTable") == true)

						{
							Thread tracksThread = new Thread(new Runnable() {
								public void run() {
									if (checkboxSubBg.isSelected() == true) {
										TablePanel<Integer> trackTable = createTrackTable(model, ds);
										trackJTable = trackTable.getTable();
										if (checkTracks.isSelected() == Boolean.TRUE)
											columnNamesTrack = new String[] { "LABEL", "TRACK_INDEX", "TRACK_ID",
													"NUMBER_SPOTS", "NUMBER_GAPS", "NUMBER_SPLITS", "NUMBER_MERGES",
													"NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION", "TRACK_START",
													"TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION",
													"TRACK_Y_LOCATION", "TRACK_Z_LOCATION", "TRACK_MEAN_SPEED",
													"TRACK_MAX_SPEED", "TRACK_MIN_SPEED", "TRACK_MEDIAN_SPEED",
													"TRACK_STD_SPEED", "TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED",
													"MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO",
													"MEAN_STRAIGHT_LINE_SPEED", "LINEARITY_OF_FORWARD_PROGRESSION",
													"MEAN_DIRECTIONAL_CHANGE_RATE", "MSD Coef.",
													"Intensity-Bg Subtract",
													"Intensity-Bg Subtract" + " (" + minTracksJTF + "-" + maxTracksJTF
															+ ")",
													"D1-4 Coef.", "Track Length", "Motility", "Alpha", "Movement",
													"sMSS", "sMSS Movement" };

										if (checkTracks.isSelected() == Boolean.FALSE)
											columnNamesTrack = new String[] { "LABEL", "TRACK_INDEX", "TRACK_ID",
													"NUMBER_SPOTS", "NUMBER_GAPS", "NUMBER_SPLITS", "NUMBER_MERGES",
													"NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION", "TRACK_START",
													"TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION",
													"TRACK_Y_LOCATION", "TRACK_Z_LOCATION", "TRACK_MEAN_SPEED",
													"TRACK_MAX_SPEED", "TRACK_MIN_SPEED", "TRACK_MEDIAN_SPEED",
													"TRACK_STD_SPEED", "TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED",
													"MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO",
													"MEAN_STRAIGHT_LINE_SPEED", "LINEARITY_OF_FORWARD_PROGRESSION",
													"MEAN_DIRECTIONAL_CHANGE_RATE", "MSD Coef.",
													"Intensity-Bg Subtract", "D1-4 Coef.", "Track Length", "Motility",
													"Alpha", "Movement", "sMSS", "sMSS Movement" };
										String[][] rowDataTrack = new String[trackJTable.getModel()
												.getRowCount()][columnNamesTrack.length];
										for (int r = 0; r < trackJTable.getModel().getRowCount(); r++) {
											rowDataTrack[r][columnNamesTrack.length - 1] = "";
											for (int c = 0; c < trackJTable.getModel().getColumnCount(); c++)
												rowDataTrack[r][c] = String
														.valueOf(trackJTable.getModel().getValueAt(r, c));
										}

										List<Integer> nOfTracks = new ArrayList<Integer>();
										for (int t = 0; t < trackJTable.getRowCount(); t++) {
											nOfTracks.add(Integer.valueOf(trackJTable.getValueAt(t, 2).toString()));
											// IJ.log(Integer.valueOf(trackJTable.getValueAt(t, 2).toString()) + "---");
										}
										List<Double> allTracks = new ArrayList<Double>();
										List<Double> allTracksDef = new ArrayList<Double>();
										for (int r = 0; r < nOfTracks.size(); r++) {
											int counter = 0;
											List<Double> perTrack = new ArrayList<Double>();
											List<Double> perTrackDef = new ArrayList<Double>();
											for (int t = 0; t < tableSpot.getRowCount(); t++) {
												if (Integer.valueOf(tableSpot.getModel().getValueAt(t, 2).toString())
														.equals(nOfTracks.get(r)) == Boolean.TRUE) {

													perTrack.add(Double.valueOf(tableSpot.getModel()
															.getValueAt(t, tableSpot.getColumnCount() - 1).toString()));
												}

											}

											if (checkTracks.isSelected() == Boolean.TRUE) {

												for (int j = ((int) minTracksJTF); j < ((int) maxTracksJTF); j++) {

													perTrackDef.add(perTrack.get(j));

												}
											}
											if (checkTracks.isSelected() == Boolean.TRUE)
												if (perTrackDef.size() != 0)
													allTracksDef.add(Double.valueOf(perTrackDef.stream()
															.mapToDouble(a -> a).average().getAsDouble()));

											if (perTrack.size() != 0)
												allTracks.add(Double.valueOf(
														perTrack.stream().mapToDouble(a -> a).average().getAsDouble()));
										}
										List<Double> avg = new ComputeMSD().Compute();
										if (checkTracks.isSelected() == Boolean.FALSE) {
											for (int r = 0; r < trackJTable.getModel().getRowCount(); r++) {
												rowDataTrack[r][columnNamesTrack.length - 9] = String
														.valueOf(avg.get(r).toString());
												rowDataTrack[r][columnNamesTrack.length - 8] = String
														.valueOf(allTracks.get(r).toString());
												rowDataTrack[r][columnNamesTrack.length - 7] = String
														.valueOf(ComputeMSD.avgScaledInstantDif.get(r).toString());

												if (Double.valueOf(trackJTable.getModel().getValueAt(r, 3)
														.toString()) >= Double.valueOf((double) thLengthJTF)) {
													rowDataTrack[r][columnNamesTrack.length - 6] = String
															.valueOf("Long");
												} else {
													rowDataTrack[r][columnNamesTrack.length - 6] = String
															.valueOf("Short");
												}
												if (thD14.getText() != null || thD14.getText() != "Diff") {
													if (Double.valueOf(ComputeMSD.avgScaledInstantDif.get(r)
															.toString()) <= thD14JTF)
														rowDataTrack[r][columnNamesTrack.length - 5] = String
																.valueOf("Immobile");

													if (Double.valueOf(ComputeMSD.avgScaledInstantDif.get(r)
															.toString()) > thD14JTF)
														rowDataTrack[r][columnNamesTrack.length - 5] = String
																.valueOf("Mobile");
												}
												rowDataTrack[r][columnNamesTrack.length - 3] = String
														.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString());

												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 0.6
														&& Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r)
																.toString()) >= 0)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Confined");
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 0.9
														&& Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r)
																.toString()) >= 0.6)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Anomalous");
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 1.1
														&& Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r)
																.toString()) >= 0.9)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Free");
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) >= 1.1)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Directed");
												if (Double.valueOf(trackJTable.getModel().getValueAt(r, 3)
														.toString()) >= Double.valueOf((double) thLengthJTF)) {
													rowDataTrack[r][columnNamesTrack.length - 2] = String
															.valueOf(ComputeMSD.mssValues.get(r).toString());
													if (ComputeMSD.mssValues.get(r) == 1.0)
														rowDataTrack[r][columnNamesTrack.length
																- 1] = "Unidirectional Ballistic";
													if (ComputeMSD.mssValues.get(r) == 0)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Immobility";
													if (ComputeMSD.mssValues.get(r) >= 0.45
															&& ComputeMSD.mssValues.get(r) <= 0.55)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Free";
													if (ComputeMSD.mssValues.get(r) > 0
															&& ComputeMSD.mssValues.get(r) < 0.45)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Confined";
													if (ComputeMSD.mssValues.get(r) > 0.55)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Directed";

												} else {
													rowDataTrack[r][columnNamesTrack.length - 2] = "";

													rowDataTrack[r][columnNamesTrack.length - 1] = "";

												}
											}

										}
										if (checkTracks.isSelected() == Boolean.TRUE) {
											for (int r = 0; r < trackJTable.getModel().getRowCount(); r++) {
												rowDataTrack[r][columnNamesTrack.length - 10] = String
														.valueOf(avg.get(r).toString());
												rowDataTrack[r][columnNamesTrack.length - 9] = String
														.valueOf(allTracks.get(r).toString());
												rowDataTrack[r][columnNamesTrack.length - 8] = String
														.valueOf(allTracksDef.get(r).toString());
												rowDataTrack[r][columnNamesTrack.length - 7] = String
														.valueOf(ComputeMSD.avgScaledInstantDif.get(r).toString());
												if (Double.valueOf(trackJTable.getModel().getValueAt(r, 3)
														.toString()) >= Double.valueOf((double) thLengthJTF)) {
													rowDataTrack[r][columnNamesTrack.length - 6] = String
															.valueOf("Long");
												} else {
													rowDataTrack[r][columnNamesTrack.length - 6] = String
															.valueOf("Short");
												}
												if (Double.valueOf(
														ComputeMSD.avgScaledInstantDif.get(r).toString()) <= thD14JTF)
													rowDataTrack[r][columnNamesTrack.length - 5] = String
															.valueOf("Immobile");

												if (Double.valueOf(
														ComputeMSD.avgScaledInstantDif.get(r).toString()) > thD14JTF)
													rowDataTrack[r][columnNamesTrack.length - 5] = String
															.valueOf("Mobile");
												rowDataTrack[r][columnNamesTrack.length - 4] = String
														.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString());
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 0.6
														&& Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r)
																.toString()) >= 0)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Confined");
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 0.9
														&& Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r)
																.toString()) >= 0.6)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Anomalous");
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 1.1
														&& Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r)
																.toString()) >= 0.9)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Free");
												if (Double.valueOf(
														ComputeMSD.alphaToClassifyScaled.get(r).toString()) >= 1.1)
													rowDataTrack[r][columnNamesTrack.length - 3] = String
															.valueOf("Directed");

												if (Double.valueOf(trackJTable.getModel().getValueAt(r, 3)
														.toString()) >= Double.valueOf((double) thLengthJTF)) {
													rowDataTrack[r][columnNamesTrack.length - 2] = String
															.valueOf(ComputeMSD.mssValues.get(r).toString());
													if (ComputeMSD.mssValues.get(r) == 1.0)
														rowDataTrack[r][columnNamesTrack.length
																- 1] = "Unidirectional Ballistic";
													if (ComputeMSD.mssValues.get(r) == 0)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Immobility";
													if (ComputeMSD.mssValues.get(r) >= 0.45
															&& ComputeMSD.mssValues.get(r) <= 0.55)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Free";
													if (ComputeMSD.mssValues.get(r) > 0
															&& ComputeMSD.mssValues.get(r) < 0.45)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Confined";
													if (ComputeMSD.mssValues.get(r) > 0.55)
														rowDataTrack[r][columnNamesTrack.length - 1] = "Directed";

												} else {
													rowDataTrack[r][columnNamesTrack.length - 2] = "";

													rowDataTrack[r][columnNamesTrack.length - 1] = "";

												}

											}
										}

										tableTrack = new JTable(rowDataTrack, columnNamesTrack);
										JFrame f = new JFrame();
										f.add(new JScrollPane(tableTrack));

										f.setSize(300, 400);
										f.setVisible(true);
										exportToCSV(tableTrack, new File(directImages.getAbsolutePath() + File.separator
												+ imps.getShortTitle() + "_" + "Tracks statistics" + ".csv"));

									}
									if (checkboxSubBg.isSelected() == false) {
										// DisplaySettings ds = DisplaySettingsIO.readUserDefault();
										TablePanel<Integer> trackTable = createTrackTable(model, ds);

										List<Double> avg = new ComputeMSD().Compute();
										JTable trackJTable = trackTable.getTable();
										columnNamesTrack = new String[] { "LABEL", "TRACK_INDEX", "TRACK_ID",
												"NUMBER_SPOTS", "NUMBER_GAPS", "NUMBER_SPLITS", "NUMBER_MERGES",
												"NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION", "TRACK_START",
												"TRACK_STOP", "TRACK_DISPLACEMENT", "TRACK_X_LOCATION",
												"TRACK_Y_LOCATION", "TRACK_Z_LOCATION", "TRACK_MEAN_SPEED",
												"TRACK_MAX_SPEED", "TRACK_MIN_SPEED", "TRACK_MEDIAN_SPEED",
												"TRACK_STD_SPEED", "TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED",
												"MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO", "MEAN_STRAIGHT_LINE_SPEED",
												"LINEARITY_OF_FORWARD_PROGRESSION", "MEAN_DIRECTIONAL_CHANGE_RATE",
												"MSD Coef.", "D1-4 Coef.", "Track Length", "Motility", "Alpha",
												"Movement", "sMSS", "sMSS Movement" };
										String[][] rowDataTrack = new String[trackJTable.getModel()
												.getRowCount()][columnNamesTrack.length];

										for (int r = 0; r < trackJTable.getModel().getRowCount(); r++)
											for (int c = 0; c < trackJTable.getModel().getColumnCount(); c++)
												rowDataTrack[r][c] = String
														.valueOf(trackJTable.getModel().getValueAt(r, c));
										for (int r = 0; r < trackJTable.getModel().getRowCount(); r++) {
											rowDataTrack[r][columnNamesTrack.length - 8] = String
													.valueOf(avg.get(r).toString());
											rowDataTrack[r][columnNamesTrack.length - 7] = String
													.valueOf(ComputeMSD.avgScaledInstantDif.get(r).toString());
											if (Double.valueOf(
													trackJTable.getModel().getValueAt(r, 3).toString()) >= 50) {
												rowDataTrack[r][columnNamesTrack.length - 6] = String.valueOf("Long");
											} else {
												rowDataTrack[r][columnNamesTrack.length - 6] = String.valueOf("Short");
											}

											if (Double.valueOf(
													ComputeMSD.avgScaledInstantDif.get(r).toString()) <= thD14JTF)
												rowDataTrack[r][columnNamesTrack.length - 5] = String
														.valueOf("Immobile");

											if (Double.valueOf(
													ComputeMSD.avgScaledInstantDif.get(r).toString()) > thD14JTF)
												rowDataTrack[r][columnNamesTrack.length - 5] = String.valueOf("Mobile");
											rowDataTrack[r][columnNamesTrack.length - 4] = String
													.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString());
											if (Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 0.6
													&& Double.valueOf(
															ComputeMSD.alphaToClassifyScaled.get(r).toString()) >= 0)
												rowDataTrack[r][columnNamesTrack.length - 3] = String
														.valueOf("Confined");
											if (Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 0.9
													&& Double.valueOf(
															ComputeMSD.alphaToClassifyScaled.get(r).toString()) >= 0.6)
												rowDataTrack[r][columnNamesTrack.length - 3] = String
														.valueOf("Anomalous");
											if (Double.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString()) < 1.1
													&& Double.valueOf(
															ComputeMSD.alphaToClassifyScaled.get(r).toString()) >= 0.9)
												rowDataTrack[r][columnNamesTrack.length - 3] = String.valueOf("Free");
											if (Double
													.valueOf(ComputeMSD.alphaToClassifyScaled.get(r).toString()) >= 1.1)
												rowDataTrack[r][columnNamesTrack.length - 3] = String
														.valueOf("Directed");

											if (Double.valueOf(trackJTable.getModel().getValueAt(r, 3)
													.toString()) >= Double.valueOf((double) thLengthJTF)) {
												rowDataTrack[r][columnNamesTrack.length - 2] = String
														.valueOf(ComputeMSD.mssValues.get(r).toString());
												if (ComputeMSD.mssValues.get(r) == 1.0)
													rowDataTrack[r][columnNamesTrack.length
															- 1] = "Unidirectional Ballistic";
												if (ComputeMSD.mssValues.get(r) == 0)
													rowDataTrack[r][columnNamesTrack.length - 1] = "Immobility";
												if (ComputeMSD.mssValues.get(r) >= 0.45
														&& ComputeMSD.mssValues.get(r) <= 0.55)
													rowDataTrack[r][columnNamesTrack.length - 1] = "Free";
												if (ComputeMSD.mssValues.get(r) > 0
														&& ComputeMSD.mssValues.get(r) < 0.45)
													rowDataTrack[r][columnNamesTrack.length - 1] = "Confined";
												if (ComputeMSD.mssValues.get(r) > 0.55)
													rowDataTrack[r][columnNamesTrack.length - 1] = "Directed";

											} else {
												rowDataTrack[r][columnNamesTrack.length - 2] = "";

												rowDataTrack[r][columnNamesTrack.length - 1] = "";

											}

										}
										tableTrack = new JTable(rowDataTrack, columnNamesTrack);
										exportToCSV(tableTrack, new File(directImages.getAbsolutePath() + File.separator
												+ imps.getShortTitle() + "_" + "Tracks statistics" + ".csv"));

									}
									columnsMovements = new String[] { "Total Tracks.", "Long Tracks", "Long Confined",
											"Long Anomalous", "Long Free.", "Long Direct.", "Immob" };
									String[][] rowData = new String[4][columnsMovements.length];
									int totalTracks = 0;
									int longTracks = 0;
									int longConfined = 0;
									int longAnom = 0;
									int longFree = 0;
									int longDirect = 0;
									int immob = 0;
									int shortTracks = 0;
									int shortConfined = 0;
									int shortAnom = 0;
									int shortFree = 0;
									int shortDirect = 0;

									for (int r = 0; r < tableTrack.getModel().getRowCount(); r++) {

										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 6))
												.equals("Long") == true)
											longTracks++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 6))
												.equals("Short") == true)
											shortTracks++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 1))
												.equals("Confined") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Long") == true)
											longConfined++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 3))
												.equals("Confined") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Short") == true)
											shortConfined++;
//										if (String
//												.valueOf(tableTrack.getModel().getValueAt(r,
//														tableTrack.getModel().getColumnCount() - 3))
//												.equals("Anomalous") == true
//												&& tableTrack.getModel()
//														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
//														.equals("Long") == true)
//											longAnom++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 3))
												.equals("Anomalous") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Short") == true)
											shortAnom++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 1))
												.equals("Free") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Long") == true)
											longFree++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 3))
												.equals("Free") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Short") == true)
											shortFree++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 1))
												.equals("Directed") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Long") == true)
											longDirect++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 3))
												.equals("Directed") == true
												&& tableTrack.getModel()
														.getValueAt(r, tableTrack.getModel().getColumnCount() - 6)
														.equals("Short") == true)
											shortDirect++;
										if (String
												.valueOf(tableTrack.getModel().getValueAt(r,
														tableTrack.getModel().getColumnCount() - 5))
												.equals("Immobile") == true)
											immob++;

									}

									totalTracksDef += tableTrack.getModel().getRowCount();
									longTracksDef += longTracks;
									longConfinedDef += longConfined;
									longAnomDef += longAnom;
									longFreeDef += longFree;
									longDirectDef += longDirect;
									shortTracksDef += shortTracks;
									shortConfinedDef += shortConfined;
									shortAnomDef += shortAnom;
									shortFreeDef += shortFree;
									longDirectDef += shortDirect;
									immobDef += immob;

									rowData[0][0] = String.valueOf(tableTrack.getModel().getRowCount());
									rowData[1][0] = String.valueOf("");
									rowData[2][0] = String.valueOf("");
									rowData[3][0] = String.valueOf("");
									rowData[0][1] = String.valueOf(longTracks);
									rowData[1][1] = String.valueOf(" ");
									rowData[2][1] = String.valueOf("Short Tracks");
									rowData[3][1] = String.valueOf(shortTracks);
									rowData[0][2] = String.valueOf(longConfined);
									rowData[1][2] = String.valueOf(" ");
									rowData[2][2] = String.valueOf("Short Confined");
									rowData[3][2] = String.valueOf(shortConfined);
									rowData[0][3] = String.valueOf(longAnom);
									rowData[1][3] = String.valueOf(" ");
									rowData[2][3] = String.valueOf("Short Anomalous");
									rowData[3][3] = String.valueOf(shortAnom);
									rowData[0][4] = String.valueOf(longFree);
									rowData[1][4] = String.valueOf(" ");
									rowData[2][4] = String.valueOf("Short Free");
									rowData[3][4] = String.valueOf(shortFree);
									rowData[0][5] = String.valueOf(longDirect);
									rowData[1][5] = String.valueOf(" ");
									rowData[2][5] = String.valueOf("Short Direct");
									rowData[3][5] = String.valueOf(shortDirect);
									rowData[0][6] = String.valueOf(immob);
									rowData[1][6] = String.valueOf("");
									rowData[2][6] = String.valueOf("");
									rowData[3][6] = String.valueOf("");

									JTable tableTrackSum = new JTable(rowData, columnsMovements);
									exportToCSV(tableTrackSum, new File(directImages.getAbsolutePath() + File.separator
											+ imps.getShortTitle() + "_" + "SummaryTracks" + ".csv"));

									///////
									if (checkPBS.isSelected() == true)
										new PhotobleachingSpotPlot().Compute();
									if (checkSummary.isSelected() == true) {

										if (summaryColsWindow.combo.getSelectedIndex() == 0) {
											selectedItems = new ArrayList<String>();
											for (int i = 0; i < summaryColsWindow.itemsSpots.length; i++)
												if (summaryColsWindow.itemsSpots[i].isSelected() == true)
													selectedItems.add(summaryColsWindow.itemsSpots[i].text);
											List<List<String>> dataAllItems = new ArrayList<List<String>>();
											for (int i = 0; i < selectedItems.size(); i++) {
												List<String> dataPerImage = new ArrayList<String>();
												if (Arrays.asList(columnNamesSpot)
														.contains(selectedItems.get(i)) == true) {
													for (int r = 0; r < tableSpot.getModel().getRowCount(); r++) {
														dataPerImage.add(tableSpot.getModel()
																.getValueAt(r,
																		Arrays.asList(summaryColsWindow.columnNamesSpot)
																				.indexOf(selectedItems.get(i)))
																.toString());

													}
													dataAllItems.add(dataPerImage);
												}

											}
											dataAllItemsDef.add(dataAllItems);
										}

										if (summaryColsWindow.combo.getSelectedIndex() == 1) {
											selectedItems = new ArrayList<String>();
											for (int i = 0; i < summaryColsWindow.itemsLinks.length; i++)
												if (summaryColsWindow.itemsLinks[i].isSelected() == true)
													selectedItems.add(summaryColsWindow.itemsLinks[i].text);
											List<List<String>> dataAllItems = new ArrayList<List<String>>();
											for (int i = 0; i < selectedItems.size(); i++) {
												List<String> dataPerImage = new ArrayList<String>();
												if (Arrays.asList(summaryColsWindow.columnNamesLinks)
														.contains(selectedItems.get(i)) == true) {
													for (int r = 0; r < linkJTable.getModel().getRowCount(); r++) {
														dataPerImage.add(linkJTable.getModel().getValueAt(r,
																Arrays.asList(summaryColsWindow.columnNamesLinks)
																		.indexOf(selectedItems.get(i)))
																.toString());

													}
													dataAllItems.add(dataPerImage);
												}

											}
											dataAllItemsDef.add(dataAllItems);
										}

										if (summaryColsWindow.combo.getSelectedIndex() == 2) {
											selectedItems = new ArrayList<String>();
											for (int i = 0; i < summaryColsWindow.itemsTracks.length; i++)
												if (summaryColsWindow.itemsTracks[i].isSelected() == true)
													selectedItems.add(summaryColsWindow.itemsTracks[i].text);

											List<List<String>> dataAllItems = new ArrayList<List<String>>();
											for (int i = 0; i < selectedItems.size(); i++) {
												List<String> dataPerImage = new ArrayList<String>();

												for (int r = 0; r < tableTrack.getModel().getRowCount(); r++) {
													dataPerImage
															.add(tableTrack.getModel()
																	.getValueAt(r,
																			Arrays.asList(columnNamesTrack)
																					.indexOf(selectedItems.get(i)))
																	.toString());

												}
												dataAllItems.add(dataPerImage);

											}
											dataAllItemsDef.add(dataAllItems);
										}

									}
								}
							});
							tracksThread.start();
						}
						if (enableBranchTable.equals("branchTable") == true) {
							// DisplaySettings ds = DisplaySettingsIO.readUserDefault();
							TablePanel<Branch> branchTable = createBranchTable(model, selectionModel);

							try {
								branchTable.exportToCsv(new File(directImages.getAbsolutePath() + File.separator
										+ imps.getShortTitle() + "_" + "Branch analysis" + ".csv"));
							} catch (IOException e) {

								e.printStackTrace();
							}

						}

					}
					if (checkboxSP.isSelected() == Boolean.TRUE) {

						directChemo = new File(directImages.getAbsolutePath() + File.separator + "Chemotaxis_Analysis");

						if (!directChemo.exists()) {
							taskOutput.append("creating directory: " + directChemo.getName());
							boolean result = false;

							try {
								directChemo.mkdir();
								result = true;
							} catch (SecurityException se) {
								// handle it
							}
							if (result) {
								taskOutput.append("DIR created");
							}
						}

						// DisplaySettings ds = DisplaySettingsIO.readUserDefault();
						TablePanel<Spot> spotTable = createSpotTable(model, ds);
						JTable spotJTable = spotTable.getTable();
						ArrayList<Float> importedData = new ArrayList<Float>();

						Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
						Set<Spot> track = null;
						for (Integer id : trackIDs) {
//							if (checkboxSP.isSelected() == Boolean.FALSE)
//								track = model.getTrackModel().trackSpots(id);
//							if (checkboxSP.isSelected() == Boolean.TRUE
//									&& (chemoThreshold.getText() != "Frame Threshold..."
//											|| chemoThreshold.getText() != ""))
//								if (model.getTrackModel().trackSpots(id).size() > Integer
//										.valueOf(chemoThreshold.getText()))
							track = model.getTrackModel().trackSpots(id);
							ArrayList<Float> framesByTrack = new ArrayList<Float>();
							ArrayList<Float> xByTrack = new ArrayList<Float>();
							ArrayList<Float> yByTrack = new ArrayList<Float>();
							ArrayList<Float> framesByTrackSort = new ArrayList<Float>();
							ArrayList<Float> xByTrackSort = new ArrayList<Float>();
							ArrayList<Float> yByTrackSort = new ArrayList<Float>();
							ArrayList<Float> trackID = new ArrayList<Float>();
							ArrayList<Integer> indexes = new ArrayList<Integer>();
							for (Spot spot : track) {
								trackID.add((Float.valueOf(id) + Float.valueOf("1.0")));
								framesByTrack.add(Float.valueOf(spot.getFeature(Spot.FRAME).toString()));
								xByTrack.add(Float.valueOf(spot.getFeature(Spot.POSITION_X).toString()));
								yByTrack.add(Float.valueOf(spot.getFeature(Spot.POSITION_Y).toString()));
								framesByTrackSort.add(Float.valueOf(spot.getFeature(Spot.FRAME).toString()));

							}
							Collections.sort(framesByTrackSort);
							for (int z = 0; z < framesByTrackSort.size(); z++)
								indexes.add(framesByTrack.indexOf(framesByTrackSort.get(z)));
							for (int y = 0; y < indexes.size(); y++) {
								importedData.add(trackID.get(y));
								importedData.add((framesByTrackSort.get(y) + Float.valueOf("1.0")));
								importedData.add(xByTrack.get(indexes.get(y)));
								importedData.add(yByTrack.get(indexes.get(y)));

							}

						}

						Chemotaxis_ToolModified chemotool = new Chemotaxis_ToolModified(importedData);
						chemotool.run("");

					}
					if (checkboxDiff.isSelected() == Boolean.TRUE) {
						directDiff = new File(
								directImages.getAbsolutePath() + File.separator + "Trajectory_Classifier");

						if (!directDiff.exists()) {
							taskOutput.append("creating directory: " + directDiff.getName());
							boolean result = false;

							try {
								directDiff.mkdir();
								result = true;
							} catch (SecurityException se) {
								// handle it
							}
							if (result) {
								taskOutput.append("DIR created");
							}
						}

						TraJClassifierTest_ tc = new TraJClassifierTest_();
						tc.run("");

					}

					if (checkboxMSD.isSelected() == Boolean.TRUE) {
						TrackProcessorMSD_Modified msdPlot = new TrackProcessorMSD_Modified();
						msdPlot.Compute();

					}

					if (checkboxPlot.isSelected() == Boolean.TRUE) {

						// EdgeFeatureGrapherVersion grapherLink;
						// TrackFeatureGrapherVersion grapherTrack;
						List<Spot> spots1 = new ArrayList<>(trackmate.getModel().getSpots().getNSpots(true));
						Set<String> ySelectedSpotSet = new HashSet<>();
						ySelectedSpotSet.add(ySelectedSpot);
						for (Integer trackID : trackmate.getModel().getTrackModel().trackIDs(true))
							spots1.addAll(trackmate.getModel().getTrackModel().trackSpots(trackID));

						if (ESP.isSelected() == true && xSelectedSpot != null && ySelectedSpotSet != null) {
							JFreeChart chart = null;
							XYPlot plot = null;
							fiji.plugin.trackmate.Dimension xDimension = model.getFeatureModel()
									.getSpotFeatureDimensions().get(xSelectedSpot);
							Map<String, fiji.plugin.trackmate.Dimension> yDimensions = model.getFeatureModel()
									.getSpotFeatureDimensions();
							Map<String, String> featureNames = model.getFeatureModel().getSpotFeatureNames();
							// X label
							final String xAxisLabel = featureNames.get(xSelectedSpot) + " ("
									+ TMUtils.getUnitsFor(xDimension, model.getSpaceUnits(), model.getTimeUnits())
									+ ")";

							// Find how many different dimensions
							final Set<fiji.plugin.trackmate.Dimension> dimensions = getUniqueValues(ySelectedSpotSet,
									yDimensions);

							// Generate one panel per different dimension
							final ArrayList<ExportableChartPanel> chartPanels = new ArrayList<>(dimensions.size());
							for (final fiji.plugin.trackmate.Dimension dimension : dimensions) {

								// Y label
								final String yAxisLabel = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(),
										model.getTimeUnits());

								// Collect suitable feature for this dimension
								final List<String> featuresThisDimension = getCommonKeys(dimension, ySelectedSpotSet,
										yDimensions);

								// Title
								final String title = buildPlotTitle(featuresThisDimension, featureNames, xSelectedSpot);

								// Dataset.
								// DisplaySettings ds = DisplaySettingsIO.readUserDefault();

								final ModelDataset dataset = new SpotCollectionDataset(model, selectionModel, ds,
										xSelectedSpot, featuresThisDimension, spots1, true);

								final XYItemRenderer renderer = dataset.getRenderer();

								// The chart
								chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset,
										PlotOrientation.VERTICAL, true, true, false);
								chart.getTitle().setFont(Fonts.FONT);
								chart.getLegend().setItemFont(Fonts.SMALL_FONT);
								chart.setBackgroundPaint(new Color(220, 220, 220));
								chart.setBorderVisible(false);
								chart.getLegend().setBackgroundPaint(new Color(220, 220, 220));

								// The plot
								plot = chart.getXYPlot();
								plot.setRenderer(renderer);
								plot.getRangeAxis().setLabelFont(Fonts.FONT);
								plot.getRangeAxis().setTickLabelFont(Fonts.SMALL_FONT);
								plot.getDomainAxis().setLabelFont(Fonts.FONT);
								plot.getDomainAxis().setTickLabelFont(Fonts.SMALL_FONT);
								plot.setOutlineVisible(false);
								plot.setDomainCrosshairVisible(false);
								plot.setDomainGridlinesVisible(false);
								plot.setRangeCrosshairVisible(false);
								plot.setRangeGridlinesVisible(false);
								plot.setBackgroundAlpha(0f);

								// Plot range.
								((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);

								// Ticks. Fewer of them.
								plot.getRangeAxis().setTickLabelInsets(new RectangleInsets(20, 10, 20, 10));
								plot.getDomainAxis().setTickLabelInsets(new RectangleInsets(10, 20, 10, 20));

//								// The panel
//								final ExportableChartPanel chartPanel = new ExportableChartPanel(chart);
//								chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//								chartPanels.add(chartPanel);
							}
							try {
								ChartUtils.saveChartAsPNG(
										new File(directImages + File.separator + imps.getShortTitle() + "_"
												+ xSelectedSpot + "-" + ySelectedSpot + ".png"),
										plot.getChart(), 500, 270);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// return renderCharts(chartPanels);

						}

						List<DefaultWeightedEdge> edges = new ArrayList<>();
						Set<String> ySelectedLinkSet = new HashSet<>();
						ySelectedLinkSet.add(ySelectedLink);
						for (Integer trackID : trackmate.getModel().getTrackModel().trackIDs(true)) {
							edges.addAll(trackmate.getModel().getTrackModel().trackEdges(trackID));
						}

						if (ELP.isSelected() == true && xSelectedLink != null && ySelectedLinkSet != null) {

							JFreeChart chart = null;
							XYPlot plot = null;
							fiji.plugin.trackmate.Dimension xDimension = model.getFeatureModel()
									.getEdgeFeatureDimensions().get(xSelectedLink);
							Map<String, fiji.plugin.trackmate.Dimension> yDimensions = model.getFeatureModel()
									.getEdgeFeatureDimensions();
							Map<String, String> featureNames = model.getFeatureModel().getEdgeFeatureNames();
							// X label
							final String xAxisLabel = featureNames.get(xSelectedLink) + " ("
									+ TMUtils.getUnitsFor(xDimension, model.getSpaceUnits(), model.getTimeUnits())
									+ ")";

							// Find how many different dimensions
							final Set<fiji.plugin.trackmate.Dimension> dimensions = getUniqueValues(ySelectedLinkSet,
									yDimensions);

							// Generate one panel per different dimension
							final ArrayList<ExportableChartPanel> chartPanels = new ArrayList<>(dimensions.size());
							for (final fiji.plugin.trackmate.Dimension dimension : dimensions) {

								// Y label
								final String yAxisLabel = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(),
										model.getTimeUnits());

								// Collect suitable feature for this dimension
								final List<String> featuresThisDimension = getCommonKeys(dimension, ySelectedLinkSet,
										yDimensions);

								// Title
								final String title = buildPlotTitle(featuresThisDimension, featureNames, xSelectedLink);

								// Dataset.
								// DisplaySettings ds = DisplaySettingsIO.readUserDefault();

								final ModelDataset dataset = new EdgeCollectionDataset(model, selectionModel, ds,
										xSelectedLink, featuresThisDimension, edges, true);
								final XYItemRenderer renderer = dataset.getRenderer();

								// The chart
								chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset,
										PlotOrientation.VERTICAL, true, true, false);
								chart.getTitle().setFont(Fonts.FONT);
								chart.getLegend().setItemFont(Fonts.SMALL_FONT);
								chart.setBackgroundPaint(new Color(220, 220, 220));
								chart.setBorderVisible(false);
								chart.getLegend().setBackgroundPaint(new Color(220, 220, 220));

								// The plot
								plot = chart.getXYPlot();
								plot.setRenderer(renderer);
								plot.getRangeAxis().setLabelFont(Fonts.FONT);
								plot.getRangeAxis().setTickLabelFont(Fonts.SMALL_FONT);
								plot.getDomainAxis().setLabelFont(Fonts.FONT);
								plot.getDomainAxis().setTickLabelFont(Fonts.SMALL_FONT);
								plot.setOutlineVisible(false);
								plot.setDomainCrosshairVisible(false);
								plot.setDomainGridlinesVisible(false);
								plot.setRangeCrosshairVisible(false);
								plot.setRangeGridlinesVisible(false);
								plot.setBackgroundAlpha(0f);

								// Plot range.
								((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);

								// Ticks. Fewer of them.
								plot.getRangeAxis().setTickLabelInsets(new RectangleInsets(20, 10, 20, 10));
								plot.getDomainAxis().setTickLabelInsets(new RectangleInsets(10, 20, 10, 20));

//								// The panel
//								final ExportableChartPanel chartPanel = new ExportableChartPanel(chart);
//								chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//								chartPanels.add(chartPanel);
							}
							try {
								ChartUtils.saveChartAsPNG(
										new File(directImages.getAbsolutePath() + File.separator + imps.getShortTitle()
												+ "_" + xSelectedLink + "-" + ySelectedLink + ".png"),
										plot.getChart(), 500, 270);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// return renderCharts(chartPanels);

						}

						Set<String> ySelectedTrackSet = new HashSet<>();
						ySelectedTrackSet.add(ySelectedTrack);

						if (ETP.isSelected() == true && xSelectedTrack != null && ySelectedTrackSet != null) {
							tracksID = new ArrayList<>(trackmate.getModel().getTrackModel().unsortedTrackIDs(true));
							JFreeChart chart = null;
							XYPlot plot = null;
							fiji.plugin.trackmate.Dimension xDimension = model.getFeatureModel()
									.getTrackFeatureDimensions().get(xSelectedTrack);
							Map<String, fiji.plugin.trackmate.Dimension> yDimensions = model.getFeatureModel()
									.getTrackFeatureDimensions();
							Map<String, String> featureNames = model.getFeatureModel().getTrackFeatureNames();
							// X label
							final String xAxisLabel = featureNames.get(xSelectedTrack) + " ("
									+ TMUtils.getUnitsFor(xDimension, model.getSpaceUnits(), model.getTimeUnits())
									+ ")";

							// Find how many different dimensions
							final Set<fiji.plugin.trackmate.Dimension> dimensions = getUniqueValues(ySelectedTrackSet,
									yDimensions);

							// Generate one panel per different dimension
							final ArrayList<ExportableChartPanel> chartPanels = new ArrayList<>(dimensions.size());
							for (final fiji.plugin.trackmate.Dimension dimension : dimensions) {

								// Y label
								final String yAxisLabel = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(),
										model.getTimeUnits());

								// Collect suitable feature for this dimension
								final List<String> featuresThisDimension = getCommonKeys(dimension, ySelectedTrackSet,
										yDimensions);

								// Title
								final String title = buildPlotTitle(featuresThisDimension, featureNames,
										xSelectedTrack);

								// Dataset.
								// DisplaySettings ds = DisplaySettingsIO.readUserDefault();

								final ModelDataset dataset = new TrackCollectionDataset(model, selectionModel, ds,
										xSelectedTrack, featuresThisDimension, tracksID);
								final XYItemRenderer renderer = dataset.getRenderer();

								// The chart
								chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset,
										PlotOrientation.VERTICAL, true, true, false);
								chart.getTitle().setFont(Fonts.FONT);
								chart.getLegend().setItemFont(Fonts.SMALL_FONT);
								chart.setBackgroundPaint(new Color(220, 220, 220));
								chart.setBorderVisible(false);
								chart.getLegend().setBackgroundPaint(new Color(220, 220, 220));

								// The plot
								plot = chart.getXYPlot();
								plot.setRenderer(renderer);
								plot.getRangeAxis().setLabelFont(Fonts.FONT);
								plot.getRangeAxis().setTickLabelFont(Fonts.SMALL_FONT);
								plot.getDomainAxis().setLabelFont(Fonts.FONT);
								plot.getDomainAxis().setTickLabelFont(Fonts.SMALL_FONT);
								plot.setOutlineVisible(false);
								plot.setDomainCrosshairVisible(false);
								plot.setDomainGridlinesVisible(false);
								plot.setRangeCrosshairVisible(false);
								plot.setRangeGridlinesVisible(false);
								plot.setBackgroundAlpha(0f);

								// Plot range.
								((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);

								// Ticks. Fewer of them.
								plot.getRangeAxis().setTickLabelInsets(new RectangleInsets(20, 10, 20, 10));
								plot.getDomainAxis().setTickLabelInsets(new RectangleInsets(10, 20, 10, 20));

//								// The panel
//								final ExportableChartPanel chartPanel = new ExportableChartPanel(chart);
//								chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//								chartPanels.add(chartPanel);
							}
							try {
								ChartUtils.saveChartAsPNG(
										new File(directImages.getAbsolutePath() + File.separator + imps.getShortTitle()
												+ "_" + xSelectedTrack + "-" + ySelectedTrack + ".png"),
										plot.getChart(), 500, 270);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						final int maxFrame = spots.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
						final int[] nSpots = new int[maxFrame + 1];
						final double[] time = new double[maxFrame + 1];
						XYPlot plot = null;
						for (int frame = 0; frame <= maxFrame; frame++) {
							nSpots[frame] = spots.getNSpots(frame, true);
							time[frame] = frame * settings.dt;
						}
						final NSpotPerFrameDataset dataset = new NSpotPerFrameDataset(model, selectionModel, ds, time,
								nSpots);
						final String yFeature = "N spots";
						final Map<String, fiji.plugin.trackmate.Dimension> dimMap = new HashMap<>(2);
						dimMap.put(yFeature, fiji.plugin.trackmate.Dimension.NONE);
						dimMap.put(Spot.POSITION_T, fiji.plugin.trackmate.Dimension.TIME);
						final Map<String, String> nameMap = new HashMap<>(2);
						nameMap.put(yFeature, yFeature);
						nameMap.put(Spot.POSITION_T, "T");

						// X label
						final String xAxisLabel = nameMap.get(Spot.POSITION_T) + " ("
								+ TMUtils.getUnitsFor(fiji.plugin.trackmate.Dimension.TIME, model.getSpaceUnits(),
										model.getTimeUnits())
								+ ")";

						// Find how many different dimensions
						final Set<fiji.plugin.trackmate.Dimension> dimensions = getUniqueValues(
								Collections.singletonList("N spots"), dimMap);

						// Generate one panel per different dimension
						final ArrayList<ExportableChartPanel> chartPanels = new ArrayList<>(dimensions.size());
						for (final fiji.plugin.trackmate.Dimension dimension : dimensions) {

							// Y label
							final String yAxisLabel = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(),
									model.getTimeUnits());

							// Collect suitable feature for this dimension
							final List<String> featuresThisDimension = getCommonKeys(dimension,
									Collections.singletonList("N spots"), dimMap);

							// Title
							final String title = buildPlotTitle(featuresThisDimension, nameMap, Spot.POSITION_T);

							// Dataset.
							final XYItemRenderer renderer = dataset.getRenderer();

							// The chart
							final JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel,
									dataset, PlotOrientation.VERTICAL, true, true, false);
							chart.getTitle().setFont(Fonts.FONT);
							chart.getLegend().setItemFont(Fonts.SMALL_FONT);
							chart.setBackgroundPaint(new Color(220, 220, 220));
							chart.setBorderVisible(false);
							chart.getLegend().setBackgroundPaint(new Color(220, 220, 220));

							// The plot
							plot = chart.getXYPlot();
							plot.setRenderer(renderer);
							plot.getRangeAxis().setLabelFont(Fonts.FONT);
							plot.getRangeAxis().setTickLabelFont(Fonts.SMALL_FONT);
							plot.getDomainAxis().setLabelFont(Fonts.FONT);
							plot.getDomainAxis().setTickLabelFont(Fonts.SMALL_FONT);
							plot.setOutlineVisible(false);
							plot.setDomainCrosshairVisible(false);
							plot.setDomainGridlinesVisible(false);
							plot.setRangeCrosshairVisible(false);
							plot.setRangeGridlinesVisible(false);
							plot.setBackgroundAlpha(0f);

							// Plot range.
							((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);

							// Ticks. Fewer of them.
							plot.getRangeAxis().setTickLabelInsets(new RectangleInsets(20, 10, 20, 10));
							plot.getDomainAxis().setTickLabelInsets(new RectangleInsets(10, 20, 10, 20));

							// The panel
							// final ExportableChartPanel chartPanel = new ExportableChartPanel(chart);
							// .setPreferredSize(new java.awt.Dimension(500, 270));
							// chartPanels.add(chartPanel);
						}

						try {

							ChartUtils
									.saveChartAsPNG(
											new File(directImages.getAbsolutePath() + File.separator
													+ imps.getShortTitle() + "_" + "Nspotsvs.Time" + ".png"),
											plot.getChart(), 500, 270);
						} catch (IOException e) {

							e.printStackTrace();
						}

					}

					if (checkbox4.isSelected() == Boolean.TRUE) {// && optionARadioButton.isSelected() == Boolean.TRUE
						IJ.saveAs(capture, "Tiff", directImages.getAbsolutePath() + File.separator + imageTitles[i]);

					}
					if (checkbox4.isSelected() == Boolean.TRUE) {// && optionNRadioButton.isSelected() == Boolean.TRUE
						IJ.saveAs(capture, "Tiff", directImages.getAbsolutePath() + File.separator + imageTitles[i]);

					}
					if (checkbox3.isSelected() == Boolean.TRUE) {
						try {
							FileWriter writer = new FileWriter(directImages.getAbsolutePath() + File.separator + "Log"
									+ "_" + imageTitles[i].replaceAll("\\.tif+$", "") + ".txt");
							taskOutput.write(writer);
							writer.close();
						} catch (IOException e) {
						}
					}

				}
				if (enableTrackTable.equals("trackTable") == true) {
					Thread trackSummary = new Thread(new Runnable() {
						public void run() {
							String[][] rowDataDef = new String[4][columnsMovements.length];
							rowDataDef[0][0] = String.valueOf(tableTrack.getModel().getRowCount());
							rowDataDef[1][0] = String.valueOf("");
							rowDataDef[2][0] = String.valueOf("");
							rowDataDef[3][0] = String.valueOf("");
							rowDataDef[0][1] = String.valueOf(longTracksDef);
							rowDataDef[1][1] = String.valueOf(" ");
							rowDataDef[2][1] = String.valueOf("Short Tracks");
							rowDataDef[3][1] = String.valueOf(shortTracksDef);
							rowDataDef[0][2] = String.valueOf(longConfinedDef);
							rowDataDef[1][2] = String.valueOf(" ");
							rowDataDef[2][2] = String.valueOf("Short Confined");
							rowDataDef[3][2] = String.valueOf(shortConfinedDef);
							rowDataDef[0][3] = String.valueOf(longAnomDef);
							rowDataDef[1][3] = String.valueOf(" ");
							rowDataDef[2][3] = String.valueOf("Short Anomalous");
							rowDataDef[3][3] = String.valueOf(shortAnomDef);
							rowDataDef[0][4] = String.valueOf(longFreeDef);
							rowDataDef[1][4] = String.valueOf(" ");
							rowDataDef[2][4] = String.valueOf("Short Free");
							rowDataDef[3][4] = String.valueOf(shortFreeDef);
							rowDataDef[0][5] = String.valueOf(longDirectDef);
							rowDataDef[1][5] = String.valueOf(" ");
							rowDataDef[2][5] = String.valueOf("Short Direct");
							rowDataDef[3][5] = String.valueOf(shortDirectDef);
							rowDataDef[0][6] = String.valueOf(immobDef);
							rowDataDef[1][6] = String.valueOf("");
							rowDataDef[2][6] = String.valueOf("");
							rowDataDef[3][6] = String.valueOf("");

//							rowDataDef[0][0] = String.valueOf(totalTracksDef);
//							rowDataDef[1][0] = String.valueOf("");
//							rowDataDef[2][0] = String.valueOf("");
//							rowDataDef[3][0] = String.valueOf("");
//							rowDataDef[0][1] = String.valueOf(longTracksDef);
//							rowDataDef[0][2] = String.valueOf(longConfinedDef);
//							rowDataDef[0][3] = String.valueOf(longAnomDef);
//							rowDataDef[0][4] = String.valueOf(longFreeDef);
//							rowDataDef[0][5] = String.valueOf(longDirectDef);
//							rowDataDef[0][6] = String.valueOf(immobDef);
							ResultsTable rtTrackSummary = new ResultsTable();
							for (int r = 0; r < rowDataDef.length; r++)
								for (int c = 0; c < rowDataDef[r].length; c++)
									rtTrackSummary.setValue(columnsMovements[c], r, rowDataDef[r][c]);

							try {
								rtTrackSummary.saveAs(directSummary.getAbsolutePath() + File.separator
										+ "SummaryTracks_Condition" + ".csv");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					trackSummary.start();
				}
				if (checkSummary.isSelected() == true) {
					Thread trackSummaryCols = new Thread(new Runnable() {
						public void run() {
							ResultsTable rtSumCols = null;
							for (int s = 0; s < selectedItems.size(); s++) {
								rtSumCols = new ResultsTable();
								for (int x = 0; x < dataAllItemsDef.size(); x++) {

									for (int z = 0; z < dataAllItemsDef.get(x).get(s).size(); z++) {

										rtSumCols.setValue(listOfFiles[x].getName(), z,
												dataAllItemsDef.get(x).get(s).get(z));
									}
								}

								try {
									rtSumCols.saveAs(directSummary.getAbsolutePath() + File.separator
											+ selectedItems.get(s) + "_" + "SummaryCols" + ".csv");
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}
						}
					});
					trackSummaryCols.start();
				}

				if (enableSpotTable.equals("spotTable") == true) {

					try {
						rtSpot.saveAs(directSummary.getAbsolutePath() + File.separator
								+ "Spots in tracks statistics_Summary" + ".csv");
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
				if (enableLinkTable.equals("linkTable") == true) {

					try {
						rtLink.saveAs(directSummary.getAbsolutePath() + File.separator
								+ "Links in tracks statistics_Summary" + ".csv");
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
				if (enableTrackTable.equals("trackTable") == true)

				{

					try {
						rtTrack.saveAs(directSummary.getAbsolutePath() + File.separator + "Tracks statistics_Summary"
								+ ".csv");
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
				taskOutput.append("              FINISHED!!!");
				frameAnalyzer.setVisible(false);

			}

		});

		finishButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (checkTracks.isSelected() == Boolean.TRUE) {
					minTracksJTF = Integer.valueOf(minTracks.getText());
					maxTracksJTF = Integer.valueOf(maxTracks.getText());
					thLengthJTF = Integer.valueOf(thLength.getText());
					if (thD14.getText() != null || thD14.getText() != "DIff")
						thD14JTF = Double.valueOf(thD14.getText());

				}
				mainProcess.start();

			}

		});
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

	class LabelWizardPanel extends JWizardPanel {
		public LabelWizardPanel(JWizardComponents wizardComponents, String label) {
			super(wizardComponents);
			backButton = wizardComponents.getBackButton();
			backButton.setText("");
			ImageIcon iconBack = createImageIcon("img_71224.png");
			Icon iconBackCell = new ImageIcon(iconBack.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			backButton.setIcon(iconBackCell);
			backButton.setToolTipText("Click chemotool button to back the previous wizard.");

			nextButton = wizardComponents.getNextButton();
			nextButton.setText("");
			ImageIcon iconNext = createImageIcon("img_174455.png");
			Icon iconNextCell = new ImageIcon(iconNext.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			nextButton.setIcon(iconNextCell);
			nextButton.setToolTipText("Click chemotool button to go to the next wizard.");

			finishButton = wizardComponents.getFinishButton();
			finishButton.setText("");
			ImageIcon iconFinish = createImageIcon("ojala.png");
			Icon iconFinishCell = new ImageIcon(iconFinish.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			finishButton.setIcon(iconFinishCell);
			finishButton.setToolTipText("Click chemotool button to finish your settings selection.");

			JButton cancelButton = wizardComponents.getCancelButton();
			cancelButton.setText("");
			ImageIcon iconCancel = createImageIcon("delete.png");
			Icon iconCancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			cancelButton.setIcon(iconCancelCell);
			cancelButton.setToolTipText("Click chemotool button to cancel chemotool process.");

			this.setLayout(new GridBagLayout());
			this.add(new JLabel(label), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			TextField textXML = (TextField) new TextField(20);

			textXML.setText(pref1.get(TRACKMATE_TRANSF_PATH, ""));
			GridBagLayout layoutXML = (GridBagLayout) getLayout();
			GridBagConstraints constraintsXML = layoutXML.getConstraints(textXML);
			JButton buttonXML = new JButton("");
			ImageIcon iconXML = createImageIcon("browse.png");
			Icon iconXMLCell = new ImageIcon(iconXML.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			buttonXML.setIcon(iconXMLCell);
			ImageIcon iconBrowse = createImageIcon("browse.png");
			Icon iconBrowseCell = new ImageIcon(iconBrowse.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			buttonXML.setIcon(iconBrowseCell);
			DirectoryListener listenerXML = new DirectoryListener("Browse for " + label, textXML,
					JFileChooser.FILES_AND_DIRECTORIES);
			buttonXML.addActionListener(listenerXML);
			Panel panelXML = new Panel();
			panelXML.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel loadLabel = new JLabel("⊳  Load TrackMate .XML file: ");
			loadLabel.setFont(new Font("Verdana", Font.BOLD, 12));
			panelXML.add(loadLabel);
			panelXML.add(textXML);
			panelXML.add(buttonXML);
			layoutXML.setConstraints(panelXML, constraintsXML);
			TextField textImg = (TextField) new TextField(20);

			textImg.setText(pref1.get(TRACKMATE_IMAGES_PATH, ""));
			GridBagLayout layoutImg = (GridBagLayout) getLayout();
			GridBagConstraints constraintsImg = layoutImg.getConstraints(textImg);
			JButton buttonImg = new JButton("");
			ImageIcon iconIM = createImageIcon("browse.png");
			Icon iconIMCell = new ImageIcon(iconIM.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			buttonImg.setIcon(iconIMCell);
			ImageIcon iconB = createImageIcon("browse.png");
			Icon iconBCell = new ImageIcon(iconB.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			buttonImg.setIcon(iconBCell);
			DirectoryListener listenerImg = new DirectoryListener("Browse for " + label, textImg,
					JFileChooser.FILES_AND_DIRECTORIES);
			buttonImg.addActionListener(listenerImg);
			Panel panelImg = new Panel();
			panelImg.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel directLabel = new JLabel("⊳  Movie Files Directory:   ");
			directLabel.setFont(new Font("Verdana", Font.BOLD, 12));
			panelImg.add(directLabel);
			panelImg.add(textImg);
			panelImg.add(buttonImg);
			layoutImg.setConstraints(panelImg, constraintsImg);
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.add(Box.createVerticalStrut(15));
			mainPanel.add(panelXML);
			mainPanel.add(Box.createVerticalStrut(20));
			mainPanel.add(panelImg);
			mainPanel.add(Box.createVerticalStrut(15));
			mainPanel.setBorder(BorderFactory.createTitledBorder(""));
			this.add(mainPanel);
			this.setPanelTitle("");
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					pref1.put(TRACKMATE_TRANSF_PATH, textXML.getText());
					pref1.put(TRACKMATE_IMAGES_PATH, textImg.getText());
					pref1.put(TRACKMATE_MIN_SPOT, minTracks.getText());
					pref1.put(TRACKMATE_MAX_SPOT, maxTracks.getText());
					pref1.put(TRACKMATE_LENGTH_TH, thLength.getText());
					pref1.put(TRACKMATE_DIFF_TH, thD14.getText());
//					if (checkSummary.isSelected() == true)
//						pref1.put(TRACKMATE_COLUMN_PARAM, summaryColsWindow.columnParmText.getText());

					if (checkboxDiff.isSelected() == true) {
						pref1.put(TRACKMATE_MIN_TRACK, traJParametersWindow.minLengthText.getText());
						pref1.put(TRACKMATE_WINDOW, traJParametersWindow.windowText.getText());
						pref1.put(TRACKMATE_MIN_SEGMENT, traJParametersWindow.minSegText.getText());

					}

				}
			});

		}
	}

	class FirstWizardPanel extends LabelWizardPanel {
		public FirstWizardPanel(JWizardComponents wizardComponents) {
			super(wizardComponents, "");
			// setPanelTitle("Set a directory for export/import images and .xml file");
		}
	}

	class ChooserWizardPanel extends JWizardPanel {

		private ButtonGroup bg;

		// 'N' is no option selected
		// 'A', 'B' & 'F' stands for options
		public ChooserWizardPanel(JWizardComponents wizardComponents) {
			super(wizardComponents, "");
			init();
		}

		private void init() {
//			optionARadioButton = new JRadioButton();
//			optionNRadioButton = new JRadioButton();
//			ButtonGroup bg = new ButtonGroup();
//			bg.add(optionARadioButton);
//			bg.add(optionNRadioButton);

			this.setLayout(new GridBagLayout());

			CheckableItem[] items = { new CheckableItem("Spots", true), new CheckableItem("Links", true),
					new CheckableItem("Tracks", true), new CheckableItem("Branch Analysis", true) };
			// update1();
			checkbox1 = new JCheckBox("  Analysis/Statistics Results. ");
			checkbox1.setSelected(true);
			checkboxDiff = new JCheckBox("  TraJ: Trajectory Classifier. ");
			checkboxDiff.setSelected(true);
			trajButton = new JButton("Tune Parameters");
			checkboxSubBg = new JCheckBox(" Subtract Background :  ");
			checkboxSubBg.setSelected(false);
			checkPBS = new JCheckBox("Photobleaching step Analysis.");
			checkPBS.setSelected(true);
			checkSummary = new JCheckBox("");
			checkSummary.setSelected(true);
			summaryButton = new JButton("Summary Outputs");
			JPanel summPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			summPanel.add(checkSummary);
			summPanel.add(summaryButton);
			JPanel panelPBS = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelPBS.add(checkPBS);
			comboSubBg = new JComboBox();
			comboSubBg = new JComboBox();
			comboSubBg.addItem("Subtract Bg 1");
			comboSubBg.addItem("Subtract Bg 2");
			comboSubBg.addItem("Subtract Bg 3");
			comboSubBg.addItem("Subtract Bg 4");
			comboSubBg.setEnabled(false);
			checkboxMSD = new JCheckBox("  MSD-MSS plots. ");
			checkboxMSD.setSelected(true);
			checkbox2 = new JCheckBox();
			checkbox2.setText(" Tracks to .XML file. ");
			checkbox2.setSelected(true);
			checkbox3 = new JCheckBox();
			checkbox3.setText("  Log to .TXT file. ");
			checkbox3.setSelected(true);
			checkbox4 = new JCheckBox();
			checkbox4.setText("  Track-Overlays as .TIF images.");
			checkbox4.setSelected(true);
			checkboxRoi = new JCheckBox();
			checkboxRoi.setText("  Track-Rois as RoiSet.zip");
			checkboxRoi.setSelected(true);
			checkboxPlot = new JCheckBox();
			checkboxPlot.setText("  Plots as .PNG file.");
			checkboxPlot.setSelected(true);
			checkboxSP = new JCheckBox();
			checkboxSP.setText("  Chemotaxis Analysis Data.");
			checkboxSP.setSelected(true);
			chemoScaling = new JTextField("Set Axis Scaling...");
			chemoScaling.setEnabled(true);
			// chemoThreshold = new JTextField("Frame Threshold...");
			// chemoThreshold.setEnabled(true);

			comboP = new CheckedComboBox<>(new DefaultComboBoxModel<>(items));
			comboP.setOpaque(true);
			comboP.setToolTipText("Select an analysis for export csv file");
			comboP.setSelectedItem(items[0]);

			if (items[0].isSelected() == true)
				enableSpotTable = "spotTable";
			if (items[1].isSelected() == true)
				enableLinkTable = "linkTable";
			if (items[2].isSelected() == true)
				enableTrackTable = "trackTable";
			if (items[3].isSelected() == true)
				enableBranchTable = "branchTable";

			this.removeAll();
			TextField textCsv = (TextField) new TextField(20);
			textCsv.setText(pref1.get(TRACKMATE_CSV_PATH, ""));
			csvPath = textCsv.getText();

			GridBagLayout layoutCsv = (GridBagLayout) getLayout();
			GridBagConstraints constraintsCsv = layoutCsv.getConstraints(textCsv);
			buttonCsv = new JButton("");
			ImageIcon iconCsv = createImageIcon("browse.png");
			Icon iconCsvCell = new ImageIcon(iconCsv.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			buttonCsv.setIcon(iconCsvCell);
			DirectoryListener listenerCsv = new DirectoryListener("Browse for ", textCsv,
					JFileChooser.FILES_AND_DIRECTORIES);
			buttonCsv.addActionListener(listenerCsv);
			// panelOut.add(new JLabel("Output Image Files Directory: "));
			Panel panelCsv = new Panel();
			panelCsv.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			layoutCsv.setConstraints(panelCsv, constraintsCsv);
			panelCsv.add(checkbox1);
			panelCsv.add(comboP);

			JPanel panelBox = new JPanel();
			JPanel panelOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel panelOptions1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelOptions.add(Box.createHorizontalStrut(35));
			panelOptions1.add(Box.createHorizontalStrut(35));
			panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));
			// csvFileB = new JRadioButton(".CSV file", true);
			checkTracks = new JCheckBox("Spot Range in Track: ");
			checkTracks.setSelected(false);
			minTracks = new JTextField("Min", 3);
			minTracks.setText(pref1.get(TRACKMATE_MIN_SPOT, ""));
			minTracks.setEnabled(false);
			maxTracks = new JTextField("Max", 3);
			maxTracks.setText(pref1.get(TRACKMATE_MAX_SPOT, ""));
			maxTracks.setEnabled(false);
			panelOptions1.add(checkTracks);
			panelOptions1.add(minTracks);
			panelOptions1.add(new JLabel("-"));
			panelOptions1.add(maxTracks);
			checkDispSpots = new JCheckBox("Set Spots Visible ");
			checkDispSpots.setSelected(true);
			checkDispTracks = new JCheckBox("Set Tracks Visible: ");
			checkDispTracks.setSelected(true);
			comboDispTracks = new JComboBox<String>();
			comboDispTracks.addItem("FULL");
			comboDispTracks.addItem("LOCAL");
			comboDispTracks.addItem("LOCAL_BACKWARD");
			comboDispTracks.addItem("LOCAL_FORWARD");
			comboDispTracks.setSelectedIndex(0);
			JPanel panelSpotTrackDisp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelSpotTrackDisp.add(checkDispSpots);
			panelSpotTrackDisp.add(checkDispTracks);
			panelSpotTrackDisp.add(comboDispTracks);
			JLabel thLengthLabel = new JLabel("-Length Threshold: ");
			thLength = new JTextField("Length", 3);
			thLength.setText(pref1.get(TRACKMATE_LENGTH_TH, ""));
			JLabel thD14Label = new JLabel("-Diff.Threshold: ");
			thD14 = new JTextField("Diff", 3);
			thD14.setText(pref1.get(TRACKMATE_DIFF_TH, ""));
			JPanel panelLengthD14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelLengthD14.add(thLengthLabel);
			panelLengthD14.add(thLength);
			panelLengthD14.add(thD14Label);
			panelLengthD14.add(thD14);
			JPanel panelSubBg = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelSubBg.add(checkboxSubBg);
			panelSubBg.add(comboSubBg);

			panelBox.add(panelOptions);
			panelBox.add(Box.createVerticalStrut(3));
			panelBox.add(panelOptions1);
			panelBox.add(Box.createVerticalStrut(3));
			panelBox.add(panelLengthD14);
			panelBox.add(Box.createVerticalStrut(3));
			panelBox.add(panelSpotTrackDisp);
			panelBox.add(Box.createVerticalStrut(3));
			panelBox.add(panelPBS);
			panelBox.add(Box.createVerticalStrut(3));
			panelBox.add(panelSubBg);
			panelBox.add(Box.createVerticalStrut(3));
			panelBox.add(summPanel);
			Panel panelOut = new Panel();
			panelOut.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelOut.add(checkbox4);
			GridBagLayout layoutXMLL = (GridBagLayout) getLayout();
			// panelOut.add(new JLabel("Output Image Files Directory: "));
			Panel panelXMLL = new Panel();
			panelXMLL.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelXMLL.add(checkbox2);
			GridBagLayout layoutTxt = (GridBagLayout) getLayout();
			Panel panelTxt = new Panel();
			panelTxt.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelTxt.add(checkbox3);
			JPanel panelExport = new JPanel();
			JLabel labelExport = new JLabel();
			labelExport.setText("⤷ Choose a directory to export files:    ");
			labelExport.setFont(new Font("Verdana", Font.BOLD, 12));
			panelExport.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelExport.add(labelExport);
			panelExport.add(textCsv);
			panelExport.add(buttonCsv);
			JPanel panelDiff = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelDiff.add(checkboxDiff);
			panelDiff.add(trajButton);
			JPanel panelMSD = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelMSD.add(checkboxMSD);
			JPanel panelExport2 = new JPanel(new BorderLayout());
			panelExport2.add(panelExport, BorderLayout.EAST);
			Panel panelRoi = new Panel();
			panelRoi.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelRoi.add(checkboxRoi);
			Panel panelPlot = new Panel();
			panelPlot.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelPlot.add(checkboxPlot);
			Panel panelSP = new Panel();
			panelSP.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelSP.add(checkboxSP);
			panelSP.add(chemoScaling);
			// panelSP.add(chemoThreshold);
			JLabel labelExport1 = new JLabel("⊳   Tuneable Options: ");
			labelExport1.setFont(new Font("Verdana", Font.BOLD, 12));
			JPanel panelExport1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelExport1.add(labelExport1);
			JPanel mainPanel2 = new JPanel();
			mainPanel2.setBorder(BorderFactory.createTitledBorder(""));
			mainPanel2.setLayout(new BoxLayout(mainPanel2, BoxLayout.Y_AXIS));
			// mainPanel2.add(panel1);
			mainPanel2.add(Box.createVerticalStrut(3));
			mainPanel2.add(new JSeparator(SwingConstants.HORIZONTAL));
			mainPanel2.add(panelExport1);
			mainPanel2.add(new JSeparator(SwingConstants.HORIZONTAL));
			mainPanel2.add(Box.createVerticalStrut(8));
			mainPanel2.add(panelCsv);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(Box.createHorizontalStrut(15));
			mainPanel2.add(panelBox);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelDiff);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelMSD);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelXMLL);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelTxt);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelOut);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelRoi);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelPlot);
			mainPanel2.add(Box.createVerticalStrut(5));
			mainPanel2.add(panelSP);
			mainPanel2.add(Box.createVerticalStrut(15));
			mainPanel2.add(new JSeparator(SwingConstants.HORIZONTAL));
			mainPanel2.add(Box.createVerticalStrut(3));
			mainPanel2.add(panelExport2);
			mainPanel2.setBorder(BorderFactory.createTitledBorder(""));
			comboP.setEnabled(true);
			textCsv.setEnabled(true);
			buttonCsv.setEnabled(true);
			panelExport2.setEnabled(true);

			checkSummary.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						summaryButton.setEnabled(true);
					}

					if (e.getStateChange() == ItemEvent.DESELECTED) {
						summaryButton.setEnabled(false);
					}
				}
			});
			checkboxDiff.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						trajButton.setEnabled(true);
					}

					if (e.getStateChange() == ItemEvent.DESELECTED) {
						trajButton.setEnabled(false);
					}
				}
			});
			checkDispTracks.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						comboDispTracks.setEnabled(true);
					}

					if (e.getStateChange() == ItemEvent.DESELECTED) {
						comboDispTracks.setEnabled(false);
					}
				}
			});
			checkTracks.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						minTracks.setEnabled(true);
						maxTracks.setEnabled(true);
					}

					if (e.getStateChange() == ItemEvent.DESELECTED) {
						minTracks.setEnabled(false);
						maxTracks.setEnabled(false);
					}
				}
			});
			checkboxSubBg.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED)
						comboSubBg.setEnabled(true);

					if (e.getStateChange() == ItemEvent.DESELECTED)
						comboSubBg.setEnabled(false);
				}
			});
			checkbox1.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					Component[] com = panelCsv.getComponents();
					Component[] comExport = panelExport.getComponents();
					if (e.getStateChange() == ItemEvent.SELECTED) {
						for (int a = 1; a < com.length; a++)
							com[a].setEnabled(true);
						for (int a = 0; a < comExport.length; a++)
							comExport[a].setEnabled(true);

					}
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						for (int a = 1; a < com.length; a++)
							com[a].setEnabled(false);

					}
				}
			});
			checkboxSP.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						chemoScaling.setEnabled(true);
						// chemoThreshold.setEnabled(true);

					}
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						chemoScaling.setEnabled(false);
						// chemoThreshold.setEnabled(false);

					}
				}
			});
			summaryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (checkSummary.isSelected() == true) {
						new summaryColsWindow().run("");
					}

				}
			});
			trajButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (checkboxDiff.isSelected() == true) {
						new traJParametersWindow().run("");
					}

				}
			});
			finishButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					csvPath = textCsv.getText();
					pref1.put(TRACKMATE_CSV_PATH, textCsv.getText());

				}
			});

			this.add(mainPanel2);

		}

		public void update() {

			if (checkboxPlot.isSelected() == Boolean.FALSE)
				setNextButtonEnabled(false);
			if (checkboxPlot.isSelected() == Boolean.TRUE)
				setNextButtonEnabled(true);
			setFinishButtonEnabled(true);
			setBackButtonEnabled(false); // there is no way back
		}

		public void next() {
			switchPanel(DynamicJWizard.PANEL_OPTION_B);

		}

		public void back() {

			switchPanel(DynamicJWizard.PANEL_LAST);

		}
	}

	class OptionWizardPanel extends LabelWizardPanel {
		public OptionWizardPanel(JWizardComponents wizardComponents, String option) {
			super(wizardComponents, "");
			setPanelTitle("►  Check options to export Tracking results");

		}

		public void update1() {
			setNextButtonEnabled(false);
			setFinishButtonEnabled(true);
			setBackButtonEnabled(true);

		}

		public void next() {
			switchPanel(DynamicJWizard.PANEL_LAST);
		}

		public void back() {
			switchPanel(DynamicJWizard.PANEL_CHOOSER);
			if (itemCheckPlot == ItemEvent.SELECTED)
				switchPanel(DynamicJWizard.PANEL_LAST);
			if (itemPlot2 == ItemEvent.SELECTED)
				switchPanel(DynamicJWizard.PANEL_LAST);
			if (itemPlot2 == ItemEvent.SELECTED && itemCheckPlot == ItemEvent.SELECTED)
				switchPanel(DynamicJWizard.PANEL_LAST);

		}

		public void finish() {
			switchPanel(DynamicJWizard.DISPOSE_ON_CLOSE);
		}
	}

	class LastWizardPanel extends LabelWizardPanel {
		public LastWizardPanel(JWizardComponents wizardComponents) {
			super(wizardComponents, "");
			setPanelTitle("");
			update();
			this.removeAll();

			JTabbedPane tabbedPane = new JTabbedPane();
			ImageIcon iconSpot = createImageIcon(
					"5441165-point-of-light-png-104-images-in-collection-page-1-point-of-light-png-320_320_preview.png");
			Icon iconSpotCell = new ImageIcon(iconSpot.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
			ImageIcon iconLink = createImageIcon("link.png");
			Icon iconLinkCell = new ImageIcon(iconLink.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			ImageIcon iconTrack = createImageIcon("track.jpg");
			Icon iconTrackCell = new ImageIcon(iconTrack.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));

			JComponent panel1 = makeTextPanel("");
			JComponent panelX = makeTextPanel("");
			JComponent panelY = makeTextPanel("");
			panelY.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel xSpot = new JLabel();
			xSpot.setText("⊳ Spot-Feature for X axis:   ");
			xSpot.setFont(new Font("Verdana", Font.BOLD, 12));
			JLabel ySpot = new JLabel();
			ySpot.setText("⊳ Spot-Feature for Y axis:   ");
			ySpot.setFont(new Font("Verdana", Font.BOLD, 12));
			comboSpotsX = new JComboBox();
			comboSpotsY = new JComboBox();
			Object[] spotItems = null;
			// if (checkboxSubBg.isSelected() == true)
			spotItems = new Object[] { "QUALITY", "POSITION_X", "POSITION_Y", "POSITION_Z", "POSITION_T	FRAME",
					"RADIUS", "VISIBILITY", "MANUAL_SPOT_COLOR", "MEAN_INTENSITY_CH1", "MEDIAN_INTENSITY_CH1",
					"MIN_INTENSITY_CH1", "MAX_INTENSITY_CH1", "TOTAL_INTENSITY_CH1", "STD_INTENSITY_CH1",
					"CONTRAST_CH1", "SNR_CH1" };
			for (int i = 0; i < spotItems.length; i++) {
				comboSpotsX.addItem(spotItems[i]);
				comboSpotsY.addItem(spotItems[i]);
			}

			panelX.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelX.add(Box.createVerticalStrut(10));
			panelX.add(xSpot);
			panelX.add(Box.createVerticalStrut(5));
			panelX.add(comboSpotsX);
			panelY.add(ySpot);
			panelY.add(Box.createVerticalStrut(5));
			panelY.add(comboSpotsY);
			panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
			panel1.add(panelX);
			panel1.add(panelY);
			JPanel panelESP = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panelESP.setBorder(BorderFactory.createLoweredBevelBorder());
			ESP = new JCheckBox("  Export User-Defined Spot Plot.");
			panelESP.add(ESP);
			panel1.add(Box.createVerticalStrut(15));
			panel1.add(panelESP);
			panel1.setBorder(BorderFactory.createTitledBorder(""));
			tabbedPane.addTab("Spots", iconSpotCell, panel1, "Select the X-Y Spot features to plot.");
			tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

			JComponent panel2 = makeTextPanel("");
			JComponent panelLinkX = makeTextPanel("");
			JComponent panelLinkY = makeTextPanel("");
			panelLinkX.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelLinkY.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel xLink = new JLabel();
			xLink.setText("⊳ Link-Feature for X axis:   ");
			xLink.setFont(new Font("Verdana", Font.BOLD, 12));
			JLabel yLink = new JLabel();
			yLink.setText("⊳ Link-Feature for Y axis:   ");
			yLink.setFont(new Font("Verdana", Font.BOLD, 12));
			comboLinkX = new JComboBox();
			comboLinkY = new JComboBox();
			Object[] edgeItems = new Object[] { "SPOT_SOURCE_ID", "SPOT_TARGET_ID", "LINK_COST",
					"DIRECTIONAL_CHANGE_RATE", "SPEED", "DISPLACEMENT", "EDGE_TIME", "EDGE_X_LOCATION",
					"EDGE_Y_LOCATION", "EDGE_Z_LOCATION" };
			for (int i = 0; i < edgeItems.length; i++) {
				comboLinkX.addItem(edgeItems[i]);
				comboLinkY.addItem(edgeItems[i]);
			}

			panelLinkX.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelLinkX.add(Box.createVerticalStrut(10));
			panelLinkX.add(xLink);
			panelLinkX.add(Box.createVerticalStrut(5));
			panelLinkX.add(comboLinkX);
			panelLinkY.add(yLink);
			panelLinkY.add(Box.createVerticalStrut(5));
			panelLinkY.add(comboLinkY);
			panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
			panel2.add(panelLinkX);
			panel2.add(panelLinkY);
			JPanel panelELP = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panelELP.setBorder(BorderFactory.createLoweredBevelBorder());
			ELP = new JCheckBox("  Export User-Defined Links Plot.");
			panelELP.add(ELP);
			panel2.add(Box.createVerticalStrut(15));
			panel2.add(panelELP);
			panel2.setBorder(BorderFactory.createTitledBorder(""));
			tabbedPane.addTab("Links", iconLinkCell, panel2, "Select the  X-Y Link features to plot.");
			tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

			JComponent panel3 = makeTextPanel("");
			JComponent panelTrackX = makeTextPanel("");
			JComponent panelTrackY = makeTextPanel("");
			panelTrackX.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelTrackY.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel xTrack = new JLabel();
			xTrack.setText("⊳ Track-Feature for X axis:   ");
			xTrack.setFont(new Font("Verdana", Font.BOLD, 12));
			JLabel yTrack = new JLabel();
			yTrack.setText("⊳ Track-Feature for Y axis:   ");
			yTrack.setFont(new Font("Verdana", Font.BOLD, 12));
			comboTrackX = new JComboBox();
			comboTrackY = new JComboBox();
			Object[] trackItems = null;

			trackItems = new Object[] { "TRACK_INDEX", "TRACK_ID", "NUMBER_SPOTS", "NUMBER_GAPS", "NUMBER_SPLITS",
					"NUMBER_MERGES", "NUMBER_COMPLEX", "LONGEST_GAP", "TRACK_DURATION", "TRACK_START", "TRACK_STOP",
					"TRACK_DISPLACEMENT", "TRACK_X_LOCATION", "TRACK_Y_LOCATION", "TRACK_Z_LOCATION",
					"TRACK_MEAN_SPEED", "TRACK_MAX_SPEED", "TRACK_MIN_SPEED", "TRACK_MEDIAN_SPEED", "TRACK_STD_SPEED",
					"TRACK_MEAN_QUALITY", "TOTAL_DISTANCE_TRAVELED", "MAX_DISTANCE_TRAVELED", "CONFINMENT_RATIO",
					"MEAN_STRAIGHT_LINE_SPEED", "LINEARITY_OF_FORWARD_PROGRESSION", "MEAN_DIRECTIONAL_CHANGE_RATE" };
			for (int i = 0; i < trackItems.length; i++) {
				comboTrackX.addItem(trackItems[i]);
				comboTrackY.addItem(trackItems[i]);
			}

			panelTrackX.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelTrackX.add(Box.createVerticalStrut(10));
			panelTrackX.add(xTrack);
			panelTrackX.add(Box.createVerticalStrut(5));
			panelTrackX.add(comboTrackX);
			panelTrackY.add(yTrack);
			panelTrackY.add(Box.createVerticalStrut(5));
			panelTrackY.add(comboTrackY);
			panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
			panel3.add(panelTrackX);
			panel3.add(panelTrackY);
			JPanel panelETP = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panelETP.setBorder(BorderFactory.createLoweredBevelBorder());
			ETP = new JCheckBox("  Export User-Defined Tracks Plot.");
			panelETP.add(ETP);
			panel3.add(Box.createVerticalStrut(15));
			panel3.add(panelETP);
			panel3.setBorder(BorderFactory.createTitledBorder(""));

			tabbedPane.addTab("Tracks", iconTrackCell, panel3, "Select the  X-Y Track features to plot.");
			tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
			tabbedPane.setPreferredSize(new Dimension(450, 250));
			this.add(tabbedPane);

			// The following line enables to use scrolling tabs.
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		}

		public void update() {
			setNextButtonEnabled(false);
			if (itemCheckPlot == ItemEvent.SELECTED)
				setNextButtonEnabled(false);

			setFinishButtonEnabled(true);
			setBackButtonEnabled(true);

		}

		public void next() {

			// if (itemCheckPlot == ItemEvent.SELECTED)
			// switchPanel(DynamicJWizard.PANEL_OPTION_A);

		}

		public void back() {
			switchPanel(DynamicJWizard.PANEL_FIRST);

		}

		public void finish() {
			switchPanel(DynamicJWizard.DISPOSE_ON_CLOSE);
		}
	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	private static final class EdgeSourceSpotFrameComparator implements Comparator<DefaultWeightedEdge> {

		private final Model model;

		public EdgeSourceSpotFrameComparator(final Model model) {
			this.model = model;
		}

		@Override
		public int compare(final DefaultWeightedEdge e1, final DefaultWeightedEdge e2) {
			final double t1 = model.getTrackModel().getEdgeSource(e1).getFeature(Spot.FRAME).doubleValue();
			final double t2 = model.getTrackModel().getEdgeSource(e2).getFeature(Spot.FRAME).doubleValue();
			if (t1 < t2) {
				return -1;
			}
			if (t1 > t2) {
				return 1;
			}
			return 0;
		}

	}

	public static ImagePlus[] openBF(String multiSeriesFileName, boolean splitC, boolean splitT, boolean splitZ,
			boolean autoScale, boolean crop, boolean allSeries) {
		ImporterOptions options;
		ImagePlus[] imps = null;
		try {
			options = new ImporterOptions();
			options.setId(multiSeriesFileName);
			options.setSplitChannels(splitC);
			options.setSplitTimepoints(splitT);
			options.setSplitFocalPlanes(splitZ);
			options.setAutoscale(autoScale);
			options.setStackFormat(ImporterOptions.VIEW_HYPERSTACK);
			options.setStackOrder(ImporterOptions.ORDER_XYCZT);
			options.setCrop(crop);
			options.setOpenAllSeries(allSeries);

			ImportProcess process = new ImportProcess(options);
			if (!process.execute())
				return null;
			DisplayHandler displayHandler = new DisplayHandler(process);
			if (options != null && options.isShowOMEXML()) {
				displayHandler.displayOMEXML();
			}
			List<ImagePlus> impsList = new ImagePlusReaderModified(process).readImages(false);
			imps = impsList.toArray(new ImagePlus[0]);
			if (options != null && options.showROIs()) {
				displayHandler.displayROIs(imps);
			}
			if (!options.isVirtual()) {
				process.getReader().close();
			}

		} catch (Exception e) {

			return null;
		}
		return imps;
	}

	private final TablePanel<Spot> createSpotTable(final Model model, final DisplaySettings ds) {
		final List<Spot> objects = new ArrayList<>();
		for (final Integer trackID : model.getTrackModel().unsortedTrackIDs(true))
			objects.addAll(model.getTrackModel().trackSpots(trackID));
		final List<String> features = new ArrayList<>(model.getFeatureModel().getSpotFeatures());
		final Map<String, String> featureNames = model.getFeatureModel().getSpotFeatureNames();
		final Map<String, String> featureShortNames = model.getFeatureModel().getSpotFeatureShortNames();
		final Map<String, String> featureUnits = new HashMap<>();
		for (final String feature : features) {
			final fiji.plugin.trackmate.Dimension dimension = model.getFeatureModel().getSpotFeatureDimensions()
					.get(feature);
			final String units = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(), model.getTimeUnits());
			featureUnits.put(feature, units);
		}
		final Map<String, Boolean> isInts = model.getFeatureModel().getSpotFeatureIsInt();
		final Map<String, String> infoTexts = new HashMap<>();
		final Function<Spot, String> labelGenerator = spot -> spot.getName();
		final BiConsumer<Spot, String> labelSetter = (spot, label) -> spot.setName(label);

		/*
		 * Feature provider. We add a fake one to show the spot ID.
		 */
		final String SPOT_ID = "ID";
		features.add(0, SPOT_ID);
		featureNames.put(SPOT_ID, "Spot ID");
		featureShortNames.put(SPOT_ID, "Spot ID");
		featureUnits.put(SPOT_ID, "");
		isInts.put(SPOT_ID, Boolean.TRUE);
		infoTexts.put(SPOT_ID, "The id of the spot.");

		/*
		 * Feature provider. We add a fake one to show the spot *track* ID.
		 */
		final String TRACK_ID = "TRACK_ID";
		features.add(1, TRACK_ID);
		featureNames.put(TRACK_ID, "Track ID");
		featureShortNames.put(TRACK_ID, "Track ID");
		featureUnits.put(TRACK_ID, "");
		isInts.put(TRACK_ID, Boolean.TRUE);
		infoTexts.put(TRACK_ID, "The id of the track this spot belongs to.");

		final BiFunction<Spot, String, Double> featureFun = (spot, feature) -> {
			if (feature.equals(TRACK_ID)) {
				final Integer trackID = model.getTrackModel().trackIDOf(spot);
				return trackID == null ? null : trackID.doubleValue();
			} else if (feature.equals(SPOT_ID))
				return (double) spot.ID();

			return spot.getFeature(feature);
		};

		final BiConsumer<Spot, Color> colorSetter = (spot, color) -> spot
				.putFeature(ManualSpotColorAnalyzerFactory.FEATURE, Double.valueOf(color.getRGB()));

		final Supplier<FeatureColorGenerator<Spot>> coloring = () -> FeatureUtils.createSpotColorGenerator(model, ds);

		final TablePanel<Spot> table = new TablePanel<>(objects, features, featureFun, featureNames, featureShortNames,
				featureUnits, isInts, infoTexts, coloring, labelGenerator, labelSetter,
				ManualSpotColorAnalyzerFactory.FEATURE, colorSetter);

		return table;
	}

	private final TablePanel<DefaultWeightedEdge> createEdgeTable(final Model model, final DisplaySettings ds) {
		final List<DefaultWeightedEdge> objects = new ArrayList<>();
		for (final Integer trackID : model.getTrackModel().unsortedTrackIDs(true))
			objects.addAll(model.getTrackModel().trackEdges(trackID));
		final List<String> features = new ArrayList<>(model.getFeatureModel().getEdgeFeatures());
		final Map<String, String> featureNames = model.getFeatureModel().getEdgeFeatureNames();
		final Map<String, String> featureShortNames = model.getFeatureModel().getEdgeFeatureShortNames();
		final Map<String, String> featureUnits = new HashMap<>();
		for (final String feature : features) {
			final fiji.plugin.trackmate.Dimension dimension = model.getFeatureModel().getEdgeFeatureDimensions()
					.get(feature);
			final String units = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(), model.getTimeUnits());
			featureUnits.put(feature, units);
		}
		final Map<String, Boolean> isInts = model.getFeatureModel().getEdgeFeatureIsInt();
		final Map<String, String> infoTexts = new HashMap<>();
		final Function<DefaultWeightedEdge, String> labelGenerator = edge -> String.format("%s → %s",
				model.getTrackModel().getEdgeSource(edge).getName(),
				model.getTrackModel().getEdgeTarget(edge).getName());
		final BiConsumer<DefaultWeightedEdge, String> labelSetter = null;

		/*
		 * Feature provider. We add a fake one to show the spot track ID.
		 */
		final String TRACK_ID = "TRACK_ID";
		features.add(0, TRACK_ID);
		featureNames.put(TRACK_ID, "Track ID");
		featureShortNames.put(TRACK_ID, "Track ID");
		featureUnits.put(TRACK_ID, "");
		isInts.put(TRACK_ID, Boolean.TRUE);
		infoTexts.put(TRACK_ID, "The id of the track this spot belongs to.");

		final BiFunction<DefaultWeightedEdge, String, Double> featureFun = (edge, feature) -> {
			if (feature.equals(TRACK_ID)) {
				final Integer trackID = model.getTrackModel().trackIDOf(edge);
				return trackID == null ? null : trackID.doubleValue();
			}
			return model.getFeatureModel().getEdgeFeature(edge, feature);
		};

		final BiConsumer<DefaultWeightedEdge, Color> colorSetter = (edge, color) -> model.getFeatureModel()
				.putEdgeFeature(edge, ManualEdgeColorAnalyzer.FEATURE, Double.valueOf(color.getRGB()));

		final Supplier<FeatureColorGenerator<DefaultWeightedEdge>> coloring = () -> FeatureUtils
				.createTrackColorGenerator(model, ds);

		final TablePanel<DefaultWeightedEdge> table = new TablePanel<>(objects, features, featureFun, featureNames,
				featureShortNames, featureUnits, isInts, infoTexts, coloring, labelGenerator, labelSetter,
				ManualEdgeColorAnalyzer.FEATURE, colorSetter);

		return table;
	}

	private final TablePanel<Integer> createTrackTable(final Model model, final DisplaySettings ds) {
		final List<Integer> objects = new ArrayList<>(model.getTrackModel().trackIDs(true));
		final List<String> features = new ArrayList<>(model.getFeatureModel().getTrackFeatures());
		final BiFunction<Integer, String, Double> featureFun = (trackID, feature) -> model.getFeatureModel()
				.getTrackFeature(trackID, feature);
		final Map<String, String> featureNames = model.getFeatureModel().getTrackFeatureNames();
		final Map<String, String> featureShortNames = model.getFeatureModel().getTrackFeatureShortNames();
		final Map<String, String> featureUnits = new HashMap<>();
		for (final String feature : features) {
			final fiji.plugin.trackmate.Dimension dimension = model.getFeatureModel().getTrackFeatureDimensions()
					.get(feature);
			final String units = TMUtils.getUnitsFor(dimension, model.getSpaceUnits(), model.getTimeUnits());
			featureUnits.put(feature, units);
		}
		final Map<String, Boolean> isInts = model.getFeatureModel().getTrackFeatureIsInt();
		final Map<String, String> infoTexts = new HashMap<>();
		final Function<Integer, String> labelGenerator = id -> model.getTrackModel().name(id);
		final BiConsumer<Integer, String> labelSetter = (id, label) -> model.getTrackModel().setName(id, label);

		final Supplier<FeatureColorGenerator<Integer>> coloring = () -> FeatureUtils
				.createWholeTrackColorGenerator(model, ds);

		final TablePanel<Integer> table = new TablePanel<>(objects, features, featureFun, featureNames,
				featureShortNames, featureUnits, isInts, infoTexts, coloring, labelGenerator, labelSetter);

		return table;
	}

	public static final TablePanel<Branch> createBranchTable(final Model model, final SelectionModel selectionModel) {
		final Logger logger = model.getLogger();

		taskOutput.append("Generating track branches analysis.\n");
		final int ntracks = model.getTrackModel().nTracks(true);
		if (ntracks == 0)
			taskOutput.append("No visible track found. Aborting.\n");

		final TimeDirectedNeighborIndex neighborIndex = model.getTrackModel().getDirectedNeighborIndex();

		final List<Branch> brs = new ArrayList<>();
		for (final Integer trackID : model.getTrackModel().unsortedTrackIDs(true)) {
			final TrackBranchDecomposition branchDecomposition = ConvexBranchesDecomposition.processTrack(trackID,
					model.getTrackModel(), neighborIndex, true, false);
			final SimpleDirectedGraph<List<Spot>, DefaultEdge> branchGraph = ConvexBranchesDecomposition
					.buildBranchGraph(branchDecomposition);

			final Map<Branch, Set<List<Spot>>> successorMap = new HashMap<>();
			final Map<Branch, Set<List<Spot>>> predecessorMap = new HashMap<>();
			final Map<List<Spot>, Branch> branchMap = new HashMap<>();

			for (final List<Spot> branch : branchGraph.vertexSet()) {
				final Branch br = new Branch();
				branchMap.put(branch, br);

				// Track name from ID
				br.trackName = model.getTrackModel().name(trackID);
				br.putFeature(TRACK_ID, Double.valueOf(trackID));

				// First and last spot.
				final Spot first = branch.get(0);
				br.first = first;
				br.putFeature(FIRST, Double.valueOf(first.ID()));

				final Spot last = branch.get(branch.size() - 1);
				br.last = last;
				br.putFeature(LAST, Double.valueOf(last.ID()));

				// Delta T
				br.putFeature(DELTA_T, Double.valueOf(br.dt()));

				// Distance traveled.
				final double distanceTraveled = Math.sqrt(br.last.squareDistanceTo(br.first));
				br.putFeature(DISTANCE, Double.valueOf(distanceTraveled));

				// Compute mean velocity "by hand".
				final double meanV;
				if (branch.size() < 2) {
					meanV = Double.NaN;
				} else {
					final Iterator<Spot> it = branch.iterator();
					Spot previous = it.next();
					double sum = 0;
					while (it.hasNext()) {
						final Spot next = it.next();
						final double dr = Math.sqrt(next.squareDistanceTo(previous));
						sum += dr;
						previous = next;
					}
					meanV = sum / (branch.size() - 1);
				}
				br.putFeature(MEAN_VELOCITY, Double.valueOf(meanV));

				// Predecessors
				final Set<DefaultEdge> incomingEdges = branchGraph.incomingEdgesOf(branch);
				final Set<List<Spot>> predecessors = new HashSet<>(incomingEdges.size());
				for (final DefaultEdge edge : incomingEdges) {
					final List<Spot> predecessorBranch = branchGraph.getEdgeSource(edge);
					predecessors.add(predecessorBranch);
				}

				// Successors
				final Set<DefaultEdge> outgoingEdges = branchGraph.outgoingEdgesOf(branch);
				final Set<List<Spot>> successors = new HashSet<>(outgoingEdges.size());
				for (final DefaultEdge edge : outgoingEdges) {
					final List<Spot> successorBranch = branchGraph.getEdgeTarget(edge);
					successors.add(successorBranch);
				}

				successorMap.put(br, successors);
				predecessorMap.put(br, predecessors);
			}

			for (final Branch br : successorMap.keySet()) {
				final Set<List<Spot>> succs = successorMap.get(br);
				final Set<Branch> succBrs = new HashSet<>(succs.size());
				for (final List<Spot> branch : succs) {
					final Branch succBr = branchMap.get(branch);
					succBrs.add(succBr);
				}
				br.successors = succBrs;
				br.putFeature(N_SUCCESSORS, Double.valueOf(succBrs.size()));

				final Set<List<Spot>> preds = predecessorMap.get(br);
				final Set<Branch> predBrs = new HashSet<>(preds.size());
				for (final List<Spot> branch : preds) {
					final Branch predBr = branchMap.get(branch);
					predBrs.add(predBr);
				}
				br.predecessors = predBrs;
				br.putFeature(N_PREDECESSORS, Double.valueOf(predBrs.size()));
			}

			brs.addAll(successorMap.keySet());
		}
		Collections.sort(brs);

		/*
		 * Create the table.
		 */

		final Iterable<Branch> objects = brs;
		final BiFunction<Branch, String, Double> featureFun = (br, feature) -> br.getFeature(feature);
		final Map<String, String> featureUnits = new HashMap<>();
		BRANCH_FEATURES_DIMENSIONS.forEach(
				(f, d) -> featureUnits.put(f, TMUtils.getUnitsFor(d, model.getSpaceUnits(), model.getTimeUnits())));
		final Map<String, String> infoTexts = new HashMap<>();
		final Function<Branch, String> labelGenerator = b -> b.toString();
		final BiConsumer<Branch, String> labelSetter = null;
		final Supplier<FeatureColorGenerator<Branch>> colorSupplier = () -> b -> Color.WHITE;

		final TablePanel<Branch> table = new TablePanel<>(objects, BRANCH_FEATURES, featureFun, BRANCH_FEATURES_NAMES,
				BRANCH_FEATURES_SHORTNAMES, featureUnits, BRANCH_FEATURES_ISINTS, infoTexts, colorSupplier,
				labelGenerator, labelSetter);

		return table;
	}

	public static class Branch implements Comparable<Branch> {

		private final Map<String, Double> features = new HashMap<>();

		private String trackName;

		private Spot first;

		private Spot last;

		private Set<Branch> predecessors;

		private Set<Branch> successors;

		@Override
		public String toString() {
			return trackName + ": " + first + " → " + last;
		}

		double dt() {
			return last.diffTo(first, Spot.POSITION_T);
		}

		/**
		 * Returns the value corresponding to the specified branch feature.
		 *
		 * @param feature The feature string to retrieve the stored value for.
		 * @return the feature value, as a {@link Double}. Will be <code>null</code> if
		 *         it has not been set.
		 */
		public final Double getFeature(final String feature) {
			return features.get(feature);
		}

		/**
		 * Stores the specified feature value for this branch.
		 *
		 * @param feature the name of the feature to store, as a {@link String}.
		 * @param value   the value to store, as a {@link Double}. Using
		 *                <code>null</code> will have unpredicted outcomes.
		 */
		public final void putFeature(final String feature, final Double value) {
			features.put(feature, value);
		}

		/**
		 * Sort by predecessors number, then successors number, then alphabetically by
		 * first spot name.
		 */
		@Override
		public int compareTo(final Branch o) {
			if (predecessors.size() != o.predecessors.size())
				return predecessors.size() - o.predecessors.size();
			if (successors.size() != o.successors.size())
				return successors.size() - o.successors.size();
			if (first.getName().compareTo(o.first.getName()) != 0)
				return first.getName().compareTo(o.first.getName());
			return last.getName().compareTo(o.last.getName());
		}
	}

	private static final String TRACK_ID = "TRACK_ID";
	private static final String N_PREDECESSORS = "N_PREDECESSORS";
	private static final String N_SUCCESSORS = "N_SUCCESSORS";
	private static final String DELTA_T = "DELTA_T";
	private static final String DISTANCE = "DISTANCE";
	private static final String MEAN_VELOCITY = "MEAN_VELOCITY";
	private static final String FIRST = "FIRST";
	private static final String LAST = "LAST";
	private static final List<String> BRANCH_FEATURES = Arrays.asList(
			new String[] { TRACK_ID, N_PREDECESSORS, N_SUCCESSORS, DELTA_T, DISTANCE, MEAN_VELOCITY, FIRST, LAST });

	private static final Map<String, String> BRANCH_FEATURES_NAMES = new HashMap<>();
	private static final Map<String, String> BRANCH_FEATURES_SHORTNAMES = new HashMap<>();
	private static final Map<String, Boolean> BRANCH_FEATURES_ISINTS = new HashMap<>();
	private static final Map<String, fiji.plugin.trackmate.Dimension> BRANCH_FEATURES_DIMENSIONS = new HashMap<>();
	static {
		BRANCH_FEATURES_NAMES.put(TRACK_ID, "Track ID");
		BRANCH_FEATURES_SHORTNAMES.put(TRACK_ID, "Track ID");
		BRANCH_FEATURES_ISINTS.put(TRACK_ID, Boolean.TRUE);
		BRANCH_FEATURES_DIMENSIONS.put(TRACK_ID, fiji.plugin.trackmate.Dimension.NONE);

		BRANCH_FEATURES_NAMES.put(N_PREDECESSORS, "Track ID");
		BRANCH_FEATURES_SHORTNAMES.put(N_PREDECESSORS, "N predecessors");
		BRANCH_FEATURES_ISINTS.put(N_PREDECESSORS, Boolean.TRUE);
		BRANCH_FEATURES_DIMENSIONS.put(N_PREDECESSORS, fiji.plugin.trackmate.Dimension.NONE);

		BRANCH_FEATURES_NAMES.put(N_SUCCESSORS, "Track ID");
		BRANCH_FEATURES_SHORTNAMES.put(N_SUCCESSORS, "N successors");
		BRANCH_FEATURES_ISINTS.put(N_SUCCESSORS, Boolean.TRUE);
		BRANCH_FEATURES_DIMENSIONS.put(N_SUCCESSORS, fiji.plugin.trackmate.Dimension.NONE);

		BRANCH_FEATURES_NAMES.put(DELTA_T, "Branch duration");
		BRANCH_FEATURES_SHORTNAMES.put(DELTA_T, "Delta T");
		BRANCH_FEATURES_ISINTS.put(DELTA_T, Boolean.FALSE);
		BRANCH_FEATURES_DIMENSIONS.put(DELTA_T, fiji.plugin.trackmate.Dimension.TIME);

		BRANCH_FEATURES_NAMES.put(DISTANCE, "Distance traveled");
		BRANCH_FEATURES_SHORTNAMES.put(DISTANCE, "Dist");
		BRANCH_FEATURES_ISINTS.put(DISTANCE, Boolean.FALSE);
		BRANCH_FEATURES_DIMENSIONS.put(DISTANCE, fiji.plugin.trackmate.Dimension.LENGTH);

		BRANCH_FEATURES_NAMES.put(MEAN_VELOCITY, "Mean velocity");
		BRANCH_FEATURES_SHORTNAMES.put(MEAN_VELOCITY, "Mean V");
		BRANCH_FEATURES_ISINTS.put(MEAN_VELOCITY, Boolean.FALSE);
		BRANCH_FEATURES_DIMENSIONS.put(MEAN_VELOCITY, fiji.plugin.trackmate.Dimension.VELOCITY);

		BRANCH_FEATURES_NAMES.put(FIRST, "First spot ID");
		BRANCH_FEATURES_SHORTNAMES.put(FIRST, "First ID");
		BRANCH_FEATURES_ISINTS.put(FIRST, Boolean.TRUE);
		BRANCH_FEATURES_DIMENSIONS.put(FIRST, fiji.plugin.trackmate.Dimension.NONE);

		BRANCH_FEATURES_NAMES.put(LAST, "Last spot ID");
		BRANCH_FEATURES_SHORTNAMES.put(LAST, "Last ID");
		BRANCH_FEATURES_ISINTS.put(LAST, Boolean.TRUE);
		BRANCH_FEATURES_DIMENSIONS.put(LAST, fiji.plugin.trackmate.Dimension.NONE);
	}

	private final <K, V> Set<V> getUniqueValues(final Iterable<K> keys, final Map<K, V> map) {
		final Set<V> mapping = new LinkedHashSet<>();
		for (final K key : keys)
			mapping.add(map.get(key));

		return mapping;
	}

	private static final <K, V> List<K> getCommonKeys(final V targetValue, final Iterable<K> keys,
			final Map<K, V> map) {
		final ArrayList<K> foundKeys = new ArrayList<>();
		for (final K key : keys) {
			if (map.get(key).equals(targetValue))
				foundKeys.add(key);
		}
		return foundKeys;
	}

	private final String buildPlotTitle(final Iterable<String> lYFeatures, final Map<String, String> featureNames,
			String xSelectedSpot) {
		final StringBuilder sb = new StringBuilder("Plot of ");
		final Iterator<String> it = lYFeatures.iterator();
		sb.append(featureNames.get(it.next()));
		while (it.hasNext()) {
			sb.append(", ");
			sb.append(featureNames.get(it.next()));
		}
		sb.append(" vs ");
		sb.append(featureNames.get(xSelectedSpot));
		sb.append(".");
		return sb.toString();
	}

	private static class NSpotPerFrameDataset extends ModelDataset {

		private static final long serialVersionUID = 1L;

		private final double[] time;

		private final int[] nspots;

		public NSpotPerFrameDataset(final Model model, final SelectionModel selectionModel, final DisplaySettings ds,
				final double[] time, final int[] nspots) {
			super(model, selectionModel, ds, Spot.POSITION_T, Collections.singletonList("N spots"));
			this.time = time;
			this.nspots = nspots;
		}

		@Override
		public int getItemCount(final int series) {
			return nspots.length;
		}

		@Override
		public Number getX(final int series, final int item) {
			return Double.valueOf(time[item]);
		}

		@Override
		public Number getY(final int series, final int item) {
			return Double.valueOf(nspots[item]);
		}

		@Override
		public String getSeriesKey(final int series) {
			return yFeatures.get(series);
		}

		@Override
		public String getItemLabel(final int item) {
			return "" + item;
		}

		@Override
		public void setItemLabel(final int item, final String label) {
		}

		@Override
		public XYItemRenderer getRenderer() {
			return new XYLineAndShapeRenderer(true, true);
		}
	}

	private static final float[] toFloat(final double[] d) {
		final float[] f = new float[d.length];
		for (int i = 0; i < f.length; i++)
			f[i] = (float) d[i];
		return f;
	}

	public ImagePlus renderND(HyperStackDisplayer displayer, DisplaySettings ds) {

		Roi initialROI = displayer.getImp().getRoi();
		if (initialROI != null)
			displayer.getImp().killRoi();

		Overlay overlay = displayer.getImp().getOverlay();
		if (overlay == null) {
			overlay = new Overlay();
			displayer.getImp().setOverlay(overlay);
		}
		overlay.clear();

		if (initialROI != null)
			displayer.getImp().getOverlay().add(initialROI);

		if (null != displayer)
			displayer.getImp().updateAndDraw();
		displayer.getImp().setOpenAsHyperStack(true);
//		if ( !imps.isVisible() )
//			imp.show();

		displayer.getImp().getOverlay().add(new SpotOverlay(model, displayer.getImp(), ds));
		displayer.getImp().getOverlay().add(new TrackOverlay(model, displayer.getImp(), ds));
		displayer.getImp().updateAndDraw();

		return displayer.getImp();
	}

	public static ImagePlus[] stack2images(ImagePlus imp) {
		String sLabel = imp.getTitle();
		String sImLabel = "";
		ImageStack stack = imp.getStack();

		int sz = stack.getSize();
		int currentSlice = imp.getCurrentSlice(); // to reset ***

		DecimalFormat df = new DecimalFormat("0000"); // for title
		ImagePlus[] arrayOfImages = new ImagePlus[imp.getStack().getSize()];
		for (int n = 1; n <= sz; ++n) {
			imp.setSlice(n); // activate next slice ***

			// Get current image processor from stack. What ever is
			// used here should do a COPY pixels from old processor to
			// new. For instance, ImageProcessor.crop() returns copy.
			ImageProcessor ip = imp.getProcessor(); // ***
			ImageProcessor newip = ip.createProcessor(ip.getWidth(), ip.getHeight());
			newip.setPixels(ip.getPixelsCopy());

			// Create a suitable label, using the slice label if possible
			sImLabel = imp.getStack().getSliceLabel(n);
			if (sImLabel == null || sImLabel.length() < 1) {
				sImLabel = "slice" + df.format(n) + "_" + sLabel;
			}
			// Create new image corresponding to this slice.
			ImagePlus im = new ImagePlus(sImLabel, newip);
			im.setCalibration(imp.getCalibration());
			arrayOfImages[n - 1] = im;

			// Show this image.
			// imp.show();
		}
		// Reset original stack state.
		imp.setSlice(currentSlice); // ***
		if (imp.isProcessor()) {
			ImageProcessor ip = imp.getProcessor();
			ip.setPixels(ip.getPixels()); // ***
		}
		imp.setSlice(currentSlice);
		return arrayOfImages;
	}

	public void exportToCSV(JTable table, File file) {
		try {

			TableModel modelo = table.getModel();
			FileWriter excel = new FileWriter(file);
			for (int i = 0; i < modelo.getColumnCount(); i++) {
				excel.write(modelo.getColumnName(i) + ",");
			}
			excel.write("\n");
			for (int i = 0; i < modelo.getRowCount(); i++) {
				for (int j = 0; j < modelo.getColumnCount(); j++) {
					if (j == 11)
						excel.write("" + ",");
					if (j != 11) {
						if (modelo.getValueAt(i, j).toString() != null || modelo.getValueAt(i, j).toString() != "") {
							excel.write(modelo.getValueAt(i, j).toString() + ",");
						}
						if (modelo.getValueAt(i, j).toString() == null || modelo.getValueAt(i, j).toString() == "") {
							excel.write("" + "");
						}
//					if (data == "null" || data == "" || data == null) {
//						data = "";
//					}
					}
				}
				excel.write("\n");
			}

			excel.close();

		} catch (IOException ex) {

		}
	}

}
