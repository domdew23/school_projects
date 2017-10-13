#include <iostream>
#include <string>
#include <cstring>
#include "VendingMachine.h"
using namespace std;

int main(int argc, char** argv){
	cout << "Options: q - Quarter || n - Nickels || d - Dimes || w - Wait || c - Cancel || quit - Quit" << endl;
	cout << argc << endl;
	VendingMachine* VM = new VendingMachine(5, 5, 5);
	string input = "";
	while (true){
		cin >> input;
		if (input == "quit"){
			break;
		}
		char* X = new char[input.size()];
		strcpy(X, input.c_str());
		VM->lambda();
		VM->delta(input.size(), X);
		VM->print_state();
		delete[] X;
	}
	return 0;
}
