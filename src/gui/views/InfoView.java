package gui.views;

import gui.GameWindow;
import gui.View;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;

public class InfoView extends View {

    private static final String ABOUT_TEXT =
        "~ Game of Castles ~\nFOP-Projekt WiSe 18/19\n" +
            "Hauptautor: Roman Hergenreder\n\n" +
            "Mitwirkende: Philipp Imperatori, Nils Nedderhut, Louis Neumann,\n" +
            "Arne Turuc, Anna-Felicitas Hausmann, Felix Graner, Vincent Stollenwerk\n\n" +
            "Icons/Bilder: Smashicons, Freepick, Retinaicons, Skyclick (www.flaticon.com)\n" +
            "Keine Haftung für Bugs, Systemabstürze, Datenverlust und rauchende Grafikkarten\n\n" +
            "HowTo:\n" +
            "Bevor ein neues Spiel gestartet werden kann, müssen Sie 2-4 Spieler sowie die Kartengröße und die Spielmission festlegen.\n" +
            "Es ist auch möglich, ein Programm als Spieler einzustellen (z.B. BasicAI) oder mit Teams zu spielen.\n" +
            "Anschließend wird eine Karte generiert. In der ersten Runde müssen abwechselnd 3 Burgen ausgewählt werden.\n" +
            "Nachdem alle Burgen verteilt wurden, beginnt das eigentliche Spiel.\n" +
            "Sie haben die Möglichkeit neue Truppen auf Ihre Burgen aufzuteilen,\n" +
            "Truppen zwischen Ihren Burgen zu bewegen sowie andere Burgen anzugreifen.\n" +
            "Bei der Standardmission 'Eroberung' gewinnt der Spieler, der zuerst alle Burgen eingenommen hat.\n\n" +
            "Schnelles Spiel:\n"+
            "Es wird ein Fenster geöffnet, in dem festgelegt wird, nach wie vielen Runden das Spiel beendet wird.\n" +
            "Gewonnen hat dann der Spieler, der bis zu der angegebenen Runde die höchste Punktzahl erzielen konnte.\n\n" +
            "Capture the Flag:\n" +
            "Nach dem Wählen der Burgen wird noch eine der Burgen aus dem vorhandenen Besitz gewählt, in der die Flagge des Spielers aufgestellt wird.\n" +
            "Mindestens 3 Truppen müssen immer in der Burg zurückbleiben, um die Flagge zu verteidigen.\n" +
            "Wird die Burg mit der Flagge von einem gegnerischen Spieler erobert, hat der Spieler sofort verloren.\n" +
            "Der Gewinner ist somit derjenige, der am Ende noch seine Flagburg hält.\n\n" +
            "Alle Spielmodi sind auch in Teams bestreitbar, in denen man in Bündnissen zusammen arbeitet,\n" +
            "sich Truppen schenken kann und über befreundetes Gebiet hinweg angreifen kann."
            ;

    private JButton btnBack;
    private JTextPane txtInfo;
    private JLabel lblTitle;

    public InfoView(GameWindow gameWindow) {
        super(gameWindow);
    }

    @Override
    public void onResize() {

        int offsetY = 25;
        lblTitle.setLocation((getWidth() - lblTitle.getWidth()) / 2, offsetY); offsetY += lblTitle.getSize().height + 25;
        txtInfo.setLocation(25, offsetY);
        txtInfo.setSize(getWidth() - 50, getHeight() - 50 - BUTTON_SIZE.height - offsetY);

        btnBack.setLocation((getWidth() - BUTTON_SIZE.width) / 2, getHeight() - BUTTON_SIZE.height - 25);
    }

    @Override
    protected void onInit() {
        btnBack = createButton("Zurück");
        lblTitle = createLabel("Über", 25, true);
        txtInfo = createTextPane();
        txtInfo.setText(ABOUT_TEXT);
        txtInfo.setBorder(null);
        txtInfo.setBackground(this.getBackground());
        add(txtInfo);

        StyledDocument doc = txtInfo.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        getWindow().setSize(850, 750);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        getWindow().setView(new StartScreen(getWindow()));
    }
}
