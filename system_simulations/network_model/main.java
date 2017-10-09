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
		// state of M is a pair of bits (q1, q2)
		// state transition function of M is delta((q1, q2), {x}) = (q2, x)

		// Clients should be able to:
		// 1) control rate at which time in the simulation advances
		// 2) be notified when any component, atomic or network, produces output and when an atomic component changes state
		// 3) inject input into a running simulation to support interactive simulations
		

		if (true ^ false){
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}
}