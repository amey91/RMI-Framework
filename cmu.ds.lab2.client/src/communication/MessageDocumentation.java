package communication;

/*
 * This class describes the common message formats that all 
 * classes use for communication 
 * 
 * This is just for documentation of architecture and not implementation
 */

public class MessageDocumentation {
	/*
	 *  class Message describes a common message that will be sent
	 *  over the network. 
	 *  3 other classes inherit from it and thus give us 3 main types of messages
	 *  that can be sent over our system
	 *  These classes are: 
	 *  	1. InvocationMessage -> Invoke a method
	 *  	2. ExceptionMessage -> Exception occurred and op was unsuccessful
	 *  	3. ReturnMessage -> Successful execution of program  
	 */
	
}
