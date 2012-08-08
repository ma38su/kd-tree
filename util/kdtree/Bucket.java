package util.kdtree;

public class Bucket implements Node {
	int lopt;
	int hipt;
	public Bucket(int lopt, int hipt) {
		this.lopt = lopt;
		this.hipt = hipt;
	}
	public boolean isBucket() {
		return true;
	}
}
