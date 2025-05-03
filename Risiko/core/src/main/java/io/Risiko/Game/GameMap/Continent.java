package io.Risiko.Game.GameMap;

import com.badlogic.gdx.graphics.Color;

public class Continent {

	private String name;
	private int bonus;
	private Color color;
	
	protected Continent() {
		name = "Name";
		bonus = 1;
		color = Color.PINK;
	}
	
	protected Continent(String nameIn, int bonusIn, Color colorIn) {
		name = nameIn;
		bonus = bonusIn;
		color = colorIn;
	}
	
	public String getName() {
		return name;
	}
	
	protected void setName(String nameIn) {
		name = nameIn;
	}

	public int getBonus() {
		return bonus;
	}

	protected void setBonus(int bonusIn) {
		bonus = bonusIn;
	}

	public Color getColor() {
		return color;
	}

	protected void setColor(Color colorIn) {
		color = colorIn;
	}
	
	
}
