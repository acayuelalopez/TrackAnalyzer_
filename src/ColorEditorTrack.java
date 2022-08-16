import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ij.IJ;
import ij.plugin.frame.RoiManager;

public class ColorEditorTrack extends AbstractCellEditor implements TableCellEditor {
	private JPanel myPanel, panelAdd, panelEdit;
	private JLabel labelInitt;
	private int result, input;
	static DefaultTableModel modelC;
	static JTable tableC;
	private JButton addButton, editButton, deleteButton, colorButtonAdd, colorButtonEdit, okButton, okButtonEdit,
			cancelButton, cancelButtonEdit, okButtonAdd, cancelButtonAdd;
	static JList<String> featureList, classList;
	static DefaultListModel<String> modelListFeature, modelListClass;
	private JLabel addTextAdd, addTextEdit;
	private JTextField addTextFAdd, addTextFEdit;
	private Color currentColorAdd, currentColorEdit, colorCInitial, colorCFinal;
	private Object labelC, colorC, featureC;
	private String addTextInitial, addTextFinal, featureInitial, featureFinal;
	static JFrame myFrame, myFrameAdd, myFrameEdit;
	private Icon iconOKCell, iconCancelCell;
	private int indexRowC;

	public ColorEditorTrack(JList<String> featureList) {
		this.featureList = featureList;

		addButton = new JButton("");
		addButton.setBounds(50, 100, 95, 30);
		ImageIcon iconAdd = FirstWizardPanel.createImageIcon("images/add.png");
		Icon iconAddCell = new ImageIcon(iconAdd.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		addButton.setIcon(iconAddCell);
		addButton.setToolTipText("Click this button to add your class-label.");
		editButton = new JButton("");
		editButton.setBounds(50, 100, 95, 30);
		ImageIcon iconEdit = FirstWizardPanel.createImageIcon("images/edit.png");
		Icon iconEditCell = new ImageIcon(iconEdit.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		editButton.setIcon(iconEditCell);
		editButton.setToolTipText("Click this button to edit your class-label.");
		deleteButton = new JButton("");
		deleteButton.setBounds(50, 100, 95, 30);
		ImageIcon iconDelete = FirstWizardPanel.createImageIcon("images/bin.png");
		Icon iconDeleteCell = new ImageIcon(iconDelete.getImage().getScaledInstance(22, 20, Image.SCALE_SMOOTH));
		deleteButton.setIcon(iconDeleteCell);
		deleteButton.setToolTipText("Click this button to delete your class-label.");
		myFrame = new JFrame("Manage Labels");
		myFrame.setLocation(new Point(100, 100));
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		Object[][] rowData2 = {};
		Object columnNames[] = { "Name", "Color", "Feature" };

		modelC = new DefaultTableModel(rowData2, columnNames) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			// Returning the Class of each column will allow different
			// renderers to be used based on Class

			public boolean isCellEditable(int row, int col) {
				return false;

			}

			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}

				return super.getColumnClass(column);
			}
		};

		tableC = new JTable();
		tableC.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tableC.setSelectionBackground(new Color(229, 255, 204));
		tableC.setSelectionForeground(new Color(0, 102, 0));
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(modelC);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableC.setDefaultRenderer(JLabel.class, centerRenderer);
		tableC.setRowSorter(rowSorter);
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout());
		panelButtons.add(addButton);
		panelButtons.add(editButton);
		panelButtons.add(deleteButton);
		tableC.setAutoCreateRowSorter(true);
		tableC.setEnabled(true);
		tableC.setCellSelectionEnabled(true);
		// tableC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableC.setRowSelectionAllowed(true);
		tableC.setColumnSelectionAllowed(false);
		tableC.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableC.setDefaultRenderer(JLabel.class, new Renderer());
		tableC.setDefaultRenderer(Color.class, new ColorRenderer(true));
		tableC.setModel(modelC);
		TableColumn column1 = null;
		column1 = tableC.getColumnModel().getColumn(0); // this was the column which was not highlighted
		column1.setPreferredWidth(7);
		column1.setCellRenderer(new ResultRendererC());
		TableColumn column2 = null;
		column2 = tableC.getColumnModel().getColumn(1); // this was the column which was not highlighted
		column2.setPreferredWidth(5);
		column2.setCellRenderer(new ResultRendererC());
		TableColumn column3 = null;
		column3 = tableC.getColumnModel().getColumn(2); // this was the column which was not highlighted
		column3.setPreferredWidth(15);
		column3.setCellRenderer(new ResultRendererC());
		JScrollPane scrollPane = new JScrollPane(tableC);
		for (int u = 0; u < tableC.getColumnCount(); u++)
			tableC.getColumnModel().getColumn(u).setPreferredWidth(90);
		tableC.setRowHeight(25);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(panelButtons);
		myPanel.add(scrollPane, BorderLayout.CENTER);
		myPanel.setSize(300, 150);
		// frame.setVisible(true);
		myPanel.add(Box.createHorizontalStrut(15));
		okButton = new JButton("");
		okButton.setBounds(50, 100, 95, 30);
		ImageIcon iconOk = FirstWizardPanel.createImageIcon("images/add.png");
		iconOKCell = new ImageIcon(iconOk.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		okButton.setIcon(iconOKCell);
		okButton.setToolTipText("Click this button to edit your color selection.");
		cancelButton = new JButton("");
		cancelButton.setBounds(50, 100, 95, 30);
		ImageIcon iconCancel = FirstWizardPanel.createImageIcon("images/cancel.png");
		iconCancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		cancelButton.setIcon(iconCancelCell);
		cancelButton.setToolTipText("Click this button to cancel your color selection.");
		JPanel panelOkCancel = new JPanel();
		panelOkCancel.setLayout(new FlowLayout());
		panelOkCancel.add(okButton);
		panelOkCancel.add(cancelButton);
		myPanel.add(panelOkCancel);
		myFrame.getContentPane().add(myPanel);
		myFrame.pack();
		myFrame.setLocationByPlatform(true);

		///
		panelAdd = new JPanel();
		panelAdd.setPreferredSize(new Dimension(200, 100));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		panelAdd.setLayout(new FlowLayout());
		addTextAdd = new JLabel("Label Name: ");
		addTextFAdd = new JTextField(8);
		panel1.add(addTextAdd);
		panel1.add(addTextFAdd);
		JLabel pickC = new JLabel("Pick a color: ");
		panelAdd.add(pickC);
		colorButtonAdd = new JButton();
		colorButtonAdd.setPreferredSize(new Dimension(200, 75));

		panelAdd.add(colorButtonAdd);
		okButtonAdd = new JButton("");
		okButtonAdd.setBounds(50, 100, 95, 30);
		okButtonAdd.setIcon(iconOKCell);
		okButtonAdd.setToolTipText("Click this button to edit your color selection.");
		cancelButtonAdd = new JButton("");
		cancelButtonAdd.setBounds(50, 100, 95, 30);
		cancelButtonAdd.setIcon(iconCancelCell);
		cancelButtonAdd.setToolTipText("Click this button to cancel your color selection.");
		JPanel panelOkCancelAdd = new JPanel();
		panelOkCancelAdd.setLayout(new FlowLayout());
		panelOkCancelAdd.add(okButtonAdd);
		panelOkCancelAdd.add(cancelButtonAdd);
		myFrameAdd = new JFrame("Add Label");
		JPanel mainPanel = new JPanel();
		mainPanel.add(panel1);
		mainPanel.add(panelAdd);
		mainPanel.add(panelOkCancelAdd);
		myFrameAdd.setPreferredSize(new Dimension(250, 250));
		myFrameAdd.getContentPane().add(mainPanel);
		myFrameAdd.pack();
		myFrameAdd.setLocationByPlatform(true);

		/////

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 100));
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		panel.setLayout(new FlowLayout());
		addTextEdit = new JLabel("Label Name: ");
		addTextFEdit = new JTextField(8);
		panel2.add(addTextEdit);
		panel2.add(addTextFEdit);
		JLabel pickEdit = new JLabel("Pick a Color: ");
		panel.add(pickEdit);
		colorButtonEdit = new JButton();
		colorButtonEdit.setPreferredSize(new Dimension(200, 75));
		panel.add(colorButtonEdit);
		okButtonEdit = new JButton("");
		okButtonEdit.setBounds(50, 100, 95, 30);
		okButtonEdit.setIcon(iconOKCell);
		okButtonEdit.setToolTipText("Click this button to edit your color selection.");
		cancelButtonEdit = new JButton("");
		cancelButtonEdit.setBounds(50, 100, 95, 30);
		cancelButtonEdit.setIcon(iconCancelCell);
		cancelButtonEdit.setToolTipText("Click this button to cancel your color selection.");
		JPanel panelOkCancelEdit = new JPanel();
		panelOkCancelEdit.setLayout(new FlowLayout());
		panelOkCancelEdit.add(okButtonEdit);
		panelOkCancelEdit.add(cancelButtonEdit);
		myFrameEdit = new JFrame("Edit Label");
		JPanel mainPanelEdit = new JPanel();
		mainPanelEdit.add(panel2);
		mainPanelEdit.add(panel);
		mainPanelEdit.add(panelOkCancelEdit);
		myFrameEdit.setPreferredSize(new Dimension(250, 250));
		myFrameEdit.getContentPane().add(mainPanelEdit);
		myFrameEdit.pack();
		myFrameEdit.setLocationByPlatform(true);

		///
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableC.setRowHeight(featureList.getHeight());
				myFrameAdd.setVisible(true);

			}

		});
		okButtonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel labelString = new JLabel();
				JLabel labelColor = new JLabel();
				JLabel labelFeature = new JLabel();
				labelColor.setText("");
				labelColor.setBackground(currentColorAdd);
				labelString.setText(addTextFAdd.getText());
				labelString.setHorizontalAlignment(SwingConstants.CENTER);
				labelString.setBackground(currentColorAdd);
				labelColor.setOpaque(true);
				StringBuilder filterItems = new StringBuilder();
				for (int x = 0; x < featureList.getModel().getSize(); x++)
					filterItems.append(featureList.getModel().getElementAt(x)).append("<br>");
				labelFeature.setText("<html>" + filterItems.toString() + "</html>");
				modelC.addRow(new Object[] { labelString, labelColor, labelFeature });
				modelC.fireTableDataChanged();
				tableC.repaint();

				myFrameAdd.dispatchEvent(new WindowEvent(myFrameAdd, WindowEvent.WINDOW_CLOSING));
			}

		});

		cancelButtonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrameAdd.dispatchEvent(new WindowEvent(myFrameAdd, WindowEvent.WINDOW_CLOSING));
			}

		});
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				myFrameEdit.setVisible(true);

				indexRowC = tableC.getSelectedRow();
				if (tableC.getSelectedRowCount() == 0)
					return;
				if (tableC.getSelectedRowCount() == 1) {
					labelC = new Object();
					colorC = new Object();
					labelC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
					colorC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));
					addTextInitial = ((JLabel) labelC).getText();
					colorCInitial = ((JLabel) colorC).getBackground();
				}

				colorButtonEdit.setBackground(((JLabel) colorC).getBackground());
				currentColorEdit = ((JLabel) colorC).getBackground();
				colorButtonEdit.setContentAreaFilled(false);
				colorButtonEdit.setOpaque(true);

				addTextFEdit.setText(((JLabel) labelC).getText());

			}
		});

		colorButtonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.Locale.setDefault(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.getDefault());
				currentColorAdd = JColorChooser.showDialog(null, "Pick a Color: ", colorButtonAdd.getBackground());
				if (currentColorAdd != null)
					colorButtonAdd.setBackground(currentColorAdd);
			}
		});

		okButtonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel labelString = new JLabel();
				JLabel labelColor = new JLabel();
				labelColor.setText("");
				labelColor.setBackground(currentColorEdit);
				labelString.setText(addTextFEdit.getText());
				labelString.setHorizontalAlignment(SwingConstants.CENTER);
				labelString.setBackground(currentColorEdit);
				labelColor.setOpaque(true);
				addTextFinal = ((JLabel) labelString).getText();
				colorCFinal = ((JLabel) labelColor).getBackground();

				if (addTextFinal.equals(addTextInitial) == false)
					modelC.setValueAt(labelString, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
				if (addTextFinal.equals(addTextInitial) == true)
					modelC.setValueAt(labelC, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
				if (currentColorEdit != colorCInitial)
					modelC.setValueAt(labelColor, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));
				if (currentColorEdit == colorCInitial)
					modelC.setValueAt(colorC, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));

				modelC.fireTableCellUpdated(tableC.convertRowIndexToModel(indexRowC),
						tableC.convertColumnIndexToModel(0));
				modelC.fireTableCellUpdated(tableC.convertRowIndexToModel(indexRowC),
						tableC.convertColumnIndexToModel(1));
				tableC.repaint();

				myFrameEdit.dispatchEvent(new WindowEvent(myFrameEdit, WindowEvent.WINDOW_CLOSING));

			}

		});

		cancelButtonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrameEdit.dispatchEvent(new WindowEvent(myFrameEdit, WindowEvent.WINDOW_CLOSING));
			}

		});

	}

	public void setClassAction() {
		colorButtonEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.Locale.setDefault(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.getDefault());
				currentColorEdit = JColorChooser.showDialog(null, "Pick a Color: ", colorButtonEdit.getBackground());
				if (currentColorEdit != null)
					colorButtonEdit.setBackground(currentColorEdit);
			}
		});
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (tableC.getSelectedRowCount() <= 0)
					myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));

				if (tableC.getSelectedRowCount() == 1) {

					List<String> listClasses = new ArrayList<String>();
					classList = ChooserWizardPanel.classList;
					modelListClass = ChooserWizardPanel.modelListClass;
					int selectedRow = tableC.getSelectedRow();

					if (modelListClass.getSize() == 0) {
						modelListClass.addElement(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText());
						JLabel labelsTableC = new JLabel();
						for (int i = 0; i < tableC.getModel().getRowCount(); i++) {
							labelsTableC.setText(((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(0))).getText());
							labelsTableC.setHorizontalAlignment(SwingConstants.CENTER);
							labelsTableC.setBackground(((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(1))).getBackground());
							labelsTableC.setOpaque(true);
						}

						String filterFeature[] = ((JLabel) tableC.getModel().getValueAt(selectedRow,
								tableC.convertColumnIndexToModel(2))).getText().replace("</html>", "")
										.replace("<html>", "").split("<br>");
						List<String> features = new ArrayList<String>();
						List<String> featureMin = new ArrayList<String>();
						List<String> featureMax = new ArrayList<String>();
						for (int i = 0; i < filterFeature.length; i++) {
							features.add(filterFeature[i].substring(0, filterFeature[i].indexOf(":")));
							featureMin.add(filterFeature[i].substring(filterFeature[i].indexOf("[") + 1,
									filterFeature[i].indexOf(",")));
							featureMax.add(filterFeature[i].substring(filterFeature[i].indexOf(",") + 1,
									filterFeature[i].indexOf("]")));

						}
						for (int x = 0; x < ChooserWizardPanel.modelTrack.getRowCount(); x++)
							for (int u = 0; u < features.size(); u++)
								if (((Double.parseDouble(ChooserWizardPanel.tableTrack.getModel()
										.getValueAt(x,
												ChooserWizardPanel.tableTrack.getColumn(features.get(u)).getModelIndex())
										.toString()) >= Double.parseDouble(featureMin.get(u)))
										&& (Double.parseDouble(ChooserWizardPanel.tableTrack.getModel()
												.getValueAt(x,
														ChooserWizardPanel.tableTrack.getColumn(features.get(u))
																.getModelIndex())
												.toString()) <= Double.parseDouble(featureMax.get(u))))) {
									ChooserWizardPanel.tableTrack.getModel().setValueAt(labelsTableC,
											ChooserWizardPanel.tableTrack.convertRowIndexToModel(x),
											ChooserWizardPanel.tableTrack.convertColumnIndexToModel(1));
								}

					}

					if (modelListClass.getSize() >= 1) {
						for (int i = 0; i < modelListClass.getSize(); i++)
							listClasses.add(modelListClass.getElementAt(i));

						if (listClasses.contains(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText()) == false) {
							modelListClass.addElement(
									((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
											tableC.convertColumnIndexToModel(0))).getText());

							JLabel labelsTableC = new JLabel();
							for (int i = 0; i < tableC.getModel().getRowCount(); i++) {
								labelsTableC.setText(((JLabel) tableC.getModel().getValueAt(selectedRow,
										tableC.convertColumnIndexToModel(0))).getText());
								labelsTableC.setHorizontalAlignment(SwingConstants.CENTER);
								labelsTableC.setBackground(((JLabel) tableC.getModel().getValueAt(selectedRow,
										tableC.convertColumnIndexToModel(1))).getBackground());
								labelsTableC.setOpaque(true);
							}

							String filterFeature[] = ((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(2))).getText().replace("</html>", "")
											.replace("<html>", "").split("<br>");
							List<String> features = new ArrayList<String>();
							List<String> featureMin = new ArrayList<String>();
							List<String> featureMax = new ArrayList<String>();
							for (int i = 0; i < filterFeature.length; i++) {
								features.add(filterFeature[i].substring(0, filterFeature[i].indexOf(":")));
								featureMin.add(filterFeature[i].substring(filterFeature[i].indexOf("[") + 1,
										filterFeature[i].indexOf(",")));
								featureMax.add(filterFeature[i].substring(filterFeature[i].indexOf(",") + 1,
										filterFeature[i].indexOf("]")));

							}
							for (int x = 0; x < ChooserWizardPanel.modelTrack.getRowCount(); x++)
								for (int u = 0; u < features.size(); u++)
									if (((Double.parseDouble(ChooserWizardPanel.tableTrack.getModel()
											.getValueAt(x,
													ChooserWizardPanel.tableTrack.getColumn(features.get(u))
															.getModelIndex())
											.toString()) >= Double.parseDouble(featureMin.get(u)))
											&& (Double.parseDouble(ChooserWizardPanel.tableTrack.getModel()
													.getValueAt(x,
															ChooserWizardPanel.tableTrack.getColumn(features.get(u))
																	.getModelIndex())
													.toString()) <= Double.parseDouble(featureMax.get(u))))) {
										ChooserWizardPanel.tableTrack.getModel().setValueAt(labelsTableC,
												ChooserWizardPanel.tableTrack.convertRowIndexToModel(x),
												ChooserWizardPanel.tableTrack.convertColumnIndexToModel(1));
									}

						}

						if (listClasses.contains(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText()) == true)
							myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
					}
				}

			}

		});

		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
			}

		});

		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object labelC = null;
				Object colorC = null;
				Object[] labelsC = null;
				Object[] colorsC = null;
				int[] indexesRowC = tableC.getSelectedRows();
				int indexRowC = tableC.getSelectedRow();
				if (tableC.getSelectedRowCount() == 1) {
					labelC = new Object();
					colorC = new Object();
					labelC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
					colorC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));
				}
				labelsC = new Object[indexesRowC.length];
				colorsC = new Object[indexesRowC.length];
				if (tableC.getSelectedRowCount() > 1) {

					for (int k = 0; k < indexesRowC.length; k++) {
						labelsC[k] = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexesRowC[k]),
								tableC.convertColumnIndexToModel(0));
						colorsC[k] = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexesRowC[k]),
								tableC.convertColumnIndexToModel(1));
					}
				}

				java.util.Locale.setDefault(java.util.Locale.ENGLISH);
				JOptionPane.setDefaultLocale(java.util.Locale.ENGLISH.getDefault());
				if (tableC.getSelectedRowCount() > 1) {
					String labelsCtoString[] = new String[indexesRowC.length];
					for (int k = 0; k < indexesRowC.length; k++)
						labelsCtoString[k] = (((JLabel) labelsC[k]).getText());
					input = JOptionPane.showConfirmDialog(null, "Are you sure to delete the selected labels?",
							"Delete a label", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

					if (input == JOptionPane.YES_OPTION) {
						for (int f = 0; f < indexesRowC.length; f++)
							modelC.removeRow(indexesRowC[f] - f);
						modelC.fireTableDataChanged();
						tableC.repaint();

					}
					if (input == JOptionPane.NO_OPTION)
						return;
					if (input == JOptionPane.CANCEL_OPTION)
						return;

				}
				if (tableC.getSelectedRowCount() == 1) {
					String labelCtoString = (((JLabel) labelC).getText());
					input = JOptionPane.showConfirmDialog(null,
							"Are you sure to delete the following label?----- " + labelCtoString, "Delete a label",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

					if (input == JOptionPane.YES_OPTION) {
						modelC.removeRow(indexRowC);
						modelC.fireTableDataChanged();

					}
					if (input == JOptionPane.NO_OPTION)
						return;
					if (input == JOptionPane.CANCEL_OPTION)
						return;

				}
			}

		});

	}

	public Object getCellEditorValueAdd() {
		return currentColorAdd;
	}

	public Object getCellEditorValueEdit() {
		return currentColorEdit;
	}

	public Component getTableCellEditorComponentAdd(JTable table, Object value, boolean isSelected, int row,
			int column) {
		currentColorAdd = (Color) value;
		return colorButtonAdd;
	}

	public Component getTableCellEditorComponentEdit(JTable table, Object value, boolean isSelected, int row,
			int column) {
		currentColorEdit = (Color) value;
		return colorButtonEdit;
	}

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

}