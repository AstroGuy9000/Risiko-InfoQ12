package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class GenManager {
	
	private Country workingCountry;
	
	private HashMap<String, Continent> conts;
	
	private ArrayList<Country> countries;
	private TravelNetwork travel;

	protected GenManager() {
		conts = new HashMap<String, Continent>();
		countries = new ArrayList<Country>();
		travel = new TravelNetwork(new HashMap<String,Country>() ,new HashMap<String,HashSet<Country>>(), new HashMap<String,HashSet<Country>>());
		
		Country workingElementIn = new Country();
		workingCountry = workingElementIn;
	}
	
	protected GenManager(ModelSaveData saveData) {
		conts = saveData.getConts();
		countries = saveData.getCountries();
		travel = saveData.getTravel();
		
		getWorkingCountry();
	}
	
	protected GenManager(HashMap<String, Continent> contsIn, ArrayList<Country> elementsIn) {
		conts = contsIn;
		countries = elementsIn;
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
	
	protected void changeContName(Continent cont, String newName) {
		if(conts.get(cont.getName()) == null) return;
		
		conts.remove(cont.getName());
		cont.setName(newName);
		conts.putIfAbsent(newName, cont);
	}
	
	protected void changeContName(String oldName, String newName) {
		Continent cont = conts.get(oldName);
		if(cont == null) return;
		
		conts.remove(oldName);
		cont.setName(newName);
		conts.putIfAbsent(newName, cont);
	}
	
	protected void removeContinent(Continent cont) {
		for(Country i: countries) {
			if(i.getCont() == cont) return;
		}
		
		conts.remove(cont.getName());
	}
	
	protected void nukeCont(Continent cont) {
		if(!conts.containsKey(cont.getName())) {
			System.out.println("oopsie");
			return;
		}
		
		for(Country i: countries) {
			if(i.getCont() == cont) removeCountry(i);
		}
		
		conts.remove(cont.getName(), cont);
		
		System.out.println(conts.size());
	}
	
	protected ArrayList<Country> getCountries() {
		return countries;
	}

	protected void addCountry(Country element) {
		if(countries.contains(element)) return;
		
		travel.addCountry(element);
		countries.add(element);
	}
	
	protected void removeCountry(Country element) {
		if(!countries.contains(element)) return;
		
		travel.removeCountry(element);
		countries.remove(element);
	}
	
	protected TravelNetwork getTravel() {
		return travel;
	}
	
	protected void autoAddRoutes() {
		Rectangle rectA;
		Rectangle rectB;
		
		ArrayList<Country> areDrawReady = new ArrayList<Country>(countries);
		
		for(Country i: countries) {
			if(!i.isDrawReady()) areDrawReady.remove(i);
		}
		
		for(int i = 0; i < areDrawReady.size(); i++) {
			rectA = areDrawReady.get(i).getPolyFull().getBoundingRect();
			for(int x = i+1; x < areDrawReady.size(); x++) {
				rectB = areDrawReady.get(x).getPolyFull().getBoundingRect();
				
				boolean temp = rectA.overlaps(rectB);
				
				if(temp) travel.addRoute(countries.get(i), countries.get(x));
			}
		}
	}
	
	public ModelSaveData saveModel() {
		return new ModelSaveData(conts, travel);
	}
	
	protected void loadModel(ModelSaveData saveData) {
		conts = saveData.getConts();
		countries = saveData.getCountries();
		travel = saveData.getTravel();
		
		getWorkingCountry();
	}
}
