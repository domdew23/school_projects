#ifndef NETWORK
#define NETWORK
#include <vector>
#include "AtomicModel.cpp"
using namespace std;

class Network {
	public:
		class NetworkBuilder {
			public:
				NetworkBuilder(){
					this->comp_count = 0;
				}

				NetworkBuilder& addComponent(AtomicModel* component){
					components.push_back(component);
					comp_count++;
				}

				Network* build(){
					return new Network(this);
				}
			private:
				vector<AtomicModel*> components;
				int comp_count;
			friend Network;
		};

		Network(NetworkBuilder* builder){
			this->components = builder->components;
			this->comp_count = builder->comp_count;
		}
	
	private:
		vector<AtomicModel*> components;
		int comp_count;
};

#endif