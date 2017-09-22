package assignment_1;

import processing.core.PApplet;
import java.util.Random;
import java.util.concurrent.TimeUnit;
public class main extends PApplet {
	
	Pixel[] all_pixels;
	Member[] members;
	Population population;
	final int WIDTH = 800;
	final int HEIGHT = 400;
	final int AREA = WIDTH * HEIGHT;
	final int POPULATION_SIZE = 20;
	int generation = 0;

	public static void main(String[] args) {
		PApplet.main("assignment_1.main");		
	}
	
	public void settings(){
		size(WIDTH,HEIGHT);		
	}
	
	public void init_population(){
		loadPixels();
		for (int i = 0; i < POPULATION_SIZE; i++){
			int [] tmp_pixels = new int[AREA];
			Random r = new Random();
			for (int y = 0; y < HEIGHT; y++){
				for (int x = 0; x < WIDTH; x++){
					int loc = x + (y*WIDTH);
					tmp_pixels[loc] = color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
				}
			}
			Member mem = new Member(i, tmp_pixels, WIDTH, HEIGHT);
			population.add_member(mem);
		}
		population.sort();
	}
	
	public void setup(){
		background(0);
		population = new Population(POPULATION_SIZE, WIDTH, HEIGHT);
		init_population();
	}
	
	public void draw(){
		population.create_mating_pool();
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				int loc = x + (y*width);
				set(x, y, population.get_best_member().pixels[loc]);
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

