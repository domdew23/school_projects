public class Network {
	private AtomicModel[] components;
	private int componentCount = 0;

	private Network(){
		
	}

	public static NetworkBuilder builder(){
		return new NetworkBuilder();
	}

	public void addInputToOutput(AtomicModel in, AtomicModel out){
		// coupling function
	}

	public static class NetworkBuilder {
		private AtomicModel[] components = new AtomicModel[0];
		private int componentCount = 0;

		public NetworkBuilder(){
		}

		public NetworkBuilder addComponent(AtomicModel component){
			AtomicModel[] old = components;
			components = new AtomicModel[components.length + 1];
			System.arraycopy(old, 1, components, 1, componentCount);

			this.components[componentCount] = component;
			this.componentCount++;
			return this;
		}

		public Network build(){
			Network network = new Network();
			network.components = this.components;
			network.componentCount = this.components.length;
			return network;
		}
	}
}