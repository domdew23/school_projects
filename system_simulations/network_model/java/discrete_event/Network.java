import java.util.ArrayList;

public class Network {
	private ArrayList<AtomicModel> components;
	private ArrayList<AtomicModel> inputs;
	private ArrayList<AtomicModel> outputs;
	private int componentCount;

	private Network(){}

	public static NetworkBuilder builder(){
		return new NetworkBuilder();
	}

	public void addInputToOutput(AtomicModel I, AtomicModel O){
		I.addOutput(O);
		O.addInput(I);
	}

	public ArrayList<AtomicModel> getComponents(){
		return components;
	}

	public ArrayList<AtomicModel> getInputs(){
		return inputs;
	}

	public ArrayList<AtomicModel> getOutputs(){
		return outputs;
	}

	public static class NetworkBuilder{
		private ArrayList<AtomicModel> components = new ArrayList<AtomicModel>();
		private ArrayList<AtomicModel> inputs = new ArrayList<AtomicModel>();
		private ArrayList<AtomicModel> outputs = new ArrayList<AtomicModel>();
		private int componentCount = 0;

		public NetworkBuilder(){}

		public NetworkBuilder addComponent(AtomicModel component){
			this.components.add(component);
			return this;
		}

		public NetworkBuilder addInput(AtomicModel I){
			this.inputs.add(I);
			return this;
		}

		public NetworkBuilder addOutput(AtomicModel O){
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