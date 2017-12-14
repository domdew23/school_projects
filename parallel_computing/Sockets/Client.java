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
	private static final int PORT = 4444;

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException {
		try {
			/* socket: interface that allows client and server to communicate with each other */
			/* first run server then run the client */
			Socket connection = new Socket("localhost", PORT); // takes ip and port
			
			//ObjectInputStream in = new ObjectInputStream(connection.getInputStream()); // recieve response from server
			ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream()); // send data to the server

			Person person = new Person(args[0], 21, 41100.22);
			out.writeObject(person);
			out.flush();

			Scanner sc = new Scanner(System.in);
			int message = 0;
			while (message != -1){
				message = sc.nextInt();
				out.writeInt(message);
				out.flush();
			}

			out.close();
			connection.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}