/* Component */
import java.math.BigDecimal;

public interface AtomicModel<I,O>{

	public int lambda();

	/* internal state transition function */
	public void deltaInternal();

	/* external state transition function */
	public void deltaExternal(BigDecimal e, int q);

	public void deltaConfluent(int q);

	public BigDecimal timeAdvance();

	public void addInput(I I);

	public void addOutput(O O);

	public void log(Event e);

	public Time getLast();

	public String name();
}