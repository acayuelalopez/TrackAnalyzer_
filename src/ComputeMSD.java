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
import org.jfree.chart.axis.LogarithmicAxis;
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
import ij.IJ;
import ij.measure.CurveFitter;
import ij.measure.Minimizer;
import ij.measure.ResultsTable;
import math.PowerLawCurveFitModified;
import math.StraightLineFitModified;
import plugins.nchenouard.spot.Detection;

public class ComputeMSD {
	static List<Double> d1NValues, alphaValues, diffValues, msd1Values, msd2Values, msd3Values, msdValues, mssValues;
	int nOfPoints;

	public ComputeMSD(int nOfPoints) {
		this.nOfPoints = nOfPoints;
	}

	public void Compute(List<Integer> nOfTracks, ResultsTable rtSpots) {

		List<List<Double>> imported2SpotX = new ArrayList<List<Double>>();
		List<List<Double>> imported2SpotY = new ArrayList<List<Double>>();
		List<List<Double>> imported2SpotT = new ArrayList<List<Double>>();
		List<List<Double>> imported2SpotF = new ArrayList<List<Double>>();
		List<Integer> trackName = new ArrayList<Integer>();

		for (int id = 0; id < nOfTracks.size(); id++) {
			trackName.add(nOfTracks.get(id));
			List<Double> imported1SpotX = new ArrayList<Double>();
			List<Double> imported1SpotY = new ArrayList<Double>();
			List<Double> imported1SpotT = new ArrayList<Double>();
			List<Double> imported1SpotF = new ArrayList<Double>();

			for (int i = 0; i < rtSpots.size(); i++) {
				if (rtSpots.getStringValue(2, i).equals(String.valueOf(nOfTracks.get(id).intValue())) == Boolean.TRUE) {
					// IJ.log(rtSpots.getStringValue(2, i)+"------"+rtSpots.getStringValue(4, i));
					imported1SpotX.add(Double.valueOf(rtSpots.getStringValue(4, i)));
					imported1SpotY.add(Double.valueOf(rtSpots.getStringValue(5, i)));
					imported1SpotT.add(Double.valueOf(rtSpots.getStringValue(7, i)));
					imported1SpotF.add(Double.valueOf(rtSpots.getStringValue(8, i)));
					// IJ.log("x: "+Double.valueOf(rtSpots.getStringValue(4, i)));
				}

			}

			imported2SpotX.add(imported1SpotX);
			imported2SpotY.add(imported1SpotY);
			imported2SpotT.add(imported1SpotT);
			imported2SpotF.add(imported1SpotF);

		}

		diffValues = new ArrayList<Double>();
		d1NValues = new ArrayList<Double>();
		alphaValues = new ArrayList<Double>();
		msd1Values = new ArrayList<Double>();
		msd2Values = new ArrayList<Double>();
		msd3Values = new ArrayList<Double>();
		msdValues = new ArrayList<Double>();
		mssValues = new ArrayList<Double>();

		for (int i = 0; i < imported2SpotX.size(); i++) {

			XYSeriesCollection datasetMSS = new XYSeriesCollection();
			double frameInterval = imported2SpotT.get(i).get(2) - imported2SpotT.get(i).get(1);
			int nMSD = imported2SpotX.get(i).size();
			int[] tau = new int[4];
			for (int z = 0; z < 4 - 1 + 1; z++)
				tau[z] = 1 + z;
			double[] msdArray = new double[tau.length];
			double[] timeArray = new double[tau.length];
			double[] DArray = new double[tau.length];
			double msd = -1;

			double msdhelp123 = 0;
			double msdhelp1 = 0;
			double msdhelp2 = 0;
			double msdhelp3 = 0;
			for (int dt = 0; dt < tau.length; dt++) {
				double N = 0;
				msd = 0;

				for (int j = tau[dt]; j < imported2SpotX.get(i).size(); j++) {
					msd = msd + ((Math
							.pow((imported2SpotX.get(i).get(j - tau[dt]).doubleValue()
									- imported2SpotX.get(i).get(j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j - tau[dt]).doubleValue()
									- imported2SpotY.get(i).get(j).doubleValue()), 2))
//							/ Math.abs((imported2SpotF.get(i).get(j - tau[dt]).doubleValue()
//									- imported2SpotF.get(i).get(j).doubleValue()))
					);

					N++;

				}

				msd = msd / N;
				msdhelp123 = msd;
				if (tau[dt] == 1)
					msdhelp1 = msd;
				if (tau[dt] == 2)
					msdhelp2 = msd;
				if (tau[dt] == 3)
					msdhelp3 = msd;
				msdArray[dt] = msd;
				DArray[dt] = msd / (4 * tau[dt] * frameInterval);
				timeArray[dt] = ((double) tau[dt] * frameInterval);

			}

			double sum = 0;
			for (int x = 0; x < msdArray.length; x++)
				sum += msdArray[x];
			double sumD = 0;
			for (int x = 0; x < DArray.length - 1; x++)
				sumD += DArray[x];
			double DAvg = sumD / (DArray.length - 1);
			double msdT = -1;
			SimpleRegression regMSD = new SimpleRegression(true);
			int[] tauMSD = new int[nMSD - 1];
			for (int z = 0; z < nMSD - 1 - 1 + 1; z++)
				tauMSD[z] = 1 + z;
			for (int dt = 0; dt < tauMSD.length; dt++) {
				double NMSD = 0;
				msdT = 0;

				for (int j = tauMSD[dt]; j < imported2SpotX.get(i).size(); j++) {
					msdT = msdT + ((Math
							.pow((imported2SpotX.get(i).get(j - tauMSD[dt]).doubleValue()
									- imported2SpotX.get(i).get(j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j - tauMSD[dt]).doubleValue()
									- imported2SpotY.get(i).get(j).doubleValue()), 2))
//							/ Math.abs((imported2SpotF.get(i).get(j - tau[dt]).doubleValue()
//									- imported2SpotF.get(i).get(j).doubleValue()))
					);

					NMSD++;

				}

				msdT = msdT / NMSD;
				regMSD.addData(((double) tauMSD[dt] * frameInterval), msdT);
			}
			double msdReg = regMSD.getSlope();

			int[] tauD1N = new int[nOfPoints];
			for (int z = 0; z < nOfPoints - 1 + 1; z++)
				tauD1N[z] = 1 + z;
			SimpleRegression regD1N = new SimpleRegression(true);
			double msd1N = -1;
			double msdhelp1N = 0;
			for (int dt1N = 0; dt1N < tauD1N.length; dt1N++) {
				double N1N = 0;
				msd1N = 0;

				for (int j = tauD1N[dt1N]; j < imported2SpotX.get(i).size(); j++) {
					msd1N = msd1N + ((Math
							.pow((imported2SpotX.get(i).get(j - tau[dt1N]).doubleValue()
									- imported2SpotX.get(i).get(j).doubleValue()), 2)
							+ Math.pow((imported2SpotY.get(i).get(j - tau[dt1N]).doubleValue()
									- imported2SpotY.get(i).get(j).doubleValue()), 2))
//							/ Math.abs((imported2SpotF.get(i).get(j - tau[dt]).doubleValue()
//									- imported2SpotF.get(i).get(j).doubleValue()))
					);

					N1N++;

				}

				msd1N = msd1N / N1N;
				msdhelp1N = msd1N;

				regD1N.addData(((double) tauD1N[dt1N] * frameInterval), msdhelp1N);

			}

			double slopeDiff1N = regD1N.getSlope() / nOfPoints;

///////////////////////////////////////////////////////////////// msd per segment/////////////////////

			PowerLawCurveFitModified pwf = new PowerLawCurveFitModified();
			pwf.doFit(timeArray, msdArray);
			alphaValues.add(Double.valueOf(Math.abs(pwf.getAlpha())));
			diffValues.add(Double.valueOf(Math.abs(DAvg)));
			d1NValues.add(Double.valueOf(Math.abs(slopeDiff1N)));
			msdValues.add(Double.valueOf(Math.abs(msdReg)));
			msd1Values.add(Double.valueOf(Math.abs(msdhelp1)));
			msd2Values.add(Double.valueOf(Math.abs(msdhelp2)));
			msd3Values.add(Double.valueOf(Math.abs(msdhelp3)));

///////////////////////MSS//////////////////////////
			int[] tauMSS = new int[10];
			for (int z = 0; z < 10 - 1 + 1; z++)
				tauMSS[z] = 1 + z;

			double[] order = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
			double[] scalingCoef = new double[order.length];
			XYSeriesCollection dataset = new XYSeriesCollection();
			for (int o = 0; o < order.length; o++) {
				XYSeries series1 = new XYSeries(order[o]);
				SimpleRegression regMSS = new SimpleRegression(true);
				double momenthelp = 0;
				double moments = -1;
				for (int dt = 0; dt < tauMSS.length; dt++) {

					double Nmoments = 0;
					moments = 0;

					for (int j = tauMSS[dt]; j < imported2SpotX.get(i).size(); j++) {

						moments = moments + ((Math
								.pow(Math.abs((imported2SpotX.get(i).get(j - tauMSS[dt]).doubleValue()
										- imported2SpotX.get(i).get(j).doubleValue())), order[o])
								+ Math.pow(Math.abs((imported2SpotY.get(i).get(j - tauMSS[dt]).doubleValue()
										- imported2SpotY.get(i).get(j).doubleValue())), order[o]))
//								/ Math.abs((imported2SpotF.get(i).get(j - tau[dt]).doubleValue()
//										- imported2SpotF.get(i).get(j).doubleValue()))
						);

						Nmoments++;

					}

					if (moments != 0.0) {
						moments = moments / Nmoments;
						momenthelp = moments;
						// if (momenthelp != 0.0) {
						regMSS.addData(Math.log(Math.abs(tauMSS[dt] * frameInterval)), Math.log(momenthelp));
						series1.add(Math.log(Math.abs(tauMSS[dt] * frameInterval)), Math.log(momenthelp));
						// }
					}

				}
///tienque quedar 0.5////////////////////
				dataset.addSeries(series1);
				// IJ.log("track: "+i + "------order: " + order[o] + "----MSS: " +
				// regMSS.getSlope());
				scalingCoef[o] = Math.abs(regMSS.getSlope());

			}

			JFreeChart chart1 = ChartFactory.createXYLineChart(
					"log µѵ,i(nΔt) vs. log nΔt for " + String.valueOf(nOfTracks.get(i)), "log nΔt", "log µѵ,i(nΔt)",
					(XYDataset) dataset);

			try {
				ChartUtils.saveChartAsPNG(
						new File(SPTBatch_.directMSS.getAbsolutePath() + File.separator
								+ "log µѵ,i(nΔt) vs. log nΔt for " + String.valueOf(nOfTracks.get(i))),
						chart1, 640, 480);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			SimpleRegression regScal = new SimpleRegression(true);
			XYSeries series2 = new XYSeries(nOfTracks.get(i));
			for (int z = 0; z < scalingCoef.length; z++) {
				// IJ.log(i + "------" + (z+1) + "----" + scalingCoef.get(z).doubleValue());
				regScal.addData((z + 1), scalingCoef[z]);
				series2.add((z + 1), scalingCoef[z]);
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
			// IJ.log("track: "+i + "------sMSS: " + Math.abs(regScal.getSlope()));
			double sMss = Math.abs(regScal.getSlope());
			mssValues.add(Double.valueOf(sMss));
		}

	}

}
