#ifndef ATOMICMODEL_H
#define ATOMICMODEL_H
using namespace std;

class AtomicModel{
	public:
		virtual bool lambda() = 0;
		virtual void delta(bool x1, bool x2) = 0;
};

#endif