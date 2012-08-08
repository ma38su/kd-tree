package util.kdtree;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Semidynamic k-d Tree
 * @author ma38su
 */
public class KDTree {
	private final int cutoff = 5;
	private int[] perm;
	private Point[] points;
	private Node root;
	public KDTree(Point[] points) {
		this.points = points;
		this.perm = new int[points.length];
		for (int i = 0; i < this.perm.length; i++) {
			this.perm[i] = i;
		}
		this.root = build(0, this.points.length - 1);
	}

	private Node build(int l, int u) {
		if (u - l + 1 <= this.cutoff) {
			return new Bucket(l, u);
		} else {
			boolean cutdim = this.compareMaxSpread(l, u);
			int m = (l + u) / 2;
			select(l, u, m, cutdim);
			int cutval = cutdim ? this.points[this.perm[m]].x : this.points[this.perm[m]].y;
			Node loson = build(l, m);
			Node hison = build(m + 1, u);
			return new InternalNode(cutdim, cutval, loson, hison);
		}
	}
	
	private boolean compareMaxSpread(int l, int u) {
		int index = this.perm[l];
		int minX = this.points[index].x;
		int minY = this.points[index].y;
		int maxX = this.points[index].x;
		int maxY = this.points[index].y;
		for (int i = l + 1; i <= u; i++) {
			index = this.perm[i];
			if (minX > this.points[index].x) {
				minX = this.points[index].x;
			} else if (maxX < this.points[index].x) {
				maxX = this.points[index].x;
			}
			if (minY > this.points[index].y) {
				minY = this.points[index].y;
			} else if (maxY < this.points[index].y) {
				maxY = this.points[index].y;
			}
		}
		return (maxX - minX) > (maxY - minY);
	}

	public void draw(Graphics g, int r, int minX, int minY, int maxX, int maxY) {
		for (Point p : this.points) {
			g.drawOval(p.x - r, p.y - r, r * 2, r * 2);
		}
		Node node = this.root;
		this.drawCutLine(g, node, minX, minY, maxX, maxY);
	}
	
	public void drawCutLine(Graphics g, Node node, int minX, int minY, int maxX, int maxY) {
		if (!node.isBucket()) {
			InternalNode n = (InternalNode) node;
			if (n.cutdim) {
				g.drawLine(n.cutval, minY, n.cutval, maxY);
				this.drawCutLine(g, n.loson, minX, minY, n.cutval, maxY);
				this.drawCutLine(g, n.hison, n.cutval, minY, maxX, maxY);
			} else {
				g.drawLine(minX, n.cutval, maxX, n.cutval);
				this.drawCutLine(g, n.loson, minX, minY, maxX, n.cutval);
				this.drawCutLine(g, n.hison, minX, n.cutval, maxX, maxY);
			}
		}
	}

	public Point[] getPoints() {
		return this.points;
	}

	private int partition(int left, int right, boolean cutdim) {
		int storeIndex = left;
		if (cutdim) {
			int pivot = this.points[this.perm[right]].x;
			for (int i = left; i < right; i++) {
				if (this.points[this.perm[i]].x <= pivot) {
					swap(i, storeIndex++);
				}
			}
		} else {
			int pivot = this.points[this.perm[right]].y;
			for (int i = left; i < right; i++) {
				if (this.points[this.perm[i]].y <= pivot) {
					swap(i, storeIndex++);
				}
			}
		}
		swap(right, storeIndex);
		return storeIndex;
	}

	public Point getNearest(Point p) {
		int[] nn = new int[2];
		nn[0] = Integer.MAX_VALUE;
		nn[1] = -1;
		this.rnn(this.root, p, nn);
		return this.points[nn[1]];
	}
	
	public void rnn(Node node, Point p, int[] nn) {
		if (node.isBucket()) {
			Bucket bucket = (Bucket) node;
			for (int i = bucket.lopt; i <= bucket.hipt; i++) {
				int index = this.perm[i];
				int dx = (this.points[index].x - p.x);
				int dy = (this.points[index].y - p.y);
				int d2 = dx * dx + dy * dy;
				if (d2 < nn[0]) {
					nn[0] = d2;
					nn[1] = index;
				}
			}
		} else {
			InternalNode in = (InternalNode)node;
			int diff = (in.cutdim ? p.x : p.y) - in.cutval;
			if (diff < 0) {
				rnn(in.loson, p, nn);
				if (nn[0] >= diff * diff) {
					rnn(in.hison, p, nn);
				}
			} else {
				rnn(in.hison, p, nn);
				if (nn[0] >= diff * diff) {
					rnn(in.loson, p, nn);
				}
			}
		}
	}
	
	private void select(int left, int right, int m, boolean cutdim) {
		int index;
		if (cutdim) {
			do {
				index = this.partition(left, right, cutdim);
				if (index > m) {
					right = index - 1;
				} else if (index < m) {
					left = index + 1;
				}
			} while (index != m);
		} else {
			do {
				index = this.partition(left, right, cutdim);
				if (index > m) {
					right = index - 1;
				} else if (index < m) {
					left = index + 1;
				}
			} while (index != m);
		}
	}

	private void swap(int i1, int i2) {
		int tmp = this.perm[i1];
		this.perm[i1] = this.perm[i2];
		this.perm[i2] = tmp;
	}
}
