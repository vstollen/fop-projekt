package game;

import game.map.Castle;

public interface GameInterface {

    void onAttackStopped();
    void onAttackStarted(Castle source, Castle target, int troopCount);
    void onCastleChosen(Castle castle, Player player);
    void onNextTurn(Player currentPlayer, int troopsGot, boolean human);
    void onNewRound(int round);
    void onGameOver(Player winner);
    void onGameStarted(Game game);
    void onConquer(Castle castle, Player player);
    void onUpdate();
    void onAddScore(Player player, int score);
    int[] onRoll(Player player, int dices, boolean fastForward);
}
