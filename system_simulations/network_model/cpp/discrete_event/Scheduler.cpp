#ifndef SCHEDULER
#define SCHEDULER
#include "Event.cpp"
#include "Network.cpp"
#include "AtomicModel.cpp"
#include <cmath>
#include <iostream>
#include <vector>
#include <string>
using namespace std;

template <class T>
class Scheduler {
	public:
		Scheduler(){
			events.push_back(NULL);
			size = 0;
		}

		bool put(Event<T>* e){
			events.push_back(e);
			size++;
			check(size);
			return 1;
		}

		Event<T>* pull(){
			check_merge();
			sift_up(1, size);
			Event<T>* e = events[size--];
			sift_down(1);
			events.pop_back();
			return e;
		}

		bool remove(Event<T>* e){
			if (e == NULL){
				return 0;
			}

			for (int i = 1; i < size + 1; i++){
				if (events[i] == e){
					sift_up(i, size);
					events.pop_back();
					delete events[size--];
					sift_down(i);
					return 1;
				}
			}
			return 0;
		}

		Event<T>* find(string kind, T* obj){
			for (Event<T>* e : events){
				if (e != NULL && e->kind == kind && e->obj == obj){
					return e;
				}
			}
			return NULL;
		}

		bool contains(Event<T>* e){
			if (is_empty()){
				return 0;
			}

			for (Event<T>* tmp : events){
				if (tmp == e){
					return 1;
				}
			}
			return 0;
		}

		Event<T>* peek(){
			if (!is_empty()){
				return events[1];
			}
			return NULL;
		}

		int get_size(){
			return size;
		}

		bool is_empty(){
			return (size == 0);
		}

		void print(){
			int i = 0;
			for (Event<T>* e : events){
				if (e != NULL && e != nullptr){
					cout << "Event " << (++i) << ": ";
					e->print();
					cout << endl;
				}
			}
		}

	private:
		int size = 0;
		int DEFAULT_INIT_SIZE = 50;
		double EPSLION = 0.001;
		vector<Event<T>*> events;

		void check(int k){
			while (k > 1 && greater(k/2, k)){
				sift_up(k, k/2);
				k /= 2;
			}
		}

		bool greater(int i, int j){
			return (events[i]->time->get_real() > events[j]->time->get_real());
		}

		void sift_up(int i, int j){
			Event<T>* tmp = events[i];
			events[i] = events[j];
			events[j] = tmp;
		}

		void sift_down(int k){
			while(2 * k <= size){
				int j = 2 * k;
				if (j < size && greater(j, j + 1)){
					j++;
				} 

				if (!(greater(k, j))){
					break;
				}

				sift_up(k, j);
				k = j;
			}
		}

		void check_merge(){
			for (Event<T>* e : events){
				if (e != NULL && e != peek()){
					double diff = peek()->time->get_real() - e->time->get_real();
					if (abs(diff) < EPSLION && peek()->obj == e->obj){
						merge(peek(), e);
						return;
					}
				}
			}
		}

		void merge(Event<T>* one, Event<T>* two){
			Time* time = one->time;
			T* obj = one->obj;
			remove(one);
			remove(two);

			string str = "delta_confluent";
			Event<T>* event = new Event<T>(time, str, obj, 1);
			put(event);
		}
};

#endif