package com.Dom;

/* Clients are guilty by assoication (if one has a bad 
interaction with NPC (merchant) then all clients recieve
higher prices from that NPC). Most metrics will be random. */

import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Runnable {

	@State(Scope.Benchmark)
		public static class GameState {
			public ConcurrentHashMap<Integer,Merchant> merchants = new ConcurrentHashMap<Integer, Merchant>(100);
			public Random rand = new Random();
		}

	@State(Scope.Thread)
		public static class MyState {
			public Random rand = new Random();
			public double behavior = rand.nextDouble();
			public double salary = (rand.nextDouble() * rand.nextInt(100000));
	}

	public Client(int id, long seed, ConcurrentHashMap<Integer,Merchant> merchants, GameState state, MyState mystate){
		RAND = new Random(seed);
		behavior = RAND.nextDouble();
		salary = (RAND.nextDouble() * RAND.nextInt(100000));
		state.merchants = merchants;
		MyState.id = id;
	}

	public void run(){
		/* execute one action per loop */
		while (true){
			if (Math.random() <= .25){
				if (Math.random() <= .85){
					buy();
				} else {
					interact();
				}
			} else {
				travel();
			}
		}
	}

	/* most common action of clients */
	private void travel(){
		/* simulates clients traveling the world and
		earning salary. This function has no influence 
		on game state and does not need to read game state.
		Sleeps for random amount of time between 0 - 5 seconds. */
		try {
			if (Math.random() <= .9){
				// simulate completing quests/missions for money
				double earnings = RAND.nextDouble() * RAND.nextInt(1000);
				salary += (earnings);
				if (Math.random() >= behavior){
					behavior += RAND.nextDouble()/20;
					if (behavior >= 1){
						behavior = .75;
					}
				} else {
					behavior -= RAND.nextDouble()/20;
					if (behavior <= 0){
						behavior = .5;
					}
				}
				Thread.sleep(RAND.nextInt(500));
				//System.out.printf("Earned: $%.2f", earnings);
				//System.out.println();
			} else {
				// simulate doing nothing
				//System.out.println("Client " + id + " traveling...");
				Thread.sleep(RAND.nextInt(2500));
			}
		} catch (InterruptedException e){
			return;
		}
	}

	/* second most common action of clients */
	private void buy(){
		/* must read game state to get the prices of goods
		before trying to make a purchase. (read -> buy) */

		Merchant myMerchant = null;
		while (myMerchant == null){
			myMerchant = merchants.get(RAND.nextInt(Merchant.getCount()));
		}

		Good good = myMerchant.getGoods()[RAND.nextInt(myMerchant.getGoodCount())];

		if (good.getPrice() < salary){
			good.buy();
			salary -= good.getPrice();
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

	private void reFill(){
		if (!(merchants.isEmpty())){
			return;
		}
		System.out.println("Refilling...");
		while (merchants.put(Merchant.getCount(), new Merchant(RAND.nextLong(), Merchant.getCount())) != null){
		}	
		System.out.println("done refilling");	
	}
	
	/* least common action of clients */
	private void interact(){
		/* has an interaction with a merchant based on
		this clients behavior metric and based on this
		clients current relationship with the merchant.
		Good relationship means more likely to have a positive
		interaction and vice versa. High behavior metric means
		positive interactions and vice versa. If client passes 
		certain threshold their actions will influence the state
		of the game. If merchant chemistry reaches below 0 they leave
		the game. */

		// chemistry below 0 - remove
		// when chemistry is high more merchants are added
		


		/*int index = RAND.nextInt(Merchant.getCount());
		while (merchants.get(index) == null){
			if (merchants.isEmpty()){
				//reFill();
				return;
			}
			index = RAND.nextInt(Merchant.getCount());
		}

		Merchant myMerchant = merchants.get(index);
		if (myMerchant == null){
			return;
		}*/

		Merchant myMerchant = null;
    	while(myMerchant == null){
    		System.out.println("GETTING NEW MERCHANT");
    		myMerchant = merchants.get(RAND.nextInt(Merchant.getCount()));
    	}

		if (Math.random() >= behavior){
			if (myMerchant.badInteraction()){
				// remove
				//System.out.println("Client " + id + " started bad interaction");
				while (merchants.remove(myMerchant.id()) == null){
					//reFill();
					return;
				}
				System.out.println("removed " + myMerchant.id() + " - Size: " + merchants.size());
			}
		} else {
			if(myMerchant.goodInteraction()){
				//add
				//System.out.println("Client " + id + " started good interaction");
				while (merchants.put(Merchant.getCount(), new Merchant(RAND.nextLong(), Merchant.getCount())) != null){
				}
				System.out.println("added: " + (Merchant.getCount() - 1) + " - Size: " + merchants.size());
			}
		}
	}
}
