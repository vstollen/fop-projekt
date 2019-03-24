package game.jokers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

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
		
		Player playerToSkip = selectOponent(currentPlayer);
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
	
	private Player selectOponent(Player currentPlayer) {
		return getGame().getPlayerQueue().peek();
	}

}
