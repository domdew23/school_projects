#include <iostream>
#include <string>
#include "VendingMachine.h"
using namespace std;

int main(int argc, char** argv){
	cout << "Options: q - Quarter || n - Nickels || d - Dimes || w - Wait || c - Cancel || quit - Quit" << endl;
	VendingMachine* VM = new VendingMachine(5, 5, 5);
	string input = "";
	while (true){
		cin >> input;
		if (input == "quit"){
			break;
		}
		char* X = new char[input.size()];
		strcpy(X, input.c_str());
		for (int i = 0; i < 4; i++){
			switch (i){
				case 0: VM->lambda()[0]; break; // output num coffees
				case 1: VM->lambda()[1]; break; // num quarters
				case 2: VM->lambda()[2]; break; // num nickels
				case 3: VM->lambda()[3]; break; // num dimes
			}
		}
		VM->delta(input.size(), X);
		VM->print_state();
		delete[] X;
	}
	return 0;
}
