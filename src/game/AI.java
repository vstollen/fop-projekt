package game;

import java.awt.Color;
import java.util.Random;

public abstract class AI extends Player {

    private AIThread aiThread;
    private Random random;
    protected boolean fastForward;

    public AI(String name, Color color) {
        super(name, color);
        this.random = new Random();
    }

    protected Random getRandom() {
        return this.random;
    }

    protected abstract void actions(Game game) throws InterruptedException;

    public void doNextTurn(Game game) {
        if(aiThread != null)
            return;

        fastForward = false;
        aiThread = new AIThread(game);
        aiThread.start();
    }

    public void fastForward() {
        if(aiThread != null)
            fastForward = true;
    }

    protected void sleep(int ms) throws InterruptedException {
        long end = System.currentTimeMillis() + ms;
        while(System.currentTimeMillis() < end && !fastForward) {
            Thread.sleep(10);
        }
    }

    private class AIThread extends Thread {

        private Game game;

        private AIThread(Game game) {
            this.game = game;
        }

        private void finishTurn() {
            aiThread = null;
            fastForward = false;

            // Trigger next round, if not automatically
            if(game.getRound() > 1 && game.getCurrentPlayer() == AI.this)
                game.nextTurn();
        }

        @Override
        public void run() {
            try {
                actions(game);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            finishTurn();
        }
    }
}
