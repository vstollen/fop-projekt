package gameExceptions;

/**
 * Eine Exception die genutzt werden kann, falls eine Burg schon eine Flagge beherbergt.
 * 
 * @author Felix Graner
 *
 */
public class alreadyFlagCastleException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public alreadyFlagCastleException() {
		super("Dies ist schon eine Flagburg!");
		
	}

}
