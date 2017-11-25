import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class main{
	static boolean startRandom = false;
	public static void main(String[] args){
		/* two times wide as high
		parameters: S, T, C1, C2, C3, dimensions, threshold 
		calculaate final temperature for each region of the alloy */
		if (args.length < 1){
			System.out.println("Please supply an input file.");
			return;
		}
		File file;
		Scanner sc;
		try{
			file = new File(args[0]);
			sc = new Scanner(file);
		} catch (FileNotFoundException e){
			System.out.println("File not found.");
			return;
		}

		double C1 = sc.nextDouble();
		double C2 = sc.nextDouble();
		double C3 = sc.nextDouble();
		double S = sc.nextDouble();
		double T = sc.nextDouble();
		int width = sc.nextInt();
		int scale = sc.nextInt();
		int threshold = sc.nextInt(); /* number of cells per leaf */
		int height = width/2;

		Region[][] alloy = init(C1, C2, C3, S, T, width, height);
		Region[][] A = init(C1, C2, C3, S, T, width, height);
		Region[][] B = new Region[width][height];

		Jacobi j = new Jacobi(A, B, 0, height, 0, width, 1, threshold);
		//System.out.println(j.root);
		j.compute();

		//Region r = alloy[0][0];
		//System.out.println(r.getTemp());
		//alloy[0][0].compute();
		//System.out.println(r.getTemp());

		// split up work (quads) each thread gets certain indices to work on
		// threshold - when to stop dividing
		// fork -> compute -> join -> dispaly -> repeat

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame frame = new JFrame("Alloy"); // main window of the application
				GraphicsModule g = new GraphicsModule(width, height, scale, alloy);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(g);
				frame.setSize((width*(2*scale)), (height*(2*scale)));
				frame.setVisible(true);	
			}
		});
	}

	private static Region[][] init(double C1, double C2, double C3, double S, double T, int width, int height){
		Region[][] alloy = new Region[width][height];

		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				Region r = new Region(C1, C2, C3, i, j);
				alloy[i][j] = r;
				r.setTemp(1.0);
			}
		}

		alloy[0][0].setTemp(S);
		alloy[width-1][height-1].setTemp(T);
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				Region left=null,top=null,right=null,bottom=null;
				if (i-1 >= 0){
					left = alloy[i-1][j];
				}
				if (i+1 < width){
					right = alloy[i+1][j];
				}
				if (j-1 >= 0){
					//top
					top = alloy[i][j-1];
				}
				if (j+1 < height){
					bottom = alloy[i][j+1];
				}
				alloy[i][j].setNeighbors(left, top, right, bottom);
				
				if (startRandom){
					alloy[i][j].setTemp(ThreadLocalRandom.current().nextDouble(-320.0, 320.0));
				}
			}
		}
		return alloy;
	}
}