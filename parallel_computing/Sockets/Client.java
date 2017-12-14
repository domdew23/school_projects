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
	private static boolean running = true;

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException {
		try {
			/* socket: interface that allows client and server to communicate with each other */
			/* first run server then run the client */
			Socket connection = new Socket("localhost", PORT); // takes ip and port
			
			InputHandler inputHandler = new InputHandler(args[0], connection);
			OutputHandler outputHandler = new OutputHandler(args[0], connection);

			inputHandler.start();
			outputHandler.start();

			while(running){

			}
			
			connection.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}