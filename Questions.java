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

			out = new PrintWriter(new File(arg+"_q5"));
			q5();
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
			out.println("hr "+i+" to "+(i+1)+" = "+count[i]);
		out.println();
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
					connections.add(new TCPConnection(t, server, client, sp, cp, 0));
				
				else if(Parser.data.get(i).get(6).contains("[FIN, ACK]") || Parser.data.get(i).get(6).contains("[RST]"))
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isIP(client, server) && connections.get(j).isPort(cp, sp))
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

	public static void q5()
	{
		ArrayList<TCPConnection> connections = new ArrayList<>();
		ArrayList<Integer> packetsSize = new ArrayList<>();
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
					connections.add(new TCPConnection(t, server, client, sp, cp, len));

				else if(Parser.data.get(i).get(6).contains("[FIN, ACK]") || Parser.data.get(i).get(6).contains("[RST]"))
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isIP(client, server) && connections.get(j).isPort(cp, sp))
						{
							time.add(t - connections.get(j).getTime());
							packetsSize.add(connections.get(j).size+len);
							connections.remove(j);
							break;
						}
					}
				}

				else
				{
					for(int j=0;j<connections.size();++j)
					{
						if(connections.get(j).isIP(client, server) && connections.get(j).isPort(cp, sp))
							connections.get(j).size += len;
					}
				}
			}
		}

		double avg_time = 0;
		double avg_length = 0;

		for(int i=0;i<time.size();++i)
		{
			avg_time += time.get(i);
			avg_length += (double)packetsSize.get(i);

			if(time.get(i)<1000 && packetsSize.get(i)<40000)
				out.println(time.get(i)+" "+packetsSize.get(i));
		}

		avg_time = avg_time/time.size();
		avg_length = avg_length/time.size();

		double x = 0, y = 0, z = 0;

		for(int i=0;i<time.size();++i)
		{
			x += (time.get(i)-avg_time)*(packetsSize.get(i)-avg_length);
			y += (time.get(i)-avg_time)*(time.get(i)-avg_time);
			z += (packetsSize.get(i)-avg_length)*(packetsSize.get(i)-avg_length);
		}

		double corr_coeff = x/((Math.sqrt(y))*(Math.sqrt(z)));

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
}