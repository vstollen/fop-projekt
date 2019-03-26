package game.jokers;

import java.awt.Cursor;
import java.util.HashMap;

import game.CaptureTheFlagGoal;
import game.Game;
import game.Joker;
import game.Player;
import gui.components.MapPanel;
import gui.components.MapPanel.Action;
import game.GameConstants;
import game.Goal;


public class TunnelJoker extends Joker {
	
	private HashMap<Player, Integer> tunnelsLeft;
	private Game game;
	MapPanel map;
	Integer numberOfEdges;
	
	public TunnelJoker() {
		super("Tunnel", "Grabe einen Tunnel von einer Burg zu einer beliebigen anderen.\nAber vorsicht! Dein Gegner kann den Tunnel auch benutzen, setzte also deinen Maulwurf mit Weitsicht ein.");
		this.tunnelsLeft = new HashMap<Player, Integer>();
		this.game = getGame();
		for(Goal goal:GameConstants.GAME_GOALS) {
			if(goal instanceof CaptureTheFlagGoal)
				this.whitelistForGameMode.put(goal, false);
		}
	}
	
	@Override
	public boolean isUsable() {
		this.game = getGame();
		
		if(!(whitelistForGameMode.get(game.getGoal())))
				return false;
		Player player = game.getCurrentPlayer();

		if(!tunnelsLeft.containsKey(player))
			tunnelsLeft.put(player, this.getNumberOfInvocations());
		
		return tunnelsLeft.get(player) > 0;
	}
	
	/**
	 * Die Anzahl der Invocations, die je nach Kartengröße und Spielerzahl ausgeführt werden können
	 * @return 
	 */
	private Integer getNumberOfInvocations() {
		return Math.max(game.getMap().getCastles().size()/25, 1);  // min 1, max 3 Tunnel, durch |Castles| abhängig von allem
	}
	
	@Override
	public void setMapPanel(MapPanel map) {
		this.map = map;
	}
	
	@Override
	public void invoke() {
		map.reset();	
		map.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		map.repaint();
		map.setCurrentAction(Action.TUNNELING);
		map.tryTunneling();
		map.repaint();
		tunnelsLeft.put(game.getCurrentPlayer(), tunnelsLeft.get(game.getCurrentPlayer()) - 1); // verbrauche eine Nutzung
	}
	
	@Override
	public void grantInvocation(Player player) {
		this.tunnelsLeft.put(player, tunnelsLeft.get(player) + 1);
	}

}
