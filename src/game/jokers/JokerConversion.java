package game.jokers;

import java.util.HashMap;

import game.Game;
import game.Joker;
import game.Player;

public class JokerConversion extends Joker {

	private int maxInvocationsPerPlayer;
	private HashMap<Player, Integer> invocationsLeft = new HashMap<>();
	
	private static final String hint = "Der nächste Angriff konvertiert die Burg des Gegners und bekehrt die darauf liegenden Truppen.";

	public JokerConversion() {
		this(1);
	}

	public JokerConversion(int invocations) {
		super("Konversion", hint);
		maxInvocationsPerPlayer = invocations;
	}

	@Override
	public boolean isUsable() {
		Game game = getGame();
		
		if (!game.allCastlesChosen()) {
			return false;
		}
		
		Player currentPlayer = game.getCurrentPlayer();
		if (!invocationsLeft.containsKey(currentPlayer)) {
			invocationsLeft.put(currentPlayer, maxInvocationsPerPlayer);
		}
		
		return invocationsLeft.get(currentPlayer) > 0;
	}

	@Override
	public void invoke() {
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();
		
		currentPlayer.setInstantWin(true);
		
		int playerInvocationsLeft = invocationsLeft.get(currentPlayer) - 1;
		invocationsLeft.put(currentPlayer, playerInvocationsLeft);
	}

}
