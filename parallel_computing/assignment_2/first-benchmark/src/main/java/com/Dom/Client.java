package com.Dom;

/* Clients are guilty by assoication (if one has a bad 
interaction with NPC (merchant) then all clients recieve
higher prices from that NPC). Most metrics will be random. */

import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {

	@State(Scope.Benchmark)
		public static class GameState {
			public ConcurrentHashMap<Integer,Merchant> merchants = new ConcurrentHashMap<Integer, Merchant>(100);
			public Random r = new Random();

			public GameState(){
				for (int i = 0; i < 30; i++){
					long seed = r.nextLong();
					Integer id = new Integer(i);
					Merchant m = new Merchant(seed, i);
					merchants.put(id, m);
				}
			}
		}

	@State(Scope.Thread)
		public static class MyState {
			public Random rand = new Random();
			public double behavior = rand.nextDouble();
			public double salary = (rand.nextDouble() * rand.nextInt(100000));
	}

	/*public Client(int id, long seed, ConcurrentHashMap<Integer,Merchant> merchants){
		GameState.merchants = merchants;

		//RAND = new Random(seed);
		//behavior = RAND.nextDouble();
		//salary = (RAND.nextDouble() * RAND.nextInt(100000));
		MyState.id = id;
		
	}*/

	public void run(){
		/* execute one action per loop */
		/*GameState s = new GameState();
		MyState ms = new MyState();
		while (true){
			if (Math.random() <= .25){
				if (Math.random() <= .85){
					buy(s, ms);
				} else {
					interact(s, ms);
				}
			} else {
				travel(ms);
			}
		}*/
	}

	/* most common action of clients */
	public void travel(MyState mystate){
		try {
			if (Math.random() <= .9){
				// simulate completing quests/missions for money
				double earnings = mystate.rand.nextDouble() * mystate.rand.nextInt(1000);
				mystate.salary += (earnings);
				if (Math.random() >= mystate.behavior){
					mystate.behavior += mystate.rand.nextDouble()/20;
					if (mystate.behavior >= 1){
						mystate.behavior = .75;
					}
				} else {
					mystate.behavior -= mystate.rand.nextDouble()/20;
					if (mystate.behavior <= 0){
						mystate.behavior = .5;
					}
				}
				Thread.sleep(mystate.rand.nextInt(500));
			} else {
				Thread.sleep(mystate.rand.nextInt(2500));
			}
		} catch (InterruptedException e){
			return;
		}
	}
   
    @Benchmark @OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void interact(GameState state, MyState mystate){
		if (state.merchants.isEmpty()){
			System.out.println("You lose");
			System.exit(0);
		}
		mystate.behavior = mystate.rand.nextDouble();
		System.out.println("Size: " + state.merchants.size() + " | behavior: " + mystate.behavior);
		Merchant myMerchant = null;
    	while(myMerchant == null){
    		myMerchant = state.merchants.get(mystate.rand.nextInt(Merchant.getCount()));
    	}

		if (Math.random() >= mystate.behavior){
			if (myMerchant.badInteraction()){
				while (state.merchants.remove(myMerchant.id()) == null){
					return;
				}
			}
		} else {
			if(myMerchant.goodInteraction()){
				while (state.merchants.put(Merchant.getCount(), new Merchant(mystate.rand.nextLong(), Merchant.getCount())) != null){
				}
			}
		}
	}

	/* second most common action of clients */
    @Benchmark @OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void buy(GameState state, MyState mystate){
		Merchant myMerchant = null;
		while (myMerchant == null){
			myMerchant = state.merchants.get(mystate.rand.nextInt(Merchant.getCount()));
		}

		Good good = myMerchant.getGoods()[mystate.rand.nextInt(myMerchant.getGoodCount())];

		if (good.getPrice() < mystate.salary){
			good.buy();
			mystate.salary -= good.getPrice();
			/*String output = "";//-----------------------------------------------------------------------------------------------------------------\n";
			//output += 		"\n";//|\t\t|\t\t|\t\t|\t\t\t|\t\t|\t\t\t\t|\n";
			output +=		"|  Client: " + id + "  |  Merchant: " + myMerchant.id() + "  |  Good " + good.id() + "   |";
			System.out.print(output);
			String output2 = "";
			System.out.printf("  Cost: $%.2f", good.getPrice());
			System.out.printf("         |  Behavior: %.4f", behavior);
			System.out.print("     |"); 
			System.out.printf("  Salary: $%.2f", salary);
			output2 +=		"    |\n";//\t|\n|\t\t|\t\t|\t\t|\t\t\t|\t\t|\t\t\t\t|\n";
			output2 +=		"-----------------------------------------------------------------------------------------------------------------\n";
			System.out.print(output2);*/
		}
	}
}
