import java.util.ArrayList;
class TCPConnection
{
	private double startTime;
	private String serverIP, clientIP;
	private int serverPort, clientPort;
	public int sendSize, recvSize;
	private ArrayList<Long> seq, ack;
	private ArrayList<Double> seqTime, ackTime;

	TCPConnection(double t, String a, String b, int x, int y, int s1, int s2)
	{
		startTime = t;
		serverIP = a;
		clientIP = b;
		serverPort = x;
		clientPort = y;
		sendSize = s1;
		recvSize = s2;
		seq = new ArrayList<>();
		ack = new ArrayList<>();
		seqTime = new ArrayList<>();
		ackTime = new ArrayList<>();
	}

	public boolean isConn(String x, int a, String y, int b)
	{
		return (x.equals(serverIP) && y.equals(clientIP) && b==clientPort && a==serverPort) || (y.equals(serverIP) && x.equals(clientIP) && a==clientPort && b==serverPort);
	}

	public double getTime()
	{
		return startTime;
	}

	public boolean isServer(String x, int y)
	{
		return x.equals(serverIP) && y==serverPort;
	}

	public void addSeq(long l, double t)
	{
		seq.add(l);
		seqTime.add(t);
	}

	public void addAck(long l, double t)
	{
		ack.add(l);
		ackTime.add(t);
	}

	public int getSize()
	{
		return sendSize + recvSize;
	}

	public ArrayList<Long> getSeq()
	{
		return seq;
	}

	public ArrayList<Long> getAck()
	{
		return ack;
	}

	public ArrayList<Double> getSeqTime()
	{
		return seqTime;
	}

	public ArrayList<Double> getAckTime()
	{
		return ackTime;
	}	
}