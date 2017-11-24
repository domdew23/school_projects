#ifndef SCHEDULER
#define SCHEDULER
#include "Event.cpp"
#include "Network.cpp"
#include "AtomicModel.cpp"
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
			check_merge();
			return 1;
		}

		Event<T>* pull(){
			sift_up(1, size);
			Event<T>* e = events[size];
			size--;
			sift_down(1);
			return e;
		}

		bool remove(Event<T>* e){
			for (int i = 1; i < size + 1; i++){
				if (events[i] == e){
					sift_up(i, size);
					sift_down(i);
					delete events[i];
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
			for (int i = 1; i < size + 1; i++){
				cout << "Event " << i << ": ";
				events[i]->print();
				cout << endl;
			}
		}

	private:
		int size = 0;
		int DEFAULT_INIT_SIZE = 50;
		vector<Event<T>*> events;

		void check(int k){
			while (k > 1 && greater(k/2, k)){
				sift_up(k, k/2);
				k /=2;
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
					if (peek()->time->get_real() == e->time->get_real() && peek()->obj == e->obj){
						merge(peek(), e);
					}
				}
			}
		}

		void merge(Event<T>* one, Event<T>* two){
			remove(one);
			remove(two);

			string str = "deltaConfluent";
			Event<T>* event = new Event<T>(one->time, str, one->obj, one->q);
			put(event);
		}
};

#endif