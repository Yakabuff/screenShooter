package test;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectionRectangle extends JFrame{
	
	public SelectionRectangle() {
		this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0.25F));
        // opacity ranges 0.0-1.0 and is the fourth paramater
        this.add(new DrawPanel());
	}
	
	private class DrawPanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(0, 0, this.getWidth(), this.getHeight());
            // any other drawing
        } 
    }
}
