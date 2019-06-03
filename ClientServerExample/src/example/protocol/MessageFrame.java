package example.protocol;

import java.io.Serializable;

public class MessageFrame extends Frame implements Serializable
{
	private static final long serialVersionUID = 6400209339206572575L;
	
	private String message;
	
	public MessageFrame()
	{
		super.setType( FrameType.Message );
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setMessage( String message )
	{
		this.message = message;
	}
}
