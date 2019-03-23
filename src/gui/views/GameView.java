package gui.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import game.AI;
import game.Game;
import game.GameConstants;
import game.GameInterface;
import game.Joker;
import game.Player;
import game.map.Castle;
import gui.GameWindow;
import gui.View;
import gui.components.DicePanel;
import gui.components.MapPanel;

public class GameView extends View implements GameInterface {

    private MapPanel map;
    private JScrollPane scrollLog;
    private JTextPane txtStats;
    private DicePanel dices;
    private JList<String> jokerList;
    private JScrollPane scrollingJokers;
    private JTextPane jokerHint;
    private JButton jokerButton;
    private JTextPane gameLog;
    private JButton primaryActionButton;
    private Game game;

    private DefaultListModel<String> jokerListModel;
    
    private static final int jokerListHeight = 75;
    private static final int jokerHintHeight = 75;
    
    GameView(GameWindow gameWindow, Game game) {
        super(gameWindow);
        this.game = game;
    }

    private int sidebarWidth() {
        return (int) Math.max(getWidth() * 0.15, 300);
    }

    private Dimension mapPanelSize() {
        int w = getWidth();
        int h = getHeight();
        return new Dimension(w - sidebarWidth() - 40, h - 20);
    }

    @Override
    public void onResize() {

        int w = getWidth();
        int h = getHeight();

        int sidebarWidth = sidebarWidth();
        Dimension mapPanelSize = mapPanelSize();
        this.map.setBounds(10, 10, mapPanelSize.width, mapPanelSize.height);
        this.map.revalidate();
        this.map.repaint();

        int x = w - sidebarWidth - 20;
        int y = 10;

        txtStats.setSize(sidebarWidth, 50 + 20 * game.getPlayers().size());
        dices.setSize(sidebarWidth, 50);
        scrollLog.setSize(sidebarWidth, h - txtStats.getHeight() - dices.getHeight() - 80 - jokerListHeight - jokerHintHeight- 2 * BUTTON_SIZE.height);
        scrollLog.revalidate();
        scrollLog.repaint();
        
        scrollingJokers.setSize(sidebarWidth, jokerListHeight);
        jokerHint.setSize(sidebarWidth, jokerHintHeight);
        
        jokerButton.setSize(sidebarWidth, BUTTON_SIZE.height);
        
        primaryActionButton.setSize(sidebarWidth, BUTTON_SIZE.height);

        JComponent components[] = {txtStats, dices, scrollingJokers, jokerHint, jokerButton, scrollLog, primaryActionButton};
        for(JComponent component : components) {
            component.setLocation(x, y);
            y += 10 + component.getHeight();
        }
    }

    @Override
    protected void onInit() {

        this.add(this.map = new MapPanel(this, getWindow().getResources()));
        this.map.showConnections(true);

        this.txtStats = createTextPane();
        this.txtStats.addStyle("PlayerColors", null);
        this.add(txtStats);
        
        this.dices = new DicePanel(getWindow().getResources());
        this.dices.setBorder(new LineBorder(Color.BLACK));
        this.add(dices);
        
        this.jokerList = new JList<>();
        this.jokerList.addListSelectionListener(e -> updateJokerHint());
        this.jokerListModel = new DefaultListModel<>();
        this.jokerList.setModel(jokerListModel);
        this.scrollingJokers = new JScrollPane(jokerList);
        this.add(scrollingJokers);
        
        this.jokerButton = createButton("Joker Einsetzen");
        this.add(jokerButton);
        
        this.jokerHint = createTextPane();
        this.add(jokerHint);
        
        this.gameLog = createTextPane();
        this.gameLog.addStyle("PlayerColor", null);
        
        this.scrollLog = new JScrollPane(gameLog);
        this.add(scrollLog);
        
        this.primaryActionButton = createButton("Nächste Runde");

        getWindow().setSize(1080, 780);
        getWindow().setMinimumSize(new Dimension(750, 480 + jokerListHeight + jokerHintHeight + BUTTON_SIZE.height));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == primaryActionButton) {

            switch (primaryActionButton.getText()) {
                case "Nächste Runde":

                    if (game.getCurrentPlayer() instanceof AI)
                        return;

                    if (game.getCurrentPlayer().getRemainingTroops() > 0) {
                        if (game.getRound() == 1) {
                            int troops = game.getCurrentPlayer().getRemainingTroops();
                            JOptionPane.showMessageDialog(this, String.format("Du musst noch %s auswählen.",
                                troops == 1 ? "eine Burg" : troops + " Burgen"),
                                "Burgen auswählen", JOptionPane.WARNING_MESSAGE);
                            return;
                        } else {
                            int choice = JOptionPane.showOptionDialog(this, "Du hast noch unverteilte Truppen.\nBist du dir sicher, dass du die Runde beenden möchtest?",
                                "Runde beenden?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null, new String[]{"Runde beenden", "Abbrechen"}, "Runde beenden");

                            if (choice == 1)
                                return;
                        }
                    }

                    game.nextTurn();
                    break;

                case "Beenden":
                    getWindow().setView(new StartScreen(getWindow()));
                    break;

                case "Überspringen":
                    if(game.getAttackThread() != null)
                        game.getAttackThread().fastForward();
                    else if(game.getCurrentPlayer() instanceof AI) {
                        ((AI)game.getCurrentPlayer()).fastForward();
                    }

                    break;
            }
        } else if (actionEvent.getSource() == jokerButton) {
        	invokeJoker();
        	updateStats();
        }
    }

    public void updateStats() {
        txtStats.setText("");
        StyledDocument doc = txtStats.getStyledDocument();
        Style style = doc.getStyle("PlayerColors");

        try {
            doc.insertString(doc.getLength(), String.format("Runde: %d", game.getRound()), null);
            if (game.getCurrentPlayer() != null) {
                doc.insertString(doc.getLength(), ", Am Zug: ", null);
                StyleConstants.setForeground(style, game.getCurrentPlayer().getColor());
                doc.insertString(doc.getLength(), game.getCurrentPlayer().getName(), style);
            }

            doc.insertString(doc.getLength(), "\n\nName\tPunkte\tBurgen\tTruppen\n", null);
            for (Player p : game.getPlayers()) {
                StyleConstants.setForeground(style, p.getColor());
                doc.insertString(doc.getLength(), p.getName(), style);
                doc.insertString(doc.getLength(), String.format(":\t%d\t%d\t%d\n", p.getPoints(), p.getNumRegions(game), p.getRemainingTroops()), null);
            }
        } catch(BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void logText(String text) {
        Style style = this.gameLog.getStyle("PlayerColor");
        StyleConstants.setForeground(style, Color.BLACK);
        StyledDocument doc = this.gameLog.getStyledDocument();

        try { doc.insertString(doc.getLength(), text, style); }
        catch (BadLocationException ignored) {}
    }

    private void logLine(String line) {
        this.logText(line + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }

    private void logLine(String line, Player... playerFormat) {

        StyledDocument doc = this.gameLog.getStyledDocument();
        Style style = this.gameLog.getStyle("PlayerColor");

        int num = 0;
        int index;
        while(!line.isEmpty() && (index = line.indexOf("%PLAYER%")) != -1) {

            if(index > 0)
                this.logText(line.substring(0, index));

            if(num < playerFormat.length) {
                Player insert = playerFormat[num++];
                StyleConstants.setForeground(style, insert.getColor());
                try { doc.insertString(doc.getLength(), insert.getName(), style); }
                catch (BadLocationException ignored) {}
            }

            line = line.substring(index + ("%PLAYER%").length());
        }

        logLine(line);
    }

    @Override
    public void onCastleChosen(Castle castle, Player player) {
        logLine("%PLAYER% wählt "  + castle.getName() + ".", player);
        updateStats();
        map.repaint();
    }

    @Override
    public void onNextTurn(Player currentPlayer, int troopsGot, boolean human) {
        this.logLine("%PLAYER% ist am Zug.", currentPlayer);

        if(game.getRound() == 1)
            this.logLine("%PLAYER% muss " + troopsGot + " Burgen auswählen.", currentPlayer);
        else
            this.logLine("%PLAYER% erhält " + troopsGot + " Truppen.", currentPlayer);

        map.clearSelection();
        updateStats();
        updateJokers();

        primaryActionButton.setText(human ? "Nächste Runde" : "Überspringen");
    }

    @Override
    public void onNewRound(int round) {
        this.logLine(String.format("Runde %d.", round));
    }

    @Override
    public void onGameOver(Player winner) {
        if(winner == null) {
            this.logLine("Spiel vorbei - Unentschieden.");
        } else {
            this.logLine("Spiel vorbei - %PLAYER% gewinnt das Spiel.", winner);
        }

        primaryActionButton.setText("Beenden");
        updateStats();
    }

    @Override
    public void onGameStarted(Game game) {
        this.map.setGame(game);
        this.gameLog.setText("");
        this.logLine("Neues Spiel gestartet.");
        this.updateStats();

        Dimension mapSize = game.getMap().getSize();
        Dimension panelSize = mapPanelSize();
        if(mapSize.getWidth() > panelSize.getWidth() || mapSize.getHeight() > panelSize.getHeight()) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int newWidth  = Math.min(screenSize.width, mapSize.width) + getWidth() - panelSize.width + 50;
            int newHeight = Math.min(screenSize.height, mapSize.height) + getHeight() - panelSize.height + 50;

            getWindow().setSize(newWidth, newHeight);
            getWindow().setLocationRelativeTo(null);
        }
    }

    @Override
    public void onConquer(Castle castle, Player player) {
        logLine("%PLAYER% erobert " + castle.getName(), player);
        updateStats();
        map.repaint();
    }

    @Override
    public void onUpdate() {
        updateStats();
        updateJokers();
        map.repaint();
    }

    @Override
    public void onAddScore(Player player, int score) {
        updateStats();
    }

    @Override
    public int[] onRoll(Player player, int dices, boolean fastForward) {
        try {
            int[] roll = this.dices.generateRandom(dices, !fastForward);
            StringBuilder rolls = new StringBuilder();
            rolls.append("%PLAYER% würfelt: ");
            for(int i = 0; i < roll.length; i++) {
                rolls.append(i == 0 ? " " : ", ");
                rolls.append(roll[i]);
            }

            logLine(rolls.toString(), player);
            return roll;
        } catch(InterruptedException ex) {
            ex.printStackTrace();
            return new int[0];
        }
    }

    @Override
    public void onAttackStarted(Castle source, Castle target, int troopCount) {
        primaryActionButton.setText("Überspringen");
        logLine("%PLAYER% greift " + target.getName() + " mit " + troopCount + " Truppen an.", source.getOwner());
    }

    @Override
    public void onAttackStopped() {
        map.reset();
        updateStats();
        primaryActionButton.setText("Nächste Runde");
    }
    
    /**
     * Updated die Joker Liste und zeigt nur nutzbare Joker an.
     */
    private void updateJokers() {
    	ArrayList<String> oldElements = getCurrentJokerNames();
    	
    	int selectedIndex = jokerList.getSelectedIndex();
    	String selectedJokerName = "";
    	
    	if (selectedIndex >= 0) {
    		selectedJokerName = oldElements.get(selectedIndex);
    	}
    	
		jokerListModel.clear();
    	
		fillJokerList(selectedJokerName);
    	updateJokerHint();
    }
    
    /**
     * Füllt die Joker Liste mit nutzbaren Jokern.
     * Wählt zusätzlich den Joker mit dem Namen selectedJokerName aus, falls vorhanden.
     * @param selectedJokerName Name des auszuwählenden Jokers
     */
    private void fillJokerList(String selectedJokerName) {
    	for (Joker joker : GameConstants.JOKERS) {
    		
    		joker.update();
    		
    		String jokerName = joker.getName();
    		
    		if (joker.isUsable()) {
    			jokerListModel.addElement(jokerName);
    			
    			if (jokerName.equals(selectedJokerName)) {
    				int newJokerIndex = jokerListModel.size() - 1;
    				jokerList.setSelectedIndex(newJokerIndex);
    			}
    		}
    	}
    }
    
    /**
     * Setzt den passenden Hinweis zum aktuell ausgewählten Joker
     */
    private void updateJokerHint() {
    	Joker selectedJoker = getSelectedJoker();
    	
    	if (selectedJoker == null) {
    		jokerHint.setText("");
    		return;
    	}
    	
    	String hint = selectedJoker.getHint();
    	jokerHint.setText(hint);
    }
    
    /**
     * Führt den ausgewählen Joker aus
     */
    private void invokeJoker() {
    	Joker selectedJoker = getSelectedJoker();
    	
    	if (selectedJoker != null) {
        	selectedJoker.invoke();	
    	}
    	
    	updateJokers();
    }
    
    /**
     * Findet den aktuell ausgewählten Joker
     * @return Der aktuell ausgewählte Joker, null falls kein Joker ausgewählt ist
     */
    private Joker getSelectedJoker() {
    	int selectedJokerIndex = jokerList.getSelectedIndex();
    	
    	if (selectedJokerIndex < 0) {
    		return null;
    	}
    	
    	ArrayList<Joker> usableJokers = new ArrayList<>();
    	
    	for (Joker joker : GameConstants.JOKERS) {
    		if (joker.isUsable()) {
        		usableJokers.add(joker);
    		}
    	}
    	
    	return usableJokers.get(selectedJokerIndex);
    }
    
    /**
     * Bildet eine Liste aus den Joker Namen aktuell angezeigten Joker
     * @return Eine Liste der Namen der aktuell angezeigten Joker
     */
    private ArrayList<String> getCurrentJokerNames() {
    	ArrayList<String> currentJokers = new ArrayList<>();
    	currentJokers.ensureCapacity(jokerListModel.getSize());
    	
    	Enumeration<String> jokerListElements = jokerListModel.elements();
    	while(jokerListElements.hasMoreElements()) {
    		currentJokers.add(jokerListElements.nextElement());
    	}
    	
    	return currentJokers;
    }
}
