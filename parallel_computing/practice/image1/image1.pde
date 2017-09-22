PImage pic;
PImage bubb;
Bubble[] bubbles = new Bubble[5];
PImage[] pics = new PImage[3]; // picture array

void setup(){
  size(600, 360);
  bubb = loadImage("bubb.jpg");
  
  for (int i = 0; i < pics.length; i++){
    pics[i] = loadImage("bubb"+i+".jpg");
  }
  
  for (int i = 0; i < bubbles.length; i++){
    int index = int(random(0, pics.length));
    bubbles[i] = new Bubble(index, 100 + i * 100, 300, random(32, 72));
  }
}

void draw(){
  background(255);
   for (int i = 0; i < bubbles.length; i++){
     bubbles[i].ascend();
     display(bubbles[i]);
     bubbles[i].top();
   }
}

void display(Bubble b){
   imageMode(CENTER);
   image(pics[b.id], b.x, b.y, b.diameter, b.diameter);
}