package io.Risiko.Game.GameMain.Gameplay;

import java.util.ArrayList;
import java.util.HashSet;

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
import io.Risiko.Interfaces.BehaviourProfile;
import io.Risiko.Interfaces.Controller;
import io.Risiko.Interfaces.KeyProfile;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyDepressed(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonPressed(int buttoncode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonDepressed(int buttoncode) {
		// TODO Auto-generated method stub
		
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
	
	protected Main getMain() {
		return main;
	}
	
	protected GameplayModel getModel() {
		return model;
	}
	
	private class TroopMovementPhase implements BehaviourProfile {

		@Override
		public boolean step() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
