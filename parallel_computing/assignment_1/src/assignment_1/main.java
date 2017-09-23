package assignment_1;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class main extends PApplet {
	PImage pic;
	ArrayList<PImage> pics = new ArrayList<PImage>();
	int[] coordinates;
	Population population;
	final int POPULATION_SIZE = 32;
	int generation = 0;
	int total_fitness = 0;
	
	public static void main(String[] args) {
		PApplet.main("assignment_1.main");		
	}
	
	public void settings(){
		size(800, 400);
	}
	
	public void setup(){
		for (int i = 1; i <= 32; i++){
			PImage tmp = loadImage("/images/pic_"+i+".jpg");
			pics.add(tmp);
		}
		
		Collections.shuffle(pics);
		background(255);
		population = new Population(POPULATION_SIZE);
		init_population();
	}
	
	public void draw(){
		System.out.println("Generation #" + generation);
		for (int i = 0; i < POPULATION_SIZE; i++){
			Member member = population.members[i];
			image(member.img, member.x, member.y);
			if (population.avg_fitness >= 75){
				sleep(20);
				System.exit(0);
			}
		}
		get_neighbors();
		evaluation();
		population.sort();
		//System.out.println("Spot 0: " + population.members[0].fitness);
		population.set_avg_fitness();
		//population.print_fitnesses();
		//sleep(8);
		population.members = population.create_new_generation();
		generation++;
	}
	public void evaluation(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			evaluate_fitness(population.members[i]);
			//population.members[i].evaluate_fitness();
			//System.out.println("Member["+i+"] fitness: " + population.members[i].fitness);
		}
	}
	
	public boolean contains(int[] arr, int x){
		for (int n : arr){
			if (x == n){
				return true;
			}
		}
		return false;
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
	
	public void init_population(){
		int x = 0;
		int y = 0;
		for (int i = 0; i < pics.size(); i++){
			if (i % 8 == 0){
				if (i != 0){
					y+=100;
					x = 0;
				}
			} else {
				x += 100;
			}
			int[] edges = get_edges(pics.get(i));
			Member member = new Member(x, y, edges, pics.get(i));
			population.add_member(member);
			//image(pics.get(i),x,y);
		} 

	}
	
	public void show(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			System.out.println("here");
			Member member = population.members[i];
			image(member.img, member.x, member.y);
		}
	}
	
	public void set_neighbor(int i, int shift, Member m){
		Member member;
		try {
			member = population.members[i+shift]; //left
		} catch (Exception e){
			member = null;
		}
		m.add_neighbor(member);
	}
	
	public void get_neighbors(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			set_neighbor(i, - 1, population.members[i]); // left
			set_neighbor(i, - 4, population.members[i]); // top
			set_neighbor(i, + 1, population.members[i]); // right
			set_neighbor(i, + 1, population.members[i]); // bottom
		}
	}
	public void sleep(int seconds){
		System.out.println("sleeping...");
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	
	
	public void evaluate_fitness(Member member){
		member.img.loadPixels();
		int left_count = 0;
		int top_count = 0;
		int right_count = 0;
		int bottom_count = 0;
		double bright_diff = 0.0;
		double color_diff = 0.0;
		double total_diff = 0.0;
		
		for (int i = 0; i < member.edges.length; i++){
			if (i < 200){
				// left edge
				if (member.neighbors[0] != null){
					member.neighbors[0].img.loadPixels();
					bright_diff = Math.abs(brightness(member.edges[i]) - brightness(member.neighbors[0].edges[i]));
					color_diff = Math.abs(color(member.edges[i]) - color(member.neighbors[0].edges[i]));
					if (color_diff == 0 || bright_diff == 0){
						left_count++;
					} else {
						total_diff += bright_diff;
						total_diff += color_diff;
					}
				} else {
					//left_count+=30;
				}
			} else if (i > 200 && i < 400){
				//top
				if (member.neighbors[1] != null){
					member.neighbors[1].img.loadPixels();
					bright_diff = Math.abs(brightness(member.edges[i]) - brightness(member.neighbors[1].edges[i]));
					color_diff = Math.abs(color(member.edges[i]) - color(member.neighbors[1].edges[i]));
					if (color_diff == 0 || bright_diff == 0){
						top_count++;
					} else {
						total_diff += bright_diff;
						total_diff += color_diff;
					}
				}else {
					//top_count+=30;
				}
			} else if (i > 400 && i < 600){
				//right
				if (member.neighbors[2] != null){
					member.neighbors[2].img.loadPixels();
					bright_diff = Math.abs(brightness(member.edges[i]) - brightness(member.neighbors[2].edges[i]));
					color_diff = Math.abs(color(member.edges[i]) - color(member.neighbors[2].edges[i]));
					if (color_diff == 0 || bright_diff == 0){
						right_count++;
					} else {
						total_diff += bright_diff;
						total_diff += color_diff;
					}
				} else {
					//right_count+=30;
				}
			} else {
				//bottom
				if (member.neighbors[3] != null){
					member.neighbors[3].img.loadPixels();
					bright_diff = Math.abs(brightness(member.edges[i]) - brightness(member.neighbors[3].edges[i]));
					color_diff = Math.abs(color(member.edges[i]) - color(member.neighbors[3].edges[i]));
					if (color_diff == 0 || bright_diff == 0){
						bottom_count++;
					} else {
						total_diff += bright_diff;
						total_diff += color_diff;
					}
				} else {
					//bottom_count+=30;
				}
			}
		}
		
		int total = left_count + top_count + right_count + bottom_count;
		double f = ((double)total) / (double) (member.edges.length/2);
		double avg_diff = total_diff/(member.edges.length);
		//System.out.println("avg diff: " + avg_diff + " || total: " + total);
		
		//System.out.println("total: " + total + " || f: " + f);
		member.fitness = (100 - (Math.sqrt(avg_diff)/60)) + (10 * f);
		System.out.println("Member: " + member.x + ", " + member.y +  " avg diff: " + avg_diff + " || fitness: " + member.fitness);
		//System.out.println("Fitness: " + member.fitness);

	}
	
}
/*
	
	
	
	 
	Pixel[] all_pixels;
	Member[] members;
	Population population;
	final int WIDTH = 1000;
	final int HEIGHT = 320;
	final int AREA = WIDTH * HEIGHT;



	
	public void settings(){
		size(WIDTH,HEIGHT);		
	}
	
	public void init_population(){
		int [] tmp_pixels = new int[AREA];
		Random r = new Random();
		int id = 0;
		int start = 0;
		int finish = 10000;
		
		for (int j = 0; j < AREA; j++){
			tmp_pixels[j] = color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
			if ((j % 10000 == 0 || j == AREA-1) && j !=0){
				int[] new_array = Arrays.copyOfRange(tmp_pixels, start, finish);
				Member mem = new Member(id, new_array, 1000, 10);
				population.add_member(mem);
				id++;
				start+= 10000;
				finish += 10000;
			}
		}
		population.sort();
		//population.print_fitnesses();
	}
	
	public void setup(){
		background(0);
		population = new Population(POPULATION_SIZE, 1000, 10);
		init_population();
	}
	
	public void draw(){
		int i = -1;
		int loc = 0;
		boolean been_here = false;
		population.create_mating_pool();

		for (int y = 0; y < height; y++){
			been_here = false;
			for (int x = 0; x < width; x++){

				if (y % 10 == 0 && !been_here){
					i++;
					loc = 0;
					been_here = true;
				}

				set(x, y, population.members[i].pixels[loc]);
				loc++;
			}
		}
		System.out.println("Generation #" + generation + " || Max fitness: " + population.get_best_member().fitness);
		generation++;
	}
	
	public void sleep(int seconds){
		System.out.println("sleeping...");
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void run(){
		int[] tmp = new int[AREA];
		//loadPixels();
		
		//System.out.println("Best member: " + population.get_best_member().fitness);
		//pixels = population.get_best_member().pixels;
		//System.out.println("GEN DONE");
	}
}
*/
