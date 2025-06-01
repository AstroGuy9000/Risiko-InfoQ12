package io.Risiko.Game.GameMain.Preparation;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import io.Risiko.Main;
import io.Risiko.CustomWidgets.TextFieldCust;
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Interfaces.Controller;
import io.Risiko.Utils.Menu;

public class AddPlayersMenu extends Menu {
	
	private TravelNetwork map;
	
	private HorizontalGroup enterName;
	private Label enterNameLabel;
	private TextFieldCust enterNameField;
	
	private Color tempColor;
	private VerticalGroup rgb;
	private Slider red;
	private Slider green;
	private Slider blue;
	
	public AddPlayersMenu(Main mainIn, TravelNetwork mapIn) {
		super(mainIn);
		
		map = mapIn;
		
		enterName = new HorizontalGroup();
		
		enterNameLabel = new Label("Name:", skin);
		
		enterNameField = new TextFieldCust("", skin, null) {
			@Override
			public void thingTyped(char character) {
				// To-Do
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
	}
}
