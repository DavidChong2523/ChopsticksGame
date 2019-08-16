import java.util.Random;

public class QTable
{	
	final int HIT_LL = 0;
	final int HIT_LR = 1;
	final int HIT_RL = 2;
	final int HIT_RR = 3;
	final int SPLIT_1 = 4;
	final int SPLIT_2 = 5;
	final int SPLIT_3 = 6;
	final int SPLIT_4 = 7;

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
	
	public QTable()
	{
		init();
	}
	
	private void init()
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
				table[a][b][c][d][e] = -1000;
		}		
		}
		}		
		}
		}
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
}