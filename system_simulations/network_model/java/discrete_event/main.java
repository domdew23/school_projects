import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class main {
	public static void main(String[] args){
		/* Network consists of two machines for working metal
		 Press:
		- flattens small metal balls into disks
		- has bin to hold balls that are waiting to be pressed\
		- flattens ball in one second
		Drill:
		- puts hole in the center of each disk
		- has bin to hold disks waiting to be drilled
		- needs two seconds to do job
		Machines are connected sequentially:
		- output from the press goes directly to the input of the drill
		- input to the network is coupled with input to the press
		- output from the drill is coupled with the output from the network 
		state:
		p - parts for the machine process
		s - time remaining to process the first of those parts.

		*/

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

		AtomicModel press = new Machine(1, "Press");
		AtomicModel drill = new Machine(2, "Drill");
		Machine.setScheduler(new Scheduler());
		Machine.setTime(new Time(0, 0));

		Network network = Network.builder().addComponent(press).addComponent(drill).build();

		// file only contains input events
		while (sc.hasNext()){
			double time = sc.nextDouble();
			int q = sc.nextInt();
		}
		
		/*
		System.out.println(scheduler.toString());
		System.out.println(scheduler.pull().toString() + "\n");
		System.out.println(scheduler.toString());
		*/
	}
}