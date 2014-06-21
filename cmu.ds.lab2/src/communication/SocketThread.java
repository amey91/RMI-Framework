package communication;

import java.net.Socket;

public abstract class SocketThread extends java.lang.Thread{
	protected Socket clientSocket;
	public SocketThread(Socket socket)
	{
		this.clientSocket = socket;
	}
}
