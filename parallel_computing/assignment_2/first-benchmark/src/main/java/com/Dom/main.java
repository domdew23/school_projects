package com.Dom;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.
//import java.util.concurrent.ConcurrentHashMap;

public class main{

	private static final Random RAND = new Random();
	private static final int NUM_CLIENTS = 500;
	private static final int NUM_MERCHANTS = 30;
	private static final ConcurrentHashMap<Integer,Merchant> MERCHANTS = new ConcurrentHashMap<Integer,Merchant>(100);
	private static final Thread[] CLIENTS = new Thread[NUM_CLIENTS];

	public static void main(String[] args){
	
		//initMerchants();
		ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);
		for (int i = 0; i < NUM_CLIENTS; i++){
			executor.submit(new Client());	
		}
		
		try { Thread.sleep(10000); } catch (InterruptedException e) {};
		executor.shutdownNow();
		System.exit(0);

		//init();
		/*while (true){
			if (MERCHANTS.isEmpty()){
				System.out.println("YOU LOST!\nAll merchants have left the world.");
				System.exit(0);
			}
		}*/
		//test();
	}

	private static void print(){
		while (true){
			if (System.currentTimeMillis() % 10000 == 0){
				double total = 0;
				int size = MERCHANTS.size();
				for (int i = 0; i < size; i++){
					if (MERCHANTS.get(i) != null){
						total += MERCHANTS.get(i).getChemistry();		
					}
				}
				double avg = total/size;
				System.out.println("Avg chemistry: " + avg);
			}
		}	
	}

	private static void test(){
		Thread t = new Thread(new Runnable(){
			public void run(){
				for (int i = 0; i < 100000; i++){
					while(MERCHANTS.put(i, new Merchant(RAND.nextLong(), i)) != null){
						System.out.println("adding " + i);
					}
					//System.out.println("adding..." + i);
				}
			}
		});

		Thread t2 = new Thread(new Runnable(){
			public void run(){
				for (int i = 0; i < 100000; i++){
					while (MERCHANTS.remove(i) == null){
						System.out.println("removing..." + i);
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
		// me - 403 / 11225 / 822 / 536
		// dl - 139 / 16225 / 781 / 503
		
	}
	private static void init(){
		initMerchants();
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

	/*private static void initClients(){
		for (int i = 0; i < NUM_CLIENTS; i++){
			long seed = RAND.nextLong();
			Thread t = new Thread(new Client(i, seed, MERCHANTS));
			CLIENTS[i] = t;
		}
	}*/
}
