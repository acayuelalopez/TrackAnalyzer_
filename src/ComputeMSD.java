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

import de.biomedical_imaging.traj.math.PowerLawCurveFit;
import de.biomedical_imaging.traj.math.StraightLineFit;
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
import plugins.fab.trackmanager.PluginTrackManagerProcessor;
import plugins.fab.trackmanager.TrackSegment;
import plugins.nchenouard.spot.Detection;

public class ComputeMSD {
	static List<Double> avgScaledInstantDif, alphaToClassifyScaled, mssValues;
	SimpleRegression sreg;

	public ComputeMSD() {

	}

	public List<Double> Compute() {

		// XYSeriesCollection xyDataset = new XYSeriesCollection();

		XYSeries series = null;

		List<List<Spot>> imported2Spot = new ArrayList<List<Spot>>();
		Set<Integer> trackIDs = SPTBatch_.model.getTrackModel().trackIDs(true);

		for (Integer id : trackIDs) {
			List<Spot> imported1Spot = new ArrayList<Spot>();
			Set<Spot> track = SPTBatch_.model.getTrackModel().trackSpots(id);
			List<Spot> spots = new ArrayList<Spot>();
			for (Spot spot : track) {

				spots.add(spot);

			}

			for (int y = 0; y < spots.size(); y++)
				imported1Spot.add(spots.get(y));

			imported2Spot.add(imported1Spot);

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
		int counter = 0;

		for (Integer id : trackIDs) {
			XYSeriesCollection datasetMSS = new XYSeriesCollection();
			counter++;

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

			int nMSDScaled = imported2Spot.get(counter - 1).size();
			double msdPerTrack = 0;
//
//			double[] dt_n = new double[nMSDScaled];
//			double[][] msd = new double[nMSDScaled][2];
//
//			for (int dt = 1; dt < nMSDScaled; dt++) {
//				msd[dt][0] = dt * 1;
//
//				for (int j = 0; j + dt < nMSDScaled; j += dt) {
//					msd[dt][1] += Math
//							.pow((Math
//									.abs(imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_X).doubleValue()
//											- imported2Spot.get(counter - 1).get(j + dt).getFeature(Spot.POSITION_X)
//													.doubleValue())
//									* sy), 2)
//							+ Math.pow((Math
//									.abs(imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_Y).doubleValue()
//											- imported2Spot.get(counter - 1).get(j + dt).getFeature(Spot.POSITION_Y)
//													.doubleValue())
//									* sy), 2);
//					dt_n[dt]++;
//				}
//			}
//
//			for (int dt = 1; dt < nMSDScaled; dt++)
//				msd[dt][1] = (dt_n[dt] != 0) ? msd[dt][1] / dt_n[dt] : 0;
			double[] msd = new double[nMSDScaled];

	        for (int t = 1; t < nMSDScaled; t++)
	        {
	            msd[t] = 0d;
	            for (int j = 1; j <= t; j++)
	                msd[t] += Math
							.pow((Math
							.abs(imported2Spot.get(counter - 1).get(0).getFeature(Spot.POSITION_X).doubleValue()
									- imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_X)
											.doubleValue())
							* sy), 2)
					+ Math.pow((Math
							.abs(imported2Spot.get(counter - 1).get(0).getFeature(Spot.POSITION_Y).doubleValue()
									- imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_Y)
											.doubleValue())
							* sy), 2);
	            msd[t] /= t;
	        }



			double[] timeDiff = new double[5];
			double resultmsd = 0;
			double[] msdArray = new double[nMSDScaled];
			double[] timeArray = new double[nMSDScaled];
			double[] msdDiff = new double[5];
			double NtoDivide = 0;

			//msdDiff[0] = 0;
			//timeDiff[0] = 0;
			for (int dt = 1; dt < 5; dt++) {
				msdDiff[dt] = msd[dt];
				timeDiff[dt] = dt * SPTBatch_.fps;

			}

			//msdArray[0] = 0;
			//timeArray[0] = 0;
			for (int dt = 1; dt < nMSDScaled; dt++) {
				NtoDivide++;
				msdArray[dt] = msd[dt];
				timeArray[dt] = dt * SPTBatch_.fps;
				resultmsd += msdArray[dt];
			}

			msdPerTrack = resultmsd / msdArray.length;

///////////////////////////////////////////////////////////////// msd per segment/////////////////////

//			StraightLineFit fdfDiff = new StraightLineFit();
//			fdfDiff.doFit(timeDiff, msdDiff);
			
			PowerLawCurveFit pwFit = new PowerLawCurveFit();
			pwFit.doFit(timeArray, msdArray);
			double alphaLong = pwFit.getAlpha();
			double diffusionCoef = pwFit.getDiffusionCoefficient();
			alphaToClassifyScaled.add(Double.valueOf(alphaLong));
			avgScaledInstantDif.add(Double.valueOf(diffusionCoef));
			avgScaled.add(msdPerTrack);

///////////////////////MSS//////////////////////////

			double[] order = new double[] { 0, 1, 2, 3, 4, 5, 6 };

			List<Double> scalingCoef = new ArrayList<Double>();
			XYSeriesCollection dataset = new XYSeriesCollection();
			for (int o = 0; o < order.length; o++) {
				XYSeries series1 = new XYSeries(o);
				sreg = new SimpleRegression(true);
//				double[] dt_nMoments = new double[nMSDScaled];
//				double[][] moments = new double[nMSDScaled][2];
//
//				for (int dt = 1; dt < nMSDScaled; dt++) {
//					moments[dt][0] = dt * 1;
//
//					for (int j = 0; j + dt < nMSDScaled; j += dt) {
//						moments[dt][1] += Math
//								.pow((Math.abs(
//										imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_X).doubleValue()
//												- imported2Spot.get(counter - 1).get(j + dt).getFeature(Spot.POSITION_X)
//														.doubleValue())
//										* sx), order[o])
//								+ Math.pow((Math.abs(
//										imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_Y).doubleValue()
//												- imported2Spot.get(counter - 1).get(j + dt).getFeature(Spot.POSITION_Y)
//														.doubleValue())
//										* sy), order[o]);
//						dt_nMoments[dt]++;
//					}
//				}
//
//				for (int dt = 1; dt < nMSDScaled; dt++)
//					moments[dt][1] = (dt_nMoments[dt] != 0) ? moments[dt][1] / dt_nMoments[dt] : 0;
//
//				double[] momentsArray = new double[nMSDScaled];
//				double[] timeArrayMoments = new double[nMSDScaled];
//				momentsArray[0] = 0;
//				timeArrayMoments[0] = 0;
				
				
				double[] moments = new double[nMSDScaled];

		        for (int t = 1; t < nMSDScaled; t++)
		        {
		        	moments[t] = 0d;
		            for (int j = 1; j <= t; j++)
		            	moments[t] += Math
								.pow((Math
								.abs(imported2Spot.get(counter - 1).get(0).getFeature(Spot.POSITION_X).doubleValue()
										- imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_X)
												.doubleValue())
								* sy), order[o])
						+ Math.pow((Math
								.abs(imported2Spot.get(counter - 1).get(0).getFeature(Spot.POSITION_Y).doubleValue()
										- imported2Spot.get(counter - 1).get(j).getFeature(Spot.POSITION_Y)
												.doubleValue())
								* sy), order[o]);
		            moments[t] /= t;
		       // }
				
				
				//for (int dt = 1; dt < nMSDScaled; dt++) {
					//momentsArray[dt] = moments[dt][1];
					//timeArrayMoments[dt] = dt * (SPTBatch_.fps);
					if (Double.isInfinite(Math.log( moments[t])) == true) {

						sreg.addData(Math.log(t * (SPTBatch_.fps)), 0.0);
						series1.add(Math.log(t * (SPTBatch_.fps)), 0.0);
					} else {
						sreg.addData(Math.log(t * (SPTBatch_.fps)), Math.log( moments[t]));
						series1.add(Math.log(t * (SPTBatch_.fps)), Math.log( moments[t]));
					}
				}
				dataset.addSeries(series1);
				scalingCoef.add(sreg.getSlope());
			}

			JFreeChart chart1 = ChartFactory.createXYLineChart("log µν(δt) vs. log δt for " + String.valueOf(id),
					"log δt", "log µν(δt)", (XYDataset) dataset);
			try {
				ChartUtils.saveChartAsPNG(new File(SPTBatch_.directMSS.getAbsolutePath() + File.separator
						+ "log µν(δt) vs. log δt for " + String.valueOf(id)), chart1, 640, 480);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			sreg = new SimpleRegression(true);
			XYSeries series2 = new XYSeries(id);
			for (int i = 0; i < scalingCoef.size(); i++) {
				sreg.addData(i, scalingCoef.get(i));
				series2.add(i, scalingCoef.get(i));
			}
			datasetMSS.addSeries(series2);
			JFreeChart chart2 = ChartFactory.createScatterPlot("MSS γν vs.ν for " + String.valueOf(id), "γν", "ν",
					(XYDataset) datasetMSS);
			try {
				ChartUtils.saveChartAsPNG(new File(SPTBatch_.directMSS.getAbsolutePath() + File.separator
						+ "MSS γν vs.ν for " + String.valueOf(id)), chart2, 640, 480);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			double sMss = sreg.getSlope();
			mssValues.add(sMss);
		}
////////////////////////////////////////////////////

		return avgScaled;

	}

}
