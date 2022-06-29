package edu.kit.exp.server.gui.treatment;

public enum TreatmentDrawType {
	DRAW_WITHOUT_REPLACEMENT {
		@Override
		public String toString(){
			return "factorial";
		}
	},
	
	DRAW_WITH_REPLACEMENT {
		@Override
		public String toString(){
			return "completely randomized";
		}
	}
}
