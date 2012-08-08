package util.kdtree;

public class InternalNode implements Node {
	boolean cutdim;
	int cutval;
	Node loson;
	Node hison;
	public InternalNode(boolean cutdim, int cutval, Node loson, Node hison) {
		this.cutdim = cutdim;
		this.cutval = cutval;
		this.loson = loson;
		this.hison = hison;
	}
	
	public boolean isBucket() {
		return false;
	}
}
