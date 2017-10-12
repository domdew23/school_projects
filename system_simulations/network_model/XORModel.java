/* Leaf */
public class XORModel implements AtomicModel {
	private boolean x1, x2;
	private boolean state;
	private int tick;
	private static int count = 0;
	private int id;

	public XORModel(){
		this.x1 = false;
		this.x2 = false;
		this.state = false;
		this.tick = 0;
		this.id = ++count;
	}

	public boolean lambda(){
		return state;
	}

	public void delta(boolean x1, boolean x2){
		System.out.println("XOR" + id + " Input: " + ((x1) ? 1 : 0) + ((x2) ? 1 : 0));
		state = (x1 ^ x2);
		System.out.println("XOR" + id + " State: " + ((state) ? 1 : 0) + "\n==================");
		tick++;
	}

	public int getTick(){
		return tick;
	}
}