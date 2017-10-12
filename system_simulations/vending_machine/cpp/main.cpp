#include <iostream>
#include <string>
using namespace std;

int main(int argc, char** argv){
	string input = "";
	cout << "Options: q - Quarter || n - Nickels || d - Dimes || w - Wait || c - Cancel || quit - Quit" << endl;
	cin >> input;
	cout << endl << input << endl;
	return 0;
}