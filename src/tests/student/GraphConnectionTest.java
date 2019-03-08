package tests.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import base.Graph;
import base.Node;

public class GraphConnectionTest {
	
	Graph<String> testGraph;
	
	@BeforeEach
	void createNewGraph() {
		testGraph = new Graph<String>();
		
		testGraph.addNode("A");
		testGraph.addNode("B");
		testGraph.addNode("C");
		testGraph.addNode("D");
		testGraph.addNode("E");
		testGraph.addNode("F");
	}
	
	@Test
	void validGraphTest() {
		// Extrahiere die Knoten aus dem Graphen
		Node<String> nodeA = testGraph.getNode("A");
		Node<String> nodeB = testGraph.getNode("B");
		Node<String> nodeC = testGraph.getNode("C");
		Node<String> nodeD = testGraph.getNode("D");
		Node<String> nodeE = testGraph.getNode("E");
		Node<String> nodeF = testGraph.getNode("F");
		
		// Erzeuge einen verzweigten Graphen
		testGraph.addEdge(nodeA, nodeB);
		testGraph.addEdge(nodeB, nodeC);
		testGraph.addEdge(nodeB, nodeD);
		testGraph.addEdge(nodeA, nodeE);
		testGraph.addEdge(nodeC, nodeF);
		assertTrue(testGraph.allNodesConnected(), "Der verzweigte Graph wurde nicht korrekt durchlaufen!");
		
		// Erweitere den Graphen, sodass er zyklisch ist
		testGraph.addEdge(nodeA, nodeF);
		testGraph.addEdge(nodeB, nodeE);
		testGraph.addEdge(nodeB, nodeF);
		testGraph.addEdge(nodeD, nodeE);
		assertTrue(testGraph.allNodesConnected(), "Der zyklische Graph wurde nicht korrekt durchlaufen!");
		
		// Erweitere den Graphen, sodass Knoten zu sich selbst zeigen
		testGraph.addEdge(nodeC, nodeC);
		testGraph.addEdge(nodeD, nodeD);
		assertTrue(testGraph.allNodesConnected(), "Der selbstreferenzierende Graph wurde nicht korrekt durchlaufen!");
	}
	
	@Test
	void invalidGraphTest() {
		// Extrahiere die Knoten aus dem Graphen
		Node<String> nodeA = testGraph.getNode("A");
		Node<String> nodeB = testGraph.getNode("B");
		Node<String> nodeC = testGraph.getNode("C");
		Node<String> nodeD = testGraph.getNode("D");
		Node<String> nodeE = testGraph.getNode("E");
		Node<String> nodeF = testGraph.getNode("F");
		
		// Teste mit einem Graphen ohne Verbindungen
		assertFalse(testGraph.allNodesConnected(), "Der Graph ohne Verbindungen wurde fälschlicherweise als korrekt beurteilt!");
		
		// Teste mit einem Graphen, der nicht alle Knoten enthält
		testGraph.addEdge(nodeA, nodeB);
		testGraph.addEdge(nodeB, nodeC);
		testGraph.addEdge(nodeB, nodeD);
		assertFalse(testGraph.allNodesConnected(), "Der Teilgraph wurde fälschlicherweise als ein korrekter Gesamtgraph beurteilt!");
		
		// Teste mit unverbundenen Teilgraphen
		testGraph.addEdge(nodeE, nodeF);
		assertFalse(testGraph.allNodesConnected(), "Ein Teilgraph wurde fälschlicherweise als ein korrekter Gesamtgraph beurteilt!");
		
		// Verbinde Teilgraphen zu einem einzigen validen Graphen
		testGraph.addEdge(nodeA, nodeD);
		testGraph.addEdge(nodeA, nodeA);
		testGraph.addEdge(nodeB, nodeF);
		assertTrue(testGraph.allNodesConnected(), "Der nun valide Graph wurde als ein nicht korrekter Graph beurteilt!");
	}
}
