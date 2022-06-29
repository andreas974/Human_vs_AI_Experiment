package edu.kit.exp.server.gui.mainframe;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import edu.kit.exp.common.Constants;
import edu.kit.exp.server.gui.infotab.InfoTab;
import edu.kit.exp.server.gui.runtab.RunTab;
import edu.kit.exp.server.gui.starttab.StartTab;
import edu.kit.exp.server.gui.structuretab.StructureTab;

/**
 * This class provides the mainframe for the server application window.
 * 
 * @see JFrame
 */
public class MainFrame extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1904182956665009961L;

	/** The mainframe instance. */
	private static MainFrame instance;

	/** The tabbed pane. */
	private JTabbedPane tabbedPane;

	/** The tab experiment structure. */
	private StructureTab tabExperimentStructure = StructureTab.getInstance();

	/** The start tab. */
	private StartTab tabStart = StartTab.getInstance();

	/** The run tab. */
	private RunTab tabRun = RunTab.getInstance();

	/**
	 * This method gets a single instance of MainFrame. If no instance exists, a
	 * new one is created by invoking {@link MainFrame#MainFrame()}.
	 * 
	 * @return a single instance of MainFrame
	 */
	public static MainFrame getInstance() {

		if (instance == null) {
			instance = new MainFrame();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new main frame for the server. Size:
	 * (1150, 850)
	 */
	private MainFrame() {

		super();
		this.setSize(new Dimension(1150, 850));
		this.setTitle("brownie Server");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon(InfoTab.class.getResource("/edu/kit/exp/common/resources/brownie_logo_noname.png")).getImage());
		if (!Constants.isSystemDebugMode()) {
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(e.getWindow(), "Really exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    e.getWindow().dispose();
                    System.exit(0);
                }
				}
			});
		}

		init();
	}

	/**
	 * Initializes the mainframe.
	 */
	private void init() {

		/* Tabbed pane */
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.addTab("Start", tabStart);
		tabbedPane.addTab("Experiment Structure", tabExperimentStructure);
		tabbedPane.addTab("Run Session", tabRun);
		tabbedPane.addTab("Info", new InfoTab());
		
		getContentPane().add(tabbedPane);
	}

	/**
	 * This method gets the tabbed pane.
	 * 
	 * @return the tabbed pane
	 * @see JTabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * This method sets the tabbed pane.
	 * 
	 * @param tabbedPane
	 *            A JTabbedPane variable which contains a new tabbed pane.
	 * @see JTabbedPane
	 */
	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
}
