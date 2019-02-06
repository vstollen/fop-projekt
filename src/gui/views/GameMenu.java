package gui.views;

import game.*;
import game.map.MapSize;
import gui.GameWindow;
import gui.View;
import gui.components.ColorChooserButton;
import gui.components.NumberChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.util.*;

import javax.swing.*;

public class GameMenu extends View {

    private JLabel lblTitle;
    private JLabel lblPlayerCount;
    private JLabel lblMapSize;
    private JLabel lblGoal;
    private JTextArea lblGoalDescription;

    private NumberChooser playerCount;
    private JComboBox mapSize;
    private JComboBox goal;
    private JComponent[][] playerConfig;
    private JButton btnStart, btnBack;

    // map size, type?
    // goal?

    public GameMenu(GameWindow gameWindow) {
        super(gameWindow);
    }

    @Override
    public void onResize() {

        int offsetY = 25;
        int offsetX = 25;

        lblTitle.setLocation(offsetX, offsetY);
        offsetY += 50;

        int columnWidth = Math.max(300, (getWidth() - 75) / 2);

        // Column 1
        offsetX = (getWidth() - 2*columnWidth - 25) / 2 + (columnWidth - 350) / 2;
        lblPlayerCount.setLocation(offsetX, offsetY + 2);
        playerCount.setLocation(offsetX + lblPlayerCount.getWidth() + 10, offsetY);
        offsetY += 50;

        for(int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
            int tempOffsetX = offsetX;
            for(JComponent c : playerConfig[i]) {
                c.setLocation(tempOffsetX, offsetY);
                tempOffsetX += c.getWidth() + 10;
                c.setEnabled(i < playerCount.getValue());
            }

            offsetY += 40;
        }

        // Column 2
        offsetY = 125 - lblMapSize.getHeight();
        offsetX = (getWidth() - 2*columnWidth - 25) / 2 + columnWidth + 25 + (columnWidth - mapSize.getWidth()) / 2;
        lblMapSize.setLocation(offsetX, offsetY); offsetY += lblMapSize.getHeight();
        mapSize.setLocation(offsetX, offsetY); offsetY += mapSize.getHeight() + 10;
        lblGoal.setLocation(offsetX, offsetY); offsetY += lblGoal.getHeight();
        goal.setLocation(offsetX, offsetY); offsetY += goal.getHeight();
        lblGoalDescription.setLocation(offsetX, offsetY);
        lblGoalDescription.setSize(goal.getWidth() + 25, getHeight() - offsetY - BUTTON_SIZE.height - 50);

        // Button bar
        offsetY = this.getHeight() - BUTTON_SIZE.height - 25;
        offsetX = (this.getWidth() - 2*BUTTON_SIZE.width - 25) / 2;
        btnBack.setLocation(offsetX, offsetY);
        btnStart.setLocation(offsetX + BUTTON_SIZE.width + 25, offsetY);
    }

    @Override
    protected void onInit() {

        // Title
        lblTitle = createLabel("Neues Spiel starten", 25, true);

        // Player Count
        lblPlayerCount = createLabel("Anzahl Spieler:", 16);
        playerCount = new NumberChooser(2, GameConstants.MAX_PLAYERS, 2);
        playerCount.setSize(125, 25);
        playerCount.addValueListener((oldValue, newValue) -> onResize());
        add(playerCount);

        // Player rows:
        // [Number] [Color] [Name] [Human/AI] (Team?)
        Vector<String> playerTypes = new Vector<>();
        for(Class<?> c : GameConstants.PLAYER_TYPES)
            playerTypes.add(c.getSimpleName());

        playerConfig = new JComponent[GameConstants.MAX_PLAYERS][];
        for(int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
            playerConfig[i] = new JComponent[] {
                createLabel(String.format("%d.", i + 1),16),
                new ColorChooserButton(GameConstants.PLAYER_COLORS[i]),
                new JTextField(String.format("Spieler %d", i + 1)),
                new JComboBox<>(playerTypes)
            };

            playerConfig[i][1].setSize(25, 25);
            playerConfig[i][2].setSize(200, 25);
            playerConfig[i][3].setSize(125, 25);

            for(JComponent c : playerConfig[i])
                add(c);
        }

        // GameMap config
        lblMapSize = createLabel("Kartengröße", 16);
        mapSize = createCombobox(MapSize.getMapSizes(), MapSize.MEDIUM.ordinal());

        // Goals
        Vector<String> goalNames = new Vector<>();
        for(Goal goal : GameConstants.GAME_GOALS)
            goalNames.add(goal.getName());

        lblGoal = createLabel("Mission", 16);
        lblGoalDescription = createTextArea(GameConstants.GAME_GOALS[0].getDescription(), true);

        goal = createCombobox(goalNames, 0);
        goal.addItemListener(itemEvent -> {
            int i = goal.getSelectedIndex();
            if(i < 0 || i >= GameConstants.GAME_GOALS.length)
                lblGoalDescription.setText("");
            else
                lblGoalDescription.setText(GameConstants.GAME_GOALS[i].getDescription());
        });

        // Buttons
        btnBack = createButton("Zurück");
        btnStart = createButton("Starten");

        getWindow().setSize(750, 450);
        getWindow().setMinimumSize(new Dimension(750, 450));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnBack)
            getWindow().setView(new StartScreen(getWindow()));
        else if(actionEvent.getSource() == btnStart) {

            try {

                // Check Inputs
                int playerCount = this.playerCount.getValue();
                int mapSize = this.mapSize.getSelectedIndex();
                int goalIndex = this.goal.getSelectedIndex();

                // Should never happen
                if (playerCount < 2 || playerCount > GameConstants.MAX_PLAYERS) {
                    showErrorMessage("Bitte geben Sie eine gültige Spielerzahl an.", "Ungültige Eingaben");
                    return;
                }

                // Should never happen
                if (mapSize < 0 || mapSize >= MapSize.values().length) {
                    showErrorMessage("Bitte geben Sie eine gültige Kartengröße an.", "Ungültige Eingaben");
                    return;
                }

                // Should never happen
                if (goalIndex < 0 || goalIndex >= GameConstants.GAME_GOALS.length) {
                    showErrorMessage("Bitte geben Sie ein gültiges Spielziel an.", "Ungültige Eingaben");
                    return;
                }

                // Create Players
                Game game = new Game();
                for (int i = 0; i < playerCount; i++) {
                    String name = ((JTextField) playerConfig[i][2]).getText().replaceAll(";", "").trim();
                    if (name.isEmpty()) {
                        showErrorMessage(String.format("Bitte geben Sie einen gültigen Namen für Spieler %d an.", i + 1), "Ungültige Eingaben");
                        return;
                    }

                    Color color = ((ColorChooserButton) playerConfig[i][1]).getSelectedColor();
                    int playerType = ((JComboBox) playerConfig[i][3]).getSelectedIndex();

                    if (playerType < 0 || playerType >= GameConstants.PLAYER_TYPES.length) {
                        showErrorMessage(String.format("Bitte geben Sie einen gültigen Spielertyp für Spieler %d an.", i + 1), "Ungültige Eingaben");
                        return;
                    }

                    Player player = Player.createPlayer(GameConstants.PLAYER_TYPES[playerType], name, color);
                    if (player == null) {
                        showErrorMessage(String.format("Fehler beim Erstellen von Spieler %d", i + 1), "Unbekannter Fehler");
                        return;
                    }

                    game.addPlayer(player);
                }

                // Set Goal
                Goal goal = GameConstants.GAME_GOALS[goalIndex];
                GameView gameView = new GameView(getWindow(), game);
                game.setMapSize(MapSize.values()[mapSize]);
                game.setGoal(goal);
                game.start(gameView);
                getWindow().setView(gameView);
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
                showErrorMessage("Fehler beim Erstellen des Spiels: " + ex.getMessage(), "Interner Fehler");
            }
        }
    }
}
