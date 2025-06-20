package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.Risiko.KeyBinds;
import io.Risiko.Main;
import io.Risiko.CustomWidgets.ListCust;
import io.Risiko.CustomWidgets.TextFieldCust;
import io.Risiko.CustomWidgets.FileMenu.FileMenu;
import io.Risiko.CustomWidgets.FileMenu.FileMenuListener;
import io.Risiko.CustomWidgets.TextInputWindow.TextInputWindow;
import io.Risiko.CustomWidgets.TextInputWindow.TextInputWindowListener;
import io.Risiko.Game.GameMap.CreatorController.CrtKeyProfile;
import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Utils.Utils;

public class CreatorView {
	
	private CreatorController controller;
	private GenManager model;
	private Main main;
	
	private Texture backTex;
	
	// UI-Zeug
	private Stage stageUI;
	private Skin skin;
	private Table mainTab;
	private Table popupTab;
	
	private Window escMenu;
	private VerticalGroup escMenuGroup;
	private TextButton toMenu;
	private TextButton loadFile;
	private TextButton saveFile;
	private TextButton saveFileAs;
	private TextButton toggleSelectMode;
	private TextButton addContinent;
	
	// Map-Grafik
	private ExtendViewport viewport;
	private Stage stageMap;
	private OrthographicCamera cam;
	
	private FileHandle file;
	
	private Texture templateTex;
	
	private boolean viewPoly;
	private boolean viewOutline;
	private boolean viewCont;
	private boolean viewConnections;
	
	public CreatorView(CreatorController controllerIn) {
		controller = controllerIn;
		model =  controller.getModel();
		main = controller.getMain();
		
		// Spiel Grafik
		stageUI = main.getStageUI();
		stageUI.clear();
		
		viewport = Utils.makeDefaultViewport();
		stageMap = new Stage(viewport);
		cam = (OrthographicCamera) viewport.getCamera();
		
		skin = main.getSkin();
		
		mainTab = new Table(skin);
		mainTab.setFillParent(true);
		stageUI.addActor(mainTab);
		mainTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainTab.center();
		
		popupTab = new Table(skin);
		popupTab.setFillParent(true);
		stageUI.addActor(popupTab);
		popupTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		escMenu = new Window("", skin);
		escMenu.setFillParent(false);
		escMenu.center();
		escMenu.setVisible(false);
		
		escMenuGroup = new VerticalGroup();
		escMenuGroup.setFillParent(false);
		escMenuGroup.center();
		
		toMenu = new TextButton("Zurueck", skin);
		toMenu.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new MainMenu(main));
			}});
		toMenu.setFillParent(false);
		
		loadFile = new TextButton("Load File", skin);
		loadFile.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if(popupTab.hasChildren()) return;
				
				FileMenu loadFileMenu = new FileMenu("Load", skin, Gdx.files.local("MapMaker"), "json", main.getBinds());
				
				loadFileMenu.setKeyboardFocus(stageUI);
				
				popupTab.add(loadFileMenu);
				loadFileMenu.addListener(new FileMenuListener() {
					@Override
					public void selectionConfirmed(FileHandle file) {
						controller.loadModel(file);
						popupTab.clear();
					}
				});
			}});
		loadFile.setFillParent(false);
		
		saveFile = new TextButton("Save File", skin);
		saveFile.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if(popupTab.hasChildren()) return;
				
				FileMenu saveFileMenu = new FileMenu("Save", skin, Gdx.files.local("MapMaker"), "json", main.getBinds());
				popupTab.add(saveFileMenu);
				
				stageUI.setKeyboardFocus(saveFileMenu);
				
				saveFileMenu.addListener(new FileMenuListener() {
					@Override
					public void selectionConfirmed(FileHandle file) {
						controller.saveModelToFile(file);
						popupTab.clear();
					}
				});
			}});
		saveFile.setFillParent(false);
		
		saveFileAs = new TextButton("Save File As", skin);
		saveFileAs.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if(popupTab.hasChildren()) return;
				
				TextInputWindow textWindow = new TextInputWindow("New File", skin, main.getBinds());
				
				textWindow.giveKeyFocus(stageUI);
				
				popupTab.add(textWindow).width(500).fillX();
				textWindow.addListener(new TextInputWindowListener() {
					@Override
					public void textAccepted(String text) {
						if(controller.saveModelToFileAs(text)) popupTab.clear();
					}
					
					@Override
					public void exitInput() {
						stageUI.unfocusAll();
						popupTab.clear();
					}
				});
			}});
		saveFile.setFillParent(false);
		
		toggleSelectMode = new TextButton("Mode: Polygon Editing", skin);
		toggleSelectMode.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				switch(controller.getProfileEnum()) {
					default:
						controller.queueEdit();
						break;
					
					case CrtKeyProfile.EDIT:
						controller.queueConnection();
						break;
						
					case CrtKeyProfile.SELECT:
						controller.queueEdit();
						break;
						
					case CrtKeyProfile.CONNECTION:
						controller.queueSelect(); 
						break;
				}
			}});
		toggleSelectMode.setFillParent(false);
		
		addContinent = new TextButton("Kontinent Manager", skin);
		addContinent.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				popupTab.add(new ContinentMaker(skin));
			}});
		
		escMenuGroup.addActor(toMenu);
//		escMenuGroup.padBottom(5);
		escMenuGroup.addActor(loadFile);
//		escMenuGroup.padBottom(5);
		escMenuGroup.addActor(saveFile);
//		escMenuGroup.padBottom(5);
		escMenuGroup.addActor(saveFileAs);
//		escMenuGroup.padBottom(5);
		escMenuGroup.addActor(toggleSelectMode);
//		escMenuGroup.padBottom(5);
		escMenuGroup.addActor(addContinent);
		escMenu.add(escMenuGroup);
		
		escMenu.pad(30);
		
		mainTab.center().add(escMenu);
		
		FileHandle backgroundTex = Gdx.files.local("MapMaker/template.png");
		if(backgroundTex.exists()) templateTex = new Texture("mapMaker/template.png");
		else {
			Pixmap pixmap = new Pixmap( 1, 1, Format.RGBA8888 );
			pixmap.setColor(Utils.rgba(154, 157, 161, 1));
			pixmap.drawPixel(0, 0);
			templateTex = new Texture(pixmap);
		}
		
		backTex = templateTex;
		
		//stageUI.setDebugAll(true);
	}
	
	// Spiel Grafik & UI
	public void render() {
		cam.update();
		
		main.getBatch().setProjectionMatrix(cam.combined);
		main.getBatch().begin();
		
		main.getBatch().draw(backTex, 0, 0, Main.WORLD_WIDTH, Main.WORLD_HEIGHT);
		
		main.getBatch().end();
		
		main.getShRend().setProjectionMatrix(cam.combined);
		main.getShRend().begin(ShapeType.Filled);
		
		if(viewOutline) {
			for(float[] i: model.getTravel().getDecoLines()) {
				Utils.drawRoundedLine(main.getShRend(), i, 3.5f, Color.DARK_GRAY);
			}
		}
		
		if(model.getWorkingLineIndex() != -1) {
			Utils.drawRoundedLine(main.getShRend(), model.getTravel().getDecoLines().get(model.getWorkingLineIndex()), 5, Color.RED);
		}
		
		if(viewCont) {
			for(String i: model.getTravel().getContMembers().keySet()) {
				Continent cont = model.getContinents().get(i);
				Color contColor = cont.getColor();
				
				for(Country x: model.getTravel().getContMembers().get(i)) {
					drawElementOutline(x, contColor);
					drawElementPoly(x, contColor);
				}
			}
			
			main.getShRend().end();
			
			stageUI.act();
			stageUI.draw();
			
			return;
		}
		
		if(viewPoly) {
			for(Country i: model.getCountries()) {
				drawElementPoly(i, Color.RED);
			}
			
			for(Country i: model.getSelection()) {
				drawElementPoly(i, Color.YELLOW);
			}
		}
		
		if(viewOutline) {		
			for(Country i: model.getCountries()) {
				drawElementOutline(i, Color.FIREBRICK);
			}
			
			for(Country i: model.getSelection()) {
				drawElementOutline(i, Color.GOLD);
			}
		}
		
		if(viewConnections) {
			drawTravel(Color.GRAY);
			
			TravelNetwork travel = model.getTravel();
			
			Vector2 vecTemp1 = new Vector2();
			Vector2 vecTemp2 = new Vector2();
			
			for(Country i: model.getSelection()) {
				for(Country x: travel.getMovMap().get(i.getName())) {
					Utils.drawRoundedLine(
							main.getShRend(), 
							i.getPolyFull().getPolygon().getCentroid(vecTemp1),
							x.getPolyFull().getPolygon().getCentroid(vecTemp2), 
							1f, 
							Color.MAROON);
				}
				
			}
		}
		
		if(viewPoly) drawElementPoly(model.getWorkingCountry(), Color.GREEN);
		if(viewOutline) drawElementOutline(model.getWorkingCountry(), Color.FOREST);
		
		main.getShRend().end();
		
		stageUI.act();
		stageUI.draw();
	}
	
	public void resize(int width, int height) {
		viewport.update(500,500 * height/width);
		cam.update();
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
	
	public void toggleMenus() {
		if(popupTab.hasChildren()) {
			if(escMenu.isVisible()) {
				popupTab.clear();
				
				controller.suspendInput(); 
				System.out.println("suspend: popupTab --> escMenu");
				
				backTex = templateTex;
				
				return;
			}
			
			popupTab.clear();
			controller.resumeInput(); 
			return;
		}
		if(escMenu.isVisible()) {
			escMenu.setVisible(false);
			
			System.out.println("resume: escMenu --> goto game");
			
			setViewPoly(true);
			setViewOutline(true);
			
			controller.resumeInput();
		}
		else {
			escMenu.setVisible(true);
			stageUI.setKeyboardFocus(escMenu);
			
			System.out.println("suspend: game --> goto escMenu");
			
			controller.suspendInput();
		} 
	}
	
	public void setViewPoly(boolean bool) {
		viewPoly = bool;
	}
	
	public void toggleViewPoly() {
		if(viewPoly) viewPoly = false;
		else viewPoly = true;
	}
	
	public void setViewOutline(boolean bool) {
		viewOutline = bool;
	}
	
	public void toggleViewOutline() {
		if(viewOutline) viewOutline = false;
		else viewOutline = true;
	}
	
	public void setViewCont(boolean bool) {
		viewCont = bool;
	}
	
	public void toggleViewCont() {
		if(viewCont) viewCont = false;
		else viewCont = true;
	}
	
	public void setViewConnections(boolean bool) {
		viewConnections = bool;
	}
	
	public void toggleViewConnections() {
		if(viewConnections) viewConnections = false;
		else viewConnections = true;
	}
	
	protected void countryNameRequest(Country country) {
		controller.suspendInput();
		
		TextInputWindow textWindow = new TextInputWindow("Name des Landes", skin, main.getBinds());
		
		if(country.getName() != null) {
			textWindow.setText(country.getName());
		}
		
		textWindow.giveKeyFocus(stageUI);
		
		popupTab.add(textWindow).width(500).fillX();
		textWindow.addListener(new TextInputWindowListener() {
			@Override
			public void textAccepted(String text) {
				
				text = text.trim();
				
				if(text.length() > 18) {
					textWindow.setText(text.substring(0, 18));
					return;
				}
				
				if(text.equals("")) {
					textWindow.setText("");
					return;
				}
				
				for(Country i: model.getCountries()) {
					if(text.equals(i.getName())) {
						textWindow.setText(text);
						return;
					}
				}
				
				controller.receiveCountryName(text, country);
				
				popupTab.clear();
				stageUI.unfocusAll();
				controller.resumeInput();
			}
			
			@Override
			public void exitInput() {
				controller.receiveCountryName(null, country);
				
				popupTab.clear();
				stageUI.unfocusAll();
				controller.resumeInput();
			}
		});
	}
	
	protected void continentSelectionRequest() {
		if(model.getContinents().keySet().isEmpty()) return;
		
		controller.suspendInput();
		
		popupTab.clear();
		
		Window window = new Window("", skin, "dialog");
		ListCust<String> list = new ListCust<String>(skin, main.getBinds());
		TextButton confirm = new TextButton("Confirm", skin);
		
		Object[] arrGeneric = model.getContinents().keySet().toArray();
 		String[] contNameArr = new String[model.getContinents().keySet().size()];
		for(int i = 0; i < contNameArr.length; i++) {
			contNameArr[i] = (String)arrGeneric[i];
		}
		
		list.setItems(contNameArr);
		stageUI.setKeyboardFocus(list);
		list.setTypeToSelect(false);
		
		confirm.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				System.out.println("hiiiii");
				System.out.println(list.getSelected());
				
				if(list.getSelected() != null) {
					String contName = list.getSelected();
					
					model.getTravel().addMember(model.getContinents().get(contName), model.getSelection());
					
					toggleMenus();
				}
			}});
		
		window.add(list);
		window.row();
		window.add(confirm);
		
		popupTab.add(window);
	}
	
	protected void updateModeSelectorName() {
		switch(controller.getProfileEnum()) {
		default:
			toggleSelectMode.setLabel(new Label("Mode: Polygon Editing", skin));
			break;
		
		case CrtKeyProfile.EDIT:
			toggleSelectMode.setLabel(new Label("Mode: Polygon Editing", skin));
			break;
			
		case CrtKeyProfile.SELECT:
			toggleSelectMode.setLabel(new Label("Mode:  Select Element", skin));
			break;
			
		case CrtKeyProfile.CONNECTION:
			toggleSelectMode.setLabel(new Label("Mode:Edit Connections", skin));
			break;
	}
	}
	
	private void drawElementPoly(Country element, Color color) {
		int vertsNumber = element.getVertsList().size();
		
		switch(vertsNumber) {
			
		default:
			Utils.drawPolygonFilled(main.getShRend(), element.getPolyFull(), color);
//			Rectangle rect = element.getPolyFull().getBoundingRect();
//			Utils.drawDebugRect(main.getShRend(), rect, color);
			break;
			
		case 4:		
			Utils.drawRoundedLine(
					main.getShRend(), 
					
					element.getVertsList().get(0), 
					element.getVertsList().get(1), 
					
					element.getVertsList().get(2), 
					element.getVertsList().get(3), 
					
					2, 
					color);
			break;
		
		case 2:		
			main.getShRend().setColor(color);
			
			main.getShRend().circle(
					element.getVertsList().get(0), 
					element.getVertsList().get(1), 
					
					1f, 
					50);
			break;
			
		case 0:
			break;
		}
	}
	
	private void drawElementOutline(Country element, Color color) {
		int vertsNumber = element.getVertsList().size();
		
		switch(vertsNumber) {
			
		default:
			Utils.drawPolygonOutline(main.getShRend(), element.getPolyFull(), 4, color);
			break;
			
		case 4:		
			Utils.drawRoundedLine(
					main.getShRend(), 
					
					element.getVertsList().get(0), 
					element.getVertsList().get(1), 
					
					element.getVertsList().get(2), 
					element.getVertsList().get(3), 
					
					4, 
					color);
			break;
		
		case 2:		
			main.getShRend().setColor(color);
			
			main.getShRend().circle(
					element.getVertsList().get(0), 
					element.getVertsList().get(1), 
					
					2, 
					50);
			break;
			
		case 0:
			break;
		}
	}
	
	private void drawTravel(Color color) {
		TravelNetwork travel = model.getTravel();
		
		HashMap<String,Country> strToCountry = travel.getStrToCountry();
		
		Vector2 tempVec1 = new Vector2();
		Vector2 tempVec2 = new Vector2();
		

		HashMap<String,HashSet<Country>> movMap = travel.getMovMap();
			
		for(Country i: travel.getStrToCountry().values()) {
				
			for(Country x: movMap.get(i.getName())) {
				
				Utils.drawRoundedLine(
							main.getShRend(), 
							
						i.getPolyFull().getPolygon().getCentroid(tempVec1),
						x.getPolyFull().getPolygon().getCentroid(tempVec2),
							
						2f,
						color);
			}
		}
		
	}
	
	private class ContinentMaker extends Window {

		private KeyBinds binds;
		
		private int maxConts;
		
		private Continent currCont;
		
		private String tempName;
		
		private final int maxBonus = 30;
		private int tempBonus;
		
//		private Color tempColor;
		
		// Linker Teil des Fensters
		private VerticalGroup leftSide;
		
		private ListCust<String> selectCont;
		private TextButton newCont;
		private TextButton deleteCont;
		
		// Linker Teil des Fensters
		private VerticalGroup rightSide;
		
		private TextFieldCust name;
		
		private HorizontalGroup bonusModule;
		private Label bonus;
		private TextButton plus;
		private TextButton minus;
		
//		private Slider red;
//		private Slider green;
//		private Slider blue;
		
		private ContinentMaker(Skin skin) {
			super("Kontinent", skin);
			
			setViewPoly(false);
			setViewOutline(false);
			
			setMovable(false);
			setResizable(false);
			setFillParent(false);
			
			binds = main.getBinds();
			
			maxConts = 10;
			
			// Linker Teil des Fensters
			leftSide = new VerticalGroup();
			
			selectCont = new ListCust<String>(skin, binds);
			selectCont.setTypeToSelect(false);
			selectCont.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					String contName = selectCont.getOverItem();
					if(model.getContinents().get(contName) != null) {
						setCurrCont(model.getContinents().get(contName));
					}
				}});		
			
			newCont = new TextButton("Neuer Kontinent", skin);
			newCont.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					newCurrCont();
			}});
			
			deleteCont = new TextButton("Kontinent entfernen", skin);
			deleteCont.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					deleteCont();
			}});
			
			leftSide.addActor(selectCont);
			leftSide.addActor(newCont);
			leftSide.addActor(deleteCont);
			
			// Rechter Teil des Fensters
			rightSide = new VerticalGroup();
			
			name = new TextFieldCust("", skin, binds) {
				@Override
				public void thingTyped(char character) {
					updateName();
				}
				
				@Override
				public void acceptPressed() {
					stageUI.unfocusAll();
				}
				
				@Override
				public void backPressed() {
					stageUI.unfocusAll();
				}
			};
			
			bonusModule = new HorizontalGroup();
			
			bonus = new Label("", skin);
			
			plus = new TextButton("+", skin);
			plus.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					if(main.isKeyInputActive(binds.SHIFT)) modifyBonus(5);
					else modifyBonus(1);
					bonus.setText(String.format("%3s", Integer.toString(tempBonus)));
				}});
			
			minus = new TextButton("-", skin);
			minus.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					if(main.isKeyInputActive(binds.SHIFT)) modifyBonus(-5);
					else modifyBonus(-1);
					bonus.setText(String.format("%3s", Integer.toString(tempBonus)));
				}});
			
//			red = new Slider(0, 255, 1, false, skin);
//			red.addListener(new ChangeListener() {
//				public void changed(ChangeEvent event, Actor actor) {
//					updateRGBdisplay();
//				}});
//			
//			green = new Slider(0, 255, 1, false, skin);
//			green.addListener(new ChangeListener() {
//				public void changed(ChangeEvent event, Actor actor) {
//					updateRGBdisplay();
//				}});
//			
//			blue = new Slider(0, 255, 1, false, skin);
//			blue.addListener(new ChangeListener() {
//				public void changed(ChangeEvent event, Actor actor) {
//					updateRGBdisplay();
//				}});
			
			bonusModule.addActor(plus);
			bonusModule.addActor(minus);
			bonusModule.addActor(bonus);
			
			rightSide.addActor(name);
			rightSide.addActor(bonusModule);
//			rightSide.addActor(red);
//			rightSide.addActor(green);
//			rightSide.addActor(blue);
			
			add(leftSide);
			add(rightSide);
			
			TextButton saveCont = new TextButton("Kontinent speichern", skin);
			saveCont.addListener(new ChangeListener() {
				public void changed(ChangeEvent event, Actor actor) {
					saveToModel();
				}});
			
			add(saveCont);
			
			if(!model.getContinents().isEmpty()) setCurrCont((Continent)model.getContinents().values().toArray()[0]);
			else setCurrCont(new Continent());
			
			updateAll();
		}
		
		private void updateList() {
			
			String selected = selectCont.getSelected();
			
			Object[] objArr = model.getContinents().keySet().toArray();
			String[] contsNameArr = new String[model.getContinents().size()];
			
 			for(int i = 0; i <  objArr.length; i++) {
 				contsNameArr[i] = (String)objArr[i];
 			}
 			System.out.println("===");
 			System.out.println(selectCont.getItems().size);
 			selectCont.clearItems();
 			System.out.println(selectCont.getItems().size);
			selectCont.setItems(contsNameArr);
			System.out.println(selectCont.getItems().size);
			
			if(selectCont.getItems().contains(selected, false)) {
				selectCont.setSelected(selected);
				setCurrCont(model.getContinents().get(selectCont.getSelected()));
			} else {
				newCurrCont();
			}
		}
		
		private void updateName() {
			String nameNew = name.getText().trim();
			int cursorPos = name.getCursorPosition();
			
			if(nameNew.length() > 18) {
				name.setText(nameNew.substring(0, 18));
				
				System.out.println(name.getText());
				
				name.setCursorPosition(Math.min(cursorPos, name.getText().length()));
				return;
			}
			
			tempName = nameNew;
		}
		
		private void updateBonus() {
			bonus.setText(String.format("%3s", Integer.toString(tempBonus)));
		}
		
//		private void updateRGBsliders() {
//			
//			Color storeColor = new Color(tempColor);
//			
//			red.setValue(storeColor.r*255);
//			green.setValue(storeColor.g*255);
//			blue.setValue(storeColor.b*255);
//			
//			updateRGBdisplay();
//		}
		
//		private void updateRGBdisplay() {
//			tempColor = new Color(red.getValue()/255f, green.getValue()/255f, blue.getValue()/255f, 1);
//			
//			Pixmap pixmap = new Pixmap( 1, 1, Format.RGBA8888 );
//			pixmap.setColor(tempColor);
//			pixmap.drawPixel(0, 0);
//			
//			backTex = new Texture(pixmap);
//		}
		
		private void updateAll() {
			updateName();
			updateBonus();
//			updateRGBsliders();
			
			updateList();
		}
		
		private void modifyBonus(int n) {
			tempBonus = MathUtils.clamp(tempBonus + n, 1, maxBonus);
		}
		
		private boolean dupeName(HashMap<String, Continent> conts, String name) {
			for(String i: conts.keySet()) {
				if(i.equals(name)) {
					return true;
				}
			}
			
			return false;
		}
		
		private void setCurrCont(Continent cont) {
			
			currCont = cont;
			
			tempName = new String(cont.getName());
			name.setText(tempName);
			tempBonus = cont.getBonus();
//			tempColor = new Color(cont.getColor());
			
			updateName();
			updateBonus();
//			updateRGBsliders();
		}
		
		private void newCurrCont() {
			selectCont.getSelection().removeAll(selectCont.getItems());
			
			setCurrCont(new Continent());
		}
		
		private boolean saveToModel() {
			
			if(model.getAvailableContColor() == null) return false;
			
			if(model.getContinents().containsValue(currCont)) {
				HashMap<String, Continent> tempContArr = new HashMap<String, Continent>(model.getContinents());
				tempContArr.remove(currCont.getName(), currCont);
				
				if(dupeName(tempContArr, tempName)) {
					return false;
				}
				
//				updateRGBdisplay();
				
				model.renameCont(currCont, tempName);
				currCont.setBonus(tempBonus);
				currCont.setColor(model.getAvailableContColor());
				
				updateList();
				
				return true;
			} else {
				if(model.getContinents().size() >= maxConts) return false;
			}
			
			if(dupeName(model.getContinents(), tempName)) {
				return false;
			}	
			
//			updateRGBdisplay();
			
			currCont.setName(tempName);
			currCont.setBonus(tempBonus);
			currCont.setColor(model.getAvailableContColor());
			
			model.addContinent(currCont);
			
			updateList();
			
			return true;
		}
		
		private void deleteCont() {
			model.removeContinent(currCont);
			updateList();
			newCurrCont();
		}
	}
}
