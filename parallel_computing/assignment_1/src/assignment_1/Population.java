package assignment_1;

import java.util.Arrays;
import java.util.Random;

public class Population {
	Member[] members;
	int size = 0;
	int pop_size;
	double MUTATION_RATE = 0.1;
	int width, height;
	
	Population(int pop_size, int width, int height){
		this.pop_size = pop_size;
		this.width = width;
		this.height = height;
		members = new Member[pop_size];
	}
	
	public void sort(){
		Arrays.sort(members, (a, b) -> (int) a.fitness - (int) b.fitness);
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
		float[] top_scores = {-1, -1, -1, -1, -1};
		int count = 0;
		
		for (int i = 0; i < pop_size; i++){
			if (count == 5){
				break;
			}
			
			if(!contains(this.members[i].fitness, top_scores)){
				top_scores[count] = this.members[i].fitness;
				count++;
				continue;
			}
		}
		
		for (float n : top_scores){
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
		
		/*for (int i = 0; i < count; i++){
			System.out.println("mating_pool["+i+"]: " + mating_pool[i].fitness);
		}*/
		
		for (int i = 0; i < pop_size; i++){
			int rand = r.nextInt(count);
			int rand2 = r.nextInt(count);
			members[i] = combine(mating_pool[rand], mating_pool[rand2], i);
		}
	}
	
	public Member combine(Member one, Member two, int id){
		//System.out.println("Parent one fitness: " + one.fitness + " || Parent two fitness: " + two.fitness);
		Member child;
		int[] tmp_pixels = new int[one.pixels.length];
		
		for (int i = 0; i < one.pixels.length; i++){
			if (i % 2 == 0){
				tmp_pixels[i] = one.pixels[i];
			} else {
				tmp_pixels[i] = two.pixels[i];
			}
		}
		child = new Member(id, tmp_pixels, width, height);
		child.pixels = mutate(child.pixels);
		child.set_fitness();
		//System.out.println("child fitness: " + child.fitness);
		return child;
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
	
	public boolean contains(float value, float[] arr){
		for (float n : arr){
			if (value == n){
				return true;
			}
		}
		return false;
	}
}
