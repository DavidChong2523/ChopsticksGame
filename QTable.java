import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class QTable
{	
	final double WIN_REWARD = 1;	
	final double LOSE_REWARD = -1;

	/* first 4 dimensions represent state:
	   dimension 1 represents the player's left hand
	   dimension 2 represents the player's right hand
	   dimension 3 represents the opponent's left hand
	   dimension 4 represents the opponent's right hand
	
	   final dimension represents possible actions:
	   value 0-3 represent hits: left-left, left-right, right-left, right-right
	   value 4-7 represent splits: 1, 2, 3, or 4
	*/ 
	private double[][][][][] table;
	
	public QTable()
	{
		initTable();
	}	
	
	public QTable(String fileName)
	{
		table = new double[5][5][5][5][8];
		load(fileName);
	}

	public static int[] getState(ChopsticksBoard board, boolean player)
	{
		int[] state = new int[4];
		state[0] = board.get(player, Constants.LEFT);
		state[1] = board.get(player, Constants.RIGHT);
		state[2] = board.get(!player, Constants.LEFT);
		state[3] = board.get(!player, Constants.RIGHT);
		return state;
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
			if(a == 0 && b == 0)
				table[a][b][c][d][e] = WIN_REWARD;
			else if(c == 0 && d == 0)
				table[a][b][c][d][e] = LOSE_REWARD;
			else if(isLegalAction(a, b, c, d, e))
				table[a][b][c][d][e] = randNum.nextGaussian();
			else
				table[a][b][c][d][e] = Constants.NEG_INF;
		}}}}}
	}

	private boolean isLegalAction(int playerLeft, int playerRight, int opponentLeft, int opponentRight, int action)
	{
		// hit
		if(action < Constants.SPLIT_1)	
		{
			switch(action)
			{
			case Constants.HIT_LL:
				return (playerLeft != 0 && opponentLeft != 0);
			case Constants.HIT_LR:
				return (playerLeft != 0 && opponentRight != 0);
			case Constants.HIT_RL:
				return (playerRight != 0 && opponentLeft != 0);
			case Constants.HIT_RR:
				return (playerRight != 0 && opponentRight != 0);
			default:
				return false;
			}	
		}
		// split
		else
		{
			int maxValue = (playerLeft > playerRight) ? playerLeft : playerRight;
			int splitValue = action - 3;

			if(splitValue > maxValue)
				return false;

			if(maxValue == playerLeft)
				return (playerRight + splitValue) != maxValue;
			else
				return (playerLeft + splitValue) != maxValue;
		}
	}

	public int action(int[] state, double epsilon)
	{
		double[] actions = table[state[0]][state[1]][state[2]][state[3]];
		
		Random randNum = new Random();
		if(randNum.nextDouble() < epsilon)		// random move
		{
			ArrayList<Integer> legalActions = new ArrayList<Integer>();
			for(int i = 0; i < actions.length; i++)
			{
				if(actions[i] == Constants.NEG_INF)
					continue;

				legalActions.add(i);
			}

			int randIndex = legalActions.get(randNum.nextInt(legalActions.size()));
			return randIndex;	
		}

		int maxIndex = -1;
		for(int i = 0; i < actions.length; i++)		// best move
		{
			if(actions[i] == Constants.NEG_INF)
				continue;
			else if(maxIndex == -1)
				maxIndex = i;
			else if(actions[maxIndex] < actions[i])
				maxIndex = i;
		}

		return maxIndex;
	}
	
	// Qnew(St, At) = (1-a) * Qcurr(St, At) + a * (r + y * Qmax(St+1, At+1))
	public void update(double alpha, double gamma, int[] state, int[] nextState, int action, double reward)
	{
		double currentVal = table[state[0]][state[1]][state[2]][state[3]][action];
		double[] nextActions = table[nextState[0]][nextState[1]][nextState[2]][nextState[3]];
		
		double maxFutureQ = Constants.NEG_INF;
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

	public void display()
	{
		for(int a = 0; a < 5; a++) {
		for(int b = 0; b < 5; b++) {	
		for(int c = 0; c < 5; c++) {
		for(int d = 0; d < 5; d++) {
			System.out.println("State: ");	
			System.out.print("Player: "); 
			for(int i = 0; i < a; i++)
				System.out.print("|");
			for(int i = a; i < 5; i++)
				System.out.print(" ");
			for(int i = 0; i < b; i++)
				System.out.print("|");
			for(int i = b; i < 5; i++)
				System.out.print(" ");
			System.out.print("Opponent: ");
			for(int i = 0; i < c; i++)
				System.out.print("|");
			for(int i = c; i < 5; i++)
				System.out.print(" ");
			for(int i = 0; i < d; i++)
				System.out.print("|");
			for(int i = d; i < 5; i++)
				System.out.print(" ");
			System.out.println();
			System.out.println("Actions: ");
			for(int e = 0; e < 8; e++)
				System.out.print(Double.toString(table[a][b][c][d][e]) + " ");
			System.out.println();
		}}}}
	}
}