package io.Risiko.Game.GameMain.Gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import io.Risiko.Game.GameMain.Card;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Card.CardType;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Utils.Utils;

public class GameplayModel extends TravelNetwork {
	
	private ArrayList<Card> cardBank;
	
	private ArrayList<Player> players;

	public GameplayModel(TravelNetwork travel, ArrayList<Player> playersIn) {
		super(travel);
		
		ArrayList<Country> countries = new ArrayList<Country>(getStrToCountry().values());
		
		int temp = (int) Math.floor( countries.size()/3.0 );
		
		int infantry = temp + (countries.size() - temp*3);
		int cavalry = temp;
		int artillery = temp;
		
		for(int i = 0; i < infantry; i++) {
			Country country = countries.get(Utils.getRandomNumber(0, countries.size()));
			cardBank.add(new Card(CardType.Infantry, country));
			countries.remove(country);
		}
		
		for(int i = 0; i < cavalry; i++) {
			Country country = countries.get(Utils.getRandomNumber(0, countries.size()));
			cardBank.add(new Card(CardType.Cavalry, country));
			countries.remove(country);
		}
		
		for(int i = 0; i < cavalry; i++) {
			Country country = countries.get(Utils.getRandomNumber(0, countries.size()));
			cardBank.add(new Card(CardType.Artillery, country));
			countries.remove(country);
		}
		
		if(31 <= getStrToCountry().values().size()) {
			cardBank.add(new Card());
		}
		cardBank.add(new Card());
		
		players = playersIn;
	}
	
	private Card pullCard() {
		Card card = cardBank.get(Utils.getRandomNumber(0, cardBank.size()));
		cardBank.remove(card);
		return card;
	}
	
	private boolean canTradeCards(ArrayList<Card> cards) {
		
		if(cards.size() != 3) return false;
		
		ArrayList<CardType> cardTypesTemp = new ArrayList<CardType>();
		cardTypesTemp.add(CardType.Infantry);
		cardTypesTemp.add(CardType.Cavalry);
		cardTypesTemp.add(CardType.Artillery);
		
		for(CardType i: cardTypesTemp) {
			
			int counter = 0;
			
			for(Card x: cards) {
				if(x.getCardType() != i && x.getCardType() != CardType.Joker) {
					break;
				} 
				counter++;
			}
			
			if(counter == 3) {
				return true;
			}
		}
		
		Card card1 = cards.get(0);
		Card card2 = cards.get(1);
		Card card3 = cards.get(2);
		
		cardTypesTemp.remove(card1.getCardType());
		
		if(cardTypesTemp.contains(card2.getCardType())) {
			
			cardTypesTemp.remove(card2.getCardType());
			
			if(cardTypesTemp.contains(card3.getCardType())) {
				return true;
			}
		}
		
		return false;
	}
	
	private int tradeInCards(ArrayList<Card> cards) {
		
		if(!canTradeCards(cards)) return 0;
		
		return 0;
	}
	
	public class PlayerDeck {
		
		private HashSet<Card> infantry;
		private HashSet<Card> cavalry;
		private HashSet<Card> artillery;
		private HashSet<Card> joker;
		
		private PlayerDeck() {
			infantry = new HashSet<Card>();
			cavalry  = new HashSet<Card>();
			artillery  = new HashSet<Card>();
			joker  = new HashSet<Card>();
		}
		
		private HashSet<Card> getInfantry() {
			return infantry;
		}
		
		private HashSet<Card> getCavalry() {
			return cavalry;
		}
		
		private HashSet<Card> getArtillery() {
			return artillery;
		}
		
		private HashSet<Card> getJoker() {
			return joker;
		}
	}
}
