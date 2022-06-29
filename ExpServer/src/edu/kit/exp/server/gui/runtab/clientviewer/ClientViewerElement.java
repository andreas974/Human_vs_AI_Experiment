package edu.kit.exp.server.gui.runtab.clientviewer;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import edu.kit.exp.common.ClientStatus;
import edu.kit.exp.common.sensor.SensorStatus;

public class ClientViewerElement extends JPanel {

	private static final long serialVersionUID = -8190758065536621400L;

	public ClientViewerElement(ClientStatus clientStatus) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel pElements = new JPanel();
		pElements.setLayout(new BoxLayout(pElements, BoxLayout.PAGE_AXIS));
		pElements.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.add(pElements);

		if (!clientStatus.isUpToDate()) {
			JLabel lClientOnline = new JLabel("<html>Client is offline</html>");
			lClientOnline.setHorizontalAlignment(SwingConstants.CENTER);
			pElements.add(lClientOnline);
			pElements.add(createSeparator());
			
			pElements.setBackground(Color.RED);
		}
		else {
			pElements.setBackground(Color.WHITE);
		}
		
		JLabel lClientName = new JLabel("<html>Client: <i>" + clientStatus.getClientId() + "</i></html>");
		pElements.add(lClientName);
		if (!clientStatus.getHostName().equals(clientStatus.getClientId())) {
			JLabel lHostName = new JLabel("<html>Host: <i>" + clientStatus.getHostName() + "</i></html>");
			pElements.add(lHostName);
		}
		
		if (clientStatus.isUpToDate()) {
			pElements.add(createSeparator());
			
			JLabel lCurrentScreen = new JLabel("<html>Current Screen:<br>> <i>" + clientStatus.getCurrentScreenName() + "</i></html>");
			pElements.add(lCurrentScreen);
			
			if (clientStatus.getSensors().size() > 0) {
				pElements.add(createSeparator());
				
				JLabel lSensorList = new JLabel("Sensors:");
				pElements.add(lSensorList);
				for (SensorStatus sensorStatus : clientStatus.getSensors()) {
					JLabel lSensor = new JLabel("> " + sensorStatus.getSensorName() + ": " + (sensorStatus.isStatusOk() ? "ON" : "OFF"));
					pElements.add(lSensor);
					if (!sensorStatus.isStatusOk()) {
						lSensor.setForeground(Color.RED);
						JLabel lSensorMessage = new JLabel(">> " + sensorStatus.getMessage());
						pElements.add(lSensorMessage);
					}
					else {
						lSensor.setForeground(Color.BLACK);
					}
				}
			}
		}
	}
	
	private JSeparator createSeparator(){
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
		separator.setForeground(Color.GRAY);
		return separator;
	}

}
