package io.Risiko.Utils;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ShortArray;

public class PolygonTriangulated{	// --> Umgang mit Polygons in der Grafik vereinfachen
	
	private Polygon poly;
	private ShortArray triangulationRaw;
	private float[] verticesPolyRaw;
	private float[][] verticesPoly;
	private int[][] triangles;
	
	private float boundPad;
	private Rectangle boundRect;
	
	public PolygonTriangulated() {
		boundPad = 7;
	}	// leerer Konstruktor aufgrund von json-Serialisierung nötig
	
	public PolygonTriangulated(float[] verticesPolyIn) {
		boundPad = 7;
		setPolygon(new Polygon(verticesPolyIn));
	}
	
	public PolygonTriangulated(Polygon polyIn) {
		boundPad = 7;	
		setPolygon(polyIn);
	}
	
	public Polygon getPolygon() {
		return poly;
	}
	
	public ShortArray getTrianglesRaw() {
		return triangulationRaw;
	}
	
	public int[][] getTriangles() {
		return triangles;
	}
	
	public float[] getVerticesRaw() {
		return verticesPolyRaw;
	}
	
	public float[][] getVerticesFormatted() {
		return verticesPoly;
	}
	
	public void recalculateVerticesFormatted() {
		// vertN: welcher Vertex; verticesPoly[vertN][0] == vertN.x | verticesPoly[vertN][1] == vertN.y
		verticesPolyRaw = poly.getTransformedVertices();
		float[][] vertTemp = new float[verticesPolyRaw.length/2][2];
		for(int i = 0, x = 0; i < verticesPolyRaw.length; i += 2, x++) {
			vertTemp[x][0] = verticesPolyRaw[i];
			vertTemp[x][1] = verticesPolyRaw[i+1];
		}
		verticesPoly = vertTemp;
	}
	
	public void setPolygon(Polygon polyIn) {
		EarClippingTriangulator earClipper = new EarClippingTriangulator();
		poly = polyIn;
		triangulationRaw = earClipper.computeTriangles(poly.getTransformedVertices());
		
		recalculateVerticesFormatted();
		
		// triangleN: welches Dreieck; triangles[triangleN][0] == triangle.vert0Index | triangles[triangleN][1] == triangle.vert1Index | triangles[triangleN][2] == triangle.vert2Index
		// Beispiel:
		// triangle0:	p2		|	p0.x == vertices[ triangles[0][0] ][0]  |	p0.y == vertices[ triangles[0][0] ][1]
		//				| \		|	p1.x == vertices[ triangles[0][1] ][0]	|	p1.y == vertices[ triangles[0][1] ][1]
		//				p0-p1	|	p2.x == vertices[ triangles[0][2] ][0]	|	p2.y == vertices[ triangles[0][2] ][1]
		
		int[][] trianglesTemp = new int[triangulationRaw.size/3][3];
		for(int i = 0, x = 0; i < triangulationRaw.size; i += 3, x++) {
			trianglesTemp[x][0] = triangulationRaw.get(i);
			trianglesTemp[x][1] = triangulationRaw.get(i+1);
			trianglesTemp[x][2] = triangulationRaw.get(i+2);
		}
		triangles = trianglesTemp;
		
		makeBoundRect();
	}
	
	public void setBoundingPadding(float boundPadIn) {
		boundPad = boundPadIn;
	}
	
	private void makeBoundRect() {
		boundRect = new Rectangle(poly.getBoundingRectangle());
		
		float x = boundRect.x - boundPad;
		float y = boundRect.y - boundPad;
		
		float width = boundRect.width + boundPad*2;
		float height = boundRect.height + boundPad*2;

		boundRect.set(x, y, width, height);
	}
	
	public Rectangle getBoundingRect() {
		return boundRect;
	}
}
