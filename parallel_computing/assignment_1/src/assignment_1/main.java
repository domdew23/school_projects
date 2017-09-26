package assignment_1;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.*;

public class main extends PApplet {
	ArrayList<PImage> pics = new ArrayList<PImage>();
	Population main_population;
	final int POPULATION_SIZE = 32;
	final int THREAD_COUNT = 64;
	int generation = 0;
	Population population = new Population(POPULATION_SIZE);
	
	

	public static void main(String[] args) {
		PApplet.main("assignment_1.main");		
	}
	
	public void settings(){
		size(800, 400);
	}
	
	public void setup(){
		for (int i = 1; i <= 32; i++){
			PImage pic = loadImage("/images/pic_"+i+".jpg");
			pics.add(pic);
		}
		
		background(255);
		init_population(true); // perfect solution	
		shuffle();
	}
	
	public void create_threads(){
		Thread[] threads = new Thread[THREAD_COUNT];
		Runner[] runners = new Runner[THREAD_COUNT];
		
		for (int i = 0; i < THREAD_COUNT; i++){
			Collections.shuffle(pics);
			init_population(false);
			Runner r = new Runner(population, main_population);	
			Thread t = new Thread(r);
			threads[i] = t;
			runners[i] = r;
			t.start();
		}
		
		for (int i = 0; i < THREAD_COUNT; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {e.printStackTrace();}	
		}
		main_population = runners[0].main_population;
	}
		
	public void draw(){ 
		//sleep(20);
		//create_threads();
		for (int x = 0; x < population.width; x++){
			for (int y = 0; y < population.width; y++){
				population.check_swap(population.new_members[x][y]);
				population.reset();
			}
		}
		
		for (int x = 0; x < population.width; x++){
			for (int y = 0; y < population.width; y++){
				Member member = population.new_members[x][y];
				if (member.bestX != -1){
					member.x = member.bestX;
				}
				if (member.bestY != -1){
					member.y = member.bestY;
				}
				image(member.img, member.x, member.y);
			
			}
		}
		get_neighbors(false);
		//main_population.set_avg_fitness();
		//if (main_population.avg_fitness >= 97){
			//sleep(20);
			//System.exit(0);
		//}
		//System.out.println("Generation #" + generation + " || avg fitness: " + main_population.avg_fitness);
		generation++;
	}
	
	void shuffle(){
		Random r = new Random();
		for (int i = 0; i < r.nextInt(1000); i++){
			int rand1 = r.nextInt(population.width);
			int rand2 = r.nextInt(population.height);
			
			int rand3 = r.nextInt(population.width);
			int rand4 = r.nextInt(population.height);
			population.swap(population.new_members[rand1][rand2], population.new_members[rand3][rand4]);
		}
		population.reset();
		get_neighbors(false);
	}
	
	public boolean is_edge(int x, int y, PImage pic){
		int depth = 5;
		if (x < depth){
			return true;
		} 
		if (x > pic.width - depth){
			return true;
		} 
		if (y < depth){
			return true;
		} 
		if (y > pic.height - depth){
			return true;
		}
		return false;
	}
	
	public void fill_buckets(Member member){
		 // [0]: 0 - 63, [1]: 64 - 127, [2]: 128 - 191, [3]: 192 - 255
		int[] red_bucket = new int[4];	
		int[] green_bucket = new int[4];
		int[] blue_bucket = new int[4];
		
		for (int y = 0; y < member.img.height; y++){
			for (int x = 0; x < member.img.width; x++){
				if (!is_edge(x, y, member.img)){
					continue;
				}
				int r = (int) red(member.img.get(x, y));
				int g = (int) green(member.img.get(x, y));
				int b = (int) blue(member.img.get(x, y));
				
				if (r < 63){
					red_bucket[0]++;
				} else if (r < 127){
					red_bucket[1]++;
				} else if (r < 191){
					red_bucket[2]++;
				} else if (r > 191){
					red_bucket[3]++;
				}
				
				if (g < 63){
					green_bucket[0]++;
				} else if (g < 127){
					green_bucket[1]++;
				} else if (g < 191){
					green_bucket[2]++;
				} else if (g > 191){
					green_bucket[3]++;
				}
				
				if (b < 63){
					blue_bucket[0]++;
				} else if (b < 127){
					blue_bucket[1]++;
				} else if (b < 191){
					blue_bucket[2]++;
				} else if (b > 191){
					blue_bucket[3]++;
				}
			}
		}
		
		for (int i = 0; i < 4; i ++){
			member.red_hist[i] = (double) red_bucket[i] / member.img.pixels.length;
			member.green_hist[i] = (double) green_bucket[i] / member.img.pixels.length;
			member.blue_hist[i] = (double) blue_bucket[i] / member.img.pixels.length;
		}
	}
		
	public void init_population(boolean is_base){
		//Population tmp = new Population(POPULATION_SIZE);
		PImage[] tmp_pics = pics.toArray(new PImage[pics.size()]);
		int x = 0;
		int y = 0;
		for (int i = 0; i < tmp_pics.length; i++){
			if (i % 8 == 0){
				if (i != 0){
					y+=100;
					x = 0;
				}
			} else {
				x += 100;
			}
			
			Member member = new Member(i, x, y, tmp_pics[i]);
			x = x/100;
			y = y/100;
			population.new_members[x][y] = member; 

			//fill_buckets(member);
			//System.out.println(x);
			//population.add_member(member);
		}
		
		get_neighbors(is_base);
		//return population;
	}
	
	public void set_neighbor(int x, int y, Member m, int index, boolean is_base){
		Member member;
		try {
			member = population.new_members[x][y];
			System.out.println("here");
		} catch (Exception e){
			member = new Member();
		}
		
		m.neighbors[index] = member;

		//System.out.println("in set neighbor...");
		
		if (!is_base){
			return;
		}
		
		if (member.id == -9999){
			m.neighbor_ids[index] = 100;
		} else {
			m.neighbor_ids[index] = member.id;
		}
	}
	
	public void get_neighbors(boolean is_base){
		for (int x = 0; x < population.width; x++){
			for (int y = 0; y < population.height; y++){
				set_neighbor(x-1, y, population.new_members[x][y], 0, is_base); // left
				set_neighbor(x, y-1, population.new_members[x][y], 1,is_base); // top
				set_neighbor(x+1, y, population.new_members[x][y], 2,is_base); // right
				set_neighbor(x, y+1, population.new_members[x][y], 3,is_base); // bottom
			}
		}
	}
	
	public int[] get_edges(PImage pic){
		int[] edges = new int[200*4];
		int[] left_edge = new int[200];
		int[] right_edge = new int[200];
		int[] top_edge = new int[200];
		int[] bottom_edge = new int[200];

		int left_index=0, right_index=0, top_index=0, bottom_index=0;
		
		for (int y = 0; y < pic.height; y++){
			for (int x = 0; x < pic.width; x++){
				int loc = x + (y*pic.width);

				//System.out.println("RED: " + r + " GREEN: " + g + " BLUE: " + b);
				
				if (x == 0 || x == 1){
					left_edge[left_index] = pic.pixels[loc];
					left_index++;
				} 
				if (x == pic.width - 1 || x == pic.width - 2){
					right_edge[right_index] = pic.pixels[loc];
					right_index++;
				} 
				if (y == 0 || y == 1){
					top_edge[top_index] = pic.pixels[loc];
					top_index++;
				} 
				if (y == pic.height - 1 || y == pic.height - 2){
					bottom_edge[bottom_index] = pic.pixels[loc];
					bottom_index++;
				}
			}
		}
		
		System.arraycopy(left_edge, 0, edges, 0, left_edge.length);
		System.arraycopy(top_edge, 0, edges, left_edge.length, top_edge.length);
		System.arraycopy(right_edge, 0, edges, left_edge.length + top_edge.length, right_edge.length);
		System.arraycopy(bottom_edge, 0, edges, left_edge.length + top_edge.length + right_edge.length, bottom_edge.length);
		
		return edges;	
	}
	
	public void sleep(int seconds){
		System.out.println("sleeping...");
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}