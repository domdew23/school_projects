/* Component */
import java.util.ArrayList;
import java.math.BigDecimal;

public interface AtomicModel<I,O>{

	public boolean lambda();

	public void deltaExternal(boolean x);
	
	public void deltaInternal();	

	public void deltaConfluent(boolean x);

	public BigDecimal timeAdvance();

	public void addInput(I input);

	public void addOutput(O output);

	public ArrayList<I> getInputs();
}