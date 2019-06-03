import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.naming.NamingException;

import com.example.server.PublicGameServer;

public class Main
{
	public static void main( String[] args ) throws NamingException, RemoteException, InterruptedException, AlreadyBoundException
	{
		// konfiguracja
		int port = 5060;
				
		// uruchamianie serwera
		
		String name = "rmi://" + port + "/PublicGameServerObject";
		Registry registry = LocateRegistry.createRegistry( port ); // uruchomienie rejestru dla rmi na wybranym porcie
		
		try
		{
			PublicGameServer gameServer = new PublicGameServer(); // tworzenie obiektu gry na serwerze
			registry.bind( name, gameServer ); // bindowanie obiektu gry

			//TODO: bindowanie wiekszej ilosci obiektow
			
			// przykladowa logika 'podtrzymujaca' aplikacje
			
			System.out.println( "Type 'exit' to exit server." );
			Scanner scanner = new Scanner( System.in );
			
			while ( true )
			{
				if ( scanner.hasNextLine() )
				{
					if ( "exit".equals( scanner.nextLine() ) )
						break;
				}
			}
			
			scanner.close();
		}
		finally
		{
			UnicastRemoteObject.unexportObject( registry, true ); // zwalnianie rejestru
			System.out.println( "Server stopped." ); // komunikat zakonczenia
		}
	}
}
