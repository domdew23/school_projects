/* Clients are guilty by assoication (if one has a bad 
interaction with NPC (merchant) then all clients recieve
higher prices from that NPC). Most metrics will be random. */

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Client implements Runnable {
	// thread local:
	private double behavior; // high behavior metric means more likely to produce positive interactions and vice versa
	private final Random RAND;
	private static ConcurrentHashMap<Integer,Merchant> merchants;
	private int id;

	public Client(int id, long seed, ConcurrentHashMap<Integer,Merchant> MERCHANTS){
		RAND = new Random(seed);
		behavior = RAND.nextDouble();
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
			}
		}
	}

	/* most common action of clients */
	private void travel(){
		/* simulates clients traveling the world and
		doing nothing. This function has no influence 
		on game state and does not need to read game state.
		Sleeps for random amount of time between 0 - 10 seconds. */

		try {
			Thread.sleep(RAND.nextInt(10000));
		} catch (InterruptedException e){
			return;
		}

	}

	/* second most common action of clients */
	private void buy(){
		/* must read game state to get the prices of goods
		before trying to make a purchase. (read -> buy) */

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
		of the game */

	}

}