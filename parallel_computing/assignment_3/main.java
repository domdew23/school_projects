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
		Control control = new Control(settings);
	}
}