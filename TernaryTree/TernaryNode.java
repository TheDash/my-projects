package datastructures;

public class TernaryNode {

	char letter;
	TernaryNode leftLetter, centerLetter, rightLetter;
	boolean wordEnd;
	
	public TernaryNode(char ch, boolean wordEnd) {
		this.wordEnd = wordEnd;
		this.letter = ch;
	}
}
