package example.common;

public interface GamerEvent
{
	public void connectionError();
	
	public void userLogged( int id, String login, short x, short y );
	
	public void userLeft( int id );
	
	public void movementReceived( int userId, short x, short y );
	
	public void messageReceived( int userId, String userMessage );
}
