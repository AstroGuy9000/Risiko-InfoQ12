package io.Risiko.Game.GameMain.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.Risiko.Main;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Game.GameMap.CreatorController;
import io.Risiko.Game.GameMap.GenManager;
import io.Risiko.Utils.Utils;

public class GameplayView {
	
	private GameplayController controller;
	private GameplayModel model;
	private Main main;
	
	// UI-Zeug
	private Stage stageUI;
	private Skin skin;
	private Table mainTab;
	private Table popupTab;
	
	private Window escMenu;
	private VerticalGroup escMenuGroup;
	private TextButton toMenu;
	
	private BitmapFont font;
	
	// Map-Grafik
	private ExtendViewport viewport;
	private Stage stageMap;
	private OrthographicCamera cam;
	
	public GameplayView(GameplayController controllerIn) {
		controller = controllerIn;
		model =  controller.getModel();
		main = controller.getMain();
		
		// Spiel Grafik
		stageUI = main.getStageUI();
		stageUI.clear();
		
		viewport = Utils.makeDefaultViewport();
		stageMap = new Stage(viewport);
		cam = (OrthographicCamera) viewport.getCamera();
		
		skin = main.getSkin();
		font = skin.getFont("commodore-64");
		
		mainTab = new Table(skin);
		mainTab.setFillParent(true);
		stageUI.addActor(mainTab);
		mainTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainTab.center();
	}
	
	public void render() {
		
		ScreenUtils.clear(Utils.rgba(154, 157, 161, 1));
		
		ShapeRenderer shRend = main.getShRend();
		
		cam.update();
		
		shRend.setProjectionMatrix(cam.combined);
		
		shRend.begin(ShapeType.Filled);
		
		for(float[] i: model.getDecoLines()) {
			Utils.drawRoundedLine(shRend, i, 3.5f, Color.DARK_GRAY);
		}
		
		for(String i: model.getStrToCont().keySet()) {
			Color contColor = model.getStrToCont().get(i).getColor();
			for(Country x: model.getContMembers().get(i)) {
				Utils.drawPolygonOutline(shRend, x.getPolyFull(), 4, contColor);
			}
		}
		
		for(Player i: model.getPlayers()) {
			Color playerColor = i.getColor();
			for(Country x: i.getCountries()) {
				Utils.drawPolygonFilled(shRend, x.getPolyFull(), playerColor);
			}
		}
		
		shRend.end();
	}
	
	protected OrthographicCamera getCam() {
		return cam;
	}
}
