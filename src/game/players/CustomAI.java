package game.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import base.Graph;
import game.AI;
import game.Game;
import game.Player;
import game.map.Castle;
import game.map.GameMap;
import game.map.Kingdom;
import game.map.PathFinding;
import gui.AttackThread;
import gui.components.MapPanel.Action;

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
		
		sleep(500);
		
		attack();
		
		sleep(500);
		
		reinforceBorders();
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
		
		for (Castle castle : ownedCastles) {
			if (castle.isBorderCastle(game.getMap())) {
				continue;
			}
			
			game.addTroops(this, castle, remainingTroops);
			return;
		}
		
		game.addTroops(this, ownedCastles.get(0), remainingTroops);
	}
	
	private void attack() throws InterruptedException {
		Collection<Castle> attackReadyCastles = getAttackReadyCastles();
		
		while (!attackReadyCastles.isEmpty()) {
			attackWithCastles(attackReadyCastles);
			
			if (getBorderCastles().isEmpty()) {
				break;
			}
			
			attackReadyCastles = getAttackReadyCastles();
		}
	}
	
	private void attackWithCastles(Collection<Castle> attackingCastles) throws InterruptedException {
		GameMap gameMap = game.getMap();
		Graph<Castle> graph = gameMap.getGraph();
		
		List<Kingdom> targetKingdoms = getKingdomsSortedByMissingCastles();
		
		for (Castle attackingCastle : attackingCastles) {

			PathFinding pathFinding = new PathFinding(graph, attackingCastle, Action.ATTACKING, this);
			pathFinding.run();
			
			for (Kingdom nextBestKingdom : targetKingdoms) {
				AttackThread attack = attackKingdom(attackingCastle, nextBestKingdom, pathFinding);
				
				if (attack == null) {
					continue;
				}
				
				if (fastForward) {
					attack.fastForward();
				}

                attack.join();
                break;
			}
		}
	}
	
	private AttackThread attackKingdom(Castle source, Kingdom opponent, PathFinding pathFinding) {
		for (Castle possibleOpponentCastle : opponent.getCastles()) {
			if (possibleOpponentCastle.getOwner().getTeam() == this.getTeam()) {
				continue;
			}
			
			if (pathFinding.getPath(possibleOpponentCastle) != null) {
				return game.startAttack(source, possibleOpponentCastle, source.getTroopCount());
			}
		}
		
		return null;
	}
	
	private void reinforceBorders() {
		List<Castle> ownedCastles = this.getCastles(game);
		Collection<Castle> borderCastles = getBorderCastles();
		
		for (Castle castle : ownedCastles) {
			if (castle.isBorderCastle(game.getMap())) {
				continue;
			}
			
			while (castle.getTroopCount() > 1) {
				Castle weakestCastle = getWeakestCastle(borderCastles);
				
				if (weakestCastle == null) {
					break;
				}
				
				game.moveTroops(castle, weakestCastle, 1);
			}
		}
		
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
	
	private List<Kingdom> getKingdomsSortedByMissingCastles() {
		GameMap gameMap = game.getMap();
		List<Kingdom> allKingdoms = gameMap.getKingdoms();
		
		ArrayList<Kingdom> sortedKingdoms = new ArrayList<>(allKingdoms);
		
		Player thisAI = this;
		
		sortedKingdoms.sort(new Comparator<Kingdom>() {
			
			@Override
			public int compare(Kingdom kingdom1, Kingdom kingdom2) {
				List<Castle> kingdom1Castles = kingdom1.getCastles();
				List<Castle> kingdom2Castles = kingdom2.getCastles();
				
				LinkedList<Castle> kingdom1MissingCastles = new LinkedList<>();
				LinkedList<Castle> kingdom2MissingCastles = new LinkedList<>();
				
				for (Castle kingdom1Castle : kingdom1Castles) {
					if (kingdom1Castle.getOwner() != thisAI) {
						kingdom1MissingCastles.add(kingdom1Castle);
					}
				}
				
				for (Castle kingdom2Castle : kingdom2Castles) {
					if (kingdom2Castle.getOwner() != thisAI) {
						kingdom2MissingCastles.add(kingdom2Castle);
					}
				}
				
				return Integer.compare(kingdom1MissingCastles.size(), kingdom2MissingCastles.size());
			}
		});
		
		return sortedKingdoms;
	}
	
	private Collection<Castle> getAttackReadyCastles() {
		List<Castle> ownedCastles = getCastles(game);
		
		LinkedList<Castle> attackReadyCastles = new LinkedList<>();
		
		for (Castle castle : ownedCastles) {
			if (castle.getTroopCount() > 2) {
				attackReadyCastles.add(castle);
			}
		}
		
		return attackReadyCastles;
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
	
	private Castle getWeakestCastle(Collection<Castle> castles) {
		Castle weakestCastle = null;
		
		for (Castle castle : castles) {
			if (weakestCastle == null) {
				weakestCastle = castle;
				continue;
			}
			
			if (castle.getTroopCount() < weakestCastle.getTroopCount()) {
				weakestCastle = castle;
			}
		}
		
		return weakestCastle;
	}
	
	private Collection<Castle> getBorderCastles() {
		List<Castle> ownedCastles = this.getCastles(game);
		LinkedList<Castle> borderCastles = new LinkedList<>();
		
		for (Castle castle : ownedCastles) {
			if (castle.isBorderCastle(game.getMap())) {
				borderCastles.add(castle);
			}
		}
		
		return borderCastles;
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
