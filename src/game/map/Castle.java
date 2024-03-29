package game.map;

import java.awt.Point;
import java.util.List;

import base.Edge;
import game.Player;
import game.gameExceptions.alreadyFlagCastleException;

/**
 * Diese Klasse representiert eine Burg.
 * Jede Burg hat Koordinaten auf der Karte und einen Namen.
 * Falls die Burg einen Besitzer hat, hat sie auch eine Anzahl von zugewiesenen Truppen.
 * Die Burg kann weiterhin Teil eines Königreichs sein.
 */
public class Castle {

    private int troopCount;
    private Player owner;
    private Kingdom kingdom;
    private Point location;
    private String name;
    private Boolean flagCastle;
    private Player flagOwner;

    /**
     * Eine neue Burg erstellen
     * @param location die Koordinaten der Burg
     * @param name der Name der Burg
     */
    public Castle(Point location, String name) {
        this.location = location;
        this.troopCount = 0;
        this.owner = null;
        this.kingdom = null;
        this.name = name;
        this.flagCastle = false;
        this.flagOwner = null;
    }

    public Player getOwner() {
        return this.owner;
    }

    public Kingdom getKingdom() {
        return this.kingdom;
    }

    public int getTroopCount() {
        return this.troopCount;
    }

    /**
     * Prüft, ob die Burg eine Flagburg ist
     * @return true, falls die Burg als Flagburg ausgewählt wurde
     */
    public Boolean isFlagCastle() {
    	return this.flagCastle;
    }

    /**
     * Weist einer Burg einen Spieler zu, der seine Flagge in der Burg aufbewahrt
     * @param player der Spieler, dessen Flagge in der Burg aufbewahrt wird
     * @throws alreadyFlagCastleException falls die Burg schon eine Flagge hält
     */
    public void makeFlagCastle(Player player) throws alreadyFlagCastleException {
    	if(!this.flagCastle) {
    		this.flagCastle = true;
    		this.flagOwner = player;
    	} else {
    		throw new alreadyFlagCastleException();
    	}
    }

    /**
     * Gibt den Spieler zurück, dessen Flagge in der Burg aufbewahrt wird
     * @return Player der Spieler, dessen Flagge in der Burg aufbewahrt wird
     */
    public Player getFlagOwner() {
    	return this.flagOwner;    	
    }

    /**
     * Truppen von dieser Burg zur angegebenen Burg bewegen.
     * Dies funktioniert nur, wenn die Besitzer übereinstimmen und bei der aktuellen Burg mindestens eine Truppe übrig bleibt
     * @param target
     * @param troops
     */
    public void moveTroops(Castle target, int troops) {

        // Troops can only be moved to own team regions
        if(target.owner.getTeam() != this.owner.getTeam())
            return;

        // At least one unit must remain in the source region
        if(this.troopCount - troops < 1)
            return;

        // FlagCastles need 3 troops remaining
        if(this.isFlagCastle() && this.troopCount - troops < 3)
        	return;

        this.troopCount -= troops;
        target.troopCount += troops;
    }

    public Point getLocationOnMap() {
        return this.location;
    }

    /**
     * Berechnet die eukldische Distanz zu dem angegebenen Punkt
     * @param dest die Zielkoordinate
     * @return die euklidische Distanz
     */
    public double distance(Point dest) {
        return Math.sqrt(Math.pow(this.location.x - dest.x, 2) + Math.pow(this.location.y - dest.y, 2));
    }

    /**
     * Berechnet die eukldische Distanz zu der angegebenen Burg
     * @param next die Zielburg
     * @return die euklidische Distanz
     * @see #distance(Point)
     */
    public double distance(Castle next) {
        Point otherLocation = next.getLocationOnMap();
        return this.distance(otherLocation);
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public void addTroops(int i) {
        if(i <= 0)
            return;

        this.troopCount += i;
    }

    public String getName() {
        return this.name;
    }

    public void removeTroops(int i) {
        this.troopCount = Math.max(0, this.troopCount - i);
        if(this.troopCount == 0)
            this.owner = null;
    }

    /**
     * Gibt den Burg-Typen zurück. Falls die Burg einem Königreich angehört, wird der Typ des Königreichs zurückgegeben, ansonsten 0
     * @return der Burg-Typ für die Anzeige
     */
    public int getType() {
        return this.kingdom == null ? 0 : this.kingdom.getType();
    }

    /**
     * Die Burg einem Königreich zuordnen
     * @param kingdom Ein Königreich oder null
     */
    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
        if(kingdom != null)
            kingdom.addCastle(this);
    }
    
    /**
     * Prüft ob die Burg an einer Grenze liegt
     * @param map Die Spielkarte
     * @return true, wenn die Burg an einer Grenze liegt
     */
	public boolean isBorderCastle(GameMap map) {
		
		List<Edge<Castle>> allEdges = map.getEdges();
		
		for (Edge<Castle> edge : allEdges) {
			
			Castle castleA = edge.getNodeA().getValue();
			Castle castleB = edge.getNodeB().getValue();
					
			if (castleA != this && castleB != this) {
				continue;
			}
			
			if (castleA.getOwner().getTeam() != castleB.getOwner().getTeam()) {
				return true;
			}
		}
		
		return false;
	}
}
