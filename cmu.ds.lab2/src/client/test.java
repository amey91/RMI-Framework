package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class test {
 	static int[] intl = new int[3];
 	
 	public static void main(String args[]) throws UnknownHostException{
 		System.out.print(InetAddress.getLocalHost());
 		try {
			A a = new A();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
 		//superA supera = new superA();
 		//subA suba = new subA();
 		//Object n = suba;
 		//if(n instanceof superA){
 		//	System.out.println("YES");
 		//}
 	}
}


interface superA {

}
class A implements superA{
	public A() throws IOException{
		throw new IOException("HJVABKJSBD");
	}
}
class subA extends A{

	public subA() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
}