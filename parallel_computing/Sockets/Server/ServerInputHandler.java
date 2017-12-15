import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class ServerInputHandler extends Thread {
	private Socket socket;
	private int id;
	private boolean ready;
	private boolean running;

	public ServerInputHandler(int id, Socket socket){
		this.socket = socket;
		this.id = id;
		this.running = true;
		this.ready = false;
	}

	public void run(){
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}