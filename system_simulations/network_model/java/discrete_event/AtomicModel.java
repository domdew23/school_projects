/* Component */
public interface AtomicModel{

	public boolean lambda();

	/* internal state transition function */
	public void deltaInternal();

	/* external state transition function */
	public void deltaExternal();

	public void deltaConfluent();

	public int timeAdvance();
}