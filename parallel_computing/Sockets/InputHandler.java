import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class InputHandler extends Thread {
	private Socket socket;
	private String clientId;
	private boolean running;

	public InputHandler(String clientId, Socket socket){
		this.socket = socket;
		this.clientId = clientId;
		this.running = true;
	}

	public void run(){
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Person[] myChunk = new Person[4];

			while (running){
				try {
					myChunk = (Person[]) in.readObject();
					System.out.println(clientId + ":\n");
					for (int i = 0; i < myChunk.length; i++){
						System.out.println(myChunk[i]);
					}
				} catch (EOFException ex){
					running = false;
				}
			}
			in.close();
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
}