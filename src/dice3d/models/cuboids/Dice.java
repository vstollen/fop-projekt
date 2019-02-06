package dice3d.models.cuboids;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import dice3d.main.World;
import dice3d.models.Vertex;

public class Dice extends Cube {


	private Vertex[][] face = new Vertex[6][4];


	public Dice(double x, double y, double z, int size) {
		super(x,y,z, size);

		face[0][0] = vertices.get(0); // a 
		face[0][1] = vertices.get(1); // b
		face[0][2] = vertices.get(2); // c ...
		face[0][3] = vertices.get(3);

		face[1][0] = vertices.get(1);
		face[1][1] = vertices.get(5);
		face[1][2] = vertices.get(6);
		face[1][3] = vertices.get(2);

		face[2][0] = vertices.get(5);
		face[2][1] = vertices.get(4);
		face[2][2] = vertices.get(7);
		face[2][3] = vertices.get(6);

		face[3][0] = vertices.get(4);
		face[3][1] = vertices.get(0);
		face[3][2] = vertices.get(3);
		face[3][3] = vertices.get(7);

		face[4][0] = vertices.get(3);
		face[4][1] = vertices.get(2);
		face[4][2] = vertices.get(6);
		face[4][3] = vertices.get(7);

		face[5][0] = vertices.get(1);
		face[5][1] = vertices.get(0);
		face[5][2] = vertices.get(4);
		face[5][3] = vertices.get(5);
	}


	public void draw(Graphics2D g) {
		drawDice(g);
		// draw all edges, even inner ones
		// for(Edge e: edges) {
		// e.draw(g);
		// }

		// for (Vertex point : vertices) {
		// point.draw(g);
		// }
	}

	public int getNumberRolled() {
		return 1;
	}

	private void drawDice(Graphics2D g) {
		for (int f = 0; f < 6; f++) {
			// each face as polygon
			Polygon polygon = new Polygon();

			for (int v = 0; v < 4; v++) {
				Vertex vertex = face[f][v];
				double vx = World.projectionDistance * vertex.position.x / vertex.position.z;
				double vy = World.projectionDistance * vertex.position.y / vertex.position.z;

				polygon.addPoint((int) vx, (int) vy);
			}

			// back-face culling
			double v1x = polygon.xpoints[0] - polygon.xpoints[1];
			double v1y = polygon.ypoints[0] - polygon.ypoints[1];
			double v2x = polygon.xpoints[2] - polygon.xpoints[1];
			double v2y = polygon.ypoints[2] - polygon.ypoints[1];
			double visible = v1x * v2y - v1y * v2x;
			if (visible < 0) {
				g.setColor(Color.WHITE);
				if(collided) {
					g.setColor(Color.RED);
				}
				g.fill(polygon);
				g.setColor(Color.BLACK);
				g.draw(polygon);

				// draw number of each face (that is visible)
				// calc center of each face/square
				double xs = 0d;
				double ys = 0d;

				for (int i = 0; i < 4; i++) {
					xs += polygon.xpoints[i];
					ys += polygon.ypoints[i];
				}
				xs = xs / 4;
				ys = ys / 4;
				g.drawString(f + 1 + "", (int) xs, (int) ys);
			}
		}

	}

}
