package example.protocol;

import java.io.Serializable;

public class LoginFrame extends Frame implements Serializable
{
	private static final long serialVersionUID = 7993150840780373218L;
	
	private String username;
	private String password;
	
	public LoginFrame()
	{
		super.setType( FrameType.Login );
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername( String username )
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword( String password )
	{
		this.password = password;
	}
}
