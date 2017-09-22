import java.util.Random;
import java.util.concurrent.*;

class Bubble {
	float x;
	float y;
	float diameter;
	int id;

	Bubble(int tmp_id, float tempX, float tempY, float tempD){
		x = tempX;
		y = tempY;
		diameter = tempD;
		id = tmp_id;
	}

	void ascend(){
		Random r = new Random();
		int shift = r.nextInt(2 + 1 + 2) - 2;
		System.out.println(shift);
		y--;
		x = x + shift;
	}

	void top(){
		if (y < diameter/2){
			y = diameter/2;
		}
	}


}