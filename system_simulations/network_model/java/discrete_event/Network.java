import java.util.ArrayList;

public class Network<Input, Output> {
	private AtomicModel[] components;
	private int componentCount;
	private ArrayList<Input> inputs;
	private ArrayList<Output> outputs;

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

	public ArrayList<Input> getInputs(){
		return inputs;
	}

	public ArrayList<Output> getOutputs(){
		return outputs;
	}

	public static class NetworkBuilder<Input, Output>{
		private AtomicModel[] components = new AtomicModel[0];
		private ArrayList<Input> inputs = new ArrayList<Input>();
		private ArrayList<Output> outputs = new ArrayList<Output>();
		private int componentCount = 0;

		public NetworkBuilder(){
		}

		public NetworkBuilder addComponent(AtomicModel component){
			AtomicModel[] old = components;
			components = new AtomicModel[components.length + 1];
			System.arraycopy(old, 1, components, 1, componentCount);

			this.components[componentCount++] = component;
			return this;
		}

		public NetworkBuilder addInput(Input I){
			this.inputs.add(I);
			return this;
		}

		public NetworkBuilder addOutput(Output O){
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