import java.util.ArrayList;
import java.util.Random;


public class Deck {

	private static int cardCount;
	private static ArrayList<Card> deck = new ArrayList<Card>(52);
	private static Random generator = new Random();
	private static int SHUFFLE_COUNT = 3;
	
	private Deck()
	{
		
	}
	
	public static void createNewDeck()
	{
		cardCount = 0;
		int cardType = 0;
		
		ArrayList<Card> newDeck = new ArrayList<Card>(52);
		
		while (cardCount <= 52)
		{
			newDeck.add(new Card((cardCount % 13), (cardType % 3)));
			
			cardType++;
			cardCount++;
		}
		
		deck = newDeck;
		
		for (int i = 0; i < SHUFFLE_COUNT; i++)
		{
			shuffleDeck();
		}
		
		System.out.println("New Deck!");
	}
	
	public static void shuffleDeck()
	{
		
		ArrayList<Card> shuffledDeck = new ArrayList<Card>(52);
		while (cardCount > 0)
		{
			shuffledDeck.add(drawRandomCard());
		}
		
		deck = shuffledDeck;
		System.out.println("Shuffled!");
	}
	
	public static int getCardCount()
	{
		return cardCount;
	}

	public static Card drawRandomCard()
	{
		
		int cardIndex = generator.nextInt(cardCount);
		Card card = deck.get(cardIndex);
		deck.remove(cardIndex);
		cardCount--;
		
		System.out.println("Random card.");
		
		return card;
		
	}
	
	public static void addCard(Card c)
	{
		if (cardCount != 52 && deck.contains(c))
		{
			deck.add(c);
		}
		
		System.out.println("Added card to deck.");
		
		return;
	}
	
	public static Card drawFromTop()
	{
		Card c = deck.get(0);
		deck.remove(0);
		
		System.out.println("Drawn card.");
		
		return c;
	}
}
