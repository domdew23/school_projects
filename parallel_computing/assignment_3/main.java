public class main{
	public static void main(String[] args){
		Settings settings;
		if (args.length < 1){
			settings = new Settings();
		} else if (args.length == 1){
			settings = new Settings(args[0]);
		} else {
			System.out.println("Unexpected amount of arguments.");
			return;
		}
		Control control = new Control();
		GraphicsEngine graphicsEngine = new GraphicsEngine(control);
		Jacobi jacobi = new Jacobi(control.A, control.B, 0, Settings.HEIGHT, 0, Settings.WIDTH, Settings.MAX_STEPS, Settings.THRESHOLD);

		graphicsEngine.start();
		jacobi.invoke();
		//run(jacobi);
	}

	private static void run(Jacobi jacobi){
		double time = System.currentTimeMillis();
		while (true){
			if (time % 1000 == 0){
				System.out.println("Maxdiff: " + jacobi.root.maxDiff);
				//System.out.println(Merger.getUpdatedAlloy()[2][2]);
			}
			time = System.currentTimeMillis();	
		}
	}
}