package io.Risiko.Game.Testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.Risiko.Main;
import io.Risiko.CustomWidgets.TextFieldCust;
import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Utils.Menu;
import io.Risiko.Utils.MiscUtils;

public class RGBdistanceTestMenu extends Menu {

	private TextFieldCust r;
	private int rInt;
	
	private TextFieldCust g;
	private int gInt;
	
	private TextFieldCust b;
	private int bInt;
	
	private TextButton distanceNegative;
	private boolean distIsNegative;
	private TextFieldCust distance;
	private int distanceInt;
	
	private TextButton toMenu;
	
	private Color colorPoint;
	private Texture colorPointTex;
	
	private Color colorDistance;
	private Texture colorDistanceTex;
	
	public RGBdistanceTestMenu(Main mainIn) {
		super(mainIn);
		
		rInt = 0;
		gInt = 0;
		bInt = 0;
		distanceInt = 0;
		
		Pixmap pixmapTemp = new Pixmap( 1, 1, Format.RGBA8888 );
		pixmapTemp.setColor(Color.BLACK);
		pixmapTemp.drawPixel(0, 0);
		
		colorPoint = new Color();
		colorPointTex = new Texture(pixmapTemp);
		
		colorDistance = new Color();
		colorDistanceTex = new Texture(pixmapTemp);
		
		r = new TextFieldCust("", skin, main.getBinds()) {

			@Override
			public void thingTyped(char character) {
				String textClean = MiscUtils.onlyNumsInString(r.getText());
				if(textClean.isBlank()) return;
				rInt = Integer.valueOf(textClean);
				r.setText(textClean);
				updateColor();
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
		
		g = new TextFieldCust("", skin, main.getBinds()) {

			@Override
			public void thingTyped(char character) {
				String textClean = MiscUtils.onlyNumsInString(g.getText());
				if(textClean.isBlank()) return;
				gInt = Integer.valueOf(textClean);
				g.setText(textClean);
				updateColor();
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
		
		b = new TextFieldCust("", skin, main.getBinds()) {

			@Override
			public void thingTyped(char character) {
				String textClean = MiscUtils.onlyNumsInString(b.getText());
				if(textClean.isBlank()) return;
				bInt = Integer.valueOf(textClean);
				b.setText(textClean);
				updateColor();
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
		
		distance = new TextFieldCust("", skin, main.getBinds()) {

			@Override
			public void thingTyped(char character) {
				String textClean = MiscUtils.onlyNumsInString(distance.getText());
				if(textClean.isBlank()) return;
				distanceInt = Integer.valueOf(textClean);
				distance.setText(textClean);
				updateColor();
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
		
		distIsNegative = false;
		
		distanceNegative = new TextButton("Distance is positive", skin);
		distanceNegative.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(distIsNegative) {
					distIsNegative = false;
					distanceNegative.setText("Distance is positive");
					updateColor();
				} else {
					distIsNegative = true;
					distanceNegative.setText("Distance is negative");
					updateColor();
				}
			}});
		
		toMenu = new TextButton("Menu", skin);
		toMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new MainMenu(main));
			}});
		
		mainTab.add(r);
		mainTab.row();
		mainTab.add(g);
		mainTab.row();
		mainTab.add(b).padBottom(70);
		mainTab.row();
		mainTab.add(distance);
		mainTab.row();
		mainTab.add(distanceNegative).padBottom(100);
		mainTab.row();
		mainTab.add(toMenu);
	}
	
	private void updateColor() {
		
		if(distIsNegative) {
			if(distanceInt > 0) distanceInt = -distanceInt;
		} else {
			if(distanceInt < 0) distanceInt = -distanceInt;
		}
		
		colorPoint = MiscUtils.rgba(rInt, gInt, bInt, 1);
		
		Pixmap colorMapPoint = new Pixmap( 100, 100, Format.RGBA8888 );
		colorMapPoint.setColor(colorPoint);
		colorMapPoint.fillRectangle(0, 0, 100, 100);
		
		colorPointTex = new Texture(colorMapPoint);
		
		colorDistance = MiscUtils.calcColorDistance(colorPoint, distanceInt);
		
		Pixmap colorMapDistance = new Pixmap( 100, 100, Format.RGBA8888 );
		colorMapDistance.setColor(colorDistance);
		colorMapDistance.fillRectangle(0, 0, 100, 100);
		
		colorDistanceTex = new Texture(colorMapDistance);
		
		popupTab.clear();
		popupTab.left().add(new Image(colorPointTex)).padBottom(200);
		popupTab.row();
		popupTab.add(new Image(colorDistanceTex));
	}
}
