package communication;

public class SendMessage {
	static Object sendLock = new Object();
	
	
	/*
	 * @param
	 * @return -1 if call unsuccessful
	 * @return 0 if call successful
	 */
	public static int SendMessage(String host, int port, Message m) {
		Socket sendingSocket = new Socket(host,port);
	}
}
