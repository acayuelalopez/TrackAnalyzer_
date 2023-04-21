import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.math3.stat.StatUtils;
import org.omg.CORBA.OMGVMCID;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import DiffusionCoefficientEstimator.AbstractDiffusionCoefficientEstimatorModified;
import DiffusionCoefficientEstimator.CovarianceDiffusionCoefficientEstimatorModified;
import DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimatorModified;
import drift.StaticDriftCalculatorModified;
import features.AbstractTrajectoryFeatureModified;
import features.ActiveTransportParametersFeatureModified;
import features.Asymmetry3FeatureModified;
import features.CenterOfGravityFeatureModified;
import features.ConfinedDiffusionParametersFeatureModified;
import features.EfficiencyFeatureModified;
import features.FractalDimensionFeatureModified;
import features.GaussianityFeautureModified;
import features.KurtosisFeatureModified;
import features.MSDRatioFeatureModified;
import features.MeanSpeedFeatureModified;
import features.PowerLawFeatureModified;
import features.StraightnessFeatureModified;
import features.TrappedProbabilityFeatureModified;
import fiji.plugin.trackmate.Spot;
import ij.IJ;
import ij.Prefs;
import ij.WindowManager;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.io.OpenDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import traJ.TrajectoryModified;
import trajectory_classifier.ExportImportToolsModified;
import trajectory_classifier.RRFClassifierRenjinModified;
import trajectory_classifier.SubtrajectoryModified;
import trajectory_classifier.VisualizationUtilsModified;
import trajectory_classifier.WeightedWindowedClassificationProcessModified;

public class TraJClassifierTest_ implements PlugIn {

	static double timelag;
	double minTrackLength;
	int windowSizeClassification;
	int minSegmentLength;
	double pixelsize;
	int resampleRate;
	boolean showID;
	boolean showOverviewClasses;
	boolean removeGlobalDrift;
	boolean useReducedModelConfinedMotion;
	int ommittedTrajectories;
	String type;
	public static TraJClassifierTest_ instance;
	ArrayList<SubtrajectoryModified> classifiedSegments;
	ArrayList<TrajectoryModified> tracksToClassify;
	ArrayList<TrajectoryModified> parentTrajectories;

	public TraJClassifierTest_() {
		instance = this;
	}

	// List<Integer> links;
	public static TraJClassifierTest_ getInstance() {
		if (instance == null) {
			instance = new TraJClassifierTest_();
		}
		return instance;
	}

	public void run(String arg) {

		/*
		 * Export model!
		 */
		String modelpath = "";
		try {
			modelpath = ExportResource("/randomForestModel.RData");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Import Data
		 */
		List<Integer> trackSize = null;
		if (!arg.contains("DEBUG")) {
			tracksToClassify = new ArrayList<TrajectoryModified>();
			TrajectoryModified.restIDCounter();

			List<List<Spot>> imported2Spot = new ArrayList<List<Spot>>();
			Set<Integer> trackIDs = SPTBatch_.model.getTrackModel().trackIDs(true);
			trackSize = new ArrayList<Integer>();

			for (Integer id : trackIDs) {
				List<Spot> imported1Spot = new ArrayList<Spot>();
				Set<Spot> track = SPTBatch_.model.getTrackModel().trackSpots(id);
				trackSize.add(track.size());
				ArrayList<Float> framesByTrack = new ArrayList<Float>();
				ArrayList<Float> framesByTrackSort = new ArrayList<Float>();
				ArrayList<Float> trackID = new ArrayList<Float>();
				ArrayList<Integer> indexes = new ArrayList<Integer>();
				List<Spot> spots = new ArrayList<Spot>();
				for (Spot spot : track) {
					trackID.add((Float.valueOf(id)));
					framesByTrack.add(Float.valueOf(spot.getFeature(Spot.FRAME).toString()));
					framesByTrackSort.add(Float.valueOf(spot.getFeature(Spot.FRAME).toString()));
					spots.add(spot);

				}
				Collections.sort(framesByTrackSort);
				for (int z = 0; z < framesByTrackSort.size(); z++)
					indexes.add(framesByTrack.indexOf(framesByTrackSort.get(z)));
				for (int y = 0; y < indexes.size(); y++)
					imported1Spot.add(spots.get(indexes.get(y)));

				imported2Spot.add(imported1Spot);

			}

			for (int i = 0; i < imported2Spot.size(); i++) {
				TrajectoryModified t = new TrajectoryModified(2);
				boolean firstPosition = true;
				for (int j = 0; j < imported2Spot.get(i).size(); j++) {
					double x = imported2Spot.get(i).get(j).getFeature(Spot.POSITION_X);
					double y = imported2Spot.get(i).get(j).getFeature(Spot.POSITION_Y);
					int time = imported2Spot.get(i).get(j).getFeature(Spot.FRAME).intValue();
					if (firstPosition) {
						t.setRelativStartTimepoint(time);
						firstPosition = false;
					}
					t.add(x, y, 0);

				}
				tracksToClassify.add(t);
			}

		}

		int maxNumberOfPositions = 0;
		for (int i = 0; i < tracksToClassify.size(); i++) {
			if (tracksToClassify.get(i).size() > maxNumberOfPositions) {
				maxNumberOfPositions = tracksToClassify.get(i).size();
			}
		}
		boolean visualize = true;

		if (traJParametersWindow.keyWord == "keyword") {
			minTrackLength = Integer.valueOf(traJParametersWindow.minLengthTextS);
			windowSizeClassification = Integer.valueOf(traJParametersWindow.windowTextS);
			minSegmentLength = Integer.valueOf(traJParametersWindow.minSegTextS);

		} else {
			minTrackLength = 10;// Collections.min(trackSize);
			windowSizeClassification = 5;// ((int)Collections.min(trackSize)/2);
			minSegmentLength = 5;// ((int)Collections.min(trackSize)/2);
		}
		resampleRate = (int) 1;
		if (SPTBatch_.imps.getCalibration().getXUnit() == "pixel")
			pixelsize = SPTBatch_.imps.getCalibration().pixelWidth;
		if (SPTBatch_.imps.getCalibration().getXUnit() != "pixel")
			pixelsize = 0;

		timelag = SPTBatch_.fps;// (1.0/30);//framerate
		useReducedModelConfinedMotion = false;
		showID = true;
		showOverviewClasses = true;
		removeGlobalDrift = false;

		/*
		 * Scaling
		 */
		if (pixelsize > 0.000001) {
			for (int i = 0; i < tracksToClassify.size(); i++) {
				tracksToClassify.get(i).scale(pixelsize);
			}
		}

		/*
		 * Trajectories which to often change its position are not suitable for the
		 * classifier. At least for 50% percent of the trajectories, the position have
		 * to change.
		 */

		for (int i = 0; i < tracksToClassify.size(); i++) {
			TrajectoryModified t = tracksToClassify.get(i);
			int changesCounter = 0;
			for (int j = 1; j < t.size(); j++) {

				if (t.get(j).distance(t.get(j - 1)) > Math.pow(10, -12)) {
					changesCounter++;
				}
			}
			if (1.0 * changesCounter / t.size() < 0.5) {
				tracksToClassify.remove(i);
				ommittedTrajectories++;
				i--;
			}
		}

		/*
		 * Classification, Segmentation & Visualization
		 */

		HashMap<String, Color> mapTypeToColor = new HashMap<String, Color>();
		mapTypeToColor.put("DIRECTED/ACTIVE", Color.MAGENTA);
		mapTypeToColor.put("NORM. DIFFUSION", Color.RED);
		mapTypeToColor.put("CONFINED", Color.YELLOW);
		mapTypeToColor.put("SUBDIFFUSION", Color.GREEN);

		/*
		 * Remove tracks which are too short
		 */
		parentTrajectories = new ArrayList<TrajectoryModified>();
		for (TrajectoryModified track : tracksToClassify) {
			if (track.size() > minTrackLength) {
				parentTrajectories.add(track);
			}
		}

		/*
		 * Remove drift
		 */
		StaticDriftCalculatorModified<TrajectoryModified> dcalc = new StaticDriftCalculatorModified<TrajectoryModified>();
//		if (removeGlobalDrift) {
//			double[] drft = dcalc.calculateDrift(parentTrajectories);
//			StaticDriftCorrector dcorr = new StaticDriftCorrector(drft);
//			parentTrajectories = dcorr.removeDrift(parentTrajectories);
//		}

		/*
		 * Classification and segmentation
		 */
		if (parentTrajectories.isEmpty() == Boolean.FALSE) {

			classifiedSegments = classifyAndSegment(parentTrajectories, modelpath, windowSizeClassification,
					minSegmentLength, 10, resampleRate);

			/*
			 * Visualization
			 */
			if (visualize) {
				// Trajectories
				Overlay ov = new Overlay();
				for (int i = 0; i < classifiedSegments.size(); i++) {
					SubtrajectoryModified tr = classifiedSegments.get(i);

					ArrayList<Roi> prois = null;
					if (pixelsize > 0.000001) {
						prois = VisualizationUtilsModified.generateVisualizationRoisFromTrack(tr,
								mapTypeToColor.get(tr.getType()), showID, pixelsize);

					} else {
						prois = VisualizationUtilsModified.generateVisualizationRoisFromTrack(tr,
								mapTypeToColor.get(tr.getType()), showID, IJ.getImage().getCalibration().pixelWidth);
					}
					for (Roi r : prois) {
						ov.add(r);
					}
				}

				// Classes
				if (showOverviewClasses) {
					Set<String> classes = mapTypeToColor.keySet();

					Iterator<String> it = classes.iterator();
					int y = 5;

					float fsize = 20;
					AffineTransform affinetransform = new AffineTransform();
					FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
					int width = (int) IJ.getImage().getProcessor().getFont().getStringBounds("Norm. Diffusion", frc)
							.getWidth();
					Font f = IJ.getImage().getProcessor().getFont();
					while (1.0 * width / IJ.getImage().getWidth() > 0.08) {
						fsize--;
						f = f.deriveFont(fsize);
						width = (int) f.getStringBounds("Norm. Diffusion", frc).getWidth();
					}

					TextRoi.setFont("TimesRoman", (int) fsize, Font.PLAIN);
					while (it.hasNext()) {
						String type = it.next();
						TextRoi troi = new TextRoi(5, y, type);
						troi.setFillColor(Color.DARK_GRAY);
						troi.setStrokeColor(mapTypeToColor.get(type));
						ov.add(troi);
						y = y + 20;

					}
				}

				IJ.getImage().setOverlay(ov);
				IJ.getImage().updateAndRepaintWindow();
			}

			/*
			 * Fill results table
			 */

			HashMap<String, ResultsTable> rtables = new HashMap<String, ResultsTable>();
			rtables.put("DIRECTED/ACTIVE", new ResultsTable());
			rtables.put("NORM. DIFFUSION", new ResultsTable());
			rtables.put("SUBDIFFUSION", new ResultsTable());
			rtables.put("CONFINED", new ResultsTable());

			double sumConf = 0;
			for (int i = 0; i < classifiedSegments.size(); i++) {
			
				IJ.showProgress(i, classifiedSegments.size());
				SubtrajectoryModified t = classifiedSegments.get(i);
				type = t.getType();
				ResultsTable rtT = rtables.get(type);
				// if (t.getType() != null) {
				if (rtT == null) {
					SPTBatch_.taskOutput.append("Type: " + type);
					ExportImportToolsModified eit = new ExportImportToolsModified();
					ArrayList<TrajectoryModified> hlp = new ArrayList<TrajectoryModified>();
					eit.exportTrajectoryDataAsCSV(hlp, SPTBatch_.csvPath + File.separator + "bad" + ".csv");
					SPTBatch_.taskOutput.append(t.toString());

				}
				ResultsTable rt = rtT.getResultsTable();
				if (type != null) {
					rt.incrementCounter();
					rt.addValue("PARENT-ID", t.getParent().getID());
					rt.addValue("ID", t.getID());
					rt.addValue("LENGTH", t.size());
					rt.addValue("START", t.getRelativeStartTimepoint());
					rt.addValue("END", t.getRelativeStartTimepoint() + t.size() - 1);
					rt.addValue("CLASS", type);
				}
				AbstractTrajectoryFeatureModified dcEstim = null;
				double dc = 0;
				DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
				NumberFormat formatter = new DecimalFormat("0.###E0", otherSymbols);
				double[] res;
				double goodness = 0;
				double alphaGoodness = 0;

				if (type != null) {
					switch (type) {
					case "DIRECTED/ACTIVE":

						ActiveTransportParametersFeatureModified apf = new ActiveTransportParametersFeatureModified(t,
								timelag);
						res = apf.evaluate();
						rt.addValue("(FIT) D", formatter.format(res[0]));
						rt.addValue("(FIT) Velocity", res[1]);
						goodness = res[2];
						break;
					case "NORM. DIFFUSION":

						dcEstim = new RegressionDiffusionCoefficientEstimatorModified(t, 1 / timelag, 1, t.size() / 3);
						res = dcEstim.evaluate();
						dc = res[0];
						rt.addValue("(FIT) D", formatter.format(dc));
						goodness = res[3];
						break;
					case "CONFINED":
						AbstractDiffusionCoefficientEstimatorModified dcEst = new RegressionDiffusionCoefficientEstimatorModified(
								t, 1 / timelag, 1, 3);
						ConfinedDiffusionParametersFeatureModified confp = new ConfinedDiffusionParametersFeatureModified(
								t, timelag, useReducedModelConfinedMotion, dcEst);
						double[] p = confp.evaluate();
						dc = p[1];
						if (useReducedModelConfinedMotion) {

							rt.addValue("(FIT) CONF. RADIUS", Math.sqrt(p[0]));
							rt.addValue("(FIT) D", formatter.format(p[1]));
							goodness = p[2];
						} else {

							rt.addValue("(FIT) CONF. RADIUS", Math.sqrt(p[0]));
							rt.addValue("(FIT) A [CONF. SHAPE]", p[2]);
							rt.addValue("(FIT) B (CONF SHAPE)", p[3]);
							rt.addValue("(FIT) D", formatter.format(p[1]));
							goodness = p[4];
						}

						break;
					case "SUBDIFFUSION":
						PowerLawFeatureModified pwf = new PowerLawFeatureModified(t, 1 / timelag, 1, t.size() / 3);
						res = pwf.evaluate();
						dc = res[1];

						rt.addValue("(FIT) D", formatter.format(dc));
						goodness = res[2];
						break;
					default:
						break;
					}
				}

				AbstractTrajectoryFeatureModified pwf = new PowerLawFeatureModified(t, 1 / timelag, 1, t.size() / 3);
				res = pwf.evaluate();
				double alpha = res[0];
				alphaGoodness = res[2];

				AbstractTrajectoryFeatureModified f = new CenterOfGravityFeatureModified(t);
				double cog_x = f.evaluate()[0];
				double cog_y = f.evaluate()[1];

				rt.addValue("X (COG)", cog_x);
				rt.addValue("Y (COG)", cog_y);
				if (t.getType() != null) {
					if (t.getType().equals("NONE") == false) {

						FractalDimensionFeatureModified fdf = new FractalDimensionFeatureModified(t);
						double v = fdf.evaluate()[0];
						;
						rt.addValue("FRACT. DIM.", v);

						TrappedProbabilityFeatureModified trapped = new TrappedProbabilityFeatureModified(t);
						v = trapped.evaluate()[0];
						rt.addValue("TRAPPEDNESS", v);

						EfficiencyFeatureModified eff = new EfficiencyFeatureModified(t);
						v = eff.evaluate()[0];
						rt.addValue("EFFICENCY", v);

						StraightnessFeatureModified straight = new StraightnessFeatureModified(t);
						v = straight.evaluate()[0];
						rt.addValue("STRAIGHTNESS", v);

						MeanSpeedFeatureModified msfeature = new MeanSpeedFeatureModified(t, timelag);
						v = msfeature.evaluate()[1];
						rt.addValue("SPEED", v);

						KurtosisFeatureModified kurt = new KurtosisFeatureModified(t);
						v = kurt.evaluate()[0];
						rt.addValue("KURTOSIS", v);

						// AbstractTrajectoryFeature pwf = new PowerLawFeature(t, 1, t.size()/3);
						// res = pwf.evaluate();
						// v = res[0];
						rt.addValue("(FIT) ALPHA", alpha);

						GaussianityFeautureModified gauss = new GaussianityFeautureModified(t, 1);
						v = gauss.evaluate()[0];
						rt.addValue("GAUSSIANITY", v);

						Asymmetry3FeatureModified asym3 = new Asymmetry3FeatureModified(t);
						v = asym3.evaluate()[0];
						rt.addValue("Asymmetry", v);

						MSDRatioFeatureModified msdratio = new MSDRatioFeatureModified(t, 1, 5);
						v = msdratio.evaluate()[0];
						rt.addValue("MSDRatio", v);

						CovarianceDiffusionCoefficientEstimatorModified cest = new CovarianceDiffusionCoefficientEstimatorModified(
								t, 1 / timelag);
						res = cest.evaluate();
						rt.addValue("Loc. noise_sigma", (res[1] + res[2]) / 2);
						rt.addValue("Fit Goodness", goodness);
						rt.addValue("Alpha Fit Goodness", alphaGoodness);
						double conf = t.getConfidence();
						sumConf += conf;
						rt.addValue("Confidence", conf);

						// }

					}
				}
			}

			/*
			 * Fill parents results table
			 */
			Iterator<String> rtIt = rtables.keySet().iterator();

			ResultsTable parents = new ResultsTable();

			for (int i = 0; i < parentTrajectories.size(); i++) {
				parents.incrementCounter();
				TrajectoryModified t = parentTrajectories.get(i);
				parents.addValue("ID", t.getID());
				parents.addValue("LENGTH", t.size());
				parents.addValue("START", t.getRelativeStartTimepoint());
				parents.addValue("END", t.getRelativeStartTimepoint() + t.size() - 1);
				int subPosCount = 0;
				int subSegCount = 0;
				int normPosCount = 0;
				int normSegCount = 0;
				int directedPosCount = 0;
				int directSegCount = 0;
				int confPosCount = 0;
				int confSegCount = 0;

				ArrayList<SubtrajectoryModified> sameParent = SubtrajectoryModified
						.getTracksWithSameParant(classifiedSegments, t.getID());
				for (SubtrajectoryModified sub : sameParent) {
					if (sub.getType() != null) {
						switch (sub.getType()) {
						case "DIRECTED/ACTIVE":
							directedPosCount += sub.size();
							directSegCount++;
							break;
						case "NORM. DIFFUSION":
							normPosCount += sub.size();
							normSegCount++;
							break;
						case "CONFINED":
							confPosCount += sub.size();
							confSegCount++;
							break;
						case "SUBDIFFUSION":
							subPosCount += sub.size();
							subSegCount++;
							break;
						default:
							break;
						}
					}
				}
				parents.addValue("#SEG_NORM", normSegCount);
				parents.addValue("#POS_NORM", normPosCount);
				parents.addValue("#SEG_SUB", subSegCount);
				parents.addValue("#POS_SUB", subPosCount);
				parents.addValue("#SEG_CONF", confSegCount);
				parents.addValue("#POS_CONF", confPosCount);
				parents.addValue("#SEG_DIRECTED", directSegCount);
				parents.addValue("#POS_DIRECTED", directedPosCount);
			}

			String trajVersion = TrajectoryModified.class.getPackage().getImplementationVersion();
			double[] drift = dcalc.calculateDrift(parentTrajectories);
			ResultsTable overall = new ResultsTable();
			overall.incrementCounter();
			overall.addValue("Mean confindence", sumConf / classifiedSegments.size());
			overall.addValue("Drift x", drift[0]);
			overall.addValue("Drift y", drift[1]);
			overall.addValue("Omitted segments", ommittedTrajectories);
			overall.addValue("Min. track length", minTrackLength);
			overall.addValue("Window size", windowSizeClassification * 2);
			overall.addValue("Min. segment length", minSegmentLength);
			overall.addValue("Resamplerate", resampleRate);
			overall.addValue("Pixelsize", pixelsize);
			overall.addValue("Framerate", 1 / timelag);
			overall.addValue("Reduced conf. model", Boolean.toString(useReducedModelConfinedMotion));
			overall.addValue("Remove global drift", Boolean.toString(removeGlobalDrift));
			overall.addValue("TraJ library version", trajVersion);
			overall.save(SPTBatch_.directDiff + File.separator + "Settings & Miscellaneous"
					+ SPTBatch_.imps.getShortTitle() + ".xls");

			// show tables
			parents.save(SPTBatch_.directDiff + File.separator + "Parents_" + SPTBatch_.imps.getShortTitle() + ".xls");
			// parents.show("Parents");
			rtIt = rtables.keySet().iterator();
			while (rtIt.hasNext()) {
				String rt = rtIt.next();
				if (rt.equals("DIRECTED/ACTIVE") == Boolean.TRUE) {
					rtables.get(rt).save(SPTBatch_.directDiff + File.separator + "DIRECTED_ACTIVE" + "_trajectories_"
							+ SPTBatch_.imps.getShortTitle() + ".xls");
				} else {
					rtables.get(rt).save(SPTBatch_.directDiff + File.separator + rt + "_trajectories_"
							+ SPTBatch_.imps.getShortTitle() + ".xls");
				}
				// rtables.get(rt).show(rt + " trajectories");
			}
		}

	}

	public ArrayList<SubtrajectoryModified> classifyAndSegment(TrajectoryModified trackToClassify, String modelpath,
			int windowSizeClassification, int minSegmentLength, int modeFilterLength, int resampleRate) {
		ArrayList<TrajectoryModified> help = new ArrayList<TrajectoryModified>();
		help.add(trackToClassify);
		return classifyAndSegment(help, modelpath, windowSizeClassification, minSegmentLength, modeFilterLength,
				resampleRate);
	}

	public ArrayList<SubtrajectoryModified> classifyAndSegment(ArrayList<TrajectoryModified> tracksToClassify,
			String modelpath, int windowSizeClassification, int minSegmentLength, int modeFilterLength,
			int resampleRate) {
		ArrayList<SubtrajectoryModified> classified = new ArrayList<SubtrajectoryModified>();
		int j = 0;
		RRFClassifierRenjinModified rrf = new RRFClassifierRenjinModified(modelpath, resampleRate * timelag);
		rrf.start();

		WeightedWindowedClassificationProcessModified wcp = new WeightedWindowedClassificationProcessModified();
		int subidcounter = 1;
		for (TrajectoryModified track : tracksToClassify) {
			j++;
			IJ.showProgress(j, tracksToClassify.size());
			TrajectoryModified mTrack = track;

			String[] classes = wcp.windowedClassification(mTrack, rrf, windowSizeClassification, resampleRate);

			double[] classConfidence = wcp.getPositionConfidence();
			// Moving mode
			classes = movingMode(classes, modeFilterLength);
			double sumConf = 0;
			int Nconf = 0;
			SubtrajectoryModified tr = new SubtrajectoryModified(track, 2);

			tr.setID(subidcounter);
			subidcounter++;
			tr.add(track.get(0).x, track.get(0).y, 0);
			sumConf += classConfidence[0];
			Nconf++;
			String prevCls = classes[0];
			int start = track.getRelativeStartTimepoint();
			tr.setRelativStartTimepoint(start);
			tr.setType(prevCls);

			for (int i = 1; i < classes.length; i++) {
				if (prevCls == classes[i]) {
					tr.add(track.get(i).x, track.get(i).y, 0);
					sumConf += classConfidence[i];
					Nconf++;
				} else {
					;
					tr.setConfidence(sumConf / Nconf);
					classified.add(tr);
					tr = new SubtrajectoryModified(track, 2);
					tr.setID(subidcounter);
					subidcounter++;
					tr.setRelativStartTimepoint(start + i);
					tr.add(track.get(i).x, track.get(i).y, 0);
					sumConf = classConfidence[i];
					Nconf = 1;
					prevCls = classes[i];
					tr.setType(prevCls);
				}
			}
			tr.setConfidence(sumConf / Nconf);
			classified.add(tr);
			sumConf = 0;
			Nconf = 0;

		}
		rrf.stop();

		/*
		 * FILTER
		 */

		// Remove segments smaller then the minimum segment length
		for (int i = 0; i < classified.size(); i++) {
			if (classified.get(i).size() < minSegmentLength) {
				classified.remove(i);
				i--;
			}
		}
		return classified;
	}

	public double getTimelag() {
		return timelag;
	}

	public static String[] movingMode(String[] types, int n) {
		ArrayList<String> ltypes = new ArrayList<String>();
		for (int i = 0; i < types.length; i++) {
			ltypes.add(types[i]);
		}
		return movingMode(ltypes, n);

	}

	public static String[] movingMode(ArrayList<String> types, int n) {
		int windowsize = 2 * n + 1;
		HashSet<String> uniqueTypes = new HashSet<String>();
		uniqueTypes.addAll(types);
		HashMap<String, Integer> mapTypeToInt = new HashMap<String, Integer>();
		HashMap<Integer, String> mapIntToType = new HashMap<Integer, String>();
		Iterator<String> it = uniqueTypes.iterator();
		int key = 0;
		while (it.hasNext()) {
			String type = it.next();
			mapTypeToInt.put(type, key);
			mapIntToType.put(key, type);
			key++;
		}

		String[] medTypes = new String[types.size()];

		for (int i = 0; i < n; i++) {
			medTypes[i] = types.get(i);
		}
		for (int i = types.size() - n; i < types.size(); i++) {
			medTypes[i] = types.get(i);
		}

		for (int i = 0; i < (types.size() - windowsize + 1); i++) {
			List<String> sub = types.subList(i, i + windowsize - 1);
			double[] values = new double[sub.size()];
			for (int j = 0; j < sub.size(); j++) {
				values[j] = mapTypeToInt.get(sub.get(j));
			}

			medTypes[i + n] = mapIntToType.get(((int) StatUtils.mode(values)[0]));
		}
		return medTypes;
	}

	public ArrayList<SubtrajectoryModified> getClassifiedTrajectories() {
		return classifiedSegments;
	}

	public ArrayList<TrajectoryModified> getParentTrajectories() {
		return parentTrajectories;
	}

	/**
	 * Export a resource embedded into a Jar file to the local file path.
	 *
	 * @param resourceName ie.: "/SmartLibrary.dll"
	 * @return The path to the exported resource
	 * @throws Exception
	 */
	public String ExportResource(String resourceName) throws Exception {
		InputStream stream = null;
		OutputStream resStreamOut = null;
		String tmpFolder;
		try {
			stream = this.getClass().getResourceAsStream(resourceName);// note that each / is a directory down in the
																		// "jar tree" been the jar the root of the tree
			if (stream == null) {
				IJ.error("Cannot get resource \"" + resourceName + "\" from Jar file.");
				throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
			}

			int readBytes;
			byte[] buffer = new byte[4096];
			File folderDir = new File(IJ.getDirectory("temp") + "/.trajclassifier");

			// if the directory does not exist, create it
			if (!folderDir.exists()) {
				folderDir.mkdir();
			}
			tmpFolder = folderDir.getPath().replace('\\', '/');
			resStreamOut = new FileOutputStream(tmpFolder + resourceName);
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
		} catch (Exception ex) {
			IJ.error(ex.getMessage());
			throw ex;
		} finally {
			stream.close();
			resStreamOut.close();
		}

		return tmpFolder + resourceName;
	}

	public void setTracksToClassify(ArrayList<TrajectoryModified> t) {
		tracksToClassify = t;
	}

	public double getMinTrackLength() {
		return minTrackLength;
	}

	public void setMinTrackLength(double minTrackLength) {
		this.minTrackLength = minTrackLength;
	}

	public double getPixelsize() {
		return pixelsize;
	}

	public void setPixelsize(double pixelsize) {
		this.pixelsize = pixelsize;
	}

	public boolean isShowID() {
		return showID;
	}

	public void setShowID(boolean showID) {
		this.showID = showID;
	}

	public int getWindowSizeClassification() {
		return windowSizeClassification;
	}

	public boolean isUseReducedModelConfinedMotion() {
		return useReducedModelConfinedMotion;
	}

	public void setTimelag(double timelag) {
		this.timelag = timelag;
	}

	public void setWindowSizeClassification(int windowSizeClassification) {
		this.windowSizeClassification = windowSizeClassification;
	}

}