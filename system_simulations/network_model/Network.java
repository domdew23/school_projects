/* Composite */
public class Network implements AtomicModel{
	private AtomicModel[] components;
	private boolean state;
	private int componentCount;
	private int tick;

	private Network(){
	}

	public static NetworkBuilder builder(){
		return new NetworkBuilder();
	}

	public boolean lambda(){
		return state;
	}

	public void delta(boolean[] X){
		for (int i = 0; i < componentCount; i++){
			boolean[][] inputs = coupling(X);
			for (int j = 0; j < componentCount; j++){
				components[j].delta(inputs[j]); // input to the Network
			}
			System.out.println();
		}
		state = components[1].lambda();
		System.out.println("\nNetwork state: " + ((state) ? 1 : 0) + "\n");
		tick++;
	}

	private boolean[][] coupling(boolean[] X){
		boolean XOR1y = components[0].lambda(); // coupled with input to XOR2
		boolean XOR2y = components[1].lambda(); // coupled with output of network AND input to M
		boolean My = components[2].lambda(); // coupled with input to XOR2

		boolean[] XOR1x = X;
		boolean[] XOR2x = {XOR1y, My};
		boolean[] Mx = {My, XOR2y};
		boolean[][] retValue = {XOR1x, XOR2x, Mx};

		return retValue;
	}

	public int getTick(){
		return this.tick;
	}

	// coupling function push over to dest

	public static class NetworkBuilder {
		private AtomicModel[] components = new AtomicModel[3];
		private int componentCount = 0;

		public NetworkBuilder(){
		}

		public NetworkBuilder addComponent(AtomicModel component){
			this.components[componentCount] = component;
			this.componentCount++;
			return this;
		}

		public Network build(){
			Network network = new Network();
			network.components = this.components;
			network.componentCount = this.components.length;
			network.state = false;
			network.tick = 0;
			return network;
		}
	}
}
