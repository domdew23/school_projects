package assignment_1;

import java.util.Arrays;
import java.util.Random;

public class Population {
	int size = 0;
	int pop_size;
	double fitness = 0.0;
	double MUTATION_RATE = 0.01;
	int width = 8;
	int height = 4;
	Member[] members;
	int id;

	Population(int pop_size){
		this.pop_size = pop_size;
		members = new Member[pop_size];
		this.fitness = 0.0;
	}

	/*public void check_swap(Member mem){
		if (mem.bestX != -1 || mem.bestY != -1){
			return;
		}
		
		int neighbor_fitness = 0;
		int count = 0;
		for (int i = 0; i < 4; i++){
			neighbor_fitness = mem.new_neighbor_ids[i];
			System.out.println("member: " + mem.id + "neighbor fit: " + neighbor_fitness + " || expected: " + mem.neighbor_ids[i]);
			
			if (mem.neighbor_ids[i] == neighbor_fitness){
				if (neighbor_fitness == -9999){
					continue;
				}
				mem.neighbors[i] = get(neighbor_fitness);
				mem.new_neighbor_ids[i] = neighbor_fitness;
				count++;
			}
		}
				
		if (count == 4){
			//try { Thread.sleep(4000); } catch (InterruptedException e){}
			mem.bestX = mem.x;
			mem.bestY = mem.y;
			for (int i = 0; i < 4; i++){
				mem.neighbors[i].bestX = mem.neighbors[i].x;
				mem.neighbors[i].bestX = mem.neighbors[i].y;
			}
			return;
		}
		
		Random r = new Random();
		double odds = (double) count/4 * ((r.nextDouble() *2) + 1);
		System.out.println("count: " + count + " -- odds: " + odds);
		if (!(Math.random() <= odds)){
			swap(mem, mem.neighbors[r.nextInt(4)]);
		}
		
		if (Math.random() <= .2){
			int rand1 = r.nextInt(4);
			int rand2 = r.nextInt(4);
			try {
				swap(mem.neighbors[rand1], mem.neighbors[rand1].neighbors[rand2]);
			} catch (Exception e){
				return;
			}
		} 
		return;
	}*/
	
	public int[] mutate(int[] pixels){
		for (int i = 0; i < pixels.length; i++){
			Random r = new Random();
			if (Math.random() <= MUTATION_RATE){
				int rand = r.nextInt(pixels.length);
				int temp = pixels[i];
				pixels[i] = pixels[rand];
				pixels[rand] = temp;
			}
		}
		return pixels;
	}
	
	public boolean contains(double value, double[] arr){
		for (double n : arr){
			if (value == n){
				return true;
			}
		}
		return false;
	}
	
	/*public void set_avg_fitness(){
		int total = 0;
		for (Member m : members){
			total += m.fitness;
		}
		this.avg_fitness = (double) total / (double) members.length;
		//System.out.println("Population avg fitness: " + avg_fitness);
	}*/
	
}
