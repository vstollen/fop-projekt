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
            "Hauptautor: Roman Hergenreder\n" +
            "Mitwirkende: Philipp Imperatori, Nils Nedderhut, Louis Neumann\n" +
            "Icons/Bilder: Smashicons, Freepick, Retinaicons, Skyclick (www.flaticon.com)\n" +
            "Keine Haftung für Bugs, Systemabstürze, Datenverlust und rauchende Grafikkarten\n\n" +
            "HowTo:\n" +
            "Bevor ein neues Spiel gestartet werden kann, müssen Sie 2-4 Spieler sowie die Kartengröße und die Spielmission festlegen. " +
            "Es ist auch möglich, ein Programm als Spieler einzustellen (z.B. BasicAI). " +
            "Anschließend wird eine Karte generiert. In der ersten Runde müssen abwechselnd 3 Burgen ausgewählt werden. Nachdem alle Burgen " +
            "verteilt wurden, beginnt das eigentliche Spiel. Sie haben die Möglichkeit neue Truppen auf Ihre Burgen aufzuteilen, Truppen zwischen " +
            "Ihren Burgen zu bewegen sowie andere Burgen anzugreifen. Bei der Standardmission 'Eroberung' gewinnt der Spieler, der zuerst alle Burgen " +
            "eingenommen hat.\n\n" +
            "Schnelles Spiel:\n"+
            "Es wird ein Fenster geöffnet in dem festgelegt wird nach wie vielen Runden das Spiel beendet wird.\nGewonnen hat dann der Spieler mit den meisten " +
            "Punkten, beziehungsweise dessen Team.\n\n" +
            "Capture the Flag:\n" +
            "Nach dem Wählen der Burgen wird noch eine der Burgen aus dem vorhandenen Besitz gewählt in der die Flagge des Spielers aufgestellt wird.\n" +
            "Dabei ist zu beachten, dass immer mindestens 3 Truppen in der Burg zurückbleiben müssen um die Flagge zu verteidigen.\n" +
            "Wird die Burg mit der Flagge von einem gegnerischen Spieler erobert hat der Spieler sofort verloren.\n Der Gewinner ist somit derjenige, der " +
            "am Ende noch seine Flagburg hält.\n " +
            "Wenn in dem Spielmodus Teams ausgewählt sind gewinnt das Team das am Ende den Gewinnspieler enthält." 
            
            
            
            
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
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        getWindow().setView(new StartScreen(getWindow()));
    }
}
