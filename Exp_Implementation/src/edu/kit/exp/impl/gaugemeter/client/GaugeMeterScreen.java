package edu.kit.exp.impl.gaugemeter.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.sensor.LBFManager;
import edu.kit.exp.common.sensor.LBFUpdateEvent;
import edu.kit.exp.sensor.dummy.DummySensorRecorder;
import eu.hansolo.steelseries.gauges.RadialBargraph;
import eu.hansolo.steelseries.tools.ColorDef;

public class GaugeMeterScreen extends Screen {
	private static final long serialVersionUID = -1686302797991338642L;

	private JButton endButton;

	public GaugeMeterScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		Dimension screen = getScreenSize();
		setBounds(0, 0, screen.width, 612);
		setBackground(Color.WHITE);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(438, 127, 427, 366);
		add(panel);
		panel.setLayout(null);

		final RadialBargraph gauge = new RadialBargraph();
		gauge.setBounds(67, 41, 284, 327);
		gauge.setTitle("Heart Beats");
		gauge.setLcdVisible(false);
		gauge.setLedVisible(false);
		panel.add(gauge);

		LBFManager.getInstance().addLBFUpdateEvent(DummySensorRecorder.class.getName(), new LBFUpdateEvent() {

			@Override
			public void LBFValueUpdate(Object value) {
				double gaugeValue = ((double) value) * 100;
				gauge.setValue(gaugeValue);

				if (gaugeValue < 50) {
					gauge.setBarGraphColor(ColorDef.GREEN);
				} else if (gaugeValue < 80) {
					gauge.setBarGraphColor(ColorDef.YELLOW);
				} else {
					gauge.setBarGraphColor(ColorDef.RED);
				}
			}
		});

		endButton = new JButton("End Experiment");
		endButton.setBounds(438, 511, 427, 54);
		endButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thisScreen.sendResponse(new GaugeMeterScreen.ResponseObject());
				endButton.setEnabled(false);
				endButton.setText("Waiting for all other clients");
			}
		});
		add(endButton);
	}
}