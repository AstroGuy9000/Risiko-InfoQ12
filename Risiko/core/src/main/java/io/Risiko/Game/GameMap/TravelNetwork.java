package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TravelNetwork {

	private HashMap<String,Country> strToCountry;
	
	private HashMap<String,HashSet<Country>> movement;
	private HashMap<String,HashSet<Country>> alwaysDraw;
	
	private TravelNetwork() {}
	
	protected TravelNetwork(HashMap<String,Country> strToCountryIn ,HashMap<String,HashSet<Country>> movementIn, HashMap<String,HashSet<Country>> alwaysDrawIn) {
		strToCountry = strToCountryIn;
		movement = movementIn;
		alwaysDraw = alwaysDrawIn;
	}
	
	protected void addCountry(Country country) {
		strToCountry.put(country.getName(), country);
		
		movement.putIfAbsent(country.getName(), new HashSet<Country>());
		alwaysDraw.putIfAbsent(country.getName(), new HashSet<Country>());
	}
	
	protected void removeCountry(Country country) {
		String name = country.getName(); 
		
		strToCountry.remove(name);
		
		movement.remove(name);
		alwaysDraw.remove(name);
		
		for(HashSet<Country> i: movement.values()) {
			i.remove(country);
		}
		
		for(HashSet<Country> i: alwaysDraw.values()) {
			i.remove(country);
		}
	}
	
	protected void addRoute(Country countryA, Country countryB) {
		movement.get(countryA.getName()).add(countryB);
		movement.get(countryB.getName()).add(countryA);
	}
	
	protected void removeRoute(Country countryA, Country countryB) {
		movement.get(countryA.getName()).remove(countryB);
		movement.get(countryB.getName()).remove(countryA);
	}
	
	protected void addDraw(Country countryA, Country countryB) {
		alwaysDraw.get(countryA.getName()).add(countryB);
		alwaysDraw.get(countryB.getName()).add(countryA);
	}
	
	protected void removeDraw(Country countryA, Country countryB) {
		alwaysDraw.get(countryA.getName()).remove(countryB);
		alwaysDraw.get(countryB.getName()).remove(countryA);
	}
	
	public HashMap<String,Country> getStrToCountry() {
		return strToCountry;
	}
	
	public HashMap<String,HashSet<Country>> getMovMap() {
		return movement;
	}
	
	public HashMap<String,HashSet<Country>> getDraw() {
		return alwaysDraw;
	}
}
