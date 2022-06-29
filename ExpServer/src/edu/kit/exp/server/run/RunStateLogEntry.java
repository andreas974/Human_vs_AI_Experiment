package edu.kit.exp.server.run;

import org.joda.time.DateTime;

import edu.kit.exp.common.Constants;

public class RunStateLogEntry {

	public enum ServerEvent {
		INITIALIZING {
		      public String toString() {
		          return "Initializing";
		      }
		  },
		SESSIONUPDATE {
		      public String toString() {
		          return "Session update";
		      }
		  },
		FINISHING {
		      public String toString() {
		          return "Finishing";
		      }
		  },
		CLIENTUPDATE {
		      public String toString() {
		          return "Client update";
		      }
		  }
	}
	
	private String[] values = new String[5];
	public String[] getValues(){
		return values;
	}	

	public void setTimestamp(String timestamp) {
		this.values[0] = timestamp;
	}

	public String getClientId() {
		return values[1];
	}
	public void setClientId(String clientId) {
		this.values[1] = clientId;
	}

	public String getEvent() {
		return values[2];
	}
	public void setEvent(String event) {
		this.values[2] = event;
	}
	
	public String getName() {
		return values[3];
	}
	public void setName(String name) {
		this.values[3] = name;
	}

	public String getValue() {
		return values[4];
	}
	public void setValue(String value) {
		this.values[4] = value;
	}
	
	private boolean overwriteLatestEntry;
	public boolean isOverwriteLatestEntry() {
		return overwriteLatestEntry;
	}
	public void setOverwriteLatestEntry(boolean overwriteLatestEntry) {
		this.overwriteLatestEntry = overwriteLatestEntry;
	}

	public RunStateLogEntry(){
		for (int i = 0; i < values.length; i++) {
			values[i] = "";
		}
		
		this.setTimestamp(DateTime.now().toString(Constants.TIME_STAMP_FORMAT));
		this.setOverwriteLatestEntry(false);
	}
	
	public RunStateLogEntry(String clientId, String event, String name, String value){
		this();
		this.setClientId(clientId);
		this.setEvent(event);
		this.setName(name);
		this.setValue(value);
	}
}
