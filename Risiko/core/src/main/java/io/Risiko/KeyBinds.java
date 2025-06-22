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
	
	public final int CYCLE;
	public final int FUNC1;
	public final int FUNC2;
	public final int UNDO;
	
	public final int SHIFT;
	
	public static final String[] bindsKeysArr = new String[] {"UP ", "DOWN ", "LEFT ", "RIGHT ", "ACCEPT ", "BACK ", "ZOOM IN ", "ZOOM OUT ", "CYCLE ", "FUNCTION 1 ", "FUNCTION 2 ", "UNDO ", "SHIFT "};
	
	public KeyBinds() {
		UP = Keys.W;
		DOWN = Keys.S;
		LEFT = Keys.A;
		RIGHT = Keys.D;
		
		ACCEPT = Keys.ENTER;
		BACK = Keys.ESCAPE;
		
		ZOOM_IN = Keys.Q;
		ZOOM_OUT = Keys.E;
		
		CYCLE = Keys.TAB;
		FUNC1 = Keys.R;
		FUNC2 = Keys.O;
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
		
		CYCLE = binds[8];
		FUNC1 = binds[9];
		FUNC2 = binds[10];
		UNDO = binds[11];
		
		SHIFT = binds[12];
	}
	
	public static int[] defaultBindsArr() {
		return new int[] {Keys.W, Keys.S, Keys.A, Keys.D, Keys.ENTER, Keys.ESCAPE, Keys.Q, Keys.E, Keys.TAB, Keys.R, Keys.O, Keys.Z, Keys.SHIFT_LEFT};
	}
	
	public int[] currentBindsArr() {
		return new int[] {UP, DOWN, LEFT, RIGHT, ACCEPT, BACK, ZOOM_IN, ZOOM_OUT, CYCLE, FUNC1, FUNC2, UNDO, SHIFT};
	}
}
