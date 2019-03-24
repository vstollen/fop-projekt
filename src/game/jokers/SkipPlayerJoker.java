package game.jokers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import game.Game;
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

	private Player selectOpponent(Player currentPlayer) {
		
		List<Player> opponents = getOpponents(currentPlayer);
		
		// Should not happen
		if (opponents.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Es gibt keine Gegner.");
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

	private List<Player> getOpponents(Player currentPlayer) {
		Game game = getGame();

		ArrayList<Player> opponents = new ArrayList<>();

		for (Player player : game.getPlayers()) {
			if (player.getTeam() != currentPlayer.getTeam()) {
				opponents.add(player);
			}
		}

		return opponents;
	}

}
