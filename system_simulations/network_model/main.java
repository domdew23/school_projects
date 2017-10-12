import java.util.Scanner;

public class main {
	public static void main(String[] args){
		// main function -> y(n+1) = y(n) XOR (x1(n) XOR x2(n))
		// x1(n)/x2(n) are the two one-bit inputs to the network at tick n
		// y(n) is the one-bit output of the network at tick n

		// input to the network is combined with input to XOR1
		// output of XOR1 is combined with input to XOR2
		// output of XOR2 is combined with output of the network
		// output of XOR2 is also combined with input to a memory atomic model, M
		// output of M is combined with input to XOR2
		// output and state values of each XOR model is a single bit
		// input set of each XOR models contains two bits {b1, b2}
		// output function of each XOR model is lamda(s) = {s}
		// state transition function of each XOR model is delta(s, {b1, b2}) = b1 XOR b2
		// input and output of M are a single bit
		// state transition function of M is delta((q1, q2), {x}) = (q2, x)

		// Clients should be able to:
		// 1) control rate at which time in the simulation advances
		// 2) be notified when any component, atomic or network, produces output and when an atomic component changes state
		// 3) inject input into a running simulation to support interactive simulations
		// state of M is a pair of bits (q1, q2)

		// make sure lambda and delta are independent of each other
		// lambda and delta can have similar functions to one another

		AtomicModel XOR1 = new XORModel();
		AtomicModel XOR2 = new XORModel();
		AtomicModel M = new MemoryModel();

		AtomicModel[] components = {XOR1, XOR2, M};
		Network network = Network.builder().addComponent(XOR1).addComponent(XOR2).addComponent(M).build();


		boolean[] input = new boolean[2];
		Scanner sc = new Scanner(System.in);
		String str = ""; 	
		boolean bad_input = false;

		while (!str.equals("quit")){
			bad_input = false;
			System.out.println("Enter input:\n");
			str = sc.nextLine();

			for (int i = 0, index=0; i < str.length(); i++, index++){
				if (bad_input)
					break;
				switch (str.charAt(i)){
					case '1': input[index] = true; break;
					case '0': input[index] = false; break;
					case ' ': index--; break;
					default: System.out.println("Invalid input"); bad_input = true; break;
				}
			}
			
			if (!bad_input){
				System.out.println("\n" + ((network.lambda()) ? 1 : 0));
				network.delta(input[0], input[1]);
			}
		}
	}
}







