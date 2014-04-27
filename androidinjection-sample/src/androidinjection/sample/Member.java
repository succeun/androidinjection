package androidinjection.sample;


public class Member {
	private int no;
	private String name;
	private int age;
	
	public Member(int no, String name, int age) {
		this.no = no;
		this.name = name;
		this.age = age;
	}
	
	public int getNo() {
		return no;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	@Override
	public String toString() {
		return "[no: " + no + ", name: " + name + ", age: " + age + "]";
	}
}