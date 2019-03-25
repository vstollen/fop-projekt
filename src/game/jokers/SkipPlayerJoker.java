package game.jokers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import game.Game;
import game.Goal;
import game.Joker;
import game.Player;

public class SkipPlayerJoker extends Joker {

	private int maxInvocationsPerPlayer;
	private HashMap<Player, Integer> invocationsLeft = new HashMap<>();

	private Collection<Player> skippedPlayers = new LinkedList<>();

	public SkipPlayerJoker() {
		this(1);
	}

	public SkipPlayerJoker(int invocations) {
		super("Aussetzen", "Lässt einen Gegner deiner Wahl eine Runde aussetzen.");
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

		Player playerToSkip = selectOpponent(currentPlayer);
		
		if (playerToSkip == null) {
			return;
		}
		
		skippedPlayers.add(playerToSkip);

		int playerInvocationsLeft = invocationsLeft.get(currentPlayer) - 1;
		invocationsLeft.put(currentPlayer, playerInvocationsLeft);
	}

	@Override
	public boolean shouldSkipTurn() {
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();

		if (skippedPlayers.remove(currentPlayer)) {
			return true;
		}

		return false;
	}

	/**
	 * Öffnet ein Fenster um einen Gegner auszuwählen.
	 * @param currentPlayer Der Spieler, der Gegner auswählen soll
	 * @return Den ausgewählten Gegner
	 */
	private Player selectOpponent(Player currentPlayer) {
		
		List<Player> opponents = getOpponents(currentPlayer);
		
		if (opponents.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Aktuell kannst du keinen Spieler Aussetzen lassen.\nMan kann nur Gegner, die nicht bereits aussetzen müssen aussetzen lassen.");
			return null;
		}

		Player selectedOpponent = (Player) JOptionPane.showInputDialog(
				null,
				"Welcher Spieler soll aussetzen?",
				"Aussetzen",
				JOptionPane.QUESTION_MESSAGE,
				null,
				opponents.toArray(new Player[opponents.size()]),
				opponents.get(0));

		return selectedOpponent;
	}

	/**
	 * Bildet eine Liste aller Spieler, die nicht mit currentPlayer verbündet sind.
	 * @param currentPlayer Der Spieler dessen Gegner gesucht werden
	 * @return Einer Liste aller Gegner von currentPlayer
	 */
	private List<Player> getOpponents(Player currentPlayer) {
		Game game = getGame();
		Goal goal = game.getGoal();
		
		ArrayList<Player> opponents = new ArrayList<>();

		for (Player player : game.getPlayers()) {
			if (goal.hasLost(player)) {
				continue;
			}
			
			if (skippedPlayers.contains(player)) {
				continue;
			}
			
			if (player.getTeam() != currentPlayer.getTeam()) {
				opponents.add(player);
			}
		}

		return opponents;
	}

}
