#include <iostream>
#include <string>
#include "VendingMachine.h"
using namespace std;

int main(int argc, char** argv){
	string input = "";
	cout << "Options: q - Quarter || n - Nickels || d - Dimes || w - Wait || c - Cancel || quit - Quit" << endl;
	cin >> input;
	cout << endl << input << endl;
	VendingMachine* VM = new VendingMachine(5, 5, 5);
	return 0;
}
