/* Leaf */
public class XORModel implements AtomicModel {
	private boolean state;
	private int tick;
	private static int count = 0;
	private int id;

	public XORModel(){
		this.state = false;
		this.tick = 0;
		this.id = ++count;
	}

	public boolean lambda(){
		System.out.println("XOR" + id + " Output: " + ((state) ? 1 : 0));
		return state;
	}

	public void delta(boolean[] X){
		System.out.println("XOR" + id + " Input: " + ((X[0]) ? 1 : 0) + ((X[1]) ? 1 : 0));
		state = (X[0] ^ X[1]);
		System.out.println("XOR" + id + " State: " + ((state) ? 1 : 0) + "\n==================");
		tick++;
	}

	public int getTick(){
		return tick;
	}
}