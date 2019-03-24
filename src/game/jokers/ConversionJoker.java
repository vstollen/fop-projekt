package game.jokers;

import java.util.HashMap;

import game.Game;
import game.Joker;
import game.Player;

public class ConversionJoker extends Joker {

	private int maxInvocationsPerPlayer;
	private HashMap<Player, Integer> invocationsLeft = new HashMap<>();
	
	private static final String hint = "Der nächste Angriff konvertiert die Burg des Gegners und bekehrt die darauf liegenden Truppen.";

	public ConversionJoker() {
		this(1);
	}

	public ConversionJoker(int invocations) {
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
		
		if (currentPlayer.isInstantAttackWin()) {
			return false;
		}
		
		return invocationsLeft.get(currentPlayer) > 0;
	}

	@Override
	public void invoke() {
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();
		
		currentPlayer.setInstantAttackWin(true);
		
		int playerInvocationsLeft = invocationsLeft.get(currentPlayer) - 1;
		invocationsLeft.put(currentPlayer, playerInvocationsLeft);
	}

}
