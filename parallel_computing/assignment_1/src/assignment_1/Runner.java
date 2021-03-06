package assignment_1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Runner implements Runnable{
	
	public AtomicReference<Population> GLOBAL_POPULATION;
	private final int POPULATION_SIZE = 32;
	private final ThreadLocal<Population> local_population  = new ThreadLocal<Population>();
	private final Population tmp_population;
	
	AtomicInteger best_pos = new AtomicInteger();
	public Runner(Population local_population, AtomicReference<Population> GLOBAL_POPULATION, AtomicInteger best_pos){
		this.tmp_population = local_population;
		this.GLOBAL_POPULATION = GLOBAL_POPULATION;
		this.best_pos = best_pos;
	}
	
	public void run(){
			this.local_population.set(tmp_population);
			mutate();
			evaluate_fitness();
			eval();
			update();
	}
	
	public void eval(){
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
			if (has_neighbor(i-local_population.get().WIDTH)){
				id = local_population.get().members[i-local_population.get().WIDTH].id;
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
			
			if (has_neighbor(i+local_population.get().WIDTH)){
				id = local_population.get().members[i+local_population.get().WIDTH].id;
				if (local_population.get().members[i].affinites[id] != 0){
					//bottom
					total++;
				}
			}
		}
		
		local_population.get().fitness = (double) total/100;
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
			if (bottom_swap && has_neighbor(i+local_population.get().WIDTH) && has_neighbor(i+(2*local_population.get().WIDTH))){
				int id1 = local_population.get().members[i+local_population.get().WIDTH].id;
				int id2 = local_population.get().members[i+(2*local_population.get().WIDTH)].id;
				
				int fitness = local_population.get().members[i].affinites[id1];
				int fitness2 = local_population.get().members[i].affinites[id2];
				if (fitness > fitness2){
					break;
				}
				swap(local_population.get().members[i+1], local_population.get().members[i+2],i+local_population.get().WIDTH, i+(2*local_population.get().WIDTH), false);
			} else if (has_neighbor(i-local_population.get().WIDTH) && has_neighbor(i-(2*local_population.get().WIDTH))){
				int id1 = local_population.get().members[i-local_population.get().WIDTH].id;
				int id2 = local_population.get().members[i-(2*local_population.get().WIDTH)].id;
				
				int fitness = local_population.get().members[i].affinites[id1];
				int fitness2 = local_population.get().members[i].affinites[id2];
				if (fitness > fitness2){
					break;
				}
				swap(local_population.get().members[i-local_population.get().WIDTH], local_population.get().members[i-(2*local_population.get().WIDTH)],i-local_population.get().WIDTH,i-(2*local_population.get().WIDTH), false);
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
				shift = local_population.get().WIDTH;
			} else {
				shift = -1;
			}
		} else {
			boolean bool2 = r.nextBoolean();
			if (bool2){
				shift = -local_population.get().WIDTH;
			} else {
				shift = 1;
			}
		}

		int count = r.nextInt(3) * Math.abs(1000 -  (int) (local_population.get().fitness * 1000));
		if (count <= 0) {
			return;
		}
		for (int i = 0; i < r.nextInt(count); i++){
			int rand1 = r.nextInt(POPULATION_SIZE);
			if (has_neighbor(rand1+shift)){
				swap(local_population.get().members[rand1], local_population.get().members[rand1+shift], rand1, rand1+shift, false);
			}
		}
	}

	public void swap(Member one, Member two, int index1, int index2, boolean is_base){
		if (Math.random() <= local_population.get().fitness*1.2){
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
			System.out.println(Thread.currentThread().getName() + " -- " + Thread.currentThread().getId() + " best fitness: " + 
			local_population.get().fitness + " -- global fitness: " + GLOBAL_POPULATION.get().fitness + " || pop id: " +
			local_population.get().id);
			
			if (local_population.get().fitness > GLOBAL_POPULATION.get().fitness) {
				this.best_pos.set(local_population.get().id);
			}
	}
}
