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
	
	/**
	 * Wählt so lange freie Burgen aus, bis alle Truppen aufgebraucht sind, oder es keine Burgen mehr gibt.
	 * Dazu werden die Königreiche von klein nach groß bevorzugt.
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Verteilt alle unverteilten Truppen auf die Burgen.
	 * Hierbei wird versucht alle Truppen auf eine einzige Burg, die nicht an einer Grenze liegt zu setzen.
	 * Falls dies nicht geht wird eine Burg an der Grenze gewählt.
	 */
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
	
	/**
	 * Führt so lange Angriffe aus, bis keine Burg mehr die Kriterien von getAttackReadyCastles() erfüllt,
	 * oder es keine Burgen zu feindlichen Grenzen mehr gibt.
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Lässt jede Burg aus attackingCastles wenn möglich einmal angreifen.
	 * Dabei werden bevorzugt Königreiche angegriffen, bei denen der AI noch wenige Burgen zur vollständigen Einnahme fehlen.
	 * @param attackingCastles Die Burgen mit denen angegriffen werden soll
	 * @throws InterruptedException
	 */
	private void attackWithCastles(Collection<Castle> attackingCastles) throws InterruptedException {
		List<Kingdom> targetKingdoms = getKingdomsSortedByMissingCastles();
		
		for (Castle attackingCastle : attackingCastles) {
			
			for (Kingdom nextBestKingdom : targetKingdoms) {
				AttackThread attack = attackKingdom(attackingCastle, nextBestKingdom);
				
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
	
	/**
	 * Versucht die schwächste Burg aus dem Königreich opponent anzugreifen.
	 * @param source Die Burg, von der aus angegriffen werden soll
	 * @param opponent Das Königreich, das angegriffen werden soll
	 * @return Einen AttackThread zu dem ausgeführten Angriff, oder null falls das Königreich nicht angegriffen werden konnte
	 */
	private AttackThread attackKingdom(Castle source, Kingdom opponent) {
		GameMap gameMap = game.getMap();
		Graph<Castle> graph = gameMap.getGraph();
		
		PathFinding pathFinding = new PathFinding(graph, source, Action.ATTACKING, this);
		pathFinding.run();
		
		LinkedList<Castle> reachableOpponents = new LinkedList<>();
		
		for (Castle possibleOpponentCastle : opponent.getCastles()) {
			if (possibleOpponentCastle.getOwner().getTeam() == this.getTeam()) {
				continue;
			}
			
			if (pathFinding.getPath(possibleOpponentCastle) != null) {
				reachableOpponents.add(possibleOpponentCastle);
			}
		}
		
		if (reachableOpponents.isEmpty()) {
			return null;
		}
		
		return game.startAttack(source, getWeakestCastle(reachableOpponents), source.getTroopCount());
	}
	
	/**
	 * Zieht alle Truppen aus geschützten Gebieten gleichmäßig verteilt an die Fronten.
	 */
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
	
	/**
	 * Bildet eine Liste von Königreichen, aufsteigend nach ihrer Größe sortiert.
	 * @return Die sortierte Liste von Königreichen
	 */
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
	
	/**
	 * Bildet eine Liste von Königreichen, aufsteigend nach der Anzahl gegnerischer Burgen sortiert.
	 * @return Die sortierte Liste von Königreichen
	 */
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
	
	/**
	 * Findet alle Burgen, die bereit für einen Kampf sind.
	 * Hierzu zählen alle Burgen mit mindestens drei Truppen
	 * @return Alle Burgen, die bereit zum Kämpfen sind
	 */
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
	
	/**
	 * Findet alle Burgen, die noch nicht durch einen Spieler besetzt sind
	 * @return Alle unbesetzten Burgen
	 */
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
	
	/**
	 * Findet in einer Liste von Burgen die Burg, die am wenigsten Truppen beherbergt.
	 * @param castles
	 * @return
	 */
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
	
	/**
	 * Bilder eine Liste aller Burgen zur gegnerischen Grenze
	 * @return Alle Burgen zur gegnerischen Grenze
	 */
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
	
	/**
	 * Findet eine Burg aus castles, welche im Königreich kingdom liegt.
	 * @param castles Alle Burgen, aus welchen gesucht werden soll
	 * @param kingdom Das Königreich, aus dem die gesuchte Burg stammen soll
	 * @return Eine Burg, welche im Königreich kingdom liegt
	 */
	private Castle findCastleInKingdom(Collection<Castle> castles, Kingdom kingdom) {
    	for (Castle castle : castles) {
    		if (castle.getKingdom() == kingdom) {
    			return castle;
    		}
    	}
    	
    	return null;
	}

}
