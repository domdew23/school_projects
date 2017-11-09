/* Component */
public interface AtomicModel{

	public int lambda();

	/* internal state transition function */
	public void deltaInternal();

	/* external state transition function */
	public void deltaExternal(int e, int q);

	public void deltaConfluent(int q);

	public int timeAdvance();
}