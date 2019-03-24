package gui;

import game.Game;
import game.map.Castle;

public class ConvertThread extends AttackThread {

	public ConvertThread(Game game, Castle attackerCastle, Castle defenderCastle, int troopAttackCount) {
		super(game, attackerCastle, defenderCastle, troopAttackCount);
	}

	@Override
	public void run() {
		attacker.setInstantAttackWin(false);

		attackerCastle.removeTroops(troopAttackCount);
		defenderCastle.setOwner(attacker);
		defenderCastle.addTroops(troopAttackCount);

		game.stopAttack();
	}
}
