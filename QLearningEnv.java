public class QLearningEnv
{
	final int MAX_GAME_MOVES = 50;

	final int LEFT = 0;
	final int RIGHT = 1;

	final int HIT_LL = 0;
	final int HIT_LR = 1;
	final int HIT_RL = 2;
	final int HIT_RR = 3;
	final int SPLIT_1 = 4;
	final int SPLIT_2 = 5;
	final int SPLIT_3 = 6;
	final int SPLIT_4 = 7;
	
	private QTable table;
	private double alpha;
	private double gamma;
	private double epsilon;
		
	public QLearningEnv(double a, double g, double e)
	{	
		table = new QTable();
		alpha = a;
		gamma = g;
		epsilon = e;
	}

	public QLearningEnv(double a, double g, double e, String fileName)
	{
		table = new QTable(fileName);
		alpha = a;
		gamma = g;
		epsilon = e;
	}

	// 1 for winning position
	// -1 for losing position
	// -0.1 otherwise
	private double reward(ChopsticksBoard board, boolean player)
	{
		if(player)	// player is P1
		{
			if(board.winP1())
				return 1;
			else if(board.winP2())
				return -1;
			else 
				return -0.1;
		}
		else		// player is P2
		{
			if(board.winP2())
				return 1;
			else if(board.winP1())
				return -1;
			else 
				return -0.1;
		}			
	} 

	public void train(int numEpisodes, String fileName)
	{
		ChopsticksPlayer randOpponent = new ChopsticksComputerPlayer(false, 0);
		ChopsticksPlayer qOpponent = new ChopsticksAgent(false, 0.3, "model_1.txt");
		ChopsticksPlayer opponent = qOpponent;

		int episode = numEpisodes;
		while(episode > 0)
		{
			ChopsticksBoard board = new ChopsticksBoard();
			trainingGame(board, opponent, true);

			if(episode % 100000 == 0)
				System.out.println("Episode " + episode + " finished");

			if(episode < 100 && board.winP1())
				System.out.println("Victory on episode " + episode);

			epsilon -= epsilon / numEpisodes;
			episode--;
		}		

		table.save(fileName);	
	}

	private void trainingGame(ChopsticksBoard board, ChopsticksPlayer opponent, boolean player)
	{
		int[] state = new int[] {-1, -1, -1, -1};
		int[] nextState = new int[] {-1, -1, -1, -1};
		int action = -1;

		boolean validState = false;
		boolean validNextState = false;
		
		int move = 0;
		boolean isP1Turn = true;
		while(!board.winP1() && !board.winP2() && move < MAX_GAME_MOVES)
		{
			if(isP1Turn == player)	// q-table move
			{
				state = new int[] {board.get(player, LEFT), board.get(player, RIGHT), 
						   board.get(!player, LEFT), board.get(!player, RIGHT)};

				action = table.action(state, epsilon);
				parseAction(board, true, action);

				validState = true;
				if(board.winP1() || board.winP2() || move < MAX_GAME_MOVES)
				{
					nextState = new int[] {board.get(player, LEFT), board.get(player, RIGHT),
							       board.get(!player, LEFT), board.get(!player, RIGHT)};

					validNextState = true;
				}
				else
					validNextState = false;
			}
			else			// opponent move
			{
				opponent.chooseAction(board);
				
				nextState = new int[] {board.get(player, LEFT), board.get(player, RIGHT),
						       board.get(!player, LEFT), board.get(!player, RIGHT)};

				validNextState = true;
			}

			if(validState && validNextState)
				table.update(alpha, gamma, state, nextState, action, reward(board, true));

			isP1Turn = !isP1Turn;
			if(isP1Turn)
				move++;
		}
	}

	private void parseAction(ChopsticksBoard board, boolean isP1, int action)
	{
		int value;	
		switch(action)
		{
		case HIT_LL:
			board.hit(isP1, LEFT, LEFT);
			return;
		case HIT_LR:
			board.hit(isP1, LEFT, RIGHT);
			return;
		case HIT_RL:
			board.hit(isP1, RIGHT, LEFT);
			return;
		case HIT_RR:
			board.hit(isP1, RIGHT, RIGHT);
			return;
		case SPLIT_1:
			value = 1;
			break;
		case SPLIT_2:
			value = 2;
			break;
		case SPLIT_3:
			value = 3;
			break;
		case SPLIT_4:
			value = 4;
			break;
		default:
			return;
		}

		int playerHand = (board.get(isP1, LEFT) > board.get(isP1, RIGHT) ? LEFT : RIGHT);
		board.split(isP1, playerHand, value);
	}
} 