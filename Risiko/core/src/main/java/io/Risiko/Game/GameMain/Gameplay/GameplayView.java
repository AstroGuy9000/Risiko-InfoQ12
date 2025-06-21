package io.Risiko.Game.GameMain.Gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.Risiko.Main;
import io.Risiko.CustomWidgets.ListCust;
import io.Risiko.CustomWidgets.TextFieldCust;
import io.Risiko.Game.GameMain.Card;
import io.Risiko.Game.GameMain.Card.Card_Type;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMap.Continent;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Game.GameMap.CreatorController;
import io.Risiko.Game.GameMap.GenManager;
import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel.FortifyPhase;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel.GameStartup;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel.Phase;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel.Phase_Type;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel.SetupPhase;
import io.Risiko.Game.GameMain.GameplayMenus.WinScreen;
import io.Risiko.Utils.RiskListener;
import io.Risiko.Utils.MiscUtils;

public class GameplayView {
	
	private GameplayController controller;
	private GameplayModel model;
	private Main main;
	
	// UI-Zeug
	private Stage stageUI;
	private Skin skin;
	private Table permUItab;
	private Table mainTab;
	private Table popupTab;
	
	private Window escMenu;
	private VerticalGroup escMenuGroup;
	private TextButton toMenu;
	
	private BitmapFont font;
	
	private Window turnInfo;
	private Label activePlayer;
	private Label turnPhase;
	
	// Map-Grafik
	private ExtendViewport viewport;
	private Stage stageMap;
	private OrthographicCamera cam;
	
	//Misc bzw. unschönes Zeug
	int actionInt;
	TextFieldCust enterNum;
	
	public GameplayView(GameplayController controllerIn) {
		controller = controllerIn;
		model =  controller.getModel();
		main = controller.getMain();
		
		// Spiel Grafik
		stageUI = main.getStageUI();
		stageUI.clear();
		
		viewport = MiscUtils.makeDefaultViewport();
		stageMap = new Stage(viewport);
		cam = (OrthographicCamera) viewport.getCamera();
		
		skin = main.getSkin();
		font = skin.getFont("commodore-64");
		
		permUItab = new Table(skin);
		permUItab.setFillParent(true);
		stageUI.addActor(permUItab);
		permUItab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		permUItab.center();
		
		mainTab = new Table(skin);
		mainTab.setFillParent(true);
		stageUI.addActor(mainTab);
		mainTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainTab.center();
		
		popupTab = new Table(skin);
		popupTab.setFillParent(true);
		stageUI.addActor(popupTab);
		popupTab.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		popupTab.center();
		
		turnInfo = new Window("", skin);
		turnInfo.setFillParent(false);
		
		activePlayer = new Label(model.getTurnOwner().getName(), skin);
		turnInfo.add(activePlayer);
		
		model.addTurnListener(new RiskListener() {
			@Override
			public void thingHappened() {
				
				if(model.getTurnOwner().getMission().checkWinCon()) {
					stageUI.clear();
					main.setState(new WinScreen(main, model.getTurnOwner()));
					return;
				}
				
				turnInfo.clear();
				
				activePlayer.setText(model.getTurnOwner().getName());
				turnInfo.add(activePlayer);
				
				int sWidth = Gdx.graphics.getWidth();
				int sHeight = Gdx.graphics.getHeight();
				Pixmap tempPixmap = new Pixmap(sWidth/10, sHeight/30, Format.RGBA4444);
				tempPixmap.setColor(model.getTurnOwner().getColor());
				tempPixmap.fill();
				Texture tempTexture = new Texture(tempPixmap);
				Image playerColorImg = new Image(tempTexture);
				turnInfo.add(playerColorImg);
				
				turnInfo.row();
				turnInfo.add(turnPhase);
				
				switch(model.getTurnPhase()) {
				
				default:
					turnPhase.setText("Phase: Error  ");
					break;
					
				case Phase_Type.GAME_STARTUP:
					turnPhase.setText("Phase: Startup");
					//if(model.getPhaseObject().getClass() != GameStartup.class) break;
					GameStartup phaseTemp1 = (GameStartup) model.getPhaseObject();
					int troopsTemp1 = phaseTemp1.getToSpendTroops();
					turnInfo.add(new Label("Troops: " + Integer.toString(troopsTemp1), skin));
					break;
					
				case Phase_Type.SETUP:
					turnPhase.setText("Phase: Setup  ");
					//if(model.getPhaseObject().getClass() != GameStartup.class) break;
					SetupPhase phaseTemp2 = (SetupPhase) model.getPhaseObject();
					int troopsTemp2 = phaseTemp2.getToSpendTroops();
					turnInfo.add(new Label("Troops: " + Integer.toString(troopsTemp2), skin));
					break;
					
				case Phase_Type.ATTACK:
					turnPhase.setText("Phase: Attack ");
					break;
					
				case Phase_Type.FORTIFY:
					turnPhase.setText("Phase: Fortify");
					break;
				
				case Phase_Type.MISC:
					turnPhase.setText("Phase: Misc   ");
					break;
				}
					
			}
		});
		
		int sWidth = Gdx.graphics.getWidth();
		int sHeight = Gdx.graphics.getHeight();
		Pixmap tempPixmap = new Pixmap(sWidth/10, sHeight/30, Format.RGBA4444);
		tempPixmap.setColor(model.getTurnOwner().getColor());
		tempPixmap.fill();
		Texture tempTexture = new Texture(tempPixmap);
		Image playerColorImg = new Image(tempTexture);
		turnInfo.add(playerColorImg);
		turnInfo.row();
		
		turnPhase = new Label("Phase: Startup", skin);
		turnInfo.add(turnPhase);
		
		GameStartup phaseTemp1 = (GameStartup) model.getPhaseObject();
		int troopsTemp1 = phaseTemp1.getToSpendTroops();
		turnInfo.add(new Label("Troops: " + Integer.toString(troopsTemp1), skin));
		
		permUItab.left().top();
		
		permUItab.add(turnInfo);
	}
	
	public void render() {
		
		ScreenUtils.clear(MiscUtils.rgba(190, 190, 190, 1));
		
		ShapeRenderer shRend = main.getShRend();
		
		cam.update();
		
		shRend.setProjectionMatrix(cam.combined);
		
		shRend.begin(ShapeType.Filled);
		
		for(float[] i: model.getDecoLines()) {
			MiscUtils.drawRoundedLine(shRend, i, 3.5f, Color.DARK_GRAY);
		}
		
		for(String i: model.getStrToCont().keySet()) {
			Color contColor = model.getStrToCont().get(i).getColor();
			for(Country x: model.getContMembers().get(i)) {
				MiscUtils.drawPolygonOutline(shRend, x.getPolyFull(), 4, contColor);
			}
		}
		
		for(Player i: model.getPlayersCopy()) {
			Color playerColor = i.getColor();
			for(Country x: i.getCountries()) {
				MiscUtils.drawPolygonFilled(shRend, x.getPolyFull(), playerColor);
			}
		}
		
		for(Country i: model.getSelection()) {
			MiscUtils.drawPolygonFilled(shRend, i.getPolyFull(), Color.GOLDENROD);
		}
		
		shRend.end();
		
		SpriteBatch batch = main.getBatch();
				
		batch.setProjectionMatrix(cam.combined);
		
		batch.begin();
		
		for(Country i: model.getStrToCountry().values()) {
			Vector2 centroid = i.getPolyFull().getPolygon().getCentroid(new Vector2());
			Label temp = new Label(Integer.toString(i.getTroops()), skin);
			float offsetX = temp.getWidth()/2f;
			float offsetY = temp.getHeight()/2f;
			font.draw(batch, Integer.toString(i.getTroops()), centroid.x-offsetX, centroid.y+offsetY);
		}
		
		batch.end();
		
		stageUI.draw();
	}
	
	protected OrthographicCamera getCam() {
		return cam;
	}
	
	public void closePopupMenus() {
		popupTab.clear();;
		controller.resumeInput();
	}
	
	public void openCardMenu() {
		
		controller.suspendInput();
		
		Window cardWindow = new Window("", skin, "dialog");
		cardWindow.setFillParent(false);
		cardWindow.setMovable(false);
		
		if(!model.getTurnOwner().getDeck().isEmpty()) {
		
			HashMap<String, Card> strToCard = new HashMap<String, Card>();
		
			for(Card i: model.getTurnOwner().getDeck()) {
				String cardName = new String();
			
				switch(i.getCardType()) {
			
				default:
					throw(new IllegalArgumentException("Card has no valid Card_Type"));
				
				case Card_Type.Infantry:
					cardName = "Infantry  | " + i.getCountry().getName() + "  ";
					break;
				
				case Card_Type.Cavalry:
					cardName = "Cavalry   | " + i.getCountry().getName()+ "  ";
					break;
				
				case Card_Type.Artillery:
					cardName = "Artillery | " + i.getCountry().getName()+ "  ";
					break;
				
				case Card_Type.Joker:
					cardName = "Joker     |   ";
					break;
				}
				
				strToCard.put(cardName, i);
			}
			
			//HorizontalGroup horGroup = new HorizontalGroup();
			VerticalGroup selectedCardsGroup = new VerticalGroup();
			
			ListCust<String> selectedCardsList = new ListCust<String>(skin, main.getBinds());
			selectedCardsList.setItems(new String[0]);
			selectedCardsList.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					selectedCardsList.setSelectedIndex(-1);
				}});
			TextButton tradeCards = new TextButton("Trade Cards", skin);
			tradeCards.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(model.getPhaseObject().getClass() != SetupPhase.class) return;
					SetupPhase phase = (SetupPhase) model.getPhaseObject();
					
					ArrayList<Card> cards = new ArrayList<Card>();
					
					for(Object i: selectedCardsList.getItems()) {
						cards.add(strToCard.get( (String) i) );
					}
					
					phase.exchangeCards(cards);
					closePopupMenus();
				}});
			
			selectedCardsGroup.addActor(new Label("Selected Cards:", skin));
			selectedCardsGroup.addActor(selectedCardsList);
			selectedCardsGroup.addActor(tradeCards);
		
			ListCust<String> playerCards = new ListCust<String>(skin, main.getBinds()) {
				@Override
				public boolean acceptPressed() {
					if(getSelected() == null) {
						return false;
					}
					
					ArrayList<String> tempList = new ArrayList<String>();
					for(String i: selectedCardsList.getItems()) {
						tempList.add(i);
					}
					
					if(tempList.contains(getSelected())) tempList.remove(getSelected());
					else tempList.add(getSelected());
					
					String[] tempArr = new String[tempList.size()];
					for(int i = 0; i < tempArr.length; i++) {
						tempArr[i] = tempList.get(i);
					}
					
					selectedCardsList.setItems(tempArr);
					
					return true;
				}};
				
			selectedCardsList.setWidth(playerCards.getWidth());
				
			Object[] badArr = strToCard.keySet().toArray();
			String[] tempStrArr = new String[badArr.length];
 			for(int i = 0; i < tempStrArr.length; i++) {
				tempStrArr[i] = (String) badArr[i];
			}
				
			playerCards.setItems(tempStrArr);
			playerCards.setTypeToSelect(true);
		
			cardWindow.add(playerCards);
			cardWindow.padLeft(20);
			cardWindow.add(selectedCardsGroup);
			
			stageUI.setKeyboardFocus(playerCards);
		} else {
			cardWindow.add(new Label("No Cards", skin));
		}
	
		popupTab.clear();
		popupTab.add(cardWindow);
	}
	
	public void openPauseMenu() {
		
		controller.suspendInput();
		
		Window pauseMenu = new Window("", skin, "dialog");
		pauseMenu.setMovable(false);
		toMenu = new TextButton("Main Menu", skin);
		toMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new MainMenu(main));
			}});
		Label mission = new Label(model.getTurnOwner().getMission().getDescription(), skin);
		
		pauseMenu.add(toMenu).padBottom(20);
		pauseMenu.row();
		pauseMenu.add(new Label("Your mission is:", skin));
		pauseMenu.row();
		pauseMenu.add(mission);
		
		popupTab.clear();
		popupTab.add(pauseMenu);
	}
	
	public void openContLegend() {
		
		controller.suspendInput();
		
		Window legend = new Window("", skin, "dialog");
		legend.setMovable(false);
		
		int sWidth = Gdx.graphics.getWidth();
		int sHeight = Gdx.graphics.getHeight();
		
		int maxLength = 0;
		for(Continent i: model.getStrToCont().values()) {
			if(i.getName().length() > maxLength) maxLength = i.getName().length();
		}
		
		for(Continent i: model.getStrToCont().values()) {
			
			HorizontalGroup group = new HorizontalGroup();
			
			String contName = MiscUtils.spacingString(maxLength - i.getName().length()) + i.getName() + " ";
			group.addActor(new Label(contName, skin));
			
			Pixmap tempPixmap = new Pixmap(sWidth/10, sHeight/30, Format.RGBA4444);
			tempPixmap.setColor(i.getColor());
			tempPixmap.fill();
			Texture tempTexture = new Texture(tempPixmap);
			Image contColorImg = new Image(tempTexture);
			group.addActor(contColorImg);
			
			legend.add(group);
			legend.row();
		}
		
		popupTab.clear();
		
		popupTab.add(legend);
	}
	
	public void requestNumberForAction() {
		
		controller.suspendInput();
		
		actionInt = 0;
		
		Window numberDialog = new Window("", skin, "dialog");
		enterNum = new TextFieldCust("", skin, main.getBinds()) {

			@Override
			public void thingTyped(char character) {
				String textClean = MiscUtils.onlyNumsInString(enterNum.getText());
				if(textClean.isBlank()) return;
				actionInt = Integer.valueOf(textClean);
				enterNum.setText(textClean);
			}

			@Override
			public void acceptPressed() {
				stageUI.unfocusAll();
				popupTab.clear();
				if(actionInt > 0) model.doAction(actionInt);
				controller.resumeInput();
			}

			@Override
			public void backPressed() {
				stageUI.unfocusAll();
				popupTab.clear();
				controller.resumeInput();
			}
			
		};
		
		numberDialog.add(enterNum);
		
		popupTab.clear();
		popupTab.add(numberDialog);
		
		stageUI.setKeyboardFocus(enterNum);
	}
}
