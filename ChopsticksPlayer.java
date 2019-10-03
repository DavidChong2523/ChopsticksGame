public abstract class ChopsticksPlayer
{	
	private boolean m_isP1;
		
	public ChopsticksPlayer(boolean player)
	{
		m_isP1 = player;
	}

	public void chooseAction(ChopsticksBoard board)
	{
	
	}
	
	// returns player's side
	public boolean getPlayer()
	{
		return m_isP1;
	}
}