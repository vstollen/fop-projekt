package gui.components;

import gui.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

public class NumberChooser extends JComponent implements MouseListener {

    private int min, max, value;
    private boolean minClicked, maxClicked;
    private List<ValueListener> valueListeners;

    public NumberChooser(int min, int max, int val) {
        this.min = min;
        this.max = max;
        this.setValue(val);
        this.valueListeners = new LinkedList<>();
        this.addMouseListener(this);
    }

    private void setValue(int val) {
        int prev = this.value;
        this.value = clamp(val, min, max);
        repaint();
        if(prev != this.value && this.valueListeners != null)
            this.valueListeners.forEach(l -> l.valueChanged(prev, value));
    }

    public int getValue() {
        return this.value;
    }

    public void decrement() {
        this.setValue(this.getValue() - 1);
    }

    public void increment() {
        this.setValue(this.getValue() + 1);
    }

    public void addValueListener(ValueListener valueListener) {
        this.valueListeners.add(valueListener);
    }

    private static int clamp(int val, int min, int max) {
        return Math.max(Math.min(val, max), min);
    }

    private int getButtonWidth() {
        return Math.max(25, (int) ((getWidth() - 1) * 0.1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth() - 1;
        int h = getHeight() - 1;
        int buttonWidth = getButtonWidth();

        // Borders
        g.setColor(Color.DARK_GRAY);
        g.drawRect(0, 0, w, h);
        g.drawRect(0, 0, buttonWidth, h);
        g.drawRect(w - buttonWidth, 0, buttonWidth, h);

        // Background Colors
        g.setColor(minClicked ? Color.GRAY : Color.LIGHT_GRAY);
        g.fillRect(1, 1, buttonWidth - 1,  h - 1);
        g.setColor(maxClicked ? Color.GRAY : Color.LIGHT_GRAY);
        g.fillRect(w - buttonWidth + 1, 1, buttonWidth - 1, h - 1);

        // Text
        Font font = View.createFont(16);
        FontMetrics fm = g.getFontMetrics(font);
        g.setColor(Color.BLACK);
        g.setFont(font);

        Dimension dim1 = View.calculateTextSize("-", font);
        Dimension dim2 = View.calculateTextSize("+", font);
        Dimension dim3 = View.calculateTextSize(String.valueOf(value), font);

        g.drawString("-", (int) ((buttonWidth - dim1.getWidth()) / 2) - 1, (int)((h - dim1.getHeight()) / 2 + fm.getAscent()) - 1);
        g.drawString("+", w - buttonWidth + (int) ((buttonWidth - dim2.getWidth()) / 2), (int)((h - dim2.getHeight()) / 2 + fm.getAscent()) - 1);
        g.drawString(String.valueOf(value), buttonWidth + (int) ((w - 2 * buttonWidth - dim3.getWidth()) / 2) - 1, (int)((h - dim3.getHeight()) / 2 + fm.getAscent()) - 1);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            int x = mouseEvent.getX();

            if(x < getButtonWidth())
                decrement();
            else if(x >= getWidth() - getButtonWidth())
                increment();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            int x = mouseEvent.getX();
            minClicked = x < getButtonWidth();
            maxClicked = x >= getWidth() - getButtonWidth();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            minClicked = false;
            maxClicked = false;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) { }
    @Override
    public void mouseExited(MouseEvent mouseEvent) { }
}
