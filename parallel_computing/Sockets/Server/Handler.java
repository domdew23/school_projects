import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Handler extends Thread {
	private Socket socket;
	private boolean running;
	private int id;
	ObjectOutputStream out;
	ObjectInputStream in;
	Person[][] chunk;

	public Handler(int id, Socket socket, Person[][] chunk){
		this.id = id;
		this.socket = socket;
		this.running = true;
		this.chunk = chunk;
	}

	public void run(){
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			while (running){
				send();
				recieve();				
				running = false;
				// add part / await (phaser)
			}
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	private void send() throws IOException {
		out.writeObject(chunk);
		out.flush();
	}

	private void recieve() throws IOException, ClassNotFoundException {
		if ((chunk = (Person[][]) in.readObject()) != null){
			for (int y = 0; y < chunk.length; y++) { 
				for (int x = 0; x < chunk[y].length; x++){
					System.out.println(chunk[y][x]); 
				}
			}
		}
	}

	public void updateChunk(Person[][] chunk){
		this.chunk = chunk;
	}
}