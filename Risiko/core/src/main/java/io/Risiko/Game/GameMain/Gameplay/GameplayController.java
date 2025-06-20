package io.Risiko.Game.GameMain.Gameplay;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.Risiko.KeyBinds;
import io.Risiko.Main;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMap.Continent;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Utils.BehaviourProfile;
import io.Risiko.Utils.Controller;
import io.Risiko.Utils.KeyProfile;
import io.Risiko.Utils.Utils;

public class GameplayController implements Controller{
	
	private Main main;
	
	private Vector2 mouseDown;
	private Vector2 mouseDrag;
	private Vector2 mouseDragLast;
	
	private float panMult = 0.5f;
	
	private KeyBinds binds;
	
	private GameplayModel model;
	private GameplayView view;
	
	private OrthographicCamera cam;
	
	private KeyProfile profile;
	
	public GameplayController(Main mainIn, TravelNetwork map,ArrayList<Player> playersIn) {
		main = mainIn;
		
		binds = main.getBinds();
		
		model = new GameplayModel(map, playersIn);
		view = new GameplayView(this);
		
		cam = view.getCam();
		clampCam();
		
		Object[] countriesArr = model.getStrToCountry().values().toArray();
		ArrayList<Country> countries = new ArrayList<Country>();
		for(Object i: countriesArr) {
			countries.add( (Country) i );
		}
		
		playersIn.get(0).distributeCountries(countries);
		
		profile = new Generic();
	}

	@Override
	public void drawScreen() {
		view.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doTick(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
		profile.duringPressed(keyInputs, buttonInputs);
		
		cam.update();
	}

	@Override
	public void keyPressed(int keycode) {
		profile.keyPressed(keycode);
	}

	@Override
	public void keyDepressed(int keycode) {
		profile.keyDepressed(keycode);
	}

	@Override
	public void buttonPressed(int buttoncode) {
		profile.buttonPressed(buttoncode);
	}

	@Override
	public void buttonDepressed(int buttoncode) {
		profile.buttonDepressed(buttoncode);
	}
	
	private void clampCam() {
		float ZoomX = Main.WORLD_WIDTH/cam.viewportWidth;
		float ZoomY = Main.WORLD_HEIGHT/cam.viewportHeight;
		float maxZoom = Math.min(ZoomX, ZoomY);
		cam.zoom = MathUtils.clamp(cam.zoom, 0.2f, maxZoom);
		
		float realViewportWidth = cam.viewportWidth * cam.zoom;
		float realViewportHeight = cam.viewportHeight * cam.zoom;

		cam.position.x = MathUtils.clamp(
				cam.position.x, 
				realViewportWidth / 2f,
				Main.WORLD_WIDTH - realViewportWidth / 2f);
			
		cam.position.y = MathUtils.clamp(
				cam.position.y, 
				realViewportHeight / 2f,
				Main.WORLD_HEIGHT - realViewportHeight/ 2f);
	}
	
	public void suspendInput() {
		profile = new Suspended();
	}
	
	public void resumeInput() {
		profile = new Generic();
	}
	
	protected Main getMain() {
		return main;
	}
	
	protected GameplayModel getModel() {
		return model;
	}
	
	private void defDuringPressed(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
		
		if(keyInputs.contains(binds.UP)) {
			cam.position.y += 2;
			clampCam();
		}
			
		if(keyInputs.contains(binds.DOWN)) {
			cam.position.y -= 2;
			clampCam();
		}
		
		if(keyInputs.contains(binds.LEFT)) {
			cam.position.x -= 2;
			clampCam();
		}
		
		if(keyInputs.contains(binds.RIGHT)) {
			cam.position.x += 2;
			clampCam();
		}
		
		if(keyInputs.contains(binds.ZOOM_IN)) {
			cam.zoom += 0.05;
			clampCam();
		}
		
		if(keyInputs.contains(binds.ZOOM_OUT)) {
			cam.zoom -= 0.05;
			clampCam();
		}
		
		if(buttonInputs.contains(Input.Buttons.RIGHT)) {	
			if(mouseDragLast == null) mouseDragLast = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			mouseDrag = new Vector2(Gdx.input.getX(), Gdx.input.getY());	// wenn man mouseDrag aus dem Input-Handler geht funktionerts nicht richtig???
			
			cam.position.x += (
					(mouseDragLast.x - mouseDrag.x) * panMult
					);
			cam.position.y += (
					(mouseDrag.y - mouseDragLast.y) * panMult
					);	// andersrum weil World-Koordinaten von unten nach oben und Screen Koordinaten von oben nach unten gehen

			mouseDragLast = mouseDrag;
			
			clampCam();
		}
	}
	
	private void mouseDrag(int buttoncode) {
		if(buttoncode == Input.Buttons.RIGHT) {
			mouseDrag = new Vector2(Gdx.input.getX(), Gdx.input.getY());	// mouseDrag aus dem Input-Handler ist wieder komisch
			mouseDragLast = mouseDrag;
		}
	}
	
	private class Suspended implements KeyProfile {

		@Override
		public void keyPressed(int keycode) {
			
			if(keycode == binds.BACK) {
				// To-Do
			}
			
			main.addKeyInput(keycode);
		}

		@Override
		public void keyDepressed(int keycode) {
			main.removeKeyInput(keycode);
		}

		@Override
		public void buttonPressed(int buttoncode) {
			mouseDown = main.getMouseDown();
			mouseDown = Utils.vec2unproject(cam, mouseDown);
			
			main.addButtonInput(buttoncode);
		}

		@Override
		public void buttonDepressed(int buttoncode) {
			main.removeButtonInput(buttoncode);
		}

		@Override
		public void duringPressed(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {}

		@Override
		public void nextMode() {}
	}
	
	private class Generic implements KeyProfile {

		@Override
		public void keyPressed(int keycode) {
			
			if(main.isKeyInputActive(binds.SHIFT)) {
				if(keycode == binds.ACCEPT) {
					model.nextPhase();
				}
				
				if(keycode == binds.UNDO) {
					System.out.println("undo");
					model.undoAction();
				}
			} else {
				if(keycode == binds.ACCEPT) {
					view.requestNumberForAction();
				}
				
				if(keycode == binds.UNDO) {
					model.resetSelection();
				}
			}	
			
			if(keycode == binds.BACK) {
				// To-Do
			}
			
			main.addKeyInput(keycode);
		}

		@Override
		public void keyDepressed(int keycode) {
			main.removeKeyInput(keycode);
		}

		@Override
		public void buttonPressed(int buttoncode) {
			
			mouseDown = main.getMouseDown();
			mouseDown = Utils.vec2unproject(cam, mouseDown);
			
			if(buttoncode == Input.Buttons.LEFT) {
				for(Country i: model.getStrToCountry().values()) {
					if(i.getPolyFull().getPolygon().contains(mouseDown)) {
						model.intoSelection(i);
					}
				}
			}
			
			mouseDrag(buttoncode);
			
			main.addButtonInput(buttoncode);
		}

		@Override
		public void buttonDepressed(int buttoncode) {
			main.removeButtonInput(buttoncode);
		}

		@Override
		public void duringPressed(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
			defDuringPressed(keyInputs, buttonInputs);
		}

		@Override
		public void nextMode() {}
	}
}
