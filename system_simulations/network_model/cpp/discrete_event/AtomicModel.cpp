#ifndef ATOMICMODEL
#define ATOMICMODEL
using namespace std;

class AtomicModel {
	public:
		virtual bool lambda() = 0;
		virtual void delta_internal() = 0;
		virtual void delta_external() = 0;
		virtual void delta_confluent() = 0;
		virtual double time_advance() = 0;
};

#endif