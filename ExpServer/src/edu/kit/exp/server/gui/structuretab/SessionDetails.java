package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.gui.mainframe.MainFrameController;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.run.ExistingDataException;
import edu.kit.exp.server.run.SessionDoneException;
import edu.kit.exp.server.run.SessionRunException;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

/**
 * This class generates a container for editing sessions.</br>
 * It is a child panel of the structure tab and allows users to edit the details
 * of the sessions.</br>
 * Sessions can be added to the structure tree for experiments.
 * 
 * @see StructureTab
 * @see StructureTreeBuilder
 */
public class SessionDetails extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6433621850611921076L;

	/** The gui controller. */
	private StructureTabController guiController = StructureTabController.getInstance();

	/** The text field name. */
	private JTextField textFieldName;

	/** The text field planned date. */
	private JTextField textFieldPlannedDate;

	/** The updated session. */
	private Session updatedSession;

	/** The editor pane. */
	private JEditorPane editorPane;

	/** The text cohorts. */
	private JTextField textCohorts;

	/** The text subjects per cohort. */
	private JTextField textSubjectsPerCohort;
	private JFormattedTextField textFieldPlannedTime;
	private JPanel inputPanel;

	/**
	 * This constructor instantiates a new JPanel that shows the details of a
	 * sessions. All sessions are persisted on the server side.
	 * 
	 * @param session
	 *            A Session variable that contains informations about a session.
	 */
	public SessionDetails(Session session) {

		updatedSession = session;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow][]", "[][][][][23.00][23.00][100,grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(session.getIdSession().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblSessionName = new JLabel("Session Name:");
		inputPanel.add(lblSessionName, "cell 0 1,alignx left");

		textFieldName = new JTextField(session.getName());
		inputPanel.add(textFieldName, "cell 1 1,growx");
		textFieldName.setColumns(10);

		JLabel lblPlannedDate = new JLabel("Planned Date:");
		inputPanel.add(lblPlannedDate, "cell 0 2,alignx left");

		JLabel lblPlannedTime = new JLabel("Planned Time:");
		inputPanel.add(lblPlannedTime, "cell 0 3,alignx left");

		DateTime d = new DateTime(session.getPlannedDate());
		textFieldPlannedDate = new JTextField(d.toString("dd-MM-YYYY"));
		textFieldPlannedDate.setColumns(10);
		inputPanel.add(textFieldPlannedDate, "cell 1 2,alignx left");

		textFieldPlannedTime = new JFormattedTextField(session.getPlannedDate().toString().substring(12, 19));
		textFieldPlannedTime.setColumns(10);
		inputPanel.add(textFieldPlannedTime, "cell 1 3,alignx left");
		// adding code from here - R22

		JLabel lblCohorts = new JLabel("Cohorts:");
		inputPanel.add(lblCohorts, "cell 0 4,alignx left");

		String numberOfCohorts = String.valueOf(session.getCohorts().size());
		String numberOfSubjects;
		if (session.getCohorts().size() > 0) {
			numberOfSubjects = String.valueOf(session.getCohorts().get(0).getSize());
		} else {
			numberOfSubjects = "";
		}

		textCohorts = new JTextField(numberOfCohorts);
		inputPanel.add(textCohorts, "cell 1 4,alignx left");
		textCohorts.setColumns(10);

		JLabel lblSubjectsPerCohort = new JLabel("Subjects per Cohort:");
		inputPanel.add(lblSubjectsPerCohort, "cell 0 5,alignx left");

		textSubjectsPerCohort = new JTextField(numberOfSubjects);

		inputPanel.add(textSubjectsPerCohort, "cell 1 5,alignx left");
		textSubjectsPerCohort.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		inputPanel.add(lblDescription, "cell 0 6,alignx left");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(session.getDescription());
		inputPanel.add(editorPane, "cell 1 6,grow");

		JPanel headPanel = new JPanel();
		headPanel.setLayout(new BorderLayout(0, 0));
		panel.add(headPanel, BorderLayout.NORTH);

		JButton buttonRunSession = new JButton("Run Session Now");
		buttonRunSession.setPreferredSize(new Dimension(100, 50));
		buttonRunSession.setIcon(new ImageIcon(MainFrame.class.getResource("/edu/kit/exp/server/resources/run_session_2.jpg")));
		buttonRunSession.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				runSessionNowAction();

			}
		});
		headPanel.add(buttonRunSession, BorderLayout.NORTH);

		JLabel labelHeading = new JLabel("<html><br>Details of Session<html>");
		headPanel.add(labelHeading, BorderLayout.SOUTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				updatedSession.setDescription(editorPane.getText());
				updatedSession.setName(textFieldName.getText());

                Integer numberOfCohorts = 0;
                Integer cohortSize = 0;

                try {
                    numberOfCohorts = Integer.valueOf(textCohorts.getText());
                    cohortSize = Integer.valueOf(textSubjectsPerCohort.getText());

                    if (cohortSize < 1 || numberOfCohorts < 1) {
                        throw new DataInputException("Please check your cohort settings!");
                    }

                } catch (NumberFormatException | DataInputException e) {
                    LogHandler.popupException(e, "Incorrect number input!");
                }

				try {
					updatedSession.setPlannedDate(guiController.parseDateString(textFieldPlannedDate.getText() + " " + textFieldPlannedTime.getText()));
                    guiController.updateSession(updatedSession, numberOfCohorts, cohortSize);
				} catch (StructureManagementException | DataInputException e1) {
					LogHandler.popupException(e1, "Could not update session details");
				}
			}
		});
		buttonPanel.add(buttonApplyChanges);

	}

	private void runSessionNowAction() {
		MainFrameController mainFrameController = MainFrameController.getInstance();
		
		try {
			mainFrameController.runSession();
			mainFrameController.switchToTab(2);
		} catch (DataInputException | SessionRunException | ConnectionException ex) {
			LogHandler.popupException(ex);
		} catch (ExistingDataException e1) {
			int answer = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "<html><body>Session was started before but not finished! Do you want to restart the session?<br>Yes = Restart (Data will be lost),<br> No = Continue,<br>Cancel = No action</html></body>");

			try {
				if (answer == JOptionPane.NO_OPTION) {
					mainFrameController.continueSession();
					mainFrameController.switchToTab(2);
				} else if (answer == JOptionPane.YES_OPTION) {
					mainFrameController.resetSession();
					mainFrameController.switchToTab(2);
				}
			} catch (DataInputException | SessionRunException | ConnectionException | StructureManagementException ex) {
				LogHandler.popupException(ex);
			} catch (ExistingDataException e2) {
				LogHandler.printException(e2); // Illegal State
			}

		} catch (StructureManagementException e1) {
			LogHandler.popupException(e1);
		} catch (SessionDoneException e1) {
			int answer = JOptionPane.showConfirmDialog(MainFrame.getInstance(), e1.getMessage());

			if (answer == JOptionPane.YES_OPTION) {

				try {
					mainFrameController.resetSession();
					mainFrameController.switchToTab(2);
				} catch (DataInputException | SessionRunException | ConnectionException | StructureManagementException ex) {
					LogHandler.popupException(ex);
				} catch (ExistingDataException e2) {
					LogHandler.printException(e2); // Illegal State
				}
			}
		}
	}
}
