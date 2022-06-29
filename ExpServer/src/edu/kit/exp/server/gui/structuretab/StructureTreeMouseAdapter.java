package edu.kit.exp.server.gui.structuretab;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.structure.StructureManagementException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class is a mouse adapter for the experiment tree.
 * 
 */
public class StructureTreeMouseAdapter implements MouseListener {

	/** The experiment tree. */
	private JTree experimentTree;

	/** The experiment builder. */
	private StructureTreeBuilder experimentBuilder;

	/** The node. */
	private DefaultMutableTreeNode node = null;

	/** The menu. */
	private JPopupMenu menu = null;

	/** The item add session. */
	private JMenuItem itemAddSession = null;

	/** The item add treatment block. */
	private JMenuItem itemAddTreatmentBlock;

	/** The item adds a single period. */
	private JMenuItem itemAddSinglePeriod;

	/** The item adds multiple periods. */
	private JMenuItem itemAddMultiplePeriods;

	/** The item remove. */
	private JMenuItem itemRemove;

	/** The item add quiz. */
	private JMenuItem itemAddQuiz;

	/** The item add questionnaire. */
	private JMenuItem itemAddQuestionnaire;

	/** The item add pause. */
	private JMenuItem itemAddPause;

	/** The item add practice treatment block. */
	private JMenuItem itemAddPracticeTreatmentBlock;
	private JMenuItem itemCopySession;
	private JMenuItem itemCopyTreatmentBlock;
	private boolean disable;

	/**
	 * This constructor instantiates a new structure tree mouse adapter.
	 * 
	 * @param experimentBuilder
	 *            The
	 *            {@link edu.kit.exp.server.gui.structuretab.StructureTreeBuilder
	 *            StructureTreeBuilder} variable which contains an experiment
	 *            tree builder.
	 */
	public StructureTreeMouseAdapter(StructureTreeBuilder experimentBuilder) {
		super();

		this.experimentBuilder = experimentBuilder;
		this.experimentTree = experimentBuilder.getTree();

		initMenuItems();
	}

	/**
	 * This method initializes menu items and their actions.
	 */
	private void initMenuItems() {

		itemAddSession = new JMenuItem("Add Session");
		itemAddSession.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addSession();

			}
		});

		itemAddTreatmentBlock = new JMenuItem("Add Treatment Block");
		itemAddTreatmentBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addTreatmentBlock();

			}
		});

		itemAddPracticeTreatmentBlock = new JMenuItem("Add Practice Treatment Block");
		itemAddPracticeTreatmentBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPracticeTreatmentBlock();

			}
		});

		itemCopySession = new JMenuItem("Copy Session");
		itemCopySession.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				Session selectedSession = null;
				try {
					selectedSession = StructureTabController.getInstance().getSelectedSession();
				} catch (DataInputException | StructureManagementException e) {
                    LogHandler.printException(e);
				}

				experimentBuilder.copySession(selectedSession.getIdSession());

			}
		});

		itemCopyTreatmentBlock = new JMenuItem("Copy TreatmentBlock");
		itemCopyTreatmentBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				TreatmentBlock treatmentBlock = null;
				try {
					treatmentBlock = StructureTreeBuilder.getInstance().getSelectedTreatmentBlock();
				} catch (DataInputException e) {
					LogHandler.printException(e);
				}

				experimentBuilder.copyTreatmentBlock(treatmentBlock.getIdsequenceElement());
			}
		});

		itemAddSinglePeriod = new JMenuItem("Add Single Period");
		itemAddSinglePeriod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPeriod();
			}
		});

		itemAddMultiplePeriods = new JMenuItem("Add Multiple Periods");
		itemAddMultiplePeriods.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPeriods();
			}
		});

		itemRemove = new JMenuItem("Remove");
		itemRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.removeItem();
			}
		});

		itemAddQuiz = new JMenuItem("Add Quiz");
		itemAddQuiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addQuiz();
			}
		});

		itemAddQuestionnaire = new JMenuItem("Add Questionnaire");
		itemAddQuestionnaire.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addQuestionnaire();
			}
		});

		itemAddPause = new JMenuItem("Add Pause");
		itemAddPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPause();
			}
		});

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (!disable) {

			int selRow = experimentTree.getRowForLocation(e.getX(), e.getY());

			if (selRow != -1) {

				TreePath selPath = experimentTree.getPathForLocation(e.getX(), e.getY());

				experimentTree.setSelectionPath(selPath);
				experimentTree.scrollPathToVisible(selPath);
				node = (DefaultMutableTreeNode) experimentTree.getLastSelectedPathComponent();

				if (e.getButton() == MouseEvent.BUTTON3) {

					if (e.isPopupTrigger()) {

						showPopupMenu(node.getUserObject(), e.getComponent(), e.getX(), e.getY());

					}
				}

				experimentBuilder.refreshDetails(node.getUserObject());
				experimentBuilder.setSelectedNodeObject(node.getUserObject());

			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (!disable) {

			int selRow = experimentTree.getRowForLocation(e.getX(), e.getY());

			if (selRow != -1) {

				TreePath selPath = experimentTree.getPathForLocation(e.getX(), e.getY());

				experimentTree.setSelectionPath(selPath);
				experimentTree.scrollPathToVisible(selPath);
				node = (DefaultMutableTreeNode) experimentTree.getLastSelectedPathComponent();

				if (e.getButton() == MouseEvent.BUTTON3) {

					if (e.isPopupTrigger()) {

						showPopupMenu(node.getUserObject(), e.getComponent(), e.getX(), e.getY());

					}
				}

				experimentBuilder.refreshDetails(node.getUserObject());
				experimentBuilder.setSelectedNodeObject(node.getUserObject());

			}
        }

	}

	/**
	 * This method shows a context menu for a specific Node.
	 * 
	 * @param userObject
	 *            the user object
	 * @param component
	 *            the component
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	private void showPopupMenu(Object userObject, Component component, int x, int y) {

		menu = new JPopupMenu();

		if (userObject.getClass().equals(Experiment.class)) {
			menu.add(itemAddSession);
		}

		if (userObject.getClass().equals(Session.class)) {
			menu.add(itemAddTreatmentBlock);
			menu.add(itemAddPracticeTreatmentBlock);
			menu.add(itemAddQuiz);
			menu.add(itemAddQuestionnaire);
			menu.add(itemAddPause);
			menu.add(itemCopySession);
		}

		if (userObject.getClass().equals(TreatmentBlock.class)) {
			menu.add(itemAddSinglePeriod);
			menu.add(itemAddMultiplePeriods);
			menu.add(itemCopyTreatmentBlock);
		}

		menu.add(itemRemove);
		menu.show(component, x, y);
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}
}
