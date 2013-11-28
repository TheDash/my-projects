import java.util.ArrayList;


public class BigTwoGame {

	private static int NUMBER_OF_PLAYERS = 4;
	private static ArrayList<Player> players = new ArrayList<Player>(NUMBER_OF_PLAYERS);
	
	
	public static void main(String args[])
	{
		
		
		
		Deck.createNewDeck();
		
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++)
		{
			players.add(new Player());
		}
		
	}
	
}
