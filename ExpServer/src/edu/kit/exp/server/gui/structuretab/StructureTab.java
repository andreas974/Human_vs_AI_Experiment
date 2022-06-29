package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.jpa.entity.Experiment;

/**
 * This class generates aJPanel tab for editing the structure of an
 * experiment.</br> It is integrated into the mainframe and implements the
 * Observer interface.
 * 
 * @see Observer
 * @see MainFrame
 */
public class StructureTab extends JPanel implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4150123067575255277L;

	/** The instance. */
	private static StructureTab instance;

	/* Panels */
	/** The info panel. */
	private JPanel infoPanel;

	/** The tree panel. */
	private JPanel treePanel;

	/** The button panel. */
	private JPanel buttonPanel;

	/* Label */
	/** The j label information. */
	private JLabel jLabelInformation;

	/** The gui controller. */
	private StructureTabController guiController = StructureTabController.getInstance();

	/** The experiment builder. */
	private StructureTreeBuilder experimentBuilder = StructureTreeBuilder.getInstance();

	/**
	 * This constructor instantiates a new structure tab.
	 */
	private StructureTab() {
		super();

		guiController.addObserver(this);
		setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setPreferredSize(new Dimension(1091, 743));
		init();
	}

	/**
	 * Gets the single instance of StructureTab.
	 * 
	 * @return a single instance of StructureTab
	 */
	public static StructureTab getInstance() {

		if (instance == null) {
			instance = new StructureTab();
		}

		return instance;
	}

	private void init() {

		// Tree Panel
		this.setLayout(new BorderLayout(0, 0));
		treePanel = new JPanel();
		treePanel.setBackground(Color.WHITE);
		treePanel.setLayout(new GridLayout(1, 0, 0, 0));
		add(treePanel);

		// Button Panel
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		add(buttonPanel, BorderLayout.SOUTH);

		// Info Panel
		infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
		infoPanel.setLayout(new GridLayout(0, 1, 0, 0));
		add(infoPanel, BorderLayout.NORTH);

		jLabelInformation = new JLabel("Please choose an experiment from the list below.");
		infoPanel.add(jLabelInformation);

		Experiment currentExperiment = guiController.getCurrentExperiment();

		if (currentExperiment != null) {
			initExperimentBuilder(guiController.getCurrentExperiment());
		}

	}

	/**
	 * This method initializes the experiment builder.</br> After calling it,
	 * the structure tree of the experiment will be shown in the structure tab.
	 * 
	 * @param experiment
	 *            An experiment that will be shown in the structure tab.
	 */
	public void initExperimentBuilder(Experiment experiment) {

		treePanel.removeAll();

		treePanel.add(experimentBuilder.createExperimentBuilder(experiment));

		treePanel.revalidate();
		revalidate();

	}

	@Override
	public void update(Observable o, Object arg) {

		initExperimentBuilder((Experiment) arg);

	}

}
