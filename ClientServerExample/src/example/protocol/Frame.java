package example.protocol;

import java.io.Serializable;

public abstract class Frame implements Serializable
{
	private static final long serialVersionUID = 3851837710608297424L;
	
	private FrameType type;
	
	public FrameType getType()
	{
		return this.type;
	}
	
	public void setType( FrameType type )
	{
		this.type = type;
	}
}
