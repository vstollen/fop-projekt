package game.gameExceptions;

import game.map.Castle;

/**
 * Eine Exception die genutzt werden kann, falls ein Spieler schon eine Flagburg besitzt
 * 
 * @author Felix Graner
 *
 */
public class hasFlagCastleException extends Exception {
	
	static final long serialVersionUID = 1L;

	public hasFlagCastleException(Castle castle) {
		super("Dieser Spieler hat schon die Flagburg " + castle.getName());
	}

}
