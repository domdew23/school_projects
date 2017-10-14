#include <iostream>
#include <string>
#include <sstream>
#include <cstring>
#include "VendingMachine.h"
using namespace std;

string to_string(int i)
{
    stringstream ss;
    ss << i;
    return ss.str();
}

void print_output(int* y){
	string output = "";
	int coffee=0, quarters=0, nickels=0, dimes=0, nothing=0;
	for (int i = 0; i < 5; i++){
		//cout << "y["<<i<<"]: " << y[i] << endl;
		switch (i){
			case 0: coffee = y[i]; break; //coffee to output
			case 1: quarters = y[i]; break; //output quarters
			case 2: nickels = y[i]; break; //output nickels
			case 3: dimes = y[i]; break; //output dimes
			case 4: nothing = y[i]; break;//output nothing
			default: cout << "Something went wrong" << endl;
		}
	}
	if (nothing){
		output = "Nothing.\n";
	} else if (coffee){
		string str = to_string(coffee);
		output += (str + " Coffee(s)\n");
	} else {
		string str = to_string(quarters) + " Quarters\n" + to_string(nickels) + " Nickels\n" + to_string(dimes) + " Dimes";
		output += (str + "\n");
	}
	cout << "\n=================\n" << output << "=================\n" << endl;
}


int main(int argc, char** argv){
	cout << "Options: q - Quarter || n - Nickels || d - Dimes || w - Wait || c - Cancel || quit - Quit" << endl;
	int q=5, n=0, d=5;

	VendingMachine* VM = new VendingMachine(q, n, d);
	string input = "";
	while (true){
		cin >> input;
		if (input == "quit"){
			break;
		}
		char* X = new char[input.size()];
		strcpy(X, input.c_str());
		print_output(VM->lambda());
		VM->delta(input.size(), X);
		VM->print_state();
		delete[] X;
	}
	delete VM;
	return 0;
}
