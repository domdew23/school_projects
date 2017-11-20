import java.util.ArrayList;

public class Network {
	private AtomicModel[] components;
	private int componentCount;
	private ArrayList<AtomicModel> inputs;
	private ArrayList<AtomicModel> outputs;

	private Network(){
		
	}

	public static NetworkBuilder builder(){
		return new NetworkBuilder();
	}

	public void addInputToOutput(AtomicModel I, AtomicModel O){
		I.addOutput(O);
		O.addInput(I);
	}

	public AtomicModel[] getComponents(){
		return components;
	}

	public ArrayList<AtomicModel> getInputs(){
		return inputs;
	}

	public ArrayList<AtomicModel> getOutputs(){
		return outputs;
	}

	public static class NetworkBuilder{
		private AtomicModel[] components = new AtomicModel[10];
		private ArrayList<AtomicModel> inputs = new ArrayList<AtomicModel>();
		private ArrayList<AtomicModel> outputs = new ArrayList<AtomicModel>();
		private int componentCount = 0;

		public NetworkBuilder(){
		}

		public NetworkBuilder addComponent(AtomicModel component){
			/*AtomicModel[] old = components;
			components = new AtomicModel[components.length + 1];
			System.arraycopy(old, 1, components, 1, componentCount);
			*/
			
			this.components[componentCount++] = component;
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
			network.componentCount = this.components.length;
			network.inputs = this.inputs;
			network.outputs = this.outputs;
			return network;
		}
	}
}