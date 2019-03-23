package game;

import java.util.LinkedList;
import java.util.List;

public class Team {

	private List<Player> members;

	/**
	 * Konstruktor für leeres Team
	 */
	public Team() {
		this.members = new LinkedList<>();
	}

	/**
	 * Konstruktor mit angegebenem Spieler
	 */
	public Team(Player player) {
		this.members = new LinkedList<>();
		addPlayer(player);
	}

	/**
	 * Fügt einen Spieler dem Team hinzu
	 * @param player der hinzuzufügende Spieler
	 * @return das Team des Spielers
	 */
	public Team addPlayer(Player player) {
		this.members.add(player);
		return this;
	}

	/**
	 * Entfernt einen Spieler aus dem Team
	 * @param player der zu entfernende Spieler
	 * @return true, wenn der Spieler im Team vorhanden war
	 */
	public boolean removePlayer(Player player) {
		return this.members.remove(player);
	}

	/**
	 * Prüft, ob der übergebene Spieler im Team ist
	 * @param player der gesuchte Spieler
	 * @return true, wenn der Spieler im Team ist
	 */
	public boolean contains(Player player) {
		return this.members.contains(player);
	}

	/**
	 * Gibt die Anzahl der Teammitglieder zurück
	 * @return die Anzahl der Mitglieder
	 */
	public int size() {
		return this.members.size();
	}

	/**
	 * Gibt eine Liste der Teammitglieder zurück
	 * @return die Liste der Mitglieder
	 */
	public List<Player> getMembers() {
		return this.members;
	}

}
