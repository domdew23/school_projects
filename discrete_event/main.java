import java.util.Scanner;
import java.io.File;

public class main {
	public static void main(String[] args){
		try {
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);
			VendingMachine VM = new VendingMachine(5, 5, 5);
			while (sc.hasNext()){
				double e = sc.nextDouble() - VM.getReal();
				String x = sc.next();
				VM.run(x.charAt(0), e);
				VM.printState();
			}
		} catch (Exception e){
			//e.printStackTrace();
			System.out.println("Please supply a valid input file.");
			System.exit(0);
		}
	}
}
