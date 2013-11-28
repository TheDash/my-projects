import java.util.ArrayList;


public class Hand {

	private ArrayList<Card> cards;
	private int handSize;
	
	public Hand(int handSize)
	{
		cards = new ArrayList(handSize);
		
		for (int i = 0; i < cards.size(); i++)
		{
			cards.add(i, new Card());
		}
	}
	
}
