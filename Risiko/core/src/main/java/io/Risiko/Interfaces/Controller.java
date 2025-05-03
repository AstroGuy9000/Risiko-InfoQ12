package io.Risiko.Interfaces;

import java.util.ArrayList;

import io.Risiko.Main;

public interface Controller {
	
	public void drawScreen();
	
	public void resize(int width, int height);
	
	public void doTick(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs);
	
	public void keyPressed(int keycode);
	
	public void keyDepressed(int keycode);
	
	public void buttonPressed(int buttoncode);
	
	public void buttonDepressed(int buttoncode);
}
