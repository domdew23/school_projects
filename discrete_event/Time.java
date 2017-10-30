public class Time {
	private double real;
	private int discrete;

	public Time(double real, int discrete){
		this.real = real;
		this.discrete = discrete;
	}

	public Time advance(Time interval){
		if (interval.real > 0){
			return new Time(real + interval.real, 0); // go to the right
		} else {
			return new Time(real, discrete + interval.discrete); // go up
		}
	}

	public double getReal(){
		return real;
	}

	public int getDiscrete(){
		return discrete;
	}
}