public abstract class ChopsticksPlayer
{	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	private boolean m_isP1;
		
	public ChopsticksPlayer(boolean player)
	{
		m_isP1 = player;
	}

	public void chooseAction(ChopsticksBoard board)
	{
	
	}
	
	// hit opponentHand with playerHand
	// returns true if the action was completed
	// returns false if the action was invalid
	public static boolean hit(ChopsticksBoard board, boolean isP1, int playerHand, int opponentHand)
	{
		// get value of playerHand and opponentHand
		int playerValue = board.get(isP1, playerHand);
		int opponentValue = board.get(!isP1, opponentHand);

		// check that action is valid
		if(playerValue > 0 && opponentValue > 0) 
		{
			// set opponentHand to sum of playerHand and opponentHand
			board.set(!isP1, opponentHand, (playerValue + opponentValue));
			return true;
		}
		
		return false;
	}

	// split player's total hand values between their two hands
	// returns true if the action was completed
	// returns false if the action was invalid
	public static boolean split(ChopsticksBoard board, boolean isP1, int subtractHand, int value)
	{
		// get values of player's hands
		int addHand = (1 - subtractHand);
		int subtractHandValue = board.get(isP1, subtractHand);
		int addHandValue = board.get(isP1, addHand);
	
		// check for validity
		if(value > 0 && subtractHandValue >= value)
		{
			if(subtractHandValue != (addHandValue + value))
			{
				// subtract value from subtractHand and add it to addHand
				board.set(isP1, subtractHand, (subtractHandValue - value));
				board.set(isP1, addHand, (addHandValue + value));
				return true;
			}
		}
	
		return false;	
	}

	// returns player's side
	public boolean getPlayer()
	{
		return m_isP1;
	}
}