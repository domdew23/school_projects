import java.io.Serializable;

public class Person implements Serializable {
	int x;
	int y;

	public Person(int x, int y){
		this.x = x;
		this.y = y;
	}

	public String toString(){
		return "x: " + x + " | y: " + y;
	}
}