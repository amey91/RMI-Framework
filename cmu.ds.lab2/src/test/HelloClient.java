package test;
import java.rmi.Naming;


public class HelloClient {
	  /**
	   * Client program for the "Hello, world!" example.
	   * @param argv The command line arguments which are ignored.
	   */
	  public static void main (String[] argv) {
	    try {
	      HelloInterface hello = 
	        (HelloInterface) Naming.lookup ("//localhost/Hello123");
	      System.out.println (hello.say());
	    } catch (Exception e) {
	      System.out.println ("HelloClient exception: " + e);
	    }
	  }
}
