package assignment_1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class Runner implements Runnable{
	
	public AtomicReference<Population> GLOBAL_POPULATION;
	private final int POPULATION_SIZE = 32;
	private final ThreadLocal<Population> local_population  = new ThreadLocal<Population>();
	private final Population tmp_population;
	private final ReentrantLock lock = new ReentrantLock();

	public Runner(Population local_population, AtomicReference<Population> GLOBAL_POPULATION){
		this.tmp_population = local_population;
		this.GLOBAL_POPULATION = GLOBAL_POPULATION;
	}
	
	public void run(){
		this.local_population.set(tmp_population);
		
		//while(true){
			evaluate_fitness();
			eval();
			update();
			this.local_population.set(GLOBAL_POPULATION.get());
		//}

	}
	
	synchronized void eval(){
		int id = 0;
		int total = 0;
		for (int i = 0; i < POPULATION_SIZE; i++){
			if (has_neighbor(i-1)){
				id = local_population.get().members[i-1].id;
				if (local_population.get().members[i].affinites[id] != 0){
					//left
					total++;
				}

			}
			if (has_neighbor(i-local_population.get().width)){
				id = local_population.get().members[i-local_population.get().width].id;
				if (local_population.get().members[i].affinites[id] != 0){
					//top
					total++;
				}
			}
			if (has_neighbor(i+1)){
				id = local_population.get().members[i+1].id;
				if (local_population.get().members[i].affinites[id] != 0){
					//right
					total++;
				}
			}
			
			if (has_neighbor(i+local_population.get().width)){
				id = local_population.get().members[i+local_population.get().width].id;
				if (local_population.get().members[i].affinites[id] != 0){
					//bottom
					total++;
				}
			}
		}
		
		local_population.get().fitness = (double) total/100;
		System.out.println(local_population.get().fitness);
	}
	
	
	public void evaluate_fitness(){
		Random r = new Random();
		boolean right_swap = r.nextBoolean();
		boolean bottom_swap = r.nextBoolean();
		
		for (int i = 0; i < POPULATION_SIZE; i++){
			if (right_swap && has_neighbor(i+1) && has_neighbor(i+2)){
				int id1 = local_population.get().members[i+1].id;
				int id2 = local_population.get().members[i+2].id;
				
				int fitness = local_population.get().members[i].affinites[id1];
				int fitness2 = local_population.get().members[i].affinites[id2];
				if (fitness > fitness2){
					break;
				}
				swap(local_population.get().members[i+1], local_population.get().members[i+2], i+1, i+2, false);
			} else if (has_neighbor(i-1) && has_neighbor(i-2)){
				int id1 = local_population.get().members[i-1].id;
				int id2 = local_population.get().members[i-2].id;
				
				int fitness = local_population.get().members[i].affinites[id1];
				int fitness2 = local_population.get().members[i].affinites[id2];
				if (fitness > fitness2){
					break;
				}
				swap(local_population.get().members[i-1], local_population.get().members[i-2], i-1, i-2, false);
			}
		}
		
		for (int i = 0; i < POPULATION_SIZE; i++){
			if (bottom_swap && has_neighbor(i+local_population.get().width) && has_neighbor(i+(2*local_population.get().width))){
				int id1 = local_population.get().members[i+local_population.get().width].id;
				int id2 = local_population.get().members[i+(2*local_population.get().width)].id;
				
				int fitness = local_population.get().members[i].affinites[id1];
				int fitness2 = local_population.get().members[i].affinites[id2];
				if (fitness > fitness2){
					break;
				}
				swap(local_population.get().members[i+1], local_population.get().members[i+2],i+local_population.get().width, i+(2*local_population.get().width), false);
			} else if (has_neighbor(i-local_population.get().width) && has_neighbor(i-(2*local_population.get().width))){
				int id1 = local_population.get().members[i-local_population.get().width].id;
				int id2 = local_population.get().members[i-(2*local_population.get().width)].id;
				
				int fitness = local_population.get().members[i].affinites[id1];
				int fitness2 = local_population.get().members[i].affinites[id2];
				if (fitness > fitness2){
					break;
				}
				swap(local_population.get().members[i-local_population.get().width], local_population.get().members[i-(2*local_population.get().width)],i-local_population.get().width,i-(2*local_population.get().width), false);
			}
		}
		mutate();
	}
	
	public boolean has_neighbor (int neighbor_index){
	if (neighbor_index < 0 || neighbor_index >= 32){
		return false;
	}
	return true;
}

	public void mutate(){
		Random r = new Random();
		boolean bool = r.nextBoolean();
		int shift = 0;
		if (bool){
			boolean bool2 = r.nextBoolean();
			if (bool2){
				shift = local_population.get().width;
			} else {
				shift = -1;
			}
		} else {
			boolean bool2 = r.nextBoolean();
			if (bool2){
				shift = -local_population.get().width;
			} else {
				shift = 1;
			}
		}

		if (Math.random() < 0.3){
			for (int i = 0; i < r.nextInt(5); i++){
				int rand1 = r.nextInt(POPULATION_SIZE);
				if (has_neighbor(rand1+shift)){
					swap(local_population.get().members[rand1], local_population.get().members[rand1+shift], rand1, rand1+shift, false);
				}
			}
		}
	}


	public void swap(Member one, Member two, int index1, int index2, boolean is_base){
		if (Math.random() <= local_population.get().fitness){
			return;
		}
		
		int one_tmpX =  one.x;
		int one_tmpY = one.y;
		Member tmp_one = local_population.get().members[index1];
	
		one.x = two.x;
		two.x = one_tmpX;
		
		one.y = two.y;
		two.y = one_tmpY;
		
		local_population.get().members[index1] = local_population.get().members[index2];
		local_population.get().members[index2] = tmp_one;
	}

	private void update(){
		lock.lock();
		try{
			//for (int i = 0; i < 32; i++){
				//System.out.println("member id: " +local_population.get().members[i].id );
			//}
			System.out.println(Thread.currentThread().getName() + " best fitness: " + 
			local_population.get().fitness + " -- global fitness: " + GLOBAL_POPULATION.get().fitness);
			
			if (local_population.get().fitness < GLOBAL_POPULATION.get().fitness){
				
			} else {
				System.out.println("here");
				//GLOBAL_POPULATION.get().fitness = local_population.get().fitness; 
				//GLOBAL_POPULATION.get().members = local_population.get().members;
				if (GLOBAL_POPULATION.compareAndSet(GLOBAL_POPULATION.get(), local_population.get())){
				}
			}
		} finally {
			lock.unlock();
		}
	}
}
