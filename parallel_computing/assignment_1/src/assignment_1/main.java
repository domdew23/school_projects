package assignment_1;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class main extends PApplet {
	Population main_population;
	final int POPULATION_SIZE = 32;
	final int THREAD_COUNT =32;
	int generation = 0;
	double fitness = 0;
	Population population = new Population(POPULATION_SIZE);
	//private Population GLOBAL_POPULATION = new Population(POPULATION_SIZE);
	PImage[] pics = new PImage[POPULATION_SIZE];
	Population tmp_pop = new Population(POPULATION_SIZE);
	AtomicReference<Population> GLOBAL_POPULATION = new AtomicReference<Population>(tmp_pop);


	public static void main(String[] args) {
		PApplet.main("assignment_1.main");		
	}
	
	public void settings(){
		size(800, 400);
	}
	
	public void setup(){
		for (int i = 0; i < 32; i++){
			PImage pic = loadImage("/images/pic_"+(i+1)+".jpg");
			pics[i] = pic;
		}
		background(255);
		//create_threads();
	}
	
	public void create_threads(){
		Thread[] threads = new Thread[THREAD_COUNT];
	
		for (int i = 0; i < THREAD_COUNT; i++){
			init_population();
			set_neighbors();
			shuffle();
			Thread t = new Thread(new Runner(population, GLOBAL_POPULATION), "thread-"+i);
			threads[i] = t;
			t.start();
		}
		
		for (int i = 0; i < THREAD_COUNT; i++){
			try {threads[i].join();} catch (InterruptedException e) {e.printStackTrace();}	
		}
	}
		
	public void draw(){
		//sleep(1);
		try {  Thread.sleep(500); } catch (InterruptedException e){}
		create_threads();
		System.out.println(GLOBAL_POPULATION.get().fitness);
		for (int i = 0; i < POPULATION_SIZE; i++){				
			image(GLOBAL_POPULATION.get().members[i].img, (GLOBAL_POPULATION.get().members[i].x * 100), (GLOBAL_POPULATION.get().members[i].y * 100));
		}
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
			if (has_neighbor(i-population.width)){
				population.members[i].affinites[i-population.width] = 100;
			}
			if (has_neighbor(i+1)) {
				population.members[i].affinites[i+1] = 100;
			}
			if (has_neighbor(i+population.width)) {
				//System.out.println(i+population.width + " -- " + i);
				population.members[i].affinites[i+population.width] = 100;
			}
		}
	}

	public boolean has_neighbor (int neighbor_index){
		if (neighbor_index < 0 || neighbor_index >= 32){
			return false;
		}
		return true;
	}
	
	public void shuffle(){
		Random r = new Random();
		for (int i = 0; i < r.nextInt(500); i++){
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