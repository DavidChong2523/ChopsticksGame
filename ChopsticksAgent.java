public class ChopsticksAgent extends ChopsticksPlayer
{
	final int HIT_LL = 0;
	final int HIT_LR = 1;
	final int HIT_RL = 2;
	final int HIT_RR = 3;
	final int SPLIT_1 = 4;
	final int SPLIT_2 = 5;
	final int SPLIT_3 = 6;
	final int SPLIT_4 = 7;

	private double epsilon;
	private QTable table;
	
	public ChopsticksAgent(boolean isP1, double e, String fileName)
	{
		super(isP1);
		epsilon = e;
		table = new QTable(fileName);
	}

	public void chooseAction(ChopsticksBoard board)
	{
		boolean isP1 = getPlayer();

		int[] state = new int[] {board.get(isP1, LEFT), board.get(isP1, RIGHT),
					 board.get(!isP1, LEFT), board.get(!isP1, RIGHT)};

		int action = table.action(state, epsilon);
		parseAction(board, isP1, action);

	System.out.println("action: " + action);
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
