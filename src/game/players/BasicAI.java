package game.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import base.Edge;
import base.Graph;
import base.Node;
import game.AI;
import game.Game;
import game.map.Castle;
import gui.AttackThread;

public class BasicAI extends AI {

    public BasicAI(String name, Color color) {
        super(name, color);
    }

    private Castle getCastleWithFewestTroops(List<Castle> castles) {
        Castle fewestTroops = castles.get(0);
        for(Castle castle : castles) {
            if(castle.getTroopCount() < fewestTroops.getTroopCount()) {
                fewestTroops = castle;
            }
        }

        return fewestTroops;
    }

    @Override
    protected void actions(Game game) throws InterruptedException {
        if(game.getRound() == 1) {

            List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
            while(availableCastles.size() > 0 && getRemainingTroops() > 0) {

                sleep(1000);

                Castle randomCastle = availableCastles.remove(this.getRandom().nextInt(availableCastles.size()));
                game.chooseCastle(randomCastle, this);
            }

            // For Capture the Flag mode
            if (availableCastles.size() == 0 && getRemainingTroops() > 0) {
            	availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == this).collect(Collectors.toList());

            	sleep(1000);

            	Castle randomFlagCastle = availableCastles.remove(this.getRandom().nextInt(availableCastles.size()));
            	game.chooseCastle(randomFlagCastle, this);
            }

        } else {

            // 1. Distribute remaining troops
            Graph<Castle> graph = game.getMap().getGraph();
            List<Castle> castleNearEnemy = new ArrayList<>();
            for(Castle castle : this.getCastles(game)) {
                Node<Castle> node = graph.getNode(castle);
                for(Edge<Castle> edge : graph.getEdges(node)) {
                    Castle otherCastle = edge.getOtherNode(node).getValue();
                    if(otherCastle.getOwner().getTeam() != this.getTeam()) {
                        castleNearEnemy.add(castle);
                        break;
                    }
                }
            }

            // If no enemy castles are nearby, idle
            if (castleNearEnemy.isEmpty()) {
            	sleep(500);
            	return;
            }

            while(this.getRemainingTroops() > 0) {
                Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
                sleep(500);
                game.addTroops(this, fewestTroops, 1);
            }

            boolean attackWon;

            do {
                // 2. Move troops from inside to border
                for (Castle castle : this.getCastles(game)) {
                    if (!castleNearEnemy.contains(castle) && castle.getTroopCount() > 1) {
                        Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
                        game.moveTroops(castle, fewestTroops, castle.getTroopCount() - 1);
                    }
                }

                // 3. attack!
                attackWon = false;
                for (Castle castle : castleNearEnemy) {
                    if(castle.getTroopCount() < 2)
                        continue;

                    if(castle.isFlagCastle() && castle.getTroopCount() < 4)
                    	continue;

                    Node<Castle> node = graph.getNode(castle);
                    for (Edge<Castle> edge : graph.getEdges(node)) {
                        Castle otherCastle = edge.getOtherNode(node).getValue();
                        int attackingTroops = castle.getTroopCount();

                        if (castle.isFlagCastle()) {
                        	attackingTroops -= 3;
                        }

                        if (otherCastle.getOwner().getTeam() != this.getTeam() && attackingTroops >= otherCastle.getTroopCount()) {
                    		AttackThread attackThread = game.startAttack(castle, otherCastle, attackingTroops);
                            if(fastForward)
                                attackThread.fastForward();

                            attackThread.join();
                            attackWon = attackThread.getWinner() == this;
                            break;
                        }
                    }

                    if(attackWon)
                        break;
                }
            } while(attackWon);
        }
    }
}
