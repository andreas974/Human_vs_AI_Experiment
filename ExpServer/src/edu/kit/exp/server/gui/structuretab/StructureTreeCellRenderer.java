package edu.kit.exp.server.gui.structuretab;

import edu.kit.exp.server.jpa.entity.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.net.URL;

/**
 * This class is a renderer for structure tree elements.
 */
class StructureTreeCellRenderer extends DefaultTreeCellRenderer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6942963121233876244L;

	/** The treatment block icon url. */
	private static URL treatmentBlockIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/treatmentBlock.gif");

	/** The quiz icon url. */
	private static URL quizIconUrl = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/quiz.gif");

	/** The questionnaire icon url. */
	private static URL questionnaireIconUrl = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/quiz.gif");

	/** The pause icon url. */
	private static URL pauseIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/pause.gif");

	/** The session icon url. */
	private static URL sessionIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/session.gif");

	/** The practice tb icon url. */
	private static URL practiceTBIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/practiceTreatmentBlock.gif");

	/** The practice period icon url. */
	private static URL practicePeriodIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/practicePeriod.gif");

	/** The period icon url. */
	private static URL periodIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/exp/server/resources/period.gif");

	/**
	 * This constructor instantiates a new structure tree cell renderer.
	 */
	public StructureTreeCellRenderer() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
	 * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
	 * boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value.getClass().equals(DefaultMutableTreeNode.class)) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object obj = node.getUserObject();

			if (obj.getClass().equals(TreatmentBlock.class)) {

				TreatmentBlock tb = (TreatmentBlock) obj;

				if (tb.getPractice()) {
					setIcon(new ImageIcon(practiceTBIconURL));
				} else {
					setIcon(new ImageIcon(treatmentBlockIconURL));
				}
			}
			if (obj.getClass().equals(Quiz.class)) {
				setIcon(new ImageIcon(quizIconUrl));
			}
			if (obj.getClass().equals(Questionnaire.class)) {
				setIcon(new ImageIcon(questionnaireIconUrl));
			}
			if (obj.getClass().equals(Pause.class)) {
				setIcon(new ImageIcon(pauseIconURL));
			}
			if (obj.getClass().equals(Period.class)) {

				Period p = (Period) obj;

				if (p.getPractice()) {
					setIcon(new ImageIcon(practicePeriodIconURL));
				} else {
					setIcon(new ImageIcon(periodIconURL));
				}

			}
			if (obj.getClass().equals(Session.class)) {
				setIcon(new ImageIcon(sessionIconURL));
			}
		}

		return this;

	}
}