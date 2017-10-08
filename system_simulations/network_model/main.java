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

		if (true ^ false){
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}
}