package game.goals;

import game.Game;
import game.Goal;
import game.Player;
import game.Team;
import game.map.Castle;

public class ConquerGoal extends Goal {

    public ConquerGoal() {
        super("Eroberung", "Derjenige Spieler gewinnt, der als erstes alle Gebiete erobert hat.");
    }

    @Override
    public boolean isCompleted() {
        return this.getWinnerTeam() != null;
    }

    @Override
    public Team getWinnerTeam() {
        Game game = this.getGame();
        if(game.getRound() < 2)
            return null;

        Player p = null;
        for(Castle c : game.getMap().getCastles()) {
            if(c.getOwner() == null)
                return null;
            else if(p == null)
                p = c.getOwner();
            else if(p.getTeam() != c.getOwner().getTeam())
                return null;
        }

        return p.getTeam();
    }

    @Override
    public boolean hasLost(Player player) {
        if(getGame().getRound() < 2)
            return false;

        return player.getNumRegions(getGame()) == 0;
    }
}
