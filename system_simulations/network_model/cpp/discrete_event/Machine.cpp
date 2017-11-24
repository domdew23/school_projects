#ifndef MACHINE
#define MACHINE
#include <string>
#include <vector>
#include <typeinfo>
#include "Network.cpp"
#include "AtomicModel.cpp"
#include "Time.cpp"
#include "Scheduler.cpp"
#include "Event.cpp"
using namespace std;

template<class I, class O>
class Machine : public AtomicModel {
	public:
		Machine(double t, string name, Scheduler<AtomicModel>* scheduler, I* input, O* output){
			this->t = t;
			this->name = name;
			this->s = 0;
			this->p = 0;
			this->last_time = new Time(0.0, 0);
			this->scheduler = scheduler;
			cout << "hi1" << endl;
			inputs.push_back(input);
			outputs.push_back(output);
			cout << "hi2" << endl;
		}

		int lambda(){
			return 1;
		}

		void delta_internal(){
			last_time = current_time;
			p--;
			s = t;

			for (O* output : outputs){
				if (dynamic_cast<Network<AtomicModel>*>(output) != nullptr){
					cout << "Network output: " << (++count) << endl;
				} else {
					double x = current_time->get_real();
					Event<O>* event = new Event<O>(new Time(x, 0), EXTERNAL, output, lambda());
					scheduler->put((Event<AtomicModel>*) event);
				}
			}

			if (p > 0){
				double x = current_time->get_real() + time_advance();
				Event<AtomicModel>* event = new Event<AtomicModel>(new Time(x,0), INTERNAL, this);
				scheduler->put(event);
			}
		}

		void delta_external(double e, int q){
			last_time = current_time;
			
			Event<AtomicModel>* event;
			if (p > 0){
				p += q;
				s -= t;
				if (scheduler->remove(scheduler->find(INTERNAL, this))){
					double x = current_time->get_real() + time_advance();
					event = new Event<AtomicModel>(new Time(x,0), INTERNAL, this);
				}
			} else {
				p += q;
				s = t;
				double x = current_time->get_real() + t;
				event = new Event<AtomicModel>(new Time(x,0), INTERNAL, this);
			}
			scheduler->put(event);
		}

		void delta_confluent(int q){
			last_time = current_time;
			p += (q + 1);
			s = t;

			double x = current_time->get_real() + time_advance();
			Event<AtomicModel>* output_event = new Event<AtomicModel>(new Time(x,0), INTERNAL, this);
			scheduler->put(output_event);

			for (O* output : outputs){
				if (dynamic_cast<Network<AtomicModel>*>(output) != nullptr){
					cout << "Network output: " << (++count) << endl;
				} else {
					double x = current_time->get_real();
					Event<O>* event = new Event<O>(new Time(x,0), EXTERNAL, output, lambda());
					scheduler->put((Event<AtomicModel>*) event);
				}
			}
		}

		double time_advance(){
			if (p > 0){
				return s;
			} else {
				return 9999999.99999;
			}
		}

	private:
		string name;
		double t;
		double s;
		int p;
		Time* last_time;
		int count = 0;
		vector<I*> inputs;
		vector<O*> outputs;
		string INTERNAL = "delta_internal";
		string EXTERNAL = "delta_external";
		Scheduler<AtomicModel>* scheduler;
};

#endif