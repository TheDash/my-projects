import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class DrewProblem {
	
	
	public static void main(String args[])
	{
		Long start = System.currentTimeMillis();
		class FileLog 
		{
			File file;
			Date d;
			String data = "";
			
			FileLog(File f, Date d)
			{
				this.file = f;
				this.d = d;
			}
			
			File getFile()
			{
				return file;
			}
			
			Date getDate()
			{
				return d;
			}
			
			void addToData(String d)
			{
				data = data + "\n" +d;
			}
		}
		final Comparator<FileLog> dateComparator = new Comparator<FileLog>() 
				{
				@Override
				public int compare(FileLog arg0, FileLog arg1) {
					Date d1 = arg0.getDate();
					Date d2 = arg1.getDate();
					
					return d1.compareTo(d2);
					
					
				}
				};
		final String HOMEDIR = "/home/marco/DrewProblem/";
		PriorityQueue<FileLog> orderedFiles = new PriorityQueue<FileLog>(1000000, dateComparator);
		
		File homeDir = new File(HOMEDIR);
		System.out.println(homeDir);
		File[] systems = homeDir.listFiles();
		if (systems != null)
		{
		for (File system : systems)
		{
			File[] logs = system.listFiles();
			for (File log : logs)
			{
				try {
					BufferedReader reader = new BufferedReader(new FileReader(log));
					
					String line = null;
					while ((line = reader.readLine()) != null)
					{
						System.out.println(line);
						if (line.startsWith("**** BEG"))
						{
							FileLog fLog = new FileLog(log, getDate(line));
							fLog.addToData(line);
							while ((line = reader.readLine()) != null && !line.startsWith("**** END") );
							{
								System.out.println(line);
								fLog.addToData(line);
							}
							fLog.addToData(line);
							orderedFiles.add(fLog);
							
						}
					}
					
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		}
		
		// Priority QUeue time!
		
		for (FileLog orderedFile : orderedFiles)
		{
			try {
					BufferedReader br = new BufferedReader(new FileReader(orderedFile.getFile()));
					File newFile = new File(HOMEDIR+orderedFile.getFile().getName()+".txt");
					PrintWriter pz = new PrintWriter(newFile);
					String line = null;
					while ((line = br.readLine()) != null)
					{
						pz.append(line);
						pz.println();
					}
					
					br.close();
					pz.close();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		Long end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end-start) +"ms");
	}
	
	static Date getDate(String line)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
		return sdf.parse(line, new ParsePosition(22));
	}
}
