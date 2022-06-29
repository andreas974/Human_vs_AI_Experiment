package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.structure.StructureManagementException;
import net.miginfocom.swing.MigLayout;

/**
 * This class generates a container for adding periods to TreatmentBlocks.</br>
 * I opens a prompt that asks the experimenter to enter the number of periods
 * that should be created.</br>
 * 
 * @see StructureTab
 * @see StructureTreeBuilder
 * @see TreatmentBlockDetails
 */
public class PeriodDetails extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6433621850611921076L;

	/** The gui controller. */
	private StructureTabController guiController = StructureTabController.getInstance();

	/** The updated period. */
	private Period updatedPeriod;

	private JCheckBox chckbxMarkAsPractice;

	/**
	 * This constructor instantiates a new JPanel that shows the details of a
	 * period. All periods are persisted on the server side.
	 * 
	 * @param period
	 *            A Period variable that contains informations about a pause.
	 */
	public PeriodDetails(Period period) {

		this.updatedPeriod = period;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][][grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(period.getIdPeriod().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblSequenceNumber = new JLabel("Sequence Number:");
		inputPanel.add(lblSequenceNumber, "cell 0 1,alignx trailing");

        JLabel labelSequenceNumberInfo = new JLabel(period.getSequenceNumber().toString());
        inputPanel.add(labelSequenceNumberInfo, "cell 1 1");

		JLabel lblPractice = new JLabel("Practice:");
		inputPanel.add(lblPractice, "cell 0 2,alignx left");

		chckbxMarkAsPractice = new JCheckBox("Mark as practice period.");
		chckbxMarkAsPractice.setSelected(period.getPractice());
		inputPanel.add(chckbxMarkAsPractice, "cell 1 2");

		JLabel labelHeading = new JLabel("Details of Period");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				updatedPeriod.setPractice(chckbxMarkAsPractice.isSelected());

				try {
					guiController.updatePeriod(updatedPeriod);
				} catch (StructureManagementException e1) {
					JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.ERROR_MESSAGE);
                    LogHandler.printException(e1, "Could not update period");
				}
			}
		});
		buttonPanel.add(buttonApplyChanges);

	}
}
