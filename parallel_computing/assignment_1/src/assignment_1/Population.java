package assignment_1;

import java.util.Arrays;
import java.util.Random;

public class Population {
	Member[] members;
	int size = 0;
	int pop_size;
	double avg_fitness = 0.0;
	double MUTATION_RATE = 0.01;
	int width = 8;
	int height = 4;
	Member[][] new_members;

	Population(int pop_size){
		this.pop_size = pop_size;
		members = new Member[pop_size];
		new_members = new Member[width][height];
	}
	
	public void sort(){
		//Arrays.sort(members, (a, b) -> (int) a.fitness - (int) b.fitness);
	}
	
	public void add_member(Member member){
		members[size] = member;
		size++;
	}
	
	public void print_fitnesses(){
		for (int i = 0; i < pop_size; i++){
			for (int j = 0; j < 4; j++){
				System.out.println("members["+i+"]" + " neighbors["+i+"] fitness: " + members[i].fitness);
			}
		}
	}

	public void check_swap(Member mem){
		if (mem.bestX != -1 && mem.bestY != -1){
			return;
		}
		int neighbor_fitness = 0;

		for (int i = 0; i < 4; i++){
			if (mem.neighbors[i] == null){
				neighbor_fitness = 100;
			} else {
				neighbor_fitness = mem.neighbors[i].id;
			}
			System.out.println("member: " + mem.id + "neighbor fit: " + neighbor_fitness + " || neighbors["+i+"] fitness: " + mem.neighbor_ids[i]);
			if (mem.neighbor_ids[i] == neighbor_fitness || neighbor_fitness == 100){
				continue;
			}
			swap(mem, mem.neighbors[i]);
			return;
		}
		System.out.println("HERE -- x: " + mem.x + ", y: " + mem.y);
		
		mem.bestX = mem.x;
		mem.bestY = mem.y;
		
		/*double odds = mem.fitness;
		Random r = new Random();
		//System.out.println("ODDS: " + odds+.1);
		//System.out.println("MATH.RANDOM: " + Math.random());
		
		if (r.nextInt(100) > odds){
			//if (r.nextInt(2) == 1){
				//swap(mem, members[r.nextInt(members.length)]);
			//} else {
				swap(mem, mem.neighbors[index]);
			//}*/
		}
	
	public void reset(){
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				new_members[x][y].reset();
			}
		}
	}
	
	public void swap(Member one, Member two){
			/*if (two == null){
				return;
			}*/

			int one_tmpX =  one.x;
			int one_tmpY = one.y;
			Member tmp_one = new_members[one.x/100][one.y/100];
			
			new_members[one.x/100][one.y/100] = new_members[two.x/100][two.y/100];
			new_members[two.x/100][two.y/100] = tmp_one;
			//int two_tmpX =  two.x;
			//int two_tmpY = two.y;
			
			//double one_tmp_fitness = one.fitness;
			//double two_tmp_fitness = two.fitness;

			one.x = two.x;
			two.x = one_tmpX;
			
			one.y = two.y;
			two.y = one_tmpY;
			
			//one.evaluate_fitness();
			//two.evaluate_fitness();
			/*if (one_tmp_fitness > one.fitness || two_tmp_fitness > two.fitness){
				one.x = one_tmpX; 
				one.y = one_tmpY;
				two.x = two_tmpX;
				two.y = two_tmpY;
				one.fitness = one_tmp_fitness;
				two.fitness = one_tmp_fitness;
			}*/
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
		//System.out.println("Population avg fitness: " + avg_fitness);
	}
	
}
