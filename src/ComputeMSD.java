import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jgrapht.graph.DefaultWeightedEdge;

import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.features.track.TrackIndexAnalyzer;
import fiji.plugin.trackmate.gui.displaysettings.Colormap;
import fiji.plugin.trackmate.visualization.PerTrackFeatureColorGenerator;
import icy.canvas.IcyCanvas;
import icy.gui.util.GuiUtil;
import icy.painter.Painter;
import icy.preferences.GeneralPreferences;
import icy.sequence.Sequence;
import icy.util.StringUtil;
import icy.util.XLSUtil;
import ij.IJ;
import ij.measure.CurveFitter;
import ij.measure.Minimizer;
import ij.measure.ResultsTable;
import math.PowerLawCurveFitModified;
import math.StraightLineFitModified;
import plugins.fab.trackmanager.PluginTrackManagerProcessor;
import plugins.fab.trackmanager.TrackSegment;
import plugins.nchenouard.spot.Detection;

public class ComputeMSD {
	static List<Double> avgScaledInstantDif, alphaToClassifyScaled, mssValues;
	// SimpleRegression sreg;

	public ComputeMSD() {
	}

	public List<Double> Compute(List<Integer> nOfTracks, ResultsTable rtSpots) {

		// XYSeriesCollection xyDataset = new XYSeriesCollection();

		XYSeries series = null;

		List<List<Double>> imported2SpotX = new ArrayList<List<Double>>();
		List<List<Double>> imported2SpotY = new ArrayList<List<Double>>();
		List<List<Double>> imported2SpotT = new ArrayList<List<Double>>();
		List<Integer> trackName = new ArrayList<Integer>();

		for (int id = 0; id < nOfTracks.size(); id++) {
			trackName.add(nOfTracks.get(id));
			List<Double> imported1SpotX = new ArrayList<Double>();
			List<Double> imported1SpotY = new ArrayList<Double>();
			List<Double> imported1SpotT = new ArrayList<Double>();
			// Set<Spot> track =
			// SPTBatch_.model.getTrackModel().trackSpots(nOfTracks.get(id));
			List<Spot> spots = new ArrayList<Spot>();

			for (int i = 0; i < rtSpots.size(); i++) {
				if (rtSpots.getStringValue(2, i).equals(String.valueOf(nOfTracks.get(id).intValue())) == Boolean.TRUE) {
					// IJ.log(rtSpots.getStringValue(2, i)+"------"+rtSpots.getStringValue(4, i));
					imported1SpotX.add(Double.valueOf(rtSpots.getStringValue(4, i)));
					imported1SpotY.add(Double.valueOf(rtSpots.getStringValue(5, i)));
					imported1SpotT.add(Double.valueOf(rtSpots.getStringValue(7, i)));
				}

			}

			imported2SpotX.add(imported1SpotX);
			imported2SpotY.add(imported1SpotY);
			imported2SpotT.add(imported1SpotT);

		}
		String TitleString = "";
		String TitleString2 = "";
		String TitleString3 = "";

		// Set<Integer> trackIDs = SPTBatch_.model.getTrackModel().trackIDs(true);

		// avgInstantDif = new ArrayList<Double>();
		avgScaledInstantDif = new ArrayList<Double>();
		// alphaToClassify = new ArrayList<Double>();
		alphaToClassifyScaled = new ArrayList<Double>();
		mssValues = new ArrayList<Double>();
		List<Double> avgScaled = new ArrayList<Double>();
		List<Double> avg = new ArrayList<Double>();
		// int counter = 0;

		for (int i = 0; i < imported2SpotX.size(); i++) {

			XYSeriesCollection datasetMSS = new XYSeriesCollection();
			// counter++;
			double frameInterval = imported2SpotT.get(i).get(2) - imported2SpotT.get(i).get(1);
			double sx = 0, sy = 0, sz = 0;
			if (SPTBatch_.imps.getCalibration().getUnits() == "pixel") {

				sx = SPTBatch_.imps.getCalibration().pixelWidth;
				sy = SPTBatch_.imps.getCalibration().pixelHeight;
				sz = SPTBatch_.imps.getCalibration().pixelDepth;
				TitleString = "Mean Square Displacement";
				TitleString2 = "Delta (s)";
				TitleString3 = "MSD (" + SPTBatch_.imps.getCalibration().getXUnit() + "^2)";

			}
			if (SPTBatch_.imps.getCalibration().getUnits() != "pixel") {

				sx = 1d;
				sy = 1d;
				sz = 1d;
				TitleString = "Mean Square Displacement";
				TitleString2 = "Delta (frame)";
				TitleString3 = "MSD (pixel^2)";

			}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////scaled

			int nMSD = imported2SpotX.get(i).size();
			double[] msdArray = new double[nMSD];
			double[] timeArray = new double[nMSD];
			int[] tau = new int[nMSD];
			for (int z = 0; z < nMSD - 1 + 1; z++)
				tau[z] = 1 + z;

			for (int z = 0; z < tau.length; z++)
				tau[z] = 1 + z;
			double msd = -1;
			for (int dt = 0; dt < tau.length; dt++) {
				double N = 0;
				msd = 0;

				for (int j = tau[dt]; j < imported2SpotX.get(i).size(); j++) {
					msd = msd
							+ Math.pow((imported2SpotX.get(i).get(j - tau[dt]).doubleValue()
									- imported2SpotX.get(i).get(j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j - tau[dt]).doubleValue()
									- imported2SpotY.get(i).get(j).doubleValue()), 2);

					N++;
				}
				msdArray[dt] = msd / N;
				timeArray[dt] = tau[dt] * frameInterval;
			}
			// msd14_1.add((double) 0);
			/////////////////////////////
			int[] tau14 = new int[4];
			for (int z = 0; z < 4 - 1 + 1; z++)
				tau14[z] = 1 + z;

			for (int z = 0; z < tau14.length; z++) {
				tau14[z] = 1 + z;
			}
			double msd14 = -1;
			SimpleRegression reg = new SimpleRegression(true);
			for (int dt = 0; dt < tau14.length; dt++) {
				double N14 = 0;
				msd14 = 0;
				for (int j = tau14[dt]; j < imported2SpotX.get(i).size(); j++) {
					msd14 = msd14
							+ Math.pow((imported2SpotX.get(i).get(j - tau14[dt]).doubleValue()
									- imported2SpotX.get(i).get(j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j - tau14[dt]).doubleValue()
									- imported2SpotY.get(i).get(j).doubleValue()), 2);

					N14++;

				}
				reg.addData(tau14[dt] * (frameInterval), msd14 / N14);
			}

//////one msd/////////////////
			double sum = 0;
			for (int z = 0; z < msdArray.length; z++) {
				if (Double.valueOf(msdArray[z]).isNaN() == Boolean.FALSE) {
					sum += msdArray[z];

				}
			}
			double msdd = sum / msdArray.length;
///////////////////////////////////////////////////////////////// msd per segment/////////////////////

			PowerLawCurveFitModified pwf = new PowerLawCurveFitModified();
			pwf.doFit(timeArray, msdArray);
			double alphaLong;
			if (Double.valueOf(pwf.getAlpha()).isNaN() == Boolean.TRUE) {
				alphaLong = 0;
			} else {
				alphaLong = pwf.getAlpha();
			}

			double diffusionCoef = reg.getSlope() / 4;
			alphaToClassifyScaled.add(Double.valueOf(Math.abs(alphaLong)));
			avgScaledInstantDif.add(Double.valueOf(Math.abs(diffusionCoef)));
			avgScaled.add(Double.valueOf(msdd));

///////////////////////MSS//////////////////////////

			double[] order = new double[] { 0, 1, 2, 3, 4, 5, 6 };
			double[] scalingCoef = new double[order.length];
			XYSeriesCollection dataset = new XYSeriesCollection();
			for (int o = 0; o < order.length; o++) {
				XYSeries series1 = new XYSeries(o);
				SimpleRegression fdf3 = new SimpleRegression(true);
				double moments = -1;
				for (int dt = 0; dt < tau.length; dt++) {
					double Nmoments = 0;
					moments = 0;

					for (int j = tau[dt]; j < imported2SpotX.get(i).size(); j++) {
						moments = moments
								+ Math.pow((imported2SpotX.get(i).get(j - tau[dt]).doubleValue()
										- imported2SpotX.get(i).get(j).doubleValue()), o)
								+ Math.pow((imported2SpotY.get(i).get(j - tau[dt]).doubleValue()
										- imported2SpotY.get(i).get(j).doubleValue()), o);

						Nmoments++;
					}
					double moment = moments / Nmoments;
					double momentToAdd = -1;
					if (moment == 0) {
						momentToAdd = 0.0;
					} else {
						momentToAdd = Math.log(Math.abs(moment));
					}
					if (Double.valueOf(momentToAdd).isNaN() == Boolean.FALSE) {
						series1.add(Math.log((tau[dt] * frameInterval)), momentToAdd);
						fdf3.addData(Math.log((tau[dt] * frameInterval)), momentToAdd);
					}
				}

				dataset.addSeries(series1);
				scalingCoef[o] = fdf3.getSlope();

			}

			JFreeChart chart1 = ChartFactory.createXYLineChart(
					"log µν(δt) vs. log δt for " + String.valueOf(nOfTracks.get(i)), "log δt", "log µν(δt)",
					(XYDataset) dataset);
			try {
				ChartUtils
						.saveChartAsPNG(
								new File(SPTBatch_.directMSS.getAbsolutePath() + File.separator
										+ "log µν(δt) vs. log δt for " + String.valueOf(nOfTracks.get(i))),
								chart1, 640, 480);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			SimpleRegression fdf2 = new SimpleRegression();
			XYSeries series2 = new XYSeries(nOfTracks.get(i));
			for (int z = 0; z < scalingCoef.length; z++) {
				fdf2.addData(z, scalingCoef[z]);
				series2.add(z, scalingCoef[z]);
			}

			datasetMSS.addSeries(series2);
			JFreeChart chart2 = ChartFactory.createScatterPlot("MSS γν vs.ν for " + String.valueOf(nOfTracks.get(i)),
					"γν", "ν", (XYDataset) datasetMSS);
			try {
				ChartUtils.saveChartAsPNG(new File(SPTBatch_.directMSS.getAbsolutePath() + File.separator
						+ "MSS γν vs.ν for " + String.valueOf(nOfTracks.get(i))), chart2, 640, 480);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			double sMss = Math.abs(fdf2.getSlope());
			mssValues.add(sMss);
		}
////////////////////////////////////////////////////

		return avgScaled;

	}

}
