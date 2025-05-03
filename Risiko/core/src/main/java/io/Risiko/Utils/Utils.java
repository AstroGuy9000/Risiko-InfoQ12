package io.Risiko.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import io.Risiko.Main;

public class Utils {
	
	private Utils() {};
	
	static public Vector2 vec2unproject(Camera camIn, Vector2 vec2) {
		Vector3 vec3Temp = camIn.unproject(new Vector3(
				vec2.x,
				vec2.y,
				0));
		vec2 = new Vector2(vec3Temp.x, vec3Temp.y);
		return vec2;
	}
	
	static public void preparePoly(Polygon poly, float posX, float posY) {
		poly.setPosition(posX, posY);
		Vector2 vecTemp1 = new Vector2(0f, 0f);
		poly.setOrigin(
				poly.getCentroid(vecTemp1).x-poly.getX(),
				poly.getCentroid(vecTemp1).y-poly.getY());
	}
	
	static public Color rgba(int r, int g, int b, float a) {	// von hier Farben nehmen https://www.c64-wiki.de/wiki/Farbe
		float rNew = r/255f;
		float gNew = g/255f;
		float bNew = b/255f;
		return new Color(rNew, gNew, bNew, a);
	}
	
	static public void drawPolygonOutline(ShapeRenderer shRend, PolygonTriangulated poly, float width, Color color) {
		
		float[][] verticesPoly = poly.getVerticesFormatted();
		
		shRend.setColor(color);
		for (int i = 0; i+1 < verticesPoly.length; i++) {
			
			float p0x = verticesPoly[i][0];
			float p0y = verticesPoly[i][1];
			
			float p1x = verticesPoly[i+1][0];
			float p1y = verticesPoly[i+1][1];
			
			shRend.circle(p0x, p0y, width/2, 50);
			shRend.rectLine(p0x,p0y,p1x,p1y,width);
			
		}
		
		float p0x = verticesPoly[0][0];
		float p0y = verticesPoly[0][1];
		
		float p1x = verticesPoly[verticesPoly.length-1][0];
		float p1y = verticesPoly[verticesPoly.length-1][1];
		
		shRend.circle(p1x, p1y, width/2, 50);
		shRend.rectLine(p0x,p0y,p1x,p1y,width);
	}
	
	static public void drawPolygonFilled(ShapeRenderer shRend, PolygonTriangulated poly, Color color) {

		float[][] verticesPoly = poly.getVerticesFormatted();
		int[][] triangles = poly.getTriangles();
		
		shRend.setColor(color);
		for (int[] i : triangles) {

			float p0x = verticesPoly[i[0]][0];
			float p0y = verticesPoly[i[0]][1];

			float p1x = verticesPoly[i[1]][0];
			float p1y = verticesPoly[i[1]][1];

			float p2x = verticesPoly[i[2]][0];
			float p2y = verticesPoly[i[2]][1];

			shRend.triangle(p0x, p0y, p1x, p1y, p2x, p2y);
		}
	}
	
	static public void drawRoundedLine(ShapeRenderer shRend, Vector2 p0, Vector2 p1, float width, Color color) {
		drawRoundedLine(shRend, p0.x, p0.y, p1.x, p1.y, width, color);
	}
	
	static public void drawRoundedLine(ShapeRenderer shRend, float[] verts, float width, Color color) {
		
		if( !(verts.length == 4) ) {
			return;
		}
		
		drawRoundedLine(shRend, verts[0], verts[1], verts[2], verts[3], width, color);
	}
	
	static public void drawRoundedLine(ShapeRenderer shRend, float p0x, float p0y, float p1x, float p1y, float width, Color color) {
		
		shRend.setColor(color);
		
		shRend.circle(p0x, p0y, width/2, 50);
		shRend.rectLine(p0x, p0y, p1x, p1y, width);
		shRend.circle(p1x, p1y, width/2, 50);
	}
	
	static public void drawDebugRect(ShapeRenderer shRend, Rectangle rect, Color color) {
		
		shRend.setColor(color);
		
		float x = rect.x;
		float y = rect.y;
		
		float width = rect.width + x;
		float height = rect.height + y;
		
		shRend.line(x, y, x, height);
		shRend.line(x, height, width, height);
		shRend.line(width, height, width, y);
		shRend.line(width, y, x, y);
	}
	
	static public  ExtendViewport makeDefaultViewport() {
		OrthographicCamera cam = new OrthographicCamera();
		cam.position.set(Main.WORLD_HEIGHT / 2f, Main.WORLD_WIDTH / 2f, 0);
		cam.update();
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		return new ExtendViewport(Main.WORLD_HEIGHT, Main.WORLD_WIDTH * height/width, cam);
	}
	
	static public void resetDefaultCamPos(OrthographicCamera cam) {
		cam.position.set(Main.WORLD_HEIGHT / 2f, Main.WORLD_WIDTH / 2f, 0);
	}
	
	static public final class ColorsC64 {
		public static final Color BLUE = rgba(0, 0, 170, 1);
		public static final Color BROWN = rgba(102, 68, 0, 1);
		public static final Color YELLOW = rgba(238, 238, 119, 1);
		public static final Color GRAY1 = rgba(51, 51, 51, 1);
		public static final Color GRAY2 = rgba(119, 119, 119, 1);
		public static final Color GRAY3 = rgba(187, 187, 187, 1);
		public static final Color GREEN = rgba(0, 204, 85, 1);
		public static final Color LIGHT_BLUE = rgba(0, 136, 255, 1);
		public static final Color LIGHT_GREEN = rgba(170, 255, 102, 1);
		public static final Color LIGHT_RED = rgba(255, 119, 119, 1);
		public static final Color ORANGE = rgba(221, 136, 85, 1);
		public static final Color RED = rgba(136, 0, 0, 1);
		public static final Color BLACK = rgba(0, 0, 0, 1);
		public static final Color TURQUOISE = rgba(170, 255, 238, 1);
		public static final Color VIOLET = rgba(204, 68, 204, 1);
		public static final Color WHITE = rgba(255, 255, 255, 1);
	}
}
