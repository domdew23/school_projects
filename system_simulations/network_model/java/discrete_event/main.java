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
		- outpput from the press goes directly to the input of the drill
		- input to the network is coupled with input to the press
		- output from the drill is coupled with the output from the network 
		state:
		p - parts for the machine process
		s - time remaining to process the first of those parts0.

		*/

		AtomicModel press = new Machine(1);
		AtomicModel drill = new Machine(2);
		
		Scheduler scheduler = new Scheduler(10);
		for (int i = 0; i < 30; i++){
			Time t = new Time(Math.random() * 100, 0);
			Event e = new Event(t, "", "");
			scheduler.put(e);
		}
		System.out.println(scheduler.toString());
		System.out.println(scheduler.pull().toString() + "\n");
		System.out.println(scheduler.toString());

	}
}