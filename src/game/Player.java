package game;

import game.map.Castle;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player {

    private final String name;
    private Color color;
    private int points;
    private int remainingTroops;

    protected Player(String name, Color color) {
        this.name = name;
        this.points = 0;
        this.color = color;
        this.remainingTroops = 0;
    }

    public int getRemainingTroops() {
        return this.remainingTroops;
    }

    public static Player createPlayer(Class<?> playerType, String name, Color color) {
        if(!Player.class.isAssignableFrom(playerType))
            throw new IllegalArgumentException("Not a player class");

        try {
            Constructor<?> constructor = playerType.getConstructor(String.class, Color.class);
            return (Player) constructor.newInstance(name, color);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addTroops(int troops) {
        if(troops < 0)
            return;

        this.remainingTroops += troops;
    }

    public void removeTroops(int troops) {
        if(this.remainingTroops - troops < 0 || troops < 0)
            return;

        this.remainingTroops -= troops;
    }

    public int getNumRegions(Game game) {
        return this.getCastles(game).size();
    }

    public List<Castle> getCastles(Game game) {
        return game.getMap().getCastles().stream().filter(c -> c.getOwner() == this).collect(Collectors.toList());
    }

    public void reset() {
        this.remainingTroops = 0;
        this.points = 0;
    }
}
