package example.protocol;

import java.io.Serializable;

public class ResultFrame extends Frame implements Serializable
{
	private static final long serialVersionUID = 6795364875126860031L;
	
	private boolean correct;
	
	public ResultFrame()
	{
		super.setType( FrameType.Result );
	}
	
	public boolean isCorrect()
	{
		return this.correct;
	}
	
	public void setCorrect( boolean correct )
	{
		this.correct = correct;
	}
}
