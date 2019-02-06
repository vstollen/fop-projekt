package game;

public abstract class Goal {

    private Game game;
    private final String description;
    private final String name;

    public Goal(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract boolean isCompleted();
    public abstract Player getWinner();
    public abstract boolean hasLost(Player player);

    public final  String getDescription() {
        return this.description;
    }

    public final String getName() {
        return this.name;
    }

    protected Game getGame() {
        return this.game;
    }
}
