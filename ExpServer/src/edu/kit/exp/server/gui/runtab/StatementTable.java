package edu.kit.exp.server.gui.runtab;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StatementTable extends JTable {

	private static final long serialVersionUID = -8296076701639918834L;

	public StatementTable(DefaultTableModel tableModel) {
		super(tableModel);
	}

	@Override
	public boolean isCellEditable(int x, int y) {

		if (y == 0) {
			return false;
		}

		return true;
	}

}
// Liang
