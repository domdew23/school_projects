import java.util.Random;

public class Good {
	private int id;
	private double price;
	private int quantity;
	private Random rand;

	public Good(double price, int quantity, int id){
		rand = new Random();
		if (price <= 0){
			price += ((rand.nextDouble() * 100) * rand.nextInt(1000));
		}
		this.price = price;
		this.quantity = quantity;
		this.id = id;
	}

	public void buy(){
		if (quantity > 0){
			quantity--;
		} else {
			reStock(rand.nextInt(300));
			quantity--;
		}
	}

	private void reStock(int amt){
		while(amt <= 0){
			amt = rand.nextInt(300);
		}
		quantity += amt;
	}

	public double getPrice(){
		return price;
	}

	public int getQuantity(){
		return quantity;
	}

	public int id(){
		return id;
	}
}