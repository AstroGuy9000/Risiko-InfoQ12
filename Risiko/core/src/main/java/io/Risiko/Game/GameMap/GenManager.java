package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class GenManager {
	
	private Country workingCountry;
	
	private HashMap<String, Continent> conts;
	
	private TravelNetwork travel;
	
	private ArrayList<Country> selection;

	protected GenManager() {
		conts = new HashMap<String, Continent>();
		travel = new TravelNetwork(new HashMap<String,Country>() ,new HashMap<String,HashSet<Country>>(), new HashMap<String,HashSet<Country>>());
		
		Country workingElementIn = new Country();
		workingCountry = workingElementIn;
		
		selection = new ArrayList<Country>();
	}
	
	protected GenManager(HashMap<String, Continent> contsIn, ArrayList<Country> elementsIn) {
		conts = contsIn;
	}
	
	protected Country getWorkingCountry() {
		if(workingCountry != null) return workingCountry;
		setWorkingCountry(new Country());
		return workingCountry;
	}
	
	protected void setWorkingCountry(Country element) {
		workingCountry = element;
	}
	
	protected HashMap<String, Continent> getContinents() {
		return conts;
	}
	
	protected void addContinent(String name, int bonus, Color color) {
		conts.putIfAbsent(name, new Continent(name, bonus, color));
	}
	
	protected void addContinent(Continent cont) {
		conts.putIfAbsent(cont.getName(), cont);
	}
	
	// Wird nur gerufen wenn schon geprüft wurde ob der Name frei ist
	protected void changeContName(Continent cont, String newName) {
		changeContName(cont.getName(), newName);
	}
	
	// Wird nur gerufen wenn schon geprüft wurde ob der Name frei ist
	protected void changeContName(String oldName, String newName) {
		Continent cont = conts.get(oldName);
		if(cont == null) return;
		
		conts.remove(oldName);
		cont.setName(newName);
		conts.putIfAbsent(newName, cont);
	}
	
	protected void removeContinent(Continent cont) {
		for(Country i: travel.getStrToCountry().values()) {
			if(i.getCont() == cont) return;
		}
		
		conts.remove(cont.getName());
	}
	
	protected void nukeCont(Continent cont) {
		if(!conts.containsKey(cont.getName())) {
			System.out.println("oopsie");
			return;
		}
		
		for(Country i: travel.getStrToCountry().values()) {
			if(i.getCont() == cont) removeCountry(i);
		}
		
		conts.remove(cont.getName(), cont);
		
		System.out.println(conts.size());
	}
	
	protected Collection<Country> getCountries() {
		return travel.getStrToCountry().values();
	}

	protected void addCountry(Country element) {
		if(travel.getStrToCountry().values().contains(element)) return;
		
		travel.addCountry(element);
	}
	
	// Wird nur gerufen wenn schon geprüft wurde ob der Name frei ist
	protected void changeCountryName(Country country, String newName) {
		changeCountryName(country.getName() , newName);
	}
	
	// Wird nur gerufen wenn schon geprüft wurde ob der Name frei ist
	protected void changeCountryName(String oldName, String newName) {
		
		System.out.println("Hello <3");
		
		Country country = travel.getStrToCountry().get(oldName);
		if(country == null) {
			System.out.println("Country is null :(");
			return;
		}
		
		HashSet<Country> temp = travel.getMovMap().get(oldName);
		travel.getMovMap().remove(oldName);
		travel.getMovMap().put(newName, temp);
		
		temp = travel.getDraw().get(oldName);
		travel.getDraw().remove(oldName);
		travel.getDraw().put(newName, temp);
		
		travel.getStrToCountry().remove(oldName);
		country.setName(newName);
		travel.getStrToCountry().put(newName, country);
		
		System.out.println("--- --- ---");
		System.out.println("Old Name:" + oldName);
		System.out.println("New Name:" + country.getName());
		System.out.println("--- --- ---");
	}
	
	protected void removeCountry(Country element) {
		if(!travel.getStrToCountry().values().contains(element)) return;
		
		travel.removeCountry(element);
	}
	
	protected TravelNetwork getTravel() {
		return travel;
	}
	
	protected void autoAddRoutes() {
		Rectangle rectA;
		Rectangle rectB;
		
		ArrayList<Country> areDrawReady = new ArrayList<Country>(travel.getStrToCountry().values());
		
		for(Country i: travel.getStrToCountry().values()) {
			if(!i.isDrawReady()) areDrawReady.remove(i);
		}
		
		for(int i = 0; i < areDrawReady.size(); i++) {
			rectA = areDrawReady.get(i).getPolyFull().getBoundingRect();
			for(int x = i+1; x < areDrawReady.size(); x++) {
				rectB = areDrawReady.get(x).getPolyFull().getBoundingRect();
				
				boolean temp = rectA.overlaps(rectB);
				
				if(temp) travel.addRoute(areDrawReady.get(i), areDrawReady.get(x));
			}
		}
	}
	
	public ArrayList<Country> getSelection() {
		return selection;
	}
	
	public void setSelection(ArrayList<Country> selectioIn) {
		selection = selectioIn;
	}
	
	public ModelSaveData saveModel() {
		return new ModelSaveData(conts, travel);
	}
	
	protected void loadModel(ModelSaveData saveData) {
		conts = saveData.getConts();
		travel = saveData.getTravel();
		
		getWorkingCountry();
	}
}
