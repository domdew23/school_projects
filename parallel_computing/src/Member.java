
import processing.core.PImage;

public class Member{
	int id;
	int x,y; 
	PImage img;
	int[] affinites; // id of neighbors != 0
	
	Member(int id, int x, int y, PImage img){
		this.id = id;
		this.x = x;
		this.y = y;
		this.img = img;
		this.affinites = new int[32];
	}
	
	Member(){
		this.id = -9999;
		this.x = -1;
		this.y = -1;
	}
}
