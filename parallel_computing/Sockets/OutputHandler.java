import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class OutputHandler extends Thread {
	private Socket socket;
	private String clientId;

	public OutputHandler(String clientId, Socket socket){
		this.socket = socket;
		this.clientId = clientId;
	}

	public void run(){
		//try {
			//ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			
			//out.close();
		//} catch(IOException e){
			//e.printStackTrace();
		//}
	}
}