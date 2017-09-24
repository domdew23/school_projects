package assignment_1;

import java.util.ArrayList;
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

	
	public void create_mating_pool(){
		Member[] mating_pool = new Member[pop_size];
		Random r = new Random();
		double[] top_scores = {-1, -1, -1, -1,};
		int count = 0;
		
		for (int i = 0; i < pop_size; i++){
			if (count == top_scores.length){
				break;
			}
			
			if(!contains(this.members[i].fitness, top_scores)){
				top_scores[count] = this.members[i].fitness;
				count++;
				continue;
			}
		}
		
		for (double n : top_scores){
			//System.out.println("TOP SCORE: " + n);
		}
		
		count = 0;
		for (int i = 0; i < pop_size; i++){
			//System.out.println("member fitness["+i+"]: " + this.members[i].fitness);
			if (contains(this.members[i].fitness, top_scores)){
				mating_pool[count] = this.members[i];
				count++;
				continue;
			}
		}
		
		for (int i = 0; i < count; i++){
			//System.out.println("mating_pool["+i+"]: " + mating_pool[i].fitness);
		}
		
		for (int i = 0; i < pop_size; i++){
			int rand = r.nextInt(count);
			int rand2 = r.nextInt(count);
			//members[i] = combine(mating_pool[rand], mating_pool[rand2], i);
		}
	}
	
	public Member[] create_new_generation(){
		Random r = new Random();
		ArrayList<Member> needs_to_swap = new ArrayList<Member>();
		ArrayList<Member> stays = new ArrayList<Member>();

		//Member[] new_population = new Member[pop_size];

		for (int i = 0; i < pop_size; i++){
			double odds = (avg_fitness / 100.00);
			//System.out.println("ODDS: " + odds+.6);
			//double new_odds = ((double) members[i].fitness / (double)100);
			double fit = members[i].fitness;
			if ((100 - fit) < 0){
				fit = 100;
			}
			//int odds = (int) Math.abs((Math.round(100 - fit)));
			//System.out.println("odds: " + odds);
			if ((Math.random() <= odds+.095)){
				stays.add(members[i]);
			} else {
				needs_to_swap.add(members[i]);
			}
		}
		//System.out.println("TO SWAP: " + members[i].fitness);
		swap(needs_to_swap);
		stays.addAll(needs_to_swap);
		Member[] new_population = stays.toArray(new Member[stays.size()]);
		for (int i = 0; i < new_population.length; i++){
			new_population[i].reset();
		}
		return new_population;
	}
	
	private void swap(ArrayList<Member> needs_to_swap){
		Random r = new Random();
		for (int i = 0; i < needs_to_swap.size(); i++){
			//System.out.println("Swapping: " + needs_to_swap.get(i).fitness + " || x:" + needs_to_swap.get(i).x + " || y:" + needs_to_swap.get(i).y);
			int one = r.nextInt(needs_to_swap.size());
			int two = r.nextInt(needs_to_swap.size());
			
			while (one == two){
				one = r.nextInt(needs_to_swap.size());
				two = r.nextInt(needs_to_swap.size());
			}
			
			int tmpX =  needs_to_swap.get(one).x;
			int tmpY = needs_to_swap.get(one).y;
			
			needs_to_swap.get(one).x = needs_to_swap.get(two).x;
			needs_to_swap.get(two).x = tmpX;
			
			needs_to_swap.get(one).y = needs_to_swap.get(two).y;
			needs_to_swap.get(two).y = tmpY;
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
