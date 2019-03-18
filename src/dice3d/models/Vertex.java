package dice3d.models;

import java.awt.Graphics2D;

import dice3d.main.World;
import dice3d.math.Vector;

public class Vertex {

	public Vector position = new Vector();
	public Vector positionReset = new Vector();
	public Vector velocityReset = new Vector();

	public String id;
	
	final private double gravity = 9.81;
	
	private Vector previousPosition = null;
	private int time = 0;
	private double speed = 0.01;

	public Vertex(double x, double y, double z) {
		position.set(x, y, z);
		positionReset.set(position);
		
		velocityReset.set(0, 0, 0);
	}
	

	public void reset() {
		position.set(positionReset);
		time = 0;
	}

	public void update() {
		time++;
		previousPosition = position;
		position = getPosition();
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

	/**
	 * Berechnet die Position des Vertex für den aktuellen Zeitpunkt
	 * @return die neue Position des Vertex
	 */
	private Vector getPosition() {
		if (time == 1) {
			return getFirstPosition();
		} else {
			return getNextPosition();
		}
	}
	
	/**
	 * Berechnet die Position des Vertex zum Zeitpunkt t=1
	 * @return Die Position des Vertex bei t=1
	 */
	private Vector getFirstPosition() {
		Vector newPosition = new Vector(positionReset);
		newPosition.add(velocityReset);
		
		Vector acceleration = new Vector(0, gravity, 0);
		acceleration.scale(0.5);
		
		newPosition.add(acceleration);
		
		return newPosition;
	}
	
	/**
	 * Berechnet die neue Position des Vertex zum Zeitpunkt time
	 * @return Die neue Position des Vertex
	 */
	private Vector getNextPosition() {
		Vector newPosition = new Vector(position);
		newPosition.scale(2);
		
		newPosition.sub(previousPosition);
		
		Vector acceleration = new Vector(0, gravity, 0);
		// a * t^2
		acceleration.scale(Math.pow(speed * time, 2));
		
		newPosition.add(acceleration);
		
		return newPosition;
		
		
	}
}
