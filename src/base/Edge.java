package base;

/**
 * Diese Klasse representiert eine generische Kante zwischen zwei Knoten
 * @param <T> die zugrunde liegende Datenstruktur
 */
public class Edge<T> {

    private Node<T> nodeA, nodeB;

    /**
     * Erstellt eine neue Kante zwischen zwei gegebenen Knoten
     * @param nodeA der erste Knoten
     * @param nodeB der zweite Knoten
     */
    Edge(Node<T> nodeA, Node<T> nodeB) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    /**
     * Gibt an, ob die Kante mit dem gegebenen Knoten verbunden ist
     * @param node Der Knoten der überprüft wird
     * @return true, wenn die Kante mit dem Knoten verbunden ist
     */
    boolean contains(Node<T> node) {
        return nodeA == node || nodeB == node;
    }

    /**
     * Gibt den ersten Knoten zurück
     * @return der erste Knoten
     */
    public Node<T> getNodeA() {
        return nodeA;
    }

    /**
     * Gibt den zweiten Knoten zurück
     * @return der zweite Knoten
     */
    public Node<T> getNodeB() {
        return nodeB;
    }

    /**
     * Gibt den jeweils anderen Knoten zurück, abhängig von dem gegebenen
     * @param source der eine Knoten
     * @return der andere Knoten
     */
    public Node<T> getOtherNode(Node<T> source) {
        return (nodeA == source ? nodeB : nodeA);
    }
}
