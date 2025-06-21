package io.Risiko.Game.Menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.Risiko.KeyBinds;
import io.Risiko.Main;
import io.Risiko.CustomWidgets.ListCust;
import io.Risiko.Utils.Controller;
import io.Risiko.Utils.Menu;

public class BindsMenu extends Menu {
	
	private Label title;
	private ListCust<String> nameList;
	private ListCust<String> bindsList;
	private HorizontalGroup listGroup;
	private TextButton saveChanges;
	private TextButton toDefault;
	private TextButton toMenu;
	
	private Window notif;

	private int[] bindsArr;
	private String[] itemsArr;
	private final int unassigned = 9999;
	private int unassignedCounter;
	
	private boolean isListening;
	private int whoIsListening;
	
	public BindsMenu(Main mainIn) {
		super(mainIn);
		
		unassignedCounter = 0;
		
		title = new Label("Settings", skin);
		title.setFontScale(2);
		
		nameList = new ListCust<String>(skin, "dimmed", main.getBinds());
		nameList.setTypeToSelect(false);
		String[] nameListItems = new String[KeyBinds.bindsKeysArr.length];
		String x;
		for(int i = 0; i < nameListItems.length; i++) {
			x = " " + KeyBinds.bindsKeysArr[i];
			while(x.length() < 9) {
				x += " ";
			}
			nameListItems[i] = x;
		}
		nameList.setItems(nameListItems);
		nameList.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				bindsList.setSelectedIndex(
					nameList.getSelectedIndex());
			}});
		
		List<String> fillerList = new List<String>(skin, "dimmed");
		fillerList.setTypeToSelect(false);
		String[] fillerChar = new String[KeyBinds.bindsKeysArr.length];
		for(int i = 0; i < fillerChar.length; i++) {
			fillerChar[i] = ":";
		}
		fillerList.setItems(fillerChar);
		
		bindsArr = main.getBinds().currentBindsArr();
		itemsArr = new String[bindsArr.length];
		bindsList = new ListCust<String>(skin, "dimmed", main.getBinds());
		bindsList.setTypeToSelect(false);
		updateListContent();
		bindsList.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				nameList.setSelectedIndex(
					bindsList.getSelectedIndex());
				isListening = false;
				//stageUI.unfocusAll();
			}});
		bindsList.setSelectedIndex(0);
		
		listGroup = new HorizontalGroup();
		listGroup.center().top();
		listGroup.addActor(nameList);
		listGroup.addActor(fillerList);
		listGroup.addActor(bindsList);
		
		stageUI.setKeyboardFocus(bindsList);
		
		saveChanges = new TextButton("Save Settings", skin);
		saveChanges.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if(checkBinds()) {
					main.setBinds(new KeyBinds(bindsArr));
					nameList.setBinds(main.getBinds());
					bindsList.setBinds(main.getBinds());
				} else {
					return;
				}
				bindsArr = main.getBinds().currentBindsArr();
				updateListContent();
			}});
		
		toDefault = new TextButton("Default", skin);
		toDefault.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				bindsArr = KeyBinds.defaultBindsArr();
				updateListContent();
			}});
		
		toMenu = new TextButton("Main Menu", skin);
		toMenu.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new MainMenu(main));
			}});
		
		mainTab.center().add(title).padBottom(30);
		mainTab.row();
		mainTab.add(listGroup).padBottom(10).expandX();
		mainTab.row();
		mainTab.add(saveChanges).padBottom(10);
		mainTab.row();
		mainTab.add(toDefault).padBottom(20);
		mainTab.row();
		mainTab.add(toMenu);
		
		notif = new Window("", skin, "dialog");
		notif.add(new Label("Waiting for Input", skin, "optional")).pad(20).padTop(10);
		
		//stageUI.setDebugAll(true);
	}
	
	@Override
	public void keyPressed(int keycode) {
		if(isListening) {
			if(keycode == Keys.BACKSPACE) {
				if(whoIsListening == 4) {	// bindsKeysArr[4] --> Accept
					main.addKeyInput(keycode);
					setListening(false);
					popupTab.clear();
					return;
				}
				bindsArr[whoIsListening] = unassigned + unassignedCounter;
				unassignedCounter++;
				updateListContent();
				main.addKeyInput(keycode);
				setListening(false);
				return;
			}
			for(int i: bindsArr) {
				if(i == keycode) {
					main.addKeyInput(keycode);
					setListening(false);
					return;
				}
			}
			bindsArr[whoIsListening] = keycode;
			updateListContent();
			setListening(false);
			popupTab.clear();
		}
		
		if(keycode == main.getBinds().ACCEPT) {
			setListening(true);
			whoIsListening = bindsList.getSelectedIndex();
		}
		main.addKeyInput(keycode);
	}
	
	private void updateListContent() {
		for(int i = 0; i < bindsArr.length; i++) {
			if(bindsArr[i] >= unassigned) {
				itemsArr[i] = " NICHTS " + (bindsArr[i] - unassigned);
				while(10 > itemsArr[i].length()) {
					itemsArr[i] += " ";
				}
			} else {
				itemsArr[i] = " [" + Keys.toString(bindsArr[i]) + "] ";
				while(10 > itemsArr[i].length()) {
					itemsArr[i] += " ";
				}
			}
		}
		int i = bindsList.getSelectedIndex();
		bindsList.setItems(itemsArr);
		bindsList.setSelectedIndex(i);
	}
	
	private boolean checkBinds() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i: bindsArr) {
			if(i >= unassigned || temp.contains(i)) {
				return false;
			}
			temp.add(i);
		}
		return true;
	}
	
	private void setListening(boolean bool) {
		isListening = bool;
		if(bool) {
			stageUI.unfocusAll();
			popupTab.reset();
			popupTab.add(notif);
		} else {
			stageUI.setKeyboardFocus(bindsList);
			popupTab.reset();
		}
	}
}
