
public class TrieNonLeaf extends TrieNode {
	
	public boolean endOfWord = false;
	public String letters;
	public TrieNode[] ptrs = new TrieNode[1];
	
	public TrieNonLeaf() {
		isLeaf = false;
	}
	
	public TrieNonLeaf(char ch) {
		letters = new String();
		letters += ch;
		isLeaf = false;
	}
	

}

