package io.Risiko.Game.GameMap;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

import io.Risiko.KeyBinds;
import io.Risiko.Main;
import io.Risiko.Interfaces.Controller;
import io.Risiko.Interfaces.KeyProfile;
import io.Risiko.Utils.Utils;

public class CreatorController implements Controller {
	
	private Main main;
	
	private Vector2 mouseDown;
	private Vector2 mouseDrag;
	private Vector2 mouseDragLast;
	
	private float panMult = 0.5f;
	
	private KeyBinds binds;
	
	private GenManager model;
	private CreatorView view;
	
	private OrthographicCamera cam;
	
	private KeyProfile profile;
	private CrtKeyProfile profileEnum;
	
	public CreatorController(Main mainIn) {
		main = mainIn;
		
		binds = main.getBinds();
		
		model = new GenManager();
		view = new CreatorView(this);
		
		cam = view.getCam();
		clampCam();
		
		profile = new Edit();
		profileEnum = CrtKeyProfile.EDIT;
		
		view.setViewPoly(true);
		view.setViewOutline(true);
	}
	
	@Override
	public void drawScreen() {
		ScreenUtils.clear(1, 0, 0, 1, true);
		view.render();
	}
	
	public void resize(int width, int height) {
		
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
	
	public boolean isSuspended() {
		return profile.getClass() == Suspended.class;
	}
	
	public void suspendInput() {
		profile = new Suspended();
	}
	
	public void resumeInput() {
		System.out.println("called");
		switch(profileEnum) {
			default:
				profile = new Edit();
				profileEnum = CrtKeyProfile.EDIT;
				System.out.println("called default");
				break;
				
			case CrtKeyProfile.EDIT:
				profile = new Edit();
				break;
				
			case CrtKeyProfile.SELECT:
				profile = new Select();
				break;
				
			case CrtKeyProfile.CONNECTION:
				profile = new Connection();
				break;
				
		}
	}
	
	public Main getMain() {
		return main;
	}
	
	public GenManager getModel() {
		return model;
	}
	
	public CreatorView getView() {
		return view;
	}
	
	private void resetViewSettings() {
		view.setViewPoly(true);
		view.setViewOutline(true);
		view.setViewCont(false);
		view.setViewConnections(false);
	}
	
	public void queueSelect() {
		
		resetViewSettings();
		
		if(!isSuspended()) {
			profile = new Select();
			newWorkingCountry();
		}
		model.setSelection(new ArrayList<Country>());
		profileEnum = CrtKeyProfile.SELECT;
		
		view.updateModeSelectorName();
	}
	
	public void queueEdit() {
		
		resetViewSettings();
		
		if(!isSuspended()) profile = new Edit();
		model.setSelection(new ArrayList<Country>());
		profileEnum = CrtKeyProfile.EDIT;
		
		view.updateModeSelectorName();
	}
	
	public void queueConnection() {
		
		resetViewSettings();
		
		if(!isSuspended()) profile = new Connection();
		newWorkingCountry();
		model.setSelection(new ArrayList<Country>());
		profileEnum = CrtKeyProfile.CONNECTION;
		
		view.updateModeSelectorName();
	}
	
	protected CrtKeyProfile getProfileEnum() {
		return profileEnum;
	}
	
	public boolean saveModelToFile(FileHandle file) {
		if(file.isDirectory()) return false;
		
		Json json = new Json();
		
		try {
			file.writeString(json.prettyPrint(model.saveModel()), false);
			
			view.toggleMenus();
			view.toggleMenus();
		}catch (Exception e) {
			System.out.println("Error when saving to existing file");
		}
		return true;
	}
	
	public boolean saveModelToFileAs(String filename) {
		Json json = new Json();
		FileHandle file = Gdx.files.local("MapMaker/"+filename + ".json");
		
		try {
			file.writeString(json.prettyPrint(model.saveModel()), false);
			
			System.out.println("Saved to: " + file.path());
			
			view.toggleMenus();
			view.toggleMenus();
		}catch (Exception e) {
			System.out.println("Error when saving to new File");
		}
		return true;
	}

	public boolean loadModel(FileHandle file) {
		if(file.isDirectory()) return false;

		Json json = new Json();
		
		try {
		
			TravelNetworkSave loadedData = json.fromJson(TravelNetworkSave.class, file);
			model.loadModel(loadedData);
			
			view.setViewPoly(false);
			view.setViewOutline(false);
			
			view.toggleMenus();
			view.toggleMenus();
		}catch (Exception e) {
			//System.out.println("Error when loading");
		}
		return true;
	}
	
	private void newWorkingCountry() {
		setWorkingCountry(new Country());
	}
	
	private void setWorkingCountry(Country country) {
		Country workingCountry = model.getWorkingCountry();
		
		if((workingCountry.getVertsList().size() >= 6)) {
			if(workingCountry.getName() == null) {
				view.countryNameRequest(workingCountry);
				return;
			}else {
				model.addCountry(workingCountry);
			}
		}
		
		model.setWorkingCountry(country);
	}
	
	protected void receiveCountryName(String name, Country country) {
		
		if(name == null) {
			System.out.println("name is null");
			
			model.setWorkingCountry(new Country());
			return;
		}

		for(Country i: model.getCountries()) {
			if(i.getName() == name) {
				model.setWorkingCountry(new Country());
				return;
			}
		}
		
		if(country.getName() == null) country.setName(name);
		else model.renameCountry(country, name);
		
		model.addCountry(country);
		model.setWorkingCountry(new Country());
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
	
	// Default Verhalten für manche Sachen
	
	public void defDuringPressed(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
		
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
	
	private void defKeyPressed(int keycode) {
		if(keycode == binds.BACK) {
			view.toggleMenus();
		}
		
		if(main.isKeyInputActive(binds.SHIFT) && keycode == binds.CYCLE) {
			main.addKeyInput(keycode);
			profile.nextMode();
			return;
		}
	}
	
	private void mouseDrag(int buttoncode) {
		if(buttoncode == Input.Buttons.RIGHT) {
			mouseDrag = new Vector2(Gdx.input.getX(), Gdx.input.getY());	// mouseDrag aus dem Input-Handler ist wieder komisch
			mouseDragLast = mouseDrag;
		}
	}
	
	// Verhalten in verschiedenen Modi
	private class Suspended implements KeyProfile{

		@Override
		public void keyPressed(int keycode) {
			if(keycode == binds.BACK) {
				view.toggleMenus();
			}
			
			main.addKeyInput(keycode);
		}

		@Override
		public void keyDepressed(int keycode) {
			main.removeKeyInput(keycode);
		}

		@Override
		public void buttonPressed(int buttoncode) {
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
	
	private class Select implements KeyProfile{

		@Override
		public void keyPressed(int keycode) {
			
			if(main.isKeyInputActive(binds.SHIFT)) {			
				if(keycode == binds.UNDO) {
					model.setSelection(new ArrayList<Country>());
				}
			}else {
				if(keycode == binds.UNDO) {
					if(!model.getSelection().isEmpty()) model.getSelection().removeLast();
				}
			}
			
			if(keycode == binds.OUTLINES) {
				view.toggleViewCont();
			}
			
			
			if(keycode == binds.ACCEPT) {
				view.continentSelectionRequest();
			}
			
			defKeyPressed(keycode);
			
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
				for(Country i: model.getCountries()) {
					if(i.isDrawReady() && i.getPolyFull().getPolygon().contains(mouseDown)) {
						if(main.isKeyInputActive(binds.SHIFT)) {
							view.countryNameRequest(i);
						}else {
							if(model.getSelection().contains(i)) model.getSelection().remove(i);
							else model.getSelection().add(i);
						}
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
		public void nextMode() {
			queueConnection();
		}
	}
	
	private class Edit implements KeyProfile {
		
		@Override
		public void keyPressed(int keycode) {
			
			if(keycode == binds.SHOW_POLY) {
				view.toggleViewPoly();
			}
			
			if(keycode == binds.OUTLINES) {
				view.toggleViewOutline();
			}
			
			if(keycode == binds.UNDO) {
				model.getWorkingCountry().undoVert();
			}
			
			if(keycode == binds.ACCEPT) {
				newWorkingCountry();
			}
			
			defKeyPressed(keycode);
			
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
			
			if(main.isKeyInputActive(binds.SHIFT)) {
				if(buttoncode == Input.Buttons.LEFT) {
					for(Country i: model.getCountries()) {
						if(i.isDrawReady() && i.getPolyFull().getPolygon().contains(mouseDown)) {
							if(model.getWorkingCountry() == i) newWorkingCountry();

							else setWorkingCountry(i);
							break;
						}
					}
				}
			}else {
				if(buttoncode == Input.Buttons.LEFT) model.getWorkingCountry().addVert(mouseDown);
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
		public void nextMode() {
			queueSelect();
		}
	}
	
	private class Connection implements KeyProfile {
		
		Vector2 mouseDownPerm;
		
		private Connection() {
			model.setSelection(new ArrayList<Country>());
			view.setViewConnections(true);
			
			mouseDownPerm = null;
		} 

		@Override
		public void keyPressed(int keycode) {
			
			ArrayList<Country> selection = model.getSelection();
			
			if(keycode == binds.UNDO) {
				if(main.isKeyInputActive(binds.SHIFT)) {
					selection.clear();
					model.setWorkingLineIndex(-1);
				}else {
					model.removeLine(model.getWorkingLineIndex());
				}
			}
			
			if(keycode == binds.ACCEPT) {
				
				if(selection.size() == 2 && !main.isKeyInputActive(binds.SHIFT)) {
					TravelNetwork travel = model.getTravel();
					
					Country countryA = selection.get(0);
					Country countryB = selection.get(1);
					
					if(travel.getMovMap().get(countryA.getName()).contains(countryB)) {
						travel.removeRoute(countryA, countryB);
					} else {
						travel.addRoute(countryA, countryB);
					} 
				}
				
				if(main.isKeyInputActive(binds.SHIFT)) model.autoAddRoutes();
			}
			
			if(!main.isKeyInputActive(binds.SHIFT) && keycode == binds.CYCLE) {
				model.nextLine();
			}
			
			defKeyPressed(keycode);
			
			main.addKeyInput(keycode);
		}

		@Override
		public void keyDepressed(int keycode) {
			main.removeKeyInput(keycode);
		}

		@Override
		public void buttonPressed(int buttoncode) {
			
			ArrayList<Country> selection = model.getSelection();
			
			mouseDown = main.getMouseDown();
			mouseDown = Utils.vec2unproject(cam, mouseDown);
			
			if(!main.isKeyInputActive(binds.SHIFT)) {
				if(buttoncode == Input.Buttons.LEFT) {
					for(Country i: model.getCountries()) {
						if(i.isDrawReady() && i.getPolyFull().getPolygon().contains(mouseDown)) {
							if(selection.contains(i)) {
								selection.remove(i);
								return;
							}
							if(selection.size() >= 2) {
								selection.removeLast();
							}
							selection.add(i);
							break;
						}
					}
				}
			}else {
				if(buttoncode == Input.Buttons.LEFT) mouseDownPerm = mouseDown;
			}
			
			mouseDrag(buttoncode);
			
			main.addButtonInput(buttoncode);
		}

		@Override
		public void buttonDepressed(int buttoncode) {
			
			if(mouseDownPerm != null && buttoncode == Input.Buttons.LEFT) {
				Vector2 mouseUp = Utils.vec2unproject(cam, new Vector2(Gdx.input.getX(), Gdx.input.getY()));;
				
				model.addLine(mouseDownPerm, mouseUp);
				mouseDownPerm = null;
			}
			
			main.removeButtonInput(buttoncode);
			
		}

		@Override
		public void duringPressed(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
			defDuringPressed(keyInputs, buttonInputs);
		}

		@Override
		public void nextMode() {
			queueEdit();
		}
	}
	
	protected enum CrtKeyProfile {
		SELECT,
		EDIT,
		CONNECTION;
	}
}
