package io.Risiko.Game.GameMain;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;

import io.Risiko.Game.GameMain.Gameplay.GameplayModel.PlayerDeck;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Utils.Utils;

public class Player {
	
	private String name;

	private Color color;
	
	private PlayerDeck deck;
	
	private Player nextPlayer;
	
	private Mission mission;

	private HashSet<Country> countries;
	
	public Player(String nameIn, Color colorIn) {
		name = nameIn;
		color = colorIn;
		
		countries = new  HashSet<Country>();
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setPlayerDeck(PlayerDeck deckIn) {
		deck = deckIn;
	}
	
	public PlayerDeck getDeck() {
		return deck;
	}
	
	public HashSet<Country> getCountries() {
		return countries;
	}
	
	public void addCountry(Country country) {
		countries.add(country);
		country.setOccupant(this);
	}
	
	public void setNextPlayer(Player nextPlayerIn) {
		nextPlayer = nextPlayerIn;
	}
	
	public Player getNextPlayer() {
		return nextPlayer;
	}
	
	public void distributeCountries(ArrayList<Country> toDistribute) {
		if(toDistribute.isEmpty() || toDistribute == null) return;
		Country toAdd = toDistribute.get(Utils.getRandomNumber(0, toDistribute.size()-1));
		addCountry(toAdd);
		toDistribute.remove(toAdd);
		nextPlayer.distributeCountries(toDistribute);
	}
}
