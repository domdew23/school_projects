import java.util.Random;

public class Merchant {
	// bad relationship means high prices
	// good relationship means low prices
	private double chemistry;
	private int goodCount;
	private Good[] goods;
	private double avgPrice;
	private Random rand;
	private int id;

	public Merchant(long seed, int id){
		rand = new Random(seed);
		goodCount = rand.nextInt(100);
		goods = new Good[goodCount];
		chemistry = (rand.nextDouble() * 100); // max = 100.0 : min = 0.0
		this.id = id;
		fill();
	}

	private void fill(){
		double total = 0.0;
		for (int i = 0; i < goodCount; i++){
			goods[i] = new Good((rand.nextDouble() * rand.nextInt(5000))/(chemistry/rand.nextInt(50)),rand.nextInt(300), i);
			total += goods[i].getPrice();
		}
		avgPrice = (total / goodCount);
	}

	public void printGoodPrices(){
		for (int i = 0; i < goods.length; i++){
			System.out.printf("Goods #" + i + " price: $%.2f", goods[i].getPrice());
			System.out.println();
		}
	}

	public void printMerchant(){
		System.out.println("\nMember #" + id + " || Chemistry: " + chemistry + " || avg price: " + avgPrice);			
		System.out.println("====================");
		printGoodPrices();
		System.out.println("====================");
	}

	public void badInteraction(){

	}

	public void goodInteraction(){
		
	}

	public double getAvgPrice(){
		return avgPrice;
	}

	public int getGoodCount(){
		return goodCount;
	}

	public Good[] getGoods(){
		return goods;
	}

	public double getChemistry(){
		return chemistry;
	}

	public int id(){
		return id;
	}
}