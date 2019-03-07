package game.map;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Diese Klasse teilt Burgen in Königreiche auf
 */
public class Clustering {

    private Random random;
    private final List<Castle> allCastles;
    private final int kingdomCount;
    private final Dimension mapSize;
    
    private final ArrayList<Kingdom> kingdoms = new ArrayList<>();

    /**
     * Ein neues Clustering-Objekt erzeugen.
     * @param castles Die Liste von Burgen, die aufgeteilt werden sollen
     * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
     */
    public Clustering(List<Castle> castles, int kingdomCount, Dimension mapSize) {
        if (kingdomCount < 2)
            throw new IllegalArgumentException("Ungültige Anzahl an Königreichen");

        this.random = new Random();
        this.kingdomCount = kingdomCount;
        this.allCastles = Collections.unmodifiableList(castles);
        this.mapSize = mapSize;
    }

    /**
     * Gibt eine Liste von Königreichen zurück.
     * Jedes Königreich sollte dabei einen Index im Bereich 0-5 bekommen, damit die Burg richtig angezeigt werden kann.
     * Siehe auch {@link Kingdom#getType()}
     */
    public List<Kingdom> getPointsClusters() {
        // TODO Clustering#getPointsClusters()
    	createKingdoms(kingdomCount);
    	List<Castle> centers = chooseRandomCenters();
    	
    	List<Kingdom> oldAssignments = null;
    	List<Kingdom> newAssignments = null;
    	do {
    		oldAssignments = newAssignments;
    		
    		setKingdomsToNearestCenter(centers);
    		newAssignments = getCastleKingdomAssignments();
    		
    		centers = findNewCenters();
    	} while (!newAssignments.equals(oldAssignments));
    	
        return kingdoms;
    }
    
    /**
     * Füllt {@link Clustering#kingdoms} mit n neuen Königreichen.
     * @param n Die Anzahl an Königreichen die generiert werden sollen
     * @return Die Liste mit den Königreichen
     */
    private List<Kingdom> createKingdoms(int n) {
    	
    	kingdoms.clear();
    	
    	for (int i = 0; i < n; i++) {
    		kingdoms.add(new Kingdom(i));
    	}
    	
    	return kingdoms;
    }
    
    /**
     * Ordnet jedem Königreich eine zufällige Burg zu
     * @return Eine Liste mit den zugeordneten Burgen
     */
    private List<Castle> chooseRandomCenters() {
    	
    	ArrayList<Castle> centers = new ArrayList<>();
    	
    	for (Kingdom kingdom : kingdoms) {
    		
    		Castle newCenter = getRandomCastle();
    		
    		while (centers.contains(newCenter)) {
    			newCenter = getRandomCastle();
    		}
    		
    		centers.add(newCenter);
    		
    		newCenter.setKingdom(kingdom);
    	}
    	
    	return centers;
    }
    
    /**
     * Findet für alle Königreiche neue Zentren durch berechnung der durchschnittlichen Koordinaten
     * @return Eine Liste mit den neuen Zentren
     */
    private List<Castle> findNewCenters() {
    	
    	ArrayList<Castle> centers = new ArrayList<>();
    	
    	for (Kingdom kingdom : kingdoms) {
    		
    		Point averageLocation = getAverageLocation(kingdom.getCastles());
    		Castle newCenter = getNearestCastle(allCastles, averageLocation);
    		centers.add(newCenter);
    	}
    	
    	return centers;
    }
    
    /**
     * Ordnet alle Burgen, dem ihnen nächtgelegenen Königreich zu
     * @param centers
     */
    private void setKingdomsToNearestCenter(Collection<Castle> centers) {
    	
    	for (Castle castle : allCastles) {
    		
    		Kingdom nearestKingdom = getNearestKingdom(castle, centers);
    		castle.setKingdom(nearestKingdom);
    	}
    }
    
    /**
     * Findet das Königreich, mit der geringsten euklidischen Distanz zwischen castle und dem zugehörigen Zentrum.
     * @param castle Die Burg deren nächstes Königreich gesucht werden soll
     * @param centers Die Zentren aller Königreiche
     * @return Das Königreich mit dem nächsten Zentrum zu castle
     */
    private Kingdom getNearestKingdom(Castle castle, Collection<Castle> centers) {
    	
    	Castle nearestCenter = null;
    	
    	for (Castle newCenter : centers) {
    		
    		if (nearestCenter == null) {
    			nearestCenter = newCenter;
    			continue;
    		}
    		
    		if (castle.distance(newCenter) < castle.distance(nearestCenter)) {
    			nearestCenter = newCenter;
    		}
    	}
    	
    	return nearestCenter.getKingdom();
    }
    
    /**
     * Findet die nächste Burg zu einem Ort
     * @param castles Die Burgen aus denen die nächste Burg zu location gesucht wird
     * @param location Der Ort, zu dem die nächste Burg gesucht wird
     * @return Die location am nächsten liegende Burg
     */
    private Castle getNearestCastle(Collection<Castle> castles, Point location) {
    	
    	Castle nearestCastle = null;
    	
    	for (Castle castle : castles) {
    		
    		if (nearestCastle == null) {
    			nearestCastle = castle;
    			continue;
    		}
    		
    		if (castle.distance(location) < nearestCastle.distance(location)) {
    			nearestCastle = castle;
    		}
    	}
    	
    	return nearestCastle;
    }
    
    /**
     * Bildet eine Liste, die in der Reihenfolge der Burgen in allCastles jeweils die Königreiche der Burg enthält.
     * @return Eine Liste mit Königreichen, in der Reihenfolge der jeweiligen Königreiche von allCastles
     */
    private List<Kingdom> getCastleKingdomAssignments() {
    	ArrayList<Kingdom> castleKingdoms = new ArrayList<>(allCastles.size());
    	
    	for (Castle castle : allCastles) {
    		castleKingdoms.add(castle.getKingdom());
    	}
    	
    	return castleKingdoms;
    }
    
    /**
     * Berechnet die durchschnittlichen Koordinaten der Burgen in castles
     * @param castles Die Burgen deren durchschnittliche Koordinaten gesucht sind
     * @return Die durchschnittlichen Koordinaten der Burgen
     */
    private Point getAverageLocation(Collection<Castle> castles) {
    	
    	int totalX = 0;
    	int totalY = 0;
    	
    	for (Castle castle : castles) {
    		
    		Point location = castle.getLocationOnMap();
    		
    		totalX += location.getX();
    		totalY += location.getY();
    	}
    	
    	double averageX = Math.round((double) totalX / castles.size());
    	double averageY = Math.round((double) totalY / castles.size());

    	return new Point((int) averageX, (int) averageY);
    }
    
    /**
     * Wählt eine zufällige Burg aus
     * @return Eine zufällige Burg aus {@link Clustering#allCastles}
     */
    private Castle getRandomCastle() {
    	
		int randomCastleIndex = random.nextInt(allCastles.size()); 
		
		return allCastles.get(randomCastleIndex);
    }
}
