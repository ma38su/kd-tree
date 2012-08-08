import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JApplet;

import util.kdtree.KDTree;

public class Main extends JApplet {
	final List<Point> points = new ArrayList<Point>();
	final Observable observable = new Observable() {
		@Override
		public void notifyObservers(Object arg) {
			super.setChanged();
			super.notifyObservers(arg);
		}
	};
	@Override
	public void init() {
		this.setLayout(new BorderLayout());
		TreePanel tpanel = new TreePanel();
		this.observable.addObserver(tpanel);
		this.add(tpanel, BorderLayout.CENTER);
		tpanel.addMouseListener(new MouseAdapter() {
			boolean flag = true;
			@Override
			public void mousePressed(MouseEvent e) {
				if (this.flag) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						Main.this.observable.notifyObservers(new Point(e.getX(), e.getY()));
					} else {
						Main.this.points.add(new Point(e.getX(), e.getY()));
						rebuild();
					}
					this.flag = false;
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				this.flag = true;
			}
		});
	}
	
	public void rebuild() {
		KDTree tree = new KDTree(this.points.toArray(new Point[]{}));
		this.observable.notifyObservers(tree);
	}
}
