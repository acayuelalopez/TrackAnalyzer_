import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.function.PowerFunction2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo showing one way to fit regression lines to XY data.
 */
public class STScatterPlot extends ApplicationFrame {

	private XYDataset data1;
	List<Double> valuesDomain, valuesRange;
	IntervalMarker markerRange, markerDomain;
	String domainName, rangeName;// domainValue, rangeValue,
	Object[][] data;
	Color[] classColor;
	String label1, label2;
	XYSeriesCollection dataset;
	static XYPlot plot;
	ChartPanel panel;
	XYSeries series1;
	XYLineAndShapeRenderer renderer;
	double maxDomain;
	int filterOrder;

	/**
	 * Creates a new instance of the demo application.
	 * 
	 * @param title the frame title.
	 */
	public STScatterPlot(String title) {
		super(title);

	}

	public ChartPanel createChartPanelPolynomial() {
		return null;

	}

	/**
	 * Creates and returns a sample dataset. The data was randomly generated.
	 * 
	 * @return a sample dataset.
	 */
	public ChartPanel createScatterChartPanelInitial(String label1, String label2, List<Double> valuesDomain,
			List<Double> valuesRange, IntervalMarker markerRange, IntervalMarker markerDomain, Object[][] dataCh2,
			Double[][] dataCh3) {
		this.label1 = label1;
		this.label2 = label2;
		this.valuesDomain = valuesDomain;
		this.valuesRange = valuesRange;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;

		dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("");
		for (int i = 0; i < valuesDomain.size(); i++)
			series1.add(valuesDomain.get(i), valuesRange.get(i));
		dataset.addSeries(series1);

		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot("", "", "", dataset);

		// Changes background color
		plot = (XYPlot) chart.getPlot();
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		panel = new ChartPanel(chart, false);
		panel.setMaximumDrawWidth(4000);
		panel.setPreferredSize(new Dimension(450, 300));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setVisible(false);
		panel.setMouseWheelEnabled(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);

		Font font3 = new Font("Dialog", Font.ITALIC, 9);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);
		return panel;
	}

	public void addScatterPlotSeriesLinear(String domainName, String rangeName, List<Double> valuesDomain,
			List<Double> valuesRange, IntervalMarker markerRange, IntervalMarker markerDomain, Object[][] data,
			Color[] classColor) {

		this.valuesDomain = valuesDomain;
		this.rangeName = rangeName;
		this.domainName = domainName;
		this.valuesRange = valuesRange;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;
//		this.domainValue = domainValue;
//		this.rangeValue = rangeValue;
		this.classColor = classColor;

		panel.removeAll();
		dataset = new XYSeriesCollection();
		series1 = new XYSeries("");
		for (int i = 0; i < valuesDomain.size(); i++)
			series1.add(valuesDomain.get(i), valuesRange.get(i));
		dataset.addSeries(series1);
		double minDomain = Collections.min(valuesDomain);
		double maxDomain = Collections.max(valuesDomain);
		double[] coefficients = Regression.getOLSRegression(dataset, 0);
		Function2D curve = new LineFunction2D(coefficients[0], coefficients[1]);
		XYDataset regressionData = DatasetUtils.sampleFunction2D(curve, minDomain, maxDomain, valuesDomain.size(),
				"Fitted Regression Line");

		JFreeChart chart = ChartFactory.createScatterPlot("", domainName, rangeName, dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer1);
		plot.setBackgroundPaint(new Color(255, 228, 196));
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		plot.setDataset(1, regressionData);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(0, Color.RED);
		plot.setRenderer(1, renderer2);
		Shape cross = new Ellipse2D.Double(0, 0, 5, 5);
		List<String[]> featureLists = new ArrayList<String[]>();
		for (int i = 0; i < ColorEditorSpot.tableC.getModel().getRowCount(); i++)
			featureLists.add(((JLabel) ColorEditorSpot.tableC.getModel().getValueAt(i,
					ColorEditorSpot.tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "")
							.split("<br>"));

		renderer = new XYLineAndShapeRenderer() {
			@Override
			public Paint getItemPaint(int row, int col) {
				Paint cpaint = getItemColor(row, col);
				if (cpaint == null) {
					cpaint = super.getItemPaint(row, col);
				}
				return cpaint;
			}

			public Color getItemColor(int row, int col) {
				double x = dataset.getXValue(row, col);
				double y = dataset.getYValue(row, col);

				try {
					return classColor[col];
				} catch (Exception e) {

				}

				return null;

			}

		};
		plot.setRenderer(renderer);
		renderer.setUseOutlinePaint(true);
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesOutlinePaint(0, Color.black);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(1));
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
		plot.getRangeCrosshairValue();
		plot.getDomainCrosshairValue();

		// Create Panel
		panel.setChart(chart);
		panel.setMaximumDrawWidth(6000);
		panel.setPreferredSize(new Dimension(450, 300));
		panel.setMouseWheelEnabled(true);
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);
		markerRange.setLabel("Low-High");
		markerRange.setLabelFont(new Font("SansSerif", 0, 15));
		markerRange.setLabelPaint(new Color(0, 102, 0));

		Font font3 = new Font("Dialog", Font.BOLD, 12);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		// domain.setRange(minDomain, maxDomain);
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		// range.setRange(minRange, maxRange);
		domain.setAutoRange(true);
		range.setAutoRange(true);
		computeLinearCoefficients(chart, plot, dataset);

	}

	public void addScatterPlotSeriesPower(String domainName, String rangeName, List<Double> valuesDomain,
			List<Double> valuesRange, IntervalMarker markerRange, IntervalMarker markerDomain, Object[][] data,
			Color[] classColor) {

		this.valuesDomain = valuesDomain;
		this.rangeName = rangeName;
		this.domainName = domainName;
		this.valuesRange = valuesRange;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;
//		this.domainValue = domainValue;
//		this.rangeValue = rangeValue;
		this.classColor = classColor;
		panel.removeAll();
		dataset = new XYSeriesCollection();
		series1 = new XYSeries("");
		for (int i = 0; i < valuesDomain.size(); i++)
			series1.add(valuesDomain.get(i), valuesRange.get(i));
		dataset.addSeries(series1);

		double minDomain = Collections.min(valuesDomain);
		double maxDomain = Collections.max(valuesDomain);
		double[] coefficients = Regression.getPowerRegression(dataset, 0);
		Function2D curve = new PowerFunction2D(coefficients[0], coefficients[1]);
		XYDataset regressionData = DatasetUtils.sampleFunction2D(curve, minDomain, maxDomain, valuesDomain.size(),
				"Fitted Regression Line");

		JFreeChart chart = ChartFactory.createScatterPlot("", domainName, rangeName, dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer1);
		plot.setBackgroundPaint(new Color(255, 228, 196));
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		plot.setDataset(1, regressionData);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(0, Color.RED);
		plot.setRenderer(1, renderer2);
		Shape cross = new Ellipse2D.Double(0, 0, 5, 5);
		List<String[]> featureLists = new ArrayList<String[]>();
		for (int i = 0; i < ColorEditorSpot.tableC.getModel().getRowCount(); i++)
			featureLists.add(((JLabel) ColorEditorSpot.tableC.getModel().getValueAt(i,
					ColorEditorSpot.tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "")
							.split("<br>"));

		renderer = new XYLineAndShapeRenderer() {
			@Override
			public Paint getItemPaint(int row, int col) {
				Paint cpaint = getItemColor(row, col);
				if (cpaint == null) {
					cpaint = super.getItemPaint(row, col);
				}
				return cpaint;
			}

			public Color getItemColor(int row, int col) {
				double x = dataset.getXValue(row, col);
				double y = dataset.getYValue(row, col);

				try {
					return classColor[col];
				} catch (Exception e) {

				}

				return null;

			}

		};
		plot.setRenderer(renderer);
		renderer.setUseOutlinePaint(true);
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesOutlinePaint(0, Color.black);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(1));
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
		plot.getRangeCrosshairValue();
		plot.getDomainCrosshairValue();

		// Create Panel
		panel.setChart(chart);
		panel.setMaximumDrawWidth(6000);
		panel.setPreferredSize(new Dimension(450, 300));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);
		markerRange.setLabel("Low-High");
		markerRange.setLabelFont(new Font("SansSerif", 0, 15));
		markerRange.setLabelPaint(new Color(0, 102, 0));

		Font font3 = new Font("Dialog", Font.BOLD, 12);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		// domain.setRange(minDomain, maxDomain);
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		// range.setRange(minRange, maxRange);
		domain.setAutoRange(true);
		range.setAutoRange(true);
		computePowerCoefficients(chart, plot, dataset);

	}

	public void addScatterPlotSeriesPolynomial(String domainName, String rangeName, List<Double> valuesDomain,
			List<Double> valuesRange, IntervalMarker markerRange, IntervalMarker markerDomain, Object[][] data,
			Color[] classColor, int filterOrder) {

		this.valuesDomain = valuesDomain;
		this.rangeName = rangeName;
		this.domainName = domainName;
		this.valuesRange = valuesRange;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;
//		this.domainValue = domainValue;
//		this.rangeValue = rangeValue;
		this.classColor = classColor;
		this.filterOrder = filterOrder;

		panel.removeAll();
		dataset = new XYSeriesCollection();
		series1 = new XYSeries("");
		for (int i = 0; i < valuesDomain.size(); i++)
			series1.add(valuesDomain.get(i), valuesRange.get(i));
		dataset.addSeries(series1);
		double minDomain = Collections.min(valuesDomain);
		double maxDomain = Collections.max(valuesDomain);

		double[] coefficients = Regression.getPolynomialRegression(dataset, 0, filterOrder);
		Function2D curve = new PowerFunction2D(coefficients[0], coefficients[1]);
		XYDataset regressionData = DatasetUtils.sampleFunction2D(curve, minDomain, maxDomain, valuesDomain.size(),
				"Fitted Regression Line");

		JFreeChart chart = ChartFactory.createScatterPlot("", domainName, rangeName, dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer1);
		plot.setBackgroundPaint(new Color(255, 228, 196));
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		plot.setDataset(1, regressionData);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(0, Color.RED);
		plot.setRenderer(1, renderer2);
		Shape cross = new Ellipse2D.Double(0, 0, 5, 5);
		List<String[]> featureLists = new ArrayList<String[]>();
		for (int i = 0; i < ColorEditorSpot.tableC.getModel().getRowCount(); i++)
			featureLists.add(((JLabel) ColorEditorSpot.tableC.getModel().getValueAt(i,
					ColorEditorSpot.tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "")
							.split("<br>"));

		renderer = new XYLineAndShapeRenderer() {
			@Override
			public Paint getItemPaint(int row, int col) {
				Paint cpaint = getItemColor(row, col);
				if (cpaint == null) {
					cpaint = super.getItemPaint(row, col);
				}
				return cpaint;
			}

			public Color getItemColor(int row, int col) {
				double x = dataset.getXValue(row, col);
				double y = dataset.getYValue(row, col);

				try {
					return classColor[col];
				} catch (Exception e) {

				}

				return null;

			}

		};
		plot.setRenderer(renderer);
		renderer.setUseOutlinePaint(true);
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesOutlinePaint(0, Color.black);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(1));
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
		plot.getRangeCrosshairValue();
		plot.getDomainCrosshairValue();

		// Create Panel
		panel.setChart(chart);
		panel.setMaximumDrawWidth(6000);
		panel.setPreferredSize(new Dimension(450, 300));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);
		markerRange.setLabel("Low-High");
		markerRange.setLabelFont(new Font("SansSerif", 0, 15));
		markerRange.setLabelPaint(new Color(0, 102, 0));

		Font font3 = new Font("Dialog", Font.BOLD, 12);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		// domain.setRange(minDomain, maxDomain);
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		// range.setRange(minRange, maxRange);
		domain.setAutoRange(true);
		range.setAutoRange(true);
		computePolynomialCoefficients(chart, plot, dataset, filterOrder);

	}

	public void addScatterPlotSeriesLogarithmic(String domainName, String rangeName, List<Double> valuesDomain,
			List<Double> valuesRange, IntervalMarker markerRange, IntervalMarker markerDomain, Object[][] data,
			Color[] classColor) {

		this.valuesDomain = valuesDomain;
		this.rangeName = rangeName;
		this.domainName = domainName;
		this.valuesRange = valuesRange;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;
//		this.domainValue = domainValue;
//		this.rangeValue = rangeValue;
		this.classColor = classColor;
		this.filterOrder = filterOrder;

		panel.removeAll();
		dataset = new XYSeriesCollection();
		series1 = new XYSeries("");
		for (int i = 0; i < valuesDomain.size(); i++)
			series1.add(valuesDomain.get(i), valuesRange.get(i));
		dataset.addSeries(series1);
		double minDomain = Collections.min(valuesDomain);
		double maxDomain = Collections.max(valuesDomain);
		double[] coefficients = RegressionLE_.getLogarithmicRegression(dataset, 0);
		Function2D curve = new LogarithmicFunction2D(coefficients[0], coefficients[1]);
		XYDataset regressionData = DatasetUtils.sampleFunction2D(curve, minDomain, maxDomain, valuesDomain.size(),
				"Fitted Regression Line");

		JFreeChart chart = ChartFactory.createScatterPlot("", domainName, rangeName, dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer1);
		plot.setBackgroundPaint(new Color(255, 228, 196));
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		plot.setDataset(1, regressionData);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(0, Color.RED);
		plot.setRenderer(1, renderer2);
		Shape cross = new Ellipse2D.Double(0, 0, 5, 5);
		List<String[]> featureLists = new ArrayList<String[]>();
		for (int i = 0; i < ColorEditorSpot.tableC.getModel().getRowCount(); i++)
			featureLists.add(((JLabel) ColorEditorSpot.tableC.getModel().getValueAt(i,
					ColorEditorSpot.tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "")
							.split("<br>"));

		renderer = new XYLineAndShapeRenderer() {
			@Override
			public Paint getItemPaint(int row, int col) {
				Paint cpaint = getItemColor(row, col);
				if (cpaint == null) {
					cpaint = super.getItemPaint(row, col);
				}
				return cpaint;
			}

			public Color getItemColor(int row, int col) {
				double x = dataset.getXValue(row, col);
				double y = dataset.getYValue(row, col);

				try {
					return classColor[col];
				} catch (Exception e) {

				}

				return null;

			}

		};
		plot.setRenderer(renderer);
		renderer.setUseOutlinePaint(true);
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesOutlinePaint(0, Color.black);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(1));
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
		plot.getRangeCrosshairValue();
		plot.getDomainCrosshairValue();

		// Create Panel
		panel.setChart(chart);
		panel.setMaximumDrawWidth(6000);
		panel.setPreferredSize(new Dimension(450, 300));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);
		markerRange.setLabel("Low-High");
		markerRange.setLabelFont(new Font("SansSerif", 0, 15));
		markerRange.setLabelPaint(new Color(0, 102, 0));

		Font font3 = new Font("Dialog", Font.BOLD, 12);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		// domain.setRange(minDomain, maxDomain);
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		// range.setRange(minRange, maxRange);
		domain.setAutoRange(true);
		range.setAutoRange(true);
		computeLogarithmicCoefficients(chart, plot, dataset, filterOrder);

	}

	public void addScatterPlotSeriesExponential(String domainName, String rangeName, List<Double> valuesDomain,
			List<Double> valuesRange, IntervalMarker markerRange, IntervalMarker markerDomain, Object[][] data,
			Color[] classColor) {

		this.valuesDomain = valuesDomain;
		this.rangeName = rangeName;
		this.domainName = domainName;
		this.valuesRange = valuesRange;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;
//		this.domainValue = domainValue;
//		this.rangeValue = rangeValue;
		this.classColor = classColor;
		this.filterOrder = filterOrder;

		panel.removeAll();
		dataset = new XYSeriesCollection();
		series1 = new XYSeries("");
		for (int i = 0; i < valuesDomain.size(); i++)
			series1.add(valuesDomain.get(i), valuesRange.get(i));
		dataset.addSeries(series1);

		double minDomain = Collections.min(valuesDomain);
		double maxDomain = Collections.max(valuesDomain);
		double[] coefficients = RegressionLE_.getExponentialRegression(dataset, 0);
		Function2D curve = new ExponentialFunction2D(coefficients[0], coefficients[1]);
		XYDataset regressionData = DatasetUtils.sampleFunction2D(curve, minDomain, maxDomain, valuesDomain.size(),
				"Fitted Regression Line");

		JFreeChart chart = ChartFactory.createScatterPlot("", domainName, rangeName, dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer1);
		plot.setBackgroundPaint(new Color(255, 228, 196));
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		plot.setDataset(1, regressionData);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(0, Color.RED);
		plot.setRenderer(1, renderer2);
		Shape cross = new Ellipse2D.Double(0, 0, 5, 5);
		List<String[]> featureLists = new ArrayList<String[]>();
		for (int i = 0; i < ColorEditorSpot.tableC.getModel().getRowCount(); i++)
			featureLists.add(((JLabel) ColorEditorSpot.tableC.getModel().getValueAt(i,
					ColorEditorSpot.tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "")
							.split("<br>"));

		renderer = new XYLineAndShapeRenderer() {
			@Override
			public Paint getItemPaint(int row, int col) {
				Paint cpaint = getItemColor(row, col);
				if (cpaint == null) {
					cpaint = super.getItemPaint(row, col);
				}
				return cpaint;
			}

			public Color getItemColor(int row, int col) {
				double x = dataset.getXValue(row, col);
				double y = dataset.getYValue(row, col);

				try {
					return classColor[col];
				} catch (Exception e) {

				}

				return null;

			}

		};
		plot.setRenderer(renderer);
		renderer.setUseOutlinePaint(true);
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesOutlinePaint(0, Color.black);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(1));
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
		plot.getRangeCrosshairValue();
		plot.getDomainCrosshairValue();

		// Create Panel
		panel.setChart(chart);
		panel.setMaximumDrawWidth(6000);
		panel.setPreferredSize(new Dimension(450, 300));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);
		markerRange.setLabel("Low-High");
		markerRange.setLabelFont(new Font("SansSerif", 0, 15));
		markerRange.setLabelPaint(new Color(0, 102, 0));

		Font font3 = new Font("Dialog", Font.BOLD, 12);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		// domain.setRange(minDomain, maxDomain);
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		// range.setRange(minRange, maxRange);
		domain.setAutoRange(true);
		range.setAutoRange(true);
		computeExponentialCoefficients(chart, plot, dataset, filterOrder);

	}

	private void computeLinearCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		// Calculate Linear Regression
		try {
			coefficients = RegressionLE_.getOLSRegression(dataset, 0);
			retVal = new LineFunction2D(coefficients[0], coefficients[1]);
			r2 = coefficients[2];
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		final double intercept = coefficients[0];
		final double slope = coefficients[1];
		final String linearEquation;
		if (intercept >= 0) {
			linearEquation = "y = " + String.format("%.2f", slope) + "x + " + String.format("%.2f", intercept);
		} else {
			linearEquation = "y = " + String.format("%.2f", slope) + "x - " + Math.abs(intercept);
		}

		TextTitle tt = new TextTitle(linearEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.98, 0.02, tt, RectangleAnchor.BOTTOM_RIGHT);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computePowerCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		// Calculate Linear Regression

		// Calculate Power Regression
		try {
			coefficients = RegressionLE_.getPowerRegression(dataset, 0);
			if (coefficients[2] > r2) {
				retVal = new PowerFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		final double intercept = coefficients[0];
		final double slope = coefficients[1];
		final String linearEquation;
		if (intercept >= 0) {
			linearEquation = "y = " + String.format("%.2f", slope) + "x^ " + String.format("%.2f", intercept);
		} else {
			linearEquation = "y = " + String.format("%.2f", slope) + "x^ -" + Math.abs(intercept);
		}

		TextTitle tt = new TextTitle(linearEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.98, 0.02, tt, RectangleAnchor.BOTTOM_RIGHT);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computePolynomialCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset,
			int filterOrder) {

		final double[] coefficients = Regression.getPolynomialRegression(dataset, 0, filterOrder);
		double r2 = coefficients[coefficients.length - 1];
		String polynomialEquation = "";
		for (int i = coefficients.length - 1; i >= 0; i--) {
			if (i == 0) {
				polynomialEquation += String.format("%.2f", coefficients[i]);

			} else if (i == 1) {
				polynomialEquation += String.format("%.2f", coefficients[i]) + "*x+";
			} else if (i > 1) {
				polynomialEquation += String.format("%.2f", coefficients[i]) + "*x^" + i + "+";

			}
		}

		TextTitle tt = new TextTitle("y = " + polynomialEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.98, 0.02, tt, RectangleAnchor.BOTTOM_RIGHT);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computeLogarithmicCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset,
			int filterOrder) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		try {
			coefficients = RegressionLE_.getLogarithmicRegression(dataset, 0);
			if (coefficients[2] > r2) {
				retVal = new LogarithmicFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		String logarithmicEquation = "y = " + String.format("%.2f", coefficients[0]) + " + " + "( "
				+ String.format("%.2f", coefficients[1]) + " * ln(x) ) ";

		TextTitle tt = new TextTitle("y = " + logarithmicEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.98, 0.02, tt, RectangleAnchor.BOTTOM_RIGHT);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computeExponentialCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset,
			int filterOrder) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		try {
			coefficients = RegressionLE_.getExponentialRegression(dataset, 0);
			if (coefficients[2] > r2) {
				retVal = new LogarithmicFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		String exponentialEquation = "y = " + String.format("%.2f", coefficients[0]) + " * " + "( e^("
				+ String.format("%.2f", coefficients[1]) + " * x) ) ";

		TextTitle tt = new TextTitle("y = " + exponentialEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.98, 0.02, tt, RectangleAnchor.BOTTOM_RIGHT);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

}