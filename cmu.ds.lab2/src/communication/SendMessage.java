package communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SendMessage {
	static Object sendLock = new Object();
	private static Socket sendingSocket;
	
	
	/*
	 * @param
	 * @return -1 if call unsuccessful
	 * @return 0 if call successful
	 */
	public static int sendMessage(String hostName, int port, Message m) throws InterruptedException {
		try {
			sendingSocket = new Socket(InetAddress.getByName(hostName),port);
			ObjectOutputStream os = new ObjectOutputStream(sendingSocket.getOutputStream());
			os.writeObject(m);
			Thread.sleep(200);
			//close sending socket
			sendingSocket.close();
			return 0;
			
		} catch (IOException e) {
			e.printStackTrace( );
			return -1;
		}
		
	}
}
