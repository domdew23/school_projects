#ifndef NETWORK
#define NETWORK
#include <vector>
#include "AtomicModel.cpp"
using namespace std;

template<class T>
class Network {
	public:
		class NetworkBuilder {
			public:
				NetworkBuilder(){
				}

				NetworkBuilder& add_component(T* component){
					this->components.push_back(component);
					return *this;
				}

				NetworkBuilder& add_input(T* input){
					this->inputs.push_back(input);
					return *this;
				}

				NetworkBuilder& add_output(T* output){
					this->outputs.push_back(output);
					return *this;
				}

				Network* build(){
					return new Network(this);
				}

			private:
				vector<T*> components;
				vector<T*> inputs;
				vector<T*> outputs;
			friend Network;
		};

		Network(NetworkBuilder* builder){
			this->components = builder->components;
			this->inputs = builder->inputs;
			this->outputs = builder->outputs;
		}

		vector<T*> get_inputs(){
			return inputs;
		}

		vector<T*> get_outputs(){
			return outputs;
		}

		vector<T*> get_components(){
			return components;
		}

		string get_name(){
			return "Network";
		}
		
	private:
		vector<T*> components;
		vector<T*> inputs;
		vector<T*> outputs;
};

#endif