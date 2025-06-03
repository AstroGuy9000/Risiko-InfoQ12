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
import io.Risiko.Utils.Utils;

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
				String textClean = cleanString(r.getText());
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
				String textClean = cleanString(g.getText());
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
				String textClean = cleanString(b.getText());
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
				String textClean = cleanString(distance.getText());
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
		
		colorPoint = Utils.rgba(rInt, gInt, bInt, 1);
		
		Pixmap colorMapPoint = new Pixmap( 100, 100, Format.RGBA8888 );
		colorMapPoint.setColor(colorPoint);
		colorMapPoint.fillRectangle(0, 0, 100, 100);
		
		colorPointTex = new Texture(colorMapPoint);
		
		colorDistance = calcColorDistance(colorPoint, distanceInt);
		
		Pixmap colorMapDistance = new Pixmap( 100, 100, Format.RGBA8888 );
		colorMapDistance.setColor(colorDistance);
		colorMapDistance.fillRectangle(0, 0, 100, 100);
		
		colorDistanceTex = new Texture(colorMapDistance);
		
		popupTab.clear();
		popupTab.left().add(new Image(colorPointTex)).padBottom(200);
		popupTab.row();
		popupTab.add(new Image(colorDistanceTex));
	}
	
	private Color calcColorDistance(Color point, int dist) {
		if(dist == 0) return point;
		
		Vector3 colorPointVector = new Vector3(point.r*255, point.g*255, point.b*255);
		
		float pointR = point.r*255;
		float pointG = point.g*255;
		float pointB = point.b*255;
		
		float a = 1 + ( Utils.square(pointG/pointR) ) + ( Utils.square(pointB/pointR) );
		float b = -2*(pointR + ( (Utils.square(pointG))/pointR ) + ( (Utils.square(pointB))/pointR ));
		float c = Utils.square(pointR) + Utils.square(pointG) + Utils.square(pointB) - Utils.square( (float) (Math.sqrt(Utils.square(pointR) + Utils.square(pointG) + Utils.square(pointB)) + dist));
		
		float calc = (float) ( (-1*b + Math.sqrt( b*b - (4 * a * c) )) / (2 * a) );
		
		int calcR = (int) (calc - pointR);
		int calcG = (int) (( calc*(pointG/pointR) ) - pointG);
		int calcB = (int) (( calc*(pointB/pointR) ) - pointB);
		
		Color calcColor = Utils.rgba(calcR, calcG, calcB, 1);
		
		System.out.println(dist);
		System.out.println("cR" + calcR);
		System.out.println("cG" + calcG);
		System.out.println("cB" + calcB);
		System.out.println("--");
		System.out.println("pR" + pointR);
		System.out.println("pG" + pointG);
		System.out.println("pB" + pointB);
		System.out.println("-|-|-|-|-");
		
		return calcColor;
	}
	
	private String cleanString(String text) {
		return text.replaceAll("[^0-9.]", "");
	}
}
