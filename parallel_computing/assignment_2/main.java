import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;

public class main{

	private static final Random RAND = new Random();
<<<<<<< HEAD
	private static final int NUM_CLIENTS = 1000;
=======
	private static final int NUM_CLIENTS = 1;
>>>>>>> e07feafae27d032502d2bc23363b18e48d9cf587
	private static final int NUM_MERCHANTS = 10;
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
		
		init();
		//test();
	}

	private static void test(){
		Thread t = new Thread(new Runnable(){
			public void run(){
				for (int i = 0; i < 100000; i++){
					while(MERCHANTS.put(i, new Merchant(RAND.nextLong(), i)) != null){
					}
					//System.out.println("adding..." + i);
				}
			}
		});

		Thread t2 = new Thread(new Runnable(){
			public void run(){
				for (int i = 0; i < 100000; i++){
					while (MERCHANTS.remove(i) == null){
					}
					//System.out.println("removing..." + i);
				}
			}
		});

		long start = System.currentTimeMillis();
		t.start();
		t2.start();

		try {
			t.join();
			t2.join();	
			System.out.println("Size: " + MERCHANTS.size() + "\n");// + MERCHANTS.toString());	
		} catch (InterruptedException e){

		}

		long end = System.currentTimeMillis();
		System.out.println("Took: {" + (end - start) + "} milliseconds.");
		// me - 403 / 11225 / 822
		// dl - 139 / 16225 / 781 ~ 804 
		
	}
	private static void init(){
		initMerchants();
		initClients();
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
