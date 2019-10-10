import java.util.*;
class Questions
{
	public static void main(String[] args)
	{
		runAll("lbnl.anon-ftp.03-01-11.csv");
		runAll("lbnl.anon-ftp.03-01-14.csv");
		runAll("lbnl.anon-ftp.03-01-18.csv");
	}

	public static void runAll(String arg)
	{
		Parser.read(arg);
		// q1();
		// q2();
		q3();
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

		System.out.println("No. of Servers = "+servers.size());
		System.out.println("No. of Clients = "+clients.size()+"\n");
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

		System.out.println("No. of unique TCP flows = "+set.size()+"\n");
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
				String p1 = s.substring(0,s.indexOf('>'));
				String p2 = s.substring(s.indexOf('>')+1, s.indexOf('['));
				p1.trim();p2.trim();

				res+= (p1+" "+p2);
				if(set.add(res))
					count[hr]++;
			}
		}

		for(int i=0;i<24;++i)
			System.out.println("hr "+i+" to "+(i+1)+" = "+count[i]);
		System.out.println();
	}
}