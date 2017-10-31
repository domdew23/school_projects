import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
public class main{

	private static final Random RAND = new Random();
	private static final int NUM_CLIENTS = 40;
	private static final int NUM_MERCHANTS = 60;
	private static final ConcurrentHashMap<Integer,Merchant> MERCHANTS = new ConcurrentHashMap<Integer,Merchant>(100);
	private static final Thread[] CLIENTS = new Thread[NUM_CLIENTS];

	public static void main(String[] args){
		/*
		~10 Merchants, ~50 clients
		Non-blocking data structure to store game state
		Game state is collection of Merchants
		Solution using a data struture and/or locking scheme - use standard JDK components
		Compare throughput (amount of items passed to system) across two different loads 
		on two different platforms use JMH
		Plot results as a graph on a web page
		*/

		initMerchants();
		ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);
		for (int i = 0; i < NUM_CLIENTS; i++){
			executor.submit(new Client(i, RAND.nextLong(), MERCHANTS));	
		}
		
		//try { Thread.sleep(5000); } catch (InterruptedException e) {};
		//System.out.println("DONE SLEEPING");
		//executor.shutdownNow();
		//System.exit(0);
	}

	private static void initMerchants(){
		for (int i = 0; i < NUM_MERCHANTS; i++){
			long seed = RAND.nextLong();
			Integer id = new Integer(i);
			Merchant m = new Merchant(seed, i);
			MERCHANTS.put(id, m);
		}
	}
}
