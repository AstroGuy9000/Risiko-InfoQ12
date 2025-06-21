package io.Risiko.Game.GameMain.GameplayMenus;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;

import io.Risiko.Main;
import io.Risiko.CustomWidgets.ListCust;
import io.Risiko.CustomWidgets.FileMenu.FileMenu;
import io.Risiko.CustomWidgets.FileMenu.FileMenuListener;
import io.Risiko.Game.GameMain.Missions.Mission;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Game.GameMap.TravelNetworkSave;
import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Utils.Menu;

public class GameOptionsMenu extends Menu {
	
	private TextButton openMapSelection;
	private Label selectedMapName;
	private TextButton selectGameMode;	
	private TextButton toAddPlayers;	
	private TextButton toMenu;
	
	private TravelNetwork selectedMap;
	private boolean mapIsPlayable;
	
	private boolean isSecretMission;

	public GameOptionsMenu(Main mainIn) {
		super(mainIn);
		
		openMapSelection = new TextButton("Select a Map", skin);
		openMapSelection.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(popupTab.hasChildren()) return;
				
				FileMenu loadFileMenu = new FileMenu("Load", skin, Gdx.files.local("MapMaker"), "json", main.getBinds());
				
				loadFileMenu.setKeyboardFocus(stageUI);
				
				popupTab.add(loadFileMenu);
				loadFileMenu.addListener(new FileMenuListener() {
					@Override
					public void selectionConfirmed(FileHandle file) {
						setSelectedMap(file);
						popupTab.clear();
					}});
				}});
		
		selectedMapName = new Label("Selected Map:\n" + "NONE" + "\n-              -", skin);
		
		// muss noch Auswahl des Spielmodus implementieren
		
		isSecretMission = false;
		selectGameMode = new TextButton("-   Standard   -", skin);
		selectGameMode.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(isSecretMission) {
					isSecretMission = false;
					selectGameMode.setText("-   Standard   -");
				} else {
					isSecretMission = true;
					selectGameMode.setText("-Secret Mission-");
				}
			}});	
		
		toAddPlayers = new TextButton("next", skin);
		toAddPlayers.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(mapIsPlayable) main.setState(new AddPlayersMenu(main, selectedMap, isSecretMission));
			}});	
		
		toMenu = new TextButton("Main Menu", skin);
		toMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				main.setState(new MainMenu(main));
			}});	
		
		mainTab.add(openMapSelection).padBottom(5);
		mainTab.row();
		mainTab.add(selectedMapName).padBottom(20);
		mainTab.row();
		mainTab.add(selectGameMode).padBottom(15);
		mainTab.row();
		mainTab.add(toAddPlayers).padBottom(20);
		mainTab.row();
		mainTab.add(toMenu);
		
		mapIsPlayable = false;
	}
	
	private void setSelectedMap(FileHandle file) {
		if(loadModel(file)) {
			if(isMapDone()) selectedMapName.setText("Selected Map:\n" + file.nameWithoutExtension() + "\n-IS    FINISHED-");
			else selectedMapName.setText("Selected Map:\n" + file.nameWithoutExtension() + "\n-IS  UNFINISHED-");
			
		} else {
			selectedMapName.setText("Selected Map:\n" + file.nameWithoutExtension() + "\n-NOT   PLAYABLE-");
		}
	}
	
	private boolean isMapDone() {
		
		if(selectedMap.getStrToCountry().values().size() < 30) return false;
		
		Object[] tempCountryArr = selectedMap.getStrToCountry().values().toArray();
		ArrayList<Country> countries = new ArrayList<Country>();
		for(Object i: tempCountryArr) {
			countries.add( (Country) i);
		}
		
		for(HashSet<Country> i: selectedMap.getContMembers().values()) {
			for(Country x: i) {
				countries.remove(x);
			}
		}
		
		if(!countries.isEmpty()) return false;
		
		Country start = (Country) selectedMap.getStrToCountry().values().toArray()[0];
		for(Country i: selectedMap.getStrToCountry().values()) {
			if(!selectedMap.depthSearch(start, i)) return false;
		}
		
		return true;
	}
	
	@Override
	public void keyPressed(int keycode) {
		if(keycode == main.getBinds().BACK) {
			popupTab.clear();
		}
	}
	
	private boolean loadModel(FileHandle file) {
		Json json = new Json();
		
		try {
			TravelNetworkSave loadedData = json.fromJson(TravelNetworkSave.class, file);
			selectedMap = loadedData.rebuildTravelNetwork();
			mapIsPlayable = true;
			return true;
		}catch (Exception e) {
			mapIsPlayable = false;
			return false;
		}
	}
}
