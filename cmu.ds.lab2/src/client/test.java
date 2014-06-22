package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import core.Remote440Exception;

public class test {
 	static int[] intl = new int[3];
 	
 	public static void main(String args[]) throws IOException{
 		ServerSocket s = new ServerSocket(6666);
 		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"),1099);
 		System.out.println(s.getInetAddress()+ "   <-THIS");
 		
 		System.out.println(InetAddress.getLocalHost().getHostAddress());
 		
 		Socket socket1 = new Socket(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),7878);
 		
 		socket1.close();
 		try {
			A a = new A();
		} catch (Remote440Exception e) {
			System.out.println(e.hashCode());
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
	public A() throws Remote440Exception{
		throw new Remote440Exception("HJVABKJSBD");
	}
}
class subA extends A{

	public subA() throws IOException, Remote440Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
}