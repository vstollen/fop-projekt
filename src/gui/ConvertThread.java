package gui;

import game.Game;
import game.map.Castle;

public class ConvertThread extends AttackThread {

	public ConvertThread(Game game, Castle attackerCastle, Castle defenderCastle, int troopAttackCount) {
		super(game, attackerCastle, defenderCastle, troopAttackCount);
	}

	@Override
	public void run() {
		if (!defenderCastle.isFlagCastle()) {
			
			attacker.setInstantAttackWin(false);
			defenderCastle.setOwner(attacker);

			int moveUntil = Math.max(1, attackerCastle.getTroopCount() - troopAttackCount);

			try {
				super.sleep(1500);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			while (attackerCastle.getTroopCount() > moveUntil) {
				attackerCastle.removeTroops(1);
				defenderCastle.addTroops(1);
			}

			winner = attacker;
		}

		game.stopAttack();
	}
}
