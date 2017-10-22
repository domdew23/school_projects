import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;

public class main{

	private static final Random RAND = new Random();
	private static final int NUM_CLIENTS = 2;
	private static final int NUM_MERCHANTS = 10;
	private static final float LOAD_FACTOR = .75f;
	private static final ConcurrentHashMap<Integer,Merchant> MERCHANTS = new ConcurrentHashMap<Integer,Merchant>(100);
	private static final Thread[] CLIENTS = new Thread[NUM_CLIENTS];

	public static void main(String[] args){
		/*
		~10 Merchants, ~50 clients
		Non-blocking data structure to store game state
		Game state is collection of Merchants
		Solution using a data struture and/or locking scheme - use standard JDK components
		Compare throughput (amount of items passed to system) across two different loads on two different platforms - use JMH
		Plot results as a graph on a web page
		*/
		initMerchants();
		initClients();
		System.out.println(MERCHANTS.toString());
		System.out.println("Size: " + MERCHANTS.size());
		for (int i = 0; i < NUM_CLIENTS; i++){
			CLIENTS[i].start();
		}

	}

	private static void initMerchants(){
		for (int i = 0; i < NUM_MERCHANTS; i++){
			long seed = RAND.nextLong();
			Integer id = new Integer(i);
			Merchant m = new Merchant(seed, i);
			MERCHANTS.put(id, m);
			//m.printMerchant();
		}
	}

	private static void initClients(){
		for (int i = 0; i < NUM_CLIENTS; i++){
			long seed = RAND.nextLong();
			Thread t = new Thread(new Client(i, seed, MERCHANTS));
			CLIENTS[i] = t;
		}
	}
}
