package io.Risiko.Game.GameMain.Missions;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;

public class CaptureTwentyfour extends Mission {

	public CaptureTwentyfour(GameplayModel modelIn, Player playerIn) {
		super(modelIn, playerIn);
	}

	@Override
	public boolean checkWinCon() {
		if(player.getCountries().size() >= 24) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		String description;
		if(model.getStrToCountry().values().size() < 24) description = "This map has less than 24 Countries\n!This mission is impossible!";
		else description = "Conquer and hold at least 24 Countries";
		return description;
	}
}
