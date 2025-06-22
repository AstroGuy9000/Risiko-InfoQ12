package io.Risiko.Game.GameMain.Missions;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;

public class WorldDomination extends Mission {

	public WorldDomination(GameplayModel modelIn, Player playerIn) {
		super(modelIn, playerIn);
	}
	
	@Override
	public boolean checkWinCon() {
		return player.getCountries().containsAll(model.getStrToCountry().values());
	}

	@Override
	public String getDescription() {
		String description = "Conquer the entire World";
		return description;
	}
}
