package edu.kit.exp.server.gui.treatment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.structuretab.StructureTab;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This class provides a dialog frame for the management of treatments.</br> It
 * can be opened by clicking on the "Add/Remove" button in the treatment block
 * details frame.</br> The frame provides buttons for creating new treatments,
 * updating and deleting treatments and adding/removing them to treatment
 * blocks.
 */
class TreatmentManagementDialog extends JDialog implements Observer {

    private static final long serialVersionUID = 8722129063752300961L;
    private static final String UPDATE_TREATMENT_BUTTON_LABEL = "Update Treatment";
    private static final String DELETE_TREATMENT_BUTTON_LABEL = "Delete Treatment";
    private static final String RIGHT_BUTTON_LABEL = ">>";
    private static final String NEW_TREATMENT_BUTTON_LABEL = "New Treatment";
    private static final String CLOSE_BUTTON_LABEL = "Close";
    private static final String LEFT_BUTTON_LABEL = "<<";
    private static final String OUTSIDE_LABEL = "Outside the Treatment Block";
    private static final String INSIDE_LABEL = "Inside the Treatment Block";
    private static final String TREATMENT_MANAGEMENT_DIALOG_LABEL = "Treatment Management Dialog";

    private TreatmentManagementDialogController guiController = TreatmentManagementDialogController.getInstance();
    private List<Treatment> treatmentList;
    private JButton updateButton;
    private JPanel treatmentListPanel;
    private JList<Treatment> jListSelected;
    private JList<Treatment> jListUnselected;

    private static TreatmentManagementDialog instance;

    /**
     * Gets the single instance of TreatmentManagementDialog.
     *
     * @return a single instance of TreatmentManagementDialog
     */
    public static TreatmentManagementDialog getInstance() throws StructureManagementException, DataInputException {

        if (instance == null) {
            instance = new TreatmentManagementDialog();
        }

        return instance;
    }

    private TreatmentManagementDialog() throws StructureManagementException, DataInputException {

        guiController.addObserver(this);
        setTitle(TREATMENT_MANAGEMENT_DIALOG_LABEL);
        this.setBounds(200, 200, 800, 450);
        this.setLocationRelativeTo(this.getParent());
        this.setModal(true);

        JPanel contentPanel = new JPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(initializeLabelPanel(), BorderLayout.NORTH);

        try {
            treatmentList = guiController.getAllTreatments();
        } catch (StructureManagementException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LogHandler.printException(e, "Could not get all treatments");
        }

        treatmentListPanel = new JPanel();
        treatmentListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        treatmentListPanel.setLayout(new BorderLayout(10, 10));
        initializeTreatmentPanel();
        contentPanel.add(treatmentListPanel, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        contentPanel.add(panelButtons, BorderLayout.SOUTH);
        panelButtons.add(initializeNewButton());
        panelButtons.add(initializeUpdateButton());
        panelButtons.add(initializeDeleteButton());
        panelButtons.add(initializeCloseButton());
    }

    private JButton initializeNewButton() {
        JButton newButton = new JButton(NEW_TREATMENT_BUTTON_LABEL);
        newButton.addActionListener(arg0 -> guiController.showTreatmentCreationDialog());
        return newButton;
    }

    private JPanel initializeLabelPanel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        labelPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        labelPanel.add(new JLabel(OUTSIDE_LABEL), BorderLayout.WEST);
        labelPanel.add(new JLabel(INSIDE_LABEL), BorderLayout.EAST);
        return labelPanel;
    }

    private JButton initializeCloseButton() {
        JButton closeButton = new JButton(CLOSE_BUTTON_LABEL);
        closeButton.addActionListener(event -> close());
        return closeButton;
    }

    private JButton initializeDeleteButton() {
        JButton deleteButton = new JButton(DELETE_TREATMENT_BUTTON_LABEL);

        deleteButton.addActionListener(e -> {

            List<Treatment> treatmentInUnselectedList = jListUnselected.getSelectedValuesList();
            List<Treatment> treatmentInSelectedList = jListSelected.getSelectedValuesList();
            try {
                for (Treatment treatment : treatmentInSelectedList) {
                    guiController.removeTreatmentFromTreatmentBlock(treatment);
                    guiController.deleteTreatment(treatment);
                }
                for (Treatment treatment : treatmentInUnselectedList) {
                    guiController.deleteTreatment(treatment);
                }
            } catch (StructureManagementException | DataInputException ex) {
                JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                LogHandler.printException(ex);
            }

        });

        return deleteButton;
    }

    private JButton initializeUpdateButton() {
        updateButton = new JButton(UPDATE_TREATMENT_BUTTON_LABEL);
        updateButton.addActionListener(arg0 -> {

            List<Treatment> treatmentInUnselectedList = jListUnselected.getSelectedValuesList();
            List<Treatment> treatmentInSelectedList = jListSelected.getSelectedValuesList();
            try {
                if (treatmentInSelectedList.size() > 0) {
                    guiController.showTreatmentUpdateDialog(treatmentInSelectedList.get(0));
                } else if (treatmentInUnselectedList.size() > 0) {
                    guiController.showTreatmentUpdateDialog(treatmentInUnselectedList.get(0));
                }
            } catch (DataInputException | StructureManagementException e) {
                JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                LogHandler.printException(e);
            }

        });

        return updateButton;
    }

    private void initializeTreatmentPanel() throws StructureManagementException, DataInputException {

        treatmentListPanel.removeAll();

        List<Treatment> listSelected = new ArrayList<>();
        List<Treatment> listUnselected = new ArrayList<>();
        for (Treatment treatment : treatmentList) {
            if (guiController.isTreatmentOfTreatmentBlock(treatment)) {
                listSelected.add(treatment);
            } else {
                listUnselected.add(treatment);
            }
        }

        JScrollPane listSelectedScroller = createSelectedListScroller(listSelected);
        treatmentListPanel.add(listSelectedScroller, BorderLayout.EAST);

        JScrollPane listUnselectedScroller = createUnselectedListScroller(listUnselected);
        treatmentListPanel.add(listUnselectedScroller, BorderLayout.WEST);

        JPanel directionButtons = new JPanel();
        directionButtons.setLayout(new BorderLayout());
        treatmentListPanel.add(directionButtons, BorderLayout.CENTER);

        directionButtons.add(createRightButton(), BorderLayout.NORTH);
        directionButtons.add(createLeftButton(), BorderLayout.SOUTH);
        treatmentListPanel.revalidate();
    }

    private JScrollPane createSelectedListScroller(List<Treatment> treatments) {
        jListSelected = new JList<>(treatments.toArray(new Treatment[0]));
        jListSelected.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jListSelected.setLayoutOrientation(JList.VERTICAL);
        jListSelected.setVisibleRowCount(-1);
        JScrollPane listSelectedScroller = new JScrollPane(jListSelected);
        listSelectedScroller.setPreferredSize(new Dimension(250, 120));
        jListSelected.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                List<Treatment> selectedTreatments = jListSelected.getSelectedValuesList();
                try {
                    for (Treatment treatment : selectedTreatments) {
                        guiController.removeTreatmentFromTreatmentBlock(treatment);
                    }
                } catch (DataInputException | StructureManagementException e) {
                    LogHandler.printException(e);
                }
            }
                }
        });
        // Guarantees that the selection of "unselected"-List is cleared when the "selected"-List is selected
        jListSelected.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                jListUnselected.clearSelection();
                updateButton.setEnabled(false);
            }
        });
        return listSelectedScroller;
    }

    private JScrollPane createUnselectedListScroller(List<Treatment> treatments) {
        jListUnselected = new JList<>(treatments.toArray(new Treatment[0]));
        jListUnselected.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jListUnselected.setLayoutOrientation(JList.VERTICAL);
        jListUnselected.setVisibleRowCount(-1);
        JScrollPane listSelectedScroller = new JScrollPane(jListUnselected);
        listSelectedScroller.setPreferredSize(new Dimension(250, 120));
        jListUnselected.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                List<Treatment> unselectedTreatments = jListUnselected.getSelectedValuesList();
                try {
                    for (Treatment treatment : unselectedTreatments) {
                        guiController.addTreatmentToTreatmentBlock(treatment);
                    }
                } catch (DataInputException | StructureManagementException e) {
                    LogHandler.printException(e);
                }
            }
                }
        });
        // Guarantees that the selection of "selected"-List is cleared when the "unselected"-List is selected
        jListUnselected.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                jListSelected.clearSelection();
                updateButton.setEnabled(true);
            }
        });
        return listSelectedScroller;
    }

    private JButton createRightButton() {
        JButton right = new JButton(RIGHT_BUTTON_LABEL);

        right.addActionListener(arg0 -> {

            List<Treatment> unselectedTreatments = jListUnselected.getSelectedValuesList();
            try {
                for (Treatment treatment : unselectedTreatments) {
                    guiController.addTreatmentToTreatmentBlock(treatment);
                }
            } catch (DataInputException | StructureManagementException e) {
                LogHandler.printException(e);
            }

        });
        return right;
    }

    private JButton createLeftButton() {
        JButton left = new JButton(LEFT_BUTTON_LABEL);
        left.addActionListener(arg0 -> {

            List<Treatment> selectedTreatments = jListSelected.getSelectedValuesList();
            try {
                for (Treatment treatment : selectedTreatments) {
                    guiController.removeTreatmentFromTreatmentBlock(treatment);
                }
            } catch (DataInputException | StructureManagementException e) {
                LogHandler.printException(e);
            }

        });
        return left;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object arg) {

        treatmentList = (List<Treatment>) arg;
        try {
            initializeTreatmentPanel();
        } catch (StructureManagementException | DataInputException e) {
            LogHandler.printException(e);
        }
    }

    private void close() {
        this.dispose();
    }
}
