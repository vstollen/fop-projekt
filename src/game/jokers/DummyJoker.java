package game.jokers;

import game.Joker;

public class DummyJoker extends Joker {

	private boolean isUsed = false;
	
	public DummyJoker(String name, String hint) {
		super(name, hint);
	}

	@Override
	public boolean isUsable() {
		return !isUsed;
	}

	@Override
	public void invoke() {
		System.out.println("Joker " + getName() + " genutzt.");
		isUsed = true;
	}

}
