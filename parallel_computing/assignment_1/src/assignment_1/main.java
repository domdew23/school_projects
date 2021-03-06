package assignment_1;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class main extends PApplet {
	final int POPULATION_SIZE = 32;
	final int THREAD_COUNT = 32;
	int generation = 0;
	Population population; 
	PImage[] pics = new PImage[POPULATION_SIZE];
	AtomicReference<Population> GLOBAL_POPULATION = new AtomicReference<Population>(new Population(POPULATION_SIZE));
	AtomicInteger best_pos = new AtomicInteger(0);
	Thread[] threads = new Thread[THREAD_COUNT];

	Population[] all = new Population[THREAD_COUNT];

	public static void main(String[] args) {
		PApplet.main("assignment_1.main");		
	}
	
	public void settings(){
		size(800, 400);
	}
	
	public void setup(){
		for (int i = 0; i < 32; i++){
			PImage pic = loadImage("images/pic_"+(i+1)+".jpg");
			pics[i] = pic;
		}
		background(255);
	}
	
	public void create_threads(){		
		for (int i = 0; i < THREAD_COUNT; i++){
			population = new Population(POPULATION_SIZE);
			init_population();
			set_neighbors();
			shuffle();
			all[i] = population;
			population.id = i;
			Thread t = new Thread(new Runner(all[i], GLOBAL_POPULATION, best_pos), "thread-"+i);
			threads[i] = t;
			t.start();
		}
		
		for (int i = 0; i < THREAD_COUNT; i++){
			try {threads[i].join();} catch (InterruptedException e) {e.printStackTrace();}	
		}
		generation++;
		System.out.println("Generation: " + generation);
	}
		
	public void draw(){
		loadPixels();
		Random r = new Random();
		for (int i = 0; i < pixels.length; i++){
			set(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		}
		updatePixels();
		/*try {  Thread.sleep(500); } catch (InterruptedException e){}
		
		if (!(GLOBAL_POPULATION.get().fitness > 1.07)){
			create_threads();
		}

		if(!(GLOBAL_POPULATION.compareAndSet(GLOBAL_POPULATION.get(), all[best_pos.get()]))){
			return;
		}

		for (int i = 0; i < POPULATION_SIZE; i++){				
			image(GLOBAL_POPULATION.get().members[i].img, (GLOBAL_POPULATION.get().members[i].x * 100), (GLOBAL_POPULATION.get().members[i].y * 100));
		}*/
	}
		
	public void init_population(){
		int x = 0;
		int y = 0;
		for (int i = 0; i < pics.length; i++){
			if (i % 8 == 0){
				if (i != 0){
					y+=1;
					x = 0;
				}
			} else {
				x += 1;
			}
			Member member = new Member(i, x, y, pics[i]);
			population.members[i] = member; 
		}
	}
	
	public void set_neighbors(){
		for (int i = 0; i < population.members.length; i++){
			if (has_neighbor(i-1)){
				population.members[i].affinites[i-1] = 100;
			}
			if (has_neighbor(i-population.WIDTH)){
				population.members[i].affinites[i-population.WIDTH] = 100;
			}
			if (has_neighbor(i+1)) {
				population.members[i].affinites[i+1] = 100;
			}
			if (has_neighbor(i+population.WIDTH)) {
				population.members[i].affinites[i+population.WIDTH] = 100;
			}
		}
	}

	public boolean has_neighbor(int neighbor_index){
		if (neighbor_index < 0 || neighbor_index >= 32){
			return false;
		}
		return true;
	}
	
	public void shuffle(){
		if (Math.random() <= (GLOBAL_POPULATION.get().fitness / 5)){
			return;
		}
		Random r = new Random();
		int count = r.nextInt(5) * Math.abs(1000 -  (int) (GLOBAL_POPULATION.get().fitness * 1000) );
		if (count <= 0) {
			return;
		}
		for (int i = 0; i < r.nextInt(count); i++){
			int rand1 = r.nextInt(POPULATION_SIZE);
			int rand2 = r.nextInt(POPULATION_SIZE);
			swap(population.members[rand1], population.members[rand2], rand1, rand2);
		}
	}

	public void swap(Member one, Member two, int index1, int index2){
		
		int one_tmpX =  one.x;
		int one_tmpY = one.y;
		Member tmp_one = population.members[index1];
	
		one.x = two.x;
		two.x = one_tmpX;
		
		one.y = two.y;
		two.y = one_tmpY;
		
		population.members[index1] = population.members[index2];
		population.members[index2] = tmp_one;
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