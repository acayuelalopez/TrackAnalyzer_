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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
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
import ij.ImagePlus;
import ij.gui.OvalRoi;
import plugins.fab.trackmanager.PluginTrackManagerProcessor;
import plugins.fab.trackmanager.TrackSegment;
import plugins.nchenouard.spot.Detection;

/**
 * @author Fabrice de Chaumont
 */
public class PhotobleachingSpotPlot {
	JFreeChart chart;
	JPanel chartpanel = new JPanel();

	public PhotobleachingSpotPlot() {

		chartpanel.add(new ChartPanel(chart, 500, 300, 500, 300, 500, 300, false, false, true, true, true, true));

	}

	public void Compute() {

		// for (int j = ((int) minTracksJTF); j < ((int) maxTracksJTF); j++);
		Set<Integer> trackIDs = SPTBatch_.model.getTrackModel().trackIDs(true);

		if (SPTBatch_.checkboxSubBg.isSelected() == true) {

			for (int t = 0; t < SPTBatch_.trackJTable.getRowCount(); t++)
				SPTBatch_.nOfTracks.add(Integer.valueOf(SPTBatch_.trackJTable.getValueAt(t, 2).toString()));
			for (int r = 0; r < SPTBatch_.nOfTracks.size(); r++) {
				XYSeriesCollection xyDataset = new XYSeriesCollection();
				XYSeries seriesTrack = null;
				XYSeries seriesBg = null;
				List<Double> perTrack = new ArrayList<Double>();
				List<Double> perFrame = new ArrayList<Double>();
				List<Double> perTrackDef = new ArrayList<Double>();
				List<Double> perXPositionDef = new ArrayList<Double>();
				List<Double> perYPositionDef = new ArrayList<Double>();
				List<Double> perFrameDef = new ArrayList<Double>();

				for (int t = 0; t < SPTBatch_.tableSpot.getRowCount(); t++) {
					if (Integer.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 2).toString())
							.equals(SPTBatch_.nOfTracks.get(r)) == Boolean.TRUE) {
						perTrack.add(Double.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 12).toString()));
						perFrame.add(Double.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 8).toString()));
						// if (SPTBatch_.checkTracks.isSelected() == Boolean.FALSE) {
						perTrackDef.add(Double.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 12).toString()));
						perFrameDef.add(Double.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 8).toString()));
						perXPositionDef.add(Double.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 4).toString()));
						perYPositionDef.add(Double.valueOf(SPTBatch_.tableSpot.getModel().getValueAt(t, 5).toString()));
						// }
					}
				}
//				if (SPTBatch_.checkTracks.isSelected() == Boolean.TRUE) {
//
//					for (int j = ((int) SPTBatch_.minTracksJTF); j < ((int) SPTBatch_.maxTracksJTF); j++) {
//
//						perTrackDef.add(perTrack.get(j));
//						perFrameDef.add(perFrame.get(j));
//
//					}
//				}

				seriesTrack = new XYSeries("Spot Mean Raw Intensity Track ");
				seriesBg = new XYSeries("Bg Intensity");

//				if (perFrameDef.get(0).intValue() != 0) {
//					for (int i = 0; i < perFrameDef.get(0); i++)
//						series.add(i, Double.valueOf(SPTBatch_.slicesIntensitySpot[i]));
//				}
				for (int i = 0; i < perTrackDef.size(); i++)
					seriesTrack.add(perFrameDef.get(i), perTrackDef.get(i));

				for (int i = 0; i < SPTBatch_.imps.getStack().getSize(); i++)
					seriesBg.add(i, Double.valueOf(SPTBatch_.slicesIntensitySpot[i]));

				if (perFrameDef.get(perFrameDef.size() - 1).intValue() != SPTBatch_.imps.getStack().getSize() - 1) {
					ImagePlus[] slices = SPTBatch_.stack2images(SPTBatch_.imps.duplicate());
					OvalRoi ovalRoi = new OvalRoi(perXPositionDef.get(perXPositionDef.size() - 1).doubleValue(),
							perXPositionDef.get(perXPositionDef.size() - 1).doubleValue(),
							(Double.valueOf(SPTBatch_.RADIUS).doubleValue()
									/ SPTBatch_.imps.getCalibration().pixelWidth),
							(Double.valueOf(SPTBatch_.RADIUS).doubleValue()
									/ SPTBatch_.imps.getCalibration().pixelWidth));

					for (int i = perFrameDef.get(perFrameDef.size() - 1).intValue(); i < SPTBatch_.imps.getStack()
							.getSize(); i++) {
						slices[i].setRoi(ovalRoi);
						seriesTrack.add(i, slices[i].getStatistics().mean);

					}
				}
				// }
				xyDataset.addSeries(seriesTrack);
				xyDataset.addSeries(seriesBg);
				chart = ChartFactory.createXYLineChart("PhotoBleaching step for Track " + SPTBatch_.nOfTracks.get(r), // chart
																														// title
						"Frame", // x axis label
						"Intensity", // y axis label
						xyDataset, // data
						PlotOrientation.VERTICAL, true, // include legend
						true, // tooltips
						false // urls
				);

				chartpanel.removeAll();

				if (chart != null) {
					// replace default chart colors by detection colors (taken from t=0)
					XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();

					PerTrackFeatureColorGenerator tcg = new PerTrackFeatureColorGenerator(SPTBatch_.model,
							TrackIndexAnalyzer.TRACK_INDEX, null, null, Colormap.Turbo, 0, 1);
					XYPlot plot = chart.getXYPlot();
//				    final NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
//			        xAxis.setTickUnit(new NumberTickUnit(1));
					plot.setDataset(0, xyDataset);
					plot.setRenderer(0, renderer);
					plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0,
							tcg.colorOf(SPTBatch_.nOfTracks.get(r)));

				}
				chartpanel.add(new ChartPanel(chart));

				chartpanel.updateUI();
				try {
					ChartUtils.saveChartAsPNG(new File(SPTBatch_.directPBS.getAbsolutePath() + File.separator
							+ SPTBatch_.imgTitle + "_" + SPTBatch_.nOfTracks.get(r).toString() + ".png"), chart, 2000,
							400);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	Rectangle2D chartRectangleInSequence = new Rectangle2D.Float(250, 20 + 260 * 0, 490, 240);

	JLabel outLabel = new JLabel();

	public void keyPressed(Point p, KeyEvent e) {

	}

	public void mouseClick(Point p, MouseEvent e) {

	}

	public void mouseDrag(Point p, MouseEvent e) {

	}

	public void mouseMove(Point p, MouseEvent e) {

	}

}
