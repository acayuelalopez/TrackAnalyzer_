import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fiji.plugin.trackmate.Dimension;
import fiji.plugin.trackmate.FeatureModel;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.SelectionModel;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.DogDetectorFactory;
import fiji.plugin.trackmate.detection.LogDetectorFactory;
import fiji.plugin.trackmate.detection.ManualDetectorFactory;
import fiji.plugin.trackmate.features.FeatureFilter;
import fiji.plugin.trackmate.features.FeatureUtils;
import fiji.plugin.trackmate.features.edges.EdgeTargetAnalyzer;
import fiji.plugin.trackmate.features.edges.EdgeTimeLocationAnalyzer;
import fiji.plugin.trackmate.features.manual.ManualEdgeColorAnalyzer;
import fiji.plugin.trackmate.features.manual.ManualSpotColorAnalyzerFactory;
import fiji.plugin.trackmate.features.spot.SpotContrastAndSNRAnalyzerFactory;
import fiji.plugin.trackmate.features.spot.SpotMorphologyAnalyzerFactory;
import fiji.plugin.trackmate.features.track.TrackBranchingAnalyzer;
import fiji.plugin.trackmate.features.track.TrackDurationAnalyzer;
import fiji.plugin.trackmate.features.track.TrackIndexAnalyzer;
import fiji.plugin.trackmate.features.track.TrackLocationAnalyzer;
import fiji.plugin.trackmate.features.track.TrackSpeedStatisticsAnalyzer;
import fiji.plugin.trackmate.features.track.TrackSpotQualityFeatureAnalyzer;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettingsIO;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings.TrackDisplayMode;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings.TrackMateObject;
import fiji.plugin.trackmate.io.TmXmlReader;
import fiji.plugin.trackmate.tracking.LAPUtils;
import fiji.plugin.trackmate.tracking.ManualTrackerFactory;
import fiji.plugin.trackmate.tracking.TrackerKeys;
import fiji.plugin.trackmate.tracking.kalman.KalmanTrackerFactory;
import fiji.plugin.trackmate.tracking.sparselap.SimpleSparseLAPTrackerFactory;
import fiji.plugin.trackmate.tracking.sparselap.SparseLAPTrackerFactory;
import fiji.plugin.trackmate.util.TMUtils;
import fiji.plugin.trackmate.visualization.FeatureColorGenerator;
import fiji.plugin.trackmate.visualization.PerTrackFeatureColorGenerator;
import fiji.plugin.trackmate.visualization.SpotColorGenerator;
import fiji.plugin.trackmate.visualization.TrackMateModelView;
import fiji.plugin.trackmate.visualization.hyperstack.HyperStackDisplayer;
import fiji.plugin.trackmate.visualization.table.TablePanel;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.process.ColorProcessor;

public class ProcessTrackMateXml {
	Thread mainProcess;
	String zstart, zend, ystart, yend, xstart, xend, tstart, tend, RADIUS, THRESHOLD, TARGET_CHANNEL,
			DO_SUBPIXEL_LOCALIZATION, DO_MEDIAN_FILTERING, DETECTOR_NAME, NSPLIT, DOWNSAMPLE_FACTOR, initialSpotFilter,
			TRACKER_NAME, CUTOFF_PERCENTILE, ALTERNATIVE_LINKING_COST_FACTOR, LINKING_MAX_DISTANCE, MAX_FRAME_GAP,
			MAX_DISTANCE, ALLOW_GAP_CLOSING, SPLITTING_MAX_DISTANCE, ALLOW_TRACK_SPLITTING, MERGING_MAX_DISTANCE,
			ALLOW_TRACK_MERGING, BLOCKING_VALUE;
	static Model model;
	static Settings settings;
	static SelectionModel selectionModel;
	static TrackMate trackmate;
	SpotCollection totalSpots;
	static HyperStackDisplayer displayer;
	ImagePlus impAnal;
	ImagePlus[] imps = FirstWizardPanel.imps;
	static boolean tracksVisible, spotsVisible;
	static String[][] dataSpot, dataTrack;
	static String[] columnHeadersSpot, columnHeadersTrack;
	static DisplaySettings ds;
	// public static final ImageIcon ICON = new
	// ImageIcon(TrackMateWizard.class.getResource("images/calculator.png"));

	public static final String NAME = "Export statistics to tables";

	public static final String KEY = "EXPORT_STATS_TO_IJ";

	public static final String INFO_TEXT = "<html>" + "Compute and export all statistics to 3 ImageJ results table. "
			+ "Statistisc are separated in features computed for: " + "<ol> " + "	<li> spots in filtered tracks; "
			+ "	<li> links between those spots; " + "	<li> filtered tracks. " + "</ol> "
			+ "For tracks and links, they are recalculated prior to exporting. Note "
			+ "that spots and links that are not in a filtered tracks are not part " + "of this export." + "</html>";

	private static final String SPOT_TABLE_NAME = "Spots in tracks statistics";

	private static final String EDGE_TABLE_NAME = "Links in tracks statistics";

	private static final String TRACK_TABLE_NAME = "Track statistics";

	private static final String ID_COLUMN = "ID";

	private static final String TRACK_ID_COLUMN = "TRACK_ID";

	private ResultsTable spotTable;

	private ResultsTable edgeTable;

	private ResultsTable trackTable;

	public static final String KEYLINEAR = "Linear track analysis";

	public static final String TRACK_TOTAL_DISTANCE_TRAVELED = "TOTAL_DISTANCE_TRAVELED";

	public static final String TRACK_MAX_DISTANCE_TRAVELED = "MAX_DISTANCE_TRAVELED";

	public static final String TRACK_CONFINMENT_RATIO = "CONFINMENT_RATIO";

	public static final String TRACK_MEAN_STRAIGHT_LINE_SPEED = "MEAN_STRAIGHT_LINE_SPEED";

	public static final String TRACK_LINEARITY_OF_FORWARD_PROGRESSION = "LINEARITY_OF_FORWARD_PROGRESSION";

	// public static final String TRACK_MEAN_DIRECTIONAL_CHANGE_RATE =
	// "MEAN_DIRECTIONAL_CHANGE_RATE";

	public static final String TOTAL_ABSOLUTE_ANGLE_XY = "TOTAL_ABSOLUTE_ANGLE_XY";

	public static final String TOTAL_ABSOLUTE_ANGLE_YZ = "TOTAL_ABSOLUTE_ANGLE_YZ";

	public static final String TOTAL_ABSOLUTE_ANGLE_ZX = "TOTAL_ABSOLUTE_ANGLE_ZX";

	public static final List<String> FEATURES = new ArrayList<String>(9);

	public static final Map<String, String> FEATURE_NAMES = new HashMap<String, String>(9);

	public static final Map<String, String> FEATURE_SHORT_NAMES = new HashMap<String, String>(9);

	public static final Map<String, Dimension> FEATURE_DIMENSIONS = new HashMap<String, Dimension>(9);

	public static final Map<String, Boolean> IS_INT = new HashMap<String, Boolean>(9);

	public ProcessTrackMateXml() {

	}

	public void processTrackMateXml() {
		mainProcess = new Thread(new Runnable() {
			public void run() {

				// if (impAnal != null)
				// impAnal.close();

				File fileXML = new File(TrackAnalyzer_.xmlPath);
				List<ImagePlus> impAnalClose = new ArrayList<ImagePlus>();
				int[] IDs = WindowManager.getIDList();
				if (IDs != null)
					for (int i = 0; i < IDs.length; i++)
						impAnalClose.add(WindowManager.getImage(IDs[i]));

				if (FirstWizardPanel.spotEnable.equals("spotEnable") == Boolean.TRUE
						&& FirstWizardPanel.tableImages.getSelectedRow() != -1) {
					if (IDs != null)
						for (int i = 0; i < IDs.length; i++)
							impAnalClose.get(i).hide();
					impAnal = imps[FirstWizardPanel.tableImages.getSelectedRow()];
				}
				if (FirstWizardPanel.spotEnable.equals("spotEnable") == Boolean.TRUE
						&& FirstWizardPanel.tableImages.getSelectedRow() == -1)
					impAnal = imps[ChooserWizardPanel.tableImages.getSelectedRow()];
				if (ChooserWizardPanel.trackEnable.equals("trackEnable") == Boolean.TRUE
						&& ChooserWizardPanel.tableImages.getSelectedRow() != -1) {
					if (IDs != null)
						for (int i = 0; i < IDs.length; i++)
							impAnalClose.get(i).hide();
					;
					impAnal = imps[ChooserWizardPanel.tableImages.getSelectedRow()];
				}
				if (ChooserWizardPanel.trackEnable.equals("trackEnable") == Boolean.TRUE
						&& ChooserWizardPanel.tableImages.getSelectedRow() == -1)
					impAnal = imps[FirstWizardPanel.tableImages.getSelectedRow()];

				impAnal.show();
				int[] dims = impAnal.getDimensions();

				if (dims[4] == 1 && dims[3] > 1) {

					impAnal.setDimensions(dims[2], dims[4], dims[3]);
					Calibration calibration = impAnal.getCalibration();
					calibration.frameInterval = 1;

				}

				TmXmlReader reader = new TmXmlReader(fileXML);
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Document doc = null;
				try {
					doc = builder.parse(TrackAnalyzer_.xmlPath);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprLinkingP = (XPathExpression) xpath.compile("//Linking/FeaturePenalties[@MEAN_INTENSITY]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					exprDetectorSettings = (XPathExpression) xpath.compile("//Settings/DetectorSettings[@RADIUS]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprInitialSpotFilter = (XPathExpression) xpath.compile("//Settings/InitialSpotFilter[@feature]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprFilter = (XPathExpression) xpath.compile("//SpotFilterCollection/Filter[@feature]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {

					exprTrackerSettings = (XPathExpression) xpath.compile("//Settings/TrackerSettings[@TRACKER_NAME]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprLinking = (XPathExpression) xpath.compile("//TrackerSettings/Linking[@LINKING_MAX_DISTANCE]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprGapClosing = (XPathExpression) xpath.compile("//TrackerSettings/GapClosing[@MAX_FRAME_GAP]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprSplitting = (XPathExpression) xpath
							.compile("//TrackerSettings/TrackSplitting[@SPLITTING_MAX_DISTANCE]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprMerging = (XPathExpression) xpath
							.compile("//TrackerSettings/TrackMerging[@MERGING_MAX_DISTANCE]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {

					exprTrackFilter = (XPathExpression) xpath.compile("//TrackFilterCollection/Filter[@feature]");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < nlBasicSettings.getLength(); i++) {
					Node currentItem = nlBasicSettings.item(i);
					zstart = currentItem.getAttributes().getNamedItem("zstart").getNodeValue();
					zend = currentItem.getAttributes().getNamedItem("zend").getNodeValue();
					ystart = currentItem.getAttributes().getNamedItem("ystart").getNodeValue();
					yend = currentItem.getAttributes().getNamedItem("yend").getNodeValue();
					xstart = currentItem.getAttributes().getNamedItem("xstart").getNodeValue();
					xend = currentItem.getAttributes().getNamedItem("xend").getNodeValue();
					tstart = currentItem.getAttributes().getNamedItem("tstart").getNodeValue();
					tend = currentItem.getAttributes().getNamedItem("tend").getNodeValue();
				}
				Node currentItem = null;
				for (int i = 0; i < nlDetectorSettings.getLength(); i++) {
					currentItem = nlDetectorSettings.item(i);
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

				String linkingNames = null;
				String linkingValues = null;
				for (int i = 0; i < nlLinkingP.getLength(); i++) {
					linkingNames = nlLinkingP.item(i).getAttributes().item(i).getNodeName();
					linkingValues = nlLinkingP.item(i).getAttributes().item(i).getNodeValue();
				}
				for (int i = 0; i < nlInitialSpotFilter.getLength(); i++) {
					currentItem = nlInitialSpotFilter.item(i);
					initialSpotFilter = currentItem.getAttributes().getNamedItem("value").getNodeValue();

				}
				String initialFilterFeature = null;
				String initialFilterValue = null;
				String initialFilterAbove = null;
				for (int i = 0; i < nlFilter.getLength(); i++) {
					currentItem = nlFilter.item(i);
					initialFilterFeature = currentItem.getAttributes().getNamedItem("feature").getNodeValue();
					initialFilterValue = currentItem.getAttributes().getNamedItem("value").getNodeValue();
					initialFilterAbove = currentItem.getAttributes().getNamedItem("isabove").getNodeValue();

				}

				String initialTrackFilterFeature = null;
				String initialTrackFilterValue = null;
				String initialTrackFilterAbove = null;

				for (int i = 0; i < nlTrackerSettings.getLength(); i++) {
					currentItem = nlTrackerSettings.item(i);
					TRACKER_NAME = currentItem.getAttributes().getNamedItem("TRACKER_NAME").getNodeValue();
					CUTOFF_PERCENTILE = currentItem.getAttributes().getNamedItem("CUTOFF_PERCENTILE").getNodeValue();
					BLOCKING_VALUE = currentItem.getAttributes().getNamedItem("BLOCKING_VALUE").getNodeValue();
					ALTERNATIVE_LINKING_COST_FACTOR = currentItem.getAttributes()
							.getNamedItem("ALTERNATIVE_LINKING_COST_FACTOR").getNodeValue();
				}
				for (int i = 0; i < nlLinking.getLength(); i++) {
					currentItem = nlLinking.item(i);
					LINKING_MAX_DISTANCE = currentItem.getAttributes().getNamedItem("LINKING_MAX_DISTANCE")
							.getNodeValue();
				}
				for (int i = 0; i < nlGapClosing.getLength(); i++) {
					currentItem = nlGapClosing.item(i);
					MAX_FRAME_GAP = currentItem.getAttributes().getNamedItem("MAX_FRAME_GAP").getNodeValue();
					MAX_DISTANCE = currentItem.getAttributes().getNamedItem("GAP_CLOSING_MAX_DISTANCE").getNodeValue();
					ALLOW_GAP_CLOSING = currentItem.getAttributes().getNamedItem("ALLOW_GAP_CLOSING").getNodeValue();

				}
				for (int i = 0; i < nlSplitting.getLength(); i++) {
					currentItem = nlSplitting.item(i);
					SPLITTING_MAX_DISTANCE = currentItem.getAttributes().getNamedItem("SPLITTING_MAX_DISTANCE")
							.getNodeValue();
					ALLOW_TRACK_SPLITTING = currentItem.getAttributes().getNamedItem("ALLOW_TRACK_SPLITTING")
							.getNodeValue();
				}
				for (int i = 0; i < nlMerging.getLength(); i++) {
					currentItem = nlMerging.item(i);
					MERGING_MAX_DISTANCE = currentItem.getAttributes().getNamedItem("MERGING_MAX_DISTANCE")
							.getNodeValue();
					ALLOW_TRACK_MERGING = currentItem.getAttributes().getNamedItem("ALLOW_TRACK_MERGING")
							.getNodeValue();
				}

				settings = new Settings(impAnal);
				// settings.setFrom(impAnal);
				settings.dt = 0.05;

//				if (DETECTOR_NAME.equals("BLOCK_LOG_DETECTOR") == true) {
//
//					settings.detectorFactory = new BlockLogDetectorFactory();
//					settings.detectorSettings.put(BlockLogDetectorFactory.KEY_NSPLIT, Integer.parseInt(NSPLIT));
//					settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
//					settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL, Integer.parseInt(TARGET_CHANNEL));
//					settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.parseDouble(THRESHOLD));
//					settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION,
//							Boolean.parseBoolean(DO_SUBPIXEL_LOCALIZATION));
//					settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING,
//							Boolean.parseBoolean(DO_MEDIAN_FILTERING));
//					if (initialSpotFilter != null)
//						settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);
//
//				}

				if (DETECTOR_NAME.equals("LOG_DETECTOR") == true) {

					settings.detectorFactory = new LogDetectorFactory();
					settings.detectorSettings = settings.detectorFactory.getDefaultSettings();
					settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION,
							Boolean.parseBoolean(DO_SUBPIXEL_LOCALIZATION));
					settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
					settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL, Integer.parseInt(TARGET_CHANNEL));
					settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.parseDouble(THRESHOLD));
					settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING,
							Boolean.parseBoolean(DO_MEDIAN_FILTERING));
					if (initialSpotFilter != null)
						settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);

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

					if (DETECTOR_NAME.equals("MANUAL_DETECTOR") == true) {

						settings.detectorFactory = new ManualDetectorFactory();
						settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.parseDouble(RADIUS));
						if (initialSpotFilter != null)
							settings.initialSpotFilterValue = Double.parseDouble(initialSpotFilter);

					}

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

					if (initialFilterFeature != null) {

						FeatureFilter filter1 = new FeatureFilter(initialFilterFeature,
								Double.parseDouble(initialFilterValue), Boolean.parseBoolean(initialFilterAbove));

						settings.addSpotFilter(filter1);
					}

				}

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

				settings.addAllAnalyzers();

				model = new Model();
				trackmate = new TrackMate(model, settings);

				Boolean ok = null;

				ok = trackmate.checkInput();
				ok = trackmate.process();

				FeatureModel fm = model.getFeatureModel();
				Set<Integer> trackIDs = model.getTrackModel().trackIDs(true);
				Set<Spot> track = null;
				for (int id = 0; id < trackIDs.size(); id++) {
					Double v = fm.getTrackFeature(id, "TRACK_MEAN_SPEED");
					track = model.getTrackModel().trackSpots(id);

				}
				for (Spot spot : track) {
					int sid = spot.ID();
					Double x = spot.getFeature("POSITION_X");
					Double y = spot.getFeature("POSITION_Y");
					Double t = spot.getFeature("FRAME");
					Double q = spot.getFeature("QUALITY");
					Double snr = spot.getFeature("SNR");
					Double mean = spot.getFeature("MEAN_INTENSITY");
				}
				totalSpots = model.getSpots();
				displayer = null;
				selectionModel = new SelectionModel(model);
				ds = DisplaySettingsIO.readUserDefault();
				ds.setSpotShowName(true);
				ds.setSpotVisible(spotsVisible);
				ds.setSpotColorBy(TrackMateObject.TRACKS, "TRACK_INDEX");
				ds.setTrackVisible(tracksVisible);
				ds.setTrackColorBy(TrackMateObject.TRACKS, "TRACK_INDEX");
				ds.setTrackDisplayMode(TrackDisplayMode.FULL);
				displayer = new HyperStackDisplayer(model, selectionModel, impAnal, ds);
				displayer.render();
				displayer.refresh();

				Rectangle bounds = null;
				Integer firstFrame = null;
				Integer lastFrame = null;
				Integer width = null;
				Integer height = null;
				Integer nCaptures = null;
				ImageStack stack = null;
				Integer channel = null;
				Integer slice = null;
				BufferedImage bi = null;
				ColorProcessor cp = null;
				Integer index = null;
				ImagePlus capture = null;

				if (impAnal.getNFrames() > 1) {
					firstFrame = Math.max(1, Math.min(impAnal.getNFrames(), 1));
					lastFrame = Math.min(impAnal.getNFrames(), Math.max(impAnal.getNFrames(), 1));
				}
				if (impAnal.getNSlices() > 1) {
					firstFrame = Math.max(1, Math.min(impAnal.getNSlices(), 1));
					lastFrame = Math.min(impAnal.getNSlices(), Math.max(impAnal.getNSlices(), 1));
				}
				bounds = displayer.getImp().getCanvas().getBounds();
				width = bounds.width;
				height = bounds.height;
				nCaptures = lastFrame - firstFrame + 1;
				stack = new ImageStack(width, height);

				channel = displayer.getImp().getChannel();
				slice = displayer.getImp().getSlice();
				displayer.getImp().getCanvas().hideZoomIndicator(true);
				for (int frame = firstFrame; frame <= lastFrame; frame++) {
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

				if (FirstWizardPanel.spotEnable.equals("spotEnable") == Boolean.TRUE) {
					// SLTResultsTableVersion sltRT = new SLTResultsTableVersion(selectionModel);
					// ResultsTable spotTable = sltRT.executeSpot(trackmate);
					Model model = trackmate.getModel();
					FeatureModel fm1 = model.getFeatureModel();

					// Export spots

					Set<Integer> trackIDs1 = model.getTrackModel().trackIDs(true);
					Collection<String> spotFeatures = trackmate.getModel().getFeatureModel().getSpotFeatures();

					ResultsTable spotTable = new ResultsTable();

					// Parse spots to insert values as objects
					for (Integer trackID : trackIDs1) {
						Set<Spot> track1 = model.getTrackModel().trackSpots(trackID);
						// Sort by frame
						final List<Spot> sortedTrack = new ArrayList<>(track1);
						Collections.sort(sortedTrack, Spot.frameComparator);

						for (final Spot spot : sortedTrack) {
							spotTable.incrementCounter();
							spotTable.addLabel(spot.getName());
							spotTable.addValue("ID", "" + spot.ID());
							spotTable.addValue("TRACK_ID", "" + trackID.intValue());
							for (final String feature : spotFeatures) {
								final Double val = spot.getFeature(feature);
								if (null == val) {
									spotTable.addValue(feature, "None");
								} else {
									if (fm1.getSpotFeatureIsInt().get(feature).booleanValue()) {
										spotTable.addValue(feature, "" + val.intValue());
									} else {
										spotTable.addValue(feature, val.doubleValue());
									}
								}
							}
						}
					}

					columnHeadersSpot = spotTable.getHeadings();
					int rowsSpot = spotTable.size();
					List<List<String>> dataListSpot = new ArrayList<List<String>>();
					for (int r = 0; r < rowsSpot; r++) {
						List<String> stringsSpot = new ArrayList<String>();
						for (int c = 0; c < columnHeadersSpot.length; c++) {
							String valuesSpot = spotTable.getStringValue(columnHeadersSpot[c], r);
							stringsSpot.add(valuesSpot);

						}
						dataListSpot.add(stringsSpot);
					}
					dataSpot = new String[dataListSpot.size()][];
					for (int j = 0; j < dataSpot.length; j++)
						dataSpot[j] = new String[dataListSpot.get(j).size()];

					for (int j = 0; j < dataListSpot.size(); j++)
						for (int u = 1; u < dataListSpot.get(j).size(); u++)
							dataSpot[j][u] = dataListSpot.get(j).get(u);

					FirstWizardPanel.createSpotTable();

					FirstWizardPanel.tableSpot.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseReleased(final MouseEvent e) {
							if (null != selectionModel && FirstWizardPanel.command == "enable"
									&& FirstWizardPanel.command != null) {
								ListSelectionModel lsm = FirstWizardPanel.tableSpot.getSelectionModel();
								final int selStart = lsm.getMinSelectionIndex();
								final int selEnd = lsm.getMaxSelectionIndex();
								if (selStart < 0 || selEnd < 0)
									return;

								final int minLine = Math.min(selStart, selEnd);
								final int maxLine = Math.max(selStart, selEnd);
								final Set<Spot> spots = new HashSet<>();
								for (int row = minLine; row <= maxLine; row++) {
									final int spotID = Integer
											.parseInt((String) FirstWizardPanel.tableSpot.getValueAt(row, 2));
									final Spot spot = totalSpots.search(spotID);
									if (null != spot)
										spots.add(spot);
								}
								selectionModel.clearSelection();
								selectionModel.addSpotToSelection(spots);
							}

						}
					});

				}
				if (ChooserWizardPanel.trackEnable.equals("trackEnable") == Boolean.TRUE) {

					FEATURES.add(TRACK_TOTAL_DISTANCE_TRAVELED);
					FEATURES.add(TRACK_MAX_DISTANCE_TRAVELED);
					FEATURES.add(TRACK_CONFINMENT_RATIO);
					FEATURES.add(TRACK_MEAN_STRAIGHT_LINE_SPEED);
					FEATURES.add(TRACK_LINEARITY_OF_FORWARD_PROGRESSION);
					// FEATURES.add( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE );
					FEATURES.add(TOTAL_ABSOLUTE_ANGLE_XY);
					FEATURES.add(TOTAL_ABSOLUTE_ANGLE_YZ);
					FEATURES.add(TOTAL_ABSOLUTE_ANGLE_ZX);

					FEATURE_NAMES.put(TRACK_TOTAL_DISTANCE_TRAVELED, "Total distance traveled");
					FEATURE_NAMES.put(TRACK_MAX_DISTANCE_TRAVELED, "Max distance traveled");
					FEATURE_NAMES.put(TRACK_CONFINMENT_RATIO, "Confinment ratio");
					FEATURE_NAMES.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, "Mean straight line speed");
					FEATURE_NAMES.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, "Linearity of forward progression");
					// FEATURE_NAMES.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, "Mean directional
					// change rate" );
					FEATURE_NAMES.put(TOTAL_ABSOLUTE_ANGLE_XY, "Absolute angle in xy plane");
					FEATURE_NAMES.put(TOTAL_ABSOLUTE_ANGLE_YZ, "Absolute angle in yz plane");
					FEATURE_NAMES.put(TOTAL_ABSOLUTE_ANGLE_ZX, "Absolute angle in zx plane");

					FEATURE_SHORT_NAMES.put(TRACK_TOTAL_DISTANCE_TRAVELED, "Total dist.");
					FEATURE_SHORT_NAMES.put(TRACK_MAX_DISTANCE_TRAVELED, "Max dist.");
					FEATURE_SHORT_NAMES.put(TRACK_CONFINMENT_RATIO, "Cnfnmnt ratio");
					FEATURE_SHORT_NAMES.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, "Mean v. line");
					FEATURE_SHORT_NAMES.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, "Lin. fwd. progr.");
					// FEATURE_SHORT_NAMES.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, "Mean 𝛾 rate"
					// );
					FEATURE_SHORT_NAMES.put(TOTAL_ABSOLUTE_ANGLE_XY, "Abs. angle xy");
					FEATURE_SHORT_NAMES.put(TOTAL_ABSOLUTE_ANGLE_YZ, "Abs. angle yz");
					FEATURE_SHORT_NAMES.put(TOTAL_ABSOLUTE_ANGLE_ZX, "Abs. angle zx");

					FEATURE_DIMENSIONS.put(TRACK_TOTAL_DISTANCE_TRAVELED, Dimension.LENGTH);
					FEATURE_DIMENSIONS.put(TRACK_MAX_DISTANCE_TRAVELED, Dimension.LENGTH);
					FEATURE_DIMENSIONS.put(TRACK_CONFINMENT_RATIO, Dimension.NONE);
					FEATURE_DIMENSIONS.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, Dimension.VELOCITY);
					FEATURE_DIMENSIONS.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, Dimension.NONE);
					// FEATURE_DIMENSIONS.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, Dimension.RATE );
					FEATURE_DIMENSIONS.put(TOTAL_ABSOLUTE_ANGLE_XY, Dimension.ANGLE);
					FEATURE_DIMENSIONS.put(TOTAL_ABSOLUTE_ANGLE_YZ, Dimension.ANGLE);
					FEATURE_DIMENSIONS.put(TOTAL_ABSOLUTE_ANGLE_ZX, Dimension.ANGLE);

					IS_INT.put(TRACK_TOTAL_DISTANCE_TRAVELED, Boolean.FALSE);
					IS_INT.put(TRACK_MAX_DISTANCE_TRAVELED, Boolean.FALSE);
					IS_INT.put(TRACK_CONFINMENT_RATIO, Boolean.FALSE);
					IS_INT.put(TRACK_MEAN_STRAIGHT_LINE_SPEED, Boolean.FALSE);
					IS_INT.put(TRACK_LINEARITY_OF_FORWARD_PROGRESSION, Boolean.FALSE);
					// IS_INT.put( TRACK_MEAN_DIRECTIONAL_CHANGE_RATE, Boolean.FALSE );
					IS_INT.put(TOTAL_ABSOLUTE_ANGLE_XY, Boolean.FALSE);
					IS_INT.put(TOTAL_ABSOLUTE_ANGLE_YZ, Boolean.FALSE);
					IS_INT.put(TOTAL_ABSOLUTE_ANGLE_ZX, Boolean.FALSE);

					// Model
					final Model model = trackmate.getModel();
					final FeatureModel fm1 = model.getFeatureModel();
					// Export tracks

					final Set<Integer> trackIDs1 = model.getTrackModel().trackIDs(true);
					// Yield available edge feature

					trackTable = new ResultsTable();

					// Sort by track
					for (Integer trackID : trackIDs1) {

						final List<Spot> spots = new ArrayList<>(model.getTrackModel().trackSpots(trackID));
						Collections.sort(spots, Spot.frameComparator);
						final Spot first = spots.get(0);

						/*
						 * Iterate over edges.
						 */

						final Set<DefaultWeightedEdge> edges = model.getTrackModel().trackEdges(trackID);

						double totalDistance = 0.0;
						double maxDistanceSq = Double.NEGATIVE_INFINITY;
						double maxDistance = 0.0;
						double dx = 0;
						double dy = 0;
						double dz = 0;

						for (final DefaultWeightedEdge edge : edges) {
							// Total distance travelled.
							final Spot source = model.getTrackModel().getEdgeSource(edge);
							final Spot target = model.getTrackModel().getEdgeTarget(edge);
							final double d = Math.sqrt(source.squareDistanceTo(target));
							totalDistance += d;

							// Max distance traveled.
							final double dToFirstSq = first.squareDistanceTo(target);
							if (dToFirstSq > maxDistanceSq) {
								maxDistanceSq = dToFirstSq;
								maxDistance = Math.sqrt(maxDistanceSq);
							}

							dx += target.getDoublePosition(0) - source.getDoublePosition(0);
							dy += target.getDoublePosition(1) - source.getDoublePosition(1);
							dz += target.getDoublePosition(2) - source.getDoublePosition(2);
						}

						// Dependency features.
						final double netDistance = fm1.getTrackFeature(trackID,
								TrackDurationAnalyzer.TRACK_DISPLACEMENT);
						final double tTotal = fm1.getTrackFeature(trackID, TrackDurationAnalyzer.TRACK_DURATION);
						final double vMean = fm1.getTrackFeature(trackID,
								TrackSpeedStatisticsAnalyzer.TRACK_MEAN_SPEED);

						// Our features.
						final double confinmentRatio = netDistance / totalDistance;
						final double meanStraightLineSpeed = netDistance / tTotal;
						final double linearityForwardProgression = meanStraightLineSpeed / vMean;
						// final double meanAngleSpeed = sumAngleSpeed / nAngleSpeed;

						// Angle features.
						final double angleXY = Math.atan2(dy, dx);
						final double angleYZ = Math.atan2(dz, dy);
						final double angleZX = Math.atan2(dx, dz);
						final Collection<String> trackFeatures = fm1.getTrackFeatures();
						trackTable.incrementCounter();
						trackTable.addLabel(model.getTrackModel().name(trackID));
						trackTable.addValue(TRACK_ID_COLUMN, "" + trackID.intValue());

						for (final String feature : trackFeatures) {
							final Double val = fm1.getTrackFeature(trackID, feature);
							if (null == val) {
								trackTable.addValue(feature, "None");
							} else {
								if (fm1.getTrackFeatureIsInt().get(feature).booleanValue()) {
									trackTable.addValue(feature, "" + val.intValue());
								} else {
									trackTable.addValue(feature, val.doubleValue());
								}
							}
						}

						trackTable.addValue(TRACK_TOTAL_DISTANCE_TRAVELED,
								"" + (double) Math.round(totalDistance * 1000d) / 1000d);
						trackTable.addValue(TRACK_MAX_DISTANCE_TRAVELED,
								"" + (double) Math.round(maxDistance * 1000d) / 1000d);
						trackTable.addValue(TRACK_MEAN_STRAIGHT_LINE_SPEED,
								"" + (double) Math.round(meanStraightLineSpeed * 1000d) / 1000d);
						trackTable.addValue(TRACK_LINEARITY_OF_FORWARD_PROGRESSION,
								"" + (double) Math.round(linearityForwardProgression * 1000d) / 1000d);
						trackTable.addValue(TOTAL_ABSOLUTE_ANGLE_XY, "" + (double) Math.round(angleXY * 1000d) / 1000d);
						trackTable.addValue(TOTAL_ABSOLUTE_ANGLE_YZ, "" + (double) Math.round(angleYZ * 1000d) / 1000d);
						trackTable.addValue(TOTAL_ABSOLUTE_ANGLE_ZX, "" + (double) Math.round(angleZX * 1000d) / 1000d);
						trackTable.addValue(TRACK_CONFINMENT_RATIO,
								"" + (double) Math.round(confinmentRatio * 1000d) / 1000d);
						trackTable.addValue("TRACK_CLASSIFICATION", "");
						// for (int row = 0; row < trackTable.size(); row++) {
						if (confinmentRatio == 0.0)
							trackTable.addValue("TRACK_CLASSIFICATION", "Total-Confined Track");
						if (confinmentRatio == 1.0)
							trackTable.addValue("TRACK_CLASSIFICATION", "Perfectly Straight Track");
						if (confinmentRatio > 0.0 && confinmentRatio <= 0.5)
							trackTable.addValue("TRACK_CLASSIFICATION", "Strongly Confined Track");
						if (confinmentRatio > 0.05 && confinmentRatio <= 0.25)
							trackTable.addValue("TRACK_CLASSIFICATION", "Purely Random Track");
						if (confinmentRatio > 0.25 && confinmentRatio < 1.0)
							trackTable.addValue("TRACK_CLASSIFICATION", "Fairly Straight Track");
						// }
					}
					columnHeadersTrack = trackTable.getHeadings();
					int rowsTrack = trackTable.size();
					List<List<String>> dataListTrack = new ArrayList<List<String>>();
					for (int r = 0; r < rowsTrack; r++) {
						List<String> stringsTrack = new ArrayList<String>();
						for (int c = 0; c < columnHeadersTrack.length; c++) {
							String valuesTrack = trackTable.getStringValue(columnHeadersTrack[c], r);
							stringsTrack.add(valuesTrack);

						}
						dataListTrack.add(stringsTrack);
					}
					dataTrack = new String[dataListTrack.size()][];
					for (int j = 0; j < dataTrack.length; j++)
						dataTrack[j] = new String[dataListTrack.get(j).size()];

					for (int j = 0; j < dataListTrack.size(); j++)
						for (int u = 1; u < dataListTrack.get(j).size(); u++)
							dataTrack[j][u] = dataListTrack.get(j).get(u);

					ChooserWizardPanel.createTrackTable();
					ChooserWizardPanel.tableTrack.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseReleased(final MouseEvent e) {
							if (null != selectionModel && ChooserWizardPanel.command == "enable"
									&& ChooserWizardPanel.command != null) {
								ListSelectionModel lsm = ChooserWizardPanel.tableTrack.getSelectionModel();
								final int selStart = lsm.getMinSelectionIndex();
								final int selEnd = lsm.getMaxSelectionIndex();
								if (selStart < 0 || selEnd < 0)
									return;

								final int minLine = Math.min(selStart, selEnd);
								final int maxLine = Math.max(selStart, selEnd);
								final Set<DefaultWeightedEdge> edges = new HashSet<>();
								final Set<Spot> spots = new HashSet<>();
								for (int row = minLine; row <= maxLine; row++) {
									final int trackID = Integer
											.parseInt((String) ChooserWizardPanel.tableTrack.getValueAt(row, 2));
									spots.addAll(model.getTrackModel().trackSpots(trackID));
									edges.addAll(model.getTrackModel().trackEdges(trackID));
								}
								selectionModel.clearSelection();
								selectionModel.addSpotToSelection(spots);
								selectionModel.addEdgeToSelection(edges);

							}

						}
					});

				}

			}

		});
		mainProcess.start();

	}

	private static void transferCalibration(ImagePlus from, ImagePlus to) {
		Calibration fc = from.getCalibration();
		Calibration tc = to.getCalibration();

		tc.setUnit(fc.getUnit());
		tc.setTimeUnit(fc.getTimeUnit());
		tc.frameInterval = fc.frameInterval;

		double mag = from.getCanvas().getMagnification();
		tc.pixelWidth = fc.pixelWidth / mag;
		tc.pixelHeight = fc.pixelHeight / mag;
		tc.pixelDepth = fc.pixelDepth;
	}

	private final TablePanel<Spot> createSpotTableRT(final Model model, final DisplaySettings ds) {
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

}
