public class VendingMachine{
	private int quarters=0, nickels=0, dimes=0, value=0;
	private boolean cancel = false;
	private int count = 0;
	private String coffee = "";
	private int state = 0;
	private String change = "";

	public VendingMachine(int init_quarters, int init_nickels, int init_dimes){
		this.quarters = init_quarters;
		this.nickels = init_nickels;
		this.dimes = init_dimes;	
	}
	
	public void print_state(){
		System.out.println("State #" + state + ":\nquarters: " + quarters + " || nickels: " + nickels
		+ " || dimes: " + dimes + " || value: " + value + " || cancel: " + cancel + "\n");
	}

	// lambda and delta cannot depend on each other (can call similar code in both lambda and delta)
	public String lambda(){
		return "\nOutput:\n---------------------\n" + coffee + change + "---------------------\n";
	}

	public void delta(String[] args){
		state++;
		cancel = false;
		change = "";
		coffee = "";
		int new_quarters=0, new_nickels=0, new_dimes=0;
		for (int i = 0; i < args.length; i++){
			switch (args[i]){
				case "q": new_quarters++; break;
				case "n": new_nickels++; break;
				case "d": new_dimes++; break;
				case "c": cancel = true; break;
				case " ": break;
				default : System.out.println("Something went wrong");
			}
		}
		value += (new_quarters*25) + (new_nickels*5) + (new_dimes*10);
		quarters += new_quarters;
		nickels += new_nickels;
		dimes += new_dimes;
		check();
	}

	private void check(){
		if (cancel){
			try {
				dispense_change();
			} catch (AtomicModelException e){
				e.printStackTrace();
				System.out.println("\nOUT OF ORDER...");
				System.exit(0);
			}
			return;
		}
		count = 0;
		if (value >= 100) {
			count = value/100;
			coffee = count + " coffee(s)\n";	
		}
		value = value % 100;
	}

	private void dispense_change() throws AtomicModelException{
		int q_count=0, n_count=0, d_count=0;
		if (value != 0){
			while(value != 0){
				while (value >= 25){
					if (has_quarters()){
						if (value < 50 && value % 10 == 0){
							if (has_dimes()){
								break;
							}
						}
						value -= 25;
						quarters--;
						q_count++;
					} else {
						break;
					}
				}

				while (value >= 10){
					if (has_dimes()){
						value -= 10;
						dimes--;
						d_count++;
					} else {
						break;
					}
				}

				while (value >= 5){
					if (has_nickels()){
						value -= 5;
						nickels--;
						n_count++;
					} else {
						break;
					}
				}
				if (value != 0){
					throw new AtomicModelException("Insufficent Change. Please call xxx-xxx-xxxx for more information");
				}
			}
		}
		change = "Change: " + q_count + " quarters\n" + n_count + " nickels -- " + d_count + " dimes.\n";
	}

	private boolean has_quarters(){
		if (quarters == 0){
			return false;
		}
		return true;
	}

	private boolean has_nickels(){
		if (nickels == 0){
			return false;
		}
		return true;
	}

	private boolean has_dimes(){
		if (dimes == 0){
			return false;
		}
		return true;
	}
}
