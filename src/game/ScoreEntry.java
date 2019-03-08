package game;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Diese Klasse stellt einen Eintrag in der Bestenliste dar.
 * Sie enthält den Namen des Spielers, das Datum, die erreichte Punktzahl sowie den Spieltypen.
 */
public class ScoreEntry implements Comparable<ScoreEntry> {

    private String name;
    private Date date;
    private int score;
    private String gameType;

    /**
     * Erzeugt ein neues ScoreEntry-Objekt
     * @param name der Name des Spielers
     * @param score die erreichte Punktzahl
     * @param date das Datum
     * @param gameGoal der Spieltyp
     */
    private ScoreEntry(String name, int score, Date date, String gameGoal) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.gameType = gameGoal;
    }

    /**
     * Erzeugt ein neues ScoreEntry-Objekt
     * @param player der Spieler
     * @param gameGoal der Spieltyp
     */
    public ScoreEntry(Player player, Goal gameGoal) {
        this.name = player.getName();
        this.score = player.getPoints();
        this.date = new Date();
        this.gameType = gameGoal.getName();
    }

    @Override
    public int compareTo(ScoreEntry scoreEntry) {
        return Integer.compare(this.score, scoreEntry.score);
    }

    /**
     * Schreibt den Eintrag als neue Zeile mit dem gegebenen {@link PrintWriter}
     * Der Eintrag sollte im richtigen Format gespeichert werden.
     * @see #read(String)
     * @see Date#getTime()
     * @param printWriter der PrintWriter, mit dem der Eintrag geschrieben wird
     */
        public void write(PrintWriter printWriter) { 
        // TODO: ScoreEntry#write(PrintWriter)
    	StringBuilder bobTheStringBuilder = new StringBuilder();
    	bobTheStringBuilder.append(this.getName()) //line format is "Name;Time_as_Unix_timestamp;Score;Mode"
    					   .append(";")
    					   .append(this.getDate().getTime())
    					   .append(";")
    					   .append(this.getScore())
    					   .append(";")
    					   .append(this.getMode());
    	
    	printWriter.write(bobTheStringBuilder.toString());
    }

    /**
     * List eine gegebene Zeile ein und wandelt dies in ein ScoreEntry-Objekt um.
     * Ist das Format der Zeile ungültig oder enthält es ungültige Daten, wird null zurückgegeben.
     * Eine gültige Zeile enthält in der Reihenfolge durch Semikolon getrennt:
     *    den Namen, das Datum als Unix-Timestamp (in Millisekunden), die erreichte Punktzahl, den Spieltypen
     * Gültig wäre beispielsweise: "Florian;1546947397000;100;Eroberung"
     *
     *
     * @see String#split(String)
     * @see Long#parseLong(String)
     * @see Integer#parseInt(String)
     * @see Date#Date(long)
     *
     * @param line Die zu lesende Zeile
     * @return Ein ScoreEntry-Objekt oder null
     */
    public static ScoreEntry read(String line) {
    	
    	// Missing information if there are other invalid data points
		Pattern scorePattern = Pattern.compile(".*[;][1234567890]*;[0123456789]*;Eroberung"); // actually \d would do the same thing as [0123456789] but my eclipse doesn't compile it
    	Matcher scorePatternMatcher = scorePattern.matcher(line);
    	if(scorePatternMatcher.matches()) {
	    	
	        try {
	        	String[] entries = line.split(";"); // divide and assign
	        	
	        	String tempname = entries[0];  // since we only get a string, split, convert and then use to make a ScoreEntry object
	        	Date tempDate = new Date(readLong(entries[1])); 
	        	int tempscore = Integer.parseInt(entries[2]);
	        	String tempGametype = entries[3];
	        	
	        	ScoreEntry finalEntry = new ScoreEntry(tempname, tempscore, tempDate,  tempGametype);
	        	
	        	return finalEntry;
	        
	        // return null, if any of the given line  contained  invalid information
	        }catch(NumberFormatException numbFormEx) { // actually readLong won't throw an exception, because the regex prevents wrong input
	        	return null;
	        }catch(RuntimeException run) {
	        	return null;
	        	
	        }
    	}
    	return null;
    }
    
    /**
     * Nimmt einen String der einzig aus Zahlen besteht und gibt ein long mit dem entsprechenden Wert aus.
     * Dies entspricht also einer coercion von String zu long.
     * 
     * @param input die Zeichenfolge deren Numerischen Wert man herausfinden möchte, erlaubt sind Zeichenfolgen die von einer regex mit [0123456789]* anerkannt werden
     * @return die Zahl die durch die eingegebene Zeichenfolge repräsentiert wird
     * @throws NumberFormatException, falls in der Zahl andere Zeichen enthalten sind
     */
    public static long readLong(String input) throws NumberFormatException {
    	long sum = 0;
    	for(int i=0; i<input.length(); i++) {
    		if(!(47 < input.charAt(i) && input.charAt(i) < 58)) {
    			throw new NumberFormatException();
    		} else {
    			sum += (input.charAt(input.length() -1 - i) - 48)*Math.pow(10, i); // uniCode magic, add all numbers in the input string
    		}
    	}
    	return sum;
    }
    
    public Date getDate() {
        return date;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public String getMode() {
        return this.gameType;
    }
}
