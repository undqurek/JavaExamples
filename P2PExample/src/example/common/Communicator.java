package example.common;

public interface Communicator 
{
	public int getUserId();
	public String getUserLogin();
	
	public void setEvent(GamerEvent event);
	
	public boolean sendMovementFrame(int movementType);
	public boolean sendMessageFrame(String message);
}
