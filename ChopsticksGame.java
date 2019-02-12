import java.util.Scanner;

class ChopsticksGame
{
	private static final int LEFT = 0;
	private static final int RIGHT = 1;

	private ChopsticksBoard board;
	private ChopsticksPlayer player1;
	private ChopsticksPlayer player2;
	private boolean isP1move;

	public ChopsticksGame()
	{
		board = new ChopsticksBoard();
		isP1move = true;
	}

	// play from custom board position
	public void debug()
	{
		initialize();

		// enter board position as a 4 digit number
		// the first two digits are player1's left and right hand values
		// the second two digits are player2's left and right hand values
		System.out.println("Enter board position: ");
		Scanner input = new Scanner(System.in);
		int position = input.nextInt();		

		board.set(false, RIGHT, position % 10);
		position /= 10;
		board.set(false, LEFT, position % 10);
		position /= 10;
		board.set(true, RIGHT, position % 10);
		position /= 10;
		board.set(true, LEFT, position % 10);

		playGame();
	}
			 
	// play from default start position
	public void startGame()
	{
		initialize();
		playGame();
	}

	// initialize player1 and player2
	private void initialize()
	{
		// choose player vs player or player vs computer
		System.out.println("Player vs Player(1) or Player vs Computer(2)?");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		
		// player vs player
		if(choice == 1)
		{
			player1 = new ChopsticksHumanPlayer(true);
			player2 = new ChopsticksHumanPlayer(false);
		}
		// player vs computer
		else
		{
			// set computer depth
			System.out.println("Set the computer depth: ");
			int depth = input.nextInt();

			// choose player side
			System.out.println("Are you player1(1) or player2(2)?");
			choice = input.nextInt();
			
			if(choice == 1)
			{
				player1 = new ChopsticksHumanPlayer(true);
				player2 = new ChopsticksComputerPlayer(false, depth);
			}
			else
			{
				player1 = new ChopsticksComputerPlayer(true, depth);
				player2 = new ChopsticksHumanPlayer(false);
			}
		}

		System.out.println();
	}

	// run game
	private void playGame()
	{
		// alternate between player1 and player2 until the game ends
		for(;;)
		{
			board.display();

			if(isP1move)
			{
				player1.chooseAction(board);
				isP1move = false;
			}
			else
			{
				player2.chooseAction(board);
				isP1move = true;
			}
	
			System.out.println();

			if(gameOver())
				break;
		}
	}

	// check for a game over state
	private boolean gameOver()
	{
		// P2 wins
		if(board.get(true, LEFT) == 0 &&
		   board.get(true, RIGHT) == 0)
		{
			board.display();
			System.out.println("P2 wins!");

			return true;
		}
		
		// P1 wins
		if(board.get(false, LEFT) == 0 && 
		   board.get(false, RIGHT) == 0)
		{
			board.display();
			System.out.println("P1 wins!");
		
			return true;
		}
	
		// game continues
		return false;
	}
}