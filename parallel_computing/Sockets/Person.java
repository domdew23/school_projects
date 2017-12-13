import java.io.Serializable;

public class Person implements Serializable {
	String name;
	int age;
	double income;

	public Person(String name, int age, double income){
		this.name = name;
		this.age = age;
		this.income = income;
	}

	public String toString(){
		return "Name: " + name + " | Age: " + age + " | Income: " + income;
	}
}