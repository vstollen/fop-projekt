package game;

import gui.components.MapPanel;

public abstract class Joker {
	
	private Game game;
	private final String name;
	private String hint;
	private MapPanel map;
	
	public Joker(String name, String hint) {
		this.name = name;
		this.hint = hint;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	/**
	 * Gibt zurück, ob der Joker aktuell nutzbar ist
	 * @return true wenn der Joker nutzbar ist
	 */
	public abstract boolean isUsable();
	
	/**
	 * Wird ausgeführt, wenn der Joker ausgewählt wird
	 */
	public abstract void invoke();
	
	/**
	 * Wird nach größeren Ereignissen im Spiel ausgeführt (Nach Angriffen, etc.)
	 */
	public void update() {
		
	}
	
	/**
	 * Gibt zurück, ob der aktuelle Zug übersprungen werden sollte
	 * @return true, wenn der aktuelle Zug übersprungen werden sollte
	 */
	public boolean shouldSkipTurn() {
		return false;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getHint() {
		return hint;
	}
	
	/**
	 * Gibt eine Log Nachricht für den Joker aus
	 * @return passende Log Nachricht
	 */
	public String getLogMessage() {
		// %%PLAYER%% wird von der logLine() Methode durch den passenden Spieler ersetzt
		return String.format("%%PLAYER%% hat den Joker %s ausgelöst.", name);
	}
	
	protected Game getGame() {
		return game;
	}
	
	public void setMapPanel(MapPanel map) {
		this.map = map;
	}
}
