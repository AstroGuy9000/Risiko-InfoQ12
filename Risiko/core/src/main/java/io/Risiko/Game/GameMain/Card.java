package io.Risiko.Game.GameMain;

import java.util.ArrayList;
import java.util.HashSet;

import io.Risiko.Game.GameMap.Country;

public class Card {
	
	private Card_Type type;
	private Country country;
	private final ArrayList<Card> ownerSet;
	private int cardID;
	
	public Card(Card_Type typeIn, Country countryIn, ArrayList<Card> ownerSetIn) {
		type = typeIn;
		country = countryIn;
		ownerSet = ownerSetIn;
		cardID = ownerSetIn.size();
		ownerSetIn.add(this);
	}

	// nur für Joker
	public Card(ArrayList<Card> ownerSetIn) {
		type = Card_Type.Joker;
		country = null;
		ownerSet = ownerSetIn;
		cardID = ownerSetIn.size();
		ownerSetIn.add(this);
	}
	
	public Card_Type getCardType() {
		return type;
	}
	
	public Country getCountry() {
		return country;
	}
	
	protected ArrayList<Card> getOwnerSet() {
		return ownerSet;
	}
	
	public enum Card_Type {
		Infantry,
		Cavalry,
		Artillery,
		Joker
	}
}
