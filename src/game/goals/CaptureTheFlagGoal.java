package game.goals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;

/**
 * Eine Klasse für den beliebten Spielmodus "Capture the Flag" in dem jeder Spieler nach der Auswahl
 * der Burgen eine seiner Burgen wählt in der seine Flagge aufbewahrt werden soll.
 * Diese zuletzt gewählte Burg wird mit zusätzlichen Truppen versehen und kann nur über einen Angriff weniger als
 * 3 Truppen halten.
 * Es ist nicht möglich über die Truppenbewegung unter 3 Truppen in der Burg zurückzulassen.
 * 
 * @author Felix Graner
 *
 */
public class CaptureTheFlagGoal extends Goal {
	
	public CaptureTheFlagGoal() {
		super("Capture the Flag", "Derjenige Spieler gewinnt, der als letztes noch seine Flagburg hält.");
	}

	@Override
	public boolean isCompleted() {
		Game game = this.getGame();
		if(game.getRound() < 2)
			return false;
		
		List<Castle> flagDefenders = game.getMap().getCastles().stream().filter(c -> c.isFlagCastle())
				.filter(g -> g.getFlagOwner() == g.getOwner()).collect(Collectors.toList());
		
		Player p = null;
		for(Castle c : flagDefenders) {
			if(p == null)
				p = c.getOwner();
			else if(p.getTeam() != c.getOwner().getTeam())
				return false;
		}
		
		return true;
	}

	@Override
	public Player getWinner() {
		Game game = this.getGame();
		if(game.getRound() < 2)
			return null;
            
		List<Castle> flagCastles = game.getMap().getCastles().stream().filter(c -> c.isFlagCastle()).collect(Collectors.toList());
		
		ArrayList<Player> winners = new ArrayList<>();
		for (Castle castle : flagCastles) {
			if(castle.getFlagOwner() == castle.getOwner())
				winners.add(castle.getFlagOwner());
		}
		
		if(winners.size() > 0) {
			Player p = winners.remove(0);
			for(Player g : winners) {
				if(p.getTeam() != g.getTeam())
					return null;
			}
			return p;
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
