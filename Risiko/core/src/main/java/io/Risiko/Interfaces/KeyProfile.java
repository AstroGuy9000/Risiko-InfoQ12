package io.Risiko.Interfaces;

import java.util.ArrayList;

public interface KeyProfile {

	public void keyPressed(int keycode);

	public void keyDepressed(int keycode);
	
	public void buttonPressed(int buttoncode);
	
	public void buttonDepressed(int buttoncode);
	
	public void duringPressed(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs);
	
	public void nextMode();
}
