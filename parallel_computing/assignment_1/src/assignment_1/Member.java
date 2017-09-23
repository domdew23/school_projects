package assignment_1;

import java.util.Arrays;
import processing.core.PImage;

public class Member{
	int x=0, y=0; 
	int[] edges;
	Member[] neighbors; // [0] left, [1] top, [2] right, [3] bottom
	int count;
	double fitness;
	PImage img;
	boolean needs_to_swap = false;
	
	Member(int x, int y, int[] edges, PImage img){
		this.x = x;
		this.y = y;
		this.edges = edges;
		this.fitness = 0;
		this.img = img;
		neighbors = new Member[4];
		count = 0;
	}
	
	public void evaluate_fitness(){
		img.loadPixels();
		int left_count = 0;
		int top_count = 0;
		int right_count = 0;
		int bottom_count = 0;
		double total_diff = 0.0;
		double diff = 0.0;
		for (int i = 0; i < edges.length; i++){
			if (i < 200){
				// left edge
				if (neighbors[0] != null){
					neighbors[0].img.loadPixels();
					diff = Math.abs(edges[i] - neighbors[0].edges[i]);
					total_diff += diff;
					if (edges[i] == neighbors[0].edges[i]){
						left_count++;
					}
				}else {
					//left_count+=30;
				}
			} else if (i > 200 && i < 400){
				//top
				if (neighbors[1] != null){
					neighbors[1].img.loadPixels();
					diff = Math.abs(edges[i] - neighbors[1].edges[i]);
					total_diff += diff;
					if (edges[i] == neighbors[1].edges[i]){
						top_count++;
					} 
				}else {
					//top_count+=30;
				}
			} else if (i > 400 && i < 600){
				//right
				if (neighbors[2] != null){
					neighbors[2].img.loadPixels();
					diff = Math.abs(edges[i] - neighbors[2].edges[i]);
					total_diff += diff;
					if (edges[i] == neighbors[2].edges[i]){
						right_count++;
					}
				} else {
					//right_count+=30;
				}
			} else {
				//bottom
				if (neighbors[3] != null){
					neighbors[3].img.loadPixels();
					diff = Math.abs(edges[i] - neighbors[3].edges[i]);
					total_diff += diff;
					if (edges[i] == neighbors[3].edges[i]){
						bottom_count++;
					}
				} else {
					//bottom_count+=30;
				}
			}
		}
		
		int total = left_count + top_count + right_count + bottom_count;
		double f = ((double)total) / (double) (edges.length/2);
		double avg_diff = total_diff/edges.length;
		System.out.println("total diff: " + total_diff + " || total: " + total);
		//System.out.println("total: " + total + " || edges len: " + edges.length/2 + " = " + f);
		this.fitness = f * 100;
		System.out.println(fitness);
	}
	
	public void add_neighbor(Member member){
		neighbors[count] = member;
		count++;
	}
	
	public void reset(){
		count = 0;
		fitness = 0;
	}

}
