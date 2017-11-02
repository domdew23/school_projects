#ifndef XORMODEL_H
#define XORMODEL_H
using namespace std;

class XORModel : public AtomicModel{
	public:
		XORModel(int id);
		bool lambda();
		void delta(bool x1, bool x2);
	private:
		bool state;
		int tick;
		int id;
};

#endif