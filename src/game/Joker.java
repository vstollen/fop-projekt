package game;

public abstract class Joker {
	
	private Game game;
	private final String name;
	private final String hint;
	
	public Joker(String name, String hint) {
		this.name = name;
		this.hint = hint;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Gibt zurück, ob der Joker aktuell nutzbar ist
	 * @return true wenn der Joker nutzbar ist
	 */
	public abstract boolean isUsable();
	
	/**
	 * Wird ausgeführt, wenn der Joker ausgewählt wird
	 */
	public abstract void onInvocation();
	
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
}
