#include <iostream>
#include "AtomicModel.h"
#include "XORModel.h"
using namespace std;

XORModel::XORModel(int id){
	state = 0;
	tick = 0;
	this->id = id;
}

bool XORModel::lambda(){
	//cout << "XOR" << id << " Output: " << state << endl;
	return state;
}

void XORModel::delta(bool x1, bool x2){
	cout << "XOR" << id << " Input: " << x1 << x2 << endl;
	state = (x1 ^ x2);
	tick++;
	cout << "XOR" << id << " New State: " << state << endl << "==================" << endl;
}
