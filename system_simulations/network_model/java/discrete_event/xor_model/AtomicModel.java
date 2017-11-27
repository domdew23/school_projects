/* Component */
import java.util.ArrayList;

public interface AtomicModel<I,O>{

	public boolean lambda();

	public void deltaExternal(boolean[]  X);
	
	public void deltaInternal();	

	public void deltaConfluent(boolean[] X);

	public int timeAdvance();

	public void addInput(I input);

	public void addOutput(O output);

	public ArrayList<I> getInputs();
}