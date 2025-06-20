package io.Risiko.Game.GameMain;

import io.Risiko.Game.GameMain.Gameplay.GameplayModel;

public class WordlDomination implements Mission {

	private GameplayModel model;
	private Player player;
	
	public WordlDomination(GameplayModel modelIn, Player playerIn) {
		model = modelIn;
		player = playerIn;
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
