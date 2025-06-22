package io.Risiko.Game.GameMain.Missions;

import java.util.ArrayList;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Gameplay.GameplayModel;
import io.Risiko.Utils.MiscUtils;

public class Assassination extends Mission {
	
	private Player target;

	public Assassination(GameplayModel modelIn, Player playerIn) {
		super(modelIn, playerIn);
		ArrayList<Player> temp = model.getPlayersCopy();
		temp.remove(player);
		target = temp.get(MiscUtils.getRandomNumber(0, temp.size()-1));
	}

	@Override
	public boolean checkWinCon() {
		if(target.getCountries().isEmpty()) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		String description = "Eliminate the player " + target.getName();
		return description;
	}
}
