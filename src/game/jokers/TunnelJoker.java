package game.jokers;

import java.awt.Cursor;
import java.util.HashMap;

import game.CaptureTheFlagGoal;
import game.Game;
import game.Joker;
import game.Player;
import game.map.MapSize;
import game.GameConstants;
import game.Goal;

import gui.components.MapPanel;
import gui.components.MapPanel.Action;




public class TunnelJoker extends Joker {
	
	private HashMap<Player, Integer> tunnelsLeft;
	private Game game;
	MapPanel map;
	Integer numberOfEdges;
	
	public TunnelJoker() {
		super("Tunnel graben", "Der Spieler wählt zwei beliebige Burgen aus, zwischen denen ein Tunnel gegraben wird.\nDiesen können alle Spieler fortan als Weg benutzen und darüber einen Gegner angreifen.");
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
		if(game.getMapSize() == MapSize.LARGE || game.getMapSize().ordinal() > MapSize.LARGE.ordinal())
			return 2;
		else {
			return 1;  // Ein Tunnel für kleine, 2 Tunnel für ganz große Karten
		}
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
