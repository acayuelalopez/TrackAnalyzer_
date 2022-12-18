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
import ij.measure.ResultsTable;
import plugins.fab.trackmanager.PluginTrackManagerProcessor;
import plugins.fab.trackmanager.TrackSegment;
import plugins.nchenouard.spot.Detection;

/**
 * @author Fabrice de Chaumont
 */
public class TrackProcessorMSD_Modified {
	JFreeChart chart;
	JCheckBox displayLegendCheckBox = new JCheckBox("Display legend.", false);
	JCheckBox displayGraphInSequenceCheckBox = new JCheckBox("Display graph on sequence.", false);
	JButton useRoiAsBoundsForChartButton = new JButton("place graph in ROI #1");
	JCheckBox forceAllSequenceGraphWidthCheckBox = new JCheckBox("Force graph width.", false);
	JCheckBox useRealScalesBox = new JCheckBox("use real scales", false);
	JPanel chartpanel = new JPanel();
	JTextField scaleTextField = new JTextField("1.0");
	JButton exportButton = new JButton("export to console");
	JButton exportExcelButton = new JButton("export to excel");

	public TrackProcessorMSD_Modified() {

		chartpanel.add(new ChartPanel(chart, 500, 300, 500, 300, 500, 300, false, false, true, true, true, true));

	}

	public void Compute(List<Integer> nOfTracks, ResultsTable rtSpots) {

		XYSeriesCollection xyDataset = new XYSeriesCollection();
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
			for (int i = 0; i < rtSpots.size(); i++) {
				if (rtSpots.getStringValue(2, i).equals(String.valueOf(nOfTracks.get(id).intValue())) == Boolean.TRUE) {
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

		for (int i = 0; i < imported2SpotX.size(); i++) {
			series = new XYSeries("Track " + i);

			if (SPTBatch_.imps.getCalibration().getXUnit() != "pixel") {
				TitleString = "Mean Square Displacement";
				TitleString2 = "Delta (s)";
				TitleString3 = "MSD (" + SPTBatch_.imps.getCalibration().getXUnit() + "^2)";

			}
			if (SPTBatch_.imps.getCalibration().getXUnit() == "pixel") {
				TitleString = "Mean Square Displacement";
				TitleString2 = "Delta (frame)";
				TitleString3 = "MSD (pixel^2)";

			}

			double frameInterval = imported2SpotT.get(i).get(2) - imported2SpotT.get(i).get(1);
			int nMSD = imported2SpotX.get(i).size();
			int[] tau = new int[nMSD - 1];
			double[] msdArray = new double[tau.length];
			for (int z = 0; z < nMSD - 1 - 1 + 1; z++)
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
				msd = msd / N;
				msdArray[dt] = msd;
				series.add(tau[dt] * frameInterval, msd);
			}
			//for (int j = 0; j < msdArray.length; j++)
				

			xyDataset.addSeries(series);
		}

		chart = ChartFactory.createXYLineChart(TitleString, // chart title
				TitleString2, // x axis label
				TitleString3, // y axis label
				xyDataset, // data
				PlotOrientation.VERTICAL, displayLegendCheckBox.isSelected(), // include legend
				true, // tooltips
				false // urls
		);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainMinorGridlinePaint(Color.GRAY);
		plot.setDomainGridlinePaint(Color.DARK_GRAY);
		plot.setRangeMinorGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.DARK_GRAY);
		chartpanel.removeAll();

		if (chart != null) {
			// replace default chart colors by detection colors (taken from t=0)
			XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
			for (Integer id : nOfTracks) {
				PerTrackFeatureColorGenerator tcg = new PerTrackFeatureColorGenerator(SPTBatch_.model,
						TrackIndexAnalyzer.TRACK_INDEX, null, null, Colormap.Turbo, 0, 1);
				renderer.setSeriesPaint(id, tcg.colorOf(id));
			}
		}
		chartpanel.add(new ChartPanel(chart, 400, 300, 400, 300, 400, 300, false, false, true, true, true, true));

		chartpanel.updateUI();
		if (SPTBatch_.checkboxDiff.isSelected() == Boolean.TRUE) {
			try {
				ChartUtils.saveChartAsPNG(new File(SPTBatch_.directDiff.getAbsolutePath() + File.separator
						+ "MSD_curve_" + SPTBatch_.imps.getShortTitle() + ".png"), chart, 500, 400);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (SPTBatch_.checkboxDiff.isSelected() == Boolean.FALSE) {
			try {
				ChartUtils.saveChartAsPNG(new File(SPTBatch_.directImages.getAbsolutePath() + File.separator
						+ "MSD_curve_" + SPTBatch_.imps.getShortTitle() + ".png"), chart, 500, 400);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Rectangle2D chartRectangleInSequence = new Rectangle2D.Float(250, 20 + 260 * 0, 490, 240);

	JLabel outLabel = new JLabel();

	private static double scaledSquaredDistance(Spot spotSource, Spot spotTarget, double sx, double sy, double sz) {
		return Math
				.pow((spotSource.getFeature(Spot.POSITION_X).doubleValue()
						- spotTarget.getFeature(Spot.POSITION_X).doubleValue()) * sx, 2)
				+ Math.pow((spotSource.getFeature(Spot.POSITION_Y).doubleValue()
						- spotTarget.getFeature(Spot.POSITION_Y).doubleValue()) * sy, 2);
//				+ Math.pow((spotSource.getFeature(Spot.POSITION_Z).doubleValue()
//						- spotTarget.getFeature(Spot.POSITION_Z).doubleValue()) * sz, 2);
	}

	public void keyPressed(Point p, KeyEvent e) {

	}

	public void mouseClick(Point p, MouseEvent e) {

	}

	public void mouseDrag(Point p, MouseEvent e) {

	}

	public void mouseMove(Point p, MouseEvent e) {

	}

	public void paint(Graphics2D g, Sequence sequence, IcyCanvas canvas) {

		double scale = Double.parseDouble(scaleTextField.getText());
		double minX = chartRectangleInSequence.getCenterX();
		double minY = chartRectangleInSequence.getCenterY();

		Rectangle2D transformedChartRectangleInSequence = (Rectangle2D) chartRectangleInSequence.clone();
		transformedChartRectangleInSequence.setRect((-chartRectangleInSequence.getWidth() / 2) * (1d / scale),
				(-chartRectangleInSequence.getHeight() / 2) * (1d / scale),
				chartRectangleInSequence.getWidth() * (1d / scale),
				chartRectangleInSequence.getHeight() * (1d / scale));

		Graphics2D g2 = (Graphics2D) g;

		AffineTransform transform = g2.getTransform();
		g2.scale(scale, scale);
		g2.translate(minX * (1d / scale), minY * (1d / scale));

		if (displayGraphInSequenceCheckBox.isSelected())
			chart.draw((Graphics2D) g, transformedChartRectangleInSequence);

		g2.setTransform(transform);

	}

}
