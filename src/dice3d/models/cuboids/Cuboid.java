package dice3d.models.cuboids;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import dice3d.math.Vector;
import dice3d.models.Edge;
import dice3d.models.Vertex;

public class Cuboid {

	public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();

	
	public boolean collided = false;

	public Cuboid(double x, double y, double z, int length, int width, int height) {
		
		Vector corner = new Vector(x, y, z);

		Vertex a = new Vertex(corner.x, corner.y, corner.z);
		Vertex b = new Vertex(corner.x + length, corner.y, corner.z);
		Vertex c = new Vertex(corner.x + length, corner.y + height, corner.z);
		Vertex d = new Vertex(corner.x, corner.y + height, corner.z);

		Vertex e = new Vertex(corner.x, corner.y, corner.z + width);
		Vertex f = new Vertex(corner.x + length, corner.y, corner.z + width);
		Vertex g = new Vertex(corner.x + length, corner.y + height, corner.z + width);
		Vertex h = new Vertex(corner.x, corner.y + height, corner.z + width);

		a.id = "A";
		b.id = "B";
		c.id = "C";
		d.id = "D";
		e.id = "E";
		f.id = "F";
		g.id = "G";
		h.id = "H";

		vertices.add(a);
		vertices.add(b);
		vertices.add(c);
		vertices.add(d);

		vertices.add(e);
		vertices.add(f);
		vertices.add(g);
		vertices.add(h);

		Edge edge01 = new Edge(a, b);
		Edge edge02 = new Edge(b, c);
		Edge edge03 = new Edge(c, d);
		Edge edge04 = new Edge(d, a);

		Edge edge05 = new Edge(e, f);
		Edge edge06 = new Edge(f, g);
		Edge edge07 = new Edge(g, h);
		Edge edge08 = new Edge(h, e);

		Edge edge09 = new Edge(a, e);
		Edge edge10 = new Edge(b, f);
		Edge edge11 = new Edge(c, g);
		Edge edge12 = new Edge(d, h);

		Edge edge13 = new Edge(a, g);
		Edge edge14 = new Edge(b, h);
		Edge edge15 = new Edge(c, e);
		Edge edge16 = new Edge(d, f);

		Edge edge17 = new Edge(a, c);
		Edge edge18 = new Edge(b, g);
		Edge edge19 = new Edge(f, h);
		Edge edge20 = new Edge(d, e);
		Edge edge21 = new Edge(d, g);
		Edge edge22 = new Edge(a, f);

		edges.add(edge01);
		edges.add(edge02);
		edges.add(edge03);
		edges.add(edge04);
		edges.add(edge05);
		edges.add(edge06);
		edges.add(edge07);
		edges.add(edge08);
		edges.add(edge09);
		edges.add(edge10);
		edges.add(edge11);
		edges.add(edge12);

		edges.add(edge13);
		edges.add(edge14);
		edges.add(edge15);
		edges.add(edge16);

		edges.add(edge17);
		edges.add(edge18);
		edges.add(edge19);
		edges.add(edge20);
		edges.add(edge21);
		edges.add(edge22);

	}

	public void update() {
		for (Vertex v : vertices) {
			v.update();
		}
		for (Edge e : edges) {
			e.update();
		}

	}

	public void reset() {
		for (Vertex v : vertices) {
			v.reset();
		}
	}

	public void draw(Graphics2D g) {
		// draw all edges, even inner ones
		g.setColor(Color.GREEN);
		
		if(collided) {
			g.setColor(Color.RED);
		}
		 for(Edge e: edges) {
		 e.draw(g);
		 }
		 
		 g.setColor(Color.BLACK);
		 for (Vertex point : vertices) {
		 point.draw(g);
		 }
	}

	public boolean notMoving() {
		return false;
	}
	
	public void updateCollision(Cuboid d) {
		collided = false;
	}


}
