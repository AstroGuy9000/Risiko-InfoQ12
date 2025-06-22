package io.Risiko.Game.GameMap;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Utils.PolygonTriangulated;

/**
 * Enthält die Daten eines Landes 
 */
public class Country {

	private String name;
	
	private ArrayList<Float> vertsList;
	
	private PolygonTriangulated polyFull;
	
	private boolean drawReady;
	
	private boolean flag;
	
	private Player occupant;
	private int troops;
	
	protected Country() {
		vertsList = new ArrayList<Float>();
		troops = 1;
	}
	
	protected Country(String nameIn, ArrayList<Float> vertsListIn) {;
	
		name = nameIn;
	
		vertsList = vertsListIn;
	
		makePoly();
		
		troops = 1;
	}
	
	protected Country(String nameIn, PolygonTriangulated polyFullIn) {;
		
		name = nameIn;
		
		vertsList = new ArrayList<Float>();
		for(float i: polyFullIn.getVerticesRaw()) {
			vertsList.add(i);
		}
		
		polyFull = polyFullIn;
	}
	
	public String getName() {
		return name;
	}
	
	protected void setName(String nameIn) {
		name = nameIn;
	}
	
	public PolygonTriangulated getPolyFull() {
		return polyFull;
	}
	
	protected void setPolyFull(PolygonTriangulated polyFullIn) {
		vertsList = new ArrayList<Float>();
		for(float i: polyFullIn.getVerticesRaw()) {
			vertsList.add(i);
		}
		
		makePoly();
	}
	
	protected void nukeVerts() {
		vertsList = new ArrayList<Float>();
		polyFull = null;
		
		drawReady = false;
	}
	
	public ArrayList<Float> getVertsList() {
		return vertsList;
	}
	
	protected void addVert(Vector2 mouseDown) {
		vertsList.add(mouseDown.x);
		vertsList.add(mouseDown.y);
		makePoly();
	}
	
	protected void undoVert() {
		if(vertsList.size() <= 2) {
			nukeVerts();
			drawReady = false;
			return;
		}
		vertsList.removeLast();
		vertsList.removeLast();
		makePoly();
	}
	
	private void makePoly() {
		if(vertsList == null || vertsList.size() < 6) {
			drawReady = false;
			return;
		}
		
		float[] verts = new float[vertsList.size()];
		for(int i = 0; i < verts.length; i++) {
			verts[i] = vertsList.get(i);
		}
		polyFull = new PolygonTriangulated(verts);
		drawReady = true;
	}
	
	public boolean isDrawReady() {
		makePoly();
		return drawReady;
	}
	
	public boolean getFlag() {
		return flag;
	}
	
	public void setFlag(boolean flagIn) {
		flag = flagIn;
	}
	
	public Player getOccupant() {
		return occupant;
	}
	
	public void setOccupant(Player occupantIn) {
		if(!occupantIn.getCountries().contains(this)) {
			occupantIn.addCountry(this);
			return;
		}
		if(occupantIn != occupant && occupant != null) occupant.removeCountry(this);
		occupant = occupantIn;
	}
	
	public int getTroops() {
		return troops;
	}
	
	public void addTroops(int n) {
		troops = troops + n;
	}
	
	public void setTroops(int n) {
		troops = n;
		if(troops <= 0) troops = 1;
	}
}
