#ifndef NETWORK_H
#define NETWORK_H
#include "AtomicModel.h"
using namespace std;

class Network : public AtomicModel{
	private:
		int compCount;
		AtomicModel* components[20];
		int tick;
	public:
		class NetworkBuilder {
			private:
				AtomicModel* components[20];
				int compCount;
			public:
				NetworkBuilder();
				NetworkBuilder& addComponent(AtomicModel* component);
				Network* build();
				friend Network;
		};
		Network(NetworkBuilder* builder);
		bool lambda();
		void delta(bool x1, bool x2);
		bool** couple(bool x1, bool x2);
};

#endif