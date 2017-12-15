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
	Person[][] chunk;

	public Handler(String clientId, Socket socket){
		this.socket = socket;
		this.clientId = clientId;
		this.running = true;
		this.chunk = null;
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
		this.chunk = (Person[][]) in.readObject();
		for (int y = 0; y < chunk.length; y++) { 
			for (int x = 0; x < chunk[y].length; x++){
				chunk[y][x].name += " is a fag";		
			}
		}
	}

	private void send() throws IOException {
		if (chunk != null) out.writeObject(chunk);
		out.flush();
	}
}