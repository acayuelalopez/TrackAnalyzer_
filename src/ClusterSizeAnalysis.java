import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramBin;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import edu.mines.jtk.dsp.Histogram;
import ij.IJ;
import ij.io.FileSaver;
import ij.measure.CurveFitter;
import smileModified.GaussianMixtureModified;
import smileModified.MixtureModified;
import smileModified.MixtureModified.Component;

public class ClusterSizeAnalysis {
	List<Double> pdf = new ArrayList<Double>();
	double[] values;
	int BINS;
	HistogramDataset dataset;
	XYPlot plot;
	ChartPanel panel;
	XYBarRenderer renderer;
	Integer nOfTrack;

	public ClusterSizeAnalysis() {

	}

	public void Compute(List<Double> xData, Integer nOfTrack) {
		double[] xDataArray = new double[xData.size()];
		float[] xDataArrayFloat = new float[xData.size()];
		for (int i = 0; i < xData.size(); i++) {
			xDataArray[i] = xData.get(i).doubleValue();
			xDataArrayFloat[i] = xData.get(i).floatValue();
		}
		double meanValue = Double.valueOf(xData.stream().mapToDouble(a -> a).average().getAsDouble());
		Histogram histogram = new Histogram(xDataArrayFloat);
		int BINS = histogram.getBinCount();
		long[] counts = histogram.getCounts();
		float[] densities = histogram.getDensities();
		// createChartPanel(xDataArray, BINS, nOfTrack);
		GaussianMixtureModified gm2 = GaussianMixtureModified.fit(xDataArray);
		int k = gm2.size();
		MixtureModified.Component[] components = gm2.components;
		NormalDistributionMine nd = new NormalDistributionMine(nOfTrack, components, xDataArray.length, meanValue);
		nd.runNormalDistribution();
//		for (int i = 0; i < components.length; i++) {
//			IJ.log("Components:    " + i);
//			IJ.log(components[i].distribution.mean() + "---" + components[i].distribution.sd() + "        -" + i);
//		}

//		HistogramDataset dataset = new HistogramDataset();
//		int bins = new HistogramBin(0, 1400).getCount();
//		dataset.addSeries("intensity", xDataArray, bins);
//		String plotTitle = "Histogram";
//		String xaxis = "number";
//		String yaxis = "value";
//		PlotOrientation orientation = PlotOrientation.VERTICAL;
//		boolean show = false;
//		boolean toolTips = false;
//		boolean urls = false;
//		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips,
//				urls);
//		try {
//			ChartUtils.saveChartAsPNG(new File(SPTBatch_.directCluster.getAbsolutePath() + File.separator
//					+ SPTBatch_.imps.getShortTitle() + "_Histogram_" + nOfTrack.toString() + ".png"),
//					chart, 500, 400);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
