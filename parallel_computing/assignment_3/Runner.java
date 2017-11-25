public class Runner implements Runnable {
	Tree section;
	public Runner(Tree section){
		this.section = section;
	}

	public void run(){
		section.compute();
	}
}