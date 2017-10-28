#include <iostream>
#include "AtomicModel.h"
#include "Network.h"
using namespace std;

Network::Network(NetworkBuilder* builder){
	for (int i = 0; i < builder->compCount; i++){
		this->components[i] = builder->components[i];
	}
	this->compCount = builder->compCount;
	this->tick = 0;
}

bool Network::lambda(){
	return components[compCount-2]->lambda();
}

void Network::delta(bool x1, bool x2){
	for (int i = 0; i < compCount; i++){
		bool** inputs = couple(x1, x2);
		for (int j = 0; j < compCount; j++){
			cout << j << endl;
			components[j]->delta(inputs[j][0], inputs[j][1]);
		}
		cout << endl;
	}
	cout << "New Network Output: " << lambda() << endl;
}

bool** Network::couple(bool x1, bool x2){
	bool* outputs = new bool[compCount];
	for (int i = 0; i < compCount; i++){
		outputs[i] = components[i]->lambda();
	}

	bool* XOR1x = new bool[2]{x1, x2};
	bool* XOR2x = new bool[2]{outputs[0], outputs[2]};
	bool* Mx = new bool[2]{outputs[1], outputs[2]};
	//bool ret[compCount][2] = ;

	bool** retValue; //= new bool[3][2]{{XOR1x}, {XOR2x}, {Mx}};
	retValue[0] = XOR1x;
	retValue[1] = XOR2x;
	retValue[2] = Mx;

	return retValue;
}

Network::NetworkBuilder::NetworkBuilder(){
	this->compCount = 0;
}

Network::NetworkBuilder& Network::NetworkBuilder::addComponent(AtomicModel* component){
	this->components[this->compCount] = component;
	this->compCount++;
	return *this;
}

Network* Network::NetworkBuilder::build(){
	return new Network(this);
}