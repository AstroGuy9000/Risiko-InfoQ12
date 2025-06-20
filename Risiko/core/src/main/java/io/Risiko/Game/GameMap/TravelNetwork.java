package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;

/**
 * Enthält die Daten einer Spielkarte
 */
public class TravelNetwork {

	/**
	 * Verbindet Kontinente mit ihrem Namen
	 */
	private HashMap<String,Continent> strToContinent;
	/**
	 * Verbindet Länder mit ihrem Namen
	 */
	private HashMap<String,Country> strToCountry;
	
	/**
	 * Enthält die Zugehörigkeit von Ländern zu Kontinenten
	 */
	private HashMap<String,HashSet<Country>> contMembers;	//String --> Kontinent; HashSet<Country> --> Länder die im Kontinent sind
	
	/**
	 * Enthält die Verbindungen zwischen Ländern
	 */
	private HashMap<String,HashSet<Country>> movement;		// String--> Land; HashSet<Country> --> Länder die eine Verbindung haben	
	
	/**
	 * Enthält die Punkte aller kosmetischen linien
	 */
	private ArrayList<float[]> decoLines;
	
	/**
	 * Erstellt eine leere Karte.
	 */
	public TravelNetwork() {
		strToContinent = new HashMap<String,Continent>();
		strToCountry = new HashMap<String,Country>();
		contMembers = new HashMap<String,HashSet<Country>>();
		movement = new HashMap<String,HashSet<Country>>();
		decoLines = new ArrayList<float[]>();
	}
	
	/**
	 * Sollte nur beim Laden einer Karte verwendet werden
	 * 
	 * @param strToContinentIn
	 * @param strToCountryIn
	 * @param contMembersIn
	 * @param movementIn
	 * @param decoLinesIn
	 */
	public TravelNetwork(HashMap<String,Continent> strToContinentIn, HashMap<String,Country> strToCountryIn, HashMap<String,HashSet<Country>> contMembersIn, 
			HashMap<String,HashSet<Country>> movementIn, ArrayList<float[]> decoLinesIn) {
		
		strToContinent = strToContinentIn;
		strToCountry = strToCountryIn;
		contMembers = contMembersIn;
		movement = movementIn;
		decoLines = decoLinesIn;
	}
	
	/**
	 * Kopiert eine andere Karte
	 * 
	 * @param travel
	 */
	public TravelNetwork(TravelNetwork travel) {
		strToContinent = travel.getStrToCont();
		strToCountry = travel.getStrToCountry();
		contMembers = travel.getContMembers();
		movement = travel.getMovMap();
		decoLines = travel.getDecoLines();
	}
	
	/**
	 * Fügt einen Kontinent zur Karte hinzu.
	 * 
	 * @param cont	Kontinent
	 */
	protected void addContinent(Continent cont) {
		strToContinent.putIfAbsent(cont.getName(), cont);
		
		contMembers.put(cont.getName(), new HashSet<Country>());
	}
	
	/**
	 * Enfernt einen Kontinent von der Karte.
	 * 
	 * @param cont	Kontinent
	 */
	protected void removeContinent(Continent cont) {
		strToContinent.remove(cont.getName());
		
		contMembers.remove(cont.getName());
	}
	
	/**
	 * Ändert den Namen eines Kontinents.
	 * Wenn ein anderer Kontinent schon den Namen hat passiert nichts.
	 * 
	 * @param country	Kontinent
	 * @param newName	Neuer Name
	 */
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
	
	/**
	 * Fügt ein Land zur Karte hinzu.
	 * 
	 * @param country	Land
	 */
	protected void addCountry(Country country) {
		strToCountry.put(country.getName(), country);
		
		movement.put(country.getName(), new HashSet<Country>());
	}
	
	/**
	 * Enfernt ein Land von der Karte.
	 * 
	 * @param country	Land
	 */
	protected void removeCountry(Country country) {
		String name = country.getName(); 
		
		if(!strToCountry.containsKey(name)) return;
		
		strToCountry.remove(name);
		
		movement.remove(name);
		
		for(HashSet<Country> i: movement.values()) {
			i.remove(country);
		}
	}
	
	/**
	 * Ändert den Namen eines Landes.
	 * Wenn ein anderes Land schon den Namen hat passiert nichts.
	 * 
	 * @param country	Land
	 * @param newName	Neuer Name
	 */
	protected void renameCountry(Country country, String newName) {
		
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
	
	/**
	 * Fügt mehrere Länder einem Kontinent hinzu.
	 * Ruft für jedes Land {@link #addMember(Continent, Country)}
	 * 
	 * @param cont
	 * @param countries
	 */
	protected void addMember(Continent cont, ArrayList<Country> countries) {
		for(Country i: countries) {
			addMember(cont, i);
		}
	}
	
	/**
	 * Fügt ein Land einem Kontinent hinzu.
	 * Falls das Land 
	 * Macht nichts wenn der Kontinent oder das Land nicht in der Karte enthalten sind.
	 * 
	 * @param cont
	 * @param country
	 */
	protected void addMember(Continent cont, Country country) {
		if(!strToContinent.values().contains(cont) || !strToCountry.values().contains(country)) return;
		for(HashSet<Country> i: contMembers.values()) {
			i.remove(country);
		}
		
		contMembers.get(cont.getName()).add(country);
	}
	
	/**
	 * Entfernt mehrere Länder von einem Kontinent.
	 * Ruft für jedes Land {@link #removeMember(Continent, Country)}
	 * 
	 * @param cont
	 * @param countries
	 */
	protected void removeMember(Continent cont, ArrayList<Country> countries) {
		for(Country i: countries) {
			removeMember(cont, i);
		}
	}
	
	/**
	 * Entfernt ein Land von einem Kontinent.
	 * Macht nichts wenn der Kontinent oder das Land nicht in der Karte enthalten sind.
	 * 
	 * @param cont
	 * @param country
	 */
	protected void removeMember(Continent cont, Country country) {
		if(!strToContinent.values().contains(cont) || !strToCountry.values().contains(country)) return;
		for(HashSet<Country> i: contMembers.values()) {
			i.remove(country);
		}
	}
	
	/**
	 * Fügt eine Verbindung im Graphen zwischen zwei Ländern hinzu
	 * 
	 * @param countryA	Knoten A
	 * @param countryB	Knoten B
	 */
	protected void addRoute(Country countryA, Country countryB) {
		movement.get(countryA.getName()).add(countryB);
		movement.get(countryB.getName()).add(countryA);
		
		System.out.println("   Add Route");
		System.out.println(" Has Route: " + countryA.getName() + " " + countryB.getName() + " : " + movement.get(countryA.getName()).contains(countryB));
		System.out.println("--  --  --  --  --  --  --  --");
	}
	
	/**
	 * Enfernt eine Verbindung zwischen zwei Ländern
	 * 
	 * @param countryA	Knoten A
	 * @param countryB	Knoten B
	 */
	protected void removeRoute(Country countryA, Country countryB) {
		movement.get(countryA.getName()).remove(countryB);
		movement.get(countryB.getName()).remove(countryA);
		
		System.out.println("Remove Route");
		System.out.println(" Has Route: " + countryA.getName() + " " + countryB.getName() + " : " + movement.get(countryA.getName()).contains(countryB));
		System.out.println("--  --  --  --  --  --  --  --");
	}
	
	/**
	 * Fügt eine kosmetische Linie hinzu
	 * 
	 * @param p0x	X des ersten Punkts
	 * @param p0y	Y des ersten Punkts
	 * @param p1x	X des zweiten Punkts
	 * @param p1y	Y des zweiten Punkts
	 */
	protected void addLine(float p0x, float p0y, float p1x, float p1y) {
		float[] pArr = new float[] {p0x, p0y, p1x, p1y};
		
		addLine(pArr);
	}
	
	/**
	 * Fügt eine kosmetische Linie hinzu
	 * 
	 * @param p0	Erster Punkt
	 * @param p1	Zweiter Punkt
	 */
	protected void addLine(Vector2 p0, Vector2 p1) {
		float[] pArr = new float[] {p0.x, p0.y, p1.x, p1.y};
		
		addLine(pArr);
	}
	
	/**
	 * Fügt eine kosmetische Linie zur Karte hinzu
	 * 
	 * @param pArr	Punkte der Linie {p0x, p0y, p1x, p1y}
	 */
	protected void addLine(float[] pArr) {	// pArr --> [p0x, p0y, p1x, p1y]
		if(pArr.length != 4) return;
		
		decoLines.add(pArr);
	}
	
	/**
	 * Entfernt eine kosmetische Linie
	 * 
	 * @param index	Index der Linie im {@link #decoLines} Array
	 */
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
	
	/**
	 * Sucht eine Route zwischen zwei Knoten mittels Tiefensuche.
	 * 
	 * @param start	Anfangsknoten
	 * @param target	Zielknoten
	 * @return	True wenn eine Route existiert
	 * @throws Exception 
	 */
	public boolean depthSearch(Country start, Country target) {
		
		for(Country i: getStrToCountry().values()) {
			i.setFlag(false);
		}
		
		return depthSearchRec(start, target);
	}
	
	private boolean depthSearchRec(Country start, Country target) {	
		start.setFlag(true);
		if(start == target) return true;

		for(Country i: movement.get(start.getName())) {
			if(i.getFlag() == false && depthSearchRec(i, target)) return true;
		}
		
		return false;
	}
}
