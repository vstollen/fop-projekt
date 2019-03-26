package game.players;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;

import game.Game;
import game.GameConstants;
import game.Joker;
import game.jokers.TroopbonusJoker;

public class JokerAI extends CustomAI {

	public JokerAI(String name, Color color) {
		super(name, color);
	}
	
	@Override
	protected void actions(Game game) throws InterruptedException {
		
		useJokers(game);
		
		super.actions(game);
	}
	
	private void useJokers(Game game) {
		Collection<Joker> usableJokers = getUsableJokers();
		
		for (Joker joker : usableJokers) {
			if (joker instanceof TroopbonusJoker) {
				if (getCastles(game).size() < 3) {
					joker.invoke();
					game.logIfPossible(joker.getLogMessage(), this);
				}
			}
		}
	}
	
	private Collection<Joker> getUsableJokers() {
		LinkedList<Joker> usableJokers = new LinkedList<>();
		
		for (Joker joker : GameConstants.JOKERS) {
			if (joker.isUsable()) {
				usableJokers.add(joker);
			}
		}
		
		return usableJokers;
	}

}
