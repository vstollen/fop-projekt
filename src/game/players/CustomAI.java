package game.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import game.AI;
import game.Game;
import game.map.Castle;
import game.map.GameMap;
import game.map.Kingdom;

public class CustomAI extends AI {
	
	private Game game;

	public CustomAI(String name, Color color) {
		super(name, color);
	}

	@Override
	protected void actions(Game game) throws InterruptedException {
		this.game = game;
		int round = game.getRound();
		
		if (round == 1) {
			chooseCastles();
			return;
		}
		
		distributeTroops();
	}
	
	private void chooseCastles() throws InterruptedException {
		List<Kingdom> smallestKingdoms = getKingdomsSortedBySize();
		Collection<Castle> freeCastles = getFreeCastles();
		
		while(freeCastles.size() > 0 && getRemainingTroops() > 0) {

            sleep(1000);

            for (Kingdom nextSmallestKingdom : smallestKingdoms) {
            	Castle usefullCastle = findCastleInKingdom(freeCastles, nextSmallestKingdom);
            	
            	if (usefullCastle == null) {
            		continue;
            	}
            	
            	freeCastles.remove(usefullCastle);
            	game.chooseCastle(usefullCastle, this);
            	break;
            }
        }
	}
	
	private void distributeTroops() {
		List<Castle> ownedCastles = this.getCastles(game);
		int remainingTroops = getRemainingTroops();
		
		if (ownedCastles.isEmpty()) {
			return;
		}
		
		ownedCastles.get(0).addTroops(remainingTroops);
	}
	
	private List<Kingdom> getKingdomsSortedBySize() {
		GameMap gameMap = game.getMap();
		List<Kingdom> allKingdoms = gameMap.getKingdoms();
		
		ArrayList<Kingdom> sortedKingdoms = new ArrayList<>(allKingdoms);
		
		sortedKingdoms.sort(new Comparator<Kingdom>() {

			@Override
			public int compare(Kingdom kingdom1, Kingdom kingdom2) {
				List<Castle> kingdom1Castles = kingdom1.getCastles();
				List<Castle> kingdom2Castles = kingdom2.getCastles();
				
				return Integer.compare(kingdom1Castles.size(), kingdom2Castles.size());
			}
		});
		
		return sortedKingdoms;
	}
	
	private Collection<Castle> getFreeCastles() {
		GameMap gameMap = game.getMap();
		List<Castle> allCastles = gameMap.getCastles();
		
		LinkedList<Castle> freeCastles = new LinkedList<>();
		
		for (Castle castle : allCastles) {
			if (castle.getOwner() == null) {
				freeCastles.add(castle);
			}
		}
		
		return freeCastles;
	}
	
	private Castle findCastleInKingdom(Collection<Castle> castles, Kingdom kingdom) {
    	for (Castle castle : castles) {
    		if (castle.getKingdom() == kingdom) {
    			return castle;
    		}
    	}
    	
    	return null;
	}

}
