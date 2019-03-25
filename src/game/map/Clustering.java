package game.map;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Diese Klasse teilt Burgen in Königreiche auf
 */
public class Clustering {

    private final List<Castle> allCastles;
    private final int kingdomCount;
    private final Dimension mapSize;
    private final ArrayList<Kingdom> kingdoms = new ArrayList<>();
    private Random random;

    /**
     * Ein neues Clustering-Objekt erzeugen.
     *
     * @param castles      Die Liste von Burgen, die aufgeteilt werden sollen
     * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
     * @param mapSize      Die Größe der Map
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
     * Generiert eine Liste von Königreichen
     * Jedes Königreich sollte dabei einen Index im Bereich 0-5 bekommen, damit die Burg richtig angezeigt werden kann.
     * Siehe auch {@link Kingdom#getType()}
     *
     * @return Die Generierte Liste von Königreichen
     */
    public List<Kingdom> getPointsClusters() {
        createKingdoms(kingdomCount);
        setRandomCenters(kingdoms);

        List<Kingdom> oldAssignments = null;
        List<Kingdom> newAssignments = null;
        do {
            oldAssignments = newAssignments;

            setKingdomsToNearestCenter(allCastles);
            newAssignments = getCastleKingdomAssignments();

            findNewCenters();
        } while (!newAssignments.equals(oldAssignments));

        return kingdoms;
    }

    /**
     * Füllt {@link Clustering#kingdoms} mit n neuen Königreichen.
     *
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
     * Ordnet jedem Königreich ein zufälliges Zentrum zu
     *
     * @param kingdoms die königreiche denen neue Zentren zugeordnet werden sollen
     */
    private void setRandomCenters(Collection<Kingdom> kingdoms) {

        for (Kingdom kingdom : kingdoms) {
            Point newCenter = getRandomLocation();
            kingdom.setCenter(newCenter);
        }
    }

    /**
     * Findet für alle Königreiche neue Zentren durch berechnung der durchschnittlichen Koordinaten
     */
    private void findNewCenters() {

        for (Kingdom kingdom : kingdoms) {

            Point averageLocation = getAverageLocation(kingdom.getCastles());
            kingdom.setCenter(averageLocation);
        }
    }

    /**
     * Ordnet allen Burgen aus castles, dem ihnen nächtgelegenen Königreich zu
     *
     * @param castles Burgen denen ein neues Königreich zugeordnet werden soll
     */
    private void setKingdomsToNearestCenter(Collection<Castle> castles) {

        for (Castle castle : castles) {
            Kingdom nearestKingdom = getNearestKingdom(castle);
            castle.setKingdom(nearestKingdom);
        }
    }

    /**
     * Findet das Königreich, mit der geringsten euklidischen Distanz zwischen castle und dem zugehörigen Zentrum.
     *
     * @param castle Die Burg deren nächstes Königreich gesucht werden soll
     * @return Das Königreich mit dem nächsten Zentrum zu castle
     */
    private Kingdom getNearestKingdom(Castle castle) {

        Kingdom nearestKingdom = null;

        for (Kingdom newKingdom : kingdoms) {

            if (nearestKingdom == null) {
                nearestKingdom = newKingdom;
                continue;
            }

            if (castle.distance(newKingdom.getCenter()) < castle.distance(nearestKingdom.getCenter())) {
                nearestKingdom = newKingdom;
            }
        }

        return nearestKingdom;
    }

    /**
     * Bildet eine Liste, die in der Reihenfolge der Burgen in allCastles jeweils die Königreiche der Burg enthält.
     *
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
     *
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
     * Findet eine zufällige Position auf der Karte
     *
     * @return Die zufällige Position
     */
    private Point getRandomLocation() {
        int x = random.nextInt(mapSize.width);
        int y = random.nextInt(mapSize.height);

        return new Point(x, y);
    }
}
