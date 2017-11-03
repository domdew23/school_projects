#include <iostream>
#include "Scheduler.h"
#include "VendingMachine.h"
using namespace std;

Scheduler::Scheduler(VendingMachine* VM){
	this->VM = VM;
}

void Scheduler::schedule(char x, double e){
	int ta = VM->time_advance();
	Time* next;
	if (e == 0){
		next = new Time(e, 1);
	}

	if (e > ta){
		next = new Time(ta, 0);
		print_output(VM->lambda());
		VM->delta_internal();
		VM->current = VM->current->advance(next);
		cout << "Executing delta_internal at " << VM->get_real() << endl;
		schedule(x, e-ta);
		return;
	} else if (e == ta){
		next = new Time(ta, 0);
		print_output(VM->lambda());
		cout << "Executing delta_confluent at ";
		VM->delta_confluent(x);
	} else {
		next = new Time(e, 0);
		cout << "Executing delta_external at ";
		VM->delta_external(x);
	}
	
	VM->current = VM->current->advance(next);
	cout << VM->get_real() << endl;
}

void Scheduler::print_output(int* y){
	string output = "";
	int coffee=0, quarters=0, nickels=0, dimes=0;
	for (int i = 0; i < 4; i++){
		switch (i){
			case 0: coffee = y[i]; break; //coffee to output
			case 1: quarters = y[i]; break; //output quarters
			case 2: nickels = y[i]; break; //output nickels
			case 3: dimes = y[i]; break; //output dimes
			default: cout << "Something went wrong" << endl;
		}
	}
	
	if (coffee){
		string str = to_string(coffee);
		output += (str + " Coffee(s)\n");
	} 
	if ((quarters > 0 || nickels > 0) || dimes > 0) {
		string str = to_string(quarters) + " Quarters\n" + to_string(nickels) + " Nickels\n" + to_string(dimes) + " Dimes";
		output += (str + "\n");
	}
	cout << "\n=================\n" << output << "=================\n" << endl;
}

