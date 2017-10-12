import java.util.Scanner;

public class main {
	public static void main(String[] args){
		//accepts nickels dimes and quarters
		//when 1 dollar is inserted dispense coffee
		//when change is pressed return unspent coins
		//input: {nickel, dime, quarter, cancel, wait}
		//output: {nickel,dime,quarter,coffee,nothing}
		// apply output function before the state transition
		System.out.println("\nOptions:\nq - Quarters || n - Nickels || d - Dimes || c - Cancel || w - Wait || quit - Quit\n");
		start();
	}

	private static void start(){

		VendingMachine VM = new VendingMachine(10, 5, 8); // Q N D
		Scanner sc = new Scanner(System.in);
		String input = "";
		while (true){
			VM.print_state();
			input = sc.nextLine();
			String[] X = new String[input.length()];
			if (!fill_bag(input, X)){
				continue;
			}
			System.out.println(VM.lambda());
			VM.delta(X);
		}
	}

	private static boolean fill_bag(String input, String[] X){
		if (input.equals("quit")){
			System.exit(0);
		}
		for (int i = 0; i < input.length(); i++){
			input = input.toLowerCase();
			switch (input.charAt(i)){
				case 'q': X[i] = "q"; break;
				case 'n': X[i] = "n"; break;
				case 'd': X[i] = "d"; break;
				case 'c': X[i] = "c"; break;
				case 'w': X[i] = " "; break;
				case ' ': X[i] = " "; break;
				default:
					System.out.println("Please enter valid input");
					return false;	
			}
		}
		return true;
	}
}
