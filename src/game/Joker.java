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

	public abstract boolean isActive();
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
