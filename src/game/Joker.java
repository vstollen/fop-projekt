package game;

public abstract class Joker {
	
	private Game game;
	private final String name;
	
	public Joker(String name) {
		this.name = name;
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
	
	protected Game getGame() {
		return game;
	}
}
