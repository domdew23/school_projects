/* Leaf */
public class MemoryModel implements AtomicModel{
	private boolean[] state = new boolean[2];
	private int tick;

	public MemoryModel(){
		this.state[0] = false;
		this.state[1] = false;
		this.tick = 0;
	}

	public boolean lambda(){
<<<<<<< HEAD
		System.out.println("M Output: " + ((state[0] ^ state[1]) ? 1 : 0));
		return (state[0] ^ state[1]);
=======
		return state[0];
>>>>>>> 5893852dccfc81e4b659240b7811415b9bbcc40e
	}

	public void delta(boolean[] X){
		System.out.println("M Input: " + ((X[0]) ? 1 : 0) + ((X[1]) ? 1 : 0));
		state[0] = state[1];
<<<<<<< HEAD
		state[1] = x2;
=======
		state[1] = X[1];
>>>>>>> 5893852dccfc81e4b659240b7811415b9bbcc40e
		System.out.println("M New State: " + ((state[0]) ? 1 : 0) + " || " + ((state[1]) ? 1 : 0) + "\n==================");
		tick++;
	}

	public int getTick(){
		return this.tick;
	}
}