#ifndef MEMORYMODEL_H
#define MEMORYMODEL_H
using namespace std;

class MemoryModel : public AtomicModel{
	public:
		MemoryModel();
		bool lambda();
		void delta(bool x1, bool x2);
	private:
		bool state[2];
		int tick;
};

#endif