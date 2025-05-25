package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TravelNetworkSave {
	
	private HashMap<String,Continent> strToContinent;
	private HashMap<String,Country> strToCountry;
	
	private HashMap<String,HashSet<String>> contMembers;	//String --> Kontinent; HashSet<Country> --> Länder die im Kontinent sind
	
	private HashMap<String,HashSet<String>> movement;		// String--> Land; HashSet<Country> --> Länder die eine Verbindung haben	
	
	private ArrayList<float[]> decoLines;
	
	private TravelNetworkSave() {};
	
	protected TravelNetworkSave(TravelNetwork travel) {
		strToContinent = travel.getStrToCont();
		strToCountry = travel.getStrToCountry();
		
		contMembers = new HashMap<String,HashSet<String>>();
		for(String i: travel.getContMembers().keySet()) {
			HashSet<String> temp = new HashSet<String>();
			for(Country x: travel.getContMembers().get(i)) {
				temp.add(x.getName());
			}
			contMembers.put(i, temp);
		}
		
		movement = new HashMap<String,HashSet<String>>();
		for(String i: travel.getMovMap().keySet()) {
			HashSet<String> temp = new HashSet<String>();
			for(Country x: travel.getMovMap().get(i)) {
				temp.add(x.getName());
			}
			movement.put(i, temp);
		}
		
		decoLines = travel.getDecoLines();
	}
	
	protected TravelNetwork rebuildTravelNetwork() {
		
		HashMap<String,HashSet<Country>> contMembersRebuilt = new HashMap<String,HashSet<Country>>();
		for(String i: contMembers.keySet()) {
			HashSet<Country> temp = new HashSet<Country>();
			for(String x: contMembers.get(i)) {
				temp.add(strToCountry.get(x));
				
				if(strToCountry.get(x) == null) {
					System.out.println("Key " + i + " is null in contMembers");
				}
				System.out.println("testttt");
			}
			contMembersRebuilt.put(i, temp);
		}
		
		HashMap<String,HashSet<Country>> movementRebuilt = new HashMap<String,HashSet<Country>>();
		for(String i: movement.keySet()) {
			HashSet<Country> temp = new HashSet<Country>();
			for(String x: movement.get(i)) {
				temp.add(strToCountry.get(x));
				
				if(strToCountry.get(i) == null) {
					System.out.println("Key " + i + " is null in movement");
				} 
			}
			movementRebuilt.put(i, temp);
		}
		
		return new TravelNetwork(strToContinent, strToCountry, contMembersRebuilt, movementRebuilt, decoLines);
	}
}
