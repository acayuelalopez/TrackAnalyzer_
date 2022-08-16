
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import ij.process.ImageProcessor;
import ij.ImagePlus;
import ij.ImageStack;
import java.io.FileNotFoundException;
import ij.IJ;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.awt.Container;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Color;
import ij.gui.PlotWindow;
import ij.gui.Plot;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.awt.geom.Point2D;
import ij.measure.ResultsTable;
import java.util.Collection;
import java.util.Collections;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import ij.WindowManager;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;
import javax.swing.event.ChangeListener;

import java.awt.event.WindowListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;
import ij.plugin.PlugIn;

// 
// Decompiled by Procyon v0.5.36
// 

public class Chemotaxis_ToolModified implements PlugIn {
	ChemotaxisGUI gui;
	ScalingDialog _dialog;
	float[][] x_values;
	float[][] y_values;
	float _angleBetweenCircle;
	float _angleBetweenDiagram;
	ArrayList _openInfoWindows;
	ArrayList _selectedDatasets;
	ArrayList _currentOpenWindows;
	ArrayList _currentOpenDiagrams;
	Vector _maxVector;
	int _maxPosition;
	float _anglePosition;
	int _currentSelectedDataset;
	ArrayList _variableSliceNumber;
	ArrayList _importedData;
	Hashtable _hashSliceNumber;
	LinkedList _listArrayList;
	Hashtable _hashImportedDataset;
	Hashtable _hashSlicesImported;
	Hashtable _hashTracks;
	Hashtable _hashCurrentDataset;
	Hashtable _hashCurrentPosition;
	Hashtable _hashPlot;
	Hashtable _hashWindow;
	float _coordSize;
	float _calxy;
	double _timeInterval;
	String _unitsPath;
	String _unitsTime;
	int _plotHeight;
	int _plotWidth;
	ArrayList arrayToImport;

	public Chemotaxis_ToolModified(ArrayList arrayToImport) {
		this.arrayToImport = arrayToImport;
		this.gui = null;
		this._dialog = null;
		this._angleBetweenCircle = 66.0f;
		this._angleBetweenDiagram = 66.0f;
		this._maxVector = null;
		this._maxPosition = 0;
		this._anglePosition = 0.0f;
		this._variableSliceNumber = null;
		this._coordSize = 0.0f;
		this._calxy = 1.0f;
		this._timeInterval = 2.0;
	}

	public void run(String var1) {

		PlotWindow.plotHeight = 500;
		PlotWindow.plotWidth = 500;
		this._plotHeight = 500;
		this._plotWidth = 500;
		(this._dialog = new ScalingDialog(this.gui)).setHeight(this._plotHeight);
		this._dialog.setWidth(this._plotWidth);
		this._openInfoWindows = new ArrayList();
		this._hashImportedDataset = new Hashtable();
		this._hashSlicesImported = new Hashtable();
		this._hashTracks = new Hashtable();
		this._hashCurrentDataset = new Hashtable();
		this._hashCurrentPosition = new Hashtable();
		this._hashPlot = new Hashtable();
		this._hashWindow = new Hashtable();
		this._currentOpenWindows = new ArrayList();
		this._currentOpenDiagrams = new ArrayList();
		this._importedData = new ArrayList();
		this._hashSliceNumber = new Hashtable();
		this._selectedDatasets = new ArrayList();
		this._listArrayList = new LinkedList();
		this._maxVector = new Vector();
		this._selectedDatasets.clear();
		this._hashSlicesImported.clear();
		this._hashImportedDataset.clear();
		this._importedData.clear();
		this._hashSliceNumber.clear();
		this._listArrayList.clear();
		this._hashTracks.clear();
		this._hashCurrentDataset.clear();
		this._hashCurrentPosition.clear();
		this._hashPlot.clear();
		this._hashWindow.clear();

		try {
			this.readData("prueba2.xls");
		} catch (IOException ex5) {
			JOptionPane.showMessageDialog(this.gui, "Error reading from file");
		}
//Add dataset
		boolean b4 = true;
		final String item = "1: " + "prueba2.xls";

		if (this._hashSlicesImported.containsKey(item)) {

			ArrayList list16 = (ArrayList<Integer>) this._hashSlicesImported.get(item);
			if (list16.size() == 1) {

				try {
					list16 = new ArrayList();
					this._hashSlicesImported.remove(item);
					list16.add(1);
					list16.add(SPTBatch_.imps.getStackSize());
					if (Integer.valueOf(SPTBatch_.imps.getStackSize()) < Integer.valueOf("1")) {
						JOptionPane.showMessageDialog(this.gui, "Second value can`t be smaller than the first");
					} else {
						this._hashSlicesImported.put(item, list16);
					}
				} catch (NumberFormatException ex12) {
					this.gui.firstSlicesField.setText("0");
					this.gui.secondSlicesField.setText("0");
					list16.add(new Integer(0));
					list16.add(new Integer(0));
					this._hashSlicesImported.put(item, list16);
					JOptionPane.showMessageDialog(this.gui, "Please enter correct value for number of slices!");
				}
			}
			if (list16.size() == 2) {
				;
				try {
					if (Integer.valueOf(1) != list16.get(0)
							|| Integer.valueOf(SPTBatch_.imps.getStackSize()) != list16.get(1)) {
						this._hashSlicesImported.remove(item);
						final ArrayList<Integer> value7 = new ArrayList<Integer>();
						value7.add(Integer.valueOf("1"));
						value7.add(Integer.valueOf(SPTBatch_.imps.getStackSize()));
						if (Integer.valueOf(SPTBatch_.imps.getStackSize()) < Integer.valueOf("1.0")) {
							JOptionPane.showMessageDialog(this.gui, "Second value can`t be smaller than the first");
						} else {
							this._hashSlicesImported.put(item, value7);
						}
					}
				} catch (NumberFormatException ex13) {
					final ArrayList<Integer> list17 = (ArrayList<Integer>) this._hashSlicesImported.get(item);
					JOptionPane.showMessageDialog(this.gui, "Setting old values!");

				}
			}

		} else {
			try {

				final ArrayList<Integer> value9 = new ArrayList<Integer>();
				value9.add(Integer.valueOf("1"));
				value9.add(Integer.valueOf(SPTBatch_.imps.getStackSize()));
				if (Integer.valueOf(SPTBatch_.imps.getStackSize()) < Integer.valueOf("1")) {
					b4 = false;
					JOptionPane.showMessageDialog(this.gui, "Second value can`t be smaller than the first");
				} else {
					this._hashSlicesImported.put(item, value9);
				}

			} catch (NumberFormatException ex14) {
				b4 = false;
				JOptionPane.showMessageDialog(this.gui, "Please enter correct value for number of slices!");
			}
		}

		// Apply settings

		boolean b5 = true;
		this._maxVector.clear();
		final ArrayList<String> list18 = new ArrayList<String>();

		final String s20 = item;
		if (!list18.contains(s20)) {
			list18.add(s20);
		}

//		final String s22 = (String) item;
//		if (!list18.contains(s22)) {
//			list18.add(s22);
//		}

		this._unitsPath = (String) "µm";
		this._unitsTime = (String) "min";
		int intValue31 = 0;
		int intValue32 = 0;
		int n68 = 0;
		double n69 = 0.0;
		double doubleValue = 0.0;

		int n70 = 0;
		double n71 = 0.0;
		double doubleValue2 = 0.0;

		this._calxy = new Float(1.0);
		this._timeInterval = new Double(1.0);

		b5 = true;
		if (b5) {

			this._listArrayList.clear();
			this._hashCurrentDataset.clear();
			this._hashSliceNumber.clear();

			for (int n72 = 0; n72 < list18.size(); ++n72) {
				this._variableSliceNumber = new ArrayList();
				final String s26 = list18.get(n72);
				this._hashCurrentDataset.put(new Integer(n72), s26);
				this._hashCurrentPosition.put(s26, new Integer(n72));
				final ArrayList<Integer> list20 = (ArrayList) this._hashSlicesImported.get(s26);
				final int intValue35 = (Integer) this._hashImportedDataset.get(s26);
				final ArrayList list21 = new ArrayList<Float>();
				final ArrayList<Float> list22 = new ArrayList<Float>();
				final ArrayList<Float> list23 = new ArrayList<Float>();
				final ArrayList<Object> list24 = (ArrayList) this._importedData.get(intValue35 - 1);
				for (int index24 = 0; index24 < list24.size(); index24 += 4) {
					list21.add(list24.get(index24));
					list22.add((Float) list24.get(index24 + 2));
					list23.add((Float) list24.get(index24 + 3));
				}
				final int intValue36 = Math.round((Float) list21.get(list21.size() - 1));
				final int[] array70 = new int[3 * intValue36];
				int n73 = 1;
				for (int n74 = 0; n74 <= 3 * intValue36 - 3; n74 += 3) {
					array70[n74] = list21.indexOf(new Float((float) n73));
					array70[n74 + 1] = list21.lastIndexOf(new Float((float) n73));
					array70[n74 + 2] = n73;
					++n73;
				}
				final ArrayList<Float> list25 = new ArrayList<Float>();
				final ArrayList<Float> list26 = new ArrayList<Float>();
				int n75 = 0;
				if (list20.size() == 1) {
					final int intValue37 = list20.get(0);
					for (int n76 = 0; n76 <= array70.length - 3; n76 += 3) {
						if (array70[n76 + 1] != -1 && array70[n76] != -1
								&& array70[n76 + 1] - array70[n76] == intValue37 - 1) {
							++n75;
							this._variableSliceNumber.add(new Integer(array70[n76 + 1] - array70[n76] + 1));
							for (int n77 = array70[n76]; n77 <= array70[n76 + 1]; ++n77) {
								list25.add(new Float(list22.get(n77) * this._calxy));
								list26.add(new Float(list23.get(n77) * this._calxy));
							}
						}
					}
				}
				if (list20.size() == 2) {
					final int intValue38 = list20.get(0);
					final int intValue39 = list20.get(1);
					for (int n78 = 0; n78 <= array70.length - 3; n78 += 3) {
						if (array70[n78 + 1] != -1 && array70[n78] != -1
								&& array70[n78 + 1] - array70[n78] >= intValue38 - 1
								&& array70[n78 + 1] - array70[n78] <= intValue39 - 1) {
							++n75;
							this._variableSliceNumber.add(new Integer(array70[n78 + 1] - array70[n78] + 1));
							for (int n79 = array70[n78]; n79 <= array70[n78 + 1]; ++n79) {
								list25.add(new Float(list22.get(n79) * this._calxy));
								list26.add(new Float(list23.get(n79) * this._calxy));
							}
						}
					}
				}
				this.updateNumberofTracks(s26, n75);
				if (this._variableSliceNumber.isEmpty()) {
					if (list20.size() == 1) {
						JOptionPane.showMessageDialog(this.gui, "No tracks available for current number of slices");
					}
					if (list20.size() == 2) {
						JOptionPane.showMessageDialog(this.gui,
								"No tracks available for current range of used number of slices");
					}
				} else {
					final int intValue40 = Collections.max((Collection<? extends Integer>) this._variableSliceNumber);

					int n88 = 0;
					this.x_values = new float[n75][intValue40];
					this.y_values = new float[n75][intValue40];
					for (int index26 = 0; index26 < n75; ++index26) {
						this.x_values[index26][0] = 0.0f;
						this.y_values[index26][0] = 0.0f;
						int n89;
						int intValue42;
						for (n89 = 1, intValue42 = (int) this._variableSliceNumber
								.get(index26); n89 < intValue42; ++n89) {
							final float n90 = list25.get(n89 + n88) - list25.get(n88);
							final float n91 = -(list26.get(n89 + n88) - list26.get(n88));
							this.x_values[index26][n89] = n90;
							this.y_values[index26][n89] = n91;
						}
						n88 += intValue42;
					}

					this._listArrayList.add(this.x_values);
					this._listArrayList.add(this.y_values);
					this._hashSliceNumber.put(new Integer(n72), this._variableSliceNumber);
				}

			}
		}
//Calulate slice series
		// centerofmass

		for (int j = 0; j < this._listArrayList.size(); j += 2) {
			final String s4 = (String) this._hashCurrentDataset.get(new Integer(j / 2));
			final ArrayList coll = (ArrayList) this._hashSliceNumber.get(new Integer(j / 2));
			final int intValue5 = (int) this._hashTracks.get(s4);
			final int intValue6 = Collections.max((Collection<? extends Integer>) coll);
			final float[][] array3 = (float[][]) this._listArrayList.get(j);
			final float[][] array4 = (float[][]) this._listArrayList.get(j + 1);
			final ResultsTable resultsTable = new ResultsTable();
			final String[] array5 = { "Slice", "Center of mass x [" + this._unitsPath + "]",
					"Center of mass y [" + this._unitsPath + "]", "Center of mass length [" + this._unitsPath + "]" };
			for (int k = 0; k < array5.length; ++k) {
				resultsTable.setHeading(k, array5[k]);
			}
			for (int l = 1; l <= intValue6; ++l) {
				final double[] centerofMassSeries = ChemotaxisStatistic.centerofMassSeries(l, array3, array4, intValue5,
						coll);
				resultsTable.incrementCounter();
				resultsTable.addValue(0, (double) l);
				resultsTable.addValue(1, centerofMassSeries[0]);
				resultsTable.addValue(2, centerofMassSeries[1]);
				resultsTable.addValue(3, centerofMassSeries[2]);
			}
			// resultsTable.show("Center of mass");
			resultsTable.save(SPTBatch_.directChemo + File.separator + "Center of mass series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}
		// directionality

		for (int index3 = 0; index3 < this._listArrayList.size(); index3 += 2) {
			final String s5 = (String) this._hashCurrentDataset.get(new Integer(index3 / 2));
			final ArrayList coll2 = (ArrayList) this._hashSliceNumber.get(new Integer(index3 / 2));
			final int intValue7 = Collections.max((Collection<? extends Integer>) coll2);
			final int intValue8 = (int) this._hashTracks.get(s5);
			final float[][] array6 = (float[][]) this._listArrayList.get(index3);
			final float[][] array7 = (float[][]) this._listArrayList.get(index3 + 1);
			final ResultsTable resultsTable2 = new ResultsTable();
			final String[] array8 = { "Slice", "Directionality" };
			for (int n2 = 0; n2 < array8.length; ++n2) {
				resultsTable2.setHeading(n2, array8[n2]);
			}
			for (int n3 = 1; n3 <= intValue7; ++n3) {
				final double computeChemotaxisIndex = ChemotaxisStatistic.computeChemotaxisIndex(array6, array7, n3,
						intValue8, coll2);
				resultsTable2.incrementCounter();
				resultsTable2.addValue(0, (double) n3);
				resultsTable2.addValue(1, computeChemotaxisIndex);
			}
			// resultsTable2.show("Directionality series for " + s5);
			resultsTable2.save(SPTBatch_.directChemo + File.separator + "Directionality series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}
		// fmi
		for (int index5 = 0; index5 < this._listArrayList.size(); index5 += 2) {
			final String s7 = (String) this._hashCurrentDataset.get(new Integer(index5 / 2));
			final ArrayList coll3 = (ArrayList) this._hashSliceNumber.get(new Integer(index5 / 2));
			final int intValue11 = (int) this._hashTracks.get(s7);
			final int intValue12 = Collections.max((Collection<? extends Integer>) coll3);
			final float[][] array12 = (float[][]) this._listArrayList.get(index5);
			final float[][] array13 = (float[][]) this._listArrayList.get(index5 + 1);
			final ResultsTable resultsTable4 = new ResultsTable();
			final String[] array14 = { "Slice", "x FMI", "y FMI" };
			for (int n6 = 0; n6 < array14.length; ++n6) {
				resultsTable4.setHeading(n6, array14[n6]);
			}
			for (int n7 = 1; n7 <= intValue12; ++n7) {
				final double computeFMIIndex = ChemotaxisStatistic.computeFMIIndex(array12, array13, n7, intValue11, 1,
						coll3);
				final double computeFMIIndex2 = ChemotaxisStatistic.computeFMIIndex(array12, array13, n7, intValue11, 2,
						coll3);
				resultsTable4.incrementCounter();
				resultsTable4.addValue(0, (double) n7);
				resultsTable4.addValue(1, computeFMIIndex2);
				resultsTable4.addValue(2, computeFMIIndex);
			}
			// resultsTable4.show("FMI index series for " + s7);
			resultsTable4.save(SPTBatch_.directChemo + File.separator + "FMI index series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}
//TrackSeries
		// directionality

		for (int index4 = 0; index4 < this._listArrayList.size(); index4 += 2) {
			final String s6 = (String) this._hashCurrentDataset.get(new Integer(index4 / 2));
			final ArrayList<Integer> list3 = (ArrayList<Integer>) this._hashSliceNumber.get(new Integer(index4 / 2));
			final int intValue9 = (int) this._hashTracks.get(s6);
			final float[][] array9 = (float[][]) this._listArrayList.get(index4);
			final float[][] array10 = (float[][]) this._listArrayList.get(index4 + 1);
			final ArrayList computeDirectionality = ChemotaxisStatistic.computeDirectionality(array9, array10, list3,
					intValue9);
			final ResultsTable resultsTable3 = new ResultsTable();
			final String[] array11 = { "Track Number", "Directionality", "Endpoint X Value", "Endpoint Y Value" };
			for (int n4 = 0; n4 < array11.length; ++n4) {
				resultsTable3.setHeading(n4, array11[n4]);
			}
			for (int n5 = 0; n5 < intValue9; ++n5) {
				final int intValue10 = list3.get(n5);
				final float value = array9[n5][intValue10 - 1];
				final float value2 = array10[n5][intValue10 - 1];
				resultsTable3.incrementCounter();
				resultsTable3.addValue(0, (double) (n5 + 1));
				resultsTable3.addValue(1, (double) computeDirectionality.get(n5));
				resultsTable3.addValue(2, (double) new Float(value));
				resultsTable3.addValue(3, (double) new Float(value2));
			}
			// resultsTable3.show("Directionality series for " + s6);
			resultsTable3.save(SPTBatch_.directChemo + File.separator + "Directionality track series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}
		// fmi

		for (int index6 = 0; index6 < this._listArrayList.size(); index6 += 2) {
			final String s8 = (String) this._hashCurrentDataset.get(new Integer(index6 / 2));
			final ArrayList<Integer> list4 = (ArrayList<Integer>) this._hashSliceNumber.get(new Integer(index6 / 2));
			final int intValue13 = (int) this._hashTracks.get(s8);
			final float[][] array15 = (float[][]) this._listArrayList.get(index6);
			final float[][] array16 = (float[][]) this._listArrayList.get(index6 + 1);
			final ResultsTable resultsTable5 = new ResultsTable();
			final String[] array17 = { "Track Number", "x FMI", "y FMI", "Endpoint X Value", "Endpoint Y Value" };
			for (int n8 = 0; n8 < array17.length; ++n8) {
				resultsTable5.setHeading(n8, array17[n8]);
			}
			for (int index7 = 0; index7 < intValue13; ++index7) {
				final int intValue14 = list4.get(index7);
				double n9 = 0.0;
				for (int n10 = 0; n10 < intValue14 - 1; ++n10) {
					n9 += Point2D.distance(new Float(array15[index7][n10]), new Float(array16[index7][n10]),
							new Float(array15[index7][n10 + 1]), new Float(array16[index7][n10 + 1]));
				}
				final float value3 = array16[index7][intValue14 - 1];
				final float value4 = array15[index7][intValue14 - 1];
				final double roundDoubleNumbers = this.roundDoubleNumbers(value4 / n9);
				final double roundDoubleNumbers2 = this.roundDoubleNumbers(value3 / n9);
				resultsTable5.incrementCounter();
				resultsTable5.addValue(0, (double) (index7 + 1));
				resultsTable5.addValue(1, roundDoubleNumbers);
				resultsTable5.addValue(2, roundDoubleNumbers2);
				resultsTable5.addValue(3, (double) new Float(value4));
				resultsTable5.addValue(4, (double) new Float(value3));
			}
			// resultsTable5.show("FMI track series for " + s8);
			resultsTable5.save(SPTBatch_.directChemo + File.separator + "FMI track series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}

		// velocity
		for (int index8 = 0; index8 < this._listArrayList.size(); index8 += 2) {
			final String s9 = (String) this._hashCurrentDataset.get(new Integer(index8 / 2));
			final ArrayList list5 = (ArrayList) this._hashSliceNumber.get(new Integer(index8 / 2));
			final int intValue15 = (int) this._hashTracks.get(s9);
			final ArrayList computeDistandVelocity = ChemotaxisStatistic.computeDistandVelocity("velocity",
					(float[][]) this._listArrayList.get(index8), (float[][]) this._listArrayList.get(index8 + 1), list5,
					intValue15, this._timeInterval);
			final ResultsTable resultsTable6 = new ResultsTable();
			final String[] array18 = { "Track Number", "Velocity [" + this._unitsPath + "/" + this._unitsTime + "]" };
			for (int n11 = 0; n11 < array18.length; ++n11) {
				resultsTable6.setHeading(n11, array18[n11]);
			}
			for (int index9 = 0; index9 < intValue15; ++index9) {
				final float roundFloatNumbers = this
						.roundFloatNumbers(Float.valueOf((computeDistandVelocity.get(index9)).toString()));
				resultsTable6.incrementCounter();
				resultsTable6.addValue(0, (double) (index9 + 1));
				resultsTable6.addValue(1, (double) roundFloatNumbers);
			}
			// resultsTable6.show("Velocity series for " + s9);
			resultsTable6.save(SPTBatch_.directChemo + File.separator + "Velocity series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}
		// distance

		for (int index10 = 0; index10 < this._listArrayList.size(); index10 += 2) {
			final String s10 = (String) this._hashCurrentDataset.get(new Integer(index10 / 2));
			final ArrayList list6 = (ArrayList) this._hashSliceNumber.get(new Integer(index10 / 2));
			final int intValue16 = (int) this._hashTracks.get(s10);
			final float[][] array19 = (float[][]) this._listArrayList.get(index10);
			final float[][] array20 = (float[][]) this._listArrayList.get(index10 + 1);
			final ArrayList computeDistandVelocity2 = ChemotaxisStatistic.computeDistandVelocity("accumulated distance",
					array19, array20, list6, intValue16, this._timeInterval);
			final ArrayList computeDistandVelocity3 = ChemotaxisStatistic.computeDistandVelocity("euclid distance",
					array19, array20, list6, intValue16, this._timeInterval);
			final ResultsTable resultsTable7 = new ResultsTable();
			final String[] array21 = { "Track Number", "Accumulated distance [" + this._unitsPath + "]",
					"Euclidean distance [" + this._unitsPath + "]" };
			for (int n12 = 0; n12 < array21.length; ++n12) {
				resultsTable7.setHeading(n12, array21[n12]);
			}
			for (int n13 = 0; n13 < intValue16; ++n13) {
				final float roundFloatNumbers2 = this
						.roundFloatNumbers((Float.valueOf(computeDistandVelocity2.get(n13).toString())));
				final float roundFloatNumbers3 = this
						.roundFloatNumbers((Float.valueOf(computeDistandVelocity3.get(n13).toString())));
				resultsTable7.incrementCounter();
				resultsTable7.addValue(0, (double) (n13 + 1));
				resultsTable7.addValue(1, (double) roundFloatNumbers2);
				resultsTable7.addValue(2, (double) roundFloatNumbers3);
			}
			// resultsTable7.show("Distance series for " + s10);
			resultsTable7.save(SPTBatch_.directChemo + File.separator + "Distance series for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		}

///ver original dataset to check
		final String s18 = (String) item;
		final int intValue27 = (int) this._hashImportedDataset.get(s18);
		final ResultsTable resultsTable13 = new ResultsTable();
		final String[] array66 = { "Track n", "Slice n", "X", "Y" };
		for (int n65 = 0; n65 < array66.length; ++n65) {
			resultsTable13.setHeading(n65, array66[n65]);
		}
		final ArrayList<Float> list14 = (ArrayList<Float>) this._importedData.get(intValue27 - 1);
		for (int index19 = 0; index19 < list14.size(); index19 += 4) {
			resultsTable13.incrementCounter();
			resultsTable13.addValue(0, (double) list14.get(index19));
			resultsTable13.addValue(1, (double) list14.get(index19 + 1));
			resultsTable13.addValue(2, (double) list14.get(index19 + 2));
			resultsTable13.addValue(3, (double) list14.get(index19 + 3));
		}
		// resultsTable13.show("Original data for " + s18);
		resultsTable13.save(SPTBatch_.directChemo + File.separator + "Original data for "
				+ SPTBatch_.imps.getShortTitle() + ".xls");

		// show current data to check
		final String s19 = (String) item;
		if (this._hashCurrentDataset.contains(s19)) {
			final int intValue28 = (int) this._hashCurrentPosition.get(s19);
			final ArrayList<Integer> list15 = (ArrayList<Integer>) this._hashSliceNumber.get(new Integer(intValue28));
			final int index20 = intValue28 * 2;
			final int intValue29 = (int) this._hashTracks.get(s19);
			final float[][] array67 = (float[][]) this._listArrayList.get(index20);
			final float[][] array68 = (float[][]) this._listArrayList.get(index20 + 1);
			final ResultsTable resultsTable14 = new ResultsTable();
			final String[] array69 = { "Track n", "Slice n", "X [" + this._unitsPath + "]",
					"Y [" + this._unitsPath + "]" };
			for (int n66 = 0; n66 < array69.length; ++n66) {
				resultsTable14.setHeading(n66, array69[n66]);
			}
			for (int index21 = 0; index21 < intValue29; ++index21) {
				for (int intValue30 = list15.get(index21), n67 = 0; n67 < intValue30; ++n67) {
					resultsTable14.incrementCounter();
					resultsTable14.addValue(0, (double) new Integer(index21 + 1));
					resultsTable14.addValue(1, (double) new Integer(n67 + 1));
					resultsTable14.addValue(2, (double) new Float(array67[index21][n67]));
					resultsTable14.addValue(3, (double) new Float(array68[index21][n67]));
				}
			}
			// resultsTable14.show("Current used data for " + s19);
			resultsTable14.save(SPTBatch_.directChemo + File.separator + "Current used data for "
					+ SPTBatch_.imps.getShortTitle() + ".xls");
		} else {
			JOptionPane.showMessageDialog(this.gui, "No current data available!");
		}

		this._hashPlot.clear();
		int n92 = 0;
		final String s27 = (String) "Mark up/down";

		if (s27.equals("Mark up/down")) {
			n92 = 3;
		}

		if (this._dialog.auto) {
			float abs = 0.0f;
			float abs2 = 0.0f;
			if (this._listArrayList.size() > 1) {
				final ArrayList coll4 = new ArrayList();
				for (int index27 = 0; index27 < this._listArrayList.size(); index27 += 2) {
					final int intValue43 = (int) this._hashTracks
							.get(this._hashCurrentDataset.get(new Integer(index27 / 2)));
					final ArrayList<Integer> list27 = (ArrayList<Integer>) this._hashSliceNumber
							.get(new Integer(index27 / 2));
					final float[][] array71 = (float[][]) this._listArrayList.get(index27);
					final float[][] array72 = (float[][]) this._listArrayList.get(index27 + 1);
					for (int index28 = 0; index28 < intValue43; ++index28) {
						for (int intValue44 = list27.get(index28), n93 = 1; n93 < intValue44; ++n93) {
							if (abs < Math.abs(array71[index28][n93])) {
								abs = Math.abs(array71[index28][n93]);
							}
							if (abs2 < Math.abs(array72[index28][n93])) {
								abs2 = Math.abs(array72[index28][n93]);
							}
						}
					}
					if (abs > abs2) {
						coll4.add(new Float(abs));
					} else {
						coll4.add(new Float(abs2));
					}
				}
				this._coordSize = (Float) Collections.max(coll4) + 5.0f;
			}
		}

		for (int n94 = 0; n94 < this._listArrayList.size(); n94 += 2) {
			this.plotGraph(n92, n94);
		}

	}

	float angleCorrection(final float n) {
		float n2 = n;
		if (n2 < 0.0f) {
			n2 = n2 % 360.0f + 360.0f;
		}
		if (n2 > 360.0f) {
			n2 %= 360.0f;
		}
		return n2;
	}

	float[] calculatePoints(final float n) {
		final float n2 = this._coordSize * 2.0f;
		return new float[] { this.roundNumber(new Float(n2 * Math.cos(Math.toRadians(n)))),
				this.roundNumber(new Float(n2 * Math.sin(Math.toRadians(n)))) };
	}

	int countCells(final float n, final int index) {
		final String key = (String) this._hashCurrentDataset.get(new Integer(index / 2));
		final ArrayList<Integer> list = (ArrayList<Integer>) this._hashSliceNumber.get(new Integer(index / 2));
		final int intValue = (int) this._hashTracks.get(key);
		final float[][] array = (float[][]) this._listArrayList.get(index);
		final float[][] array2 = (float[][]) this._listArrayList.get(index + 1);
		int n2 = 0;
		final float angleCorrection = this.angleCorrection(this._anglePosition + n / 2.0f);
		final float angleCorrection2 = this.angleCorrection(this._anglePosition - n / 2.0f);
		final float roundNumber = this.roundNumber(new Float(Math.tan(Math.toRadians(angleCorrection))));
		final float roundNumber2 = this.roundNumber(new Float(Math.tan(Math.toRadians(angleCorrection2))));
		for (int i = 0; i < intValue; ++i) {
			final int intValue2 = list.get(i);
			final float n3 = array[i][intValue2 - 1];
			final float n4 = array2[i][intValue2 - 1];
			if (((angleCorrection > 0.0f && angleCorrection < 90.0f && angleCorrection2 > 0.0f
					&& angleCorrection2 < 90.0f)
					|| (angleCorrection > 270.0f && angleCorrection < 360.0f && angleCorrection2 > 270.0f
							&& angleCorrection2 < 360.0f))
					&& n4 <= roundNumber * n3 && n4 >= roundNumber2 * n3) {
				++n2;
			}
			if (((angleCorrection > 90.0f && angleCorrection < 180.0f && angleCorrection2 > 90.0f
					&& angleCorrection2 < 180.0f)
					|| (angleCorrection > 180.0f && angleCorrection < 270.0f && angleCorrection2 > 180.0f
							&& angleCorrection2 < 270.0f))
					&& n4 >= roundNumber * n3 && n4 <= roundNumber2 * n3) {
				++n2;
			}
			if (angleCorrection > 0.0f && angleCorrection < 90.0f) {
				if (angleCorrection2 > 180.0f && angleCorrection2 < 270.0f && n4 <= roundNumber * n3
						&& n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 > 270.0f && angleCorrection2 < 360.0f && n4 <= roundNumber * n3
						&& n4 >= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 270.0f && n3 >= 0.0f && n4 <= roundNumber * n3) {
					++n2;
				}
				if (angleCorrection2 == 0.0f && n4 >= 0.0f && n4 <= roundNumber * n3) {
					++n2;
				}
			}
			if (angleCorrection > 90.0f && angleCorrection < 180.0f) {
				if (angleCorrection2 > 0.0f && angleCorrection2 < 90.0f && n4 >= roundNumber * n3
						&& n4 >= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 > 270.0f && angleCorrection2 < 360.0f && n4 >= roundNumber * n3
						&& n4 >= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 90.0f && n3 <= 0.0f && n4 >= roundNumber * n3) {
					++n2;
				}
				if (angleCorrection2 == 0.0f && n4 >= 0.0f && n4 >= roundNumber * n3) {
					++n2;
				}
			}
			if (angleCorrection > 180.0f && angleCorrection < 270.0f) {
				if (angleCorrection2 > 90.0f && angleCorrection2 < 180.0f && n4 >= roundNumber * n3
						&& n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 > 0.0f && angleCorrection2 < 90.0f && n4 >= roundNumber * n3
						&& n4 >= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 90.0f && n3 <= 0.0f && n4 >= roundNumber * n3) {
					++n2;
				}
				if (angleCorrection2 == 180.0f && n4 <= 0.0f && n4 >= roundNumber * n3) {
					++n2;
				}
			}
			if (angleCorrection > 270.0f && angleCorrection < 360.0f) {
				if (angleCorrection2 > 180.0f && angleCorrection2 < 270.0f && n4 <= roundNumber * n3
						&& n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 > 90.0f && angleCorrection2 < 180.0f && n4 <= roundNumber * n3
						&& n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 180.0f && n4 <= 0.0f && n4 <= roundNumber * n3) {
					++n2;
				}
				if (angleCorrection2 == 270.0f && n3 >= 0.0f && n4 <= roundNumber * n3) {
					++n2;
				}
			}
			if (angleCorrection == 90.0f) {
				if (((angleCorrection2 > 0.0f && angleCorrection2 < 90.0f)
						|| (angleCorrection2 > 270.0f && angleCorrection2 < 360.0f)) && n3 >= 0.0f
						&& n4 >= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 0.0f && n3 >= 0.0f && n4 >= 0.0f) {
					++n2;
				}
				if (angleCorrection2 == 270.0f && n3 >= 0.0f) {
					++n2;
				}
			}
			if (angleCorrection == 180.0f) {
				if (((angleCorrection2 > 0.0f && angleCorrection2 < 90.0f)
						|| (angleCorrection2 > 90.0f && angleCorrection2 < 180.0f)) && n4 >= 0.0f
						&& n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 0.0f && n4 >= 0.0f) {
					++n2;
				}
				if (angleCorrection2 == 90.0f && n3 <= 0.0f && n4 >= 0.0f) {
					++n2;
				}
			}
			if (angleCorrection == 270.0f) {
				if (((angleCorrection2 > 90.0f && angleCorrection2 < 180.0f)
						|| (angleCorrection2 > 180.0f && angleCorrection2 < 270.0f)) && n3 <= 0.0f
						&& n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 90.0f && n3 <= 0.0f) {
					++n2;
				}
				if (angleCorrection2 == 180.0f && n3 <= 0.0f && n4 <= 0.0f) {
					++n2;
				}
			}
			if (angleCorrection == 360.0f) {
				if (angleCorrection2 > 180.0f && angleCorrection2 < 270.0f && n4 <= 0.0f && n4 <= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 > 270.0f && angleCorrection2 < 360.0f && n4 <= 0.0f && n4 >= roundNumber2 * n3) {
					++n2;
				}
				if (angleCorrection2 == 180.0f && n4 <= 0.0f) {
					++n2;
				}
				if (angleCorrection2 == 270.0f && n3 >= 0.0f && n4 <= 0.0f) {
					++n2;
				}
			}
		}
		return n2;
	}

	void plotGraph(final int value, final int n) {
		final String s = (String) this._hashCurrentDataset.get(new Integer(n / 2));
		final ArrayList<Integer> list = (ArrayList<Integer>) this._hashSliceNumber.get(new Integer(n / 2));
		final int intValue = (int) this._hashTracks.get(s);
		final float[][] array = (float[][]) this._listArrayList.get(n);
		final float[][] array2 = (float[][]) this._listArrayList.get(n + 1);
		double roundDoubleNumbers = 0.0;
		double roundDoubleNumbers2 = 0.0;
		int i = 0;
		int j = 0;
		ArrayList<Double> list2 = null;

		for (int k = 0; k < intValue; ++k) {
			final int intValue2 = list.get(k);
			roundDoubleNumbers += array[k][intValue2 - 1];
			roundDoubleNumbers2 += array2[k][intValue2 - 1];
		}
		if (intValue != 0) {
			roundDoubleNumbers /= intValue;
			roundDoubleNumbers2 /= intValue;
		}
		roundDoubleNumbers = this.roundDoubleNumbers(roundDoubleNumbers);
		roundDoubleNumbers2 = this.roundDoubleNumbers(roundDoubleNumbers2);

		final float[] array3 = { 0.0f };
		final Plot plot = new Plot(s, "x axis [" + this._unitsPath + "]", "y axis  [" + this._unitsPath + "]", array3,
				array3);
		if (SPTBatch_.chemoScaling.getText().contains("...") == Boolean.TRUE) {
			final float[] array4 = { this._coordSize, -this._coordSize };
			final float[] array5 = new float[2];
			plot.setLimits((double) (-this._coordSize), (double) this._coordSize, (double) (-this._coordSize),
					(double) this._coordSize);
			plot.addPoints(array4, array5, 2);
			plot.addPoints(array5, array4, 2);
		}
		if (SPTBatch_.chemoScaling.getText().contains("...") == Boolean.FALSE) {
			final float[] array6 = { -Integer.valueOf(SPTBatch_.chemoScaling.getText()),
					Integer.valueOf(SPTBatch_.chemoScaling.getText()) };
			final float[] array7 = new float[2];
			final float[] array8 = { -Integer.valueOf(SPTBatch_.chemoScaling.getText()),
					Integer.valueOf(SPTBatch_.chemoScaling.getText()) };
			plot.setLimits(-Double.valueOf(SPTBatch_.chemoScaling.getText()),
					Double.valueOf(SPTBatch_.chemoScaling.getText()), -Double.valueOf(SPTBatch_.chemoScaling.getText()),
					Double.valueOf(SPTBatch_.chemoScaling.getText()));
			plot.addPoints(array6, array7, 2);
			plot.addPoints(array7, array8, 2);
		}
		plot.draw();
		plot.setLineWidth(1);
		final float[] array9 = { 0.0f };
		final float[] array10 = { 0.0f };

		if (value == 3) {
			for (int index = 0; index < intValue; ++index) {
				final int intValue4 = list.get(index);
				final float[] array13 = new float[intValue4];
				final float[] array14 = new float[intValue4];
				array9[0] = array[index][intValue4 - 1];
				array10[0] = array2[index][intValue4 - 1];
				if (array10[0] >= 0.0f) {
					plot.setColor(Color.black);
					++i;
				} else {
					plot.setColor(Color.red);
					++j;
				}

				for (int n3 = 0; n3 < intValue4; ++n3) {
					array13[n3] = array[index][n3];
					array14[n3] = array2[index][n3];
				}
				plot.setLineWidth(1);
				plot.addPoints(array13, array14, 2);

				plot.setLineWidth(3);
				plot.addPoints(array9, array10, 0);
			}
			plot.setColor(Color.black);

			plot.addLabel(0.0, 0.0, "Number of tracks: " + intValue + "  Counts up: " + i + "  Counts down: " + j);

			plot.addLabel(0.0, 0.04,
					"Center of mass [" + this._unitsPath + "]: x=" + roundDoubleNumbers + " y=" + roundDoubleNumbers2);

		}

		final float[] array19 = { new Float(roundDoubleNumbers) };
		final float[] array20 = { new Float(roundDoubleNumbers2) };
		plot.setColor(Color.blue);
		plot.setLineWidth(3);
		plot.addPoints(array19, array20, 5);

		PlotWindow show;
		if (this._currentOpenWindows.size() < this._listArrayList.size() / 2) {
			// show = plot.show();
			// this._currentOpenWindows.add(show);
			// ((Window) show).addWindowListener(this);
			// ((Frame) show).setResizable(false);
		} else {
			// show = (PlotWindow) this._currentOpenWindows.get(n / 2);
			// show.drawPlot(plot);
		}
		final ArrayList<Integer> value2 = new ArrayList<Integer>();
		value2.add(new Integer(value));
		value2.add(new Integer(n));
		value2.add(new Integer(intValue));
		this._hashPlot.put(s, value2);
		// this._hashWindow.put(s, show);
		// ((Frame) show).setTitle("Chemotaxis Plot for " + s);
		// plot.getImagePlus().show();
		IJ.save(plot.getImagePlus(), SPTBatch_.directChemo + File.separator + "Chemotaxis Plot for "
				+ SPTBatch_.imps.getShortTitle() + ".png");
		// this.gui.butshowSector.setEnabled(true);
		// this.gui.butshowCircle.setEnabled(true);
	}

	void readData(String string) throws FileNotFoundException, IOException {

		this._importedData.add(this.arrayToImport);
		IJ.showStatus("Dataset imported");
		string = this._importedData.size() + ": " + string;
		this._hashImportedDataset.put(string, new Integer(this._importedData.size()));
		// this.gui.datasetImported(string);
	}

	double roundDoubleNumbers(final double n) {
		return Math.round(n * 100.0) / 100.0;
	}

	float roundFloatNumbers(final float n) {
		return Math.round(n * 100.0f) / 100.0f;
	}

	float roundNumber(final float n) {
		return Math.round(n * 10000.0f) / 10000.0f;
	}

	public void stateChanged(final ChangeEvent changeEvent) {
		final int selectedIndex = ((JTabbedPane) changeEvent.getSource()).getSelectedIndex();
		if (selectedIndex == 1) {
			if (this._dialog.auto) {
				PlotWindow.plotHeight = this._plotHeight;
				PlotWindow.plotWidth = this._plotWidth;
			} else {
				PlotWindow.plotHeight = (int) (this._dialog.fraction
						* (Math.abs(this._dialog.minimumY) + Math.abs(this._dialog.maximumY)));
				PlotWindow.plotWidth = (int) (this._dialog.fraction
						* (Math.abs(this._dialog.minimumX) + Math.abs(this._dialog.maximumX)));
			}
		}
		if (selectedIndex == 3) {
			PlotWindow.plotHeight = this._plotHeight;
			PlotWindow.plotWidth = this._plotWidth;
		}
	}

	void updateNumberofTracks(final String s, final int n) {
		if (this._hashTracks.containsKey(s)) {
			this._hashTracks.remove(s);
			this._hashTracks.put(s, new Integer(n));
		} else {
			this._hashTracks.put(s, new Integer(n));
		}
	}

	public void windowActivated(final WindowEvent windowEvent) {
	}

	public void windowClosed(final WindowEvent windowEvent) {
	}

	public void windowClosing(final WindowEvent windowEvent) {
		if (windowEvent.getSource() == this.gui) {
			if (this._openInfoWindows != null) {
				for (int i = 0; i < this._openInfoWindows.size(); ++i) {
					((JFrame) this._openInfoWindows.get(i)).dispose();
				}
				this._openInfoWindows.clear();
			}
			this._currentOpenWindows.clear();
			this._currentOpenDiagrams.clear();
			WindowManager.closeAllWindows();
			this.gui.dispose();
		} else {
			if (windowEvent.getSource() instanceof JFrame && this._openInfoWindows.contains(windowEvent.getSource())) {
				this._openInfoWindows.remove(windowEvent.getSource());
			}
			if (windowEvent.getSource() instanceof PlotWindow) {
				if (this._currentOpenWindows.contains(windowEvent.getSource())) {
					for (int j = 0; j < this._currentOpenWindows.size(); ++j) {
						((Window) this._currentOpenWindows.get(j)).dispose();
					}
					this._currentOpenWindows.clear();
					this.gui.plotClosed();
				}
				if (this._currentOpenDiagrams.contains(windowEvent.getSource())) {
					for (int k = 0; k < this._currentOpenDiagrams.size(); ++k) {
						((Window) this._currentOpenDiagrams.get(k)).dispose();
					}
					this._currentOpenDiagrams.clear();
				}
			}
		}
	}

	public void windowDeactivated(final WindowEvent windowEvent) {
	}

	public void windowDeiconified(final WindowEvent windowEvent) {
	}

	public void windowIconified(final WindowEvent windowEvent) {
	}

	public void windowOpened(final WindowEvent windowEvent) {
	}
}
