public class Scheduler {
	
	private VendingMachine VM;

	public Scheduler(VendingMachine VM){
		this.VM = VM;
	}

	public void schedule(char x, double e){
		int ta = VM.timeAdvance();
		Time next;
		if (e == 0){
			next = new Time(e, 1); // go up
		}
		if (e > ta){
			next = new Time(ta, 0);
			printOutput(VM.lambda());
			VM.deltaInternal();
			VM.current = VM.current.advance(next);
			System.out.println("Executing deltaInternal at " + VM.getReal());
			schedule(x, e-ta);
			return;
		} else if (e == ta) {
			next = new Time(ta, 0);
			printOutput(VM.lambda());
			System.out.print("Executing deltaConfluent at ");
			VM.deltaConfluent(x);
		} else {
			next = new Time(e, 0);
			System.out.print("Executing deltaExternal at ");
			VM.deltaExternal(x, e);
		}

		VM.current = VM.current.advance(next);
		System.out.println(VM.getReal());
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
}