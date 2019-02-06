package dice3d.main;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dice3d.models.cuboids.Cuboid;
import dice3d.models.cuboids.Dice;


public class MainWindow extends JPanel {
	World w = new World();


	public MainWindow() {
		addKeyListener(new KeyHandler());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw((Graphics2D) g);
	}


	
	public void draw(Graphics2D g) {
		for(Cuboid c : w.cuboids) {
			c.draw(g);
		}
		int yOffset = 0;
		g.drawString("Press space key to roll again", 10, yOffset =+ 20);
		
		for(int i = 0; i < w.cuboids.size(); i++) {
			Cuboid c = w.cuboids.get(i);
			if(c instanceof Dice) {
				Dice d = (Dice) c;
				g.drawString("dice[" + i + "]: moving=" + !d.notMoving() + " number_rolled=" + d.getNumberRolled(), 10, yOffset += 20);
			} else {
				g.drawString("cuboid[" + i + "]: moving=" + !c.notMoving(), 10, yOffset += 20);
			}
			
		}

	
		repaint();
	}

	private class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getID() != KeyEvent.KEY_PRESSED) {
				return;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				for(Cuboid c : w.cuboids) {
					c.reset();
				}
			}
			
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow view = new MainWindow();
				JFrame frame = new JFrame();
				frame.setTitle("Dice");
				frame.getContentPane().add(view);
				frame.setSize(1280, 720);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// frame.setResizable(false);
				frame.setVisible(true);
				view.requestFocus();
			}
		});
	}

}
