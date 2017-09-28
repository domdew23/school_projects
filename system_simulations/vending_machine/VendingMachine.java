public class VendingMachine{
	private int quarters=0, nickels=0, dimes=0, value=0;
	private boolean change = false;

	public VendingMachine(int init_quarters, int init_nickels, int init_dimes){
		this.quarters = init_quarters;
		this.nickels = init_nickels;
		this.dimes = init_dimes;	
	}
	
	public void insert_coins(int new_quarters, int new_nickels, int new_dimes){
		quarters += new_quarters;
		nickels += new_nickels;
		dimes += new_dimes;
		update_value(new_quarters, new_nickels, new_dimes);
	}

	private void update_value(int q, int n, int d){
		value += ((q*25) + (n*5) + d*10);
	}


	public void set_change(boolean new_change){
		this.change = new_change;
	}

	private void dispense_coffee(){
		System.out.println("Dispensing coffee");
	}

	public String toString(){
		return "q: " + quarters + " || n: " + " || d: " + dimes + " || value: " + value + " || change: " + change;
	}
	
}
