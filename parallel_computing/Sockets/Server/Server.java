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
	private static int id = 0;
	private static final int CLIENTS = 4;
	private static final int width = 16;
	private static final int height = width/2;

	public static void main(String[] args) {
		Person[][] array = new Person[height][width];

		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				array[y][x] = new Person(x, y);
			}
		}

		if (height % CLIENTS != 0){
			System.out.println("invalid client size");
			return;
		}

		int chunkSize = height/CLIENTS;
		int offset = 0;

		Person[][][] allChunks = new Person[CLIENTS][height][width];

		for (int i = 0; i < CLIENTS; i++,offset+=chunkSize){
			Person[][] chunk = new Person[chunkSize][width];
			for (int y = offset, z = 0; z < chunkSize; y++,z++){
				for (int x = 0; x < width; x++){	
					chunk[z][x] = array[y][x];
				}
			}
			
			allChunks[i] = chunk;
		}

		try {
			ServerSocket listener = new ServerSocket(PORT); // port to connect to		
			while (running){
				new Handler(id, listener.accept(), allChunks[id++]).start();
			}
			listener.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}