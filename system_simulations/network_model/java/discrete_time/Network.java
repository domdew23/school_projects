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
		return components[1].lambda();
	}

	public void delta(boolean[] X){
		for (int i = 0; i < componentCount; i++){
			boolean[][] inputs = coupling(X);
			for (int j = 0; j < componentCount; j++){
				components[j].delta(inputs[j]); // input to the Network
			}
			System.out.println();
		}
		// network does not have state, it's state is the state of all of its components
		// output of the network (in this case) is output of XOR2 - output is not the state
		System.out.println("\nNetwork state: " + (lambda() ? 1 : 0) + "\n");
		tick++;
	}

	// need to make coupling function more abstract
	// **should not be hard coded in **
	private boolean[][] coupling(boolean[] X){
		boolean[] outputs = new boolean[componentCount];
		boolean[][] retVal = new boolean[componentCount][2];

		for (int i = 0; i < componentCount; i++){
			outputs[i] = components[i].lambda();
		}

		for (int i = 0; i < componentCount; i++){
			if (i == 0){
				boolean[] tmp = X;
				retVal[i] = tmp;
			} else {
				boolean[] tmp = {outputs[componentCount-1], outputs[i-1]};
				retVal[i] = tmp;
			}
		}
		return retVal;
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
