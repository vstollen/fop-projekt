package gui.components;

import javax.swing.*;
import java.util.InputMismatchException;

public class NumberDialog {

    private String text;
    private int min, max, value;

    public NumberDialog(String text, int min, int max, int initial) {
        this.text = text;
        this.min = min;
        this.max = max;
        this.value = initial;
    }

    boolean showDialog(JComponent parent) {
        String result = JOptionPane.showInputDialog(parent, text, String.valueOf(value));

        if(result == null)
            return false;

        result = result.trim();
        try {
            value = Integer.parseInt(result);
            if(value >= min && value <= max)
                return true;
        } catch(NumberFormatException | InputMismatchException ignored) {
        }

        JOptionPane.showMessageDialog(parent, "Bitte gib eine gültige Zahl ein.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public int getValue() {
        return this.value;
    }
}
