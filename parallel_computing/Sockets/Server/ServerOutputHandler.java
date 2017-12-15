import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class ServerOutputHandler extends Thread {
	private Socket socket;
	private boolean running;
	private int id;

	public ServerOutputHandler(int id, Socket socket){
		this.id = id;
		this.socket = socket;
		this.running = true;
	}

	public void run(){
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			
			Person[][] array = new Person[2][4];
			for (int y = 0; y < 2; y++){
				for (int x = 0; x < 4; x++){
					array[y][x] = new Person(x, y);
					System.out.println(array[y][x]);
				}
			}

			for (int i = 0; i < 1; i++){
				Person[] row = array[i];
				out.writeObject(row);
				out.flush();
			}
			
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run1() {
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