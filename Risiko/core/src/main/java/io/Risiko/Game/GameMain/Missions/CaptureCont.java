package io.Risiko.Game.GameMain.Missions;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;
import io.Risiko.Game.GameMap.Continent;
import io.Risiko.Utils.MiscUtils;

public class CaptureCont extends Mission{

	private Continent cont;
	
	public CaptureCont(GameplayModel modelIn, Player playerIn) {
		super(modelIn, playerIn);
		if(modelIn.getStrToCont().values().size() <= 0) return;
		cont = (Continent) modelIn.getStrToCont().values().toArray()[MiscUtils.getRandomNumber(0, modelIn.getStrToCont().values().size()-1)];
	}

	@Override
	public boolean checkWinCon() {
		if(cont == null) return false;
		return false;
	}

	@Override
	public String getDescription() {
		String description;
		if(cont == null) description = "This map has no Continents\n!This mission is impossible!";
		else description = "Conquer " + cont.getName();
		return description;
	}
}
