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

void execute(Event<AtomicModel>* event, AtomicModel* model, double e){
	if (event->obj == model){
		if (model->last_time->get_real() == 0){
			event->update_e(0.0);
		} else {
			event->update_e(AtomicModel::current_time->get_real() - model->last_time->get_real());
		}

		if (event->kind == "delta_external"){
			model->delta_external(event->e, event->q);
		} else if (event->kind == "delta_internal"){
			model->lambda();
			model->delta_internal();
		} else if (event->kind == "delta_confluent"){
			model->lambda();
			model->delta_confluent(event->q);
		}

		cout << "Executing: ";
		event->print();
		cout << endl;
		delete event;
	}
}


int main(int argc, char** argv){
	if (argc <= 1){
		cout << "Please supply an input file" << endl;
		return 0;
	}

	Scheduler<AtomicModel>* scheduler = new Scheduler<AtomicModel>();
	
	Machine<Network<AtomicModel>,AtomicModel>* ptr = new Machine<Network<AtomicModel>, AtomicModel>(1.0, "Press", scheduler);
	Machine<AtomicModel,Network<AtomicModel>>* ptr2 = new Machine<AtomicModel,Network<AtomicModel>>(2.0, "Drill", scheduler);
	AtomicModel* press = ptr;
	AtomicModel* drill = ptr2;
	Network<AtomicModel>* network = Network<AtomicModel>::NetworkBuilder().add_component(press).add_component(drill).add_input(press).add_output(drill).build();

	ptr->add_input(network);
	ptr->add_output(drill);

	ptr2->add_input(press);
	ptr2->add_output(network);


	ifstream file(argv[1]);
	double time = 0;
	int q = 0;
	double last_time = 0.0;
	string ext = "delta_external";
	
	while(1){
		file >> time >> q;
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

	while(!(scheduler->is_empty())){
		cout << endl << "Global time: " << AtomicModel::current_time->get_real() << endl;
		Event<AtomicModel>* event = scheduler->pull();
		double e = event->time->get_real() - AtomicModel::current_time->get_real();

		Time* interval = new Time(e, 1);
		AtomicModel::current_time = AtomicModel::current_time->advance(interval);

		for (AtomicModel* model : network->get_components()){
			execute(event, model, e);
		}
		cout << "---------------------------" << endl;
		scheduler->print();
	}
	
	return 0;
}