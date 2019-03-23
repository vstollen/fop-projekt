package game;

public abstract class Joker {
	
	private Game game;
	private final String name;
	private String hint;
	
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
	
	public final String getName() {
		return name;
	}
	
	public final String getHint() {
		return hint;
	}
	
	protected Game getGame() {
		return game;
	}
}
