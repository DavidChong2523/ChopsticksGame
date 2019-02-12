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
	
	public void set(boolean isP1, int hand, int value)
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
			