package game.jokers;

import java.util.HashMap;

import game.Game;
import game.Joker;
import game.Player;

public class ConversionJoker extends Joker {

	private int maxInvocationsPerPlayer;
	private HashMap<Player, Integer> invocationsLeft = new HashMap<>();
	
	private static final String hintTemplate = "Verbleibend: %d\n-------------------\nDer nächste Angriff konvertiert die Burg des Gegners und bekehrt die darauf liegenden Truppen.\nNicht einsetzbar auf Hauptburgen im Capture the Flag-Modus.";

	public ConversionJoker() {
		this(1);
	}

	public ConversionJoker(int invocations) {
		super("Konversion", String.format(hintTemplate, 0));
		maxInvocationsPerPlayer = invocations;
	}

	/**
	 * Setze die Anzahl, wie oft ein Spieler diesen Joker einsetzen kann
	 * @param maxInvocationsPerPlayer die neue Anzahl maximaler Einsätze
	 */
	public void setMaxInvocations(int maxInvocationsPerPlayer) {
		this.maxInvocationsPerPlayer = maxInvocationsPerPlayer;
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

	@Override
	public void update() {
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();
		
		if (!invocationsLeft.containsKey(currentPlayer)) {
			invocationsLeft.put(currentPlayer, maxInvocationsPerPlayer);
		}
		
		setHint(String.format(hintTemplate, invocationsLeft.get(currentPlayer)));
		super.update();
	}

}
