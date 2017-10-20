package org.systemx.vehiclecontrol;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class mainFrame extends JFrame implements KeyListener {
	private mainDraw draw;
	private vehicleControl vc;

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			draw.moveRight();
			vc.moveRight();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			draw.moveLeft();
			vc.moveLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			draw.moveDown();
			vc.moveDown();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			draw.moveUp();
			vc.moveUp();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	public mainFrame(mainDraw draw, vehicleControl vc) {
		this.draw = draw;
		this.vc = vc;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}

	public void run() {
		this.setTitle("Square Move Practice");
		this.setResizable(false);
		this.setSize(600, 600);
		this.setMinimumSize(new Dimension(600, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(this.draw);
		this.pack();
		this.setVisible(true);
	}
}