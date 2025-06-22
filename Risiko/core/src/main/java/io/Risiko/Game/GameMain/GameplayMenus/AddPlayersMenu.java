package io.Risiko.Game.GameMain.GameplayMenus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.Risiko.Main;
import io.Risiko.CustomWidgets.TextFieldCust;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayController;
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Utils.Controller;
import io.Risiko.Utils.Menu;
import io.Risiko.Utils.MiscUtils;

public class AddPlayersMenu extends Menu {
	
	private TravelNetwork map;
	
	private Label playerNames;
	private HorizontalGroup enterName;
	private Label enterNameLabel;
	private TextFieldCust enterNameField;
	
	private String nameCurrent;
	private ArrayList<Color> availablePlayerColors;
	private ArrayList<Player> players;
	
	private TextButton nextPlayer;
	private TextButton toGame;
	
	private final boolean isSecretMission;
	
	public AddPlayersMenu(Main mainIn, TravelNetwork mapIn, boolean isSecretMissionIn) {
		super(mainIn);
		
		isSecretMission = isSecretMissionIn;
		
		map = mapIn;
		
		playerNames = new Label("", skin);
		
		enterName = new HorizontalGroup();
		
		enterNameLabel = new Label("Name:", skin);
		
		enterNameField = new TextFieldCust("", skin, null) {
			@Override
			public void thingTyped(char character) {
				updateName();
			}
			
			@Override
			public void acceptPressed() {
				nextPlayer();
			}

			@Override
			public void backPressed() {
				stageUI.unfocusAll();
			}
		};
		
		enterName.addActor(enterNameLabel);
		enterName.addActor(enterNameField);
		
		nextPlayer = new TextButton("Next Player", skin);
		nextPlayer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				nextPlayer();
			}
		});
		nextPlayer.setVisible(false);
		
		toGame = new TextButton("Add and play", skin);
		toGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				toGame();
			}
		});
		toGame.setVisible(false);
		
		mainTab.add(playerNames).padBottom(10);
		mainTab.row();
		mainTab.add(enterName).padBottom(15);
		mainTab.row();
		mainTab.add(nextPlayer).padBottom(10);
		mainTab.row();
		mainTab.add(toGame);
		
		availablePlayerColors = MiscUtils.makePlayerColors();
		
		players = new ArrayList<Player>();
	}
	
	private void nextPlayer() {
		if(!canHaveNextPlayer()) {
			toGame();
			return;
		}
		if(!isNameOK()) return;
		
		nextPlayer.setVisible(false);
		toGame.setVisible(false);
		
		players.add(new Player(nameCurrent, getAvailablePlayerColor() ));
		
		String allNames = new String();
		for(Player i: players) {
			allNames = allNames + i.getName() + "\n";
		}
		playerNames.setText(allNames);
		
		enterNameField.setText("");
	}
	
	private boolean canHaveNextPlayer() {
		getAvailablePlayerColor();
		if(availablePlayerColors.size() <= 1) {
			return false;
		}
		if(players.size() > 5) return false;
		return true;
	}
	
	private void toGame() {
		if(!isNameOK()) return;
		
		players.add(new Player(nameCurrent, getAvailablePlayerColor() ));
		
		for(int i = 0; i < players.size()-1; i++) {
			players.get(i).setNextPlayer(players.get(i+1));
		}
		players.get(players.size()-1).setNextPlayer(players.get(0));
		
		main.setState(new GameplayController(main, map, players, isSecretMission));
	}
	
	private void updateName() {
		String nameNew = enterNameField.getText().trim();
		int cursorPos = enterNameField.getCursorPosition();
		
		if(nameNew.length() > 18) {
			enterNameField.setText(nameNew.substring(0, 18));
			
			enterNameField.setCursorPosition(Math.min(cursorPos, enterNameField.getText().length()));
			return;
		}
		
		nameCurrent = nameNew;
		
		if(isNameOK()) {
			if(canHaveNextPlayer()) nextPlayer.setVisible(true);
			if(!players.isEmpty()) toGame.setVisible(true);
		} else {
			nextPlayer.setVisible(false);
			toGame.setVisible(false);
		}
	}
	
	private boolean isNameOK() {
		if(nameCurrent == null) return false;
		if(nameCurrent.length() < 3) return false;
		
		for(Player i: players) {
			if(nameCurrent.equals(i.getName())) return false;
		}
		
		return true;
	}
	
	private Color getAvailablePlayerColor() {
		for(Player i: players) {
			availablePlayerColors.remove(i.getColor());
		}
		
		if(!availablePlayerColors.isEmpty()) return availablePlayerColors.get(0);
		return null;
	}
}
