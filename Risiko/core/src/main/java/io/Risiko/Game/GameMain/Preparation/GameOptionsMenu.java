package io.Risiko.Game.GameMain.Preparation;

import java.util.ArrayList;

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
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Game.GameMap.TravelNetworkSave;
import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Utils.Menu;

public class GameOptionsMenu extends Menu {
	
	private TextButton openMapSelection;
	private Label selectedMapName;
	private TextButton selectGameMode;	// muss noch implementieren
	private TextButton toAddPlayers;	// muss noch implementieren
	private TextButton toMenu;
	
	private TravelNetwork selectedMap;
	private boolean mapIsPlayable;

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
		
		selectedMapName = new Label("Selected Map:\n" + "NONE" + "\n-            -", skin);
		
		// muss noch Auswahl des Spielmodus implementieren
		
		toAddPlayers = new TextButton("next", skin);
		toAddPlayers.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(mapIsPlayable) main.setState(new AddPlayersMenu(main, selectedMap));
			}});	
		
		toMenu = new TextButton("Zum Menu", skin);
		toMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				main.setState(new MainMenu(main));
			}});	
		
		mainTab.add(openMapSelection).padBottom(5);
		mainTab.row();
		mainTab.add(selectedMapName).padBottom(15);
		mainTab.row();
		mainTab.add(toAddPlayers).padBottom(20);
		mainTab.row();
		mainTab.add(toMenu);
		
		mapIsPlayable = false;
	}
	
	private void setSelectedMap(FileHandle file) {
		if(loadModel(file)) {
			selectedMapName.setText("Selected Map:\n" + file.nameWithoutExtension() + "\n-IS  PLAYABLE-");
		} else {
			selectedMapName.setText("Selected Map:\n" + file.nameWithoutExtension() + "\n-NOT PLAYABLE-");
		}
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
