package io.Risiko.Game.GameMain.Missions;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;
import io.Risiko.Game.GameMap.Country;

public class CaptureTwelve extends Mission {

	public CaptureTwelve(GameplayModel modelIn, Player playerIn) {
		super(modelIn, playerIn);
	}
	
	@Override
	public boolean checkWinCon() {
		int counter = 0;
		for(Country i: player.getCountries()) {
			if(i.getTroops() >= 2) counter++;
			if(counter >= 12) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		String description;
		if(model.getStrToCountry().values().size() < 12) description = "This map has less than 12 Countries\n!This mission is impossible!";
		else description = "Conquer and hold at least 12 Countries\nEvery Country has to have 2 troops";
		return description;
	}
}
