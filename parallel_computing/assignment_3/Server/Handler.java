import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.Phaser;

public class Handler extends Thread {
	private Socket socket;
	private boolean running;
	private int id;
	ObjectOutputStream out;
	ObjectInputStream in;
	Chunk chunk;
 	Phaser synchPoint;

	public Handler(int id, Socket socket, Chunk chunk, Phaser synchPoint){
		this.id = id;
		this.socket = socket;
		this.running = true;
		this.chunk = chunk;
		this.synchPoint = synchPoint;
	}

	public void run(){
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			synchPoint.register();

			while (running){
				send();
				recieve();
				synchPoint.arriveAndAwaitAdvance();
			}
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	private void send() throws IOException {
		//Control.updateNeighbors(chunk.elements);
		out.writeObject(chunk);
		out.flush();
	}

	private void recieve() throws IOException, ClassNotFoundException {
		if ((chunk = (Chunk) in.readObject()) != null) Control.allChunks[id] = chunk;
		System.out.println("Recieved: " +  Control.allChunks[id]);
	}

	public void updateChunk(Chunk chunk){
		this.chunk = chunk;
	}
}