import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import util.kdtree.KDTree;

public class TreePanel extends JComponent implements Observer {
	private KDTree tree;
	
	private Point query;
	private Point nearest;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int r = 3;
		if (this.tree != null) {
			this.tree.draw(g, r, 0, 0, this.getWidth(), this.getHeight());
		}
		if (this.query != null) {
			g.setColor(Color.BLUE);
			g.fillOval(this.query.x - r, this.query.y - r, r * 2, r * 2);
			g.setColor(Color.BLACK);
			g.drawOval(this.query.x - r, this.query.y - r, r * 2, r * 2);
		}
		if (this.nearest != null) {
			g.setColor(Color.RED);
			g.fillOval(this.nearest.x - r, this.nearest.y - r, r * 2, r * 2);
			g.setColor(Color.BLACK);
			g.drawOval(this.nearest.x - r, this.nearest.y - r, r * 2, r * 2);
		}
	}

	public void update(Observable o, Object arg) {
		if (arg instanceof KDTree) {
			this.query = null;
			this.nearest = null;
			this.tree = (KDTree) arg;
			this.repaint();
		} else if (arg instanceof Point) {
			this.query = (Point) arg;
			this.nearest = this.tree.getNearest(this.query);
			this.repaint();
		}
	}
}
