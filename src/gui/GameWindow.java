package gui;

import gui.views.StartScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diese Klasse bietet das Fenster wo die grafische Oberfläche angezeigt wird.
 *
 * @author Roman Hergenreder
 */
public class GameWindow extends JFrame {

    private View activeView;
    private Resources resources;

    private GameWindow(Resources resources) {
        this.resources = resources;
        this.initWindow();
        this.setView(new StartScreen(this));
        this.setVisible(true);
    }

    private void initWindow() {

        LookAndFeel laf = UIManager.getLookAndFeel();
        if(laf.getSupportsWindowDecorations()) {
            UIManager.getCrossPlatformLookAndFeelClassName();
        }

        this.setTitle("Game of Castles - FOP Projekt WiSe 18/19");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setMinimumSize(new Dimension(600, 400));
        this.setLocationRelativeTo(null); // Center Window
        this.addWindowStateListener(windowStateListener); // Resize Event
    }

    public void setView(View view) {
        this.activeView = view;
        this.activeView.setSize(getContentPane().getSize());
        this.setContentPane(view);
        this.requestFocus();
    }

    private WindowStateListener windowStateListener = new WindowStateListener() {
        @Override
        public void windowStateChanged(WindowEvent windowEvent) {
            if ((windowEvent.getNewState() & JFrame.MAXIMIZED_BOTH) != JFrame.MAXIMIZED_BOTH) {
                activeView.onResize();
            }
        }
    };

    public static void main(String[] args) {

        Resources resources = Resources.getInstance();
        if(!resources.load())
            return;

        new GameWindow(resources);

        Runtime.getRuntime().addShutdownHook(new Thread(resources::save));
    }

    public Resources getResources() {
        return this.resources;
    }
}
