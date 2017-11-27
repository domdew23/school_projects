/* Composite */
import java.util.ArrayList;

public class Network<T> {
	private ArrayList<T> components;
	private ArrayList<T> inputs;
	private ArrayList<T> outputs;
	private boolean state;
	private int componentCount;

	private Network(){}

	public ArrayList<T> getComponents(){
		return components;
	}

	public ArrayList<T> getInputs(){
		return inputs;
	}

	public ArrayList<T> getOutputs(){
		return outputs;
	}

	public static NetworkBuilder builder(){
		return new NetworkBuilder();
	}

	public static class NetworkBuilder<T> {
		private ArrayList<T> components;
		private ArrayList<T> inputs;
		private ArrayList<T> outputs;

		public NetworkBuilder(){
			components = new ArrayList<T>();
			inputs = new ArrayList<T>();
			outputs = new ArrayList<T>();
		}

		public NetworkBuilder addComponent(T component){
			components.add(component);
			return this;
		}

		public NetworkBuilder addInput(T in){
			inputs.add(in);
			return this;
		}

		public NetworkBuilder addOutput(T out){
			outputs.add(out);
			return this;
		}

		public Network build(){
			Network<T> network = new Network<T>();
			network.components = this.components;
			network.inputs = this.inputs;
			network.outputs = this.outputs;
			return network;
		}
	}
}
