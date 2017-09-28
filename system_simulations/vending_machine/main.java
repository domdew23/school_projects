import java.util.Scanner;

public class main {
	public static void main(String[] args){
		//accepts nickels dimes and quarters
		//when 1 dollar is inserted dispense coffee
		//when change is pressed return unspent coins
		//input: {nickel, dime, quarter, cancel, wait}
		//output: {nickel,dime,quarter,coffee,nothing}
		// apply output function before the state transition
		
		VendingMachine VM = new VendingMachine(0, 0, 0);
		
		Scanner sc = new Scanner(System.in);
		String input = "";
		while (true){
			System.out.println("Options:\n q - Quarters || n - Nickels || d - Dimes || c - Change || quit - Quit\n");
			input = sc.nextLine();
			if (input.equals("quit")){
				break;
			}
			boolean bad_input = false;
			boolean wants_change = false;
			int quarter_count=0, nickel_count=0, dime_count=0;
			for (int i = 0; i < input.length(); i++){
				if (bad_input){
					break;
				}
				switch (input.charAt(i)){
					case 'q':
						quarter_count++;
						break;
					case 'n': 
						nickel_count++;
						break;
					case 'd':
						dime_count++;
						break;
					case 'c':
						wants_change = true;
						break;
					case ' ':
						break;
					default:
						System.out.println("Please enter valid input");
						bad_input = true;
						break;
				}
			}
			VM.insert_coins(quarter_count, nickel_count, dime_count);
			VM.set_change(wants_change);
			System.out.println(VM.toString()); 		
		}
	}
}
