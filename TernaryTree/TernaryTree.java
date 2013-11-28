package datastructures;

public class TernaryTree {

	
	public static void main(String args[]) {
		TernaryTree tt = new TernaryTree();
		tt.add("Alphabet");
		tt.add("Euietju");
		tt.add("fgfdgfdgt");
		tt.add("ninjvicjf");
	}
	
	private TernaryNode rootNode = null;
	
	private void add(String s, int pos, TernaryNode node) {
		
		
		char ltr = s.charAt(pos);
		
		if (node == null) {
			System.out.println("NUll");
			node = new TernaryNode(ltr, false);
		}
		
		System.out.println("Looking at ltr: " + ltr);
		
		if (ltr < node.letter) {
			add(s, pos, node.leftLetter);
			System.out.println("Went left compared to " + node.letter);
		} else if (ltr > node.letter) {
			add(s, pos, node.rightLetter);
			System.out.println("Went right compared to " + node.rightLetter);
		} else {
			if (pos+1 == s.length()) {
				node.wordEnd = true;
			} else {
				System.out.println("Went center compared to " + node.letter);
				add(s, pos+1, node.centerLetter);
			}
		}
		
	}
	
	public void add(String s) {
		if (s == null || s == "") {
			return;
		}			
		
		add(s, 0, rootNode);
	}
	
	public boolean contains(String s) {
		
		if (s == null || s == "") {
			return false;
		}
		
		int pos = 0;
		TernaryNode node = rootNode;
		char ltr = s.charAt(pos);
		while (node != null) {
			if (ltr < node.letter) {
				node = node.leftLetter;
			} else if (ltr > node.letter) {
				node = node.rightLetter;
			} else {
				if (++pos == s.length()) {
					System.out.println("Do we reach");
					return node.wordEnd;
				}
			}
		}
		return false;
	}
	
}
