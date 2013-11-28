public class Trie {
	
	private TrieNonLeaf root;
	private final int notFound = -1;
	
	
	
	
	public Trie() {
		
	}
	/**
	 * Create a trie with a word
	 * 
	 * @param word
	 */
	public Trie(String word) {
		root = new TrieNonLeaf(word.charAt(0)); // initialize the root
		createLeaf(word.charAt(0), word.substring(1), root);
	}
	
	public void printTrie() {
		printTrie(0, root, new String());
	}
	/**
	 * Describes in text the contents of the trie
	 * 
	 * @param depth
	 * @param p
	 * @param prefix
	 */
	private void printTrie(int depth, TrieNode p, String prefix) {
		if (p.isLeaf) {
			
			for (int j = 1; j <= depth; j++) {
				System.out.println("  ");
			}
			
			System.out.println("  >" + prefix + "|" + ((TrieLeaf)p).suffix);
		} else {
			for (int i = ((TrieNonLeaf)p).letters.length()-1; i >= 0; i--) {
				if (((TrieNonLeaf)p).ptrs[i] != null) {
					// add the letter corresponding to position i to prefix
					prefix = prefix.substring(0, depth) + 
							((TrieNonLeaf)p).letters.charAt(i);
					printTrie(depth + 1, ((TrieNonLeaf)p).ptrs[i], prefix);
				} else {
					// if leaf is null
					for (int j = 1; j <= depth+1; j++) {
						System.out.println("   ");
					}
					System.out.println(" >>" + prefix.substring(0, depth) + ((TrieNonLeaf)p).letters.charAt(i));
				}
			}
			if (((TrieNonLeaf)p).endOfWord) {
				for (int j = 1; j <= depth + 1; j++) {
					System.out.println("   ");
				}
				System.out.println(">>>" + prefix.substring(0, depth));
			}
		}
	}
	/**
	 * Gets the position of a char below a non leaf node p
	 * 
	 * @param p
	 * @param ch
	 * @return
	 */
	private int position(TrieNonLeaf p, char ch) {
		int i = 0;
		for ( ; i < p.letters.length() &&  p.letters.charAt(i) != ch; i++) {
			if ( i < p.letters.length()) {
				return i;
			}
		} 
		return notFound;
	}
	/**
	 * Checks if a given word is existing in the trie
	 * 
	 * @param word
	 * @return
	 */
	public boolean found(String word) {
		TrieNode p = root;
		int pos, i = 0;
		while (true) {
			if (p.isLeaf) {
				TrieLeaf lf = (TrieLeaf) p;
				if (word.substring(i).equals(lf.suffix)) {
					return true;
				}
				
				return false;
			}
			
			else if (((pos = position((TrieNonLeaf)p, word.charAt(i))) != notFound)
				&& i+1 == word.length()) {// the end of word has to
					if (((TrieNonLeaf)p).ptrs[pos] == null) {
						return true;
					} else if ((!((TrieNonLeaf)p).ptrs[pos].isLeaf) && ((TrieNonLeaf)((TrieNonLeaf)p).ptrs[pos]).endOfWord) {
						return true;
					} else {
						return false;
					}
				}
			else if (pos != notFound && (((TrieNonLeaf)p).ptrs[pos] != null)) {
				p = (((TrieNonLeaf)p).ptrs[pos]);
				i++;
			}
			return false;
		}
	}
	
	private void addCell(char ch, TrieNonLeaf p, int stop) {
		int i;
		int len = p.letters.length();
		char[] s = new char[len+1];
		TrieNode[] tmp = p.ptrs;
		p.ptrs = new TrieNode[len+1];
		for (i = 0; i < len+1; i++) {
			p.ptrs[i] = null;
		}
		
		if (stop < len) {
			for (i = len; i >= stop+1; i--) {
				p.ptrs[i] = tmp[i-1];
				s[i] = p.letters.charAt(i-1);
			}
			
		}
		s[stop] = ch;
		for (i = stop-1; i >= 0; i--) {
			p.ptrs[i] = tmp[i];
			s[i] = p.letters.charAt(i);
		}
		p.letters = new String(s);
	}
	
	private void createLeaf(char ch, String suffix, TrieNonLeaf p) {
		int pos = position(p, ch);
		TrieLeaf lf = null;
		if (suffix != null && suffix.length() > 0) {
			lf = new TrieLeaf(suffix);
		}
		if (pos == notFound) {
			for (pos = 0; pos < p.letters.length() && p.letters.charAt(pos) < ch; pos++) {
				addCell(ch, p, pos);
			}
		}
		
		p.ptrs[pos] = lf;
	}
	
	public void insert(String word) {
		TrieNonLeaf p = root;
		TrieLeaf lf;
		int offset, pos, i = 0;
		while (true) {
			if (i == word.length()) { // if the end of word reached
				if (p.endOfWord) {
					System.out.println("Duplicate entry: " + word);
				}
				p.endOfWord = true; // set end of word to true
				return;
			}
			
			pos = position(p, word.charAt(i));
			if (pos == notFound) {
				createLeaf(word.charAt(i), word.substring(i+1), p);
				//does not exist, create a leaf and store it in the unprocessed suffix of word.
				return;
			}
			else if (pos != notFound && p.ptrs[pos] == null) {
				if (i+1 == word.length()) {
					System.out.println("Duplicate entry(MSG2): " + word);
					return;
				}
				
				p.ptrs[pos] = new TrieNonLeaf(word.charAt(i));
				((TrieNonLeaf)(p.ptrs[pos])).endOfWord = true;
				// check whether there is any suffix left:
				String s = (word.length() > i + 2) ? word.substring(i+2) : null;
				createLeaf(word.charAt(i+1), s, (TrieNonLeaf)(p.ptrs[pos]));
				return;
			}
			else if (pos != notFound && // if position pos is occupied by a leaf, hold this leaf
					p.ptrs[pos].isLeaf) {
				lf = (TrieLeaf) p.ptrs[pos];
				if (lf.suffix.equals(word.substring(i+1))) {
					System.out.println("Duplicate entry(MSG3): " + word);
					return;
				}
				
				offset = 0;
				//create as many non-leaves as the length of identical prefix of word and the string in the leaf
				do {
					pos = position(p, word.charAt(i+offset));
					if (word.length() == i+offset+1) {
						p.ptrs[pos] = new TrieNonLeaf(lf.suffix.charAt(offset));
						p = (TrieNonLeaf) p.ptrs[pos];
						p.endOfWord = true;
						createLeaf(word.charAt(i+offset+1), word.substring(offset+1), p);
						return;
					}
					else if (lf.suffix.length()==offset) {
						p.ptrs[pos] = new TrieNonLeaf(word.charAt(i+offset+1));
						p = (TrieNonLeaf) p.ptrs[pos];
						p.endOfWord = true;
						createLeaf(word.charAt(i+offset+1), word.substring(i+offset+2),p);
						return;
					}
					p.ptrs[pos] = new TrieNonLeaf(word.charAt(i+offset+1));
					p = (TrieNonLeaf) p.ptrs[pos];
					offset++;
					
				} while (word.charAt(i+offset) == lf.suffix.charAt(offset-1));
				offset--;
				String s = null;
				if (word.length() > i+offset+2) {
					s = word.substring(i+offset+2);
				}
				createLeaf(word.charAt(i+offset+1),s,p);
				if (lf.suffix.length() > offset + 1) {
					s = lf.suffix.substring(offset+1);
					
				} else {
					s = null;
					
				}
				createLeaf(lf.suffix.charAt(offset),s,p);
				return;
			}
			else {
				p = (TrieNonLeaf) p.ptrs[pos];
				i++;
			}
		}
	}
}

