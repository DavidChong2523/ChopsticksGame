import java.util.ArrayList;

public class ChopsticksComputerPlayer extends ChopsticksPlayer
{
	private static final int INITIAL_DEPTH = 0;
	private static final int HIT = 0;
	private static final int SPLIT = 1;

	// depth of search
	private int finalDepth;

	public ChopsticksComputerPlayer(boolean player, int d)
	{
		super(player);
		finalDepth = d;
	}

	public void chooseAction(ChopsticksBoard board)
	{
		// generate possible moves from current board position
		ArrayList<int[]> moveList = generateMoves(board, getPlayer());
		
		// if depth 0, play a random move
		if(finalDepth == 0)
		{
			int choice = (int) Math.random() * moveList.size();
			playMove(board, getPlayer(), moveList.get(choice));
			return;
		}

		int bestMove = 0;
		int maxEvaluation = -1000;
		int evaluation;

		// evaluate each possible move and determine the best move
		ChopsticksBoard copyBoard;
		for(int i = 0; i < moveList.size(); i++)
		{
			// copy current board position and play a move onto it
			copyBoard = copy(board);
			playMove(copyBoard, getPlayer(), moveList.get(i));
			
			// evaluate the copied board
			evaluation = minimax(copyBoard, INITIAL_DEPTH + 1, !getPlayer(), -1000, 1000);

			// update bestMove if the current move is better than previous moves
			if(evaluation > maxEvaluation)
			{
				maxEvaluation = evaluation;
				bestMove = i;
			}
			// randomly update bestMove if the current move is equal to previous moves
			else if(evaluation == maxEvaluation)
			{
				int random = (int) Math.random() * 2;
				if(random == 0)
					bestMove = i;	
			}
		}

		// play the best move
		playMove(board, getPlayer(), moveList.get(bestMove));
	}
	
	// implements alpha-beta pruning
	// beta tracks maximum value opponent is assured of 
	// alpha tracks minimum value computer is assured of
	// if alpha >= beta, additional search becomes unnecessary	
	// -opponent won't choose move of greater value than current maxVal
	// -computer won't choose move of lesser value than current minVal
	public int minimax(ChopsticksBoard board, int depth, boolean isP1, int alpha, int beta)
	{
		// evaluate given board position
		int evaluation = evaluateBoard(board, getPlayer());
	
		// return evaluation if a win, loss, or finalDepth has been reached
		if(evaluation == 1000 || evaluation == -1000 || depth == finalDepth)
		{
			return evaluation;
		}

		// generate all possible moves from current board position
		ArrayList<int[]> moveList = generateMoves(board, isP1);
		ChopsticksBoard copyBoard;		

		// if evaluating from computer player perspective
		// return value of best possible move for computer
		if(isP1 == getPlayer())
		{
			int maxValue = -1000;
			int i = 0;
			do
			{
				// play the move onto a copy board and evaluate it
				copyBoard = copy(board);
				playMove(copyBoard, isP1, moveList.get(i));
				// check for new maxValue and alpha
				maxValue = Math.max(maxValue, minimax(copyBoard, depth + 1, !isP1, alpha, beta));
				alpha = Math.max(alpha, maxValue);	
				
				i++;
			} while(i < moveList.size() && beta > alpha);	// loop through all possible moves 
									// stop if alpha >= beta 
							
			
			return maxValue;
		}
		// if evaluating from opponent perspective
		// return value of worst possible move for computer 
		else
		{
			int minValue = 1000;
			int i = 0;
			do
			{
				// play the move onto a copy board and evaluate it
				copyBoard = copy(board);
				playMove(copyBoard, isP1, moveList.get(i));
				// check for new minValue and beta
				minValue = Math.min(minValue, minimax(copyBoard, depth + 1, !isP1, alpha, beta));
				beta = Math.min(beta, minValue);

				i++;
			} while(i < moveList.size() && beta > alpha);	// loop through all possible moves 
									// stop if alpha >= beta
	
			return minValue;
		}
	}
	
	// generate all possible moves from given ChopsticksBoard from perspective isP1		      
	public static ArrayList<int[]> generateMoves(ChopsticksBoard board, boolean isP1)
	{
		// store moves as array of int of size 3
		// index 0 stores hit or split
		// index 1 stores the first hand to act on
		// index 2 stores the second hand to act on
		ArrayList<int[]> moveList = new ArrayList<int[]>();
		int[] move; 
	
		// all combinations for hit 
		for(int hand1 = 0; hand1 < 2; hand1++)
		{
			for(int hand2 = 0; hand2 < 2; hand2++)
			{
				// check for valid move
				if(board.get(isP1, hand1) > 0 && board.get(!isP1, hand2) > 0)
				{
					// add move to moveList
					move = new int[3];
					move[0] = HIT;
					move[1] = hand1;
					move[2] = hand2;
					moveList.add(move);
				}
			}
		}
		
		// split
		// subtractHand value always >= addHand value
		int subtractHand = (board.get(isP1, LEFT) > board.get(isP1, RIGHT) ? LEFT : RIGHT);
		int addHand = (1 - subtractHand);
		int subtractHandValue = board.get(isP1, subtractHand);
		int addHandValue = board.get(isP1, addHand);

		// test all possible subtract values
		for(int value = 1; value <= subtractHandValue; value++)
		{
			// check for valid move
			if(subtractHandValue != addHandValue + value)
			{
				// add move to moveList
				move = new int[3];
				move[0] = SPLIT;
				move[1] = subtractHand;
				move[2] = value;
				moveList.add(move);
			}
		}
		
		return moveList;
	}

	// play move onto board
	public static void playMove(ChopsticksBoard board, boolean isP1, int[] move)
	{
		if(move[0] == HIT)
		{
			ChopsticksPlayer.hit(board, isP1, move[1], move[2]);
		}
		else 
		{
			ChopsticksPlayer.split(board, isP1, move[1], move[2]);
		}
	}

	// copy given ChopsticksBoard into new ChopsticksBoard
	public static ChopsticksBoard copy(ChopsticksBoard board)
	{
		int l1 = board.get(true, LEFT);
		int r1 = board.get(true, RIGHT);
		int l2 = board.get(false, LEFT);
		int r2 = board.get(false, RIGHT);

		return new ChopsticksBoard(l1, r1, l2, r2);
	}

	// evaluate given ChopsticksBoard from the perspective of isP1
	public static int evaluateBoard(ChopsticksBoard board, boolean isP1)
	{
		int evaluation = 0;
		
		if(board.get(isP1, LEFT) == 0 || board.get(isP1, RIGHT) == 0)
		{
			evaluation -= 5;
		}
		if(board.get(!isP1, LEFT) == 0 || board.get(!isP1, RIGHT) == 0)
		{
			evaluation += 3;
		}
		if(board.get(isP1, LEFT) == 0 && board.get(isP1, RIGHT) == 0)	
		{
			evaluation = -1000;
		}
		if(board.get(!isP1, LEFT) == 0 && board.get(!isP1, RIGHT) == 0)
		{
			evaluation = 1000;
		}

		return evaluation;
	}
}