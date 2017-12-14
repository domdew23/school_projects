import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Handler extends Thread {
	private Socket socket;
	private boolean running;

	public Handler(Socket socket){
		this.socket = socket;
		this.running = true;
	}

	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Person person = null;
			
			if ((person = (Person) in.readObject()) != null){
				System.out.println("Person:\n" + person);
			}

			int message;
			while (running){
				message = in.readInt();
				System.out.println("Recieved message: " + message);
			}

			in.close();
			socket.close();
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
}