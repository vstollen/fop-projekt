package game.jokers;

import game.Joker;

public class SkipPlayerJoker extends Joker {

	public SkipPlayerJoker() {
		super("Aussetzen", "Lässt einen Gegner deiner Wahl eine Runde aussetzen.");
	}
	
	@Override
	public boolean isUsable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invoke() {
		// TODO Auto-generated method stub

	}

}
