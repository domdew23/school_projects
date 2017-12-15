import java.io.Serializable;

public class Person implements Serializable {
	int x;
	int y;
	String name = "Jake";

	public Person(int x, int y){
		this.x = x;
		this.y = y;
	}

	public String toString(){
		return name + " | x: " + x + " | y: " + y;
	}
}