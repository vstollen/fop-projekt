package game;

import java.util.LinkedList;
import java.util.List;

public class Team {

	private List<Player> players;

	/**
	 * Konstruktor für ein neues Team
	 */
	public Team(Player player) {
		this.players = new LinkedList<>();
		addPlayer(player);
	}

	public Team addPlayer(Player player) {
		this.players.add(player);
		return this;
	}

	public boolean removePlayer(Player player) {
		return this.players.remove(player);
	}

	public boolean contains(Player player) {
		return this.players.contains(player);
	}

}
