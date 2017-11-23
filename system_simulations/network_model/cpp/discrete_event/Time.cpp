#ifndef TIME
#define TIME

class Time {
	public:
		Time(double real, int discrete){
			this->real = real;
			this->discrete = discrete;
		}

		Time* advance(Time* interval){
			if (interval->real > 0){
				return new Time(real + interval->real, 0);
			} else {
				return new Time(real, discrete + interval->discrete);
			}
		}

		double get_real(){
			return real;
		}

		int get_discrete(){
			return discrete;
		}

	private:
		double real = 0.0;
		int discrete = 0;
};

#endif