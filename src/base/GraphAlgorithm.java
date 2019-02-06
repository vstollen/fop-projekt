package base;

import java.util.*;

/**
 * Abstrakte generische Klasse um Wege zwischen Knoten in einem Graph zu finden.
 * Eine implementierende Klasse ist beispielsweise {@link game.map.PathFinding}
 * @param <T> Die Datenstruktur des Graphen
 */
public abstract class GraphAlgorithm<T> {

    /**
     * Innere Klasse um {@link Node} zu erweitern, aber nicht zu verändern
     * Sie weist jedem Knoten einen Wert und einen Vorgängerknoten zu.
     * @param <T>
     */
    private static class AlgorithmNode<T> {

        private Node<T> node;
        private double value;
        private AlgorithmNode<T> previous;

        AlgorithmNode(Node<T> parentNode, AlgorithmNode<T> previousNode, double value) {
            this.node = parentNode;
            this.previous = previousNode;
            this.value = value;
        }
    }

    private Graph<T> graph;

    // Diese Liste enthält alle Knoten, die noch nicht abgearbeitet wurden
    private List<Node<T>> availableNodes;

    // Diese Map enthält alle Zuordnungen
    private Map<Node<T>, AlgorithmNode<T>> algorithmNodes;

    /**
     * Erzeugt ein neues GraphAlgorithm-Objekt mit dem dazugehörigen Graphen und dem Startknoten.
     * @param graph der zu betrachtende Graph
     * @param sourceNode der Startknoten
     */
    public GraphAlgorithm(Graph<T> graph, Node<T> sourceNode) {
        this.graph = graph;
        this.availableNodes = new LinkedList<>(graph.getNodes());
        this.algorithmNodes = new HashMap<>();

        for(Node<T> node : graph.getNodes())
            this.algorithmNodes.put(node, new AlgorithmNode<>(node, null, -1));

        this.algorithmNodes.get(sourceNode).value = 0;
    }

    /**
     * Diese Methode gibt einen Knoten mit dem kleinsten Wert, der noch nicht abgearbeitet wurde, zurück und entfernt ihn aus der Liste {@link #availableNodes}.
     * Sollte kein Knoten gefunden werden, wird null zurückgegeben.
     * Verbindliche Anforderung: Verwenden Sie beim Durchlaufen der Liste Iteratoren
     * @return Der nächste abzuarbeitende Knoten oder null
     */
    private AlgorithmNode<T> getSmallestNode() {
        // TODO: GraphAlgorithm<T>#getSmallestNode()
        return null;
    }

    /**
     * Diese Methode startet den Algorithmus. Dieser funktioniert wie folgt:
     * 1. Suche den Knoten mit dem geringsten Wert (siehe {@link #getSmallestNode()})
     * 2. Für jede angrenzende Kante:
     * 2a. Überprüfe ob die Kante passierbar ist ({@link #isPassable(Edge)})
     * 2b. Berechne den Wert des Knotens, in dem du den aktuellen Wert des Knotens und den der Kante addierst
     * 2c. Ist der alte Wert nicht gesetzt (-1) oder ist der neue Wert kleiner, setze den neuen Wert und den Vorgängerknoten
     * 3. Wiederhole solange, bis alle Knoten abgearbeitet wurden

     * Nützliche Methoden:
     * @see #getSmallestNode()
     * @see #isPassable(Edge)
     * @see Graph#getEdges(Node)
     * @see Edge#getOtherNode(Node)
     */
    public void run() {
        // TODO: GraphAlgorithm<T>#run()
    }

    /**
     * Diese Methode gibt eine Liste von Kanten zurück, die einen Pfad zu dem angegebenen Zielknoten representiert.
     * Dabei werden zuerst beginnend mit dem Zielknoten alle Kanten mithilfe des Vorgängerattributs {@link AlgorithmNode#previous} zu der Liste hinzugefügt.
     * Zum Schluss muss die Liste nur noch umgedreht werden. Sollte kein Pfad existieren, geben Sie null zurück.
     * @param destination Der Zielknoten des Pfads
     * @return eine Liste von Kanten oder null
     */
    public List<Edge<T>> getPath(Node<T> destination) {
        // TODO: GraphAlgorithm<T>#getPath(Node<T>)
        return null;
    }

    /**
     * Gibt den betrachteten Graphen zurück
     * @return der zu betrachtende Graph
     */
    protected Graph<T> getGraph() {
        return this.graph;
    }

    /**
     * Gibt den Wert einer Kante zurück.
     * Diese Methode ist abstrakt und wird in den implementierenden Klassen definiert um eigene Kriterien für Werte zu ermöglichen.
     * @param edge Eine Kante
     * @return Ein Wert, der der Kante zugewiesen wird
     */
    protected abstract double getValue(Edge<T> edge);

    /**
     * Gibt an, ob eine Kante passierbar ist.
     * @param edge Eine Kante
     * @return true, wenn die Kante passierbar ist.
     */
    protected abstract boolean isPassable(Edge<T> edge);

    /**
     * Gibt an, ob eine Knoten passierbar ist.
     * @param node Eine Knoten
     * @return true, wenn der Knoten passierbar ist.
    */
    protected abstract boolean isPassable(Node<T> node);
}
