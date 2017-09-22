PImage pic;

void setup(){
	size(600, 400);
	pic = loadImage("pic.jpg");
}

void draw(){
	background(0);
	//tint(255, 0, 0);
	image(hog, 0, 0);
}