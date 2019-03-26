package game;

public abstract class CaptureTheFlagGoal extends Goal {

	public CaptureTheFlagGoal(String name, String description) {
		super(name, description);
	}

	@Override
	public abstract boolean isCompleted();

	@Override
	public abstract Player getWinner();

	@Override
	public abstract boolean hasLost(Player player);

}
