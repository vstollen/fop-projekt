package game.goals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;


public class CaptureTheFlagGoal extends Goal {
	
	public CaptureTheFlagGoal() {
		super("Capture the Flag", "Derjenige Spieler gewinnt, der als letztes noch seine Flagburg hält.");
	}

	@Override
	public boolean isCompleted() {
		Game game = this.getGame();
		List<Castle> flagDefenders = game.getMap().getCastles().stream().filter(c -> c.isFlagCastle())
		.filter(g -> g.getFlagOwner() == g.getOwner()).collect(Collectors.toList());
		if(flagDefenders.size() == 1 && game.getRound() > 1)
			return true;
		return false;
	}

	@Override
	public Player getWinner() {
		Game game = this.getGame();
		if(game.getRound() < 2) {
            return null;
		} else {
			List<Castle> flagCastles = game.getMap().getCastles().stream().filter(c -> c.isFlagCastle()).collect(Collectors.toList());
			ArrayList<Player> winners = new ArrayList<Player>();
			for(Castle castle:flagCastles) {  // Kann man wahrscheinlich auch direkt als filter auf den Stream werfen
				if(castle.getFlagOwner() == castle.getOwner())
					winners.add(castle.getFlagOwner());
			}
			if(winners.size()>1)
				return null;
			else if(winners.size() == 1)
				return winners.get(0);
				
		}
		
		return null;
	}

	@Override
	public boolean hasLost(Player player) {
		if(player.getFlagCastle() != null) {  // Can't loose without a FlagCastle
			return (player.getFlagCastle().getOwner() != player.getFlagCastle().getFlagOwner());
		} else {
			return false;
		}
	}

}
