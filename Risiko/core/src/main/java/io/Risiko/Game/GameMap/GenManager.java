package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GenManager {
	
	private Country workingCountry;
	
	private int workingLineIndex;
	
	private TravelNetwork travel;
	
	private ArrayList<Country> selection;

	protected GenManager() {
		travel = new TravelNetwork();
		
		Country workingElementIn = new Country();
		workingCountry = workingElementIn;
		
		selection = new ArrayList<Country>();
		
		workingLineIndex = -1;
	}
	
	protected Country getWorkingCountry() {
		if(workingCountry != null) return workingCountry;
		setWorkingCountry(new Country());
		return workingCountry;
	}
	
	protected void setWorkingCountry(Country element) {
		workingCountry = element;
	}
	
	protected int getWorkingLineIndex() {
		return workingLineIndex;
	}
	
	protected void setWorkingLineIndex(int workingLineIndexIn) {
		workingLineIndex = workingLineIndexIn;
	}
	
	protected void nextLine() {
		if(travel.getDecoLines().isEmpty()) {
			workingLineIndex = -1;
			return;
		}
		
		if(workingLineIndex == -1 && !travel.getDecoLines().isEmpty()) {
			workingLineIndex = 0;
			return;
		}
		
		if(workingLineIndex + 1 >= travel.getDecoLines().size()) {
			workingLineIndex = 0;
		} else {
			workingLineIndex ++;
		}
	}
	
	protected void addLine(Vector2 p1, Vector2 p2) {
		travel.addLine(p2, p1);
	}
	
	protected void removeLine(int index) {
		if(index < 0 || index >= travel.getDecoLines().size()) return;
		travel.removeLine(index);
		workingLineIndex = -1;
	}
	
	protected HashMap<String, Continent> getContinents() {
		return travel.getStrToCont();
	}
	
	protected void addContinent(Continent cont) {
		travel.addContinent(cont);
	}
	
	// Wird nur gerufen wenn schon geprüft wurde ob der Name frei ist
	protected void renameCont(Continent cont, String newName) {
		travel.renameCont(cont, newName);
	}
	
	protected void removeContinent(Continent cont) {
		travel.removeContinent(cont);
	}
	
	protected Collection<Country> getCountries() {
		return travel.getStrToCountry().values();
	}

	protected void addCountry(Country element) {
		if(travel.getStrToCountry().values().contains(element)) return;
		
		travel.addCountry(element);
	}
	
	protected void renameCountry(Country country, String newName) {
		travel.renameCountry(country, newName);
	}
	
	protected void removeCountry(Country country) {	
		travel.removeCountry(country);
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
	
	public TravelNetworkSave saveModel() {
		return new TravelNetworkSave(travel);
	}
	
	protected void loadModel(TravelNetworkSave travelSave) {
		travel = travelSave.rebuildTravelNetwork();
		
		getWorkingCountry();
	}
	
	protected void putTravelNetwork(TravelNetwork travelIn) {
		travel = travelIn;
		
		getWorkingCountry();
	}
}
