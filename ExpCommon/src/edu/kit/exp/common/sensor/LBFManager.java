package edu.kit.exp.common.sensor;

import java.util.HashMap;

public class LBFManager {

	private static LBFManager instance = new LBFManager();

	private HashMap<String, Object> lbfValuesList;
	private HashMap<String, LBFUpdateEvent> lbfUpdateEvents;
		
	public static LBFManager getInstance(){
		return instance;
	}

	private LBFManager(){
		lbfValuesList = new HashMap<>();
		lbfUpdateEvents = new HashMap<>();
	}
	
	public void addLBFUpdateEvent(String key, LBFUpdateEvent eventListener){
		lbfUpdateEvents.put(key, eventListener);
	}
	
	public void updateLBFValue(String key, Object value){
		lbfValuesList.put(key, value);
		if (lbfUpdateEvents.containsKey(key)) {
			lbfUpdateEvents.get(key).LBFValueUpdate(value);
		}
	}
	
	public Object getLBFValue(String key){
		return lbfValuesList.get(key);
	}
}