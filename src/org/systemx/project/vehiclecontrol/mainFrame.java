package org.systemx.project.vehiclecontrol;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import org.systemx.project.control.VehicleControl;
import org.systemx.qlearning.QLearning;

public class mainFrame extends JFrame implements KeyListener {
	private mainDraw draw;
	private VehicleControl vc;

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
		}else if (e.getKeyCode() == KeyEvent.VK_S) {
			QLearning.SaveList();
		}else if (e.getKeyCode() == KeyEvent.VK_A) {
			vc.camAttackLeft();
		}else if (e.getKeyCode() == KeyEvent.VK_Z) {
			vc.camAttackFront();
		}else if (e.getKeyCode() == KeyEvent.VK_E) {
			vc.camAttackRight();
		}
	}

	public void keyReleased(KeyEvent e) {
		draw.clear();
	}

	public void keyTyped(KeyEvent e) {

	}

	public mainFrame(mainDraw draw, VehicleControl vc) {
		this.draw = draw;
		this.vc = vc;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}

	public void run() {
		this.setTitle("Square Move Practice");
		this.setResizable(false);
		this.setSize(150, 130);
		this.setMinimumSize(new Dimension(150,130));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(this.draw);
		this.pack();
		this.setVisible(true);
	}
}