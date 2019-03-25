package game;

import java.awt.Color;

import game.goals.*;
import game.jokers.*;
import game.players.*;

public class GameConstants {

    public static final int MAX_PLAYERS = 4;

    // Determines how many regions are generated per player,
    // e.g. PlayerCount * 7 for Small, PlayerCount * 14 for Medium and PlayerCount * 21 for Large Maps
    public static final int CASTLES_NUMBER_MULTIPLIER = 7;
    public static final int CASTLES_AT_BEGINNING = 3;
    public static final int TROOPS_PER_ROUND_DIVISOR = 3;
    public static final int CONVERSION_JOKER_INVOCATION_MULTIPLIER = 1;

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
        new RoundGoal()
        // TODO: Add more Goals
    };

    public static final Class<?> PLAYER_TYPES[] = {
        Human.class,
        BasicAI.class,
        // TODO: Add more Player types, like different AIs
    };
    
    public static final Joker JOKERS[] = {
    	new TroopbonusJoker(),
    	new ConversionJoker(),
    	new SkipPlayerJoker(),
    	new TunnelJoker()
    };
    
    /**
     * Gibt den zu dem Namen passenden Joker zurück
     * 
     * @param name der Name des Jokers den man haben will
     * @return den Joker zu dem der Name passt
     */
    public static Joker getJokerByName(String name) {
    	for(Joker joker:JOKERS) {
    		if(joker.getName() == name)
    			return joker;
    	}
    	return null;
    }
}
