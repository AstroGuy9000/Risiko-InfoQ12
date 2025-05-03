package io.Risiko;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Interfaces.Controller;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main implements ApplicationListener {
	
	public static final float WORLD_HEIGHT = 550;
	public static final float WORLD_WIDTH = 800;

	private ShapeRenderer shRend;
	private SpriteBatch batch;
	
	//private BitmapFont font;

	private float accumulator;
	private static final float FRAME_TIME = 1f / 60f;
	
	private Json json;
	
	private Stage stageUI;
	private Skin skin;
	
	private InputMultiplexer multiInput;
	
	// Inputs
	private InputHandler input;
	private KeyBinds binds;
	private ArrayList<Integer> keysHeldArray;		// Inputs von der Tastatur
	private ArrayList<Integer> buttonsHeldArray;	// Inputs von der Maus
	private Vector2 mouseDown;
	private Vector2 mouseUp;
	private Vector2 mouseDrag;
	
	// Text-Input
	private TextInputHandler textHandler;
	
	// State
	private Controller scene;
	
	@Override
	public void create() {
		// Prepare your application here.
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		shRend = new ShapeRenderer();
		batch = new SpriteBatch();
		
		stageUI = new Stage(new ExtendViewport(width *2, height *2));
		skin = new Skin(Gdx.files.local("data/ui/Commodore64/uiskin.json"));
		
		json = new Json();
		
		textHandler = new TextInputHandler();
		
		scene = new MainMenu(this);
			
		// erst muss state erstellt werden weil sich InputHandler state braucht
		input = new InputHandler(this);
		FileHandle file = Gdx.files.local("data/binds.json");
		try {
			binds = new KeyBinds(json.fromJson(int[].class, file));
		} catch (Exception e) {
			file.writeString(json.prettyPrint(KeyBinds.defaultBindsArr()), false);
			binds = new KeyBinds();
			e.printStackTrace();
		}
		keysHeldArray = new ArrayList<Integer>();
		buttonsHeldArray = new ArrayList<Integer>();
		
		multiInput = new InputMultiplexer(stageUI, input);
		
		Gdx.input.setInputProcessor(multiInput);
	}

	@Override
	public void resize(int width, int height) {
		// Resize your application here. The parameters represent the new window size.
		
		scene.resize(width, height);
		
		stageUI.getViewport().update(width, height, true);
	}

	@Override
	public void render() {	// --> Funktioniert wie Main-Loop.
		scene.drawScreen();
		
		limitGameSpeed(Gdx.graphics.getDeltaTime(), keysHeldArray, buttonsHeldArray);
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void dispose() {
		// Destroy application's resources here.
	}

	// Spiel-Logik Geschwindikeit Limiter
	public void limitGameSpeed(float delta, ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
		accumulator += delta;
		
		while (accumulator >= FRAME_TIME) {		
			scene.doTick(keysHeldArray, buttonInputs);
			
			accumulator -= FRAME_TIME;
		}
	}
	
	// Tastatur-Inputs
	public void addKeyInput(int keydcode) {
		keysHeldArray.add(keydcode);
	}
	
	public void removeKeyInput(int keydcode) {
		while(keysHeldArray.contains(keydcode)) {					// damit der Wert entfernt wird falls er irgendwie mehrmals enthalten ist
			keysHeldArray.remove(Integer.valueOf(keydcode));		// Int-Wert als Objekt damit der Wert und nicht der Wert auf dem Index des Int-Werts entfernt wird
		}
	}
	
	public boolean isKeyInputActive(int keydcode) {
		return keysHeldArray.contains(keydcode);
	}
	
	// Maus-Inputs
	public void addButtonInput(int keydcode) {
		buttonsHeldArray.add(keydcode);
	}
	
	public void removeButtonInput(int keydcode) {
		while(buttonsHeldArray.contains(keydcode)) {				// damit der Wert entfernt wird falls er irgendwie mehrmals enthalten ist
			buttonsHeldArray.remove(Integer.valueOf(keydcode));		// Int-Wert als Objekt damit der Wert und nicht der Wert auf dem Index des Int-Werts entfernt wird
		}
	}
	
	public boolean isButtonInputActive(int keydcode) {
		return buttonsHeldArray.contains(keydcode);
	}
	
	public Vector2 getMouseDown() {
		mouseDown = input.getMouseDown();
		return mouseDown;
	}
	
	public Vector2 getMouseUp() {
		mouseUp = input.getMouseUp();
		return mouseUp;
	}
	
	public Vector2 getMouseDrag() {
		mouseDrag = input.getMouseDrag();
		return mouseDrag;
	}
	
	// Getter/Setter	
	public Controller getState() {
		return scene;
	}
	
	public void setState(Controller stateIn) {
		scene = stateIn;
	}
	
	public KeyBinds getBinds() {
		return binds;
	}
	
	public void setBinds(KeyBinds bindsNew) {
		FileHandle file = Gdx.files.local("data/binds.json");
		file.writeString(json.prettyPrint(bindsNew.currentBindsArr()), false);
		binds = bindsNew;
	}
	
	public Stage getStageUI() {
		return stageUI;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public TextInputHandler getTextListener() {
		return textHandler;
	}

	public ShapeRenderer getShRend() {
		return shRend;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
}