import java.util.ArrayList;

public class Network<T>{
	private ArrayList<T> components;
	private ArrayList<T> inputs;
	private ArrayList<T> outputs;
	private int componentCount;

	private Network(){}

	public static NetworkBuilder builder(){
		return new NetworkBuilder();
	}

	public ArrayList<T> getComponents(){
		return components;
	}

	public ArrayList<T> getInputs(){
		return inputs;
	}

	public ArrayList<T> getOutputs(){
		return outputs;
	}

	public static class NetworkBuilder<T>{
		private ArrayList<T> components = new ArrayList<T>();
		private ArrayList<T> inputs = new ArrayList<T>();
		private ArrayList<T> outputs = new ArrayList<T>();
		private int componentCount = 0;

		public NetworkBuilder(){}

		public NetworkBuilder addComponent(T component){
			this.components.add(component);
			return this;
		}

		public NetworkBuilder addInput(T I){
			this.inputs.add(I);
			return this;
		}

		public NetworkBuilder addOutput(T O){
			this.outputs.add(O);
			return this;
		}

		public Network build(){
			Network network = new Network();
			network.components = this.components;
			network.componentCount = this.components.size();
			network.inputs = this.inputs;
			network.outputs = this.outputs;
			return network;
		}
	}
}