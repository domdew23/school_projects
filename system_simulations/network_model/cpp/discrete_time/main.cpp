#include <iostream>
#include "AtomicModel.h"
#include "Network.h"
#include "XORModel.h"
#include "MemoryModel.h"
using namespace std;

int main(int argc, char** argv){
	Network* network = Network::NetworkBuilder().addComponent(new XORModel(1)).addComponent(new XORModel(2)).addComponent(new MemoryModel()).build();
	char* input = new char[2];
	while(1){
		cout << "Enter input:" << endl;
		cin >> input;
		char c1 = input[0];
		char c2 = input[1];
		bool x1 = (c1 == '1');
		bool x2 = (c2 == '1');
		cout << "Network output: " << network->lambda() << endl;
		network->delta(x1, x2);
	}
}








