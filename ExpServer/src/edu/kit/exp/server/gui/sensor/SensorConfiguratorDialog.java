package edu.kit.exp.server.gui.sensor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.common.sensor.SensorConfigurationElement;

public class SensorConfiguratorDialog extends JDialog {

	private static final long serialVersionUID = 9130149493102953152L;
	private JPanel panelConfElements;
	private ISensorRecorderConfiguration currentConfiguration;

	/** The instance. */
	private static SensorConfiguratorDialog instance;

	/**
	 * Gets the single instance of SensorConfiguratorDialog.
	 * 
	 * @return a single instance of SensorConfiguratorDialog
	 */
	public static SensorConfiguratorDialog getInstance() {

		if (instance == null) {
			instance = new SensorConfiguratorDialog();
		}

		return instance;
	}

	private SensorConfiguratorDialog() {
		this.setTitle("Sensor Configuration Dialog");
		this.setBounds(200, 200, 500, 600);
		this.setLocationRelativeTo(this.getParent());
		this.setModal(true);

		// Default Buttons
		JPanel panelButtons = new JPanel();
		this.add(panelButtons, BorderLayout.SOUTH);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getElementValuesFromInputFields();
				SensorConfiguratorDialog.getInstance().close();
			}
		});
		panelButtons.add(btnSave);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SensorConfiguratorDialog.getInstance().close();
			}
		});
		panelButtons.add(btnCancel);

		JButton btnLoadDefaultValues = new JButton("Load Default Values");
		btnLoadDefaultValues.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentConfiguration.setDefaultValues();
				generateElements();
			}
		});
		panelButtons.add(btnLoadDefaultValues);

		// Configuration elements areas
		panelConfElements = new JPanel();
		this.add(panelConfElements, BorderLayout.NORTH);
	}

	private void generateElements() {
		panelConfElements.removeAll();
		panelConfElements.setLayout(new BoxLayout(panelConfElements, BoxLayout.PAGE_AXIS));
		panelConfElements.setBackground(UIManager.getColor("Button.background"));

		Field[] fields = currentConfiguration.getClass().getFields();
		for (Field field : fields) {
			SensorConfigurationElement annotation = field.getAnnotation(SensorConfigurationElement.class);
			if (annotation != null) {
				JLabel elementName = new JLabel("<HTML><i>" + annotation.name() + "</i></HTML>");
				panelConfElements.add(elementName);
				JLabel elementDescription = new JLabel("<HTML>" + annotation.description() + "</HTML>");
				panelConfElements.add(elementDescription);

				JComponent elementValue;

				// Field is an enum type: Use combobox
				if (field.getType() != null && (field.getType()).isEnum()) {
					Class<?> c = field.getType();
					elementValue = new JComboBox<Object>(c.getEnumConstants());
					try {
						elementValue.setName(field.getName());
						((JComboBox<?>) elementValue).setSelectedItem(field.get(currentConfiguration));
					} catch (IllegalAccessException e) {
						LogHandler.printException(e, "Failed to populate combobox for enum");
					}
				}

				// Field is an boolean type: Use Checkbox
				else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
					elementValue = new JCheckBox(" (click to selected / unselect)");
					try {
						elementValue.setName(field.getName());
						((JCheckBox) elementValue).setSelected((boolean) field.get(currentConfiguration));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						LogHandler.printException(e, "Failed to populate checkbox for boolean");
					}
				}

				// Else use textbox
				else {
					elementValue = new JTextField("");
					try {
						elementValue.setName(field.getName());
						((JTextField) elementValue).setText(field.get(currentConfiguration).toString());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						LogHandler.printException(e, "Failed to populate textbox");
					}
				}

				panelConfElements.add(elementValue);
				panelConfElements.add(Box.createRigidArea(new Dimension(0, 15)));
			}
		}
		
		if (panelConfElements.getComponentCount() == 0) {
			panelConfElements.add(new JLabel("This sensor does not require any configuration."));
		}
		
		panelConfElements.updateUI();
	}

	/**
	 * Loads values from input fields and stores them into configuration
	 * variable
	 */
	private void getElementValuesFromInputFields() {
		Field[] fields = currentConfiguration.getClass().getFields();
		for (Field field : fields) {
			// System.out.println(field.getName());
			SensorConfigurationElement annotation = field.getAnnotation(SensorConfigurationElement.class);
			if (annotation != null) {
				for (Component element : panelConfElements.getComponents()) {
					if (field.getName().equals(element.getName())) {
						try {
							if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
								field.set(currentConfiguration, Integer.valueOf(((JTextField) element).getText()));
							} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
								field.set(currentConfiguration, Long.valueOf(((JTextField) element).getText()));
							} else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
								field.set(currentConfiguration, Boolean.valueOf(((JCheckBox) element).isSelected()));
							} else if (field.getType().equals(String.class)) {
								field.set(currentConfiguration, ((JTextField) element).getText());
							} else if (field.getType() instanceof Class && ((Class<?>) field.getType()).isEnum()) {
								field.set(currentConfiguration, ((JComboBox<?>) element).getSelectedItem());
							} else {
								JOptionPane.showMessageDialog(panelConfElements, annotation.name() + " has an unknonw data type: " + field.getType().getName());
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							JOptionPane.showMessageDialog(panelConfElements, annotation.name() + " has an invalid value. Value must be of type: " + field.getType().getName());
							LogHandler.printException(e, annotation.name() + " has an invalid value. Value must be of type: " + field.getType().getName());
						}
					}
				}
			}
		}
	}

	/**
	 * This method opens the frame.
	 */
	@SuppressWarnings("unchecked")
	public <T extends ISensorRecorderConfiguration> T showSensorConfiguratorDialog(String title, T configuration) {
		currentConfiguration = configuration;
		generateElements();

		instance.setTitle(title);
		instance.setVisible(true);

		return (T) currentConfiguration;
	}

	/**
	 * This method closes the frame.
	 */
	public void close() {
		instance.setVisible(false);
		instance.dispose();
	}
}
