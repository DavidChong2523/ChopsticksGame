public class Test
{
	final static int LEFT = 0;
	final static int RIGHT = 1;

	public static void main(String[] args)
	{
	//	trainQTable();
		playQGame();
	}

	private static void playGame()
	{
		ChopsticksGame game = new ChopsticksGame();
		game.startGame();
	}

	private static void trainQTable()
	{
		QLearningEnv env = new QLearningEnv(0.2, 0.9, 1, "model_1.txt");
		env.train(10000000, "model_2.txt");
	}

	public static void playQGame()
	{
		ChopsticksBoard board = new ChopsticksBoard();
		ChopsticksPlayer opponent = new ChopsticksAgent(true, 0, "model_2.txt");
		ChopsticksPlayer player = new ChopsticksHumanPlayer(false);
		
		board.display();
		boolean isP1Turn = true;
		for(;;)
		{
			if(isP1Turn)
			{
				opponent.chooseAction(board);
				board.display();
			}
			else
			{
				player.chooseAction(board);
				board.display();
			}
			
			isP1Turn = !isP1Turn;

			if(board.winP1())
			{
				System.out.println("P1 wins!");
				break;
			}
			else if(board.winP2())
			{
				System.out.println("P2 wins!");
				break;
			}
		}
	}
}