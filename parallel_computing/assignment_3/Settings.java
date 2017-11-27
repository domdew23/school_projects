import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Settings {
	public double C1,C2,C3,S,T;
	public int width,height,scale,threshold;

	public Settings(String fileName){
		File file = null;
		Scanner sc = null;
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
		width = sc.nextInt();
		scale = sc.nextInt();
		height = width/2;
		threshold = (width * height)/4;
	}
}