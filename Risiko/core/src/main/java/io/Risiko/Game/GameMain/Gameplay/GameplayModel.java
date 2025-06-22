package io.Risiko.Game.GameMain.Gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.math.MathUtils;

import io.Risiko.Game.GameMain.Card;
import io.Risiko.Game.GameMain.Player;
import io.Risiko.Game.GameMain.Card.Card_Type;
import io.Risiko.Game.GameMain.Missions.Assassination;
import io.Risiko.Game.GameMain.Missions.CaptureCont;
import io.Risiko.Game.GameMain.Missions.CaptureTwelve;
import io.Risiko.Game.GameMain.Missions.MissionUtils;
import io.Risiko.Game.GameMain.Missions.MissionUtils.SecretMission;
import io.Risiko.Game.GameMain.Missions.WorldDomination;
import io.Risiko.Game.GameMap.Continent;
import io.Risiko.Game.GameMap.Country;
import io.Risiko.Game.GameMap.TravelNetwork;
import io.Risiko.Utils.RiskListener;
import io.Risiko.Utils.RiskListenerGroup;
import io.Risiko.Utils.MiscUtils;

public class GameplayModel extends TravelNetwork {
	
	private ArrayList<Card> cardBank;
	
	private int cardLevel;
	
	private ArrayList<Player> players;
	
	private ArrayList<Country> selection;
	
	private Phase gamePhase;
	
	private RiskListenerGroup turnListeners;

	public GameplayModel(TravelNetwork travel, ArrayList<Player> playersIn, boolean isSecretMission) {
		super(travel);
		
		players = playersIn;
		
		for(Player i:  players) {
			i.setPlayerDeck(new HashSet<Card>());
		}
		
		if(isSecretMission) {
			for(Player i:  players) {
				switch(MissionUtils.getRandomSecretMission()) {
				
				default:
					i.setMission(new CaptureTwelve(this, i));
					break;
					
				case SecretMission.CaptureCont:
					i.setMission(new CaptureCont(this, i));
					break;
					
				case SecretMission.CaptureTwelve:
					i.setMission(new CaptureTwelve(this, i));
					break;
					
				case SecretMission.CaptureTwentyfour:
					i.setMission(new CaptureTwelve(this, i));
					break;
					
				case SecretMission.Assassination:
					i.setMission(new Assassination(this, i));
					break;
				}
			}
		} else {
			for(Player i:  players) {
				i.setMission(new WorldDomination(this, i));
			}
		}
		
		ArrayList<Country> countries = new ArrayList<Country>(getStrToCountry().values());
		
		int temp = (int) Math.floor( countries.size()/3.0 );
		
		int infantry = temp + (countries.size() - temp*3);
		int cavalry = temp;
		int artillery = temp;
		
		cardBank = new ArrayList<Card>();
		
		for(int i = 0; i < infantry; i++) {
			Country country = countries.get(MiscUtils.getRandomNumber(0, countries.size()));
			new Card(Card_Type.Infantry, country, cardBank);
			countries.remove(country);
		}
		
		for(int i = 0; i < cavalry; i++) {
			Country country = countries.get(MiscUtils.getRandomNumber(0, countries.size()));
			new Card(Card_Type.Cavalry, country, cardBank);
			countries.remove(country);
		}
		
		for(int i = 0; i < cavalry; i++) {
			Country country = countries.get(MiscUtils.getRandomNumber(0, countries.size()));
			new Card(Card_Type.Artillery, country, cardBank);
			countries.remove(country);
		}
		
		if(31 <= getStrToCountry().values().size()) {
			cardBank.add(new Card(cardBank));
		}
		cardBank.add(new Card(cardBank));
		
		cardLevel = 0;
		
		turnListeners = new RiskListenerGroup();
		
		selection = new ArrayList<Country>();
		
		gamePhase = new GameStartup(players.getLast());
	}
	
	public ArrayList<Player> getPlayersCopy() {
		return new ArrayList<Player>(players);
	}
	
	public ArrayList<Country> getSelection() {
		return selection;
	}
	
	public void intoSelection(Country country) {
		gamePhase.intoSelection(country);
	}
	
	public void resetSelection() {
		selection.clear();
	}
	
	public void nextPhase() {
		gamePhase.nextPhase();
	}
	
	public void doAction(int n) {
		gamePhase.doPrimaryAction(n);
	}
	
	public void undoAction() {
		gamePhase.undoAction();
	}
	
	public Player getTurnOwner() {
		return gamePhase.owner;
	}
	
	public Phase_Type getTurnPhase() {
		return gamePhase.getPhaseType();
	}
	
	public Phase getPhaseObject() {
		return gamePhase;
	}
	
	public void refreshContinentOwnership(Player playerRefresh) {
		for(Continent x: getStrToCont().values()) {
			if(playerRefresh.getCountries().containsAll(getContMembers().get(x.getName()))) {
				playerRefresh.addOwnedCont(x);
			} else {
				playerRefresh.removeOwnedCont(x);
			}
		}
	}
	
	public void addTurnListener(RiskListener listener) {
		turnListeners.add(listener);
	}
	
	public void removeTurnListener(RiskListener listener) {
		turnListeners.remove(listener);
	}
	
	private boolean isAdjacentToPlayer(Player player, Country country) {
		if(country.getOccupant() == player) return true;
		for(Country i: getMovMap().get(country.getName())) {
			if(i.getOccupant() == player) {
				return true;
			}
		}
		return false;
	}
	
	private boolean areAdjacent( Country countryA, Country countryB) {
		if(countryA == countryB) return true;
		for(Country i: getMovMap().get(countryA.getName())) {
			if(i == countryB) {
				return true;
			}
		}
		return false;
	}
	
	public boolean depthSearchPlayer(Country start, Country target, Player player) {
		
		if(start.getOccupant() != player || target.getOccupant() != player) return false;
		
		for(Country i: getStrToCountry().values()) {
			i.setFlag(false);
		}
		
		return depthSearchPlayerRec(start, target, player);
	}
	
	private boolean depthSearchPlayerRec(Country start, Country target, Player player) {	
		start.setFlag(true);
		if(start == target) return true;

		for(Country i: getMovMap().get(start.getName())) {
			if(i.getFlag() == false && i.getOccupant() == player && depthSearchPlayerRec(i, target, player)) return true;
		}
		
		return false;
	}
	
	public boolean canTradeCards(ArrayList<Card> cards) {
		
		if(cards.size() != 3) return false;
		
		ArrayList<Card_Type> cardTypesTemp = new ArrayList<Card_Type>();
		cardTypesTemp.add(Card_Type.Infantry);
		cardTypesTemp.add(Card_Type.Cavalry);
		cardTypesTemp.add(Card_Type.Artillery);
		
		for(Card_Type i: cardTypesTemp) {
			
			int counter = 0;
			
			for(Card x: cards) {
				if(x.getCardType() != i && x.getCardType() != Card_Type.Joker) {
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
	
	private Card pullCard() {
		if(cardBank.isEmpty()) return null;
		
		Card card = cardBank.get(MiscUtils.getRandomNumber(0, cardBank.size()-1));
		cardBank.remove(card);
		return card;
	} 
	
	protected abstract class Phase {
		protected Player owner;
		protected Phase previousPhase;
		protected ArrayList<GameAction> actions;
		
		private Phase(Player ownerIn) {
			selection.clear();
			
			setOwner(ownerIn);
			previousPhase = null;
			actions = new ArrayList<GameAction>();
			turnListeners.thingHappened();
		}
		
		private Phase(Phase phase) {
			selection.clear();
			
			setOwner(phase.owner);
			previousPhase = phase;
			Phase tempPhase = previousPhase;
			for(int i = 0; i < 4; i++) {
				if(tempPhase != null) tempPhase = tempPhase.previousPhase;
				else break;
			}
			tempPhase = null;
			actions = new ArrayList<GameAction>();
		}
		
		protected void addAction(GameAction action) {
			actions.add(action);
			turnListeners.thingHappened();
		}
		
		protected void removeAction(GameAction action) {
			actions.remove(action);
			turnListeners.thingHappened();
		}
		
		protected boolean undoAction() {
			selection.clear();
			if(actions.isEmpty() && previousPhase == null) {
				return false;
			}
			if(!actions.isEmpty()) {
				return actions.getLast().undoAction();
			}
			gamePhase = previousPhase;
			turnListeners.thingHappened();
			return true;
		}
		
		protected void setOwner(Player ownerIn) {
			if(owner == ownerIn) return;
			owner = ownerIn;
			turnListeners.thingHappened();
		}
		
		protected void setGamePhase(Phase phase) {
			gamePhase = phase;
			turnListeners.thingHappened();
		}
		
		abstract protected void intoSelection(Country country);
		
		abstract protected void doPrimaryAction(int n);
		
		abstract protected void nextPhase();
		
		abstract protected Phase_Type getPhaseType();
	}
	
	private interface GameAction {
		
		public boolean undoAction();
	}
	
	protected class GameStartup extends SetupPhase{
		
		private int counter;
		private int startTroops;
		
		private GameStartup(Player last) {
			super(new Phase(last) {
				@Override
				protected void intoSelection(Country country) {}
				@Override
				protected void doPrimaryAction(int n) {}
				@Override
				protected void nextPhase() {}
				@Override
				protected Phase_Type getPhaseType() {
					return Phase_Type.MISC;
				}
			});
			counter = 0;
			
			startTroops = 50 - (players.size()*5);
			
			toSpendTroops = startTroops;
		}
		
		@Override
		protected boolean undoAction() {
			return false;
		}
		
		@Override
		protected void doPrimaryAction(int n) {
			if(toSpendTroops > 0) super.doPrimaryAction(n);
			else nextPhase();
		}
		
		@Override
		protected void nextPhase() {
			if(toSpendTroops > 0) return;
			if(counter < players.size()-1) {
				setOwner(owner.getNextPlayer());
				selection.clear();
				toSpendTroops = startTroops;
				turnListeners.thingHappened();
				counter++;
			} else {
				owner = players.getLast();
				setGamePhase(new SetupPhase(GameStartup.this));
				gamePhase.previousPhase = null;
			}
		}
		
		@Override
		protected Phase_Type getPhaseType() {
			return Phase_Type.GAME_STARTUP;
		}
	}
	
	protected class SetupPhase extends Phase{
		
		protected int toSpendTroops;
		
		private SetupPhase(Phase previousPhase) {
			super(previousPhase);
			
			int limit = 0;
			while(owner.getNextPlayer().getCountries().isEmpty()) {
				owner = owner.getNextPlayer();
				limit ++;
				if(limit > 100) break;
			}
			
			setOwner(owner.getNextPlayer());
			
			turnListeners.thingHappened();
			
			toSpendTroops = (int) Math.floor(owner.getCountries().size()/3f);
			toSpendTroops = Math.max(3, toSpendTroops);
			
			refreshContinentOwnership(owner);
			for(Continent i: owner.getOwnedConts()) toSpendTroops = toSpendTroops + i.getBonus();
		}
		
		private class AddTroops implements GameAction {
			
			private Country target;
			private int troops;

			private AddTroops(Country targetIn, int troopsIn) {
				target = targetIn;
				troops = troopsIn;
				
				troops = Math.min(toSpendTroops, troops);
				toSpendTroops = toSpendTroops-troops;
				
				targetIn.addTroops(troops);
				
				addAction(AddTroops.this);
			}
			
			public boolean undoAction() {
				if(actions.getLast() != AddTroops.this) return false;
				target.addTroops(-troops);
				toSpendTroops = toSpendTroops+troops;
				removeAction(AddTroops.this);
				
				return true;
			}
		}
		
		private class ExchangeCards implements GameAction {
			
			private ArrayList<Card> exchangedCards;
			private int gainedTroops;
			
			public ExchangeCards(ArrayList<Card> cards) {
				
				exchangedCards = cards;
				gainedTroops = 0;
				
				if(!canTradeCards(cards)) {
					exchangedCards = new ArrayList<Card>();
					addAction(ExchangeCards.this);
					return;
				}
					
				cardLevel++;
				
				for(Card i: cards) {
					if(i.getCountry() != null && owner.getCountries().contains(i.getCountry())) {
						i.getCountry().addTroops(2);
					}
				}
					
				if(cardLevel <= 5) gainedTroops = toSpendTroops + 2 + (2*cardLevel);
				else gainedTroops = toSpendTroops + 15 + ( 5*(cardLevel-6) );
				
				toSpendTroops += gainedTroops;
				owner.getDeck().removeAll(cards);
				cardBank.addAll(cards);
				
				addAction(ExchangeCards.this);
			}

			@Override
			public boolean undoAction() {
				if(actions.getLast() != ExchangeCards.this) return false;
				
				if(exchangedCards.isEmpty()) {
					removeAction(ExchangeCards.this);
					return true;
				}
				
				for(Card i: exchangedCards) {
					if(i.getCountry() != null && owner.getCountries().contains(i.getCountry())) {
						i.getCountry().addTroops(-2);
					}
				}
				
				toSpendTroops -= gainedTroops;
				owner.getDeck().addAll(exchangedCards);
				cardBank.removeAll(exchangedCards);
				removeAction(ExchangeCards.this);
				return true;
			}
			
		}
		
		public int getToSpendTroops() {
			return toSpendTroops;
		}
		
		public void exchangeCards(ArrayList<Card> cards) {
			new ExchangeCards(cards);
		}

		@Override
		protected void doPrimaryAction(int n) {
			
			if(selection.isEmpty()) return;
			new AddTroops(selection.get(0), n);
		}
		
		@Override
		protected void intoSelection(Country country) {
			if(country.getOccupant() == owner) {
				selection.clear();
				selection.add(country);
			}
		}

		@Override
		protected void nextPhase() {
			if(toSpendTroops <= 0) {
				setGamePhase(new AttackPhase(SetupPhase.this));
			}
		}

		@Override
		protected Phase_Type getPhaseType() {
			return Phase_Type.SETUP;
		}
	}
	
	protected class AttackPhase extends Phase {
		
		private AttackPhase(Phase previousPhase) {
			super(previousPhase);
		}
		
		private class AttackCountry implements GameAction{

			private Country start;
			private Country target;
			private int troops;
			private int lossAttack;
			private int lossDefend;
			private GameAction conquered;
			
			private AttackCountry(Country startIn, Country targetIn, int troopsIn) {
				start = startIn;
				target = targetIn;
				troops = troopsIn;
				conquered = null;
				
				int inputTroops = troopsIn;
				
				troops = MathUtils.clamp(troops, 1, 3);
				int defenders = MathUtils.clamp(targetIn.getTroops(), 1, 2);
				
				int[] attackRolls = new int[troops];
				for(int i = 0; i < troops; i++) {
					attackRolls[i] = MiscUtils.d6();
				}
				Arrays.sort(attackRolls);
				
				int[] defendRolls = new int[defenders];
				for(int i = 0; i < defenders; i++) {
					defendRolls[i] = MiscUtils.d6();
				}
				Arrays.sort(defendRolls);
				
				int compareNum = Math.min(troops, defenders);
				
				for(int i = 0; i < compareNum; i++) {
					if(defendRolls[defendRolls.length-1-i] >= attackRolls[attackRolls.length-1-i]) {
						lossAttack++;
					} else {
						lossDefend++;
					}
				}
				start.addTroops(-lossAttack);
				target.addTroops(-lossDefend);
				
				if(target.getTroops() <= 0) {
					
					Card pulledCard = pullCard();
					if(pulledCard != null) start.getOccupant().addCard(pulledCard);
					
					conquered = new GameAction() {

						private int troopsToCountry = troops-lossAttack;
						private String previousOwnerName = target.getOccupant().getName();
						private Card pulledCardAction = pulledCard;
						
						public boolean undoAction() {
							
							Player previousOwner = null;
							
							if(pulledCard != null) start.getOccupant().removeCard(pulledCardAction);
							
							for(Player i: players) {
								if(i.getName().equals(previousOwnerName)) {
									previousOwner = i;
									break;
								}
							}
							
							if(previousOwner == null) return false;
							
							target.setOccupant(previousOwner);
							target.setTroops(lossDefend);
							start.addTroops(lossAttack+1);
							removeAction(AttackCountry.this);
							return true;
						}
						
					};
					target.setOccupant(start.getOccupant());
					target.setTroops(1);
					start.addTroops(-1);
					selection.clear();
				}
				
				addAction(AttackCountry.this);
				
				String attackRollsStr = "";
				for(int i = 0; i < attackRolls.length; i++) {
					attackRollsStr = Integer.toString(attackRolls[i]) + " " + attackRollsStr;
				}
				
				String defendRollsStr = "";
				for(int i = 0; i < defendRolls.length; i++) {
					defendRollsStr = Integer.toString(defendRolls[i])  + " " + defendRollsStr;
				}
				
				
				System.out.println("Attacker rolls: " + attackRollsStr);
				System.out.println("Defender rolls: " + defendRollsStr);
				System.out.println("Attacker losses: " + lossAttack);
				System.out.println("Defender losses: " + lossDefend);
				System.out.println("- - - - - - - - - - - - - - - - - - - -");
				
				if(inputTroops > troops && (start.getTroops()-1) >= (inputTroops-troops) && conquered == null) {
					new AttackCountry(start, target, inputTroops-troops);
				}
			}
			
			public boolean undoAction() {
				if(actions.getLast() != AttackCountry.this) return false;
				if(conquered != null) return conquered.undoAction();
				
				start.addTroops(lossAttack);
				target.addTroops(lossDefend);
				removeAction(AttackCountry.this);
				
				return true;
			}
		}
		
		private class FortifyTerritory implements GameAction {
			
			private boolean isValid;
			
			private Country start;
			private Country target;
			
			private int troops;

			private FortifyTerritory(int troopsIn) {
				isValid = false;
				
				if(!actions.isEmpty()) {
				if(actions.getLast().getClass() == AttackCountry.class) {
					AttackCountry attack = (AttackCountry) actions.getLast();
					
						if(attack.conquered != null) {
							
							start = attack.start;
							target = attack.target;
							
							troops = troopsIn;
						
							troops = Math.min(attack.start.getTroops()-1, troopsIn);
							start.addTroops(-troops);
							target.addTroops(troops);
						
							isValid = true;
						
							addAction(FortifyTerritory.this);
						}
					}
				}
			}
			
			@Override
			public boolean undoAction() {
				if(isValid == false) {
					removeAction(FortifyTerritory.this);
					return true;
				}
				if(actions.getLast() != FortifyTerritory.this) return false;
				start.addTroops(troops);
				target.addTroops(-troops);
				removeAction(FortifyTerritory.this);
				
				return true;
			}
			
		}
		
		@Override
		protected void intoSelection(Country country) {
			if(selection.isEmpty()) {
				if(isAdjacentToPlayer(owner,country)) {
					selection.add(country);
				} else {
					if(!actions.isEmpty()) {
						if(actions.getLast().getClass() == AttackCountry.class) {
							AttackCountry attack = (AttackCountry) actions.getLast();
							if( (country == attack.start || country == attack.target) && attack.conquered != null) {
								selection.add(country);
							}
						}
					}
				}
				return;
			}
			if(selection.contains(country)) {
				selection.remove(country);
				return;
			}
			if(selection.size() < 2) {
				if(!actions.isEmpty()) {
					if(actions.getLast().getClass() == AttackCountry.class) {
						AttackCountry attack = (AttackCountry) actions.getLast();
						if( (selection.get(0) == attack.start || selection.get(0) == attack.target) && (country == attack.start || country == attack.target) && attack.conquered != null) {
							selection.add(country);
							return;
						}
					}
				}
				if(selection.get(0).getOccupant() == country.getOccupant()) {
					selection.clear();
					intoSelection(country);
					return;
				} 
				if( (selection.get(0).getOccupant() == owner ^ country.getOccupant() == owner) && areAdjacent(selection.get(0), country)) {
					selection.add(country);
					return;
				}
				return;
			}
		}

		@Override
		protected void doPrimaryAction(int n) {
			if(selection.size() < 2 || selection.isEmpty()) return;
			if(!actions.isEmpty()) {
				if(actions.getLast().getClass() == AttackCountry.class) {
					AttackCountry attack = (AttackCountry) actions.getLast();
					if( (selection.get(0) == attack.start || selection.get(0) == attack.target) && 
							(selection.get(1) == attack.start || selection.get(1) == attack.target) && 
							attack.conquered != null) {
						new FortifyTerritory(n);
						return;
					}
				}
			}
			if( (selection.get(0).getOccupant() == owner ^ selection.get(1).getOccupant() == owner) ) {
				if(selection.get(0).getOccupant() == owner && selection.get(0).getTroops() >= 2) {
					new AttackCountry(selection.get(0), selection.get(1), n);
					return;
				}
				if(selection.get(1).getOccupant() == owner && selection.get(1).getTroops() >= 2) new AttackCountry(selection.get(1), selection.get(0), n);
			}
		}
		
		@Override
		protected void nextPhase() {
			setGamePhase(new FortifyPhase(AttackPhase.this));
		}

		protected Phase_Type getPhaseType() {
			return Phase_Type.ATTACK;
		}
	}
	
	protected class FortifyPhase extends Phase {
		
		private FortifyPhase(Phase previousPhase) {
			super(previousPhase);
		}
		
		private class TransferTroops implements GameAction {

			private Country start;
			private Country target;
			private int troops;
			
			private TransferTroops(Country startIn, Country targetIn, int troopsIn) {
				start = startIn;
				target = targetIn;
				troops = troopsIn;
				
				troops = Math.min(startIn.getTroops()-1, troops);
				
				start.addTroops(-troops);
				target.addTroops(troops);
				
				addAction(TransferTroops.this);
			}
			
			public boolean undoAction() {
				if(actions.getLast() != TransferTroops.this) return false;
				start.addTroops(troops);
				target.addTroops(-troops);
				removeAction(TransferTroops.this);
				
				return true;
			}
		}
		
		@Override
		protected void doPrimaryAction(int n) {
			if(selection.size() == 2) {
				new TransferTroops(selection.get(0), selection.get(1), n);
			}
		}

		@Override
		protected void intoSelection(Country country) {
			if(selection.contains(country)) {
				selection.remove(country);
				return;
			}
			if(selection.isEmpty() && country.getOccupant() == owner) {
				selection.add(country);
				return;
			}
			if(selection.size() < 2 && depthSearchPlayer(selection.get(0), country, owner)) {
				selection.add(country);
				return;
			}
		}

		@Override
		protected void nextPhase() {
			setGamePhase(new SetupPhase(FortifyPhase.this));
		}

		protected Phase_Type getPhaseType() {
			return Phase_Type.FORTIFY;
		}
	}
	
	public enum Phase_Type {
		GAME_STARTUP,
		SETUP,
		ATTACK,
		FORTIFY,
		MISC
	}
}
