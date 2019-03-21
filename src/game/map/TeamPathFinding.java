package game.map;

import base.Edge;
import base.Graph;
import base.Node;
import game.Player;
import gui.components.MapPanel.Action;

public class TeamPathFinding extends PathFinding {

	public TeamPathFinding(Graph<Castle> graph, Castle sourceCastle, Action action, Player currentPlayer) {
		super(graph, sourceCastle, action, currentPlayer);
	}

	@Override
	protected boolean isPassable(Edge<Castle> edge) { // kann vermutlich weg
		return super.isPassable(edge);
	}

	@Override
    protected boolean isPassable(Node<Castle> node) {
		return currentPlayer.getTeam().contains(node.getValue().getOwner());
    }
	
	// TODO TeamGoal
	// TODO UI-Einbindung

}
