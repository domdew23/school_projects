public class VendingMachine{
	private int quarters=0, nickels=0, dimes=0, value=0;
	private Time current; 

	public VendingMachine(int initQuarters, int initNickels, int initDimes){
		this.quarters = initQuarters;
		this.nickels = initNickels;
		this.dimes = initDimes;	
		this.current = new Time(0, 0);
	}

	public void run(char x, double e){
		int ta = timeAdvance();
		Time next;
		if (e == 0){
			next = new Time(e, 1); // go up
		}
		if (e > ta){
			next = new Time(ta, 0);
			printOutput(lambda());
			deltaInternal();
			current = current.advance(next);
			System.out.println("Executing deltaInternal at " + getReal());
			run(x, e-ta);
			return;
		} else if (e == ta) {
			next = new Time(ta, 0);
			printOutput(lambda());
			System.out.print("Executing deltaConfluent at ");
			deltaConfluent(x);
		} else {
			next = new Time(e, 0);
			System.out.print("Executing deltaExternal at ");
			deltaExternal(x, e);
		}

		current = current.advance(next);
		System.out.println(getReal());
	}

	public void deltaExternal(char x, double e){
		// gets executed when there is input to the machine
		switch (x){
			case 'q': quarters++; value += 25; break;
			case 'd': dimes++; value += 10; break;
			case 'n': nickels++; value += 5; break;
			default: break;
		}
	}

	public void deltaInternal(){
		// gets executed when there needs to be output
		if (value >= 100){
			value %= 100;
		}
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

	public void deltaConfluent(char x){
		// gets exectued when there is both input and output
		deltaInternal();
		deltaExternal(x, 2.0);
	}

	// lambda and delta cannot depend on each other (can call similar code in both lambda and delta)
	public int[] lambda(){
		try{
			int coffee = value/100;
			int[] change = dispense_change();
			int[] y = {coffee, change[0], change[1], change[2]};
			return y;
		} catch (AtomicModelException e){
			e.printStackTrace();
			System.out.println("OUT OF ORDER");
			System.exit(0);
			return null;
		}
	}

	public int timeAdvance(){
		if (value > 0){
			return 2;
		}
		return Integer.MAX_VALUE;
	}

	public void printState(){
		System.out.println("\nquarters: " + quarters + " || nickels: " + nickels
		+ " || dimes: " + dimes + " || value: " + value + "\n");
	}

	public double getReal(){
		return current.getReal();
	}

	private static void printOutput(int[] y){
		String output = "";
		int coffee=0, quarters=0, nickels=0, dimes=0;
		for (int i = 0; i < y.length; i++){
			switch (i){
				case 0: coffee   = y[i]; break; //coffee to output
				case 1: quarters = y[i]; break; //output quarters
				case 2: nickels  = y[i]; break; //output nickels
				case 3: dimes    = y[i]; break; //output dimes
				default: System.out.println("Something went wrong");
			}
		}

		if (coffee > 0){
			output += (coffee + " Coffee(s)\n");
		}
		if ((quarters > 0 || nickels > 0) || dimes > 0){
			output += (quarters + " Quarters\n" + nickels + " Nickels\n" + dimes + " Dimes\n");
		}
		System.out.println("\n=================\n" + output + "=================\n");
	}

	private int[] dispense_change() throws AtomicModelException{
		int q_count=0, n_count=0, d_count=0;
		int tmpValue = value % 100;
		if (tmpValue != 0){
			while(tmpValue != 0){
				while (tmpValue >= 25){
					if (quarters > 0){
						if (tmpValue < 50 && tmpValue % 10 == 0){
							if (dimes > 0){
								break;
							}
						}
						tmpValue -= 25;
						q_count++;
					} else {
						break;
					}
				}

				while (tmpValue >= 10){
					if (dimes > 0){
						tmpValue -= 10;
						d_count++;
					} else {
						break;
					}
				}

				while (tmpValue >= 5){
					if (nickels > 0){
						tmpValue -= 5;
						n_count++;
					} else {
						break;
					}
				}
				if (tmpValue != 0){
					throw new AtomicModelException("Insufficent Change. Please call xxx-xxx-xxxx for more information");
				}
			}
		}
		int[] change = {q_count, n_count, d_count};
		return change;
	}
}
