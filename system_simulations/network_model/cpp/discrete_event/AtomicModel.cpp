#ifndef ATOMICMODEL
#define ATOMICMODEL
#include "Time.cpp"
#include "Scheduler.cpp"
using namespace std;

//template<class I, class O>
class AtomicModel {
	public:
		virtual int lambda() = 0;
		virtual void delta_internal() = 0;
		virtual void delta_external(double e, int q) = 0;
		virtual void delta_confluent(int q) = 0;
		virtual double time_advance() = 0;
		static void set_time(Time* time);
		//virtual void add_input(I* input) = 0;
		//virtual void add_output(O* output) = 0;
		static Time* current_time;
};

#endif