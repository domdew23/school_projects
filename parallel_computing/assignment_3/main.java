public class main{
	public static void main(String[] args){
		/* two times wide as high
		parameters: S, T, C1, C2, C3, dimensions, threshold 
		calculaate final temperature for each region of the alloy */
		if (args.length < 1){
			System.out.println("Please supply an input file.");
			return;
		}
		Settings settings = new Settings(args[0]);
		Control control = new Control();
		GraphicsEngine graphicsEngine = new GraphicsEngine();
		Jacobi jacobi = new Jacobi(control.A, control.B, 0, Settings.HEIGHT, 0, Settings.WIDTH, Settings.MAX_STEPS, Settings.THRESHOLD);

		jacobi.start();
		graphicsEngine.start();
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