import java.io.*;
import java.util.*;

class Parser
{
	public static ArrayList<ArrayList<String>> data = new ArrayList<>();

	public static void read(String arg)
	{
		data.clear();
		Scanner sc = null;
		try
		{
			sc = new Scanner(new File(arg));
		}catch(Exception e){}
		sc.nextLine();
		
		while(sc.hasNextLine())
		{
			String line = sc.nextLine();
			int k = 1;
			ArrayList<String> columns = new ArrayList<>();
			while(k<line.length())
			{
				int index = line.indexOf('"',k);
				String temp = line.substring(k, index).trim();
				columns.add(temp);
				k = index+3;
			}
			data.add(columns);
		}		
	}

	public static long getNumber(String s, boolean seq)
	{
		if((seq&&s.indexOf("Seq")==-1)||(!seq&&s.indexOf("Ack")==-1)) return -1;

		if(seq)
			s = s.substring(s.indexOf("Seq")+4);
		else
			s = s.substring(s.indexOf("Ack")+4);

		Scanner sc = new Scanner(s);
		return sc.nextLong();
	}
}