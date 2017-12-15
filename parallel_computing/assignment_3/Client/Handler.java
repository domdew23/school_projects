import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Handler extends Thread {
	private Socket socket;
	private String clientId;
	private boolean running;
	ObjectInputStream in; 
	ObjectOutputStream out;
	Chunk chunk;
	Jacobi jacobi;
	int step;

	public Handler(String clientId, Socket socket){
		this.socket = socket;
		this.clientId = clientId;
		this.running = true;
		this.chunk = null;
		this.step = 0;
	}

	public void run(){
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
			while (running){
				recieve();
				send();
			}
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	private void recieve() throws IOException, ClassNotFoundException {
		chunk = (Chunk) in.readObject();
		//System.out.println("Client " + clientId + " got\n" + chunk);
		if (step == 0){
			jacobi = new Jacobi(chunk.elements, new Region[Settings.CHUNK_SIZE][Settings.WIDTH], 0, Settings.CHUNK_SIZE, 0, Settings.WIDTH, 1, Settings.THRESHOLD);
			step++;
		} else {
			jacobi.update(chunk);
		}
		chunk.elements = jacobi.invoke();
		jacobi.reinitialize();

		System.out.println("Client " + clientId + " after computations\n" + chunk);
	}

	private void print(Chunk c){
		if (c.hasTopEdge){
			System.out.println("TOP: ");
			for (int y = 0; y < c.topEdge.length; y++){
				System.out.println(c.topEdge[y]);
			}
			System.out.println("================");
		}

		if (c.hasBottomEdge){
			System.out.println("BOTTOM: ");
			for (int y = 0; y < c.bottomEdge.length; y++){
				System.out.println(c.bottomEdge[y]);
			}
			System.out.println("================");
		}
	}

	private void send() throws IOException {
		if (chunk != null) out.writeObject(chunk);
		out.flush();
	}
}