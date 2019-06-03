import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.naming.NamingException;

import com.example.domain.User;
import com.example.rmi.features.GameServer;

public class Main
{
	public static void main( String[] args ) throws NamingException, RemoteException, NotBoundException
	{
		// konfiguracja
		String host = "localhost";
		int port = 5060;
		
		// nawiazywanie polaczenia
		
		String name = "rmi://" + port + "/PublicGameServerObject";
		
		Registry registry = LocateRegistry.getRegistry( host, port ); // nawiazywanie polaczenia
		GameServer gameServer = (GameServer) registry.lookup( name ); // pobranie zbindowanej na serwerze logiki gry
		
		//TODO: pobieranie wiekszej ilosci zbindowanych na serwerze obiektow
		
		// przykladowa logika serwera
		
		if ( gameServer.loginUser( "admin", "admin" ) )
		{
			//TODO: wymagana implementacja sessji dla roznych uzytkownikow
			User[] users = gameServer.getUsers();
			
			for ( User el : users )
				System.out.printf( "ID: %d, Login: %s\n", el.getId(), el.getLogin() );
			
			//gameServer.logoutUser();
		}
		else
			System.out.println( "Login failed." );
	}
}
