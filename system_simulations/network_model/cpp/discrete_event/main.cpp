#include <iostream>
#include <fstream>
#include <string>
#include "Network.cpp"
#include "Machine.cpp"
#include "Scheduler.cpp"
#include "Event.cpp"
#include "Time.cpp"
#include "AtomicModel.cpp"
using namespace std;

Time* AtomicModel::current_time = new Time(0.0,0);

int main(int argc, char** argv){
	if (argc <= 1){
		cout << "Please supply an input file" << endl;
		return 0;
	}

	Scheduler<AtomicModel>* scheduler = new Scheduler<AtomicModel>();

	AtomicModel* press = NULL;
	AtomicModel* drill = NULL;
	Network<AtomicModel>* network = NULL;

	press = new Machine<Network<AtomicModel>, AtomicModel>(1.0, "Press", scheduler, network, drill);
	drill = new Machine<AtomicModel,Network<AtomicModel>>(2.0, "Drill", scheduler, press, network);

	network = Network<AtomicModel>::NetworkBuilder().add_component(press).add_component(drill).add_input(press).add_output(drill).build();
	cout << "hi down here" << endl;

	ifstream file(argv[1]);
	double time = 0;
	int q = 0;
	double last_time = 0.0;
	string ext = "delta_external";
	
	while(1){
		file >> time >> q;
		cout << time << " | " << q << endl;
		if (time == -1.0){
			break;
		}
		double e = time - last_time;
		last_time = time;

		for (AtomicModel* model : network->get_inputs()){
			Event<AtomicModel>* event = new Event<AtomicModel>(new Time(time, 0), ext, model, e, q);
			scheduler->put(event);
		}
	}

	scheduler->print();
	
	return 0;
}