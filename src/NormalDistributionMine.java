
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import ij.measure.ResultsTable;
import smileModified.MixtureModified;
import smileModified.MixtureModified.Component;

/**
 * This demo shows a normal distribution function.
 */
public class NormalDistributionMine {
	Integer nOfTrack;
	MixtureModified.Component[] components;
	int samples;
	double meanValue;

	public NormalDistributionMine(Integer nOfTrack, MixtureModified.Component[] components, int samples,
			double meanValue) {
		this.nOfTrack = nOfTrack;
		this.components = components;
		this.samples = samples;
		this.meanValue = meanValue;

	}

	public void runNormalDistribution() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		ResultsTable rtMonomer = new ResultsTable();
		String[] columnNames = new String[] { "Subpopulation", "\u03BC", "\u03C3", "Entropy", "N of Parameters",
				"Variance", "N Receptor/Particle", "Monomer Intensity", "Track Mean Intensity" };
		double monomerValue = 0;
		for (int i = 0; i < components.length; i++) {
			Function2D n1 = new NormalDistributionFunction2D(components[i].distribution.mean(),
					components[i].distribution.sd());
			XYSeries s1 = DatasetUtils.sampleFunction2DToSeries(n1,
					(components[i].distribution.mean() - (3 * components[i].distribution.sd())),
					(components[i].distribution.mean() + (3 * components[i].distribution.sd())), samples,
					"subpop-" + i + " : " + "\u03BC = " + String.format("%.3f", components[i].distribution.mean()) + ","
							+ "\u03C3 = " + String.format("%.3f", components[i].distribution.sd()));
			dataset.addSeries(s1);
			rtMonomer.setValue("Subpopulation", i, String.valueOf(i + 1));
			rtMonomer.setValue("\u03BC", i, String.format("%.3f", components[i].distribution.mean()));
			rtMonomer.setValue("\u03C3", i, String.format("%.3f", components[i].distribution.sd()));
			rtMonomer.setValue("Entropy", i, String.format("%.3f", components[i].distribution.entropy()));
			rtMonomer.setValue("N of Parameters", i, components[i].distribution.length());
			rtMonomer.setValue("Variance", i, String.format("%.3f", components[i].distribution.variance()));

			if (SPTBatch_.checkMonomer.isSelected() == Boolean.FALSE) {
				if (components.length == 2)
					monomerValue = Math.abs(components[0].distribution.mean() - components[1].distribution.mean());

			}
		}
		if (SPTBatch_.checkMonomer.isSelected() == Boolean.TRUE)
			rtMonomer.setValue("N Receptor/Particle", 0,
					String.valueOf(meanValue / Double.valueOf(SPTBatch_.monomerField.getText()).doubleValue()));
		if (SPTBatch_.checkMonomer.isSelected() == Boolean.FALSE) {
			if (monomerValue == 0) {
				rtMonomer.setValue("N Receptor/Particle", 0, "1");
				monomerValue = components[0].distribution.mean();
			} else {
				rtMonomer.setValue("N Receptor/Particle", 0, String.valueOf((meanValue / monomerValue)));
			}
		}
		if (SPTBatch_.checkMonomer.isSelected() == Boolean.FALSE)
			rtMonomer.setValue("Monomer Intensity", 0, monomerValue);
		if (SPTBatch_.checkMonomer.isSelected() == Boolean.TRUE)
			rtMonomer.setValue("Monomer Intensity", 0, SPTBatch_.monomerField.getText());
		rtMonomer.setValue("Track Mean Intensity", 0, String.valueOf(meanValue));
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Distribution of Single Integrated Intensities for Track-" + nOfTrack.toString(),
				"Integrated Intensity", "Probability Density [nmolecule-1]", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainZeroBaselineVisible(true);
		plot.setRangeZeroBaselineVisible(true);
		plot.setDomainPannable(true);
		plot.setRangePannable(true);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainMinorGridlinePaint(Color.GRAY);
		plot.setDomainGridlinePaint(Color.DARK_GRAY);
		plot.setRangeMinorGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.DARK_GRAY);
		ValueAxis xAxis = plot.getDomainAxis();
		xAxis.setLowerMargin(0.0);
		xAxis.setUpperMargin(0.0);
		XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
		r.setDrawSeriesLineAsPath(true);
		for (int i = 0; i < dataset.getSeriesCount(); i++) {
			r.setSeriesStroke(i, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f));
		}

		try {
			rtMonomer.saveAs(SPTBatch_.directCluster.getAbsolutePath() + File.separator + SPTBatch_.imps.getShortTitle()
					+ "_Cluster_Analysis_" + nOfTrack.toString() + ".csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ChartUtils.saveChartAsPNG(new File(SPTBatch_.directCluster.getAbsolutePath() + File.separator
					+ SPTBatch_.imps.getShortTitle() + "_DistributionDensityFunction_" + nOfTrack.toString() + ".png"),
					chart, 500, 400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}