package io.Risiko;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor{
	
	private Main main;
	
	private Vector2 mouseDownPos;
	private Vector2 mouseUpPos;
	private Vector2 mouseDragPos;

	public InputHandler(Main mainIn) {
		main = mainIn;
		
		mouseDownPos = new Vector2();
		mouseUpPos = new Vector2();
		mouseDragPos = new Vector2();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		main.getState().keyPressed(keycode);
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		main.getState().keyDepressed(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseDownPos = new Vector2(screenX, screenY);	//Gdx.graphics.getHeight() - 1 - 
		main.getState().buttonPressed(button);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseUpPos = new Vector2(screenX, screenY);	// Gdx.graphics.getHeight() - 1 - 
		main.getState().buttonDepressed(button);
		return true;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseDragPos = new Vector2(screenX, screenY);	// Gdx.graphics.getHeight() - 1 - 
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Getter/Setter
	public Vector2 getMouseUp() {
		return mouseUpPos;
	}
	
	public Vector2 getMouseDown() {
		return mouseDownPos;
	}
	
	public Vector2 getMouseDrag() {
		return mouseDragPos;
	}
}
