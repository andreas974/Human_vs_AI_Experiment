package edu.kit.exp.server.gui.structuretab;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.*;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

/**
 * This class represents the GUI element to create the structure of an
 * experiment and to define its elements.
 * 
 */
public class StructureTreeBuilder {

	/** The Constant DIVIDER_LOCATION. */
	private static final int DIVIDER_LOCATION = 600;

	/** The gui controller. */
	private StructureTabController guiController = StructureTabController.getInstance();

	/** The label info. */
	private JLabel labelInfo;

	/** The split pane. */
	private JSplitPane splitPane;

	/** The left scroll pane. */
	private JScrollPane leftScrollPane;

	/** The right scroll pane. */
	private JScrollPane rightScrollPane;

	/** The root node. */
	private DefaultMutableTreeNode rootNode;

	/** The tree model. */
	private DefaultTreeModel treeModel;

	/** The experiment tree. */
	private JTree experimentTree;

	/** The experiment. */
	private Experiment experiment;

	/** The details panel. */
	private JPanel detailsPanel;

	/** The right panel. */
	private JPanel rightPanel;

	/** The experiment builder panel. */
	private JPanel experimentBuilderPanel;

	/** The expanded nodes. */
	private ArrayList<Object> expandedNodes;

	/** The path map. */
	private HashMap<Object, TreeNode[]> pathMap = new HashMap<>();

	/** The selected node object. */
	private Object selectedNodeObject;

	/** The instance. */
	private static StructureTreeBuilder instance = new StructureTreeBuilder();

	private static SessionManagement sessionInstance = SessionManagement.getInstance();

	/**
	 * This constructor instantiates a new structure tree builder.
	 */
	private StructureTreeBuilder() {
		experimentBuilderPanel = new JPanel(new GridLayout(1, 0));
		experimentBuilderPanel.setBackground(Color.WHITE);
	}

	/**
	 * This method gets the single instance of StructureTreeBuilder.
	 * 
	 * @return a single instance of StructureTreeBuilder
	 */
	public static StructureTreeBuilder getInstance() {
		return instance;
	}

	/**
	 * This method creates the editable experiment tree.
	 * 
	 * @param experiment
	 *            The {@link edu.kit.exp.server.jpa.entity.Experiment
	 *            Experiment} an experiment tree is created for.
	 * @return a JPanel which contains the editable experiment tree
	 */
	public JPanel createExperimentBuilder(Experiment experiment) {

		boolean reload = false;

		if (experimentTree != null) {
			reload = true;
			saveStatus();
		}

		this.experiment = experiment;
		experimentBuilderPanel.removeAll();

		initLeft();
		initRight();

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightPanel);
		splitPane.setDividerLocation(DIVIDER_LOCATION);
		splitPane.revalidate();

		experimentBuilderPanel.add(splitPane);

		if (reload) {
			for (Object se : expandedNodes) {

				TreeNode[] tn = pathMap.get(se);

				if (tn != null) {
					experimentTree.expandPath(new TreePath(tn));
				}
			}
			reselectPath(rootNode);
			refreshDetails(selectedNodeObject);
		}

		experimentBuilderPanel.revalidate();
		return experimentBuilderPanel;

	}

	/**
	 * This method initializes the JPanel on the right side.
	 */
	private void initRight() {

		rightPanel = new JPanel(new GridLayout(1, 0));
		rightPanel.setBackground(Color.WHITE);

		labelInfo = new JLabel("Select a experiment component to see its details!");

		detailsPanel = new JPanel();
		detailsPanel.setBackground(Color.WHITE);
		detailsPanel.setLayout(new GridLayout(1, 0));
		detailsPanel.add(labelInfo);
		rightScrollPane = new JScrollPane(detailsPanel);

		Dimension size = rightScrollPane.getSize();
		rightPanel.setLayout(new GridLayout(0, 1, 0, 0));
		rightScrollPane.setMaximumSize(new Dimension(150, (int) size.getHeight()));
		rightPanel.add(rightScrollPane);

	}

	/**
	 * This method initializes the experiment tree.
	 */
	private synchronized void initLeft() {

		rootNode = new DefaultMutableTreeNode(experiment.getName());
		rootNode.setUserObject(experiment);

		// Copy data to independent vectors to avoid concurrent access
		// exceptions
		Vector<Session> vectorSession = new Vector<>(experiment.getSessions());
		Collections.sort(vectorSession); /* Sort Sessions */
		Vector<Vector<SequenceElement>> vectorSequenceElements = new Vector<>();
		HashMap<String, Vector<Period>> periodVectorMap = new HashMap<>();

		for (Session session : vectorSession) {
			vectorSequenceElements.add(new Vector<>(session.getSequenceElements()));
		}

		for (int sessionIndex = 0; sessionIndex < vectorSequenceElements.size(); sessionIndex++) {

			Vector<SequenceElement> sequneceElementVector = vectorSequenceElements.get(sessionIndex);
			Collections.sort(sequneceElementVector); // Sort Sequence Elements

			for (int sequenceElementIndex = 0; sequenceElementIndex < sequneceElementVector.size(); sequenceElementIndex++) {

				SequenceElement sequenceElement = sequneceElementVector.get(sequenceElementIndex);

				if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;
					List<Period> periods = treatmentBlock.getPeriods();
					Vector<Period> pVektor = new Vector<>(periods);
					String key = sessionIndex + "," + sequenceElementIndex;
					periodVectorMap.put(key, pVektor);
				}
			}
		}

		// Build Tree
		for (int sessionIndex = 0; sessionIndex < vectorSession.size(); sessionIndex++) {

			Session session = vectorSession.get(sessionIndex);
			DefaultMutableTreeNode sessionNode = new DefaultMutableTreeNode(session);
			rootNode.add(sessionNode);

			pathMap.put(session, sessionNode.getPath());

			List<SequenceElement> sequenceElementsOfSession = vectorSequenceElements.get(sessionIndex);

			for (int treatmentBlockIndex = 0; treatmentBlockIndex < sequenceElementsOfSession.size(); treatmentBlockIndex++) {

				SequenceElement se = sequenceElementsOfSession.get(treatmentBlockIndex);

				if (se.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) se;
					DefaultMutableTreeNode treatmentBlockNode = new DefaultMutableTreeNode(treatmentBlock);
					pathMap.put(treatmentBlock, treatmentBlockNode.getPath());
					sessionNode.add(treatmentBlockNode);

					String key = sessionIndex + "," + treatmentBlockIndex;
					Vector<Period> periods = periodVectorMap.get(key);
                    Collections.sort(periods);

					for (int periodIndex = 0; periodIndex < periods.size(); periodIndex++) {

						Period period = periods.get(periodIndex);
						DefaultMutableTreeNode periodNode = new DefaultMutableTreeNode(period);
						pathMap.put(period, periodNode.getPath());
						treatmentBlockNode.add(periodNode);
					}
				} else {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(se);
					sessionNode.add(node);
				}

			}
		}

        treeModel = new DefaultTreeModel(rootNode);
        experimentTree = new JTree(treeModel);
        experimentTree.setDragEnabled(true);
        experimentTree.setDropMode(DropMode.INSERT);
        experimentTree.setTransferHandler(new TreeTransferHandler());
        experimentTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        MouseListener mouseListener = new StructureTreeMouseAdapter(this);
        experimentTree.addMouseListener(mouseListener);
        experimentTree.setEditable(false);
        experimentTree.setCellRenderer(new StructureTreeCellRenderer());
        experimentTree.setShowsRootHandles(true);

        leftScrollPane = new JScrollPane(experimentTree);
        leftScrollPane.setMinimumSize(leftScrollPane.getSize());
	}

	public void disableComponents() {
		experimentTree.setEnabled(false);
		experimentTree.removeMouseListener(experimentTree.getMouseListeners()[0]);
		rightPanel.setEnabled(false);
	}

    /**
     * Drag and drop functionality to change sequence numbers
     */
    private class TreeTransferHandler extends TransferHandler {

		private static final long serialVersionUID = 2155040669108290594L;

		DataFlavor nodesFlavor;
        DataFlavor[] flavors = new DataFlavor[1];
        DefaultMutableTreeNode[] nodesToRemove;

        public TreeTransferHandler() {
            try {
                String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" +
                        javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
                nodesFlavor = new DataFlavor(mimeType);
                flavors[0] = nodesFlavor;
            } catch(ClassNotFoundException e) {
                LogHandler.printException(e, "ClassNotFound");
            }
        }

        /**
         * Define where an item can/can't be dropped
         * @param support
         * @return
         */
        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDrop()) {
                return false;
            }
            support.setShowDropLocation(true);
            if (!support.isDataFlavorSupported(nodesFlavor)) {
                return false;
            }
            JTree tree = (JTree)support.getComponent();
            int[] selRows = tree.getSelectionRows();
            TreePath path = tree.getPathForRow(selRows[0]);
            DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode)path.getLastPathComponent();
            JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
            TreePath dest = dl.getPath();
            DefaultMutableTreeNode target = (DefaultMutableTreeNode)dest.getLastPathComponent();
            // Do not allow to move anything below level 2
            if (firstNode.getLevel() < 2) {
                return false;
            }
            // Nodes must stay on their level
            if (firstNode.getLevel() != target.getLevel() + 1) {
                return false;
            }
            return true;
        }

        protected Transferable createTransferable(JComponent c) {
            JTree tree = (JTree)c;
            TreePath[] paths = tree.getSelectionPaths();
            if (paths != null) {
                // Make up a node array of copies for transfer and
                // another for/of the nodes that will be removed in
                // exportDone after a successful drop.
                java.util.List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
                java.util.List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
                DefaultMutableTreeNode copy = copy(node);
                copies.add(copy);
                toRemove.add(node);
                for (int i = 1; i < paths.length; i++) {
                    DefaultMutableTreeNode next = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    // Do not allow higher level nodes to be added to list.
                    if (next.getLevel() < node.getLevel()) {
                        break;
                    } else if (next.getLevel() > node.getLevel()) {  // child node
                        copy.add(copy(next));
                        // node already contains child
                    } else {                                         // sibling
                        copies.add(copy(next));
                        toRemove.add(next);
                    }
                }
                DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
                nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
                return new NodesTransferable(nodes);
            }
            return null;
        }

        /** Defensive copy used in createTransferable. */
        private DefaultMutableTreeNode copy(TreeNode node) {
            DefaultMutableTreeNode n=(DefaultMutableTreeNode)node;
            return (DefaultMutableTreeNode) n.clone();
        }

        protected void exportDone(JComponent source, Transferable data, int action) {
            TreeNode parent = ((DefaultMutableTreeNode)nodesToRemove[0]).getParent();
            if ((action & MOVE) == MOVE) {
                JTree tree = (JTree)source;
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                // Remove nodes saved in nodesToRemove in createTransferable.
                for (int i = 0; i < nodesToRemove.length; i++) {
                    model.removeNodeFromParent(nodesToRemove[i]);
                }
            }
            changeSequenceNumbers(parent);
        }

        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }
            // Extract transfer data.
            DefaultMutableTreeNode[] nodes = null;
            try {
                Transferable t = support.getTransferable();
                nodes = (DefaultMutableTreeNode[])t.getTransferData(nodesFlavor);
            } catch(UnsupportedFlavorException ufe) {
                LogHandler.printException(ufe, "Unsupported Flavor");
            } catch(java.io.IOException ioe) {
                LogHandler.printException(ioe, "I/O error");
            }
            // Get drop location info.
            JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
            int childIndex = dl.getChildIndex();
            TreePath dest = dl.getPath();
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)dest.getLastPathComponent();
            JTree tree = (JTree)support.getComponent();
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Configure for drop mode.
            int index = childIndex;    // DropMode.INSERT
            // Add data to model.
            for (int i = 0; i < nodes.length; i++) {
                model.insertNodeInto(nodes[i], parent, index++);
            }

            return true;
        }

        public String toString() {
            return getClass().getName();
        }

        public class NodesTransferable implements Transferable {
            DefaultMutableTreeNode[] nodes;

            public NodesTransferable(DefaultMutableTreeNode[] nodes) {
                this.nodes = nodes;
            }

            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException {
                if(!isDataFlavorSupported(flavor))
                    throw new UnsupportedFlavorException(flavor);
                return nodes;
            }

            public DataFlavor[] getTransferDataFlavors() {
                return flavors;
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return nodesFlavor.equals(flavor);
            }
        }
    }

	/**
	 * This method returns the selected session.
	 * 
	 * @return the selected session
	 * @throws DataInputException
	 *             If no session was selected.
	 */
	public Session getSelectedSession() throws DataInputException {

		DefaultMutableTreeNode parentNode;
		TreePath parentPath = experimentTree.getSelectionPath();

		Object userObject;

		try {

			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
			userObject = parentNode.getUserObject();
		} catch (NullPointerException e) {
			throw new DataInputException("Please select a session!");
		}

		if (userObject.getClass().equals(Session.class)) {
			return (Session) userObject;
		} else {
			throw new DataInputException("Please select a session!");
		}
	}

	/**
	 * This method returns the selected treatment block.
	 * 
	 * @return the selected treatment block
	 * @throws DataInputException
	 *             If no treatment block was selected.
	 */
	public TreatmentBlock getSelectedTreatmentBlock() throws DataInputException {

		Object obj = getSelectedNodeObject();

		if (obj.getClass().equals(TreatmentBlock.class)) {
			return (TreatmentBlock) obj;
		} else {
			throw new DataInputException("Please select a treatment block!");
		}
	}

	/**
	 * This method adds a session to the experiment tree.
	 */
	public void addSession() {

		try {
			guiController.createNewSession();
		} catch (StructureManagementException e) {
		    LogHandler.printException(e, "Could not create new session");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Copy a session to the experiment tree.
	 */
	public void copySession(Integer sessionId) {

		try {
			guiController.copySession(sessionId);
		} catch (StructureManagementException e) {
            LogHandler.printException(e, "Could not copy session");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method adds a treatment block to the experiment tree.
	 */
	public void addTreatmentBlock() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		try {
			guiController.createNewTreatmentBlock(session, false);
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		experimentTree.expandPath(new TreePath(parentNode));
	}

	/**
	 * This method adds a practice treatment block to the experiment tree.
	 */
	public void addPracticeTreatmentBlock() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		try {
			guiController.createNewTreatmentBlock(session, true);
		} catch (StructureManagementException e) {
            LogHandler.printException(e, "Could not create new treatment block");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Copy a treatment block to the experiment tree.
	 */
	public void copyTreatmentBlock(Integer treatmentId) {

		try {
			guiController.copyTreatmentBlock(treatmentId);
		} catch (StructureManagementException e) {
            LogHandler.printException(e, "Could not copy treatment block");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method adds periods to the experiment tree.
	 */
	public void addPeriods() {

		String input = JOptionPane.showInputDialog(experimentBuilderPanel, "How many periods should be created?", "Period Creation", JOptionPane.QUESTION_MESSAGE);

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		TreatmentBlock tb = (TreatmentBlock) parentNode.getUserObject();

		try {
			guiController.createNewPeriods(tb, Integer.parseInt(input));
		} catch (StructureManagementException e) {
            LogHandler.printException(e, "Could not create periods");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method adds periods to the experiment tree.
	 */
	public void addPeriod() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		TreatmentBlock tb = (TreatmentBlock) parentNode.getUserObject();

		try {
			guiController.createNewPeriods(tb, 1
			);
		} catch (StructureManagementException e) {
			LogHandler.printException(e, "Could not create periods");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method adds a quiz to the experiment tree.
	 */
	public void addQuiz() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		try {
			guiController.createNewQuiz(session);
		} catch (StructureManagementException e) {
            LogHandler.printException(e, "Could not create new quiz");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method adds a questionnaire to the experiment tree.
	 */
	public void addQuestionnaire() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		try {
			guiController.createNewQuestionnaire(session);
		} catch (StructureManagementException e) {
			LogHandler.printException(e, "Could not create new questionnaire");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method adds a pause to the experiment tree.
	 */
	public void addPause() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		try {
			guiController.createNewPause(session);
		} catch (StructureManagementException e) {
            LogHandler.printException(e, "Could not create new pause");
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

    public void changeSequenceNumbers(TreeNode parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
            //LogHandler.printInfo("Changing sequence number for element " + ((DefaultMutableTreeNode) parent.getChildAt(i)).getUserObject() + " -> " + (i+1));
            try {
                guiController.changeSequenceNumber(node.getUserObject(), i+1);
            } catch (StructureManagementException e) {
                LogHandler.printException(e, "Could not change sequence numbers");
                JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

	/**
	 * This method removes the selected item from the tree.
	 */
	public void removeItem() {

		TreePath currentSelection = experimentTree.getSelectionPath();

		if (currentSelection != null) {

			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            Object userObject = currentNode.getUserObject();

            // Remove tree node and update sequence numbers accordingly
            if (userObject.getClass().equals(TreatmentBlock.class) ||
                userObject.getClass().equals(Quiz.class) ||
                userObject.getClass().equals(Pause.class) ||
                userObject.getClass().equals(Period.class)) {
                TreeNode parent = currentNode.getParent();
                treeModel.removeNodeFromParent(currentNode);
                changeSequenceNumbers(parent);
            }

			if (userObject.getClass().equals(Session.class)) {
				try {
					guiController.removeSession((Session) userObject);
				} catch (StructureManagementException e) {
                    LogHandler.printException(e, "Could not remove session");
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			if (userObject.getClass().equals(Period.class)) {
				try {
					guiController.removePeriod((Period) userObject);
				} catch (StructureManagementException e) {
                    LogHandler.printException(e, "Could not remove period");
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			if (userObject.getClass().equals(TreatmentBlock.class) || userObject.getClass().equals(Quiz.class) || userObject.getClass().equals(Pause.class)) {
				try {
					guiController.removeSequenceElement((SequenceElement) userObject);
				} catch (StructureManagementException e) {
                    LogHandler.printException(e, "Could not remove sequence element");
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * This method refreshes the details panel.</br> Depending on the user
	 * object it can show the details for an experiment, a treatment block, a
	 * session, a pause, a quiz or a period.
	 * 
	 * @param userObject
	 *            The Object that determines what will be shown in the details
	 *            panel.
	 */
	public void refreshDetails(Object userObject) {

		if (userObject.getClass().equals(Experiment.class)) {

			Experiment exp = (Experiment) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new ExperimentDetails(exp));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(TreatmentBlock.class)) {

			TreatmentBlock block = (TreatmentBlock) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new TreatmentBlockDetails(block));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Session.class)) {

			Session block = (Session) userObject;
			try {
				block = sessionInstance.findSession(block.getIdSession());
			} catch (StructureManagementException e) {
				e.printStackTrace();
			}
			detailsPanel.removeAll();
			detailsPanel.add(new SessionDetails(block));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Pause.class)) {

			Pause pause = (Pause) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new PauseDetails(pause));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Quiz.class)) {

			Quiz quiz = (Quiz) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new QuizDetails(quiz));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Questionnaire.class)) {

			Questionnaire questionnaire = (Questionnaire) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new QuestionnaireDetails(questionnaire));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Period.class)) {

			Period quiz = (Period) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new PeriodDetails(quiz));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}
	}

	/**
	 * This method gets the experiment tree.
	 * 
	 * @return the experiment tree
	 */
	public JTree getTree() {
		return experimentTree;
	}

	/**
	 * This method sets the experiment tree.
	 * 
	 * @param tree
	 *            A JTree variable which contains the experiment tree.
	 */
	public void setTree(JTree tree) {
		this.experimentTree = tree;
	}

	/**
	 * This method saves the status of a tree for reconstruction.
	 */
	private void saveStatus() {

		expandedNodes = new ArrayList<>();
		getTreeStatus(treeModel.getRoot());

	}

	/**
	 * This method retrieves the status of an experiment tree for
	 * reconstruction.
	 * 
	 * @param anyNode
	 *            An Object variable that contains an experiment tree.
	 * @return the tree status
	 */
	private void getTreeStatus(final Object anyNode) {

		int cc = treeModel.getChildCount(anyNode);

		for (int i = 0; i < cc; i++) {

			DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeModel.getChild(anyNode, i);
			TreeNode[] path = child.getPath();
			if (experimentTree.isExpanded(new TreePath(path))) {
				expandedNodes.add(child.getUserObject());
				getTreeStatus(child);
			}
		}
	}

	/**
	 * This method reselects a path of an experiment tree.
	 * 
	 * @param anyNode
	 *            a {@link javax.swing.tree.DefaultMutableTreeNode
	 *            DefaultMutableTreeNode} of an experiment tree.
	 */
	private void reselectPath(DefaultMutableTreeNode anyNode) {

		int cc = treeModel.getChildCount(anyNode);

		for (int i = 0; i < cc; i++) {

			DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeModel.getChild(anyNode, i);
			TreeNode[] path = child.getPath();

			if (child.getUserObject().equals(selectedNodeObject)) {
				experimentTree.setSelectionPath(new TreePath(path));
				experimentTree.scrollPathToVisible(new TreePath(path));
				return;
			} else {
				getTreeStatus(child);
			}
		}

	}

	/**
	 * This method gets the selected node object.
	 * 
	 * @return the selected node object
	 */
	public Object getSelectedNodeObject() {
		return selectedNodeObject;
	}

	/**
	 * This method sets the selected node object.
	 * 
	 * @param selectedNodeObject
	 *            The new selected node object.
	 */
	public void setSelectedNodeObject(Object selectedNodeObject) {
		this.selectedNodeObject = selectedNodeObject;
	}

	public JPanel getExperimentBuilder() {
		return experimentBuilderPanel;
	}

}
