package dice3d.models;

import java.awt.Graphics2D;

import dice3d.main.World;
import dice3d.math.Vector;

public class Vertex {

	public Vector position = new Vector();
	public Vector positionReset = new Vector();

	public String id;

	public Vertex(double x, double y, double z) {
		position.set(x, y, z);
		positionReset.set(position);
	}
	

	public void reset() {
		position.set(positionReset);
	}

	public void update() {
		
	}

	public void draw(Graphics2D g) {
		double px = World.projectionDistance * position.x / position.z;
		double py = World.projectionDistance * position.y / position.z;
		if (id == null) {
			int size = 6;
			g.fillOval((int) (px - (size / 2)), (int) (py - (size / 2)), size, size);
		} else {
			g.drawString(id, (int)px, (int)py);
		}
	}

}
