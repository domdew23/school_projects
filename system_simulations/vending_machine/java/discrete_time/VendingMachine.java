public class VendingMachine{
	private int quarters=0, nickels=0, dimes=0, value=0;
	private boolean cancel = false;
	private int state = 0;

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
	public int[] lambda(){
		int coffee=0, nothing=0;

		if (cancel){
			try{
				int[] change = dispense_change();
				int[] y = {coffee, change[0], change[1], change[2], nothing};
				return y;
			} catch (AtomicModelException e){
				e.printStackTrace();
				System.out.println("OUT OF ORDER");
				System.exit(0);
			}
		} else if (value >= 100){
			coffee = value/100;
		}

		if (coffee == 0){
			nothing = 1;
		}

		int[] y = {coffee, 0, 0, 0, nothing};
		return y;
	}

	public void delta(String[] args){
		state++;
		if (cancel){
			cancel = false;
			if (value > 0){
				changeState();
			}
		} else if (value >= 100){
			value %= 100;
		}

		for (int i = 0; i < args.length; i++){
			switch (args[i]){
				case "q": quarters++; value += 25;break;
				case "n": nickels++; value += 5;break;
				case "d": dimes++; value += 10; break;
				case "c": cancel = true; break;
				case " ": break;
				default : System.out.println("Something went wrong");
			}
		}
	}

	private void changeState(){
		try {
			int[] change = dispense_change();
			for (int i = 0; i < change[0]; i++){
				value -= 25;
				quarters--;
			}
			for (int i = 0; i < change[1]; i++){
				value -= 5;
				nickels--;
			}
			for (int i = 0; i < change[2]; i++){
				value -= 10;
				dimes--;
			}
	} catch (AtomicModelException e){
			e.printStackTrace();
			System.out.println("\nOUT OF ORDER...");
			System.exit(0);		
		}
	}

	private int[] dispense_change() throws AtomicModelException{
		int q_count=0, n_count=0, d_count=0;
		if (value != 0){
			while(value != 0){
				while (value >= 25){
					if (quarters > 0){
						if (value < 50 && value % 10 == 0){
							if (dimes > 0){
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
					if (dimes > 0){
						value -= 10;
						dimes--;
						d_count++;
					} else {
						break;
					}
				}

				while (value >= 5){
					if (nickels > 0){
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
		int[] change = {q_count, n_count, d_count};
		return change;
	}
}
