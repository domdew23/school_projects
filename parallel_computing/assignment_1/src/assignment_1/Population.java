package assignment_1;

import java.util.Arrays;
import java.util.Random;

public class Population {
	Member[] members;
	int size = 0;
	int pop_size;
	double avg_fitness = 0.0;
	double MUTATION_RATE = 0.01;
	
	Population(int pop_size){
		this.pop_size = pop_size;
		members = new Member[pop_size];
	}
	
	public void sort(){
		Arrays.sort(members, (a, b) -> (int) b.fitness - (int) a.fitness);
	}
	
	public void add_member(Member member){
		members[size] = member;
		size++;
	}
	
	public Member get_best_member(){
		return members[0];
	}
	
	public void print_fitnesses(){
		for (int i = 0; i < pop_size; i++){
			System.out.println("members["+i+"]" + " fitness: " + members[i].fitness);
		}
	}

	public void check_swap(Member mem){
		double worst_fitness = 0.0;
		int index = 0;
		for (int i = 0; i < mem.neighbor_fitnesses.length; i++){
			if (mem.neighbor_fitnesses[i] > worst_fitness && mem.neighbor_fitnesses[i] != -1.0){
				worst_fitness = mem.neighbor_fitnesses[i];
				index = i;
			}
		}
		double odds = mem.fitness;
		//System.out.println("ODDS: " + odds+.1);
		if (Math.random() > odds){
			swap(mem, mem.neighbors[index]);
		}
		
	}
		
	private void swap(Member one, Member two){
			if (two == null){
				return;
			}
			
			int one_tmpX =  one.x;
			int one_tmpY = one.y;
			int two_tmpX =  two.x;
			int two_tmpY = two.y;
			
			double one_tmp_fitness = one.fitness;
			double two_tmp_fitness = two.fitness;

			one.x = two.x;
			two.x = one_tmpX;
			
			one.y = two.y;
			two.y = one_tmpY;
			
			one.evaluate_fitness();
			two.evaluate_fitness();
			if (one_tmp_fitness > one.fitness || two_tmp_fitness > two.fitness){
				one.x = one_tmpX;
				one.y = one_tmpY;
				two.x = two_tmpX;
				two.y = two_tmpY;
			}
		}
	
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
	
	public void set_avg_fitness(){
		int total = 0;
		for (Member m : members){
			total += m.fitness;
		}
		this.avg_fitness = (double) total / (double) members.length;
		System.out.println("Population avg fitness: " + avg_fitness);
	}
	
}
