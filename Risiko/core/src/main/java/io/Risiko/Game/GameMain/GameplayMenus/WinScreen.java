package io.Risiko.Game.GameMain.GameplayMenus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.Risiko.Main;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;
import io.Risiko.Game.Menus.MainMenu;
import io.Risiko.Utils.Menu;

public class WinScreen extends Menu {
	
	private Player winner;
	
	private Label winnerLabel;
	private Label genericText;
	private Label missionLabel;
	private TextButton toMenu;

	public WinScreen(Main mainIn, Player winnerIn) {
		super(mainIn);
		
		winner = winnerIn;
		
		winnerLabel = new Label(winner.getName() + " has won!", skin);
		genericText = new Label("Their Mission was:" ,skin);
		missionLabel = new Label(winner.getMission().getDescription(), skin);
		
		toMenu = new TextButton("Main Menu", skin);
		toMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stageUI.clear();
				main.setState(new MainMenu(main));
			}});
		
		mainTab.add(winnerLabel).padBottom(15);
		mainTab.row();
		mainTab.add(genericText);
		mainTab.row();
		mainTab.add(missionLabel).padBottom(30);
		mainTab.row();
		mainTab.add(toMenu);
	}
}
