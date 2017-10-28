#include <iostream>
#include "AtomicModel.h"
#include "MemoryModel.h"
using namespace std;

MemoryModel::MemoryModel(){
	state[0] = 0;
	state[1] = 0;
	tick = 0;
}

bool MemoryModel::lambda(){
	//cout << "M output: " << state[0] << endl << "==================" << endl;
	return state[0];
}

void MemoryModel::delta(bool x1, bool x2){
	cout << "M Input: " << x1 << x2 << endl;
	state[0] = state[1];
	state[1] = x2;
	tick++;
	cout << "M New State: " << state[0] << state[1] << endl;

}
