import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Settings {
	public static double C1=0, C2=0, C3=0, S=0, T=0, MAX=0;
	public static int WIDTH=4, HEIGHT, SCALE=2, THRESHOLD=4, MAX_STEPS, THRESVAL, CLIENTS=1, CHUNK_SIZE;
	public static double[] METALS = new double[3];
	public static boolean RUNNING;
	private File file = null;
	private Scanner sc = null;
	private String fileName;
	
	public Settings(String fileName){
		this.fileName = fileName;
		try{
			file = new File(fileName);
			sc = new Scanner(file);
		} catch (FileNotFoundException e){
			System.out.println("File not found.");
			System.exit(0);
		}
		
		C1 = sc.nextDouble();
		C2 = sc.nextDouble();
		C3 = sc.nextDouble();
		S = sc.nextDouble();
		T = sc.nextDouble();
		WIDTH = sc.nextInt();
		SCALE = sc.nextInt();
		THRESVAL = sc.nextInt();
		finish();
	}

	public Settings(){
		sc = new Scanner(System.in);
		
		System.out.println("C1:");
		C1 = sc.nextDouble();
		System.out.println("C2:");
		C2 = sc.nextDouble();
		System.out.println("C3:");
		C3 = sc.nextDouble();
		System.out.println("S:");
		S = sc.nextDouble();
		System.out.println("T:");
		T = sc.nextDouble();
		System.out.println("Width:");
		WIDTH = sc.nextInt();
		System.out.println("Scale:");
		SCALE = sc.nextInt();
		System.out.println("Threshold:");
		THRESVAL = sc.nextInt();
		finish();
	}

	private void finish(){
		//S = Double.MAX_VALUE/2;
		//T = Double.MAX_VALUE/2;
		METALS[0] = C1;
		METALS[1] = C2;
		METALS[2] = C3;
		HEIGHT = WIDTH/2;
		THRESHOLD = (WIDTH * HEIGHT)/THRESVAL;
		MAX_STEPS = Integer.MAX_VALUE;
		MAX = Math.max(S,T);
		CLIENTS = 3;
		CHUNK_SIZE = HEIGHT/CLIENTS;
		RUNNING = true;	
	}
}