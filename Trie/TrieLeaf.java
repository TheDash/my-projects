public class TrieLeaf extends TrieNode {

	public String suffix;
	
	public TrieLeaf() {
		isLeaf = true;
	}
	
	public TrieLeaf(String suffix) {
		this.suffix = new String(suffix);
		isLeaf = true;
	}
}

