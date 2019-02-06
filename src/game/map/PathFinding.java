package game.map;

import base.GraphAlgorithm;
import base.Node;
import base.Edge;
import base.Graph;
import game.Player;
import game.map.Castle;
import gui.components.MapPanel;

import java.util.List;

public class PathFinding extends GraphAlgorithm<Castle> {

    private MapPanel.Action action;
    private Player currentPlayer;

    public PathFinding(Graph<Castle> graph, Castle sourceCastle, MapPanel.Action action, Player currentPlayer) {
        super(graph, graph.getNode(sourceCastle));
        this.action = action;
        this.currentPlayer = currentPlayer;
    }

    @Override
    protected double getValue(Edge<Castle> edge) {
        Castle castleA = edge.getNodeA().getValue();
        Castle castleB = edge.getNodeB().getValue();
        return castleA.distance(castleB);
    }

    @Override
    protected boolean isPassable(Edge<Castle> edge) {

        Castle castleA = edge.getNodeA().getValue();
        Castle castleB = edge.getNodeB().getValue();

        // One of the regions should belong to the current player
        if(castleA.getOwner() != currentPlayer && castleB.getOwner() != currentPlayer)
            return false;

        if(action == MapPanel.Action.ATTACKING) {
            return castleA.getOwner() != null && castleB.getOwner() != null;
        } else if(action == MapPanel.Action.MOVING) {

            // One of the regions may be empty
            if(castleA.getOwner() == null || castleB.getOwner() == null)
                return true;

            // Else both regions should belong to the current player
            return castleA.getOwner() == castleB.getOwner() && castleA.getOwner() == currentPlayer;
        } else {
            return false;
        }
    }


    @Override
    protected boolean isPassable(Node<Castle> node) {
        return node.getValue().getOwner() == currentPlayer;
    }

    public List<Edge<Castle>> getPath(Castle targetCastle) {
        return this.getPath(getGraph().getNode(targetCastle));
    }
}
