import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Server {
	public static void main(String[] args) throws InterruptedException {
		try {
			ServerSocket listener = new ServerSocket(4444); // port to connect to		
			Socket connectionSocket = listener.accept(); // accept request to socket 
			System.out.println("Connection Established.");

			//ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
			System.out.println("IO streams initialized.");

			Person person = new Person("Dom Dewhurst", 21, 41100.22);
			out.writeObject(person);
			System.out.println("Object written.");
			out.flush();
		
			//in.close();
			out.close();
			connectionSocket.close();
			listener.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}