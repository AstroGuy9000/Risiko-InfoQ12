package io.Risiko.Utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.Risiko.Main;

/**
 * Class die dazu dient schneller textbasierte Menüs zu implementieren
 */
public abstract class Menu implements Controller {

	protected Main main;
	protected Stage stageUI;
	protected Table mainTab;
	protected Table popupTab;
	protected Skin skin;
	
	public Menu(Main mainIn) {
		main = mainIn;
		
		stageUI = main.getStageUI();
		stageUI.clear();
		
		skin = main.getSkin();
		
		mainTab = new Table(skin);
		mainTab.setFillParent(true);
		stageUI.addActor(mainTab);
		mainTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainTab.setBackground("window");
		
		popupTab = new Table(skin);
		popupTab.setFillParent(true);
		stageUI.addActor(popupTab);
		popupTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void drawScreen() {
		stageUI.draw();
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void doTick(ArrayList<Integer> keyInputs, ArrayList<Integer> buttonInputs) {
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
