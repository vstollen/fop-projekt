package game.jokers;

import game.Joker;

public class JokerDummy extends Joker {

	public JokerDummy(String name) {
		super(name);
	}

	@Override
	public boolean isUsable() {
		return true;
	}

	@Override
	public void onInvocation() {
		System.out.println("Joker genutzt.");
	}

}
