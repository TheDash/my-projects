


public class Card {

	private int cardType;
	private int value;
	
	public Card(int cardType, int value) 
	{
		this.cardType = cardType;
		this.value = value;
		
	}
	
	public boolean equals(Card c)
	{
		return this.value == c.value && this.cardType == c.cardType;
	}
	
	public boolean greaterThan(Card c)
	{
		if (c.value == this.value)
		{
			return c.cardType > this.cardType;
		}
		
		return this.value > c.value;
	}
	
	public boolean lessThan(Card c)
	{
		if (c.value == this.value)
		{
			return this.cardType < c.cardType;
		}
		
		return this.value < c.value;
	}
	
}
