#ifndef ATOMICMODEL
#define ATOMICMODEL
#include "Time.cpp"
#include "Scheduler.cpp"
#include <string>
using namespace std;

class AtomicModel {
	public:
		virtual int lambda() = 0;
		virtual void delta_internal() = 0;
		virtual void delta_external(double e, int q) = 0;
		virtual void delta_confluent(int q) = 0;
		virtual double time_advance() = 0;
		virtual string get_name() = 0;
		static Time* current_time;
		Time* last_time;
};

#endif