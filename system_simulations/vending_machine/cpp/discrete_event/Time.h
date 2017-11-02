#ifndef TIME_H
#define TIME_H
using namespace std;

class Time {
	public:
		Time(double real, int discrete);
		Time* advance(Time interval);
		double get_real();
		int get_discrete();	
	private:
		double real;
		int discrete;
};
#endif
