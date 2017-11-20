#include <iostream>
#include <fstream>
#include <string>
#include "VendingMachine.h"
#include "Scheduler.h"
using namespace std;

int main(int argc, char** argv){
	if (argc <= 1){
		cout << "Please supply and input file" << endl;
		return 0;	
	} else {
		double time;
		char x;
		VendingMachine* VM = new VendingMachine(5, 5, 5);
		Scheduler* scheduler = new Scheduler(VM);
		ifstream file(argv[1]);
		string line;
		
		while (getline(file, line)){
			file >> time >> x;
			double e = time - VM->get_real();
			scheduler->schedule(x, e);
			VM->print_state();
		}
	}
	return 0;
}
