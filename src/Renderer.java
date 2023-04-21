import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Renderer extends DefaultTableCellRenderer {

	/*
	 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean,
	 * boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		value = table.getModel().getValueAt(row, 0);

		if (value.equals(true)) {
			comp.setBackground(Color.LIGHT_GRAY);// table.getSelectionForeground());
			// comp.setBackground(currentColor);//table.getSelectionBackground());
		} else {
			comp.setForeground(Color.white);

		}

		return comp;
	}
}