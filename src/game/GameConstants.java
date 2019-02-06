package game;

import game.goals.*;
import game.players.*;

import java.awt.*;

public class GameConstants {

    public static final int MAX_PLAYERS = 4;

    // Determines how many regions are generated per player,
    // e.g. PlayerCount * 7 for Small, PlayerCount * 14 for Medium and PlayerCount * 21 for Large Maps
    public static final int CASTLES_NUMBER_MULTIPLIER = 7;
    public static final int CASTLES_AT_BEGINNING = 3;
    public static final int TROOPS_PER_ROUND_DIVISOR = 3;

    public static final Color COLOR_WATER = Color.BLUE;
    public static final Color COLOR_SAND  = new Color(210, 170, 109);
    public static final Color COLOR_GRASS = new Color(50, 89, 40);
    public static final Color COLOR_STONE = Color.GRAY;
    public static final Color COLOR_SNOW  = Color.WHITE;

    public static final Color PLAYER_COLORS[] = {
        Color.CYAN,
        Color.RED,
        Color.GREEN,
        Color.ORANGE
    };

    public static final Goal GAME_GOALS[] = {
        new ConquerGoal(),
        // TODO: Add more Goals
    };

    public static final Class<?> PLAYER_TYPES[] = {
        Human.class,
        BasicAI.class,
        // TODO: Add more Player types, like different AIs
    };
}
