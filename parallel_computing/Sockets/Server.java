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
	private static final int PORT = 4444;
	private static boolean running = true;

	public static void main(String[] args) {
		try {
			ServerSocket listener = new ServerSocket(PORT); // port to connect to		
			while (running){
				new Handler(listener.accept()).start();
				System.out.println("Added new Client.");
			}
			listener.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}