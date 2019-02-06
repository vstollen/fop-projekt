package gui.components;

import gui.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class DicePanel extends JPanel {

    private int diceValues[];
    private Random random;
    private Resources resources;
    private int numDices;

    public DicePanel(Resources resources) {
        this.diceValues = new int[3];
        this.resources = resources;
        this.random = new Random();
        this.generateRandom(3);
    }

    public int[] generateRandom(int numDices) {
        this.numDices = numDices;
        int[] result = new int[Math.min(numDices, diceValues.length)];

        for (int i = 0; i < Math.min(numDices, diceValues.length); i++) {
            diceValues[i] = random.nextInt(6) + 1;
            result[i] = diceValues[i];
        }

        repaint();
        return result;
    }

    public int[] generateRandom(int numDices, boolean animate) throws InterruptedException {
        if(animate) {
            long duration = 1500;
            long start = System.currentTimeMillis();
            long end = start + duration;
            long lastTick = 0;

            while(System.currentTimeMillis() < end) {
                long tick = System.currentTimeMillis() - start;
                double progress = (double)tick / (double) duration;
                long waitTime = (long) (200 * Math.pow(progress, 3) - 800 * Math.pow(progress, 2) + 850 * progress + 20);
                if(lastTick == 0 || (waitTime > 0 && (tick - lastTick) >= waitTime)) {
                    lastTick = tick;
                    generateRandom(numDices);
                } else {
                    Thread.sleep(10);
                }
            }
        }

        return generateRandom(numDices);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int margin = 10;
        int diceCount = Math.min(diceValues.length, numDices);

        int diceSize = Math.min(height - 2 * margin, (width - (diceCount + 1) * margin) / diceCount);
        int offsetX = (width - 2 * margin - (diceCount * diceSize)) / 2;

        for(int i = 0; i < diceCount; i++) {
            int x = offsetX + i * (margin + diceSize);
            int y = margin;
            int value = (diceValues[i] - 1) % 6;
            g.drawImage(resources.getDice(value), x, y, diceSize, diceSize, null);
        }
    }
}
