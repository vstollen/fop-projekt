package game.map;

import base.*;
import game.GameConstants;
import gui.Resources;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Diese Klasse representiert das Spielfeld. Sie beinhaltet das Hintergrundbild, welches mit Perlin noise erzeugt wurde,
 * eine Liste mit Königreichen und alle Burgen und deren Verbindungen als Graphen.
 *
 * Die Karte wird in mehreren Schritten generiert, siehe dazu {@link #generateRandomMap(int, int, int, int, int)}
 */
public class GameMap {

    private BufferedImage backgroundImage;
    private Graph<Castle> castleGraph;
    private List<Kingdom> kingdoms;

    // Map Generation
    private double[][] noiseValues;
    private int width, height, scale;

    /**
     * Erzeugt eine neue leere Karte. Der Konstruktor sollte niemals direkt aufgerufen werden.
     * Um eine neue Karte zu erstellen, muss {@link #generateRandomMap(int, int, int, int, int)} verwendet werden
     * @param width die Breite der Karte
     * @param height die Höhe der Karte
     * @param scale der Skalierungsfaktor
     */
    private GameMap(int width, int height, int scale) {
        this.castleGraph = new Graph<>();
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    /**
     * Wandelt einen Noise-Wert in eine Farbe um. Die Methode kann nach belieben angepasst werden
     * @param value der Perlin-Noise-Wert
     * @return die resultierende Farbe
     */
    private Color doubleToColor(double value) {
        if (value <= 0.40)
            return GameConstants.COLOR_WATER;
        else if (value <= 0.5)
            return GameConstants.COLOR_SAND;
        else if (value <= 0.7)
            return GameConstants.COLOR_GRASS;
        else if (value <= 0.8)
            return GameConstants.COLOR_STONE;
        else
            return GameConstants.COLOR_SNOW;
    }

    /**
     * Hier wird das Hintergrund-Bild mittels Perlin-Noise erzeugt.
     * Siehe auch: {@link PerlinNoise}
     */
    private void generateBackground() {
        PerlinNoise perlinNoise = new PerlinNoise(width, height, scale);
        Dimension realSize = perlinNoise.getRealSize();

        noiseValues = new double[realSize.width][realSize.height];
        backgroundImage = new BufferedImage(realSize.width, realSize.height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < realSize.width; x++) {
            for (int y = 0; y < realSize.height; y++) {
                double noiseValue = perlinNoise.getNoise(x, y);
                noiseValues[x][y] = noiseValue;
                backgroundImage.setRGB(x, y, doubleToColor(noiseValue).getRGB());
            }
        }
    }

    /**
     * Hier werden die Burgen erzeugt.
     * Dabei wir die Karte in Felder unterteilt, sodass auf jedes Fals maximal eine Burg kommt.
     * Sollte auf einem Feld keine Position für eine Burg existieren (z.B. aufgrund von Wasser oder angrenzenden Burgen), wird dieses übersprungen.
     * Dadurch kann es vorkommen, dass nicht alle Burgen generiert werden
     * @param castleCount die maximale Anzahl der zu generierenden Burgen
     */
    private void generateCastles(int castleCount) {
        double square = Math.ceil(Math.sqrt(castleCount));
        double length = width + height;

        int tilesX = (int) Math.max(1, (width / length + 0.5) * square) + 5;
        int tilesY = (int) Math.max(1, (height / length + 0.5) * square) + 5;
        int tileW = (width * scale / tilesX);
        int tileH = (height * scale / tilesY);

        if (tilesX * tilesY < castleCount) {
            throw new IllegalArgumentException(String.format("CALCULATION Error: tilesX=%d * tilesY=%d < castles=%d", tilesX, tilesY, castleCount));
        }

        // Add possible tiles
        List<Point> possibleFields = new ArrayList<>(tilesX * tilesY);
        for (int x = 0; x < tilesX - 1; x++) {
            for (int y = 0; y < tilesY - 1; y++) {
                possibleFields.add(new Point(x, y));
            }
        }

        // Generate castles
        List<String> possibleNames = generateCastleNames();
        int castlesGenerated = 0;
        while (possibleFields.size() > 0 && castlesGenerated < castleCount) {
            Point randomField = possibleFields.remove((int) (Math.random() * possibleFields.size()));
            int x0 = (int) ((randomField.x + 0.5) * tileW);
            int y0 = (int) ((randomField.y + 0.5) * tileH);

            for (int x = (int) (0.5 * tileW); x >= 0; x--) {
                boolean positionFound = false;
                for (int y = (int) (0.5 * tileH); y >= 0; y--) {
                    int x_mid = (int) (x0 + x + 0.5 * tileW);
                    int y_mid = (int) (y0 + y + 0.5 * tileH);
                    if (noiseValues[x_mid][y_mid] >= 0.6) {
                        String name = possibleNames.isEmpty() ? "Burg " + (castlesGenerated + 1) :
                            possibleNames.get((int) (Math.random() * possibleNames.size()));
                        Castle newCastle = new Castle(new Point(x0 + x, y0 + y), name);
                        boolean doesIntersect = false;

                        for (Castle r : castleGraph.getAllValues()) {
                            if (r.distance(newCastle) < Math.max(tileW, tileH)) {
                                doesIntersect = true;
                                break;
                            }
                        }

                        if (!doesIntersect) {
                            possibleNames.remove(name);
                            castleGraph.addNode(newCastle);
                            castlesGenerated++;
                            positionFound = true;
                            break;
                        }
                    }
                }

                if (positionFound)
                    break;
            }
        }
    }

    /**
     * Hier werden die Kanten erzeugt. Dazu wird jede Burg mit den n nächsten verbunden,
     * sofern es keine Überschneidungen mit bisherigen Verbindungen gibt und die Verbindungen
     * nicht unmittelbar nebeneinander liegen. Wir benutzen n=3 und den Mindestwinkel 15°.
     * Sind hiernach immernoch nicht alle Burgen in einem Graphen, so wird n weiter erhöht.
     */
    private void generateEdges() {
		ArrayList<Node<Castle>> nodes = new ArrayList<>(castleGraph.getNodes());
		int amountOfNodes = nodes.size();
    	
		// Eine HashMap, die für jede Burg die jeweils nächstgelegenen in einer sortierten Liste enthält
		HashMap<Node<Castle>, ArrayList<Node<Castle>>> closestNodes = new HashMap<>();
    	
		for (Node<Castle> nodeA : castleGraph.getNodes()) {
			nodes.sort((n1, n2) -> {
					double p = n1.getValue().distance(nodeA.getValue()) - n2.getValue().distance(nodeA.getValue());
					return p > 0 ? 1 : -1;
				}
			);
    		closestNodes.put(nodeA, new ArrayList<>(nodes));
    	}
    	
    	/*
    	 * Erzeuge neue Verbindungen, indem jede Burg mit den n nächstgelegenen verbunden wird.
    	 * Ist danach noch nicht jede Burg im selben Graphen, so wird n weiter erhöht.
    	 * Eine Verbindung wird nur gebildet, wenn sie keine bisherigen schneidet und
    	 * sie mindestens 15°deg von der nächsten Verbindung der selben Burg abweicht.
    	 */
    	for (int level = 1; level <= 3 || !castleGraph.allNodesConnected(); level++) {
    		
    		if (level == amountOfNodes)
    			break;
    		
    		for (Node<Castle> nodeA : castleGraph.getNodes()) {
    			Node<Castle> nodeB = closestNodes.get(nodeA).get(level);
    			
    			if (hasIntersection(nodeA, nodeB))
    				continue;
    			
    			if (angleBelow(15.0, nodeA, nodeB))
    				continue;
    			
    			castleGraph.addEdge(nodeA, nodeB);
    		}
    	}
    }

	/**
	 * Hier wird die neue Kante mit den bisher im Graphen vorhandenen verglichen.
	 * Schneidet sie sich mit einer, so wird true zurückgeliefert.
	 * @param nodeA der Startknoten der neuen Kante
	 * @param nodeB der Endknoten der neuen Kante
	 * @return true, wenn die Kante eine bereits vorhandene schneidet
	 */
	private boolean hasIntersection(Node<Castle> nodeA, Node<Castle> nodeB) {
		for (Edge<Castle> edge : castleGraph.getEdges()) {
			
			Point nodeAPos = nodeA.getValue().getLocationOnMap();
			Point nodeBPos = nodeB.getValue().getLocationOnMap();
			Point nodeCPos = edge.getNodeA().getValue().getLocationOnMap();
			Point nodeDPos = edge.getNodeB().getValue().getLocationOnMap();
			
			if (nodeAPos == nodeCPos || nodeAPos == nodeDPos || nodeBPos == nodeCPos || nodeBPos == nodeDPos)
				continue;
			
			if (Line2D.linesIntersect(
					nodeAPos.getX(),
					nodeAPos.getY(),
					nodeBPos.getX(),
					nodeBPos.getY(),
					nodeCPos.getX(),
					nodeCPos.getY(),
					nodeDPos.getX(),
					nodeDPos.getY()))
				return true;
		}
		return false;
	}

	/**
	 * Diese Methode erhält die beiden Punkte, zwischen denen eine Kante generiert werden soll.
	 * Sie betrachtet die bisherigen Kanten von nodeA und vergleicht die Winkel zwischen
	 * diesen und der neuen Kante.
	 * Ist einer dieser Winkel kleiner als maxDegree, so wird true zurückgegeben.
	 * @param maxDegree der maximale Grenzwinkel
	 * @param nodeA der Startknoten der neuen Verbindung
	 * @param nodeB der Endknoten der neuen Verbindung
	 * @return true, wenn eine der bisherigen Kanten von nodeA einen kleineren Winkel als maxDegree
	 * zu der neuen Kante besitzt
	 */
	private boolean angleBelow(double maxDegree, Node<Castle> nodeA, Node<Castle> nodeB) {
		
		// Koordinaten der bisherigen Kanten vom Startknoten aus
		List<Point> compareNodeLocations = castleGraph.getEdges(nodeA).stream()
    			.map(edge -> edge.getOtherNode(nodeA).getValue().getLocationOnMap())
    			.collect(Collectors.toCollection(ArrayList::new));
		
		// Koordinaten von Startknoten und neuem Endknoten
		Point origin = nodeA.getValue().getLocationOnMap();
		Point dest = nodeB.getValue().getLocationOnMap();
		
		// Für jeden Endpunkt aus den bisherigen Kanten...
		for (Point p : compareNodeLocations) {
			
			// ...bestimme den Winkel der Kante zu der neuen Kante
			double angle = Math.atan2(p.getY() - origin.getY(), p.getX() - origin.getX())
					- Math.atan2(dest.getY() - origin.getY(), dest.getX() - origin.getX());
			
			// Konvertiere und berechne positiven, kleineren Winkel
			angle = Math.toDegrees(angle);
			
			if (angle < -180)
				angle += 360;
			
			if (angle > 180)
				angle -= 360;
			
			angle = Math.abs(angle);
			
			// Wenn momentan betrachtete Kante einen kleineren Winkel als maxDegree hat
			if (angle < maxDegree)
				return true;
		}
		// Wenn keine bisherige Kante vom Startknoten mit der neuen Kante einen zu kleinen Winkel einschließt
		return false;
	}

    /**
     * Hier werden die Burgen in Königreiche unterteilt. Dazu wird der {@link Clustering} Algorithmus aufgerufen.
     * @param kingdomCount die Anzahl der zu generierenden Königreiche
     */
    private void generateKingdoms(int kingdomCount) {
        if(kingdomCount > 0 && kingdomCount < castleGraph.getAllValues().size()) {
            Clustering clustering = new Clustering(castleGraph.getAllValues(), kingdomCount, this.getSize());
            kingdoms = clustering.getPointsClusters();
        } else {
            kingdoms = new ArrayList<>();
        }
    }

    /**
     * Eine neue Spielfeldkarte generieren.
     * Dazu werden folgende Schritte abgearbeitet:
     *   1. Das Hintergrundbild generieren
     *   2. Burgen generieren
     *   3. Kanten hinzufügen
     *   4. Burgen in Köngireiche unterteilen
     * @param width die Breite des Spielfelds
     * @param height die Höhe des Spielfelds
     * @param scale die Skalierung
     * @param castleCount die maximale Anzahl an Burgen
     * @param kingdomCount die Anzahl der Königreiche
     * @return eine neue GameMap-Instanz
     */
    public static GameMap generateRandomMap(int width, int height, int scale, int castleCount, int kingdomCount) {

        width = Math.max(width, 15);
        height = Math.max(height, 10);

        if (scale <= 0 || castleCount <= 0)
            throw new IllegalArgumentException();

        System.out.println(String.format("Generating new map, castles=%d, width=%d, height=%d, kingdoms=%d", castleCount, width, height, kingdomCount));
        GameMap gameMap = new GameMap(width, height, scale);
        gameMap.generateBackground();
        gameMap.generateCastles(castleCount);
        gameMap.generateEdges();
        gameMap.generateKingdoms(kingdomCount);

        if(!gameMap.getGraph().allNodesConnected()) {
            System.out.println("Fehler bei der Verifikation: Es sind nicht alle Knoten miteinander verbunden!");
            return null;
        }

        return gameMap;
    }

    /**
     * Generiert eine Liste von Zufallsnamen für Burgen. Dabei wird ein Prefix (Schloss, Burg oder Festung) an einen
     * vorhandenen Namen aus den Resourcen angefügt. Siehe auch: {@link Resources#getcastleNames()}
     * @return eine Liste mit Zufallsnamen
     */
    private List<String> generateCastleNames() {
        String[] prefixes = {"Schloss", "Burg", "Festung"};
        List<String> names = Resources.getInstance().getCastleNames();
        List<String> nameList = new ArrayList<>(names.size());

        for (String name : names) {
            String prefix = prefixes[(int) (Math.random() * prefixes.length)];
            nameList.add(prefix + " " + name);
        }

        return nameList;
    }

    public int getWidth() {
        return this.backgroundImage.getWidth();
    }

    public int getHeight() {
        return this.backgroundImage.getHeight();
    }

    public BufferedImage getBackgroundImage() {
        return this.backgroundImage;
    }

    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public List<Castle> getCastles() {
        return castleGraph.getAllValues();
    }

    public Graph<Castle> getGraph() {
        return this.castleGraph;
    }

    public List<Edge<Castle>> getEdges() {
        return this.castleGraph.getEdges();
    }

    public List<Kingdom> getKingdoms() {
        return this.kingdoms;
    }
}
