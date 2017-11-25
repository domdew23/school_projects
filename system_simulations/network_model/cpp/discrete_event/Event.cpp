#ifndef EVENT
#define EVENT
#include <string>
#include <iostream>
#include "Time.cpp"
using namespace std;

template<class T>
class Event {
	public:
		Time* time;
		string kind;
		T* obj;
		int q;
		double e;

		Event(Time* time, string kind, T* obj, double e, int q){
			this->time = time;
			this->kind = kind;
			this->obj = obj;
			this->e = e;
			this->q = q;
		}

		Event(Time* time, string kind, T* obj, int q){
			this->time = time;
			this->kind = kind;
			this->obj = obj;
			this->q = q;
			this->e = 0;
		}

		Event(Time* time, string kind, T* obj){
			this->time = time;
			this->kind = kind;
			this->obj = obj;
			this->q = 0;
			this->e = 0;
		}

		void update_e(double e){
			this->e = e;
		}

		void print(){
			cout << "Time: " << time->get_real() << " | Kind: " << kind << " | Object: " << obj->get_name() << " | e: " << e << " | q: " << q;
		}
};

#endif