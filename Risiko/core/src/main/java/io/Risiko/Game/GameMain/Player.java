package io.Risiko.Game.GameMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;

import io.Risiko.Game.GameMap.Continent;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Utils.Utils;

public class Player {
	
	private String name;

	private Color color;
	
	private HashSet<Card> deck;
	
	private Player nextPlayer;
	
	private Mission mission;

	private HashSet<Country> countries;
	private HashSet<Continent> continents;
	
	public Player(String nameIn, Color colorIn) {
		name = nameIn;
		color = colorIn;
		
		countries = new  HashSet<Country>();
		continents = new HashSet<Continent>();
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setPlayerDeck(HashSet<Card> deckIn) {
		deck = deckIn;
	}
	
	public HashSet<Card> getDeck() {
		return deck;
	}
	
	public HashSet<Country> getCountries() {
		return countries;
	}
	
	/**
	 * Fügt ein Land zu den eroberten Ländern des Spielers hinzu.
	 * Es wird der Besatzers des Landes zum Spieler umgestellt.
	 * 
	 * @param country	Land
	 */
	public void addCountry(Country country) {
		countries.add(country);
		country.setOccupant(this);
		System.out.println("Country " + country.getName() + " added to " + name);
	}
	
	public void removeCountry(Country country) {
		countries.remove(country);
		System.out.println("Country " + country.getName() + " removed from " + name);
	}
	
	public void setNextPlayer(Player nextPlayerIn) {
		nextPlayer = nextPlayerIn;
	}
	
	public Player getNextPlayer() {
		return nextPlayer;
	}
	
	public HashSet<Continent> getOwnedConts() {
		return continents;
	}
	
	public void removeOwnedCont(Continent contIn) {
		continents.remove(contIn);
	}
	
	public void addOwnedCont(Continent contIn) {
		continents.add(contIn);
	}
	
	public void distributeCountries(ArrayList<Country> toDistribute) {
		if(toDistribute.isEmpty() || toDistribute == null) return;
		Country toAdd = toDistribute.get(Utils.getRandomNumber(0, toDistribute.size()-1));
		addCountry(toAdd);
		toDistribute.remove(toAdd);
		nextPlayer.distributeCountries(toDistribute);
	}
}
