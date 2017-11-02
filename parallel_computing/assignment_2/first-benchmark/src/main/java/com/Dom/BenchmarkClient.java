
package com.Dom;

/* Clients are guilty by assoication (if one has a bad 
interaction with NPC (merchant) then all clients recieve
higher prices from that NPC). Most metrics will be random. */

import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

public class BenchmarkClient implements Runnable {

	@State(Scope.Benchmark)
		public static class GameState {
			public ConcurrentHashMap<Integer,Merchant> merchants = new ConcurrentHashMap<Integer, Merchant>(100);
			public Random r = new Random();
	
			public GameState(){
				for (int i = 0; i < 60; i++){
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

    // @Benchmark @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void run(){
		/* execute one action per loop */
		GameState s = new GameState();
		MyState ms = new MyState();
		for (int i = 0; i < 500; i++){
		    if (ThreadLocalRandom.current().nextDouble() <= .92){
			    	buy(s, ms);
		       } else {
			    	interact(s, ms);
		    }
		}
	}
  
    @Benchmark @OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void interact(GameState state, MyState mystate){
		if (state.merchants.size() < 10){
		    state.merchants.put(Merchant.getCount(), new Merchant(mystate.rand.nextLong(), Merchant.getCount()));
	    }

		Merchant myMerchant = null;
    	while(myMerchant == null){
    		myMerchant = state.merchants.get(mystate.rand.nextInt(Merchant.getCount()));
    	}

	if (ThreadLocalRandom.current().nextDouble() >= mystate.behavior){
			if (myMerchant.badInteraction()){
				while (state.merchants.remove(myMerchant.id()) == null){

				}
			}
		} else {
			if(myMerchant.goodInteraction()){
				while (state.merchants.put(Merchant.getCount(), new Merchant(mystate.rand.nextLong(), Merchant.getCount())) != null){
				}
			}
		}
	}

    //@Benchmark @OutputTimeUnit(TimeUnit.MICROSECONDS)
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
