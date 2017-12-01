import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Settings {
	public static double C1, C2, C3, S, T;
	public static int WIDTH, HEIGHT, SCALE, THRESHOLD, MAX_STEPS;
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
		HEIGHT = WIDTH/2;
		THRESHOLD = (WIDTH * HEIGHT)/4;
		MAX_STEPS = 1000;
		RUNNING = true;
	}
}