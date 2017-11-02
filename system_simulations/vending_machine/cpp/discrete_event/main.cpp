#include <iostream>
#include "VendingMachine.h"
#include "Scheduler.h"
using namespace std;

int main(int argc, char** argv){
	if (argc <= 1){
		cout << "Please supply and input file" << endl;
		exit(0);	
	} else {
		cout << "Input file supplied" << endl;
	}

	VendingMachine* VM = new VendingMachine(5, 5, 5);
	Scheduler* scheduler = new Scheduler(VM);
	whlie(1){
		double e = // file next double - VM.getReal()
		char x = // next char
		scheduler.schedule(x, e);	
	}
}
