import java.util.*;
import java.io.*;
class Questions
{
	private static PrintWriter out;

	public static void main(String[] args)
	{
		runAll("file1.csv");
		runAll("file2.csv");
		runAll("file3.csv");
	}

	public static void runAll(String arg)
	{
		try
		{
			Parser.read(arg);

			// out = new PrintWriter(new File(arg+"_q1"));
			// q1();

			// out = new PrintWriter(new File(arg+"_q2"));
			// q2();

			// out = new PrintWriter(new File(arg+"_q3"));
			// q3();

			// out = new PrintWriter(new File(arg+"_q4"));
			// q4();

			// out = new PrintWriter(new File(arg+"_q5_send_vs_time"));
			// q5(true);

			// out = new PrintWriter(new File(arg+"_q5_send_vs_recv"));
			// q5(false);

			out = new PrintWriter(new File(arg+"_q6"));
			q6(true);

			out = new PrintWriter(new File(arg+"_q7"));
			q7(true);

			// out = new PrintWriter(new File(arg+"_q8_send"));
			// q8(true);

			// out = new PrintWriter(new File(arg+"_q8_recv"));
			// q8(false);

			// out = new PrintWriter(new File(arg+"_q9_seq"));
			// q9(true);

			// out = new PrintWriter(new File(arg+"_q9_ack"));
			// q9(false);

			// out = new PrintWriter(new File(arg+"_q11_avg_queue_size"));
			// q11(true);

			out = new PrintWriter(new File(arg+"_q11_avg_waiting_time"));
			q11(false);
		}catch(Exception e){e.printStackTrace();}
	}

	public static void q1()
	{
		HashSet<String> servers = new HashSet<>(), clients = new HashSet<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP") && Parser.data.get(i).get(6).contains("[SYN]"))
			{
				servers.add(Parser.data.get(i).get(3));
				clients.add(Parser.data.get(i).get(2));
			}
		}

		out.println("No. of Servers = "+servers.size());
		out.println("No. of Clients = "+clients.size()+"\n");
		out.close();
	}

	public static void q2()
	{
		HashSet<String> set = new HashSet<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP"))
			{
				String s = Parser.data.get(i).get(6);
				String res = Parser.data.get(i).get(2)+" "+Parser.data.get(i).get(3)+" ";
				String p1 = s.substring(0,s.indexOf('>'));
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('['));
				p1.trim();p2.trim();

				res+= (p1+" "+p2);
				set.add(res);
			}
		}

		out.println("No. of unique TCP flows = "+set.size()+"\n");
		out.close();
	}

	public static void q3()
	{
		HashSet<String> set = new HashSet<>();
		int[] count = new int[24];

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP"))
			{
				int hr = (int)(Double.parseDouble(Parser.data.get(i).get(1))/3600);
				String s = Parser.data.get(i).get(6);
				String res = Parser.data.get(i).get(2)+" "+Parser.data.get(i).get(3)+" ";
				String p1 = s.substring(0,s.indexOf('>')).trim();
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('[')).trim();

				res+= (p1+" "+p2);
				if(set.add(res))
					count[hr]++;
			}
		}

		for(int i=0;i<24;++i)
			out.println(i+" "+count[i]);
		out.close();
	}

	public static void q4()
	{
		ArrayList<TCPConnection> connections = new ArrayList<>();
		ArrayList<Double> time = new ArrayList<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP"))
			{
				double t = Double.parseDouble(Parser.data.get(i).get(1));
				String client = Parser.data.get(i).get(2);
				String server = Parser.data.get(i).get(3);
				String s = Parser.data.get(i).get(6);
				String p1 = s.substring(0,s.indexOf('>')).trim();
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('[')).trim();
				int cp = Integer.parseInt(p1);
				int sp = Integer.parseInt(p2);
					
				if(Parser.data.get(i).get(6).contains("[SYN]"))
					connections.add(new TCPConnection(t, server, client, sp, cp, 0, 0));
				
				else if(Parser.data.get(i).get(6).contains("[FIN, ACK]") || Parser.data.get(i).get(6).contains("[RST]"))
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							time.add(t - connections.get(j).getTime());
							connections.remove(j);
							break;
						}
					}
				}
			}
		}

		Collections.sort(time);
		// for(int i=0;i<time.size();++i)
			// out.println(time.get(i));
		// out.println("\"0\",\"0\"");
		double size = time.size();
		int i = 0;
		for(double j = 1; j<=3700;j+=0.5)
		{
			for(int k=i;;++k)
			{
				if(k==time.size() || time.get(k)>j)
				{
					i = k-1;
					break;
				}
			}

			out.println(j+" "+((i+1)/size));
		}
		out.close();
	}

	public static void q5(boolean tim)
	{
		ArrayList<TCPConnection> connections = new ArrayList<>();
		ArrayList<Integer> sendSizes = new ArrayList<>();
		ArrayList<Integer> recvSizes = new ArrayList<>();
		ArrayList<Double> time = new ArrayList<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP"))
			{
				double t = Double.parseDouble(Parser.data.get(i).get(1));
				String client = Parser.data.get(i).get(2);
				String server = Parser.data.get(i).get(3);
				String s = Parser.data.get(i).get(6);
				String p1 = s.substring(0,s.indexOf('>')).trim();
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('[')).trim();
				int cp = Integer.parseInt(p1);
				int sp = Integer.parseInt(p2);
				int len = Integer.parseInt(Parser.data.get(i).get(5).trim());
					
				if(Parser.data.get(i).get(6).contains("[SYN]"))
					connections.add(new TCPConnection(t, server, client, sp, cp, len, 0));

				else if(Parser.data.get(i).get(6).contains("[FIN, ACK]") || Parser.data.get(i).get(6).contains("[RST]"))
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							time.add(t - connections.get(j).getTime());
							if(connections.get(j).isServer(server, sp))
							{
								sendSizes.add(connections.get(j).sendSize+len);
								recvSizes.add(connections.get(j).recvSize);
							}
							else
							{
								sendSizes.add(connections.get(j).sendSize);
								recvSizes.add(connections.get(j).recvSize+len);
							}
							connections.remove(j);
							break;
						}
					}
				}

				else
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							if(connections.get(j).isServer(server, sp))
								connections.get(j).sendSize += len;
							else
								connections.get(j).recvSize += len;
							break;
						}
					}
				}
			}
		}

		double avg_time = 0;
		double avg_sendLength = 0;
		double avg_recvLength = 0;

		for(int i=0;i<time.size();++i)
		{
			avg_time += time.get(i);
			avg_sendLength += (double)sendSizes.get(i);
			avg_recvLength += (double)recvSizes.get(i);

			if(!tim)
			{
				if(recvSizes.get(i)<40000 && sendSizes.get(i)<40000)
					out.println(recvSizes.get(i)+" "+sendSizes.get(i));
			}
			else
			{
				if(time.get(i)<1000 && sendSizes.get(i)<40000)
					out.println(time.get(i)+" "+sendSizes.get(i));
			}
		}

		avg_time = avg_time/time.size();
		avg_sendLength = avg_sendLength/time.size();
		avg_recvLength = avg_recvLength/time.size();

		double x = 0, y = 0, z = 0, w = 0, v = 0;

		for(int i=0;i<time.size();++i)
		{
			x += (time.get(i)-avg_time)*(sendSizes.get(i)-avg_sendLength);
			y += (time.get(i)-avg_time)*(time.get(i)-avg_time);
			z += (sendSizes.get(i)-avg_sendLength)*(sendSizes.get(i)-avg_sendLength);
			w += (recvSizes.get(i)-avg_recvLength)*(recvSizes.get(i)-avg_recvLength);
			v += (recvSizes.get(i)-avg_recvLength)*(sendSizes.get(i)-avg_sendLength);
		}

		double corr_coeff = 0;
		if(!tim)
			corr_coeff = v/((Math.sqrt(w))*(Math.sqrt(z)));
		else
			corr_coeff = x/((Math.sqrt(y))*(Math.sqrt(z)));

		System.out.println(corr_coeff);


		// Collections.sort(packetsSize);
		// double size = packetsSize.size();

		// for(int j=packetsSize.get(0), i=0;j<=67000;j+=1)
		// {
		// 	for(int k=i;;++k)
		// 	{
		// 		if(k==packetsSize.size() || packetsSize.get(k)>j)
		// 		{
		// 			i = k-1;
		// 			break;
		// 		}
		// 	}

		// 	out.println(j+" "+((i+1)/size));
		// }

		out.close();
	}

	public static void q6(boolean r)
	{
		//Inter arrival time of connections
		ArrayList<Double> diff = new ArrayList<>();
		ArrayList<Double> time = new ArrayList<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP") && Parser.data.get(i).get(6).contains("[SYN]"))
				time.add(Double.parseDouble(Parser.data.get(i).get(1)));
		}

		for(int i=0;i<time.size()-1;++i)
			diff.add(time.get(i+1)-time.get(i));

		Collections.sort(diff);

		double size = diff.size(), avg = 0;
		for(int i=0;i<diff.size();++i)
			avg+= diff.get(i);
		int i = 0;

		
		
		System.out.println(avg/size);
		System.out.println(diff.get(diff.size()/2)+"\n");

		if(r)
			for(int j=0;j<diff.size()-1;++j)
				out.println(diff.get(j));
		else
		{
			for(double j = diff.get(0); j<=diff.get((int)size-1);j+=0.5)
			{
				for(int k=i;;++k)
				{
					if(k==diff.size() || diff.get(k)>j)
					{
						i = k-1;
						break;
					}
				}

				out.println(j+" "+((i+1)/size));
			}
		}
		out.close();
	}

	public static void q7(boolean r)
	{
		//Inter arrival time of packets sent to servers
		HashSet<String> servers = new HashSet<>();
		ArrayList<Double> diff = new ArrayList<>();
		ArrayList<Double> time = new ArrayList<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP") && Parser.data.get(i).get(6).contains("[SYN]"))
			{
				servers.add(Parser.data.get(i).get(3));
				time.add(Double.parseDouble(Parser.data.get(i).get(1)));
			}
			else if(Parser.data.get(i).get(4).equals("TCP") && servers.contains(Parser.data.get(i).get(3)))
				time.add(Double.parseDouble(Parser.data.get(i).get(1)));	
		}

		for(int i=0;i<time.size()-1;++i)
			diff.add(time.get(i+1)-time.get(i));

		Collections.sort(diff);

		double size = diff.size(), avg = 0;
		for(int i=0;i<diff.size();++i)
			avg+= diff.get(i);
		int i = 0;

		
		
		System.out.println(avg/size);
		System.out.println(diff.get(diff.size()/2)+"\n");

		if(r)
			for(int j=0;j<diff.size();j+=15)
				out.println(diff.get(j));
		else
		{
			for(double j = diff.get(0); j<=diff.get((int)size-1);j+=0.1)
			{
				for(int k=i;;++k)
				{
					if(k==diff.size() || diff.get(k)>j)
					{
						i = k-1;
						break;
					}
				}

				out.println(j+" "+((i+1)/size));
			}	
		}
		
		out.close();
	}

	public static double q8(boolean send)
	{
		ArrayList<TCPConnection> connections = new ArrayList<>();
		ArrayList<Integer> sendSizes = new ArrayList<>();
		ArrayList<Integer> recvSizes = new ArrayList<>();

		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP"))
			{
				String client = Parser.data.get(i).get(2);
				String server = Parser.data.get(i).get(3);
				String s = Parser.data.get(i).get(6);
				String p1 = s.substring(0,s.indexOf('>')).trim();
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('[')).trim();
				int cp = Integer.parseInt(p1);
				int sp = Integer.parseInt(p2);
				int len = Integer.parseInt(Parser.data.get(i).get(5).trim());
					
				if(Parser.data.get(i).get(6).contains("[SYN]"))
				{
					connections.add(new TCPConnection(0, server, client, sp, cp, len, 0));
					sendSizes.add(len);
				}

				else if(Parser.data.get(i).get(6).contains("[FIN, ACK]") || Parser.data.get(i).get(6).contains("[RST]"))
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							if(connections.get(j).isServer(server, sp))
								sendSizes.add(len);
							else
								recvSizes.add(len);
							connections.remove(j);
							break;
						}
					}
				}

				else
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							if(connections.get(j).isServer(server, sp))
								sendSizes.add(len);
							else
								recvSizes.add(len);
							break;
						}
					}
				}
			}
		}

		Collections.sort(sendSizes);
		Collections.sort(recvSizes);

		double sendSize = sendSizes.size();
		double recvSize = recvSizes.size();

		double avg_length = 0;

		for(int i=0;i<sendSizes.size();++i)
		{
			avg_length += sendSizes.get(i);
		}

		for(int i=0;i<recvSizes.size();++i)
		{
			avg_length += recvSizes.get(i);
		}

		avg_length = avg_length/(sendSize+recvSize);

		// System.out.println("Avg length of packet: " + avg_length);


		if(send)
		{
			int i = 0;
			for(int j = 1; j<=sendSizes.get((int)sendSize-1);j+=1)
			{
				if(i<0)
					i=0;
				for(int k=i;;++k)
				{
					if(k==sendSizes.size() || sendSizes.get(k)>j)
					{
						i = k-1;
						break;
					}
				}

				out.println(j+" "+((i+1)/sendSize));
			}
			out.close();
			return avg_length;
		}

		else
		{
			int i = 0;
			for(int j = 1; j<=recvSizes.get((int)recvSize-1);j+=1)
			{
				if(i<0)
					i=0;
				for(int k=i;;++k)
				{
					if(k==recvSizes.size() || recvSizes.get(k)>j)
					{
						i = k-1;
						break;
					}
				}

				out.println(j+" "+((i+1)/recvSize));
			}
			out.close();
		}
		return 0;
	}

	public static void q9(boolean seqBool)
	{
		ArrayList<TCPConnection> connections = new ArrayList<>();
		ArrayList<Long> seq = null, ack = null;
		ArrayList<Double> seqTime = null, ackTime = null;
		long max = 0;
		for(int i=0;i<Parser.data.size();++i)
		{
			if(Parser.data.get(i).get(4).equals("TCP"))
			{
				double t = Double.parseDouble(Parser.data.get(i).get(1));
				String client = Parser.data.get(i).get(2);
				String server = Parser.data.get(i).get(3);
				String s = Parser.data.get(i).get(6);
				String p1 = s.substring(0,s.indexOf('>')).trim();
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('[')).trim();
				int cp = Integer.parseInt(p1);
				int sp = Integer.parseInt(p2);
				int len = Integer.parseInt(Parser.data.get(i).get(5).trim());
					
				if(Parser.data.get(i).get(6).contains("[SYN]"))
					connections.add(new TCPConnection(t, server, client, sp, cp, len, 0));
				
				else if(Parser.data.get(i).get(6).contains("[FIN, ACK]") || Parser.data.get(i).get(6).contains("[RST]"))
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							if(connections.get(j).getSize()>max)
							{
								seq = connections.get(j).getSeq();
								ack = connections.get(j).getAck();
								seqTime = connections.get(j).getSeqTime();
								ackTime = connections.get(j).getAckTime();
								max = connections.get(j).getSize();
							}
							connections.remove(j);
							break;
						}
					}
				}

				else
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isConn(client, cp, server, sp))
						{
							if(connections.get(j).isServer(server, sp))
							{
								connections.get(j).sendSize += len;
								long num = Parser.getNumber(s, false);
								if(num!=-1)
									connections.get(j).addAck(num, t);
							}
							else
							{
								connections.get(j).recvSize += len;
								long num = Parser.getNumber(s, true);
								if(num!=-1)
									connections.get(j).addSeq(num, t);
							}
						}
					}	
				}
			}
		}
			
		int size = seqBool ? seq.size() : ack.size();
		System.out.println(size);
		for(int i=0;i<size;++i)
		{
			if(seqBool)
				out.println(seqTime.get(i)+" "+seq.get(i));
			else
				out.println(ackTime.get(i)+" "+ack.get(i));
		}

		out.close();
	}

	public static void q11(boolean q)
	{
		double m = 128000/(56.345 * 8);

		if(q)
			for(double rho = 0;rho<1;rho+=0.001)
				out.println(rho+" "+(rho/(1-rho)));
		else
			for(double rho = 0;rho<1;rho+=0.001)
				out.println(rho+" "+(rho/(m*(1-rho))));

		out.close();
	}
}