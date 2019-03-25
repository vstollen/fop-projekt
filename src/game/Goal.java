package game;

public abstract class Goal {

    private final String description;
    private final String name;
    private Game game;

    public Goal(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract boolean isCompleted();

    public abstract Player getWinner();

    public abstract boolean hasLost(Player player);

    /**
     * Beinhaltet Funktionalitäten, die unmittelbar vor dem Spielstart ausgeführt werden sollen
     */
    public void onGameInit() {

    }

    public final String getDescription() {
        return this.description;
    }

    public final String getName() {
        return this.name;
    }

    protected Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
