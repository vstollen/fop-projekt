package game.goals;

import java.util.List;
import java.util.stream.Collectors;

import game.Player;
import game.Team;
import game.map.Castle;
import game.CaptureTheFlagGoal;

/**
 * Eine Klasse für den beliebten Spielmodus "Capture the Flag", in dem jeder Spieler nach der Auswahl
 * der Burgen eine seiner Burgen wählt, in der seine Flagge aufbewahrt werden soll.
 * Diese zuletzt gewählte Burg wird mit zusätzlichen Truppen versehen und kann nur über einen feindlichen Angriff weniger als
 * 3 Truppen halten.
 * Es ist nicht möglich über die Truppenbewegung weniger als 3 Truppen in der Burg zurückzulassen.
 * 
 * @author Felix Graner
 *
 */
public class CaptureTheFlagAlternativeGoal extends CaptureTheFlagGoal {

	public CaptureTheFlagAlternativeGoal() {
		super("Capture the Flag (Alle Flaggen)", "Derjenige Spieler gewinnt, der alle Flaggen in seinen Besitz bringt.\n\nIm Teammodus gewinnt das Team, das alle Flaggen in seinen Besitz bringt.");
	}

	@Override
	public boolean isCompleted() {

		if(getGame().getRound() < 2)
			return false;

		List<Castle> flagCastles = this.getGame().getMap().getCastles().stream().filter(c -> c.isFlagCastle()).collect(Collectors.toList());

		if(flagCastles.size() == 0)
			return false;

		Team winnerTeam = flagCastles.get(0).getOwner().getTeam();

		for(Castle castle:flagCastles) {
			if(castle.getOwner().getTeam() != winnerTeam)
				return false;
		}

		return true;
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