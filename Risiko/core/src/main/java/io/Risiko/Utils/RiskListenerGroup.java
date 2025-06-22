package io.Risiko.Utils;

import java.util.ArrayList;

public class RiskListenerGroup {

	ArrayList<RiskListener> listeners;
	
	public RiskListenerGroup() {
		listeners = new ArrayList<RiskListener>();
	}
	
	public void add(RiskListener listener) {
		listeners.add(listener);
	}
	
	public void remove(RiskListener listener) {
		listeners.remove(listener);
	}
	
	public void nukeListeners() {
		listeners.clear();
	}
	
	public void thingHappened() {
		for(RiskListener i: listeners) {
			i.thingHappened();
		}
	}
}
