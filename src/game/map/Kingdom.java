package game.map;

import game.Player;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse representiert ein Königreich. Jedes Königreich hat eine Liste von Burgen sowie einen Index {@link #type} im Bereich von 0-5
 */
public class Kingdom {

    private List<Castle> castles;
    private int type;

    private Point center;

    /**
     * Erstellt ein neues Königreich
     *
     * @param type der Typ des Königreichs (im Bereich 0-5)
     */
    public Kingdom(int type) {
        this.castles = new LinkedList<>();
        this.type = type;
    }

    /**
     * Eine Burg zum Königreich hinzufügen
     *
     * @param castle die Burg, die hinzugefügt werden soll
     */
    public void addCastle(Castle castle) {
        this.castles.add(castle);
    }

    /**
     * Gibt den Typen des Königreichs zurück. Dies wird zur korrekten Anzeige benötigt
     *
     * @return der Typ des Königreichs.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Eine Burg aus dem Königreich entfernen
     *
     * @param castle die zu entfernende Burg
     */
    public void removeCastle(Castle castle) {
        this.castles.remove(castle);
    }

    /**
     * Gibt den Spieler zurück, der alle Burgen in dem Köngreich besitzt.
     * Sollte es keinen Spieler geben, der alle Burgen besitzt, wird null zurückgegeben.
     *
     * @return der Besitzer oder null
     */
    public Player getOwner() {
        if (castles.isEmpty())
            return null;

        Player owner = castles.get(0).getOwner();
        for (Castle castle : castles) {
            if (castle.getOwner() != owner)
                return null;
        }

        return owner;
    }

    /**
     * Gibt alle Burgen zurück, die in diesem Königreich liegen
     *
     * @return Liste von Burgen im Königreich
     */
    public List<Castle> getCastles() {
        return this.castles;
    }

    /**
     * Gibt das Zentrum des Königreiches zurück
     *
     * @return Das Zentrum des Königreiches
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Setzt das Zentrum des Königreiches
     *
     * @param center Das Zentrum des Königreihes
     */
    public void setCenter(Point center) {
        this.center = center;
    }
}
