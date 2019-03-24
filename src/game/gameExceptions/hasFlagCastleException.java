package gameExceptions;

import game.map.Castle;

public class hasFlagCastleException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public hasFlagCastleException(Castle castle) {
		super("Dieser Spieler hat schon die Flagburg " + castle.getName());
	}

}
