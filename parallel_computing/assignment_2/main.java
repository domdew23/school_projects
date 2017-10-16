import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class main{

	private static final Random RAND = new Random();
	private static final int NUM_CLIENTS = 50;
	private static final int NUM_MERCHANTS = 10;
	private static final float LOAD_FACTOR = .75f;
	private static final ConcurrentHashMap<Integer,Merchant> MERCHANTS = new ConcurrentHashMap<Integer,Merchant>(NUM_MERCHANTS, LOAD_FACTOR, NUM_CLIENTS);
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
		createMerchants();
		createClients();
	}

	private static void createMerchants(){
		for (int i = 0; i < NUM_MERCHANTS; i++){
			long seed = RAND.nextLong();
			Merchant m = new Merchant(seed, i);
			MERCHANTS.put(i, m);
			print(m, i, seed);
		}
	}

	private static void print(Merchant m, int i, long seed){
		System.out.println("\nMember #" + i + ": Seed: " + seed + " || Chemistry: " + m.getChemistry() + " || avg price: " + m.getAvgPrice());			
		System.out.println("====================");
		m.printGoodPrices();
		System.out.println("====================");
	}

	private static void createClients(){
		for (int i = 0; i < NUM_CLIENTS; i++){
			long seed = RAND.nextLong();
			Thread t = new Thread(new Client(i, seed, MERCHANTS));
			CLIENTS[i] = t;
		}
	}
}
