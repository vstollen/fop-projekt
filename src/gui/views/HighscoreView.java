package gui.views;

import game.ScoreEntry;
import gui.GameWindow;
import gui.Resources;
import gui.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

public class HighscoreView extends View {

    private JButton btnBack;
    // private JTextPane txtScores;
    private JTable scoreTable;
    private JLabel lblTitle;
    private JScrollPane scrollPane;

    public HighscoreView(GameWindow gameWindow) {
        super(gameWindow);
    }

    @Override
    public void onResize() {
        int offsetY = 25;
        lblTitle.setLocation((getWidth() - lblTitle.getWidth()) / 2, offsetY); offsetY += lblTitle.getSize().height + 25;
      //  txtScores.setLocation(25, offsetY);
      //  txtScores.setSize(getWidth() - 50, getHeight() - 50 - BUTTON_SIZE.height - offsetY);
        scrollPane.setLocation(25, offsetY);
        scrollPane.setSize(getWidth() - 50, getHeight() - 50 - BUTTON_SIZE.height - offsetY);

        btnBack.setLocation((getWidth() - BUTTON_SIZE.width) / 2, getHeight() - BUTTON_SIZE.height - 25);
    }

    @Override
    protected void onInit() {
        btnBack = createButton("Zurück");
        lblTitle = createLabel("Highscores", 25, true);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Resources resources = Resources.getInstance();
        String[] columns = { "Datum", "Name", "Spielmodus", "Punkte" };
        Object[][] data = new Object[resources.getScoreEntries().size()][];
        for(int i = 0; i < resources.getScoreEntries().size(); i++) {
            ScoreEntry scoreEntry = resources.getScoreEntries().get(i);
            data[i] = new Object[] {
                simpleDateFormat.format(scoreEntry.getDate()),
                scoreEntry.getName(),
                scoreEntry.getMode(),
                scoreEntry.getScore()
            };
        }

        scoreTable = new JTable(data, columns);
        scoreTable.setBackground(this.getBackground());
        scoreTable.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(scoreTable);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        getWindow().setView(new StartScreen(getWindow()));
    }
}
