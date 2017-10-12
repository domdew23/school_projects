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

	public void delta(boolean x1, boolean x2){
		for (int i = 0; i < componentCount; i++){
			boolean[] X1 = {components[0].lambda(), components[2].lambda()};
			boolean[] X2 = {components[2].lambda(), components[1].lambda()};
			components[0].delta(x1, x2);
			components[1].delta(X1[0], X1[1]);
			components[2].delta(X2[0], X2[1]);
			//System.out.println("\nNetwork state: " + ((state) ? 1 : 0) + "\nnext atomic tick...\n");
			System.out.println("");
		}
		state = components[2].lambda();
		System.out.println("\nNetwork state: " + ((state) ? 1 : 0) + "\n");
		tick++;
	}

	public int getTick(){
		return this.tick;
	}


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
