/* Component */
public interface AtomicModel<Input, Output>{

	public int lambda();

	/* internal state transition function */
	public void deltaInternal(int q);

	/* external state transition function */
	public void deltaExternal(double e, int q);

	public void deltaConfluent(int q, int output);

	public int timeAdvance();

	public void addInput(Input I);

	public void addOutput(Output O);

	public String name();
}