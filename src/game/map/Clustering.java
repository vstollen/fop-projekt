package game.map;

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
    
    private final ArrayList<Kingdom> kingdoms = new ArrayList<>();

    /**
     * Ein neues Clustering-Objekt erzeugen.
     * @param castles Die Liste von Burgen, die aufgeteilt werden sollen
     * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
     */
    public Clustering(List<Castle> castles, int kingdomCount) {
        if (kingdomCount < 2)
            throw new IllegalArgumentException("Ungültige Anzahl an Königreichen");

        this.random = new Random();
        this.kingdomCount = kingdomCount;
        this.allCastles = Collections.unmodifiableList(castles);
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
    	
    	setKingdomsToNearestCenter(centers);
    	
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
     * Wählt eine zufällige Burg aus
     * @return Eine zufällige Burg aus {@link Clustering#allCastles}
     */
    private Castle getRandomCastle() {
    	
		int randomCastleIndex = random.nextInt(allCastles.size()); 
		
		return allCastles.get(randomCastleIndex);
    }
}
