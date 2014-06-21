package test;

public class GenericClassTest {
	public static void main(String args[]){

		  Integer[] a = {0,1,2};
		  Object b = (Object)a;
		  System.out.println(b.getClass().toString());
		String className = "HelloTest";
		SuperClass ab = new HelloTest();
		
		System.out.println(ab.getHello());
	}
}

class SuperClass {

	public String helloString = "Hi";
	public String getHello() {
		return helloString;
		
	}
	
}

class HelloTest extends SuperClass{
	
	public HelloTest(){
		super();
	}
	@Override
	public String getHello(){
		return super.getHello();
		
	}
}
