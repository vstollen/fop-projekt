package gui;

import game.ScoreEntry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * Diese Klasse verwaltet die Resourcen des Spiels, darunter beispielsweise Bilder, Icons und Schriftarten.
 */
public class Resources {

    private static final int NUM_CASTLES = 6;
    private static Resources instance;
    private Map<Color, BufferedImage[]> castlesColored;
    private BufferedImage dices[];
    private BufferedImage check;
    private BufferedImage unit;
    private BufferedImage arrow, arrowDeactivated, plus, plusDeactivated, swords;
    private BufferedImage soldiers[];
    private List<String> castleNames;
    private Font celticFont;

    private List<ScoreEntry> scoreEntries;

    private boolean resourcesLoaded;

    /**
     * Privater Konstruktor, dieser wird normalerweise nur einmal aufgerufen
     */
    private Resources() {
        this.resourcesLoaded = false;
        this.scoreEntries = new LinkedList<>();
    }

    /**
     * Gibt die Instanz des Resourcen Managers zurück oder erzeugt eine neue
     * @return Resourcen Manager
     */
    public static Resources getInstance() {
        if(instance == null) {
            instance = new Resources();
            instance.load();
        }

        return instance;
    }

    /**
     * Lädt ein Bild aus den Resourcen
     * @param name der Name der Datei
     * @return das Bild als {@link BufferedImage}-Objekt
     * @throws IOException Eine IOException wird geworfen, falls das Bild nicht gefunden wurde oder andere Probleme beim Laden auftreten
     */
    private BufferedImage loadImage(String name) throws IOException {
        URL res = Resources.class.getClassLoader().getResource(name);
        if(res == null)
            throw new IOException("Resource not found: " + name);

        return ImageIO.read(res);
    }

    /**
     * Lädt alle Resourcen
     * @return true, wenn alle Resourcen erfolgreich geladen wurden
     */
    public boolean load() {
        if(resourcesLoaded)
            return true;

        try {
            // Load Castle
            castlesColored = new HashMap<>();
            BufferedImage castles[] = new BufferedImage[NUM_CASTLES];
            for(int i = 0; i < NUM_CASTLES; i++)
                castles[i] = loadImage("castle" + (i + 1) + ".png");
            castlesColored.put(Color.WHITE, castles);

            // Load Dices
            dices = loadDices();

            // Load Icons
            soldiers = new BufferedImage[] { loadImage("soldier1.png"), loadImage("soldier2.png") };
            check  = loadImage("check.png");
            unit   = loadImage("unit.png");
            arrow  = loadImage("arrow.png");
            swords = loadImage("swords.png");
            plus   = loadImage("plus.png");
            plusDeactivated = loadImage("plus_deactivated.png");
            arrowDeactivated = loadImage("arrow_deactivated.png");

            // Load Castle names
            castleNames = loadRegionNames();

            // Load font
            celticFont = loadFont("celtic.ttf");

            // Load score entries
            loadScoreEntries();

            resourcesLoaded = true;
            return true;
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Konnte Resourcen nicht laden: " + ex.getMessage(), "Fehler beim Laden der Resourcen", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Speichert bestimmte Resourcen, zurzeit nur den Highscore-Table
     * @return gibt true zurück, wenn erfolgreich gespeichert wurde
     */
    public boolean save() {
        try {
            saveScoreEntries();
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Diese Methode speichert alle Objekte des Typs {@link ScoreEntry} in der Textdatei "highscores.txt"
     * Jede Zeile stellt dabei einen ScoreEntry dar. Sollten Probleme auftreten, muss eine {@link IOException} geworfen werden.
     * Die Einträge sind in der Liste {@link #scoreEntries} zu finden.
     * @see ScoreEntry#write(PrintWriter)
     * @throws IOException Eine IOException wird geworfen, wenn Probleme beim Schreiben auftreten.
     */
    private void saveScoreEntries() throws IOException {
        // TODO: Resources#saveScoreEntries()
    }

    /**
     * Lädt den Highscore-Table aus der Datei "highscores.txt".
     * Dabei wird die Liste {@link #scoreEntries} neu erzeugt und befüllt.
     * Beachte dabei, dass die Liste nach dem Einlen absteigend nach den Punktzahlen sortiert sein muss.
     * Sollte eine Exception auftreten, kann diese ausgegeben werden, sie sollte aber nicht weitergegeben werden,
     * da sonst das Laden der restlichen Resourcen abgebrochen wird ({@link #load()}).
     * @see ScoreEntry#read(String)
     * @see #addScoreEntry(ScoreEntry)
     */
    private void loadScoreEntries() {
        // TODO: Resources#loadScoreEntries()
    }

    /**
     * Fügt ein {@link ScoreEntry}-Objekt der Liste von Einträgen hinzu.
     * Beachte: Nach dem Einfügen muss die Liste nach den Punktzahlen absteigend sortiert bleiben.
     * @param scoreEntry Der einzufügende Eintrag
     * @see ScoreEntry#compareTo(ScoreEntry)
     */
    public void addScoreEntry(ScoreEntry scoreEntry) {
        int i = scoreEntries.size() - 1;
        for(; i >= 0; i--) {
            if(scoreEntry.compareTo(scoreEntries.get(i)) < 0) {
                break;
            }
        }

        scoreEntries.add(i + 1, scoreEntry);
    }

    public List<ScoreEntry> getScoreEntries() {
        return scoreEntries;
    }

    private Font loadFont(String name) throws IOException, FontFormatException {
        InputStream is = Resources.class.getClassLoader().getResourceAsStream(name);
        Font f = Font.createFont(Font.TRUETYPE_FONT, is);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(f);
        return f.deriveFont(20f);
    }

    private BufferedImage[] loadDices() throws IOException {
        BufferedImage[] dices = new BufferedImage[6];
        BufferedImage diceImage = loadImage("dices.png");
        int diceSizeW = diceImage.getWidth() / 3;
        int diceSizeH = diceImage.getHeight() / 2;
        if(diceSizeW != diceSizeH)
            System.out.println("Invalid dice dimensions for resource: dice.png. Expected 3x2 dices, got dimensions: " + diceImage.getWidth() + "x" + diceImage.getHeight());

        int diceSize = diceSizeH;
        int num = 0;

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 3; y++) {
                //Initialize the image array with image chunks
                dices[num] = new BufferedImage(diceSize, diceSize, diceImage.getType());

                // draws the image chunk
                Graphics2D gr = dices[num++].createGraphics();
                gr.drawImage(diceImage, 0, 0, diceSize, diceSize, diceSize * y, diceSize * x, diceSize * y + diceSize, diceSize * x + diceSize, null);
                gr.dispose();
            }
        }

        return dices;
    }

    private List<String> loadRegionNames() throws IOException {

        List<String> regionNames = new LinkedList<>();
        InputStream is = Resources.class.getClassLoader().getResourceAsStream("castles.txt");
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        String line;
        while((line = br.readLine()) != null) {
            line = line.trim();
            if(line.length() > 0 && !line.startsWith("#")) {
                regionNames.add(line);
            }
        }

        br.close();
        return regionNames;
    }

    private BufferedImage colorImage(BufferedImage original, Color color) {
        ColorModel cm = original.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = original.copyData(null);
        BufferedImage newImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        for(int x = 0; x < original.getWidth(); x++) {
            for(int y = 0; y < original.getHeight(); y++) {
                Color oldColor = new Color(original.getRGB(x, y), true);
                if(oldColor.getRed() == 255 && oldColor.getGreen() == 255 && oldColor.getBlue() == 255 && oldColor.getAlpha() == 255) {
                    newImage.setRGB(x, y, color.getRGB());
                }
            }
        }

        return newImage;
    }

    public BufferedImage getCastle(Color color, int index) {
        if(!resourcesLoaded)
            return null;

        index = index % NUM_CASTLES;
        BufferedImage images[];
        if(castlesColored.containsKey(color))
            images = castlesColored.get(color);
        else
            images = new BufferedImage[NUM_CASTLES];

        if(images[index] != null)
            return images[index];

        BufferedImage castleGeneric = castlesColored.get(Color.WHITE)[index];
        images[index] = colorImage(castleGeneric, color);
        castlesColored.put(color, images);
        return images[index];
    }

    public BufferedImage getDice(int value) {
        if(!resourcesLoaded)
            return null;

        return dices[value % dices.length];
    }

    public BufferedImage getCheckIcon() {
        return this.check;
    }

    public BufferedImage getUnitIcon() {
        return this.unit;
    }

    public BufferedImage getPlusIcon() {
        return this.plus;
    }

    public BufferedImage getArrowIcon() {
        return this.arrow;
    }

    public BufferedImage getSwordsIcon() {
        return this.swords;
    }

    public BufferedImage getArrowIconDeactivated() {
        return this.arrowDeactivated;
    }

    public BufferedImage[] getSoldiers() {
        return this.soldiers;
    }

    public List<String> getCastleNames() {
        return castleNames;
    }

    public BufferedImage getPlusIconDeactivated() {
        return this.plusDeactivated;
    }

    public Font getCelticFont() {
        return this.celticFont;
    }
}
