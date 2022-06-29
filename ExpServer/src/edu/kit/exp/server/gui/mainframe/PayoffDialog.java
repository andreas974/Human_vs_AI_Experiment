package edu.kit.exp.server.gui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This class creates the payoff dialog frame.</br> It is only used by the
 * mainframe controller and is invoked if the mainframe menu item
 * "Calculated Payoffs" in "Run Session" is selected.</br> A new Frame opens and
 * a factor can be entered to calculate a payoff for a subject.
 * 
 */
public class PayoffDialog extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8482341470946680664L;

	/** The gui controller. */
	private MainFrameController guiController = MainFrameController.getInstance();

	/** The text field factor. */
	private JTextField textFieldFactor;

	/** The text panel. */
	private JPanel textPanel;

	/** The text area. */
	private JTextArea textArea;

	/**
	 * This constructor instantiates a new payoff dialog.</br> The user can
	 * enter a factor to calculate subject payoffs via
	 * {@link PayoffDialog#calc()}
	 */
	public PayoffDialog() {

		getContentPane().setBackground(Color.WHITE);
		this.setBounds(200, 200, 500, 400);
		this.setLocationRelativeTo(MainFrame.getInstance());
		this.setModal(true);
		this.setResizable(true);
		this.setTitle("Subject Payoffs");

		textPanel = new JPanel();
		textPanel.setBackground(Color.WHITE);
		getContentPane().add(textPanel, BorderLayout.CENTER);
		textPanel.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();

		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel.add(scrollPane);

		JPanel northPanel = new JPanel();
		getContentPane().add(northPanel, BorderLayout.NORTH);

		JLabel lblInfo = new JLabel("Enter a factor to calculate subject payoffs.\r\n");
		northPanel.add(lblInfo);

		textFieldFactor = new JTextField("1");
		northPanel.add(textFieldFactor);
		textFieldFactor.setColumns(10);

		JButton btnOK = new JButton("Calculate");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				calc();

			}

		});
		northPanel.add(btnOK);
	}

	/**
	 * This method calculates the payoff for each subject using the factor
	 * submitted by the user.
	 */
	public void calc() {
		List<Subject> list = null;
		try {
			list = guiController.getSubjects();
		} catch (DataInputException | StructureManagementException e) {
			LogHandler.popupException(e);
		}

		textArea.setText("");
		for (Subject subject : list) {
			String line = "";
			Double fac = Double.valueOf(textFieldFactor.getText());
			Double payOff = subject.getPayoff();
			Double product = payOff * fac;
			line += subject.getIdClient() + ", (" + payOff + ") : " + product.toString() + " Euro" + System.lineSeparator();
			textArea.append(line);

		}

	}
}
