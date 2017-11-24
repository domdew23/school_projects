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
					cout << "start add " << component << endl;
					if (component == NULL || component == nullptr){
						cout << "null for some reason"<< endl;
					} else {
						this->components.push_back(component);
						cout << "end add" << endl;
					}
				}

				NetworkBuilder& add_input(T* input){
					cout << "start inp" << endl;
					this->inputs.push_back(input);
					cout << "end inp" << endl;
				}

				NetworkBuilder& add_output(T* output){
					cout << "start out" << endl;
					this->outputs.push_back(output);
					cout << "end out" << endl;
				}

				Network* build(){
					cout << "start build" << endl;
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
	private:
		vector<T*> components;
		vector<T*> inputs;
		vector<T*> outputs;
};

#endif