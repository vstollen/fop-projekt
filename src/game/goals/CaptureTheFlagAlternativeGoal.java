package game.goals;

import java.util.List;
import java.util.stream.Collectors;

import game.Player;
import game.Team;
import game.map.Castle;
import game.CaptureTheFlagGoal;

public class CaptureTheFlagAlternativeGoal extends CaptureTheFlagGoal {
	
	public CaptureTheFlagAlternativeGoal() {
		super(" Alternatives Capture the Flag", "Es gewinnt der Spieler der alle Flaggen besitzt.\n\nBei Teams gewinnt das Team welches alle Flaggen besitzt.");
	}
	
	@Override
	public boolean isCompleted() {
		
		if(getGame().getRound() < 2)
			return false;
		
		List<Castle> flagCastles = this.getGame().getMap().getCastles().stream().filter(c -> c.isFlagCastle()).collect(Collectors.toList());
		
		if(flagCastles.size() > 0) {
			Team winnerTeam = flagCastles.get(0).getOwner().getTeam();
			
			for(Castle castle:flagCastles) {
				if(castle.getOwner().getTeam() != winnerTeam)
					return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public Player getWinner() {
		if(getGame().getRound() < 2)
			return null;
		
		List<Castle> flagCastles = this.getGame().getMap().getCastles().stream().filter(c -> c.isFlagCastle()).collect(Collectors.toList());
		Player winner = flagCastles.get(0).getOwner();
		
		if(winner != null) {
			for(Castle flagCastle:flagCastles) {
				if(flagCastle.getOwner().getTeam() != winner.getTeam())
					winner = null;
			}
		}
		return winner;
	}
	
	@Override
	public boolean hasLost(Player player) {
		if(getGame().getRound() < 1)
			return false;
		if(getWinner() != null)
			return getWinner().getTeam() == player.getTeam();
		return false;
	}

}