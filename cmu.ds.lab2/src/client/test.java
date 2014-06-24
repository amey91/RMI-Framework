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