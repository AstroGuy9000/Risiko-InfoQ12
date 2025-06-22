package io.Risiko.Utils;

import java.util.ArrayList;

import io.Risiko.Main;

/**
 * Die Controller Interface dient dazu ermöglicht es verschiedene Controller mit verschiedenem Verhalten zu nutzen.
 * Der Controller beinhaltet alle Funktionen die nötig sind um eine Spielszene(Menü usw.) zu implementieren.
 */
public interface Controller {
	
	public void drawScreen();
	
	public void resize(int width, int height);
	
	public void doTick(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs);
	
	public void keyPressed(int keycode);
	
	public void keyDepressed(int keycode);
	
	public void buttonPressed(int buttoncode);
	
	public void buttonDepressed(int buttoncode);
}
