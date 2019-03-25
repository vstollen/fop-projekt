package game;

import game.map.Castle;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player {

    private final String name;
    private Team team;
    private Color color;
    private int points;
    private int remainingTroops;

    protected Player(String name, Color color) {
        this.name = name;
        this.team = null;
        this.points = 0;
        this.color = color;
        this.remainingTroops = 0;
    }

    public static Player createPlayer(Class<?> playerType, String name, Color color) {
        if (!Player.class.isAssignableFrom(playerType))
            throw new IllegalArgumentException("Not a player class");

        try {
            Constructor<?> constructor = playerType.getConstructor(String.class, Color.class);
            return (Player) constructor.newInstance(name, color);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getRemainingTroops() {
        return this.remainingTroops;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public String getName() {
        return this.name;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team newTeam) {
        this.team = newTeam.addPlayer(this);
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addTroops(int troops) {
        if (troops < 0)
            return;

        this.remainingTroops += troops;
    }

    public void removeTroops(int troops) {
        if (this.remainingTroops - troops < 0 || troops < 0)
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

    /**
     * Berechnet die gesamte Anzahl von Truppen in Besitz des Spielers.
     * Dazu zählen Truppen auf Burgen und unverteilte Truppen
     *
     * @param game
     * @return Die Anzahl von Truppen in Besitz des Spielers
     */
    public int getTotalTroopCount(Game game) {
        int totalTroopCount = remainingTroops;

        for (Castle ownCastle : getCastles(game)) {
            totalTroopCount += ownCastle.getTroopCount();
        }

        return totalTroopCount;
    }

    @Override
    public String toString() {
        return name;
    }

}
