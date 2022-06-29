package edu.kit.exp.server.gui.starttab;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents a JTable for Experiments. It is integrated into the
 * start tab.
 * 
 * @see StartTab
 */
public class ExperimentTable extends JTable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8296076701639918834L;

	/**
	 * This constructor instantiates a new experiment table.
	 * 
	 * @param tableModel
	 *            A DefaultTableModel variable that contains the table model.
	 * @see DefaultTableModel
	 */
	public ExperimentTable(DefaultTableModel tableModel) {
		super(tableModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int x, int y) {

		if (y == 0) {
			return false;
		}

		return false;
	}

}
