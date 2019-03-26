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
	
	/**
	 * Nutzt intelligent verfügbare Joker
	 * @param game Das aktuelle Spiel
	 */
	private void useJokers(Game game) {
		Collection<Joker> usableJokers = getUsableJokers();
		
		for (Joker joker : usableJokers) {
			possiblyUseTroopbonus(joker, game);
		}
	}
	
	/**
	 * Prüft ob der Joker ein Truppenbonus Joker ist und setzt ihn gegebenenfalls ein,
	 * falls der Spieler weniger als 3 Burgen hält.
	 * @param joker Der Joker
	 * @param game Das aktuelle Spiel
	 */
	private void possiblyUseTroopbonus(Joker joker, Game game) {
		
		if (!(joker instanceof TroopbonusJoker)) {
			return;
		}
		
		if (getCastles(game).size() > 2) {
			return;
		}
		
		joker.invoke();
		game.logIfPossible(joker.getLogMessage(), this);
	}
	
	/**
	 * Bildet eine Liste aus allen aktuell verfügbaren Jokern
	 * @return alle aktuell verfügbaren Joker
	 */
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
