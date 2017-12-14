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
import java.util.Stack;

public class Server {
	private static final int PORT = 4444;
	private static boolean running = true;
	//private static ArrayList<Handler> clients = new ArrayList<Handler>(1);
	private static Stack<Handler> clients = new Stack<Handler>();
	private static int id = 0;

	public static void main(String[] args) {
		try {
			ServerSocket listener = new ServerSocket(PORT); // port to connect to		
			while (running){
				new Handler(id++, listener.accept()).start();
			}
			listener.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}