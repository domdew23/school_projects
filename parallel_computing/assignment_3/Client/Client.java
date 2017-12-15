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

	public static void main(String[] args) {
		try {
			/* socket: interface that allows client and server to communicate with each other */
			/* first run server then run the client */
			Settings settings = null;
			init(settings, args);

			Socket connection = new Socket("localhost", PORT); // takes ip and port
			
			Handler handler = new Handler(args[0], connection);
			handler.start();

			while(running){

			}
			
			connection.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private static void init(Settings settings, String[] args){
		if (args.length < 1){
			settings = new Settings();
		} else if (args.length == 2){
			settings = new Settings(args[1]);
		} else {
			System.out.println("Unexpected amount of arguments.");
			return;
		}
	}
}
