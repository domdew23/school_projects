#include <iostream>
#include <fstream>
#include <string>
#include "Scheduler.cpp"
#include "Event.cpp"
#include "Time.cpp"
using namespace std;


int main(int argc, char** argv){
	if (argc <= 1){
		cout << "Please supply an input file" << endl;
		return 0;
	}
	ifstream file(argv[1]);
	double time = 0;
	int q = 0;
	Scheduler<int>* s = new Scheduler<int>();
	string str = "deltaExternal";
	int x = 5;
	int* i = &x;

	Event<int>* e1 = new Event<int>(new Time(1.0, 0), str, i, i);
	Event<int>* e2 = new Event<int>(new Time(.5, 0), str, i, i);
	Event<int>* e3 = new Event<int>(new Time(2.5, 0), str, i, i);

	s->put(e1);
	s->put(e2);
	s->put(e3);
	s->print();
	file >> time >> q;
	
	return 0;
}