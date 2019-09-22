public class ChopsticksBoard
{
	private static final int LEFT = 0;
	private static final int RIGHT = 1;	

	// player hands are represented with an array of size 2
	// the first element represents the left hand 
	// the second element represents the right hand
	private int[] player1 = new int[2];
	private int[] player2 = new int[2];
	
	// initialize the board to the beginning state of the game
	public ChopsticksBoard()
	{		
		player1[LEFT] = 1;
		player1[RIGHT] = 1;
		player2[LEFT] = 1;
		player2[RIGHT] = 1;
	}
	
	// initialize the board to a custom state
	public ChopsticksBoard(int l1, int r1, int l2, int r2)
	{
		set(true, LEFT, l1);
		set(true, RIGHT, r1);
		set(false, LEFT, l2);
		set(false, RIGHT, r2);
	}

	// copy given board
	public ChopsticksBoard(ChopsticksBoard copyBoard)
	{
		set(true, LEFT, copyBoard.get(true, LEFT));
		set(true, RIGHT, copyBoard.get(true, RIGHT));
		set(false, LEFT, copyBoard.get(false, LEFT));
		set(false, RIGHT, copyBoard.get(false, RIGHT));
	}
	
	private void set(boolean isP1, int hand, int value)
	{
		// the range of hand values is 0 to 4
		if(isP1)
			player1[hand] = (value % 5);
		else
			player2[hand] = (value % 5);
	}
		
	public int get(boolean isP1, int hand)
	{
		if(isP1)
			return player1[hand];
		else
			return player2[hand];
	}

	// hit opponentHand with playerHand
	// returns true if the action was completed
	// returns false if the action was invalid
	public boolean hit(boolean isP1, int playerHand, int opponentHand)
	{
		if(playerHand != LEFT && playerHand != RIGHT)
			return false;
		if(opponentHand != LEFT && opponentHand != RIGHT)
			return false;

		// get value of playerHand and opponentHand
		int playerValue = get(isP1, playerHand);
		int opponentValue = get(!isP1, opponentHand);

		// check that action is valid
		if(playerValue > 0 && opponentValue > 0) 
		{
			// set opponentHand to sum of playerHand and opponentHand
			set(!isP1, opponentHand, (playerValue + opponentValue));

			return true;
		}
		
		return false;
	}

	// split player's total hand values between their two hands
	// returns true if the action was completed
	// returns false if the action was invalid
	public boolean split(boolean isP1, int subtractHand, int value)
	{
		if(subtractHand != LEFT && subtractHand != RIGHT)
			return false;

		// get values of player's hands
		int addHand = (1 - subtractHand);
		int subtractHandValue = get(isP1, subtractHand);
		int addHandValue = get(isP1, addHand);
	
		// check for validity
		if(value > 0 && subtractHandValue >= value)
		{
			if(subtractHandValue != (addHandValue + value))
			{
				// subtract value from subtractHand and add it to addHand
				set(isP1, subtractHand, (subtractHandValue - value));
				set(isP1, addHand, (addHandValue + value));
				return true;
			}
		}
	
		return false;
	}

	public boolean winP1()
	{
		return (player2[LEFT] == 0) && 
		       (player2[RIGHT] == 0);
	}
	
	public boolean winP2()
	{
		return (player1[LEFT] == 0) &&
		       (player1[RIGHT] == 0);
	}

	public void display()
	{
		// Print P1 left hand
		System.out.println("P1:");
		for(int i = 0; i < player1[LEFT]; i++)
		{
			System.out.print("|");
		}
		System.out.print("\t");

		// Print P1 right hand
		for(int i = 0; i < player1[RIGHT]; i++)
		{
			System.out.print("|");
		}
		System.out.println();
	
		// Print P2 left hand
		System.out.println("P2:");
		for(int i = 0; i < player2[LEFT]; i++)
 		{
			System.out.print("|");
		}
		System.out.print("\t");

		// Print P2 right hand
		for(int i = 0; i < player2[RIGHT]; i++)
		{	
			System.out.print("|");
		}
		System.out.println("\n");
	}
}
			