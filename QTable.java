import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class QTable
{	
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

	final double NEG_INFINITY = -1000;

	/* first 4 dimensions represent state:
	   dimension 1 represents the player's left hand
	   dimension 2 represents the player's right hand
	   dimension 3 represents the opponent's left hand
	   dimension 4 represents the opponent's right hand
	
	   final dimension represents possible actions:
	   index 0-3 represent hits: left-left, left-right, right-left, right-right
	   index 4-7 represent splits: 1, 2, 3, or 4
	*/ 
	private double[][][][][] table;
	private double alpha;		// learning rate, 0 < a <= 1
	private double gamma;		// discount factor
	private double epsilon;		// exploration factor
	
	public QTable(double a, double y, double e)
	{
		initTable();
		alpha = a;
		gamma = y;
		epsilon = e;
	}
	
	private void initTable()
	{
		table = new double[5][5][5][5][8];
		
		// populate table using normal distribution
		// illegal actions get value of -1000
		Random randNum = new Random();
		for(int a = 0; a < 5; a++) {
		for(int b = 0; b < 5; b++) {
		for(int c = 0; c < 5; c++) {
		for(int d = 0; d < 5; d++) {
		for(int e = 0; e < 8; e++)
		{
			if(isLegalAction(a, b, c, d, e))
				table[a][b][c][d][e] = randNum.nextGaussian();
			else
				table[a][b][c][d][e] = NEG_INFINITY;
		}}}}}
	}

	public void setAlpha(double a)
	{
		alpha = a;
	}

	public double getAlpha()	
	{
		return alpha;
	}

	public void setGamma(double y)
	{
		gamma = y;
	}

	public double setGamma()
	{
		return gamma;
	}

	public void setEpsilon(double e)
	{
		epsilon = e;
	}

	public double getEpsilon(double e)
	{
		return epsilon;
	}

	private boolean isLegalAction(int playerLeft, int playerRight, int opponentLeft, int opponentRight, int action)
	{
		// hit
		if(action < SPLIT_1)	
		{
			switch(action)
			{
			case HIT_LL:
				return (playerLeft != 0 && opponentLeft != 0);
			case HIT_LR:
				return (playerLeft != 0 && opponentRight != 0);
			case HIT_RL:
				return (playerRight != 0 && opponentLeft != 0);
			case HIT_RR:
				return (playerRight != 0 && opponentRight != 0);
			default:
				return false;
			}	
		}
		// split
		else
		{
			int maxValue = (playerLeft > playerRight) ? playerLeft : playerRight;
			if(action < maxValue)
				return false;
			
			int splitValue = action - 3;
			if(maxValue == playerLeft)
				return (playerRight + splitValue) != maxValue;
			else
				return (playerLeft + splitValue) != maxValue;
		}
	}

	public int action(ChopsticksBoard board, boolean isP1)
	{
		Random randNum = new Random();
		if(randNum.nextDouble() < epsilon)
		{
			int randIndex = randNum.nextInt(8);
			playMove(board, isP1, randIndex);
			return randIndex;				
		}

		double[] actions = table[board.get(isP1, LEFT)][board.get(isP1, RIGHT)]
					[board.get(!isP1, LEFT)][board.get(!isP1, RIGHT)];
		
		int maxIndex = -1;
		for(int i = 0; i < actions.length; i++)
		{
			if(actions[i] == NEG_INFINITY)
				continue;
			else if(maxIndex == -1)
				maxIndex = i;
			else if(actions[maxIndex] < actions[i])
				maxIndex = i;
		}

		playMove(board, isP1, maxIndex);
		return maxIndex;
	}

	private void playMove(ChopsticksBoard board, boolean isP1, int actionIndex)
	{
		switch(actionIndex)
		{
		case HIT_LL:
			hit(board, isP1, LEFT, LEFT);
			break;
		case HIT_LR:
			hit(board, isP1, LEFT, RIGHT);
			break;
		case HIT_RL:
			hit(board, isP1, RIGHT, LEFT);
			break;
		case HIT_RR:
			hit(board, isP1, RIGHT, RIGHT);
			break;
		case SPLIT_1:
			split(board, isP1, 1);
			break;
		case SPLIT_2:
			split(board, isP1, 2);
			break;
		case SPLIT_3:
			split(board, isP1, 3);
			break;
		case SPLIT_4:
			split(board, isP1, 4);
			break;
		default:
			return;
		}
	}

	private void hit(ChopsticksBoard board, boolean isP1, int playerHand, int opponentHand)
	{
		int playerValue = board.get(isP1, playerHand);
		int opponentValue = board.get(isP1, opponentHand);
		board.set(!isP1, opponentHand, playerValue + opponentValue);
	}

	private void split(ChopsticksBoard board, boolean isP1, int subtractValue)
	{
		int playerHand = (board.get(isP1, LEFT) > board.get(isP1, RIGHT)) ? LEFT : RIGHT;
		int subtractHandValue = board.get(isP1, playerHand);
		int addHandValue = board.get(isP1, 1 - playerHand);
		board.set(isP1, playerHand, subtractHandValue - subtractValue);
		board.set(isP1, 1 - playerHand, addHandValue + subtractValue);
	}

	// Qnew(St, At) = (1-a) * Qcurr(St, At) + a * (r + y * Qmax(St+1, At+1))
	public void updateTable(int[] state, int[] nextState, int action, double reward)
	{
		double currentVal = table[state[0]][state[1]][state[2]][state[3]][action];
		double[] nextActions = table[nextState[0]][nextState[1]][nextState[2]][nextState[3]];
		
		double maxFutureQ = NEG_INFINITY;
		for(int i = 0; i < nextActions.length; i++)
			maxFutureQ = (maxFutureQ < nextActions[i]) ? nextActions[i] : maxFutureQ;
	
		double updatedVal = (1 - alpha) * currentVal + alpha * (reward + gamma * maxFutureQ);
		currentVal = table[state[0]][state[1]][state[2]][state[3]][action] = updatedVal;	
	}

	public void save(String fileName)
	{
		File file = new File(fileName);
		try
		{
			if(!file.createNewFile())
			{
				file.delete();
				file.createNewFile();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(int a = 0; a < 5; a++) {
			for(int b = 0; b < 5; b++) {
			for(int c = 0; c < 5; c++) {
			for(int d = 0; d < 5; d++) {
				for(int e = 0; e < 8; e++)
					writer.write(Double.toString(table[a][b][c][d][e]) + " ");
				writer.newLine();
			}}}}

			writer.write("END");
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			System.out.println("save IOException");	
		}	
	}

	private void load(String fileName)
	{
		final String NEW_LINE = System.getProperty("line.separator");
		File file = new File(fileName);
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			for(int a = 0; a < 5; a++) {
			for(int b = 0; b < 5; b++) {
			for(int c = 0; c < 5; c++) {
			for(int d = 0; d < 5; d++) {
				String actionLine = reader.readLine();
				int index = 0;
				for(int e = 0; e < 8; e++)
				{
					String stringVal = "";
					while(actionLine.charAt(index) != ' ')
					{
						stringVal += actionLine.charAt(index);
						index++;	
					}
					index++;

					double doubleVal = Double.parseDouble(stringVal);
					table[a][b][c][d][e] = doubleVal;
				}		
			}}}}
		}
		catch(IOException e)
		{
			System.out.println("load IOException");
		}
	}
}