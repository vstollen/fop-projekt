package dice3d.models;

import java.awt.Graphics2D;

import dice3d.main.World;

public class Edge {

	private Vertex a;
	private Vertex b;


	public Edge(Vertex a, Vertex b) {
		this.a = a;
		this.b = b;
	}

	public void update() {
	
	}

	public void draw(Graphics2D g) {
		double ax = World.projectionDistance * a.position.x / a.position.z;
		double ay = World.projectionDistance * a.position.y / a.position.z;
		double bx = World.projectionDistance * b.position.x / b.position.z;
		double by = World.projectionDistance * b.position.y / b.position.z;

		g.drawLine((int) ax, (int) ay, (int) bx, (int) by);
	}

}
