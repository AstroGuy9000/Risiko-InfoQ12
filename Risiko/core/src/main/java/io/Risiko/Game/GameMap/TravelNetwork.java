package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;

public class TravelNetwork {

	private HashMap<String,Continent> strToContinent;
	private HashMap<String,Country> strToCountry;
	
	private HashMap<String,HashSet<Country>> contMembers;	//String --> Kontinent; HashSet<Country> --> Länder die im Kontinent sind
	
	private HashMap<String,HashSet<Country>> movement;		// String--> Land; HashSet<Country> --> Länder die eine Verbindung haben	
	
	private ArrayList<float[]> decoLines;
	
	public TravelNetwork(HashMap<String,Continent> strToContinentIn, HashMap<String,Country> strToCountryIn, HashMap<String,HashSet<Country>> contMembersIn, 
			HashMap<String,HashSet<Country>> movementIn, ArrayList<float[]> decoLinesIn) {
		
		strToContinent = strToContinentIn;
		strToCountry = strToCountryIn;
		contMembers = contMembersIn;
		movement = movementIn;
		decoLines = decoLinesIn;
	}
	
	public TravelNetwork() {
		strToContinent = new HashMap<String,Continent>();
		strToCountry = new HashMap<String,Country>();
		contMembers = new HashMap<String,HashSet<Country>>();
		movement = new HashMap<String,HashSet<Country>>();
		decoLines = new ArrayList<float[]>();
	}
	
	protected void addContinent(Continent cont) {
		strToContinent.putIfAbsent(cont.getName(), cont);
		
		contMembers.put(cont.getName(), new HashSet<Country>());
	}
	
	protected void removeContinent(Continent cont) {
		strToContinent.remove(cont.getName());
		
		contMembers.remove(cont.getName());
	}
	
	protected void renameCont(Continent cont, String newName) {
		String oldName = cont.getName();
		
		if(!strToContinent.values().contains(cont)) return;
		for(String i: strToContinent.keySet()) {
			if(i.equals(newName)) return;
		}
		
		strToContinent.remove(oldName);
		cont.setName(newName);
		strToContinent.put(newName, cont);
		
		HashSet<Country> temp = contMembers.get(oldName);
		
		contMembers.remove(oldName);
		contMembers.put(newName, temp);
	}
	
	protected void addCountry(Country country) {
		strToCountry.put(country.getName(), country);
		
		movement.put(country.getName(), new HashSet<Country>());
	}
	
	protected void removeCountry(Country country) {
		String name = country.getName(); 
		
		if(!strToCountry.containsKey(name)) return;
		
		strToCountry.remove(name);
		
		movement.remove(name);
		
		for(HashSet<Country> i: movement.values()) {
			i.remove(country);
		}
	}
	
	protected void renameCountry(Country country, String newName) {
		System.out.println("rename called");
		
		String oldName = country.getName();
		
		if(!strToCountry.values().contains(country)) return;
		for(String i: strToCountry.keySet()) {
			if(i.equals(newName)) return;
		}
		
		strToCountry.remove(oldName);
		country.setName(newName);
		strToCountry.put(newName, country);
		
		HashSet<Country> temp = movement.get(oldName);
		
		movement.remove(oldName);
		movement.put(newName, temp);
	}
	
	protected void addMember(Continent cont, ArrayList<Country> countries) {
		for(Country i: countries) {
			addMember(cont, i);
		}
	}
	
	protected void addMember(Continent cont, Country country) {
		if(!strToContinent.values().contains(cont) || !strToCountry.values().contains(country)) return;
		for(HashSet<Country> i: contMembers.values()) {
			i.remove(country);
		}
		
		contMembers.get(cont.getName()).add(country);
	}
	
	protected void removeMember(Continent cont, ArrayList<Country> countries) {
		for(Country i: countries) {
			removeMember(cont, i);
		}
	}
	
	protected void removeMember(Continent cont, Country country) {
		if(!strToContinent.values().contains(cont) || !strToCountry.values().contains(country)) return;
		for(HashSet<Country> i: contMembers.values()) {
			i.remove(country);
		}
	}
	
	protected void addRoute(Country countryA, Country countryB) {
		movement.get(countryA.getName()).add(countryB);
		movement.get(countryB.getName()).add(countryA);
		
		System.out.println("   Add Route");
		System.out.println(" Has Route: " + countryA.getName() + " " + countryB.getName() + " : " + movement.get(countryA.getName()).contains(countryB));
		System.out.println("--  --  --  --  --  --  --  --");
	}
	
	protected void removeRoute(Country countryA, Country countryB) {
		movement.get(countryA.getName()).remove(countryB);
		movement.get(countryB.getName()).remove(countryA);
		
		System.out.println("Remove Route");
		System.out.println(" Has Route: " + countryA.getName() + " " + countryB.getName() + " : " + movement.get(countryA.getName()).contains(countryB));
		System.out.println("--  --  --  --  --  --  --  --");
	}
	
	protected void addLine(float p0x, float p0y, float p1x, float p1y) {
		float[] pArr = new float[] {p0x, p0y, p1x, p1y};
		
		addLine(pArr);
	}
	
	protected void addLine(Vector2 p0, Vector2 p1) {
		float[] pArr = new float[] {p0.x, p0.y, p1.x, p1.y};
		
		addLine(pArr);
	}
	
	protected void addLine(float[] pArr) {	// pArr --> [p0x, p0y, p1x, p1y]
		if(pArr.length != 4) return;
		
		decoLines.add(pArr);
	}
	
	protected void removeLine(int index) {
		decoLines.remove(index);
	}
	
	public HashMap<String,Continent> getStrToCont() {
		return strToContinent;
	}
	
	public HashMap<String,Country> getStrToCountry() {
		return strToCountry;
	}
	
	public HashMap<String,HashSet<Country>> getContMembers() {
		return contMembers;
	}
	
	public HashMap<String,HashSet<Country>> getMovMap() {
		return movement;
	}
	
	public ArrayList<float[]> getDecoLines() {
		return decoLines;
	}
}
