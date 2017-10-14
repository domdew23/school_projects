/* Leaf */
public class MemoryModel implements AtomicModel{
	private boolean x1, x2;
	private boolean[] state = new boolean[2];
	private int tick;

	public MemoryModel(){
		this.x1 = false;
		this.x2 = false;
		this.state[0] = false;
		this.state[1] = false;
		this.tick = 0;
	}

	public boolean lambda(){
		System.out.println("M Output: " + ((state[0] ^ state[1]) ? 1 : 0));
		return (state[0] ^ state[1]);
	}

	public void delta(boolean x1, boolean x2){
		System.out.println("M Input: " + ((x1) ? 1 : 0) + ((x2) ? 1 : 0));
		state[0] = state[1];
		state[1] = x2;
		System.out.println("M New State: " + ((state[0]) ? 1 : 0) + " || " + ((state[1]) ? 1 : 0) + "\n==================");
		tick++;
	}

	public int getTick(){
		return this.tick;
	}
}