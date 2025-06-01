package io.Risiko.Game.GameMain;

import io.Risiko.Game.GameMap.Country;

public class Card {
	
	private CardType type;
	private Country country;
	
	public Card(CardType typeIn, Country countryIn) {
		type = typeIn;
		country = countryIn;
	}

	// nur für Joker
	public Card() {
		type = CardType.Joker;
		country = null;
	}
	
	public CardType getCardType() {
		return type;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public enum CardType {
		Infantry,
		Cavalry,
		Artillery,
		Joker
	}
}
