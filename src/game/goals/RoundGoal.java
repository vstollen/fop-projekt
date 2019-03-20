package game.goals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;

import game.Game;
import game.Goal;
import game.Player;

public class RoundGoal extends Goal {

	private int maxRounds = 10;
	
	private Game game;
	
	public RoundGoal() {
		super("Schnelles Spiel", "Der Spieler gewinnt, der nach einer bestimmten Rundenzahl am meisten Punkte hat.");
	}
	
	@Override
	public void onGameInit() {
		try {
			int chosenRounds = Integer.parseInt(JOptionPane.showInputDialog("Wie viele Runden soll gespielt werden?", "10"));
			
			if (chosenRounds < 2) {
				throw new NumberFormatException();
			}
			
			maxRounds = chosenRounds;
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Es werden nur Werte von mindestens 2 Runden akzeptiert.\nEs wurden nun " + maxRounds + " Runden ausgewählt.", "Falscher Wert", JOptionPane.WARNING_MESSAGE);
		}
		
		super.onGameInit();
	}

	@Override
	public boolean isCompleted() {
		game = getGame();
		
		Queue<Player> playerQueue = game.getPlayerQueue();
		Player nextPlayer = playerQueue.peek();
		
		if (game.getRound() >= maxRounds && nextPlayer == game.getStartingPlayer()) {
			return true;
		}
		
		List<Player> allPlayers = game.getPlayers();
		List<Player> activePlayers = getActivePlayers(allPlayers);
		
		if (activePlayers.size() == 1) {
			return true;
		}
		
		return false;
	}

	@Override
	public Player getWinner() {
		game = getGame();
		
		Player winner = null;
		
		for (Player player : game.getPlayers()) {
			if (winner == null) {
				winner = player;
			}
			
			if (player.getPoints() > winner.getPoints()) {
				winner = player;
			}
		}
		
		if (isDraw(winner, game.getPlayers())) {
			return null;
		}
		
		return winner;
	}

	@Override
	public boolean hasLost(Player player) {
		game = getGame();
		
		if (game.getRound() < 2) {
			return false;
		}
		
		if (player.getNumRegions(game) == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gibt eine Liste mit den Spielern, die noch im Spiel sind zurück
	 * @param players Alle Spieler, aus denen die Liste herausgesucht werden soll
	 * @return Die Spieler die noch im Spiel sind
	 */
	private List<Player> getActivePlayers(List<Player> players) {
		LinkedList<Player> activePlayers = new LinkedList<>();
		
		for (Player player : players) {
			if (!hasLost(player)) {
				activePlayers.add(player);
			}
		}
		
		return activePlayers;
	}
	
	/**
	 * Prüft ob das Spiel unentschieden steht.
	 * Dazu prüft es, ob es weitere Spieler mit der selben Punktzahl wie winner gibt
	 * @param winner Ein potentieller Sieger
	 * @param players Eine Liste aller Mitspieler
	 * @return Ob das Spiel unentschieden steht
	 */
	private boolean isDraw(Player winner, Collection<Player> players) {
		for (Player player : players) {
			if (player == winner) {
				continue;
			}
			
			if (player.getPoints() == winner.getPoints()) {
				return true;
			}
		}
		
		return false;
	}

}
