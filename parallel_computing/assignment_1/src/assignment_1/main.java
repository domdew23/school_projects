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
		//sleep(3);
		System.out.println("Generation #" + generation);
		for (int i = 0; i < POPULATION_SIZE; i++){
			Member member = population.members[i];
			image(member.img, member.x, member.y);
			if (population.avg_fitness >= 60){
				sleep(20);
				System.exit(0);
			}
		}
		get_neighbors();
		evaluation();
		population.sort();
		population.set_avg_fitness();
		for (Member m : population.members){
			population.check_swap(m);
			m.reset();
		}
		generation++;
	}
	public void evaluation(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			population.members[i].evaluate_fitness();;
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
	
	public void fill_buckets(Member member){
		int red_bucket_1 = 0; // 0 - 63
		int red_bucket_2 = 0; // 64 - 127
		int red_bucket_3 = 0; // 128 - 191
		int red_bucket_4 = 0; // 192 - 255

		int green_bucket_1 = 0; // 0 - 63
		int green_bucket_2 = 0; // 64 - 127
		int green_bucket_3 = 0; // 128 - 191
		int green_bucket_4 = 0; // 192 - 255
		
		int blue_bucket_1 = 0; // 0 - 63
		int blue_bucket_2 = 0; // 64 - 127
		int blue_bucket_3 = 0; // 128 - 191
		int blue_bucket_4 = 0; // 192 - 255
		
		for (int y = 0; y < member.img.height; y++){
			for (int x = 0; x < member.img.width; x++){
				int r = (int) red(member.img.get(x, y));
				int g = (int) green(member.img.get(x, y));
				int b = (int) blue(member.img.get(x, y));
				
				if (r < 63){
					red_bucket_1++;
				} else if (r < 127){
					red_bucket_2++;
				} else if (r < 191){
					red_bucket_3++;
				} else if (r > 191){
					red_bucket_4++;
				}
				
				if (g < 63){
					green_bucket_1++;
				} else if (g < 127){
					green_bucket_2++;
				} else if (g < 191){
					green_bucket_3++;
				} else if (g > 191){
					green_bucket_4++;
				}
				
				if (b < 63){
					blue_bucket_1++;
				} else if (b < 127){
					blue_bucket_2++;
				} else if (b < 191){
					blue_bucket_3++;
				} else if (b > 191){
					blue_bucket_4++;
				}
			}
		}
		
		member.red_hist1 = (double) red_bucket_1/member.img.pixels.length;
		member.red_hist2 = (double) red_bucket_2/member.img.pixels.length;
		member.red_hist3 = (double) red_bucket_3/member.img.pixels.length;
		member.red_hist4 = (double) red_bucket_4/member.img.pixels.length;
		
		member.green_hist1 = (double) green_bucket_1/member.img.pixels.length;
		member.green_hist2 = (double) green_bucket_2/member.img.pixels.length;
		member.green_hist3 = (double) green_bucket_3/member.img.pixels.length;
		member.green_hist4 = (double) green_bucket_4/member.img.pixels.length;
		
		member.blue_hist1 = (double) blue_bucket_1/member.img.pixels.length;
		member.blue_hist2 = (double) blue_bucket_2/member.img.pixels.length;
		member.blue_hist3 = (double) blue_bucket_3/member.img.pixels.length;
		member.blue_hist4 = (double) blue_bucket_4/member.img.pixels.length;

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
			//int[] edges = get_edges(pics.get(i));
			Member member = new Member(i, x, y, pics.get(i));
			fill_buckets(member);
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
			member = population.members[i+shift];
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
}
	
	
	
	