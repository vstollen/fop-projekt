package game.jokers;

import game.Joker;

public class JokerDummy extends Joker {

	private boolean isUsed = false;
	
	public JokerDummy(String name, String hint) {
		super(name, hint);
	}

	@Override
	public boolean isUsable() {
		return !isUsed;
	}

	@Override
	public void onInvocation() {
		System.out.println("Joker " + getName() + " genutzt.");
		isUsed = true;
	}

}
