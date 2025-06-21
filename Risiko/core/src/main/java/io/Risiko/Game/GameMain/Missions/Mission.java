package io.Risiko.Game.GameMain.Missions;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;
import io.Risiko.Utils.MiscUtils;

abstract public class Mission {
	
	protected GameplayModel model;
	protected Player player;
	
	public Mission(GameplayModel modelIn, Player playerIn) {
		model = modelIn;
		player = playerIn;
	}

	abstract public boolean checkWinCon();
	
	abstract public String getDescription();
}
