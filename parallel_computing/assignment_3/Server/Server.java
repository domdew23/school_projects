import java.net.ServerSocket;
import java.io.IOException;
import java.util.concurrent.Phaser;

public class Server {
	private static final int PORT = 4444;
	private static boolean running = true;
	private static int id = 0;
	private static Handler[] clients;

	public static void main(String[] args) {
		Settings settings = null;
		init(settings, args);

		if (Settings.HEIGHT % Settings.CLIENTS != 0){
			System.out.println("invalid client size");
			return;
		}

		clients = new Handler[Settings.CLIENTS];
		Control control = new Control();
		Chunk[] allChunks = control.split(Control.A);
		Control.allChunks = allChunks;
		Phaser synchPoint = new Phaser(1);

		GraphicsEngine graphics = new GraphicsEngine(control);
		graphics.start();

		try {
			ServerSocket listener = new ServerSocket(PORT); // port to connect to		
			for (int i = 0; i < Settings.CLIENTS; i++){
				clients[i] = new Handler(i, listener.accept(), allChunks[i], synchPoint);
				clients[i].start();
				System.out.println("Client " + i + " added.");
			}

			while (true){
				while(synchPoint.getArrivedParties() != Settings.CLIENTS){
					//try {Thread.sleep(5000);}catch(InterruptedException e){}
				}
				Control.update();
				for (int i = 0; i < clients.length; i++){
					clients[i].updateChunk(Control.allChunks[i]);
				}
				System.out.println("Advancing to Phase: " + (synchPoint.getPhase() + 1));
				synchPoint.arriveAndAwaitAdvance();
				//try {Thread.sleep(50000);}catch(InterruptedException e){}
			}
			//listener.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private static void init(Settings settings, String[] args){
		if (args.length < 1){
			settings = new Settings();
		} else if (args.length == 1){
			settings = new Settings(args[0]);
		} else {
			System.out.println("Unexpected amount of arguments.");
			return;
		}
	}
}