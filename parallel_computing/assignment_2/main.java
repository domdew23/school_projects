public class main{
	public static void main(String[] args){
		/*
		~10 Merchants, ~50 clients
		Non-blocking data structure to store game state
		Game state is collection of Merchants
		Solution using a data struture and/or locking scheme - use standard JDK components
		Compare throughput (amount of items passed to system) across two different loads on two different platforms - use JMH
		Plot results as a graph on a web page
		*/

		Thread t = new Thread(new Client());

	}
}
