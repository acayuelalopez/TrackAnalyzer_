//
//import java.awt.Frame;
//import java.awt.Menu;
//import java.awt.MenuItem;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//import javax.swing.JFileChooser;
//import javax.swing.filechooser.FileNameExtensionFilter;
//
//import org.knowm.xchart.XYChart;
//import org.knowm.xchart.internal.chartpart.Chart;
//
//import DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimatorModified;
//import features.ActiveTransportParametersFeatureModified;
//import features.ConfinedDiffusionParametersFeatureModified;
//import features.PowerLawFeatureModified;
//import ij.IJ;
//import ij.WindowManager;
//import ij.measure.ResultsTable;
//import ij.text.TextPanel;
//import traJ.TrajectoryModified;
//import traJ.TrajectoryUtilModified;
//import trajectory_classifier.ExportImportToolsModified;
//import trajectory_classifier.VisualizationUtilsModified;
//
//public class TraJResultsTableModified extends ResultsTable {
//
//	boolean isParentTable;
//
//	public TraJResultsTableModified() {
//		this.isParentTable = false;
//	}
//
//	public TraJResultsTableModified(boolean isParentTable) {
//		this.isParentTable = isParentTable;
//	}
//
//	@Override
//	public void show(String windowTitle) {
//		// TODO Auto-generated method stub
//		super.show(windowTitle);
//		final ResultsTable table = this;
//
//		/*
//		 * Plot trajectory
//		 */
//		MenuItem plotSelectedTrajectory = new MenuItem("Plot trajectory");
//		plotSelectedTrajectory.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				Frame f = (Frame) WindowManager.getActiveWindow();
//				if (f.getComponent(0) instanceof TextPanel) {
//					TextPanel p = (TextPanel) f.getComponent(0);
//
//					int selectionStart = p.getSelectionStart();
//					int selectionEnd = p.getSelectionEnd();
//					ArrayList<XYChart> charts = new ArrayList<XYChart>();
//					if (selectionStart >= 0 && selectionStart == selectionEnd) {
//						int id = (int) table.getValue("ID", selectionStart);
//
//						ArrayList<? extends TrajectoryModified> cTracks;
//						if (isParentTable) {
//							cTracks = TraJClassifierTest_.getInstance().getParentTrajectories();
//						} else {
//							cTracks = TraJClassifierTest_.getInstance().getClassifiedTrajectories();
//						}
//						TrajectoryModified t = TrajectoryUtilModified.getTrajectoryByID(cTracks, id);
//						XYChart c = VisualizationUtilsModified.getTrajectoryChart("Trajectory with ID " + id, t);
//						charts.add(c);
//						double timelag = TraJClassifierTest_.getInstance().getTimelag();
//						if (t.getType().equals("SUBDIFFUSION")) {
//							PowerLawFeatureModified pwf = new PowerLawFeatureModified(t, 1 / timelag, 1, t.size() / 3);
//							double[] res = pwf.evaluate();
//							c = VisualizationUtilsModified.getMSDLineWithPowerModelChart(t, 1, t.size() / 3, timelag,
//									res[0], res[1]);
//							charts.add(c);
//
//						} else if (t.getType().equals("CONFINED")) {
//							ConfinedDiffusionParametersFeatureModified cfeature = new ConfinedDiffusionParametersFeatureModified(
//									t, timelag, TraJClassifierTest_.getInstance().isUseReducedModelConfinedMotion());
//							double[] res = cfeature.evaluate();
//							if (TraJClassifierTest_.getInstance().isUseReducedModelConfinedMotion()) {
//								c = VisualizationUtilsModified.getMSDLineWithConfinedModelChart(t, 1, t.size() / 3,
//										timelag, res[0], 1, 1, res[1]);
//								charts.add(c);
//							} else {
//								c = VisualizationUtilsModified.getMSDLineWithConfinedModelChart(t, 1, t.size() / 3,
//										timelag, res[0], res[2], res[3], res[1]);
//								charts.add(c);
//							}
//						} else if (t.getType().equals("NORM. DIFFUSION")) {
//							RegressionDiffusionCoefficientEstimatorModified regest = new RegressionDiffusionCoefficientEstimatorModified(
//									t, 1 / timelag, 1, t.size() / 3);
//							double[] res = regest.evaluate();
//							double dc = res[0];
//							double intercept = res[2];
//							c = VisualizationUtilsModified.getMSDLineWithFreeModelChart(t, 1, t.size() / 3, timelag, dc,
//									intercept);
//							charts.add(c);
//						} else if (t.getType().equals("DIRECTED/ACTIVE")) {
//							ActiveTransportParametersFeatureModified apf = new ActiveTransportParametersFeatureModified(t, timelag);
//							double[] ares = apf.evaluate();
//							c = VisualizationUtilsModified.getMSDLineWithActiveTransportModelChart(t, 1, t.size() / 3, timelag,
//									ares[0], ares[1]);
//							charts.add(c);
//						} else {
//							c = VisualizationUtilsModified.getMSDLineChart(t, 1, t.size() / 3);
//							charts.add(c);
//						}
//						VisualizationUtilsModified.plotCharts(charts);
//					} else if (selectionStart != selectionEnd) {
//						IJ.showMessage("Plot of multiple trajectories is not possible");
//					} else {
//						IJ.showMessage("No trajectory selected");
//					}
//				}
//
//			}
//		});
//
//		/*
//		 * Export trajectory(s)
//		 */
//		MenuItem exportTrajectories = new MenuItem("Export trajetorie(s)");
//
//		exportTrajectories.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Frame f = (Frame) WindowManager.getActiveWindow();
//
//				if (f.getComponent(0) instanceof TextPanel) {
//					TextPanel p = (TextPanel) f.getComponent(0);
//
//					int selectionStart = p.getSelectionStart();
//					int selectionEnd = p.getSelectionEnd();
//					if (selectionStart == -1 && selectionEnd == -1) {
//						selectionStart = 0;
//						selectionEnd = p.getResultsTable().getCounter();
//					}
//
//					ArrayList<TrajectoryModified> selectedTrajectories = new ArrayList<TrajectoryModified>();
//					for (int i = selectionStart; i <= selectionEnd; i++) {
//						int id = (int) table.getValue("ID", i);
//
//						ArrayList<? extends TrajectoryModified> cTracks = null;
//						if (isParentTable) {
//							cTracks = TraJClassifierTest_.getInstance().getParentTrajectories();
//						} else {
//							cTracks = TraJClassifierTest_.getInstance().getClassifiedTrajectories();
//						}
//						TrajectoryModified t = TrajectoryUtilModified.getTrajectoryByID(cTracks, id);
//						selectedTrajectories.add(t);
//					}
//
//					JFileChooser chooser = new JFileChooser();
//					chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//					chooser.addChoosableFileFilter(new FileNameExtensionFilter("Comma seperated value (.csv)", "csv"));
//					chooser.setAcceptAllFileFilterUsed(false);
//					int c = chooser.showSaveDialog(null);
//					if (c == JFileChooser.APPROVE_OPTION) {
//						String path = chooser.getSelectedFile().getAbsolutePath();
//						if (!path.substring(path.length() - 3, path.length()).equals("csv")) {
//							path += ".csv";
//						}
//
//						ExportImportToolsModified eit = new ExportImportToolsModified();
//						eit.exportTrajectoryDataAsCSV(selectedTrajectories, path);
//					}
//
//				}
//
//			}
//		});
//
//		Menu traJ = new Menu("Trajectory classifier");
//		traJ.add(plotSelectedTrajectory);
//		traJ.add(exportTrajectories);
//		// ResultsTable.getResultsWindow().getMenuBar().
//		// Hinzufügen von Export Funktionen
//		WindowManager.getFrame(windowTitle).getMenuBar().add(traJ);
//	}
//
//}