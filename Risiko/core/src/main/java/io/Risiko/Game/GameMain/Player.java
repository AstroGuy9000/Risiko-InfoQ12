package io.Risiko.Game.GameMain;

import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;

import io.Risiko.Game.GameMain.Gameplay.GameplayModel.PlayerDeck;
import io.Risiko.Game.GameMap.Country;

public class Player {
	
	private String name;

	private Color color;
	
	private PlayerDeck deck;
	
	private Player nextPlayer;
	
	private Mission mission;

	private HashSet<Country> territories;
	
	public Player(String nameIn, Color colorIn) {
		name = nameIn;
		color = colorIn;
	}
	
	public void setPlayerDeck(PlayerDeck deckIn) {
		deck = deckIn;
	}
	
	public PlayerDeck getDeck() {
		return deck;
	}
	
	public void addTerritory(Country country) {
		territories.add(country);
		country.setOccupant(this);
	}
	
	public void setNextPlayer(Player nextPlayerIn) {
		nextPlayer = nextPlayerIn;
	}
	
	public Player getNextPlayer() {
		return nextPlayer;
	}
}
