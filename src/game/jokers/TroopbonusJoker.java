package game.jokers;

import java.util.HashMap;
import java.util.List;

import game.Game;
import game.Joker;
import game.Player;
import game.map.Castle;
import game.map.GameMap;

public class TroopbonusJoker extends Joker {

	private int maxInvocationsPerPlayer;
	private HashMap<Player, Integer> invocationsLeft = new HashMap<>();
	
	private static final String hintTemplate = "+%d Truppen\n-------------------\nDer Spieler erhält Truppen abhängig von seiner aktuellen Spielstärke.\nSchwache Spieler erhalten mehr Truppen, starke Spieler erhalten weniger Truppen.";
	
	public TroopbonusJoker() {
		this(1);
	}
	
	public TroopbonusJoker(int invocations) {
		super("Truppenbonus", String.format(hintTemplate, 0));
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
		
		int bonusTroops = getBonusTroops(currentPlayer);
		currentPlayer.addTroops(bonusTroops);
		
		int playerInvocationsLeft = invocationsLeft.get(currentPlayer) - 1;
		invocationsLeft.put(currentPlayer, playerInvocationsLeft);
	}
	
	@Override
	public void update() {
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();
		
		setHint(String.format(hintTemplate, getBonusTroops(currentPlayer)));
		super.update();
	}
	
	/**
	 * Berechnet die Anzahl von Bonustruppen, die player zum aktuellen Zeitpunkt erhalten sollte
	 * @param player Spieler für den die Zahl der Bonustruppen berechnet werden soll
	 * @return Die Zahl der Bonustruppen für player
	 */
	private int getBonusTroops(Player player) {
		Game game = getGame();
		GameMap gameMap = game.getMap();
		
		List<Castle> allCastles = gameMap.getCastles();
		List<Castle> playerCastles = player.getCastles(game);
		
		int totalTroopCount = game.getTotalTroopCount();
		int playerTroopCount = player.getTotalTroopCount(game);
		
		double playerCastleRatio = (double) playerCastles.size() / (double) allCastles.size();
		
		int bonusTroops = (int) Math.round((1 - playerCastleRatio) * 0.5 * (totalTroopCount - playerTroopCount));
		
		return Math.max(2, bonusTroops);
	}

}
