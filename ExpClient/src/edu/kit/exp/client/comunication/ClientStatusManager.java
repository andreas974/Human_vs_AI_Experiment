package edu.kit.exp.client.comunication;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.common.ClientStatus;

import java.util.Timer;
import java.util.TimerTask;

public class ClientStatusManager {

	private static ClientStatusManager instance;
	private Timer heartBeatTimer;
	private TimerTask timerTask;
	private boolean connectionIsActive;
	
	private ClientStatus clientStatus;

	public ClientStatus getClientStatus() {
		return clientStatus;
	}
	
	public static ClientStatusManager getInstance() {
		if (instance == null){
			instance = new ClientStatusManager();
		}
		return instance;
	}
	
	private ClientStatusManager(){
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				if(connectionIsActive) {
					ClientGuiController.getInstance().sendClientStatus(clientStatus);
				}
			}
		};

		clientStatus = new ClientStatus();

		connectionIsActive = false;
		heartBeatTimer = new Timer(true);
		heartBeatTimer.scheduleAtFixedRate(timerTask, 1000, 1000);		
	}
	
	public void start(){
		refreshClientId();
		connectionIsActive = true;
	}

	private void refreshClientId() {
		clientStatus.setClientId(ClientGuiController.getInstance().getClientId());
	}

	public void stop(){
		connectionIsActive = false;
	}
}
