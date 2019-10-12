class TCPConnection
{
	private double startTime;
	private String serverIP, clientIP;
	private int serverPort, clientPort;
	public int sendSize, recvSize;

	TCPConnection(double t, String a, String b, int x, int y, int s1, int s2)
	{
		startTime = t;
		serverIP = a;
		clientIP = b;
		serverPort = x;
		clientPort = y;
		sendSize = s1;
		recvSize = s2;
	}

	public boolean isIP(String x, String y)
	{
		return (x.equals(serverIP) && y.equals(clientIP)) || (y.equals(serverIP) && x.equals(clientIP));
	}

	public boolean isPort(int x, int y)
	{
		return (x==clientPort && y==serverPort) ||  (y==clientPort && x==serverPort);
	}

	public double getTime()
	{
		return startTime;
	}

	public boolean isServer(String x, int y)
	{
		return x.equals(serverIP) && y==serverPort;
	}
}