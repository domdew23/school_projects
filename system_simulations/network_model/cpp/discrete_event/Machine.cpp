#ifndef MACHINE
#define MACHINE
#include <string>
#include "AtomicModel.cpp"
#include "Time.cpp"
#include "Scheduler.cpp"
using namespace std;

class Machine : public AtomicModel{
	public:
		Machine(double t, string name){
			this->t = t;
			this->name = name;
			this->s = 0;
			this->p = 0;
			this->last_time = new Time(0.0, 0);
		}

		bool lambda(){
			return 1;
		}

		void delta_internal(){
			p--;
			s = t;
		}

		void delta_external(double e, int q){
			if (p > 0){
				p += q;
				s -= t;
			} else {
				p += q;
				s = t;
			}
		}

		void delta_confluent(int q){
			p += (q + 1);
			s = t;
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
		static Time* current_time;
		static Scheduler<Machine>* scheduler;
};

#endif