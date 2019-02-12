import java.util.Scanner;
	
public class ChopsticksHumanPlayer extends ChopsticksPlayer
{
	public ChopsticksHumanPlayer(boolean player)
	{
		super(player);
	}
	
	public void chooseAction(ChopsticksBoard board)
	{
		// display player's side
		if(getPlayer())
			System.out.println("P1: ");
		else
			System.out.println("P2: ");

		// choose hit or split
		System.out.println("Hit(1) or Split(2)?");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();

		switch(choice) 
		{
			// hit
			case 1:
				// choose playerHand and opponentHand
				System.out.println("Choose playerHand: 1 or 2");
				int playerHand = input.nextInt() - 1;
				System.out.println("Choose opponentHand: 1 or 2");
				int opponentHand = input.nextInt() - 1;
				
				// hit opponentHand with playerHand
				ChopsticksPlayer.hit(board, getPlayer(), playerHand, opponentHand);
				break;
			// split
			case 2:
				// choose subtractHand and amount
				System.out.println("Choose subtractHand: 1 or 2");
				int subtractHand = input.nextInt() - 1;
				System.out.println("Choose amount: ");
				int amount = input.nextInt();
				
				// subtract amount from subtractHand and add to other hand
				ChopsticksPlayer.split(board, getPlayer(), subtractHand, amount);
				break;
			default:
				System.out.println("Invalid player choice");
				break;
		}
	}
}
