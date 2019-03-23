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
	
	protected Game getGame() {
		return game;
	}
}
