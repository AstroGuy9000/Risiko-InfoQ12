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
import io.Risiko.Game.GameMain.GameplayMenus.GameOptionsMenu;
import io.Risiko.Game.GameMap.CreatorController;
import io.Risiko.Game.Testing.RGBdistanceTestMenu;
import io.Risiko.Utils.Controller;
import io.Risiko.Utils.Menu;

public class MainMenu extends Menu {

	private Table creditsTab;

	private Label title;
	private TextButton startGame;
	private TextButton changeBinds;
	private TextButton makeMap;
	private TextButton quit;
	
	private TextButton testing;

	private Label credits;

	public MainMenu(Main mainIn) {
		super(mainIn);
		
		System.out.println("- - - - -");
		System.out.println("MAIN MENU");
		System.out.println("- - - - -");

		creditsTab = new Table(skin);
		creditsTab.setFillParent(true);
		stageUI.addActor(creditsTab);
		creditsTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		title = new Label("Risk", skin);
		title.setFontScale(3);

		credits = new Label("UI by Raymond \"Raeleus\" Buckley", skin, "optional");
		credits.setFontScale(0.5f);

		startGame = new TextButton("PLAY", skin);
		startGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new GameOptionsMenu(main));
			}});

		changeBinds = new TextButton("Settings", skin);
		changeBinds.addListener(new ChangeListener() {	
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new BindsMenu(main));
			}});

		makeMap =  new TextButton("Map Designer", skin);
		makeMap.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new CreatorController(main));
			}});

		quit =  new TextButton("Exit to Desktop", skin);
		quit.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				stageUI.clear();
				System.exit(0);
			}});
		
//		testing =  new TextButton("Test-Zone", skin);
//		testing.addListener(new ChangeListener() {
//			public void changed (ChangeEvent event, Actor actor) {
//				stageUI.clear();
//				main.setState(new RGBdistanceTestMenu(main));
//			}});

		mainTab.center().add(title).padBottom(60);
		mainTab.row();
		mainTab.add(startGame).padBottom(10);
		mainTab.row();
		mainTab.add(changeBinds).padBottom(40);
		mainTab.row();
		mainTab.add(makeMap).padBottom(70);
		mainTab.row();
		mainTab.add(quit).padBottom(100);
//		mainTab.row();
//		mainTab.add(testing);

		creditsTab.pad(25).bottom().left().add(credits);
	}
}
