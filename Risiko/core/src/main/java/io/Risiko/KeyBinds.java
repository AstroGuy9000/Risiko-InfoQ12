package io.Risiko;

import com.badlogic.gdx.Input.Keys;

public class KeyBinds {
	
	public final int UP;
	public final int DOWN;
	public final int LEFT;
	public final int RIGHT;
	
	public final int ACCEPT;
	public final int BACK;
	
	public final int ZOOM_IN;
	public final int ZOOM_OUT;
	
	public final int LOAD;
	public final int SHOW_POLY;
	public final int OUTLINES;
	public final int UNDO;
	
	public final int SHIFT;
	
	public static final String[] bindsKeysArr = new String[] {"UP ", "DOWN ", "LEFT ", "RIGHT ", "ACCEPT ", "BACK ", "ZOOM IN ", "ZOOM OUT ", "LOAD ", "SHOW POLY ", "SHOW OUTLINES ", "UNDO ", "SHIFT "};
	
	public KeyBinds() {
		UP = Keys.W;
		DOWN = Keys.S;
		LEFT = Keys.A;
		RIGHT = Keys.D;
		
		ACCEPT = Keys.ENTER;
		BACK = Keys.ESCAPE;
		
		ZOOM_IN = Keys.Q;
		ZOOM_OUT = Keys.E;
		
		LOAD = Keys.L;
		SHOW_POLY = Keys.R;
		OUTLINES = Keys.O;
		UNDO = Keys.Z;
		
		SHIFT = Keys.SHIFT_LEFT;
	}
	
	public KeyBinds(int[] binds) {
		UP = binds[0];
		DOWN = binds[1];
		LEFT = binds[2];
		RIGHT = binds[3];
		
		ACCEPT = binds[4];
		BACK = binds[5];
		
		ZOOM_IN = binds[6];
		ZOOM_OUT = binds[7];
		
		LOAD = binds[8];
		SHOW_POLY = binds[9];
		OUTLINES = binds[10];
		UNDO = binds[11];
		
		SHIFT = binds[12];
	}
	
	public static int[] defaultBindsArr() {
		return new int[] {Keys.W, Keys.S, Keys.A, Keys.D, Keys.ENTER, Keys.ESCAPE, Keys.Q, Keys.E, Keys.L, Keys.R, Keys.O, Keys.Z, Keys.SHIFT_LEFT};
	}
	
	public int[] currentBindsArr() {
		return new int[] {UP, DOWN, LEFT, RIGHT, ACCEPT, BACK, ZOOM_IN, ZOOM_OUT, LOAD, SHOW_POLY, OUTLINES, UNDO, SHIFT};
	}
}
