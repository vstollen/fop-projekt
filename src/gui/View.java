package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Roman Hergenreder
 */
public abstract class View extends Container implements ActionListener {

    private GameWindow gameWindow;
    protected static final Dimension BUTTON_SIZE = new Dimension(125, 40);

    public View(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.onInit();
        this.setSize(gameWindow.getSize());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                onResize();
            }
        });
    }

    public abstract void onResize();
    protected abstract void onInit();

    public static Font createFont(int Size) {
      return new Font("Times New Roman", Font.PLAIN, Size);
    }

    public static Font createCelticFont(float Size) {
        Resources resources = Resources.getInstance();
        return resources.getCelticFont().deriveFont(Size);
    }

    protected GameWindow getWindow() {
        return this.gameWindow;
    }

    protected JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setSize(BUTTON_SIZE);
        button.setFont(createFont(16));
        button.addActionListener(this);
        button.setBackground(new Color(54, 103, 53));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setFont(gameWindow.getResources().getCelticFont());
        this.add(button);
        return button;
    }

    public static Dimension calculateTextSize(String text, Font font) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        int width = (int)(font.getStringBounds(text, frc).getWidth());
        int height = (int)(font.getStringBounds(text, frc).getHeight());
        return new Dimension(width, height);
    }

    private Dimension calculateLabelSize(JLabel label) {
        return calculateTextSize(label.getText(), label.getFont());
    }

    protected JLabel createLabel(String text, int fontSize) {
        return createLabel(text, fontSize, false);
    }

    protected JLabel createLabel(String text, int fontSize, boolean underline) {
        JLabel label = new JLabel(text);
        label.setFont(createFont(fontSize));
        label.setSize(calculateLabelSize(label));

        if(underline) {
            Font font = label.getFont();
            Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
        }

        add(label);
        return label;
    }


    protected JComboBox createCombobox(Vector<String> values, int selectedIndex) {
        JComboBox<String> comboBox = new JComboBox<>(values);
        comboBox.setSelectedIndex(selectedIndex);
        comboBox.setSize(200, 25);
        add(comboBox);
        return comboBox;
    }

    protected JTextPane createTextPane() {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK),
                new EmptyBorder(3,3,3,3)
        ));
        return textPane;
    }

    protected JTextArea createTextArea(String text, boolean readonly) {
        JTextArea textArea = new JTextArea(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        if(readonly) {
            textArea.setEditable(false);
            textArea.setBackground(this.getBackground());
        }

        add(textArea);
        return textArea;
    }

    protected void showErrorMessage(String text, String title) {
        JOptionPane.showMessageDialog(this, text, title, JOptionPane.ERROR_MESSAGE);
    }
}
