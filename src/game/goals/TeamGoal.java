package game.goals;

import game.Goal;
import game.Player;

public class TeamGoal extends Goal {

	public TeamGoal() {
		super("Teams", "Das Team gewinnt, das als erstes alle Gebiete erobert hat.");
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Player getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasLost(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
