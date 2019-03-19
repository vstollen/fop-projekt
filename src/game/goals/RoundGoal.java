package game.goals;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import game.Game;
import game.Goal;
import game.Player;

public class RoundGoal extends Goal {

	final private static int maxRounds = 10;
	
	private Game game;
	
	public RoundGoal() {
		super("Schnelles Spiel", "Der Spieler gewinnt, der nach " + maxRounds + " Runden am meisten Punkte hat.");
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

}
