import java.util.Scanner;

public class main {
	public static void main(String[] args){
		System.out.println("\nOptions:\nq - Quarters || n - Nickels || d - Dimes || c - Cancel || w - Wait || quit - Quit\n");
		start();
	}

	private static void printOutput(int[] y){
		String output = "";
		int coffee=0, quarters=0, nickels=0, dimes=0, nothing=0;
		for (int i = 0; i < 5; i++){
			switch (i){
				case 0: coffee   = y[i]; break; //coffee to output
				case 1: quarters = y[i]; break; //output quarters
				case 2: nickels  = y[i]; break; //output nickels
				case 3: dimes    = y[i]; break; //output dimes
				case 4: nothing  = y[i]; break;//output nothing
				default: System.out.println("Something went wrong");
			}
		}

		if (nothing == 1){
			output = "Nothing.\n";
		} else if (coffee > 0){
			output += (coffee + " Coffee(s)\n");
		} else {
			output += (quarters + " Quarters\n" + nickels + " Nickels\n" + dimes + " Dimes\n");
		}
		System.out.println("\n=================\n" + output + "=================\n");
	}

	private static void start(){

		VendingMachine VM = new VendingMachine(10, 0, 8); // Q N D
		Scanner sc = new Scanner(System.in);
		String input = "";
		while (true){
			VM.print_state();
			input = sc.nextLine();
			String[] X = new String[input.length()];
			if (!fill_bag(input, X)){
				continue;
			}
			printOutput(VM.lambda());
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
