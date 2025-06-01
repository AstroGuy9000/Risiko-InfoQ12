package io.Risiko.Game.GameMap;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import io.Risiko.Game.GameMain.Player;
import io.Risiko.Utils.PolygonTriangulated;

public class Country {

	private String name;
	
	private ArrayList<Float> vertsList;
	
	private PolygonTriangulated polyFull;
	
	private boolean drawReady;
	
	private Player occupant;
	
	protected Country() {
		vertsList = new ArrayList<Float>();
	}
	
	protected Country(String nameIn, Continent contIn, ArrayList<Float> vertsListIn) {;
	
		name = nameIn;
	
		vertsList = vertsListIn;
	
		makePoly();
	}
	
	protected Country(String nameIn, Continent contIn, PolygonTriangulated polyFullIn) {;
		
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
	
	public void setOccupant(Player occupantIn) {
		occupant = occupantIn;
	}
	
	public Player getOccupant() {
		return occupant;
	}
}
