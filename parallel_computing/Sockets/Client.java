import java.util.Scanner;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {
	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException {
		try {
			/* socket: interface that allows client and server to communicate with each other */
			/* first run server then run the client */
			Socket connection = new Socket("localhost", 4444); // takes ip and port
			
			ObjectInputStream in = new ObjectInputStream(connection.getInputStream()); // recieve response from server
			//ObjectOutputStream out = new ObjectOutputStream(new PrintStream(connection.getOutputStream())); // send data to the server

			System.out.println("Waiting for object...");
			Person personFromServer = (Person) in.readObject();
			System.out.println("Recieved Object.");

			System.out.println(personFromServer);
			in.close();
			//out.close();
			connection.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}