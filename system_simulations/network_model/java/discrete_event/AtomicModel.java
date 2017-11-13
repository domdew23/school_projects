/* Component */
import java.math.BigDecimal;

public interface AtomicModel<Input, Output>{

	public int lambda();

	/* internal state transition function */
	public void deltaInternal();

	/* external state transition function */
	public void deltaExternal(BigDecimal e, int q);

	public void deltaConfluent(int q);

	public BigDecimal timeAdvance();

	public void addInput(Input I);

	public void addOutput(Output O);

	public void log(Event e);

	public Event getLast();

	public String name();
}