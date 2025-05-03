package io.Risiko.Game.Menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.utils.ScreenUtils;

import io.Risiko.Main;
import io.Risiko.Game.GameMap.CreatorController;
import io.Risiko.Interfaces.Controller;

public class MainMenu implements Controller {

	private Main main;
	private Stage stageUI;
	private Table mainTab;
	private Table creditsTab;
	private Skin skin;

	//private Color backgroundColor;

	private Label title;
	private TextButton startGame;
	private TextButton changeBinds;
	private TextButton makeMap;
	private TextButton quit;

	private Label credits;

	public MainMenu(Main mainIn) {
		main = mainIn;
		
		stageUI = main.getStageUI();
		stageUI.clear();
		
		skin = main.getSkin();

		mainTab = new Table(skin);
		mainTab.setFillParent(true);
		stageUI.addActor(mainTab);
		mainTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainTab.setBackground("window");

		creditsTab = new Table(skin);
		creditsTab.setFillParent(true);
		stageUI.addActor(creditsTab);
		creditsTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//backgroundColor = ColorsC64.BLUE;

		title = new Label("Risiko", skin);
		title.setFontScale(3);

		credits = new Label("UI von Raymond \"Raeleus\" Buckley", skin, "optional");
		credits.setFontScale(0.5f);

		startGame = new TextButton("Spiel Starten", skin);
		startGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Changed!");
			}});

		changeBinds = new TextButton("Einstellungen", skin);
		changeBinds.addListener(new ChangeListener() {	
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new BindsMenu(main));
			}});

		makeMap =  new TextButton("Karte erstellen", skin);
		makeMap.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new CreatorController(main));
			}});

		quit =  new TextButton("Zum Desktop", skin);
		quit.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				System.exit(0);
			}});

		mainTab.center().add(title).padBottom(60);
		mainTab.row();
		mainTab.add(startGame).padBottom(10);
		mainTab.row();
		mainTab.add(changeBinds).padBottom(40);
		mainTab.row();
		mainTab.add(makeMap).padBottom(70);
		mainTab.row();
		mainTab.add(quit);

		creditsTab.pad(25).bottom().left().add(credits);
	}

	@Override
	public void drawScreen() {
		//ScreenUtils.clear(backgroundColor);
		stageUI.draw();
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void doTick(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
		// TODO Auto-generated method stub
		stageUI.act();
	}

	@Override
	public void keyPressed(int keycode) {
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
}
