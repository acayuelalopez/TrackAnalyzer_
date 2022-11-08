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

//			double[] msd = new double[imported2Spot.get(i).size()];
//			int N = 0;
//			double msdd = 0;
//			for (int dt = 1; dt < imported2Spot.get(i).size(); dt++) {
//				msd[dt] = 0d;
//				for (int j = 1; j <= dt; j++) {
//					msd[dt] += Math.pow(
//							(Math.abs(imported2Spot.get(i).get(j).getFeature(Spot.POSITION_X).doubleValue()
//									- imported2Spot.get(i).get(dt + j).getFeature(Spot.POSITION_X).doubleValue()) * sx),
//							2)
//							+ Math.pow((Math.abs(imported2Spot.get(i).get(j).getFeature(Spot.POSITION_Y).doubleValue()
//									- imported2Spot.get(i).get(dt + j).getFeature(Spot.POSITION_Y).doubleValue()) * sy),
//									2)
//							+ Math.pow((Math.abs(imported2Spot.get(i).get(j).getFeature(Spot.POSITION_Z).doubleValue()
//									- imported2Spot.get(i).get(dt + j).getFeature(Spot.POSITION_Z).doubleValue()) * sz),
//									2);
//
//					msdd = msdd
//							+ Math.pow((Math.abs(imported2Spot.get(i).get(j).getFeature(Spot.POSITION_X).doubleValue()
//									- imported2Spot.get(i).get(dt + j).getFeature(Spot.POSITION_X).doubleValue()) * sx),
//									2)
//							+ Math.pow((Math.abs(imported2Spot.get(i).get(j).getFeature(Spot.POSITION_Y).doubleValue()
//									- imported2Spot.get(i).get(dt + j).getFeature(Spot.POSITION_Y).doubleValue()) * sy),
//									2)
//							+ Math.pow((Math.abs(imported2Spot.get(i).get(j).getFeature(Spot.POSITION_Z).doubleValue()
//									- imported2Spot.get(i).get(dt + j).getFeature(Spot.POSITION_Z).doubleValue()) * sz),
//									2);
//
//				}
//
//				N++;
//				msd[dt] /= dt;
//				timeArray[dt] = dt * SPTBatch_.fps;
//
//			}
//			msdd = msdd / N;
			int nMSD = imported2SpotX.get(i).size();
			double[] timeAlpha = new double[nMSD];
			double[] dt_n = new double[nMSD];
			double[][] msd = new double[nMSD][2];
//			double[] dt_n14 = new double[nMSD];
			ArrayList<Double> msd14_0 = new ArrayList<Double>();
			ArrayList<Double> msd14_1 = new ArrayList<Double>();
			ArrayList<Double> msd14_2 = new ArrayList<Double>();
			ArrayList<Double> dt_n14 = new ArrayList<Double>();
//			double[][] msd14 = new double[nMSD][2];
			double N = 0;
			double msd14 = 0;

			for (int dt = 1; dt < nMSD; dt++) {
				msd[dt][0] = dt * 1;

				for (int j = 0; j + dt < imported2SpotX.get(i).size(); j += dt) {
					msd[dt][1] += Math
							.pow((imported2SpotX.get(i).get(j).doubleValue()
									- imported2SpotX.get(i).get(dt + j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j).doubleValue()
									- imported2SpotY.get(i).get(dt + j).doubleValue()), 2);

					dt_n[dt]++;
				}
			}
			// msd14_1.add((double) 0);
			/////////////////////////////
			for (int dt = 1; dt < 5; dt++) {
				msd14_0.add((double) (dt * 1));

				for (int j = 0; j + dt < imported2SpotX.get(i).size(); j += dt) {
					msd14 = msd14
							+ Math.pow((imported2SpotX.get(i).get(j).doubleValue()
									- imported2SpotX.get(i).get(dt + j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j).doubleValue()
									- imported2SpotY.get(i).get(dt + j).doubleValue()), 2);

					msd14_1.add(msd14);
					N++;
					dt_n14.add(N);
				}
			}

			for (int dt = 1; dt < nMSD; dt++)
				msd[dt][1] = (dt_n[dt] != 0) ? msd[dt][1] / dt_n[dt] : 0;
			msd14_2.add((double) 0);
			for (int dt = 1; dt < dt_n14.size(); dt++)
				msd14_2.add((dt_n14.get(dt) != 0) ? msd14_1.get(dt) / dt_n14.get(dt) : 0);
			double[] resultmsd = new double[nMSD];
			double[] resultmsd14 = new double[msd14_2.size()];

			resultmsd[0] = 0;
			resultmsd14[0] = 0;
			for (int dt = 1; dt < nMSD; dt++)
				resultmsd[dt] = msd[dt][1];

			for (int dt = 1; dt < msd14_2.size(); dt++)
				resultmsd14[dt] = msd14_2.get(dt);

			double sum = 0;
			for (int dt = 0; dt < resultmsd.length; dt++)
				sum += resultmsd[dt];

			double msdd = sum / resultmsd.length;

			double[] timeDiff = new double[dt_n14.size()];
			timeAlpha[0] = 0;
			for (int dt = 1; dt < dt_n.length; dt++)
				timeAlpha[dt] = dt * frameInterval;
			timeDiff[0] = 0;
			for (int dt = 1; dt < dt_n14.size(); dt++)
				timeDiff[dt] = dt * frameInterval;
//
//			for (int dt = 1; dt < 5; dt++)
//				msdDiff[dt] = resultmsd[dt];

///////////////////////////////////////////////////////////////// msd per segment/////////////////////

			StraightLineFitModified fdf = new StraightLineFitModified();
			fdf.doFit(timeAlpha, resultmsd);
			PowerLawCurveFitModified pwf = new PowerLawCurveFitModified();
			pwf.doFit(timeAlpha, resultmsd);

			double alphaLong;
			if (Double.valueOf(pwf.getAlpha()).isNaN() == Boolean.TRUE) {
				alphaLong = 0;
			} else {
				alphaLong = pwf.getAlpha();
			}
		
			double diffusionCoef = fdf.getB() / (4);
			// IJ.log(pwFit.getAlpha() + "------" + diffusionCoef);
			alphaToClassifyScaled.add(Double.valueOf(alphaLong));
			avgScaledInstantDif.add(Double.valueOf(diffusionCoef));
			avgScaled.add(msdd);

///////////////////////MSS//////////////////////////

			double[] order = new double[] { 0, 1, 2, 3, 4, 5, 6 };

			double[] scalingCoef = new double[order.length];
			XYSeriesCollection dataset = new XYSeriesCollection();
			for (int o = 0; o < order.length; o++) {
				XYSeries series1 = new XYSeries(o);
				StraightLineFitModified fdf3 = new StraightLineFitModified();

				int nMoments = imported2SpotX.get(i).size();
				double[] dt_nMoment = new double[nMoments];
				double[][] moments = new double[nMoments][2];

				for (int dt = 1; dt < nMoments; dt++) {

					moments[dt][0] = dt * 1;
					for (int j = 0; j + dt < imported2SpotX.get(i).size(); j += dt) {
						moments[dt][1] += Math
								.pow((imported2SpotX.get(i).get(j).doubleValue()
										- imported2SpotX.get(i).get(dt + j).doubleValue()), order[o])
								+ Math.pow((imported2SpotY.get(i).get(j).doubleValue()
										- imported2SpotY.get(i).get(dt + j).doubleValue()), order[o]);
						dt_nMoment[dt]++;
					}
				}

				// }

				for (int dt = 1; dt < nMoments; dt++)
					moments[dt][1] = (dt_nMoment[dt] != 0) ? moments[dt][1] / dt_nMoment[dt] : 0;

				double[] resultmoments = new double[nMoments];
				double[] time = new double[nMoments];
				resultmoments[0] = 0;
				time[0] = 0;
				for (int dt = 1; dt < nMoments; dt++) {
					double value = moments[dt][1];
					if (value == 0) {
						resultmoments[dt] = 0.0;
					} else {
						//IJ.log(value + "-----" + i + "-------------bueno");
						resultmoments[dt] = Math.log(Math.abs(value));
					}
					//IJ.log(resultmoments[dt] + "-----" + i + "-------------final");

				}

				for (int dt = 1; dt < nMoments; dt++) {
//					if (Double.isInfinite(Math.log(resultmoments[dt])) == true) {
//
//						sreg.addData(Math.log((dt * frameInterval)), 0.0);
//						series1.add(Math.log((dt * frameInterval)), 0.0);
//					} else {
					// sreg.addData(Math.log((dt * frameInterval)), Math.log(resultmoments[dt]));
					time[dt] = Math.log((dt * frameInterval));
					series1.add(Math.log((dt * frameInterval)), resultmoments[dt]);
					// }

				}
				fdf3.doFit(time, resultmoments);
				dataset.addSeries(series1);
				scalingCoef[o] = fdf3.getB();

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
			StraightLineFitModified fdf2 = new StraightLineFitModified();
			XYSeries series2 = new XYSeries(nOfTracks.get(i));
//			for (int z = 0; z < scalingCoef.size(); z++) {
//				sreg.addData(z, scalingCoef.get(z));
//				series2.add(z, scalingCoef.get(z));
//			}
			fdf2.doFit(order, scalingCoef);
			datasetMSS.addSeries(series2);
			JFreeChart chart2 = ChartFactory.createScatterPlot("MSS γν vs.ν for " + String.valueOf(nOfTracks.get(i)),
					"γν", "ν", (XYDataset) datasetMSS);
			try {
				ChartUtils.saveChartAsPNG(new File(SPTBatch_.directMSS.getAbsolutePath() + File.separator
						+ "MSS γν vs.ν for " + String.valueOf(nOfTracks.get(i))), chart2, 640, 480);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			double sMss = Math.abs(fdf2.getB());
			mssValues.add(sMss);
		}
////////////////////////////////////////////////////

		return avgScaled;

	}

}
