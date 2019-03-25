package game.jokers;

import java.awt.Cursor;
import java.util.HashMap;

import game.Game;
import game.Joker;
import game.Player;
import gui.components.MapPanel;
import gui.components.MapPanel.Action;


public class TunnelJoker extends Joker {
	
	private HashMap<Player, Integer> tunnelsLeft;
	private Game game;
	MapPanel map;
	
	public TunnelJoker() {
		super("Tunnel", "Grabe einen Tunnel von einer Burg zu einer beliebigen anderen.\nAber vorsicht! Dein Gegner kann den Tunnel auch benutzen, setzte also deinen Maulwurf mit Weitsicht ein.");
		this.tunnelsLeft = new HashMap<Player, Integer>();
		this.game = getGame();
	}
	
	@Override
	public boolean isUsable() {
		this.game = getGame();

		Player player = game.getCurrentPlayer();
		
		if(!tunnelsLeft.containsKey(player))
			tunnelsLeft.put(player, this.getNumberOfInvocations());
		
		return tunnelsLeft.get(player) > 0;
	}
	
	private Integer getNumberOfInvocations() {
		return Math.max(game.getMap().getCastles().size()/21, 1);  // min 1, max 4 Tunnel, durch |Castles| abhängig von allem
	}
	
	public void setMapPanel(MapPanel map) {
		this.map = map;
	}
	
	@Override
	public void invoke() {
		//	map.reset();
		
		map.setCurrentAction(MapPanel.Action.NONE);
        map.clearSelection();
        map.setCursor(Cursor.getDefaultCursor());
        map.repaint();
		
		map.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		map.repaint();
		map.setCurrentAction(Action.TUNNELING);
		
		map.repaint();
		tunnelsLeft.put(game.getCurrentPlayer(), tunnelsLeft.get(game.getCurrentPlayer()) -1);  // One invoke less
	}

}
