/* Clients are guilty by assoication (if one has a bad 
interaction with NPC (merchant) then all clients recieve
higher prices from that NPC). Most metrics will be random. */

import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
		this.merchants = MERCHANTS;
		this.id = id;
	}

	public void run(){
		/* execute one action per loop */
		while (true){
			if (merchants.isEmpty()){
				System.out.println("You lose.\nAll merchants have left the simulation.\n");
				System.exit(0);
			}
			if (Math.random() <= .95){
				buy();
			} else {
				interact();
			}
		}
	}

	/* most common action of clients */
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

		Merchant myMerchant = null;
    	while(myMerchant == null){
    		myMerchant = merchants.get(RAND.nextInt(Merchant.getCount()));
    	}
		if (Math.random() >= behavior){
			if (myMerchant.badInteraction()){
				// remove
				//System.out.println("Client " + id + " started bad interaction");
				while (merchants.remove(myMerchant.id()) == null){
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
