class TCPConnection
{
	private double startTime;
	private String serverIP, clientIP;
	private int serverPort, clientPort;

	TCPConnection(double t, String a, String b, int x, int y)
	{
		startTime = t;
		serverIP = a;
		clientIP = b;
		serverPort = x;
		clientPort = y;
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
}