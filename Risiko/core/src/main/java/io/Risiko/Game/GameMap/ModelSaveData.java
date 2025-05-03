package io.Risiko.Game.GameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import io.Risiko.Utils.PolygonTriangulated;

public class ModelSaveData {

	private HashMap<String, Continent> conts;
	private TravelNetwork travel;
	
	public ModelSaveData() {}
	
	public ModelSaveData(HashMap<String, Continent> contsIn,TravelNetwork travelIn) {
		conts = contsIn;
		travel = travelIn;
	}
	
	public HashMap<String, Continent> getConts() {
		return conts;
	}
	
	public ArrayList<Country> getCountries() {
		
		ArrayList<Country> arrOut = new ArrayList<Country>();
		
		Collection<Country> transToArr = travel.getStrToCountry().values();
		for(Country i: transToArr) {
			arrOut.add(i);
		}
		
		return arrOut;
	}
	
	public TravelNetwork getTravel() {
		return travel;
	}
}
