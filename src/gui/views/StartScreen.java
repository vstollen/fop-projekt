package gui.views;

import gui.GameWindow;
import gui.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class StartScreen extends View {

    private JButton btnStart, btnStats, btnInfo, btnQuit;
    private JLabel lblTitle;

    public StartScreen(GameWindow gameWindow) {
        super(gameWindow);
    }

    @Override
    public void onResize() {
        int width = getWidth();
        int height = getHeight();
        int labelHeight = 40;

        int offsetY = (height - 4 * (BUTTON_SIZE.height + 15) - labelHeight) / 3;

        lblTitle.setSize(width, labelHeight);
        lblTitle.setLocation(0, offsetY);

        offsetY += labelHeight + 50;

        int offsetX = (width - BUTTON_SIZE.width) / 2;
        JButton[] buttons = { btnStart, btnStats, btnInfo, btnQuit };
        for (JButton button : buttons) {
            button.setLocation(offsetX, offsetY);
            offsetY += BUTTON_SIZE.height + 15;
        }
    }

    @Override
    protected void onInit() {
        this.lblTitle = createLabel("Game of Castles", 25);
        this.lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.lblTitle.setFont(View.createCelticFont(25));
        this.btnStart = createButton("Start");
        this.btnStats = createButton("Punkte");
        this.btnInfo = createButton("Info");
        this.btnQuit = createButton("Beenden");

        getWindow().setSize(750, 450);
        getWindow().setMinimumSize(new Dimension(600, 400));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnQuit) {
            getWindow().dispose();
        } else if(actionEvent.getSource() == btnStart) {
            getWindow().setView(new GameMenu(getWindow()));
        } else if(actionEvent.getSource() == btnInfo) {
            getWindow().setView(new InfoView(getWindow()));
        } else if(actionEvent.getSource() == btnStats) {
            getWindow().setView(new HighscoreView(getWindow()));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        BufferedImage soldiers[] = getWindow().getResources().getSoldiers();
        int width = 200;
        int height = (int) (((double)width / soldiers[0].getWidth()) * soldiers[0].getHeight());

        g.drawImage(soldiers[0], 25, 100, width, height, null);
        g.drawImage(soldiers[1], getWidth() - 25 - width, 100, width, height, null);
    }
}
