#include <iostream>
#include "Time.h"
using namespace std;

Time::Time(double real, int discrete){
	this->real = real;
	this->discrete = discrete;
}

Time* Time::advance(Time interval){
	if (interval.real > 0){
		return new Time(real + interval.real, 0); // go right
	} else {
		return new Time(real, discrete + interval.discrete);
	}
}

double Time::get_real(){
	return real;
}

int Time::get_discrete(){
	return discrete;
}
