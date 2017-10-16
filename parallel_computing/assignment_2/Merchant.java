import java.util.Random;

public class Merchant {
	// bad relationship means high prices
	// good relationship means low prices
	private double chemistry;
	private double[] goodPrices; // for simplicity assume merchant has infinate amount of each good
	private int goodCount;
	private double avgPrice;
	private Random rand;
	private int id;

	public Merchant(long seed, int id){
		rand = new Random(seed);
		goodCount = rand.nextInt(100);
		goodPrices = new double[goodCount];
		chemistry = (rand.nextDouble() * 100); // max = 100.0 : min = 0.0
		this.id = id;
		fill();
	}

	private void fill(){
		double total = 0.0;
		for (int i = 0; i < goodCount; i++){
			goodPrices[i] = (rand.nextDouble() * rand.nextInt(5000))/(chemistry/rand.nextInt(50));
			total += goodPrices[i];
		}
		avgPrice = (total / goodCount);
	}

	public void printGoodPrices(){
		for (int i = 0; i < goodPrices.length; i++){
			System.out.printf("Goods #" + i + " price: $%.2f", goodPrices[i]);
			System.out.println();
		}
	}

	public double getAvgPrice(){
		return avgPrice;
	}

	public int getGoodCount(){
		return goodCount;
	}

	public double[] getGoods(){
		return goodPrices;
	}

	public double getChemistry(){
		return chemistry;
	}
}