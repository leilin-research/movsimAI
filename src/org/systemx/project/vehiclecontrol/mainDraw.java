package org.systemx.project.vehiclecontrol;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class mainDraw extends JComponent {

    public int x = 1000;
    public int y = 1000;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(x, y, 50, 50);
        g.fillRect(x, y, 50, 50);
        g.setColor(Color.green);
    }

    public void moveRight() {
        x = 100;
        y = 50;
        repaint();
    }

    public void moveLeft() {
        x = 0;
        y = 50;
        repaint();
    }

    public void moveDown() {
        x = 50;
        y = 50;
        repaint();
    }

    public void moveUp() {
        x = 50;
        y = 0;
        repaint();
    }
    
    public void clear() {
        x = 1000;
        y = 1000;
        repaint();
    }
}
