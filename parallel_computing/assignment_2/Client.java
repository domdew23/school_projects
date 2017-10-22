/* Clients are guilty by assoication (if one has a bad 
interaction with NPC (merchant) then all clients recieve
higher prices from that NPC). Most metrics will be random. */

import java.util.Random;
///import java.util.concurrent.ConcurrentHashMap;

public class Client implements Runnable {
	// thread local:
	private double behavior; // high behavior metric means more likely to produce positive interactions and vice versa
	private final Random RAND;
	private static ConcurrentHashMap<Integer,Merchant> merchants;
	private int id;
	private double salary;

	public Client(int id, long seed, ConcurrentHashMap<Integer,Merchant> MERCHANTS){
		RAND = new Random(seed);
		behavior = RAND.nextDouble();
		salary = (RAND.nextDouble() * RAND.nextInt(100000));
		//System.out.printf("Salary: $%.2f", salary);
		//System.out.println();
		this.merchants = MERCHANTS;
		this.id = id;
	}

	public void run(){
		/* execute one action per loop */
		while (true){
			if (Math.random() <= .35){
				if (Math.random() <= .80){
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
				Thread.sleep(RAND.nextInt(500));
				//System.out.printf("Earned: $%.2f", earnings);
				//System.out.println();
			} else {
				// simulate doing nothing
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

		Merchant myMerchant = merchants.get(RAND.nextInt(merchants.size()));
		Good good = myMerchant.getGoods()[RAND.nextInt(myMerchant.getGoodCount())];
		if (!(good.getPrice() > salary)){
			System.out.print("Client " + id + " just made a purchase from " + myMerchant.id() + " of good " + good.id());
			System.out.printf(" for $%.2f", good.getPrice());
			System.out.print(" || quant: " + good.getQuantity()); 
			System.out.printf(" salary is before $%.2f", salary);
			//System.out.println();
			good.buy();
			salary -= good.getPrice();
			System.out.printf(" || salary is now $%.2f", salary);
			System.out.print(" || good quant now: " + good.getQuantity());
			System.out.println();
		}
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

		//Merchant myMerchant = merchants.get(RAND.nextInt(merchants.size()));
		// chemistry below 0 - remove
		// when chemistry is high more merchants are added
		if (Math.random() >= behavior){
			merchants.get(RAND.nextInt(merchants.size())).badInteraction();
		} else {
			merchants.get(RAND.nextInt(merchants.size())).goodInteraction();
		}
	}

}