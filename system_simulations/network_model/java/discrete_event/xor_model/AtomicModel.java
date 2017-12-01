/* Component */
import java.util.ArrayList;

public interface AtomicModel<I,O>{

	public boolean lambda();

	public void deltaExternal(boolean x);
	
	public void deltaInternal();	

	public void deltaConfluent(boolean x);

	public int timeAdvance();

	public void addInput(I input);

	public void addOutput(O output);

	public ArrayList<I> getInputs();
}