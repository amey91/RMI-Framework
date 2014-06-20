package client;

public class test {
 	static int[] intl = new int[3];
 	
 	public static void main(String args[]){
 		System.out.print(intl.length);
 		A a = new A();
 		//superA supera = new superA();
 		subA suba = new subA();
 		Object n = suba;
 		if(n instanceof superA){
 			System.out.println("YES");
 		}
 	}
}


interface superA {
	
}
class A implements superA{
	
}
class subA extends A{
	
}