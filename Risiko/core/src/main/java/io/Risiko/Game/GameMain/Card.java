package io.Risiko.Game.GameMain;

import java.util.ArrayList;
import java.util.HashSet;

import io.Risiko.Game.GameMap.Country;

public class Card {
	
	private CardType type;
	private Country country;
	private final ArrayList<Card> ownerSet;
	private int cardID;
	
	public Card(CardType typeIn, Country countryIn, ArrayList<Card> ownerSetIn) {
		type = typeIn;
		country = countryIn;
		ownerSet = ownerSetIn;
		cardID = ownerSetIn.size();
		ownerSetIn.add(this);
	}

	// nur für Joker
	public Card(ArrayList<Card> ownerSetIn) {
		type = CardType.Joker;
		country = null;
		ownerSet = ownerSetIn;
		cardID = ownerSetIn.size();
		ownerSetIn.add(this);
	}
	
	public CardType getCardType() {
		return type;
	}
	
	public Country getCountry() {
		return country;
	}
	
	protected ArrayList<Card> getOwnerSet() {
		return ownerSet;
	}
	
	public enum CardType {
		Infantry,
		Cavalry,
		Artillery,
		Joker
	}
}
